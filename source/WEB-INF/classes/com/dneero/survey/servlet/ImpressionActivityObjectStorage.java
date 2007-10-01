package com.dneero.survey.servlet;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Num;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.money.UserImpressionFinder;
import com.dneero.money.SurveyMoneyStatus;

import java.util.List;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class ImpressionActivityObjectStorage {

    public static void store(ImpressionActivityObject iao){
//        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);
//
//        //Find user
//        User user = null;
//        if (iao.getUserid()>0){
//            user = User.get(iao.getUserid());
//        }
//
//        //Find bloggerid
//        int bloggerid = 0;
//        if (user!=null && user.getUserid()>0 && user.getBloggerid()>0){
//            bloggerid = user.getBloggerid();
//        }
//
//        //Find the survey
//        Survey survey = null;
//        int surveyimpressionspaidandtobepaid = 0;
//        if (iao.getSurveyid()>0){
//            survey = Survey.get(iao.getSurveyid());
//            if (survey!=null && survey.getSurveyid()>0){
//                int impressionspaid = 0;
//                Object obj = HibernateUtil.getSession().createQuery("select sum(impressionspaid) from Impression where surveyid='"+survey.getSurveyid()+"'").uniqueResult();
//                if (obj!=null && Num.isinteger(String.valueOf(obj))){
//                    impressionspaid = ((Long)obj).intValue();
//                }
//                int impressionstobepaid = 0;
//                Object obj2 = HibernateUtil.getSession().createQuery("select sum(impressionstobepaid) from Impression where surveyid='"+survey.getSurveyid()+"'").uniqueResult();
//                if (obj2!=null && Num.isinteger(String.valueOf(obj2))){
//                    impressionstobepaid = ((Long)obj2).intValue();
//                }
//                //Sum them
//                surveyimpressionspaidandtobepaid = impressionspaid + impressionstobepaid;
//            } else {
//                //Error, survey not found, don't record
//                logger.debug("Surveyid="+iao.getSurveyid()+" not found so aborting impression save.");
//                return;
//            }
//        }
//
//        //Find the responseid
//        Response response = null;
//        int responseid = 0;
//        if (iao.getResponseid()>0){
//            response = Response.get(iao.getResponseid());
//            if (response!=null && response.getResponseid()>0){
//                //I've got a valid incoming responseid
//                responseid = response.getResponseid();
//            }
//        }
//        if (responseid==0){
//            logger.debug("responseid=0, will try to calculate with user.getBloggerid() and surveyid");
//            //This is just a backup way to see if I can determine the responseid from the surveyid and userid
//            if (user!=null && user.getBloggerid()>0 && survey!=null && survey.getSurveyid()>0){
//                logger.debug("user.getBloggerid()="+user.getBloggerid() + " survey.getSurveyid()="+survey.getSurveyid());
//                List results = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+user.getBloggerid()+"' and surveyid='"+survey.getSurveyid()+"'").setCacheable(true).list();
//                logger.debug("results.size()="+results.size());
//                for (Iterator iterator = results.iterator(); iterator.hasNext();) {
//                    Response resp = (Response) iterator.next();
//                    if (response==null){
//                        response=resp;
//                        responseid = response.getResponseid();
//                    }
//                    //Choose the most recent response by this blogger... there should generally only be one
//                    if (response.getResponsedate().before(resp.getResponsedate())){
//                        response=resp;
//                        responseid = response.getResponseid();
//                    }
//                }
//            } else {
//                logger.debug("responseid=0 but can't calculate it because user==null and/or survey==null");
//            }
//        }
//        if (response!=null && response.getResponseid()>0){
//            if (user!=null && user.getUserid()>0){
//                if (survey!=null && survey.getSurveyid()>0){
//
//        //Check to see if userid and responseid correlate
//        if (user!=null &&  response!=null && user.getBloggerid()!=response.getBloggerid()){
//            //Somebody may be spoofing the system, trying to get credit for another's surveys, etc... just logging now to see if it's common/an issue
//            logger.error("user.getBloggerid()!=response.getBloggerid(). User possibly spoofing system.  iao.getResponseid()="+iao.getResponseid()+" iao.getUserid()="+iao.getUserid()+" iao.getReferer()="+iao.getReferer()+" user.getBloggerid()="+user.getBloggerid()+" response.getBloggerid()="+response.getBloggerid());
//        }
//
//        //Find number of impressions on this blog qualify for payment
//        int blogimpressionspaidandtobepaid = UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);
//
//        //See if this impression qualifies for payment
//        boolean qualifiesforpayment = true;
//        String qualifiesforpaymentstatusreason = "";
//        if (surveyimpressionspaidandtobepaid>survey.getMaxdisplaystotal()){
//            qualifiesforpayment = false;
//            qualifiesforpaymentstatusreason = "The survey already reached its maximum number of survey displays for all bloggers.";
//        }
//        if (blogimpressionspaidandtobepaid+1>survey.getMaxdisplaysperblog()){
//            qualifiesforpayment = false;
//            qualifiesforpaymentstatusreason = "This blogger has already displayed the survey the maximum number of times.";
//        }
//        int dayssinceclose = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(survey.getEnddate()));
//        if (dayssinceclose>SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS){
//            qualifiesforpayment = false;
//            qualifiesforpaymentstatusreason = "Over "+ SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS+" days since end of survey.";
//        }
//
//
//        //@todo Record the details
//        //impression.getImpressionid()
//        //iao.getDate()
//        //iao.getIp()
//        //qualifiesforpayment
//        //userid
//        //qualifiesforpaymentreason
//                }
//            }
//        }


    }







}
