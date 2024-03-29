package com.dneero.survey.servlet;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.HibernateUtilImpressions;
import com.dneero.helpers.VenueUtils;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.DateDiff;
import com.dneero.util.GeneralException;
import com.dneero.util.RandomString;
import com.dneero.util.Time;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class ImpressionActivityObjectCollatedStorage {

    //private static HashMap<String, Integer> impressionsbyuser;

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
        //Make sure we have a response, user and survey
        if (response!=null && response.getResponseid()>0){
            if (user!=null && user.getUserid()>0){
                if (survey!=null && survey.getSurveyid()>0){

                    //Find number of impressions on this blog qualify for payment
                    //int blogimpressionspaidandtobepaid = UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);
                    //int blogimpressionspaidandtobepaid = getTotalImpressionsPaidAndToBePaid(survey, user);
                    int blogimpressionspaidandtobepaid = response.getImpressionspaid() + response.getImpressionstobepaid();

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
                    //This must also be last... lol... venue/url validation
                    boolean isvalidurl = false;
                    int venueid = 0;
                    try{
                        String referer = iao.getReferer();
                        logger.debug("START referer="+referer);
                        Pl pl = Pl.get(survey.getPlid());
                        if (pl.getIsvenuerequired()){
                            if (user.getFacebookuserid()<=0){
                                logger.debug("not facebook");
                                if (referer!=null && !referer.equals("")){
                                    logger.debug("referer not null");
                                    Blogger blogger = Blogger.get(user.getBloggerid());
                                    if (blogger!=null && blogger.getBloggerid()>0){
                                        logger.debug("blogger not null");
                                        for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
                                            Venue venue=iterator.next();
                                            logger.debug("found venue url="+venue.getUrl());
                                            if (venue.getIsactive() && !venue.getIssysadminrejected()){
                                                logger.debug("venue is active and venue is not sysadminrejected");
                                                if (referer.indexOf(VenueUtils.stringToMatchForImpressions(venue.getUrl()))>-1){
                                                    isvalidurl = true;
                                                    venueid = venue.getVenueid();
                                                    logger.debug("PASS VALID     referer="+referer+" venue.getUrl()="+venue.getUrl()+"");
                                                    break;
                                                } else {
                                                    logger.debug("FAIL NOT VALID referer="+referer+" venue.getUrl()="+venue.getUrl()+"");
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                //It's a facebook user
                                isvalidurl = true;
                            }
                        } else {
                            //Venue not required for this pl
                            isvalidurl = true;
                        }
                        Calendar startChecking = Time.dbstringtocalendar("2009-03-01 00:00:00");
                        if (Calendar.getInstance().after(startChecking)){
                            if (!isvalidurl){
                                impressionsqualifyingforpayment = 0;
                            }
                        } else {
                            //Just record/note it in logs
                            logger.debug("FINAL isvalidurl="+isvalidurl);
                            //Now just force it to be true until Mar 1st
                            isvalidurl = true;
                        }
                    } catch (Exception ex){
                        logger.error("", ex);
                    }
                    //End of venue/url validation

                    //logger.debug("iao.getDate()="+iao.getDate().toString());
                    //logger.debug("iao.getDate()="+ Time.dateformatfordb(Time.getCalFromDate(iao.getDate())));

                    //See if there's an existing impression to append this to, if not create one
                    Impression impression = null;
                    //List<Impression> impressions = HibernateUtilImpressions.getSession().createQuery("from Impression where surveyid='"+iao.getSurveyid()+"' and userid='"+iao.getUserid()+"' and referer='"+ UserInputSafe.clean(iao.getReferer().trim())+"' and responseid='"+responseid+"'").list();
                    //@todo convert referer to a field with length so I can index it in MySQL for performance gains
                    List<Impression> impressions = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
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
                            impression.setImpressionstotal(impression.getImpressionstotal()+iao.getImpressions());
                        }
                    } else {
                        impression = new Impression();
                        impression.setFirstseen(new Date());
                        impression.setSurveyid(iao.getSurveyid());
                        impression.setUserid(iao.getUserid());
                        impression.setResponseid(responseid);
                        impression.setImpressionstotal(iao.getImpressions());
                        impression.setReferer(iao.getReferer());
                    }
                    //Save the impression
                    logger.debug("about to call impression.save()");
                    try{impression.save();} catch (GeneralException gex){logger.error(gex);}

                    //Update the impressionsbyday string for the Response, but only if this isn't rejected by sysadmin
                    ImpressionsByDayUtil ibduResponse = new ImpressionsByDayUtil(response.getImpressionsbyday());
                    if (!response.getIssysadminrejected()){
                        int dayssincetakingsurvey = DateDiff.dateDiff("day", Time.getCalFromDate(new Date()), Time.getCalFromDate(response.getResponsedate()));
                        //Only save to response day-by-day if it's a valid url... critical because response status (paid, pending, etc) depends on this and we don't want bad venues to accrue impressions that help the response
                        if (isvalidurl){
                            ibduResponse.add(iao.getImpressions(), dayssincetakingsurvey);
                        }
                    }
                    response.setImpressionsbyday(ibduResponse.getAsString());

                    //Now update the Response
                    response.setImpressionstotal(response.getImpressionstotal() + iao.getImpressions());
                    response.setImpressionstobepaid(response.getImpressionstobepaid() + impressionsqualifyingforpayment);
                    try{response.save();} catch (GeneralException gex){logger.error(gex);}
                    logger.debug("done with impression.save()");
                }
            }
        }

    }

//    private static int getTotalImpressionsPaidAndToBePaid(Survey survey, User user){
//        Logger logger = Logger.getLogger(ImpressionActivityObjectCollatedStorage.class);
//        if (impressionsbyuser==null){
//            impressionsbyuser = new HashMap<String, Integer>();
//        }
//        String key = "userid"+user.getUserid()+"-surveyid"+survey.getSurveyid();
//        int totimp = 0;
//        if (impressionsbyuser.containsKey(key)){
//            totimp = impressionsbyuser.get(key);
//        } else {
//            totimp = UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);
//        }
//        impressionsbyuser.put(key, totimp);
//        logger.debug("returning totimp="+totimp);
//        return totimp;
//    }

//    private static void addImp(Survey survey, User user, int impressions){
//        if (impressionsbyuser==null){
//            impressionsbyuser = new HashMap<String, Integer>();
//        }
//        String key = "userid"+user.getUserid()+"-surveyid"+survey.getSurveyid();
//        int totimp = 0;
//        if (impressionsbyuser.containsKey(key)){
//            totimp = impressions + impressionsbyuser.get(key);
//            impressionsbyuser.put(key, totimp);
//        } else {
//            totimp = impressions + UserImpressionFinder.getPaidAndToBePaidImpressions(user, survey);
//            impressionsbyuser.put(key, totimp);
//        }
//    }
//
//    public static void resetImpCache(){
//        impressionsbyuser = null;
//    }







}
