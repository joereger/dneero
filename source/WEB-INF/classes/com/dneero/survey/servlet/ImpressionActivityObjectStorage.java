package com.dneero.survey.servlet;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.money.SurveyMoneyStatus;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class ImpressionActivityObjectStorage {

    public static void store(ImpressionActivityObject iao){
        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);

        //Find user
        User user = null;
        if (iao.getUserid()>0){
            user = User.get(iao.getUserid());
        }

        //Find bloggerid
        int bloggerid = 0;
        if (user!=null && user.getUserid()>0 && user.getBloggerid()>0){
            bloggerid = user.getBloggerid();
        }

        //Find the survey
        Survey survey = null;
        int surveyimpressionsthatqualifyforpayment = 0;
        if (iao.getSurveyid()>0){
            survey = Survey.get(iao.getSurveyid());
            if (survey!=null && survey.getSurveyid()>0){
                Object obj = HibernateUtil.getSession().createQuery("select sum(impressionsqualifyingforpayment) from Impression where surveyid='"+survey.getSurveyid()+"'").uniqueResult();
                if (obj!=null && Num.isinteger(String.valueOf(obj))){
                    surveyimpressionsthatqualifyforpayment = ((Long)obj).intValue();
                }
            } else {
                //Error, survey not found, don't record
                logger.debug("Surveyid="+iao.getSurveyid()+" not found so aborting impression save.");
                return;
            }
        }

        //Find the responseid
        Response response = null;
        int responseid = 0;
        if (iao.getResponseid()>0){
            response = Response.get(iao.getResponseid());
            if (response!=null && response.getResponseid()>0){
                //I've got a valid incoming responseid
                responseid = response.getResponseid();
            }
        }
        if (responseid==0){
            logger.debug("responseid=0, will try to calculate with user.getBloggerid() and surveyid");
            //This is just a backup way to see if I can determine the responseid from the surveyid and userid
            if (user!=null && user.getBloggerid()>0 && survey!=null && survey.getSurveyid()>0){
                logger.debug("user.getBloggerid()="+user.getBloggerid() + " survey.getSurveyid()="+survey.getSurveyid());
                List results = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+user.getBloggerid()+"' and surveyid='"+survey.getSurveyid()+"'").setCacheable(true).list();
                logger.debug("results.size()="+results.size());
                for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                    Response resp = (Response) iterator.next();
                    if (response==null){
                        response=resp;
                        responseid = response.getResponseid();
                    }
                    //Choose the most recent response by this blogger... there should generally only be one
                    if (response.getResponsedate().before(resp.getResponsedate())){
                        response=resp;
                        responseid = response.getResponseid();
                    }
                }
            } else {
                logger.debug("responseid=0 but can't calculate it because user==null and/or survey==null");
            }
        }

        //Check to see if userid and responseid correlate
        if (user!=null &&  response!=null && user.getBloggerid()!=response.getBloggerid()){
            //Somebody may be spoofing the system, trying to get credit for another's surveys, etc... just logging now to see if it's common/an issue
            logger.error("user.getBloggerid()!=response.getBloggerid(). User possibly spoofing system.  iao.getResponseid()="+iao.getResponseid()+" iao.getUserid()="+iao.getUserid()+" iao.getReferer()="+iao.getReferer());
        }

        //Find number of impressions on this blog qualify for payment
        int blogimpressionsthatqualifyforpayment = getImpressionsThatQualifyForPayment(user, survey);

        //See if this impressiondetail qualifies for payment
        int qualifiesforpaymentstatus = Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE;
        String qualifiesforpaymentstatusreason = "";
        if (surveyimpressionsthatqualifyforpayment>survey.getMaxdisplaystotal()){
            qualifiesforpaymentstatus = Impressiondetail.QUALIFIESFORPAYMENTSTATUS_FALSE;
            qualifiesforpaymentstatusreason = "The survey already reached its maximum number of survey displays for all bloggers.";
        }
        if (blogimpressionsthatqualifyforpayment+1>survey.getMaxdisplaysperblog()){
            qualifiesforpaymentstatus = Impressiondetail.QUALIFIESFORPAYMENTSTATUS_FALSE;
            qualifiesforpaymentstatusreason = "This blogger has already displayed the survey the maximum number of times.";
        }
        int dayssinceclose = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(survey.getEnddate()));
        if (dayssinceclose>SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS){
            qualifiesforpaymentstatus = Impressiondetail.QUALIFIESFORPAYMENTSTATUS_FALSE;
            qualifiesforpaymentstatusreason = "Over "+ SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS+" days since end of survey.";
        }

        //logger.debug("iao.getDate()="+iao.getDate().toString());
        //logger.debug("iao.getDate()="+ Time.dateformatfordb(Time.getCalFromDate(iao.getDate())));

        //See if there's an existing impression to append this to, if not create one
        Impression impression = null;
        List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+iao.getSurveyid()+"' and referer='"+iao.getReferer()+"' and responseid='"+responseid+"'").list();
        if (impressions.size()>0){
            for (Iterator it = impressions.iterator(); it.hasNext(); ) {
                impression = (Impression)it.next();
                //Only increment if this qualifies
                if (qualifiesforpaymentstatus==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                    impression.setImpressionsqualifyingforpayment(impression.getImpressionsqualifyingforpayment()+1);
                }
                impression.setImpressionstotal(impression.getImpressionstotal()+1);
            }
        } else {
            impression = new Impression();
            impression.setFirstseen(iao.getDate());
            impression.setSurveyid(iao.getSurveyid());
            impression.setUserid(iao.getUserid());
            impression.setResponseid(responseid);
            //Only increment if this qualifies
            if (qualifiesforpaymentstatus==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                impression.setImpressionsqualifyingforpayment(1);
            } else {
                impression.setImpressionsqualifyingforpayment(0);
            }
            impression.setImpressionstotal(0);
            impression.setReferer(iao.getReferer());
        }
        //logger.debug("about to call impression.save()");
        try{impression.save();} catch (GeneralException gex){logger.error(gex);}
        //logger.debug("done with impression.save()");

        //Record the impressiondetail
        Impressiondetail impressiondetail = new Impressiondetail();
        impressiondetail.setImpressionid(impression.getImpressionid());
        impressiondetail.setImpressiondate(iao.getDate());
        impressiondetail.setIp(iao.getIp());
        impressiondetail.setQualifiesforpaymentstatus(qualifiesforpaymentstatus);
        impressiondetail.setBloggerid(bloggerid);
        impressiondetail.setQualifiesforpaymentstatusreason(qualifiesforpaymentstatusreason);

        try{impressiondetail.save();} catch (GeneralException gex){logger.error(gex);}

    }





    public static int getImpressionsThatQualifyForPayment(User user, Survey survey){
        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);
        //logger.debug("getImpressionsThatQualifyForPayment start");
        int impressionsthatqualifyforpayment = 0;
        if (user!=null && user.getUserid()>0){
            List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                               .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                               .add(Restrictions.eq("userid", user.getUserid()))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Impression> iterator = impressions.iterator(); iterator.hasNext();) {
                Impression impression = (Impression) iterator.next();
                impressionsthatqualifyforpayment = impressionsthatqualifyforpayment + impression.getImpressionsqualifyingforpayment();
            }
        }
        //logger.debug("getImpressionsThatQualifyForPayment end");
        return impressionsthatqualifyforpayment;
    }

}
