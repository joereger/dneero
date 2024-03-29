package com.dneero.money;

import com.dneero.dao.Balance;
import com.dneero.dao.Charitydonation;
import com.dneero.dao.Revshare;
import com.dneero.dao.User;
import com.dneero.scheduledjobs.CharityCalculateAmountDonated;
import com.dneero.util.Str;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:41:48 PM
 */
public class MoveMoneyInAccountBalance {

    public static void pay(User user, double amt, String desc, boolean qualifiesforrevsharedistribution, boolean isforcharity, String charityname, boolean isresearchermoney, boolean isbloggermoney, boolean isreferralmoney, boolean isresellermoney){
        pay(user, amt, desc, qualifiesforrevsharedistribution, isforcharity, charityname, 0, isresearchermoney, isbloggermoney, isreferralmoney, isresellermoney);
    }

    public static void pay(User user, double amt, String desc, boolean qualifiesforrevsharedistribution, boolean isforcharity, String charityname, int optionalresponseid, boolean isresearchermoney, boolean isbloggermoney, boolean isreferralmoney, boolean isresellermoney){
        Logger logger = Logger.getLogger(MoveMoneyInAccountBalance.class);

        double originalAmt = amt;
        String originalDesc = desc;
        if (isforcharity){
            //Reset some vars
            qualifiesforrevsharedistribution = false;
            amt = 0;
            desc = "Charity Donation of $"+ Str.formatForMoney(originalAmt)+" to "+charityname+": " + desc;
        }

        Balance balance = new Balance();
        balance.setAmt(amt);
        balance.setDate(new Date());
        balance.setDescription(desc);
        CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
        balance.setCurrentbalance(cbc.getCurrentbalance() + amt);
        balance.setUserid(user.getUserid());
        balance.setOptionalresponseid(optionalresponseid);
        balance.setIsbloggermoney(isbloggermoney);
        balance.setIsresearchermoney(isresearchermoney);
        balance.setIsreferralmoney(isreferralmoney);
        balance.setIsresellermoney(isresellermoney);
        try{balance.save();}catch (Exception ex){logger.error("",ex);}


        if (isforcharity){
            //Record to db
            Charitydonation charitydonation = new Charitydonation();
            charitydonation.setUserid(user.getUserid());
            charitydonation.setDate(new Date());
            charitydonation.setDescription(originalDesc);
            charitydonation.setAmt(originalAmt);
            charitydonation.setCharityname(charityname);
            charitydonation.setBalanceid(balance.getBalanceid());
            try{charitydonation.save();}catch (Exception ex){logger.error("",ex);}
            try{CharityCalculateAmountDonated.processUser(user);}catch (Exception ex){logger.error("",ex);}
        }

        //Give out a revshare if this payment requires it
        if (qualifiesforrevsharedistribution){
            //Start with the userToPayRevshareTo who's getting paid
            //Start with the current user and then move down their tree
            User userToPayRevshareTo = user;
            int sourceBloggerid = 0;
            if (user!=null && user.getBloggerid()>0){
                sourceBloggerid = user.getBloggerid();
            }
            double amtRevsharebasedon = amt;
            for(int levelsup=1; levelsup<=5; levelsup++){
                if (userToPayRevshareTo.getReferredbyuserid()>0){
                    //Switch one level up
                    userToPayRevshareTo = User.get(userToPayRevshareTo.getReferredbyuserid());
                    if (userToPayRevshareTo.getBloggerid()>0){
                        //Only pay if they're qualified for the revshare program
                        if (userToPayRevshareTo.getIsqualifiedforrevshare()){
                            //Calculate the revshare
                            double amttoshare = RevshareLevelPercentageCalculator.getAmountToShare(amtRevsharebasedon, levelsup);
                            //Store the revshare in the database
                            Revshare revshare = new Revshare();
                            revshare.setSourcebloggerid(sourceBloggerid);
                            revshare.setTargetbloggerid(userToPayRevshareTo.getBloggerid());
                            revshare.setAmt(amttoshare);
                            revshare.setDate(new Date());
                            try{revshare.save();} catch (Exception ex){logger.error("",ex);}
                            //Transfer the actual revshare
                            //Very important: note that qualifiesforrevsharedistribution=false on revshare distributions
                            MoveMoneyInAccountBalance.pay(userToPayRevshareTo, amttoshare, "Referral share from "+userToPayRevshareTo.getNickname()+" ("+userToPayRevshareTo.getEmail()+")", false, false, "", isresearchermoney, isbloggermoney, true, false);
                            //@todo Email the recipient of the revshare to tell them that their peeps are making them money!
                        }
                    }
                }
            }
        }
    }



    public static void charge(User user, double amt, String desc, boolean isresearchermoney, boolean isbloggermoney, boolean isreferralmoney, boolean isresellermoney){
        Logger logger = Logger.getLogger(MoveMoneyInAccountBalance.class);
        Balance balance = new Balance();
        balance.setAmt((-1)*amt);
        balance.setDate(new Date());
        balance.setDescription(desc);
        CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
        balance.setCurrentbalance(cbc.getCurrentbalance() - amt);
        balance.setUserid(user.getUserid());
        balance.setIsbloggermoney(isbloggermoney);
        balance.setIsresearchermoney(isresearchermoney);
        balance.setIsreferralmoney(isreferralmoney);
        balance.setIsresellermoney(isresellermoney);
        try{balance.save();}catch (Exception ex){logger.error("",ex);}
    }



}
