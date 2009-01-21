package com.dneero.review;

import com.dneero.dao.Researcher;
import com.dneero.dao.Survey;
import com.dneero.dao.Surveydiscuss;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

public class ReviewableSurveydiscuss implements Reviewable {

    public static int TYPE = 1;
    public static String TYPENAME = "Conversation Comment";
    public Surveydiscuss surveydiscuss;

    public ReviewableSurveydiscuss(int id){
        if (id>0){
            surveydiscuss =Surveydiscuss.get(id);
        }
    }

    public int getId() {
        if (surveydiscuss!=null){
            return surveydiscuss.getSurveydiscussid();
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
        return surveydiscuss.getDate();
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("This is an issue a comment made on a survey.  There is no remedy for this.  Content is simply removed.");
        return out.toString();
    }

    public int getUseridofcontentcreator() {
        if (surveydiscuss!=null){
            return surveydiscuss.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        if (surveydiscuss!=null){
            Survey survey = Survey.get(surveydiscuss.getSurveyid());
            Researcher researcher = Researcher.get(survey.getResearcherid());
            return researcher.getUserid();
        }
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        out.append("Survey Comment: "+Str.truncateString(Str.cleanForHtml(surveydiscuss.getSubject()), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        Survey survey = Survey.get(surveydiscuss.getSurveyid());
        out.append("<font class=\"mediumfont\">");
        out.append("This is a comment made on a survey discussion.");
        out.append("</font>");
        out.append("<br/>");
        out.append("<font class=\"smallfont\"><b>");
        out.append("Subject: "+Str.cleanForHtml(surveydiscuss.getSubject()));
        out.append("</b></font>");
        out.append("<br/>");
        out.append("<font class=\"tinyfont\"><b>");
        out.append("From Survey: "+Str.cleanForHtml(survey.getTitle()));
        out.append("</b></font>");
        out.append("<br/>");
        out.append(Str.cleanForHtml(surveydiscuss.getComment()));
        return out.toString();
    }

    public void rejectByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        surveydiscuss.setIsresearcherrejected(true);
        surveydiscuss.setIsresearcherreviewed(true);
        try{surveydiscuss.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void rejectBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        surveydiscuss.setIssysadminrejected(true);
        surveydiscuss.setIssysadminreviewed(true);
        try{surveydiscuss.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        surveydiscuss.setIsresearcherrejected(false);
        surveydiscuss.setIsresearcherreviewed(true);
        try{surveydiscuss.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        surveydiscuss.setIssysadminrejected(false);
        surveydiscuss.setIssysadminreviewed(true);
        try{surveydiscuss.save();}catch(Exception ex){logger.error("", ex);}
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //@todo optimize with a single hql call
        List<Surveydiscuss> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Surveydiscuss.class)
                                   .add(Restrictions.eq("isresearcherreviewed", false))
                                   .addOrder(Order.desc("surveydiscussid"))
                                   .setCacheable(true)
                                   .list();
        for (Iterator<Surveydiscuss> it=objs.iterator(); it.hasNext();) {
            Surveydiscuss obj = it.next();
            Survey survey = Survey.get(obj.getSurveyid());
            logger.debug("surveyid="+survey.getSurveyid()+" survey.getResearcherid()="+survey.getResearcherid()+" researcherid="+researcherid);
            if (survey.getResearcherid()==researcherid){
                ReviewableSurveydiscuss reviewable = new ReviewableSurveydiscuss(obj.getSurveydiscussid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Surveydiscuss> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Surveydiscuss.class)
                                           .add(Restrictions.eq("isresearcherreviewed", true))
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                           .addOrder(Order.desc("surveydiscussid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Surveydiscuss> it=objs.iterator(); it.hasNext();) {
            Surveydiscuss obj = it.next();
            ReviewableSurveydiscuss reviewable = new ReviewableSurveydiscuss(obj.getSurveydiscussid());
            out.add(reviewable);
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedByResearcher(int researcherid) {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //@todo optimize with a single hql call
        List<Surveydiscuss> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Surveydiscuss.class)
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                           .addOrder(Order.desc("surveydiscussid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Surveydiscuss> it=objs.iterator(); it.hasNext();) {
            Surveydiscuss obj = it.next();
            Survey survey = Survey.get(obj.getSurveyid());
            if (survey.getResearcherid()==researcherid){
                ReviewableSurveydiscuss reviewable = new ReviewableSurveydiscuss(obj.getSurveydiscussid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedBySysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Surveydiscuss> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Surveydiscuss.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("surveydiscussid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Surveydiscuss> it=objs.iterator(); it.hasNext();) {
            Surveydiscuss obj = it.next();
            ReviewableSurveydiscuss reviewable = new ReviewableSurveydiscuss(obj.getSurveydiscussid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        if (surveydiscuss!=null){
            return surveydiscuss.getIsresearcherreviewed();
        }
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (surveydiscuss!=null){
            return surveydiscuss.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        if (surveydiscuss!=null){
            return surveydiscuss.getIsresearcherrejected();
        }
        return false;
    }

    public boolean getIssysadminrejected() {
        if (surveydiscuss!=null){
            return surveydiscuss.getIssysadminrejected();
        }
        return false;
    }

    public boolean supportsScoringByResearcher() {
        return false;
    }

    public boolean supportsScoringBySysadmin() {
        return false;
    }

    public void scoreByResearcher(int score) {

    }

    public void scoreBySysadmin(int score) {

    }

    public boolean isMailCreated() {
        return true;
    }
}
