package com.dneero.htmluibeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Surveydiscuss;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyDiscuss implements Serializable {


    private Survey survey;

    private List<PublicSurveyDiscussListitem> surveydiscusses;
    private String discussSubject="";
    private String discussComment="";


    public PublicSurveyDiscuss(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciated.");

        //Surveyid from session or url
        int surveyid = Pagez.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("s"))) {
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("s"));
        }
        Pagez.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0){
            return;
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            return;
        }


        //Load discussion items
        surveydiscusses = new ArrayList();
        List sds = HibernateUtil.getSession().createQuery("from Surveydiscuss where surveyid='"+survey.getSurveyid()+"' and issysadminrejected=false order by surveydiscussid asc").setCacheable(true).list();
        for (Iterator iterator = sds.iterator(); iterator.hasNext();) {
            Surveydiscuss surveydiscuss = (Surveydiscuss) iterator.next();
            PublicSurveyDiscussListitem psdli = new PublicSurveyDiscussListitem();
            psdli.setSurveydiscuss(surveydiscuss);
            psdli.setUser(User.get(surveydiscuss.getUserid()));
            surveydiscusses.add(psdli);
        }


    }



    public void newComment() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        boolean haveError = false;
        if (discussSubject==null || discussSubject.equals("")){
            haveError = true;
            vex.addValidationError("Oops! Subject is required.");
        }
        if (discussComment==null || discussComment.equals("")){
            haveError = true;
            vex.addValidationError("Oops! Comment is required.");
        }
        if (haveError){
            throw vex;
        }
        if (Pagez.getUserSession().getIsloggedin()){
            if (!doesCommentAlreadyExist(discussSubject, discussComment, survey.getSurveyid())){
                Surveydiscuss surveydiscuss = new Surveydiscuss();
                surveydiscuss.setSurveyid(survey.getSurveyid());
                surveydiscuss.setSubject(discussSubject);
                surveydiscuss.setComment(discussComment);
                surveydiscuss.setDate(new Date());
                surveydiscuss.setUserid(Pagez.getUserSession().getUser().getUserid());
                surveydiscuss.setIsresearcherrejected(false);
                surveydiscuss.setIssysadminrejected(false);
                surveydiscuss.setIsresearcherreviewed(false);
                surveydiscuss.setIssysadminreviewed(false);
                try{
                    surveydiscuss.save();
                } catch (GeneralException gex){
                    vex.addValidationError("Sorry, there was an error: " + gex.getErrorsAsSingleString());
                    logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
                    throw vex;
                }
                //load();
            }
        }
        //Reset
        discussSubject = "";
        discussComment = "";
    }

    private boolean doesCommentAlreadyExist(String subject, String comment, int surveyid){
        List<Surveydiscuss> dscs = HibernateUtil.getSession().createCriteria(Surveydiscuss.class)
                                           .add(Restrictions.eq("subject", subject))
                                           .add(Restrictions.eq("comment", comment))
                                           .add(Restrictions.eq("surveyid", surveyid))
                                           .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                           .setCacheable(true)
                                           .list();
        if (dscs!=null && dscs.size()>0){
            for (Iterator<Surveydiscuss> surveydiscussIterator = dscs.iterator(); surveydiscussIterator.hasNext();) {
                Surveydiscuss surveydiscuss = surveydiscussIterator.next();
                long millisSinceComment = (new Date()).getTime() - surveydiscuss.getDate().getTime();
                if (millisSinceComment < (1000*60*15)){ //Can't post same comment within 15 mins
                    return true;
                }
            }
        }
        return false;
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public List<PublicSurveyDiscussListitem> getSurveydiscusses() {
        return surveydiscusses;
    }

    public void setSurveydiscusses(List<PublicSurveyDiscussListitem> surveydiscusses) {
        this.surveydiscusses=surveydiscusses;
    }

    public String getDiscussSubject() {
        return discussSubject;
    }

    public void setDiscussSubject(String discussSubject) {
        this.discussSubject=discussSubject;
    }

    public String getDiscussComment() {
        return discussComment;
    }

    public void setDiscussComment(String discussComment) {
        this.discussComment=discussComment;
    }
}
