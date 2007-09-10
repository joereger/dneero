package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.dao.Responsepending;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 11:18:03 AM
 */
public class AccountIndex implements Serializable {

    private String currentbalance = "$0.00";
    private boolean userhasresponsependings = false;
    private String init;
    private String msg = "";
    private boolean isfirsttimelogin = false;
    private ArrayList<BloggerCompletedsurveysListitem> completedsurveys;

    public AccountIndex(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        load();
        if (userhasresponsependings){
            try{Jsf.redirectResponse("/blogger/index.jsf");}catch(Exception ex){logger.error(ex);}
        }
    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //load();
        if (userhasresponsependings){
//            BloggerIndex bean = (BloggerIndex)Jsf.getManagedBean("bloggerIndex");
//            return bean.beginView();
            try{Jsf.redirectResponse("/blogger/index.jsf");}catch(Exception ex){logger.error(ex);}
        }
        return "accountindex";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(Jsf.getUserSession().getUser()!=null){
            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(Jsf.getUserSession().getUser());
            currentbalance = "$"+Str.formatForMoney(cbc.getCurrentbalance());
            List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                                   .add(Restrictions.eq("userid", Jsf.getUserSession().getUser().getUserid()))
                                   .setCacheable(true)
                                   .list();
            if (responsependings.size()>0){
                userhasresponsependings = true;
            }
            //Load completed surveys
            if (Jsf.getUserSession().getUser().getBloggerid()>0){
                //Limit to last 10 surveys or only surveys in last 10 days
                BloggerCompletedsurveys bcs = new BloggerCompletedsurveys();
                bcs.beginView();
                ArrayList<BloggerCompletedsurveysListitem> bcsl = bcs.getList();
                completedsurveys = new ArrayList<BloggerCompletedsurveysListitem>();
                for (int i = 0; i < bcsl.size(); i++) {
                    BloggerCompletedsurveysListitem bloggerCompletedsurveysListitem = bcsl.get(i);
                    int dayssinceresponse = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(bloggerCompletedsurveysListitem.getResponse().getResponsedate()));
                    logger.debug("dayssinceresponse="+dayssinceresponse);
                    if (dayssinceresponse<=UpdateResponsePoststatus.MAXPOSTINGPERIODINDAYS){
                        completedsurveys.add(bloggerCompletedsurveysListitem);
                    }
                }            
            }
            //Load account balance
            AccountBalance bean = (AccountBalance)Jsf.getManagedBean("accountBalance");
            bean.beginView();
        }
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (init!=null && init.equals("doinit")){
            logger.debug("init = doinit so calling load()");
            load();
        } else {
            logger.debug("init null or not doinit");
        }
    }


    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }


    public boolean isUserhasresponsependings() {
        return userhasresponsependings;
    }

    public void setUserhasresponsependings(boolean userhasresponsependings) {
        this.userhasresponsependings = userhasresponsependings;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getIsfirsttimelogin() {
        return isfirsttimelogin;
    }

    public void setIsfirsttimelogin(boolean isfirsttimelogin) {
        this.isfirsttimelogin = isfirsttimelogin;
    }

    public ArrayList<BloggerCompletedsurveysListitem> getCompletedsurveys() {
        return completedsurveys;
    }

    public void setCompletedsurveys(ArrayList<BloggerCompletedsurveysListitem> completedsurveys) {
        this.completedsurveys = completedsurveys;
    }
}
