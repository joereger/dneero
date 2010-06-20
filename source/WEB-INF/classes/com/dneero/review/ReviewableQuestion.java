package com.dneero.review;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReviewableQuestion implements Reviewable {

    public static int TYPE = 3;
    public static String TYPENAME = "User Question";
    public Question question;

    public ReviewableQuestion(int id){
        if (id>0){
            question = Question.get(id);
        }
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("This is an issue with a question added.  Questions must be relevant to the topic at hand.   If your content is flagged you will not accrue impressions until it is fixed. This sort of issue can be resolved by editing the question that you added.  Do this by editing your answers... on that screen you'll also be able to edit your question.  Once you edit the content will automatically be placed into the queue for approval.");
        return out.toString();
    }

    public int getId() {
        if (question!=null){
            return question.getQuestionid();
        }
        return 0;
    }

    public int getType() {
        return TYPE;
    }

    public String getTypeName() {
        return TYPENAME;
    }

    public Date getDate(){
        if (question.getIsuserquestion() && question.getUserid()>0){
            User user = User.get(question.getUserid());
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                    .add(Restrictions.eq("bloggerid", user.getBloggerid()))
                    .add(Restrictions.eq("surveyid", question.getSurveyid()))
                    .setCacheable(true)
                    .list();
            for (Iterator<Response> responseIterator=responses.iterator(); responseIterator.hasNext();) {
                Response response=responseIterator.next();
                return response.getResponsedate();
            }
        } else {
            Survey survey = Survey.get(question.getSurveyid());
            return survey.getStartdate();
        }
        return new Date();
    }

    public int getUseridofcontentcreator() {
        if (question!=null){
            if (question.getUserid()>0){
                return question.getUserid();
            }
            Survey survey = Survey.get(question.getSurveyid());
            Researcher researcher = Researcher.get(survey.getResearcherid());
            return researcher.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        if (question!=null){
            Survey survey = Survey.get(question.getSurveyid());
            Researcher researcher = Researcher.get(survey.getResearcherid());
            return researcher.getUserid();
        }
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        out.append("Question: "+Str.truncateString(Str.cleanForHtml(question.getQuestion()), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        Survey survey = Survey.get(question.getSurveyid());
        out.append("<font class=\"mediumfont\">");
        out.append("This is a question for a survey.");
        out.append("</font>");
        out.append("<br/>");
        out.append("<font class=\"tinyfont\"><b>");
        out.append("From Survey: "+Str.cleanForHtml(survey.getTitle()));
        out.append("</b></font>");
        out.append("<br/>");
        Component component = ComponentTypes.getComponentByType(question.getComponenttype(), question, new Blogger());
        out.append(component.getHtmlForInput(null));
        return out.toString();
    }

    public void rejectByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        question.setIsresearcherrejected(true);
        question.setIsresearcherreviewed(true);
        try{question.save();}catch(Exception ex){logger.error("", ex);}
        //Also reject the response
        if (question.getIsuserquestion() && question.getUserid()>0){
            User user = User.get(question.getUserid());
            List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                    .add(Restrictions.eq("bloggerid", user.getBloggerid()))
                    .add(Restrictions.eq("surveyid", question.getSurveyid()))
                    .setCacheable(true)
                    .list();
            for (Iterator<Response> responseIterator=responses.iterator(); responseIterator.hasNext();) {
                Response response=responseIterator.next();
                response.setIssysadminrejected(true);
                try{response.save();}catch(Exception ex){logger.error("", ex);}
            }
        }
    }

    public void rejectBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        question.setIssysadminrejected(true);
        question.setIssysadminreviewed(true);
        try{question.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        question.setIsresearcherrejected(false);
        question.setIsresearcherreviewed(true);
        try{question.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        question.setIssysadminrejected(false);
        question.setIssysadminreviewed(true);
        try{question.save();}catch(Exception ex){logger.error("", ex);}
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //@todo optimize with a single hql call
        List<Question> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Question.class)
                                   .add(Restrictions.eq("isresearcherreviewed", false))
                                   .addOrder(Order.desc("questionid"))
                                   .setCacheable(true)
                                   .list();
        for (Iterator<Question> it=objs.iterator(); it.hasNext();) {
            Question obj = it.next();
            Survey survey = Survey.get(obj.getSurveyid());
            logger.debug("surveyid="+survey.getSurveyid()+" survey.getResearcherid()="+survey.getResearcherid()+" researcherid="+researcherid);
            if (survey.getResearcherid()==researcherid){
                ReviewableQuestion reviewable = new ReviewableQuestion(obj.getQuestionid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Question> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Question.class)
                                           .add(Restrictions.eq("isresearcherreviewed", true))
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                           .addOrder(Order.desc("questionid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Question> it=objs.iterator(); it.hasNext();) {
            Question obj = it.next();
            ReviewableQuestion reviewable = new ReviewableQuestion(obj.getQuestionid());
            out.add(reviewable);
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedByResearcher(int researcherid) {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //@todo optimize with a single hql call
        List<Question> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Question.class)
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                           .addOrder(Order.desc("questionid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Question> it=objs.iterator(); it.hasNext();) {
            Question obj = it.next();
            Survey survey = Survey.get(obj.getSurveyid());
            if (survey.getResearcherid()==researcherid){
                ReviewableQuestion reviewable = new ReviewableQuestion(obj.getQuestionid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedBySysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Question> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Question.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("questionid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Question> it=objs.iterator(); it.hasNext();) {
            Question obj = it.next();
            ReviewableQuestion reviewable = new ReviewableQuestion(obj.getQuestionid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        if (question!=null){
            return question.getIsresearcherreviewed();
        }
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (question!=null){
            return question.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        if (question!=null){
            return question.getIsresearcherrejected();
        }
        return false;
    }

    public boolean getIssysadminrejected() {
        if (question!=null){
            return question.getIssysadminrejected();
        }
        return false;
    }


    public boolean supportsScoringByResearcher() {
        return true;
    }

    public boolean supportsScoringBySysadmin() {
        return true;
    }

    public void scoreByResearcher(int score) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (question!=null){
            question.setScorebyresearcher(score);
            try{question.save();}catch(Exception ex){logger.error("", ex);}
        }
    }

    public void scoreBySysadmin(int score) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (question!=null){
            question.setScorebysysadmin(score);
            try{question.save();}catch(Exception ex){logger.error("", ex);}
        }
    }

    public boolean isMailCreated() {
        return true;
    }

    public boolean isApproveSupported() {
        return true;
    }

    public boolean isWarnSupported() {
        return true;
    }

    public boolean isRejectSupported() {
        return true;
    }
}