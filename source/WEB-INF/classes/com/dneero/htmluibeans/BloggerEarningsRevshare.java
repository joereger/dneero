package com.dneero.htmluibeans;

import com.dneero.util.SortableList;
import com.dneero.util.Str;

import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.dao.Revshare;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.Balance;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.RevshareLevelPercentageCalculator;
import com.dneero.helpers.NicknameHelper;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:12:35 PM
 */
public class BloggerEarningsRevshare implements Serializable {

    private ArrayList<BloggerEarningsRevshareListRevshares> list;
    private List balances;

    private double level1percent=RevshareLevelPercentageCalculator.getPercentToShare(1);
    private double level1amt=RevshareLevelPercentageCalculator.getAmountToShare(500, 1);
    private double level2percent=RevshareLevelPercentageCalculator.getPercentToShare(2);
    private double level2amt=RevshareLevelPercentageCalculator.getAmountToShare(500, 2);
    private double level3percent=RevshareLevelPercentageCalculator.getPercentToShare(3);
    private double level3amt=RevshareLevelPercentageCalculator.getAmountToShare(500, 3);
    private double level4percent=RevshareLevelPercentageCalculator.getPercentToShare(4);
    private double level4amt=RevshareLevelPercentageCalculator.getAmountToShare(500, 4);
    private double level5percent=RevshareLevelPercentageCalculator.getPercentToShare(5);
    private double level5amt=RevshareLevelPercentageCalculator.getAmountToShare(500, 5);

    private String msg = "";

    public BloggerEarningsRevshare(){

    }


    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            list = new ArrayList();
            List revshares = HibernateUtil.getSession().createQuery("FROM Revshare WHERE targetbloggerid='"+userSession.getUser().getBloggerid()+"'").list();
            for (Iterator iterator = revshares.iterator(); iterator.hasNext();) {
                Revshare revshare = (Revshare) iterator.next();
                Blogger sourceblogger = Blogger.get(revshare.getSourcebloggerid());
                User sourceuser = User.get(sourceblogger.getUserid());
                BloggerEarningsRevshareListRevshares listitem = new BloggerEarningsRevshareListRevshares();
                listitem.setAmt(revshare.getAmt());
                listitem.setUsername(sourceuser.getNickname());
                list.add(listitem);
            }

            List bals = HibernateUtil.getSession().createQuery("from Balance where userid='"+userSession.getUser().getUserid()+"' and isreferralmoney=true order by balanceid desc").setCacheable(true).list();
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
        }

        //Create the params for the revshare chart
//        level1percent = RevshareLevelPercentageCalculator.getPercentToShare(1);
//        level1amt = RevshareLevelPercentageCalculator.getAmountToShare(500, 1);
//        level2percent = RevshareLevelPercentageCalculator.getPercentToShare(2);
//        level2amt = RevshareLevelPercentageCalculator.getAmountToShare(500, 2);
//        level3percent = RevshareLevelPercentageCalculator.getPercentToShare(3);
//        level3amt = RevshareLevelPercentageCalculator.getAmountToShare(500, 3);
//        level4percent = RevshareLevelPercentageCalculator.getPercentToShare(4);
//        level4amt = RevshareLevelPercentageCalculator.getAmountToShare(500, 4);
//        level5percent = RevshareLevelPercentageCalculator.getPercentToShare(5);
//        level5amt = RevshareLevelPercentageCalculator.getAmountToShare(500, 5);
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerEarningsRevshareListRevshares obj1 = (BloggerEarningsRevshareListRevshares)o1;
                BloggerEarningsRevshareListRevshares obj2 = (BloggerEarningsRevshareListRevshares)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("username")) {
                    return ascending ? obj1.getUsername().compareTo(obj2.getUsername()) : obj2.getUsername().compareTo(obj1.getUsername());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (list != null && !list.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(list, comparator);
        }
    }

    public ArrayList<BloggerEarningsRevshareListRevshares> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerEarningsRevshareListRevshares> list) {
        this.list = list;
    }

    public double getLevel1percent() {
        return level1percent;
    }

    public void setLevel1percent(double level1percent) {
        this.level1percent = level1percent;
    }

    public double getLevel1amt() {
        return level1amt;
    }

    public void setLevel1amt(double level1amt) {
        this.level1amt = level1amt;
    }

    public double getLevel2percent() {
        return level2percent;
    }

    public void setLevel2percent(double level2percent) {
        this.level2percent = level2percent;
    }

    public double getLevel2amt() {
        return level2amt;
    }

    public void setLevel2amt(double level2amt) {
        this.level2amt = level2amt;
    }

    public double getLevel3percent() {
        return level3percent;
    }

    public void setLevel3percent(double level3percent) {
        this.level3percent = level3percent;
    }

    public double getLevel3amt() {
        return level3amt;
    }

    public void setLevel3amt(double level3amt) {
        this.level3amt = level3amt;
    }

    public double getLevel4percent() {
        return level4percent;
    }

    public void setLevel4percent(double level4percent) {
        this.level4percent = level4percent;
    }

    public double getLevel4amt() {
        return level4amt;
    }

    public void setLevel4amt(double level4amt) {
        this.level4amt = level4amt;
    }

    public double getLevel5percent() {
        return level5percent;
    }

    public void setLevel5percent(double level5percent) {
        this.level5percent = level5percent;
    }

    public double getLevel5amt() {
        return level5amt;
    }

    public void setLevel5amt(double level5amt) {
        this.level5amt = level5amt;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getBalances() {
        return balances;
    }

    public void setBalances(List balances) {
        this.balances = balances;
    }
}
