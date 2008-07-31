package com.dneero.money;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;

import java.util.Calendar;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 9:37:33 AM
 */
public class SurveyMoneyStatus implements Serializable {

    public static double PERSURVEYCREATIONFEE = 5.00;
    public static double DEFAULTDNEEROMARKUPPERCENT = 25;
    public static int DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS = 30;
    public static double HIDESURVEYFEEPERCENT = 5;
    public static double RESELLERPERCENTDEFAULT = 5;

    private double maxPossiblePayoutForResponses = 0;
    private double maxPossiblePayoutForImpressions = 0;
    private double maxPossiblePayoutToUsers = 0;
    private double maxPossibledNeeroFee = 0;
    private double maxPossibleSpend = 0;
    private int responsesToDate = 0;
    private double spentOnResponsesToDate = 0;
    private double spentOnResponsesToDateIncludingdNeeroFee = 0;
    private int impressionsToDate = 0;
    private double spentOnImpressionsToDate = 0;
    private double spentOnImpressionsToDateIncludingdNeeroFee = 0;
    private double spentToDate = 0;
    private double spentToDateIncludingdNeeroFee = 0;
    private double remainingPossibleSpend = 0;
    private double hidesurveyfee = 0;
    private double dneeromarkuppercent = DEFAULTDNEEROMARKUPPERCENT;
    private double couponDiscountAmt = 0.0;


    public SurveyMoneyStatus(Survey survey){
        //Note that coupon is applied below each calculation into the same variable
        maxPossiblePayoutForResponses = (survey.getIncentive().getBloggerEarningsPerResponse() * survey.getNumberofrespondentsrequested());
        maxPossiblePayoutForImpressions = ((survey.getWillingtopaypercpm()*survey.getMaxdisplaystotal())/1000);
        maxPossiblePayoutToUsers = maxPossiblePayoutForResponses + maxPossiblePayoutForImpressions;
        couponDiscountAmt = calculateCouponAmt(maxPossiblePayoutToUsers, survey);
        maxPossibledNeeroFee = calculatedNeeroUpcharge(maxPossiblePayoutToUsers, survey);
        if(survey.getIsresultshidden()){
            hidesurveyfee = maxPossiblePayoutToUsers * (HIDESURVEYFEEPERCENT/100);
        }
        maxPossibleSpend = maxPossiblePayoutToUsers + maxPossibledNeeroFee + hidesurveyfee + PERSURVEYCREATIONFEE - couponDiscountAmt;
        responsesToDate = survey.getResponses().size();
        spentOnResponsesToDate = survey.getIncentive().getBloggerEarningsPerResponse() * responsesToDate;
        spentOnResponsesToDateIncludingdNeeroFee = calculateAmtToChargeResearcher(spentOnResponsesToDate, survey);
        int impressionspaid  = NumFromUniqueResult.getInt("select sum(impressionspaid) from Impression where surveyid='"+survey.getSurveyid()+"'");
        int impressionstobepaid  = NumFromUniqueResult.getInt("select sum(impressionstobepaid) from Impression where surveyid='"+survey.getSurveyid()+"'");
        impressionsToDate = impressionspaid + impressionstobepaid;
        spentOnImpressionsToDate = (Double.parseDouble(String.valueOf(impressionsToDate)) * survey.getWillingtopaypercpm())/1000;
        spentOnImpressionsToDateIncludingdNeeroFee = calculateAmtToChargeResearcher(spentOnImpressionsToDate, survey);
        spentToDate = spentOnResponsesToDate + spentOnImpressionsToDate;
        spentToDateIncludingdNeeroFee = calculateAmtToChargeResearcher(spentToDate, survey) + PERSURVEYCREATIONFEE + hidesurveyfee;

        //When calculating remainingPossibleSpend I must take survey status into account
        if (survey.getStatus()!=Survey.STATUS_CLOSED){
            //Survey is not closed
            remainingPossibleSpend = maxPossibleSpend - spentToDateIncludingdNeeroFee;
        } else {
            //Survey is closed
            int dayssinceclose = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(survey.getEnddate()));
            if (dayssinceclose>DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS){
                //Over two months have passed, can not accrue more spend
                remainingPossibleSpend = 0;
            } else {
                //Under two months have passed, could still accrue more spend
                double uncollectedSurveyRevenue = maxPossiblePayoutForResponses - spentOnResponsesToDate;
                remainingPossibleSpend = (maxPossibleSpend - uncollectedSurveyRevenue) - spentToDateIncludingdNeeroFee;
            }
        }
    }

    //Calculates total amt to charge including upcharge and coupons
    public static double calculateAmtToChargeResearcher(double amt, Survey survey){
        double couponamt = calculateCouponAmt(amt, survey);
        double upcharge = calculatedNeeroUpcharge(amt, survey);
        double amttocharge = amt + upcharge - couponamt;
        return amttocharge;
    }

    //Calculates upcharge
    private static double calculatedNeeroUpcharge(double amt, Survey survey){
        double upcharge = amt*(survey.getDneeromarkuppercent()/100);
        return upcharge;
    }

    //Calculates coupon discount amount
    private static double calculateCouponAmt(double amt, Survey survey){
        double couponamt = 0.0;
        List<Couponredemption> couponredemptions = HibernateUtil.getSession().createCriteria(Couponredemption.class)
                                           .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                           .setCacheable(true)
                                           .list();
        if (couponredemptions!=null && couponredemptions.size()>0){
            //Only apply the coupon with the highest discount... what can I say... we're kind.
            Coupon coupon = null;
            for (Iterator<Couponredemption> iterator = couponredemptions.iterator(); iterator.hasNext();) {
                Couponredemption couponredemption = iterator.next();
                Coupon tmpcoupon = Coupon.get(couponredemption.getCouponid());
                //Check start date and end date of coupon against survey launch date
                if (survey.getStartdate().after(tmpcoupon.getStartdate()) && survey.getStartdate().before(tmpcoupon.getEnddate())){
                    if (coupon==null || tmpcoupon.getDiscountpercent()>coupon.getDiscountpercent()){
                        coupon = tmpcoupon;
                    }
                }
            }
            //If we have a coupon, apply it
            if (coupon!=null){
                couponamt = amt*(coupon.getDiscountpercent()/100);
            }
        }
        return couponamt;
    }

    //Calculates reseller amt
    public static double calculateResellerAmt(double amt, User user){
        double resellerpercent = RESELLERPERCENTDEFAULT;
        if (user.getResellerpercent()>0){
            resellerpercent = user.getResellerpercent();
        }
        double reselleramt = amt*(resellerpercent/100);
        return reselleramt;
    }


    public double getDneeromarkuppercent() {
        return dneeromarkuppercent;
    }

    public double getMaxPossiblePayoutForResponses() {
        return maxPossiblePayoutForResponses;
    }

    public double getMaxPossiblePayoutForImpressions() {
        return maxPossiblePayoutForImpressions;
    }

    public double getMaxPossiblePayoutToUsers() {
        return maxPossiblePayoutToUsers;
    }

    public double getMaxPossibledNeeroFee() {
        return maxPossibledNeeroFee;
    }

    public double getMaxPossibleSpend() {
        return maxPossibleSpend;
    }

    public int getResponsesToDate() {
        return responsesToDate;
    }

    public double getSpentOnResponsesToDate() {
        return spentOnResponsesToDate;
    }

    public int getImpressionsToDate() {
        return impressionsToDate;
    }

    public double getSpentOnImpressionsToDate() {
        return spentOnImpressionsToDate;
    }

    public double getSpentToDate() {
        return spentToDate;
    }

    public double getRemainingPossibleSpend() {
        return remainingPossibleSpend;
    }


    public double getSpentOnResponsesToDateIncludingdNeeroFee() {
        return spentOnResponsesToDateIncludingdNeeroFee;
    }

    public void setSpentOnResponsesToDateIncludingdNeeroFee(double spentOnResponsesToDateIncludingdNeeroFee) {
        this.spentOnResponsesToDateIncludingdNeeroFee = spentOnResponsesToDateIncludingdNeeroFee;
    }

    public double getSpentOnImpressionsToDateIncludingdNeeroFee() {
        return spentOnImpressionsToDateIncludingdNeeroFee;
    }

    public void setSpentOnImpressionsToDateIncludingdNeeroFee(double spentOnImpressionsToDateIncludingdNeeroFee) {
        this.spentOnImpressionsToDateIncludingdNeeroFee = spentOnImpressionsToDateIncludingdNeeroFee;
    }

    public double getSpentToDateIncludingdNeeroFee() {
        return spentToDateIncludingdNeeroFee;
    }

    public void setSpentToDateIncludingdNeeroFee(double spentToDateIncludingdNeeroFee) {
        this.spentToDateIncludingdNeeroFee = spentToDateIncludingdNeeroFee;
    }

    public double getHidesurveyfee() {
        return hidesurveyfee;
    }

    public void setHidesurveyfee(double hidesurveyfee) {
        this.hidesurveyfee = hidesurveyfee;
    }

    public double getCouponDiscountAmt() {
        return couponDiscountAmt;
    }

    public void setCouponDiscountAmt(double couponDiscountAmt) {
        this.couponDiscountAmt = couponDiscountAmt;
    }
}
