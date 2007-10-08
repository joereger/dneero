package com.dneero.survey.servlet;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.money.UserImpressionFinder;
import com.dneero.helpers.UserInputSafe;

import java.util.List;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class ImpressionActivityObjectCollatedStorage {

    public static void store(ImpressionActivityObjectCollated iao){
        Logger logger = Logger.getLogger(ImpressionActivityObjectCollatedStorage.class);

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
                int impressionspaid = NumFromUniqueResult.getInt("select sum(impressionspaid) from Impression where surveyid='"+survey.getSurveyid()+"'");
                int impressionstobepaid = NumFromUniqueResult.getInt("select sum(impressionstobepaid) from Impression where surveyid='"+survey.getSurveyid()+"'");
                //Sum them
                surveyimpressionspaidandtobepaid = impressionspaid + impressionstobepaid;
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
        //Make sure we have a response
        if (response!=null && response.getResponseid()>0){
            if (user!=null && user.getUserid()>0){
                if (survey!=null && survey.getSurveyid()>0){


                    //Find number of impressions on this blog qualify for payment
                    int blogimpressionspaidandtobepaid = UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);

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

                    //logger.debug("iao.getDate()="+iao.getDate().toString());
                    //logger.debug("iao.getDate()="+ Time.dateformatfordb(Time.getCalFromDate(iao.getDate())));

                    //See if there's an existing impression to append this to, if not create one
                    Impression impression = null;
                    List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+iao.getSurveyid()+"' and userid='"+iao.getUserid()+"' and referer='"+ UserInputSafe.clean(iao.getReferer().trim())+"' and responseid='"+responseid+"'").list();
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
                    //Update the impressionsbyday string
                    int dayssincetakingsurvey = DateDiff.dateDiff("day", Time.getCalFromDate(new Date()), Time.getCalFromDate(response.getResponsedate()));
                    ImpressionsByDayUtil ibdu = new ImpressionsByDayUtil(impression.getImpressionsbyday());
                    ibdu.add(1, dayssincetakingsurvey);
                    impression.setImpressionsbyday(ibdu.getAsString());
                    //logger.debug("about to call impression.save()");
                    try{impression.save();} catch (GeneralException gex){logger.error(gex);}
                    //logger.debug("done with impression.save()");
                }
            }
        }

    }







}