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
        loadCurrentbalance();
        loadPendingearnings();
    }

    public void loadCurrentbalance(){
        currentbalance = 0;
        //@todo Replace with single sql call sum(amt)
        currentbalance = NumFromUniqueResult.getDouble("select sum(amt) from Balance where userid='"+user.getUserid()+"'");
//        List results = HibernateUtil.getSession().createQuery("from Balance where userid='"+user.getUserid()+"'").list();
//        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
//            Balance balance = (Balance) iterator.next();
//            currentbalance = currentbalance + balance.getAmt();
//        }
    }

    public void loadPendingearnings(){
        pendingearnings = 0;
        if (user.getBloggerid()>0){
            List responses = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+user.getBloggerid()+"' and ispaid=false").list();
            for (Iterator iterator = responses.iterator(); iterator.hasNext();) {
                Response response = (Response) iterator.next();
                if (!response.getIspaid()){
                    Survey survey = Survey.get(response.getSurveyid());
                    pendingearnings = pendingearnings + survey.getWillingtopayperrespondent();
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
