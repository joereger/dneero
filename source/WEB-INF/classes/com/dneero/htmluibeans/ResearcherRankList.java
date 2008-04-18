package com.dneero.htmluibeans;

import com.dneero.dao.Rank;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherRankList implements Serializable {

    private String newrankname="";
    private int newrankid=0;
    private List<Rank> ranks;


    public ResearcherRankList(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ranks = HibernateUtil.getSession().createCriteria(Rank.class)
                .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                .addOrder(Order.desc("rankid"))
                .setCacheable(true)
                .list();
    }

    public void newRank() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        Rank rank = new Rank();
        rank.setName(newrankname);
        rank.setUserid(Pagez.getUserSession().getUser().getUserid());
        try{rank.save();}catch(Exception ex){logger.error("", ex);}
        newrankid = rank.getRankid();
    }

    public String getNewrankname() {
        return newrankname;
    }

    public void setNewrankname(String newrankname) {
        this.newrankname = newrankname;
    }

    public int getNewrankid() {
        return newrankid;
    }

    public void setNewrankid(int newrankid) {
        this.newrankid = newrankid;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }
}