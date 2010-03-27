package com.dneero.htmluibeans;


import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.HibernateUtilImpressions;
import com.dneero.email.EmailActivationSend;
import com.dneero.email.LostPasswordSend;
import com.dneero.helpers.DeleteUser;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.scheduledjobs.CurrentBalanceUpdater;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.util.DateDiff;
import com.dneero.util.Num;
import com.dneero.util.Str;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class CustomercareUserDetailExtended implements Serializable {


    private int userid;
    private String name;
    private String email;
    private List balances;
    private List transactions;
    private ArrayList<BloggerCompletedsurveysListitem> responses;
    private ArrayList<BloggerCompletedsurveysListitem> recentresponses;
    private List<Impression> impressions;
    private User user;
    private Researcher researcher;
    private boolean onlyshowsuccessfultransactions = false;
    private boolean onlyshownegativeamountbalance = false;



    public CustomercareUserDetailExtended(){

    }



    public void initBean(){
        User user = null;
        if (Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            user = User.get(Integer.parseInt(Pagez.getRequest().getParameter("userid")));
        }
        if (user!=null && user.getUserid()>0){
            this.userid = user.getUserid();
            this.user = user;
            name = user.getName();
            email = user.getEmail();

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
                    //int totalimpressions = UserImpressionFinder.getTotalImpressions(Blogger.get(user.getBloggerid()), survey);
                    //int paidandtobepaidimpressions = UserImpressionFinder.getPaidAndToBePaidImpressions(Blogger.get(user.getBloggerid()), survey);
                    int totalimpressions = response.getImpressionstotal();
                    int paidandtobepaidimpressions = response.getImpressionspaid() + response.getImpressionstobepaid();
                    BloggerCompletedsurveysListitem listitem = new BloggerCompletedsurveysListitem();
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
            impressions = HibernateUtilImpressions.getSession().createQuery("from Impression where userid='"+userid+"' order by impressionid desc").setMaxResults(500).list();


        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}