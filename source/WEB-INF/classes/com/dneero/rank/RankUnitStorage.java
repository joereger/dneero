package com.dneero.rank;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 18, 2008
 * Time: 1:51:33 PM
 */
public class RankUnitStorage {

    public static void store(Rank rank, Response response, RankUnit rankUnit){
        Logger logger = Logger.getLogger(RankForQuestion.class);
        Blogger blogger = Blogger.get(response.getBloggerid());
        User user = User.get(blogger.getUserid());
        List<Rankuser> rankusers = HibernateUtil.getSession().createCriteria(Rankuser.class)
                                    .add(Restrictions.eq("rankid", rank.getRankid()))
                                    .add(Restrictions.eq("userid", user.getUserid()))
                                    .add(Restrictions.eq("responseid", response.getResponseid()))
                                    .setCacheable(true)
                                    .list();
        if (rankusers!=null && rankusers.size()>0){
            //Update points for this record (should only be one)
            for (Iterator<Rankuser> rankuserIterator = rankusers.iterator(); rankuserIterator.hasNext();) {
                Rankuser rankuser = rankuserIterator.next();
                //Only update if it's actually different
                if (rankuser.getPoints()!=rankUnit.getPoints() || rankuser.getNormalizedpoints()!=rankUnit.getNormalizedpoints()){
                    rankuser.setPoints(rankUnit.getPoints());
                    rankuser.setNormalizedpoints(rankUnit.getNormalizedpoints());
                    try{rankuser.save();}catch(Exception ex){logger.error("", ex);}
                }
            }
        } else {
            //Create a new rankuser entry
            Rankuser rankuser = new Rankuser();
            rankuser.setRankid(rank.getRankid());
            rankuser.setUserid(user.getUserid());
            rankuser.setResponseid(response.getResponseid());
            rankuser.setPoints(rankUnit.getPoints());
            rankuser.setNormalizedpoints(rankUnit.getNormalizedpoints());
            rankuser.setDate(response.getResponsedate());
            try{rankuser.save();}catch(Exception ex){logger.error("", ex);}
        }
    }

}
