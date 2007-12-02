package com.dneero.money;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:52:57 PM
 */
public class CurrentBalanceCalculator implements Serializable {

    private double currentbalance = 0;
    private double pendingearnings = 0;
    private User user;

    public CurrentBalanceCalculator(User user){
        this.user = user;
        if (user!=null){
            loadCurrentbalance();
            loadPendingearnings();
        }
    }

    public void loadCurrentbalance(){
        if (user!=null){
            currentbalance = 0;
            currentbalance = NumFromUniqueResult.getDouble("select sum(amt) from Balance where userid='"+user.getUserid()+"'");
        }
    }

    public void loadPendingearnings(){
        pendingearnings = 0;
        if (user!=null){
            if (user.getBloggerid()>0){
                //List those surveys that are ispaid=false and are not too late to post
                List responses = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+user.getBloggerid()+"' and poststatus<"+Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED+" and ispaid=false").list();
                for (Iterator iterator = responses.iterator(); iterator.hasNext();) {
                    Response response = (Response) iterator.next();
                    if (!response.getIspaid()){
                        Survey survey = Survey.get(response.getSurveyid());
                        pendingearnings = pendingearnings + survey.getWillingtopayperrespondent();
                    }
                }
            }
        }
    }


    public double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(double currentbalance) {
        this.currentbalance = currentbalance;
    }

    public double getPendingearnings() {
        return pendingearnings;
    }

    public void setPendingearnings(double pendingearnings) {
        this.pendingearnings = pendingearnings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
