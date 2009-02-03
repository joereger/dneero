package com.dneero.helpers;

import com.dneero.dao.User;
import com.dneero.dao.Twitanswer;
import com.dneero.dao.Twitask;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.SurveyCriteriaXML;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Feb 3, 2009
 * Time: 10:38:20 AM
 */
public class TwitanswerFinderAfterAccountInfoChange {

    public static void findAndChangeUseridOfTwitanswers(User user){
        Logger logger = Logger.getLogger(TwitanswerFinderAfterAccountInfoChange.class);
        try{
            if (user.getInstantnotifytwitterusername()!=null && !user.getInstantnotifytwitterusername().equals("")){
                String twitterusername = user.getInstantnotifytwitterusername().trim();
                List<Twitanswer> twitanswers = HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                                   .add(Restrictions.eq("twitterusername", twitterusername))
                                                   .add(Restrictions.eq("userid", 0))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Twitanswer> twitanswerIterator=twitanswers.iterator(); twitanswerIterator.hasNext();) {
                    Twitanswer twitanswer=twitanswerIterator.next();
                    if (user.getBloggerid()>0){
                        boolean iscriteriaxmlqualified = false;
                        if (twitanswer.getTwitaskid()>0){
                            Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
                            SurveyCriteriaXML scXML = new SurveyCriteriaXML(twitask.getCriteriaxml());
                            if (scXML.isUserQualified(user)){
                                iscriteriaxmlqualified = true;
                            }
                            if (iscriteriaxmlqualified){
                                twitanswer.setStatus(Twitanswer.STATUS_PENDINGREVIEW);
                            } else {
                                twitanswer.setStatus(Twitanswer.STATUS_DOESNTQUALIFY);
                            }
                        } else {
                            twitanswer.setStatus(Twitanswer.STATUS_NOTWITASK);
                        }
                    } else {
                        twitanswer.setStatus(Twitanswer.STATUS_NOBLOGGER);
                    }
                    twitanswer.setUserid(user.getUserid());
                    twitanswer.save();
                }
            }
        } catch (Exception ex){
            logger.error(ex);
        }
    }

    public static void findNoBloggerStatusTwitanswers(User user){
        Logger logger = Logger.getLogger(TwitanswerFinderAfterAccountInfoChange.class);
        try{
            if (user.getBloggerid()>0){
                List<Twitanswer> twitanswers = HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                                   .add(Restrictions.eq("status", Twitanswer.STATUS_NOBLOGGER))
                                                   .add(Restrictions.eq("userid", user.getUserid()))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Twitanswer> twitanswerIterator=twitanswers.iterator(); twitanswerIterator.hasNext();) {
                    Twitanswer twitanswer=twitanswerIterator.next();
                    boolean iscriteriaxmlqualified = false;
                    if (twitanswer.getTwitaskid()>0){
                        Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
                        SurveyCriteriaXML scXML = new SurveyCriteriaXML(twitask.getCriteriaxml());
                        if (scXML.isUserQualified(user)){
                            iscriteriaxmlqualified = true;
                        }
                        if (iscriteriaxmlqualified){
                            twitanswer.setStatus(Twitanswer.STATUS_PENDINGREVIEW);
                        } else {
                            twitanswer.setStatus(Twitanswer.STATUS_DOESNTQUALIFY);
                        }
                    } else {
                        twitanswer.setStatus(Twitanswer.STATUS_NOTWITASK);
                    }
                    twitanswer.save();
                }
            }
        } catch (Exception ex){
            logger.error(ex);
        }
    }


}
