package com.dneero.cache.html;

import com.dneero.dao.hibernate.HibernateUtilDbcache;
import com.dneero.dao.Dbcacheexpirable;
import com.dneero.dao.Survey;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.money.SurveyMoneyStatus;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;


/**
 * Dbcache cache implementation
 * This stores cached items in a separate database as defined in HibernateUtilDbcache.java
 */
public class DbcacheexpirableCache {


    public DbcacheexpirableCache(){

    }


    public static Object get(String key, String group) {
        Logger logger = Logger.getLogger(Dbcacheexpirable.class);
        try{
            List<Dbcacheexpirable> dbcaches = HibernateUtilDbcache.getSession().createCriteria(Dbcacheexpirable.class)
                                               .add(Restrictions.eq("grp", group))
                                               .add(Restrictions.eq("keyname", key))
                                               .setCacheable(true)
                                               .list();
            if (dbcaches!=null && dbcaches.size()>0){
                Dbcacheexpirable dbcache = dbcaches.get(0);
                //If it's expired delete and return null
                if (dbcache.getExpirationdate().before(Calendar.getInstance().getTime())){
                    logger.debug("deleting Dbcacheexpirable because it's expired, returning null to force refresh");
                    dbcache.delete();
                    return null;
                }
                return dbcache.getVal();
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return null;
    }

    public static void put(String key, String group, Object obj, Date expirationdate) {
        Logger logger = Logger.getLogger(Dbcacheexpirable.class);
        try{

            //Delete any existing entries from the same group/key
            List<Dbcacheexpirable> dbcaches = HibernateUtilDbcache.getSession().createCriteria(Dbcacheexpirable.class)
                                               .add(Restrictions.eq("grp", group))
                                               .add(Restrictions.eq("keyname", key))
                                               .setCacheable(true)
                                               .list();
            if (dbcaches!=null && dbcaches.size()>0){
                for (Iterator<Dbcacheexpirable> dbcacheIterator=dbcaches.iterator(); dbcacheIterator.hasNext();) {
                    Dbcacheexpirable dbcache=dbcacheIterator.next();
                    dbcache.delete();
                }
            }

            //Save the value
            Dbcacheexpirable dbcacheNew = new Dbcacheexpirable();
            dbcacheNew.setDate(new Date());
            dbcacheNew.setExpirationdate(expirationdate);
            dbcacheNew.setGrp(group);
            dbcacheNew.setKeyname(key);
            dbcacheNew.setVal(obj);
            dbcacheNew.save();

        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static void purgeStaleItems(){
        Logger logger = Logger.getLogger(Dbcacheexpirable.class);
        try{
            String now = Time.dateformatfordb(Calendar.getInstance());
            HibernateUtilDbcache.getSession().createQuery("delete Dbcacheexpirable d where d.dbcacheexpirableid>0 and d.expirationdate<'"+now+"'").executeUpdate();
        } catch (Exception ex){logger.error("", ex);}

        
    }

    public static void flush() {
        Logger logger = Logger.getLogger(Dbcacheexpirable.class);
        try{
            String emptyString = "";
            HibernateUtilDbcache.getSession().createQuery("delete Dbcacheexpirable d where d.dbcacheexpirableid>0"+emptyString).executeUpdate();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static void flush(String group) {
        Logger logger = Logger.getLogger(Dbcacheexpirable.class);
        try{
            HibernateUtilDbcache.getSession().createQuery("delete Dbcacheexpirable d where d.dbcacheexpirableid>0 and d.grp='"+group+"'").executeUpdate();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static void flush(String key, String group) {
        Logger logger = Logger.getLogger(Dbcacheexpirable.class);
        try{
            HibernateUtilDbcache.getSession().createQuery("delete Dbcacheexpirable d where d.dbcacheexpirableid>0 and d.grp='"+group+"' and d.keyname='"+key+"'").executeUpdate();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static Date expireIn5Min(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -5).getTime();
    }

    public static Date expireIn15Min(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -15).getTime();
    }

    public static Date expireIn30Min(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -30).getTime();
    }

    public static Date expireIn1Hr(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -60).getTime();
    }

    public static Date expireIn2Hrs(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -120).getTime();
    }

    public static Date expireIn3Hrs(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -180).getTime();
    }

    public static Date expireIn6Hrs(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -360).getTime();
    }

    public static Date expireIn12Hrs(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -720).getTime();
    }

    public static Date expireIn24Hrs(){
        return Time.xMinutesAgoEnd(Calendar.getInstance(), -1440).getTime();
    }

    //Used to handle survey-specific expiration logic
    public static Date expireSurveyInXHrs(Survey survey, int hr){
        return expireSurveyInXMin(survey, hr*60);
    }
    public static Date expireSurveyInXMin(Survey survey, int min){
        Logger logger = Logger.getLogger(Dbcacheexpirable.class);
        Date xmin = Time.xMinutesAgoEnd(Calendar.getInstance(), -1*min).getTime();
        if (survey.getStatus()==Survey.STATUS_OPEN){
            //It's open so just expire it in x minutes
            logger.debug("surveyid="+survey.getSurveyid()+" It's open so will expire in xmin");
            return xmin;
        } else {
            //Figure out whether there's still activity
            int daysold = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(survey.getEnddate()));
            logger.debug("daysold="+daysold);
            if (daysold>(SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS+1)){
                logger.debug("surveyid="+survey.getSurveyid()+"It's past the date that we collect/pay for impressions so set expiration way in the future");
                return Time.xYearsAgoEnd(Calendar.getInstance(), -10).getTime();
            } else if (daysold>3){
                logger.debug("surveyid="+survey.getSurveyid()+"It's past the date that we collect/pay for impressions but only recently so I'll expire in 24 hours");
                return Time.xHoursAgoEnd(Calendar.getInstance(), -24).getTime();
            } else {
                //It's still within the update zone... i.e. within 3 days of the closing date
                logger.debug("surveyid="+survey.getSurveyid()+"It's not open but still within the update zone so will expire in xmin");
                return xmin;
            }
        }



    }





}