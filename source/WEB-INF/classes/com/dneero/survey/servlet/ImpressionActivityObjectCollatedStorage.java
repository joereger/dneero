package com.dneero.survey.servlet;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.*;
import com.dneero.util.*;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.money.UserImpressionFinder;
import com.dneero.helpers.UserInputSafe;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class ImpressionActivityObjectCollatedStorage {

    private static HashMap<String, Integer> impressionsbyuser;

    public static void store(ImpressionActivityObjectCollated iao){
        Logger logger = Logger.getLogger(ImpressionActivityObjectCollatedStorage.class);

        //Return on nulls
        if (iao==null){
            return;
        }

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
        int surveyimpressionspaidandtobepaid = 0;
        if (iao.getSurveyid()>0){
            survey = Survey.get(iao.getSurveyid());
            if (survey!=null && survey.getSurveyid()>0){
                //int impressionspaid = NumFromUniqueResult.getInt("select sum(impressionspaid) from Impression where surveyid='"+survey.getSurveyid()+"'");
                //int impressionstobepaid = NumFromUniqueResult.getInt("select sum(impressionstobepaid) from Impression where surveyid='"+survey.getSurveyid()+"'");
                int impressionspaid = survey.getImpressionspaid();
                int impressionstobepaid = survey.getImpressionstobepaid();
                //Sum them
                surveyimpressionspaidandtobepaid = impressionspaid + impressionstobepaid;
            } else {
                //Error, survey not found, don't record
                logger.debug("Surveyid="+iao.getSurveyid()+" not found so aborting impression save.");
                return;
            }
        }
        
        //Wipe auth_code from url
        if (iao.getReferer()!=null && iao.getReferer().indexOf("auth_token")>-1){
            logger.debug("before replacing auth_token.  iao.getReferer()="+iao.getReferer());
            iao.setReferer(iao.getReferer().replaceAll("auth_token=", RandomString.randomAlphanumeric(3)+"="+RandomString.randomAlphanumeric(5)));
            logger.debug("after replacing auth_token.  iao.getReferer()="+iao.getReferer());
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
        //Make sure we have a response
        if (response!=null && response.getResponseid()>0){
            if (user!=null && user.getUserid()>0){
                if (survey!=null && survey.getSurveyid()>0){

                    //Find number of impressions on this blog qualify for payment
                    //@todo optimize... this is the main performance hog in this code
                    //int blogimpressionspaidandtobepaid = UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);
                    int blogimpressionspaidandtobepaid = getTotalImpressionsPaidAndToBePaid(survey, user);

                    //See if this impression qualifies for payment
                    int impressionsqualifyingforpayment = iao.getImpressions();
                    //Make sure we fit into the survey total
                    int remainingsurveyimpressions = survey.getMaxdisplaystotal()-surveyimpressionspaidandtobepaid;
                    if (remainingsurveyimpressions<impressionsqualifyingforpayment){
                        impressionsqualifyingforpayment = remainingsurveyimpressions;
                    }
                    //Make sure we fit into the blog total
                    int remainingblogimpressions = survey.getMaxdisplaysperblog()-blogimpressionspaidandtobepaid;
                    if (remainingblogimpressions<impressionsqualifyingforpayment){
                        impressionsqualifyingforpayment = remainingblogimpressions;
                    }
                    //This must be last... if the time period for posting is passed, set to 0
                    int dayssinceclose = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(survey.getEnddate()));
                    if (dayssinceclose>SurveyMoneyStatus.DAYSAFTERCLOSEOFSURVEYWECOLLECTFORIMPRESSIONS){
                        impressionsqualifyingforpayment = 0;
                    }
                    //This must also be last... if the response is rejected, impressions don't count for payment
                    if (response.getIssysadminrejected()){
                        impressionsqualifyingforpayment = 0;
                    }

                    //logger.debug("iao.getDate()="+iao.getDate().toString());
                    //logger.debug("iao.getDate()="+ Time.dateformatfordb(Time.getCalFromDate(iao.getDate())));

                    //See if there's an existing impression to append this to, if not create one
                    Impression impression = null;
                    //List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+iao.getSurveyid()+"' and userid='"+iao.getUserid()+"' and referer='"+ UserInputSafe.clean(iao.getReferer().trim())+"' and responseid='"+responseid+"'").list();
                    List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                           .add(Restrictions.eq("surveyid", iao.getSurveyid()))
                           .add(Restrictions.eq("userid", iao.getUserid()))
                           .add(Restrictions.eq("responseid", responseid))
                           .add(Restrictions.eq("referer", iao.getReferer().trim()))
                           .setCacheable(true)
                           .list();
                    logger.debug("impressions.size()="+impressions.size());
                    if (impressions.size()>0){
                        for (Iterator it = impressions.iterator(); it.hasNext(); ) {
                            impression = (Impression)it.next();
                            impression.setImpressionstobepaid(impression.getImpressionstobepaid()+impressionsqualifyingforpayment);
                            impression.setImpressionstotal(impression.getImpressionstotal()+iao.getImpressions());
                        }
                    } else {
                        impression = new Impression();
                        impression.setFirstseen(new Date());
                        impression.setSurveyid(iao.getSurveyid());
                        impression.setUserid(iao.getUserid());
                        impression.setResponseid(responseid);
                        impression.setImpressionstobepaid(impressionsqualifyingforpayment);
                        impression.setImpressionstotal(iao.getImpressions());
                        impression.setReferer(iao.getReferer());
                    }
                    //Update the internal perf cache
                    addImp(survey, user, impressionsqualifyingforpayment);
                    //Update the impressionsbyday string, but only if this isn't rejected by sysadmin
                    if (!response.getIssysadminrejected()){
                        int dayssincetakingsurvey = DateDiff.dateDiff("day", Time.getCalFromDate(new Date()), Time.getCalFromDate(response.getResponsedate()));
                        ImpressionsByDayUtil ibdu = new ImpressionsByDayUtil(impression.getImpressionsbyday());
                        ibdu.add(iao.getImpressions(), dayssincetakingsurvey);
                        impression.setImpressionsbyday(ibdu.getAsString());
                    }
                    logger.debug("about to call impression.save()");
                    try{impression.save();} catch (GeneralException gex){logger.error(gex);}
                    logger.debug("done with impression.save()");
                }
            }
        }

    }

    private static int getTotalImpressionsPaidAndToBePaid(Survey survey, User user){
        Logger logger = Logger.getLogger(ImpressionActivityObjectCollatedStorage.class);
        if (impressionsbyuser==null){
            impressionsbyuser = new HashMap<String, Integer>();
        }
        String key = "userid"+user.getUserid()+"-surveyid"+survey.getSurveyid();
        int totimp = 0;
        if (impressionsbyuser.containsKey(key)){
            totimp = impressionsbyuser.get(key);
        } else {
            totimp = UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);
        }
        impressionsbyuser.put(key, totimp);
        logger.debug("returning totimp="+totimp);
        return totimp;
    }

    private static void addImp(Survey survey, User user, int impressions){
        if (impressionsbyuser==null){
            impressionsbyuser = new HashMap<String, Integer>();
        }
        String key = "userid"+user.getUserid()+"-surveyid"+survey.getSurveyid();
        int totimp = 0;
        if (impressionsbyuser.containsKey(key)){
            totimp = impressions + impressionsbyuser.get(key);
            impressionsbyuser.put(key, totimp);
        } else {
            totimp = impressions + UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);
            impressionsbyuser.put(key, totimp);
        }
    }

    public static void resetImpCache(){
        impressionsbyuser = null;
    }







}
