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
public class TwitaskMoneyStatus implements Serializable {

    public static double PERTWITASKCREATIONFEE= 5.00;
    public static double DEFAULTDNEEROMARKUPPERCENT = 25;
    public static double RESELLERPERCENTDEFAULT = 5;

    private double maxPossiblePayoutForResponses = 0;
    private double maxPossiblePayoutToUsers = 0;
    private double maxPossibledNeeroFee = 0;
    private double maxPossibleSpend = 0;
    private int responsesToDate = 0;
    private double spentOnResponsesToDate = 0;
    private double spentOnResponsesToDateIncludingdNeeroFee = 0;
    private double spentToDate = 0;
    private double spentToDateIncludingdNeeroFee = 0;
    private double remainingPossibleSpend = 0;
    private double dneeromarkuppercent = DEFAULTDNEEROMARKUPPERCENT;
    private double couponDiscountAmt = 0.0;


    public TwitaskMoneyStatus(Twitask twitask){
        //Note that coupon is applied below each calculation into the same variable
        maxPossiblePayoutForResponses = (twitask.getIncentive().getResearcherCostPerResponse() * twitask.getNumberofrespondentsrequested());
        maxPossiblePayoutToUsers = maxPossiblePayoutForResponses;
        couponDiscountAmt = calculateCouponAmt(maxPossiblePayoutToUsers, twitask);
        maxPossibledNeeroFee = calculatedNeeroUpcharge(maxPossiblePayoutToUsers, twitask);
        maxPossibleSpend = maxPossiblePayoutToUsers + maxPossibledNeeroFee + PERTWITASKCREATIONFEE - couponDiscountAmt;
        responsesToDate = twitask.getTwitanswers().size();
        //@todo At some point this should take into account any Responses that have a different Surveyincentive attached to them
        spentOnResponsesToDate = twitask.getIncentive().getResearcherCostPerResponse() * responsesToDate;
        spentOnResponsesToDateIncludingdNeeroFee = calculateAmtToChargeResearcher(spentOnResponsesToDate, twitask);
        spentToDate = spentOnResponsesToDate;
        spentToDateIncludingdNeeroFee = calculateAmtToChargeResearcher(spentToDate, twitask) + PERTWITASKCREATIONFEE;

        //When calculating remainingPossibleSpend I must take survey status into account
        if (twitask.getStatus()!=Twitask.STATUS_CLOSED){
            //Survey is not closed
            remainingPossibleSpend = maxPossibleSpend - spentToDateIncludingdNeeroFee;
        } else {
            remainingPossibleSpend = 0;
        }
    }

    //Calculates total amt to charge including upcharge and coupons
    public static double calculateAmtToChargeResearcher(double amt, Twitask twitask){
        double couponamt = calculateCouponAmt(amt, twitask);
        double upcharge = calculatedNeeroUpcharge(amt, twitask);
        double amttocharge = amt + upcharge - couponamt;
        return amttocharge;
    }

    //Calculates upcharge
    private static double calculatedNeeroUpcharge(double amt, Twitask twitask){
        double upcharge = amt*(twitask.getDneeromarkuppercent()/100);
        return upcharge;
    }

    //Calculates coupon discount amount
    private static double calculateCouponAmt(double amt, Twitask twitask){
        double couponamt = 0.0;
//        List<Couponredemption> couponredemptions = HibernateUtil.getSession().createCriteria(Couponredemption.class)
//                                           .add(Restrictions.eq("surveyid", survey.getSurveyid()))
//                                           .setCacheable(true)
//                                           .list();
//        if (couponredemptions!=null && couponredemptions.size()>0){
//            //Only apply the coupon with the highest discount... what can I say... we're kind.
//            Coupon coupon = null;
//            for (Iterator<Couponredemption> iterator = couponredemptions.iterator(); iterator.hasNext();) {
//                Couponredemption couponredemption = iterator.next();
//                Coupon tmpcoupon = Coupon.get(couponredemption.getCouponid());
//                //Check start date and end date of coupon against survey launch date
//                if (survey.getStartdate().after(tmpcoupon.getStartdate()) && survey.getStartdate().before(tmpcoupon.getEnddate())){
//                    if (coupon==null || tmpcoupon.getDiscountpercent()>coupon.getDiscountpercent()){
//                        coupon = tmpcoupon;
//                    }
//                }
//            }
//            //If we have a coupon, apply it
//            if (coupon!=null){
//                couponamt = amt*(coupon.getDiscountpercent()/100);
//            }
//        }
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

    public double getMaxPossiblePayoutForResponses() {
        return maxPossiblePayoutForResponses;
    }

    public void setMaxPossiblePayoutForResponses(double maxPossiblePayoutForResponses) {
        this.maxPossiblePayoutForResponses=maxPossiblePayoutForResponses;
    }

    public double getMaxPossiblePayoutToUsers() {
        return maxPossiblePayoutToUsers;
    }

    public void setMaxPossiblePayoutToUsers(double maxPossiblePayoutToUsers) {
        this.maxPossiblePayoutToUsers=maxPossiblePayoutToUsers;
    }

    public double getMaxPossibledNeeroFee() {
        return maxPossibledNeeroFee;
    }

    public void setMaxPossibledNeeroFee(double maxPossibledNeeroFee) {
        this.maxPossibledNeeroFee=maxPossibledNeeroFee;
    }

    public double getMaxPossibleSpend() {
        return maxPossibleSpend;
    }

    public void setMaxPossibleSpend(double maxPossibleSpend) {
        this.maxPossibleSpend=maxPossibleSpend;
    }

    public int getResponsesToDate() {
        return responsesToDate;
    }

    public void setResponsesToDate(int responsesToDate) {
        this.responsesToDate=responsesToDate;
    }

    public double getSpentOnResponsesToDate() {
        return spentOnResponsesToDate;
    }

    public void setSpentOnResponsesToDate(double spentOnResponsesToDate) {
        this.spentOnResponsesToDate=spentOnResponsesToDate;
    }

    public double getSpentOnResponsesToDateIncludingdNeeroFee() {
        return spentOnResponsesToDateIncludingdNeeroFee;
    }

    public void setSpentOnResponsesToDateIncludingdNeeroFee(double spentOnResponsesToDateIncludingdNeeroFee) {
        this.spentOnResponsesToDateIncludingdNeeroFee=spentOnResponsesToDateIncludingdNeeroFee;
    }

    public double getSpentToDate() {
        return spentToDate;
    }

    public void setSpentToDate(double spentToDate) {
        this.spentToDate=spentToDate;
    }

    public double getSpentToDateIncludingdNeeroFee() {
        return spentToDateIncludingdNeeroFee;
    }

    public void setSpentToDateIncludingdNeeroFee(double spentToDateIncludingdNeeroFee) {
        this.spentToDateIncludingdNeeroFee=spentToDateIncludingdNeeroFee;
    }

    public double getRemainingPossibleSpend() {
        return remainingPossibleSpend;
    }

    public void setRemainingPossibleSpend(double remainingPossibleSpend) {
        this.remainingPossibleSpend=remainingPossibleSpend;
    }

    public double getDneeromarkuppercent() {
        return dneeromarkuppercent;
    }

    public void setDneeromarkuppercent(double dneeromarkuppercent) {
        this.dneeromarkuppercent=dneeromarkuppercent;
    }

    public double getCouponDiscountAmt() {
        return couponDiscountAmt;
    }

    public void setCouponDiscountAmt(double couponDiscountAmt) {
        this.couponDiscountAmt=couponDiscountAmt;
    }
}