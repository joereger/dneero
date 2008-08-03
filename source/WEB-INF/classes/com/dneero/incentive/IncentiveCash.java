package com.dneero.incentive;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.Num;
import com.dneero.util.RandomString;
import com.dneero.util.Str;
import com.dneero.email.EmailTemplateProcessor;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 1:39:06 PM
 */
public class IncentiveCash implements Incentive {

    public static int ID = 1;
    public static String name = "Cash Incentive";
    private Surveyincentive surveyincentive;
    public static String WILLINGTOPAYPERRESPONSE="willingtopayperresponse";

    public IncentiveCash(Surveyincentive surveyincentive){
        this.surveyincentive = surveyincentive;
    }

    public int getID() {
        return ID;
    }

    public String getSystemName() {
        return name;
    }

    public double getResearcherCostPerResponse() {
        double out = 0.0;
        String val =  IncentiveOptionsUtil.getValue(surveyincentive, WILLINGTOPAYPERRESPONSE);
        if (val!=null && !val.equals("") && Num.isdouble(val)){
            out = Double.parseDouble(val);
        }
        return out;
    }

    public double getBloggerEarningsPerResponse() {
        double out = 0.0;
        String val =  IncentiveOptionsUtil.getValue(surveyincentive, WILLINGTOPAYPERRESPONSE);
        if (val!=null && !val.equals("") && Num.isdouble(val)){
            out = Double.parseDouble(val);
        }
        return out;
    }

    public double getEstimatedMaxValueOfEarnings(){
        double out = 0.0;
        String val =  IncentiveOptionsUtil.getValue(surveyincentive, WILLINGTOPAYPERRESPONSE);
        if (val!=null && !val.equals("") && Num.isdouble(val)){
            out = Double.parseDouble(val);
        }
        return out;
    }

    public void doAwardIncentive(Response response) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Survey survey = Survey.get(response.getSurveyid());
        Blogger blogger = Blogger.get(response.getBloggerid());
        User user = User.get(blogger.getUserid());
        //Affect balance for blogger
        MoveMoneyInAccountBalance.pay(user, getBloggerEarningsPerResponse(), "Pay for responding to: '"+survey.getTitle()+"'", true, response.getIsforcharity(), response.getCharityname(), response.getResponseid(), false, true, false, false);
        //Affect balance for researcher
        MoveMoneyInAccountBalance.charge(User.get(Researcher.get(survey.getResearcherid()).getUserid()), (SurveyMoneyStatus.calculateAmtToChargeResearcher(getResearcherCostPerResponse(), survey)), "User "+user.getFirstname()+" "+user.getLastname()+" responds to '"+survey.getTitle()+"'", true, false, false, false);
        //Affect balance for reseller
        if (survey.getResellercode()!=null && !survey.getResellercode().equals("")){
            //Find the user with this resellercode
            List<User> userResellers = HibernateUtil.getSession().createCriteria(User.class)
                                               .add(Restrictions.eq("resellercode", survey.getResellercode()))
                                               .setCacheable(true)
                                               .setMaxResults(1)
                                               .list();
            if (userResellers!=null && userResellers.size()>0){
                User userReseller = userResellers.get(0);
                if (userReseller!=null){
                    //Pay them the correct amount... remember, their pay is based on the amount paid to the bloggers
                    double amtToPayReseller = SurveyMoneyStatus.calculateResellerAmt(getBloggerEarningsPerResponse(), userReseller);
                    if (amtToPayReseller>0){
                        MoveMoneyInAccountBalance.pay(userReseller, amtToPayReseller, "Reseller pay for response to '"+ Str.truncateString(survey.getTitle(), 20)+"'", false, false, "", 0, false, false, false, true);
                    }
                }
            }
        }
        //Create Incentiveaward
        Incentiveaward ia = new Incentiveaward();
        ia.setDate(new Date());
        ia.setIsvalid(true);
        ia.setResponseid(response.getResponseid());
        ia.setSurveyincentiveid(surveyincentive.getSurveyincentiveid());
        ia.setUserid(user.getUserid());
        ia.setMisc1("");
        ia.setMisc2("");
        ia.setMisc3("");
        ia.setMisc4("");
        ia.setMisc5("");
        try{ia.save();}catch(Exception ex){logger.error("", ex);}
        //Notify the recipient
        //Create the args array to hold the dynamic stuff
        String[] args = new String[10];
        args[0] = "$"+Str.formatForMoney(getBloggerEarningsPerResponse());
        args[1] = survey.getTitle();
        //Send the email
        EmailTemplateProcessor.sendMail("dNeero Cash Award for "+user.getFirstname(), "incentiveaward-cash", user, args);
    }

    public void doRemoveIncentive(Response response) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Survey survey = Survey.get(response.getSurveyid());
        Blogger blogger = Blogger.get(response.getBloggerid());
        User user = User.get(blogger.getUserid());
        //Affect balance for blogger
        MoveMoneyInAccountBalance.charge(user, getBloggerEarningsPerResponse(), "Charge for award to: '"+survey.getTitle()+"' being removed", false, true, false, false);
        //Affect balance for researcher
        MoveMoneyInAccountBalance.pay(User.get(Researcher.get(survey.getResearcherid()).getUserid()), (SurveyMoneyStatus.calculateAmtToChargeResearcher(getResearcherCostPerResponse(), survey)), "User "+user.getFirstname()+" "+user.getLastname()+" responds to '"+survey.getTitle()+"' had award removed", false, false, "", true, false, false, false);
        //Affect balance for reseller
        if (survey.getResellercode()!=null && !survey.getResellercode().equals("")){
            //Find the user with this resellercode
            List<User> userResellers = HibernateUtil.getSession().createCriteria(User.class)
                                               .add(Restrictions.eq("resellercode", survey.getResellercode()))
                                               .setCacheable(true)
                                               .setMaxResults(1)
                                               .list();
            if (userResellers!=null && userResellers.size()>0){
                User userReseller = userResellers.get(0);
                if (userReseller!=null){
                    //Pay them the correct amount... remember, their pay is based on the amount paid to the bloggers
                    double amtToPayReseller = SurveyMoneyStatus.calculateResellerAmt(getBloggerEarningsPerResponse(), userReseller);
                    if (amtToPayReseller>0){
                        MoveMoneyInAccountBalance.charge(userReseller, amtToPayReseller, "Reseller charge for response to '"+ Str.truncateString(survey.getTitle(), 20)+"' having award removed", false, false, false, true);
                    }
                }
            }
        }
        //Update the Incentiveaward
        List<Incentiveaward> ias = HibernateUtil.getSession().createCriteria(Incentiveaward.class)
                .add(Restrictions.eq("userid", user.getUserid()))
                .add(Restrictions.eq("responseid", response.getResponseid()))
                .add(Restrictions.eq("surveyincentiveid", surveyincentive.getSurveyincentiveid()))
                .setCacheable(true)
                .list();
        for (Iterator<Incentiveaward> iasIt=ias.iterator(); iasIt.hasNext();) {
            Incentiveaward incentiveaward=iasIt.next();
            incentiveaward.setIsvalid(false);
            try{incentiveaward.save();}catch(Exception ex){logger.error("", ex);}
        }
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        double bloggerEarningsPerResponse = getBloggerEarningsPerResponse();
        out.append("$"+Str.formatForMoney(bloggerEarningsPerResponse));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        double bloggerEarningsPerResponse = getBloggerEarningsPerResponse();
        out.append("$"+Str.formatForMoney(bloggerEarningsPerResponse));
        return out.toString();
    }

    public String getInstructions() {
        StringBuffer out = new StringBuffer();
        out.append("Upon successful joining of the conversation and posting to your social space (blog or social network) we'll credit your account balance with the earnings amount.");
        return out.toString();
    }

    public Surveyincentive getSurveyincentive() {
        return this.surveyincentive;  
    }

    public void doImmediatelyAfterResponse(Response response) {

    }

    public String getInstructionsAfterResponse(Response response) {
        return "";
    }

    public String getInstructionsAfterAward(Response response) {
        return "Your account balance has been affected by $"+Str.formatForMoney(getBloggerEarningsPerResponse())+".  Whenever your balance goes above $20 we'll automatically pay your PayPal account within 24-48 hours.";
    }
}
