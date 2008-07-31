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
public class PendingBalanceCalculator implements Serializable {

    private double pendingearnings = 0;
    private User user;

    public PendingBalanceCalculator(User user){
        this.user = user;
        if (user!=null){
            loadPendingearnings();
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
                        pendingearnings = pendingearnings + survey.getIncentive().getBloggerEarningsPerResponse();
                    }
                }
            }
        }
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