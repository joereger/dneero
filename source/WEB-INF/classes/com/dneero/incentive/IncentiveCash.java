package com.dneero.incentive;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.Num;
import com.dneero.util.Str;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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

    public String getName() {
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

    public void awardIncentive(Response response) {
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
    }

    public void undoIncentiveForResponse(Response response) {
        Survey survey = Survey.get(response.getSurveyid());
        Blogger blogger = Blogger.get(response.getBloggerid());
        User user = User.get(blogger.getUserid());
        //@todo implement undoIncentiveForResponse() for IncentiveCash
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
}
