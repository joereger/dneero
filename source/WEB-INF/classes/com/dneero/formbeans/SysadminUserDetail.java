package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailActivationSend;
import com.dneero.email.LostPasswordSend;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.UserImpressionFinder;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminUserDetail implements Serializable {

    private int userid;
    private String firstname;
    private String lastname;
    private String email;
    private boolean issysadmin = false;
    private String activitypin;
    private double amt;
    private String reason;
    private List balances;
    private List transactions;
    private ArrayList<BloggerCompletedsurveysListitem> responses;
    private ArrayList<BloggerCompletedsurveysListitem> recentresponses;
    private boolean isenabled = true;
    private User user;
    private Researcher researcher;


    public SysadminUserDetail(){
        beginView();
    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("beginView called:");
        String tmpUserid = Jsf.getRequestParam("userid");
        if (com.dneero.util.Num.isinteger(tmpUserid)){
            logger.debug("beginView called: found userid in request param="+tmpUserid);
            load(Integer.parseInt(tmpUserid));
        }
        return "sysadminuserdetail";
    }

    private void load(int userid){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            this.userid = user.getUserid();
            this.user = user;
            firstname = user.getFirstname();
            lastname = user.getLastname();
            email = user.getEmail();
            isenabled = user.getIsenabled();
            issysadmin = false;
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                    issysadmin = true;
                }
            }
            if (user.getResearcherid()>0){
                researcher = Researcher.get(user.getResearcherid());
            }

            //Load balance info
            List bals = HibernateUtil.getSession().createQuery("from Balance where userid='"+userid+"' order by balanceid desc").setMaxResults(50).list();
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
                balances.add(abli);
            }

            //Load transaction info
            List trans = HibernateUtil.getSession().createQuery("from Balancetransaction where userid='"+userid+"' order by balancetransactionid desc").setMaxResults(50).list();
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


        }
    }

    public String save(){
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
            try{user.save();}catch (Exception ex){logger.error(ex);}
        }
        Jsf.setFacesMessage("User details saved");
        return "sysadminuserdetail";
    }


    public String sendresetpasswordemail(){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            LostPasswordSend.sendLostPasswordEmail(user);
        }
        Jsf.setFacesMessage("Password reset email sent");
        return "sysadminuserdetail";
    }

    public String reactivatebyemail(){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            EmailActivationSend.sendActivationEmail(user);
        }
        Jsf.setFacesMessage("Reactivation email sent");
        return "sysadminuserdetail";
    }

    public String toggleisenabled(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user.getIsenabled()){
            //Disable
            user.setIsenabled(false);
            isenabled = false;
            Jsf.setFacesMessage("Account disabled.");
        } else {
            // Enable
            user.setIsenabled(true);
            isenabled = true;
            Jsf.setFacesMessage("Account enabled.");
        }
        try{user.save();}catch (Exception ex){logger.error(ex);}
        return "sysadminuserdetail";
    }

    public String togglesysadminprivs(){
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
                    try{user.save();} catch (Exception ex){logger.error(ex);}
                    issysadmin = false;
                    Jsf.setFacesMessage("User is no longer a sysadmin");
                } else {
                    Userrole role = new Userrole();
                    role.setUserid(user.getUserid());
                    role.setRoleid(Userrole.SYSTEMADMIN);
                    user.getUserroles().add(role);
                    try{role.save();} catch (Exception ex){logger.error(ex);}
                    issysadmin = true;
                    Jsf.setFacesMessage("User is now a sysadmin");
                }
                load(user.getUserid());
            }
        } else {
            Jsf.setFacesMessage("Activity Pin Not correct.");
        }
        return "sysadminuserdetail";
    }

    public String runResearcherRemainingBalanceOperations(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            if (user.getResearcherid()>0){
                try{ResearcherRemainingBalanceOperations task = new ResearcherRemainingBalanceOperations();
                task.setResearcherid(user.getResearcherid());
                task.execute(null);} catch (Exception ex){logger.error(ex);}
                Jsf.setFacesMessage("Task run.");
            }
        }
        return "sysadminuserdetail";
    }

    public String giveusermoney(){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            MoveMoneyInAccountBalance.pay(user, amt, "Manual transaction: "+reason, false, false, "");
        }
        load(user.getUserid());
        Jsf.setFacesMessage("$"+ Str.formatForMoney(amt)+" given to user account balance");
        return "sysadminuserdetail";
    }

    public String takeusermoney(){
        User user = User.get(userid);
        if (user!=null && user.getUserid()>0){
            MoveMoneyInAccountBalance.charge(user, amt, "Manual transaction: "+reason);
        }
        load(user.getUserid());
        Jsf.setFacesMessage("$"+ Str.formatForMoney(amt)+" taken from user account balance");
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
}
