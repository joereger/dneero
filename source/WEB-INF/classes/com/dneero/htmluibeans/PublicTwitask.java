package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;

import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.helpers.IsBloggerInPanel;
import com.dneero.helpers.FastGetUserStuff;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:11 AM
 */
public class PublicTwitask implements Serializable {

    private Twitask twitask;
    private List<PublicTwitaskListitem> twitanswers;

    public PublicTwitask(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get(Integer.parseInt(Pagez.getRequest().getParameter("twitaskid")));
        }

        if (twitask==null){
            return;
        }

        twitanswers = new ArrayList<PublicTwitaskListitem>();
        List<Twitanswer> tans = HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                           .add(Restrictions.eq("twitaskid", twitask.getTwitaskid()))
                                           .add(Restrictions.eq("status", Twitanswer.STATUS_APPROVED))
                                           .addOrder(Order.desc("twitaskid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Twitanswer> twitanswerIterator=tans.iterator(); twitanswerIterator.hasNext();) {
            Twitanswer twitanswer=twitanswerIterator.next();
            PublicTwitaskListitem ptli = new PublicTwitaskListitem();
            ptli.setTwitanswer(twitanswer);
            twitanswers.add(ptli);
        }


    }

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }

    public List<PublicTwitaskListitem> getTwitanswers() {
        return twitanswers;
    }

    public void setTwitanswers(List<PublicTwitaskListitem> twitanswers) {
        this.twitanswers=twitanswers;
    }
}