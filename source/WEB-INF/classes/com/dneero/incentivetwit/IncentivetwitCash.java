package com.dneero.incentivetwit;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.money.TwitaskMoneyStatus;
import com.dneero.util.Num;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 1:39:06 PM
 */
public class IncentivetwitCash implements Incentivetwit {

    public static int ID = 1;
    public static String name = "Cash Incentive";
    private Twitaskincentive twitaskincentive;
    public static String WILLINGTOPAYPERTWIT="willingtopaypertwit";

    public IncentivetwitCash(Twitaskincentive twitaskincentive){
        this.twitaskincentive = twitaskincentive;
    }

    public int getID() {
        return ID;
    }

    public String getSystemName() {
        return name;
    }

    public double getResearcherCostPerResponse() {
        double out = 0.0;
        String val =  IncentivetwitOptionsUtil.getValue(twitaskincentive, WILLINGTOPAYPERTWIT);
        if (val!=null && !val.equals("") && Num.isdouble(val)){
            out = Double.parseDouble(val);
        }
        return out;
    }

    public double getBloggerEarningsPerResponse() {
        double out = 0.0;
        String val =  IncentivetwitOptionsUtil.getValue(twitaskincentive, WILLINGTOPAYPERTWIT);
        if (val!=null && !val.equals("") && Num.isdouble(val)){
            out = Double.parseDouble(val);
        }
        return out;
    }

    public double getEstimatedMaxValueOfEarnings(){
        double out = 0.0;
        String val =  IncentivetwitOptionsUtil.getValue(twitaskincentive, WILLINGTOPAYPERTWIT);
        if (val!=null && !val.equals("") && Num.isdouble(val)){
            out = Double.parseDouble(val);
        }
        return out;
    }

    public void doAwardIncentive(Twitanswer twitanswer) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
        User user = User.get(twitanswer.getUserid());
        Blogger blogger = Blogger.get(user.getBloggerid());
        //Affect balance for blogger
        MoveMoneyInAccountBalance.pay(user, getBloggerEarningsPerResponse(), "Pay for Twitter Answer: '"+twitask.getQuestion()+"'", true, twitanswer.getIsforcharity(), twitanswer.getCharityname(), 0, false, true, false, false);
        //Affect balance for researcher
        MoveMoneyInAccountBalance.charge(User.get(twitask.getUserid()), (TwitaskMoneyStatus.calculateAmtToChargeResearcher(getResearcherCostPerResponse(), twitask)), "User "+user.getNickname()+" responds to '"+twitask.getQuestion()+"'", true, false, false, false);
        //Affect balance for reseller
        if (twitask.getResellercode()!=null && !twitask.getResellercode().equals("")){
            //Find the user with this resellercode
            List<User> userResellers = HibernateUtil.getSession().createCriteria(User.class)
                                               .add(Restrictions.eq("resellercode", twitask.getResellercode()))
                                               .setCacheable(true)
                                               .setMaxResults(1)
                                               .list();
            if (userResellers!=null && userResellers.size()>0){
                User userReseller = userResellers.get(0);
                if (userReseller!=null){
                    //Pay them the correct amount... remember, their pay is based on the amount paid to the bloggers
                    double amtToPayReseller = SurveyMoneyStatus.calculateResellerAmt(getBloggerEarningsPerResponse(), userReseller);
                    if (amtToPayReseller>0){
                        MoveMoneyInAccountBalance.pay(userReseller, amtToPayReseller, "Reseller pay for response to '"+ Str.truncateString(twitask.getQuestion(), 20)+"'", false, false, "", 0, false, false, false, true);
                    }
                }
            }
        }
        //Create Incentiveaward
        Incentivetwitaward ia = new Incentivetwitaward();
        ia.setDate(new Date());
        ia.setIsvalid(true);
        ia.setTwitanswerid(twitanswer.getTwitanswerid());
        ia.setTwitaskincentiveid(twitaskincentive.getTwitaskincentiveid());
        ia.setUserid(user.getUserid());
        ia.setMisc1("");
        ia.setMisc2("");
        ia.setMisc3("");
        ia.setMisc4("");
        ia.setMisc5("");
        try{ia.save();}catch(Exception ex){logger.error("", ex);}
        //Notify the recipient
        Pl pl = Pl.get(user.getPlid());
        //Create the args array to hold the dynamic stuff
        String[] args = new String[10];
        args[0] = "$"+Str.formatForMoney(getBloggerEarningsPerResponse());
        args[1] = twitask.getQuestion();
        //Send the email
        EmailTemplateProcessor.sendMail(pl.getNameforui()+" Cash Award for "+user.getNickname(), "incentiveaward-cash", user, args);
    }

    public void doRemoveIncentive(Twitanswer twitanswer) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
        User user = User.get(twitanswer.getUserid());
        Blogger blogger = Blogger.get(user.getBloggerid());
        //Affect balance for blogger
        MoveMoneyInAccountBalance.charge(user, getBloggerEarningsPerResponse(), "Charge for award to: '"+twitask.getQuestion()+"' being removed", false, true, false, false);
        //Affect balance for researcher
        MoveMoneyInAccountBalance.pay(User.get(twitask.getUserid()), (TwitaskMoneyStatus.calculateAmtToChargeResearcher(getResearcherCostPerResponse(), twitask)), "User "+user.getNickname()+" responds to '"+twitask.getQuestion()+"' had award removed", false, false, "", true, false, false, false);
        //Affect balance for reseller
        if (twitask.getResellercode()!=null && !twitask.getResellercode().equals("")){
            //Find the user with this resellercode
            List<User> userResellers = HibernateUtil.getSession().createCriteria(User.class)
                                               .add(Restrictions.eq("resellercode", twitask.getResellercode()))
                                               .setCacheable(true)
                                               .setMaxResults(1)
                                               .list();
            if (userResellers!=null && userResellers.size()>0){
                User userReseller = userResellers.get(0);
                if (userReseller!=null){
                    //Pay them the correct amount... remember, their pay is based on the amount paid to the bloggers
                    double amtToPayReseller = SurveyMoneyStatus.calculateResellerAmt(getBloggerEarningsPerResponse(), userReseller);
                    if (amtToPayReseller>0){
                        MoveMoneyInAccountBalance.charge(userReseller, amtToPayReseller, "Reseller charge for response to '"+ Str.truncateString(twitask.getQuestion(), 20)+"' having award removed", false, false, false, true);
                    }
                }
            }
        }
        //Update the Incentiveaward
        List<Incentivetwitaward> ias = HibernateUtil.getSession().createCriteria(Incentivetwitaward.class)
                .add(Restrictions.eq("userid", user.getUserid()))
                .add(Restrictions.eq("twitanswerid", twitanswer.getTwitanswerid()))
                .add(Restrictions.eq("twitaskincentiveid", twitaskincentive.getTwitaskincentiveid()))
                .setCacheable(true)
                .list();
        for (Iterator<Incentivetwitaward> iasIt=ias.iterator(); iasIt.hasNext();) {
            Incentivetwitaward incentiveaward=iasIt.next();
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

    public String getFullSummaryHtml() {
        StringBuffer out = new StringBuffer();
        out.append(getFullSummary());
        return out.toString();
    }

    public String getInstructions() {
        StringBuffer out = new StringBuffer();
        out.append("Upon successful joining of the conversation and posting to your social space (blog or social network) we'll credit your account balance with the earnings amount.");
        return out.toString();
    }

    public Twitaskincentive getTwitaskincentive() {
        return this.twitaskincentive;
    }

    public void doImmediatelyAfterResponse(Twitanswer twitanswer) {

    }

    public String getInstructionsAfterResponse(Twitanswer twitanswer) {
        return "";
    }

    public String getInstructionsAfterAward(Twitanswer twitanswer) {
        return "Your account balance has been affected by $"+Str.formatForMoney(getBloggerEarningsPerResponse())+".  Whenever your balance goes above $20 we'll automatically pay your PayPal account within 24-48 hours.";
    }
}