package com.dneero.review;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReviewableResponse implements Reviewable {

    public static int TYPE = 2;
    public static String TYPENAME = "Conversation Response";
    public Response response;

    public ReviewableResponse(int id){
        if (id>0){
            response = Response.get(id);
        }
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("This is an issue with the content you've added to the conversation.  Answers must be relevant to the conversation and required questions must be answered thoughtfully. If your content is flagged you will not accrue impressions or be able to earn any money for this conversation until it is fixed.  Warnings do not affect earnings but should be fixed.  This sort of issue can be resolved by editing the question that you added to the conversation.  Do this by editing your conversation answers.  Once you save your edits the content will automatically be placed into the queue for approval.");
        return out.toString();
    }

    public int getId() {
        if (response!=null){
            return response.getResponseid();
        }
        return 0;
    }

    public Date getDate(){
        return response.getResponsedate();
    }

    public int getType() {
        return TYPE;
    }

    public String getTypeName() {
        return TYPENAME;
    }

    public int getUseridofcontentcreator() {
        if (response!=null){
            Blogger blogger = Blogger.get(response.getBloggerid());
            return blogger.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        if (response!=null){
            Survey survey = Survey.get(response.getSurveyid());
            Researcher researcher = Researcher.get(survey.getResearcherid());
            return researcher.getUserid();
        }
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        Survey survey = Survey.get(response.getSurveyid());
        out.append("Response to: "+Str.truncateString(Str.cleanForHtml(survey.getTitle()), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        Survey survey = Survey.get(response.getSurveyid());
        Blogger blogger = Blogger.get(response.getBloggerid());
        User user = User.get(blogger.getUserid());
        out.append("<font class=\"mediumfont\">");
        out.append("This is a response to a survey.");
        out.append("</font>");
        out.append("<br/>");
        out.append("<font class=\"tinyfont\"><b>");
        out.append("From Survey: "+Str.cleanForHtml(survey.getTitle()));
        out.append("</b></font>");
        out.append("<br/>");
        out.append("<style>");
        out.append(".questiontitle{");
        out.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 13px; font-weight: bold; margin: 0px; border: 0px solid #8d8d8d; padding: 0px; text-align: left; background: #e6e6e6;");
        out.append("}");
        out.append(".answer{");
        out.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; margin: 0px;  padding: 0px; text-align: left;");
        out.append("}");
        out.append(".answer_highlight{");
        out.append("font-family: Arial, Arial, Helvetica, sans-serif; font-size: 11px; width: 95%; font-weight: bold; border: 0px solid #c1c1c1; margin: 0px;  padding: 0px; text-align: left; background: #ffffff;");
        out.append("}");
        out.append("</style>");
        out.append(SurveyAsHtml.getHtml(survey, user, true, true));
        return out.toString();
    }

    public void rejectByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        response.setIsresearcherrejected(true);
        response.setIsresearcherreviewed(true);
        try{response.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void rejectBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        response.setIssysadminrejected(true);
        response.setIssysadminreviewed(true);
        try{response.save();}catch(Exception ex){logger.error("", ex);}
        EmbedCacheFlusher.flushCache(response.getSurveyid(), getUseridofcontentcreator());
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        response.setIsresearcherrejected(false);
        response.setIsresearcherreviewed(true);
        try{response.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        response.setIssysadminrejected(false);
        response.setIssysadminreviewed(true);
        try{response.save();}catch(Exception ex){logger.error("", ex);}
        EmbedCacheFlusher.flushCache(response.getSurveyid(), getUseridofcontentcreator());
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //@todo optimize with a single hql call
        List<Response> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Response.class)
                                   .add(Restrictions.eq("isresearcherreviewed", false))
                                   .addOrder(Order.desc("responseid"))
                                   .setCacheable(true)
                                   .list();
        for (Iterator<Response> it=objs.iterator(); it.hasNext();) {
            Response obj = it.next();
            Survey survey = Survey.get(obj.getSurveyid());
            logger.debug("surveyid="+survey.getSurveyid()+" survey.getResearcherid()="+survey.getResearcherid()+" researcherid="+researcherid);
            if (survey.getResearcherid()==researcherid){
                ReviewableResponse reviewable = new ReviewableResponse(obj.getResponseid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Response> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("isresearcherreviewed", true))
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                           .addOrder(Order.desc("responseid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Response> it=objs.iterator(); it.hasNext();) {
            Response obj = it.next();
            ReviewableResponse reviewable = new ReviewableResponse(obj.getResponseid());
            out.add(reviewable);
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedByResearcher(int researcherid) {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //@todo optimize with a single hql call
        List<Response> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                           .addOrder(Order.desc("responseid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Response> it=objs.iterator(); it.hasNext();) {
            Response obj = it.next();
            Survey survey = Survey.get(obj.getSurveyid());
            if (survey.getResearcherid()==researcherid){
                ReviewableResponse reviewable = new ReviewableResponse(obj.getResponseid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedBySysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Response> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("responseid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Response> it=objs.iterator(); it.hasNext();) {
            Response obj = it.next();
            ReviewableResponse reviewable = new ReviewableResponse(obj.getResponseid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        if (response!=null){
            return response.getIsresearcherreviewed();
        }
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (response!=null){
            return response.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        if (response!=null){
            return response.getIsresearcherrejected();
        }
        return false;
    }

    public boolean getIssysadminrejected() {
        if (response!=null){
            return response.getIssysadminrejected();
        }
        return false;
    }
}