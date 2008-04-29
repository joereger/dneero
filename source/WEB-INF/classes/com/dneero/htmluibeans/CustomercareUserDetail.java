package com.dneero.htmluibeans;


import com.dneero.util.Str;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.util.Num;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailActivationSend;
import com.dneero.email.LostPasswordSend;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.UserImpressionFinder;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.helpers.DeleteUser;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class CustomercareUserDetail implements Serializable {

    private int userid;
    private String firstname;
    private String lastname;
    private String email;
    private String paypaladdress="";
    private int referredbyuserid=0;
    private String facebookuid="";
    private boolean issysadmin = false;
    private boolean iscustomercare = false;
    private String activitypin;
    private double amt;
    private String reason;
    private int fundstype=1;
    private List balances;
    private List transactions;
    private ArrayList<BloggerCompletedsurveysListitem> responses;
    private ArrayList<BloggerCompletedsurveysListitem> recentresponses;
    private List<Impression> impressions;
    private boolean isenabled = true;
    private User user;
    private Researcher researcher;
    private boolean onlyshowsuccessfultransactions = false;
    private boolean onlyshownegativeamountbalance = false;
    private double resellerpercent;


    public CustomercareUserDetail(){

    }



    public void initBean(){
        User user = null;
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            user = User.get(Integer.parseInt(Pagez.getRequest().getParameter("userid")));
        }
        if (user!=null && user.getUserid()>0){
            this.userid = user.getUserid();
            this.user = user;
            firstname = user.getFirstname();
            lastname = user.getLastname();
            email = user.getEmail();
            referredbyuserid = user.getReferredbyuserid();
            paypaladdress = user.getPaymethodpaypaladdress();
            isenabled = user.getIsenabled();
            issysadmin = false;
            facebookuid = String.valueOf(user.getFacebookuserid());
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                    issysadmin = true;
                }
                if (userrole.getRoleid()== Userrole.CUSTOMERCARE){
                    iscustomercare = true;
                }
            }
            if (user.getResearcherid()>0){
                researcher = Researcher.get(user.getResearcherid());
            }

            //Load balance info
            String negamtSql = "";
            if (onlyshownegativeamountbalance){
                negamtSql = " and amt<0 ";
            }
            List bals = HibernateUtil.getSession().createQuery("from Balance where userid='"+userid+"' "+negamtSql+" order by balanceid desc").setMaxResults(200).list();
            balances = new ArrayList<AccountBalanceListItem>();
            for (Iterator iterator = bals.iterator(); iterator.hasNext();) {
                Balance balance = (Balance) iterator.next();
                AccountBalanceListItem abli = new AccountBalanceListItem();
                abli.setAmt("$"+ Str.formatForMoney(balance.getAmt()));
                abli.setBalanceid(balance.getBalanceid());
                abli.setCurrentbalance("$"+ Str.formatForMoney(balance.getCurrentbalance()));
                abli.setDate(balance.getDate());
                abli.setDescription(balance.getDescription());
                abli.setUserid(balance.getUserid());
                StringBuffer fundstype = new StringBuffer();
                if (balance.getIsbloggermoney()){
                    fundstype.append("Blogger");
                }
                if (balance.getIsresearchermoney()){
                    fundstype.append("Researcher");
                }
                if (balance.getIsreferralmoney()){
                    fundstype.append("Referral");
                }
                if (balance.getIsresellermoney()){
                    fundstype.append("Reseller");
                }
                abli.setFundstype(fundstype.toString());
                balances.add(abli);
            }

            //Load transaction info
            String issuccessfulSql = "";
            if (onlyshowsuccessfultransactions){
                issuccessfulSql = " and issuccessful=true ";
            }
            List trans = HibernateUtil.getSession().createQuery("from Balancetransaction where userid='"+userid+"' "+issuccessfulSql+" order by balancetransactionid desc").setMaxResults(50).list();
            transactions = new ArrayList<AccountBalancetransactionListItem>();
            for (Iterator iterator = trans.iterator(); iterator.hasNext();) {
                Balancetransaction transaction = (Balancetransaction) iterator.next();
                AccountBalancetransactionListItem abli = new AccountBalancetransactionListItem();
                abli.setAmt("$"+ Str.formatForMoney(transaction.getAmt()));
                abli.setBalancetransactionid(transaction.getBalancetransactionid());
                abli.setDate(transaction.getDate());
                abli.setDescription(transaction.getDescription());
                abli.setNotes(transaction.getNotes());
                abli.setUserid(transaction.getUserid());
                abli.setIssuccessful(transaction.getIssuccessful());
                transactions.add(abli);
            }

            responses = new ArrayList();
            recentresponses = new ArrayList();
            if (user.getBloggerid()>0){
                //HibernateUtil.getSession().saveOrUpdate(Blogger.get(userSession.getUser().getBloggerid()));
                List<Response> responses = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+user.getBloggerid()+"' order by responseid desc").setMaxResults(50).setCacheable(false).list();
                for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                    Response response = iterator.next();
                    Survey survey = Survey.get(response.getSurveyid());
                    int totalimpressions = UserImpressionFinder.getTotalImpressions(Blogger.get(user.getBloggerid()), survey);
                    int paidandtobepaidimpressions = UserImpressionFinder.getPaidAndToBePaidImpressions(Blogger.get(user.getBloggerid()), survey);
                    BloggerCompletedsurveysListitem listitem = new BloggerCompletedsurveysListitem();
                    listitem.setAmtforresponse("$"+Str.formatForMoney(survey.getWillingtopayperrespondent()));
                    listitem.setAmttotal("$"+Str.formatForMoney(survey.getWillingtopayperrespondent() + ((paidandtobepaidimpressions*survey.getWillingtopaypercpm()/1000))));
                    listitem.setTotalimpressions(totalimpressions);
                    listitem.setPaidandtobepaidimpressions(paidandtobepaidimpressions);
                    listitem.setResponsedate(response.getResponsedate());
                    listitem.setResponseid(response.getResponseid());
                    listitem.setSurveyid(survey.getSurveyid());
                    listitem.setSurveytitle(survey.getTitle());
                    listitem.setResponse(response);
                    this.responses.add(listitem);
                    int dayssinceresponse = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(listitem.getResponse().getResponsedate()));
                    //Keep it listed for five days after it's paid
                    if (dayssinceresponse<=(UpdateResponsePoststatus.MAXPOSTINGPERIODINDAYS+5)){
                        recentresponses.add(listitem);
                    }
                }
            }

            //Load impressions
            impressions = HibernateUtil.getSession().createQuery("from Impression where userid='"+userid+"' order by impressionid desc").setMaxResults(500).list();


        }
    }

    public void save() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("save() called");
        logger.debug("userid="+userid);
        logger.debug("firstname="+firstname);
        logger.debug("lastname="+lastname);
        logger.debug("email="+email);
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setEmail(email);
            user.setReferredbyuserid(referredbyuserid);
            user.setPaymethodpaypaladdress(paypaladdress);
            if (Num.isinteger(facebookuid)){
                user.setFacebookuserid(Integer.parseInt(facebookuid));
            }
            try{user.save();}catch (Exception ex){logger.error("",ex);}
        }

    }


    public String sendresetpasswordemail() throws ValidationException {
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            LostPasswordSend.sendLostPasswordEmail(user);
        }
        Pagez.getUserSession().setMessage("Password reset email sent");
        return "sysadminuserdetail";
    }

    public String reactivatebyemail() throws ValidationException {
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            EmailActivationSend.sendActivationEmail(user);
        }
        Pagez.getUserSession().setMessage("Reactivation email sent");
        return "sysadminuserdetail";
    }

    public String toggleisenabled() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user.getIsenabled()){
            //Disable
            user.setIsenabled(false);
            isenabled = false;
            Pagez.getUserSession().setMessage("Account disabled.");
        } else {
            // Enable
            user.setIsenabled(true);
            isenabled = true;
            Pagez.getUserSession().setMessage("Account enabled.");
        }
        try{user.save();}catch (Exception ex){logger.error("",ex);}
        return "sysadminuserdetail";
    }

    public String togglesysadminprivs() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("togglesysadminprivs()");
        if (activitypin.equals("yes, i want to do this")){
            activitypin = "";
            User user = User.get(userid);
            if (user!=null && user.getUserid()>0){
                issysadmin = false;
                for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                        issysadmin = true;
                    }
                }
                if (issysadmin){
                    logger.debug("is a sysadmin");
                    //@todo revoke sysadmin privs doesn't work
                    //int userroleidtodelete=0;
                    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                        Userrole userrole = iterator.next();
                        logger.debug("found roleid="+userrole.getRoleid());
                        if (userrole.getRoleid()==Userrole.SYSTEMADMIN){
                            logger.debug("removing it from iterator");
                            iterator.remove();
                        }
                    }
                    try{user.save();} catch (Exception ex){logger.error("",ex);}
                    issysadmin = false;
                    Pagez.getUserSession().setMessage("User is no longer a sysadmin");
                } else {
                    Userrole role = new Userrole();
                    role.setUserid(user.getUserid());
                    role.setRoleid(Userrole.SYSTEMADMIN);
                    user.getUserroles().add(role);
                    try{role.save();} catch (Exception ex){logger.error("",ex);}
                    issysadmin = true;
                    Pagez.getUserSession().setMessage("User is now a sysadmin");
                }
                initBean();
            }
        } else {
            Pagez.getUserSession().setMessage("Activity Pin Not Correct.");
        }
        return "sysadminuserdetail";
    }

    public String togglecustomercareprivs() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("togglecustomercareprivs()");
        if (activitypin.equals("yes, i want to do this")){
            activitypin = "";
            User user = User.get(userid);
            if (user!=null && user.getUserid()>0){
                iscustomercare = false;
                for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()== Userrole.CUSTOMERCARE){
                        iscustomercare = true;
                    }
                }
                if (iscustomercare){
                    logger.debug("is a sysadmin");
                    //@todo revoke customercare privs doesn't work
                    //int userroleidtodelete=0;
                    for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                        Userrole userrole = iterator.next();
                        logger.debug("found roleid="+userrole.getRoleid());
                        if (userrole.getRoleid()==Userrole.CUSTOMERCARE){
                            logger.debug("removing it from iterator");
                            iterator.remove();
                        }
                    }
                    try{user.save();} catch (Exception ex){logger.error("",ex);}
                    iscustomercare = false;
                    Pagez.getUserSession().setMessage("User is no longer a customer care rep");
                } else {
                    Userrole role = new Userrole();
                    role.setUserid(user.getUserid());
                    role.setRoleid(Userrole.CUSTOMERCARE);
                    user.getUserroles().add(role);
                    try{role.save();} catch (Exception ex){logger.error("",ex);}
                    iscustomercare = true;
                    Pagez.getUserSession().setMessage("User is now a customer care rep");
                }
                initBean();
            }
        } else {
            Pagez.getUserSession().setMessage("Activity Pin Not Correct.");
        }
        return "sysadminuserdetail";
    }

    public void deleteuser() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("deleteuser()");
        if (activitypin.equals("yes, i want to do this")){
            activitypin = "";
            User user = User.get(userid);
            DeleteUser.delete(user);
        } else {
            Pagez.getUserSession().setMessage("Activity Pin Not Correct.");
        }

    }

    public String runResearcherRemainingBalanceOperations() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            if (user.getResearcherid()>0){
                try{ResearcherRemainingBalanceOperations task = new ResearcherRemainingBalanceOperations();
                task.setResearcherid(user.getResearcherid());
                task.execute(null);} catch (Exception ex){logger.error("",ex);}
                Pagez.getUserSession().setMessage("Task run.");
            }
        }
        return "sysadminuserdetail";
    }

    public String giveusermoney() throws ValidationException {
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            //Specify funds type
            boolean isbloggermoney = false;
            boolean isresearchermoney = false;
            boolean isreferralmoney = false;
            boolean isresellermoney = false;
            if (fundstype==1){
                isbloggermoney = true;
                isresearchermoney = false;
                isreferralmoney = false;
                isresellermoney = false;
            } else if (fundstype==2){
                isbloggermoney = false;
                isresearchermoney = true;
                isreferralmoney = false;
                isresellermoney = false;
            } else if (fundstype==3){
                isbloggermoney = false;
                isresearchermoney = false;
                isreferralmoney = true;
                isresellermoney = false;
            } else if (fundstype==4){
                isbloggermoney = false;
                isresearchermoney = false;
                isreferralmoney = false;
                isresellermoney = true;
            }
            //Move it
            MoveMoneyInAccountBalance.pay(user, amt, "Manual transaction: "+reason, false, false, "", isresearchermoney, isbloggermoney, isreferralmoney, isresellermoney);
        }
        initBean();
        Pagez.getUserSession().setMessage("$" + Str.formatForMoney(amt) + " given to user account balance.");
        return "sysadminuserdetail";
    }

    public String takeusermoney() throws ValidationException {
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            //Specify funds type
            boolean isbloggermoney = false;
            boolean isresearchermoney = false;
            boolean isreferralmoney = false;
            boolean isresellermoney = false;
            if (fundstype==1){
                isbloggermoney = true;
                isresearchermoney = false;
                isreferralmoney = false;
                isresellermoney = false;
            } else if (fundstype==2){
                isbloggermoney = false;
                isresearchermoney = true;
                isreferralmoney = false;
                isresellermoney = false;
            } else if (fundstype==3){
                isbloggermoney = false;
                isresearchermoney = false;
                isreferralmoney = true;
                isresellermoney = false;
            } else if (fundstype==4){
                isbloggermoney = false;
                isresearchermoney = false;
                isreferralmoney = false;
                isresellermoney = true;
            }
            //Move it
            MoveMoneyInAccountBalance.charge(user, amt, "Manual transaction: "+reason, isresearchermoney, isbloggermoney, isreferralmoney, isresellermoney);
        }
        initBean();
        Pagez.getUserSession().setMessage("$"+ Str.formatForMoney(amt)+" taken from user account balance");
        return "sysadminuserdetail";
    }

    public String updateresellerpercent() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            user.setResellerpercent(resellerpercent);
            try{user.save();} catch (Exception ex){logger.error("",ex);}
        }
        initBean();
        Pagez.getUserSession().setMessage("Reseller Percent updated.");
        return "sysadminuserdetail";
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public String getActivitypin() {
        return activitypin;
    }

    public void setActivitypin(String activitypin) {
        this.activitypin = activitypin;
    }

    public boolean getIssysadmin() {
        return issysadmin;
    }

    public void setIssysadmin(boolean issysadmin) {
        this.issysadmin = issysadmin;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }


    public List getBalances() {
        return balances;
    }

    public void setBalances(List balances) {
        this.balances = balances;
    }

    public List getTransactions() {
        return transactions;
    }

    public void setTransactions(List transactions) {
        this.transactions = transactions;
    }


    public boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }

    public ArrayList<BloggerCompletedsurveysListitem> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<BloggerCompletedsurveysListitem> responses) {
        this.responses = responses;
    }

    public ArrayList<BloggerCompletedsurveysListitem> getRecentresponses() {
        return recentresponses;
    }

    public void setRecentresponses(ArrayList<BloggerCompletedsurveysListitem> recentresponses) {
        this.recentresponses = recentresponses;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher=researcher;
    }

    public String getPaypaladdress() {
        return paypaladdress;
    }

    public void setPaypaladdress(String paypaladdress) {
        this.paypaladdress=paypaladdress;
    }

    public int getReferredbyuserid() {
        return referredbyuserid;
    }

    public void setReferredbyuserid(int referredbyuserid) {
        this.referredbyuserid=referredbyuserid;
    }

    public List<Impression> getImpressions() {
        return impressions;
    }

    public void setImpressions(List<Impression> impressions) {
        this.impressions=impressions;
    }

    public boolean getOnlyshowsuccessfultransactions() {
        return onlyshowsuccessfultransactions;
    }

    public void setOnlyshowsuccessfultransactions(boolean onlyshowsuccessfultransactions) {
        this.onlyshowsuccessfultransactions=onlyshowsuccessfultransactions;
    }

    public boolean getOnlyshownegativeamountbalance() {
        return onlyshownegativeamountbalance;
    }

    public void setOnlyshownegativeamountbalance(boolean onlyshownegativeamountbalance) {
        this.onlyshownegativeamountbalance=onlyshownegativeamountbalance;
    }

    public String getFacebookuid() {
        return facebookuid;
    }

    public void setFacebookuid(String facebookuid) {
        this.facebookuid=facebookuid;
    }

    public double getResellerpercent() {
        return resellerpercent;
    }

    public void setResellerpercent(double resellerpercent) {
        this.resellerpercent = resellerpercent;
    }

    public int getFundstype() {
        return fundstype;
    }

    public void setFundstype(int fundstype) {
        this.fundstype = fundstype;
    }

    public boolean getIscustomercare() {
        return iscustomercare;
    }

    public void setIscustomercare(boolean iscustomercare) {
        this.iscustomercare=iscustomercare;
    }
}
