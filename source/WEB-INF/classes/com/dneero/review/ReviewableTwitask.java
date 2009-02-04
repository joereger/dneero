package com.dneero.review;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import com.dneero.htmlui.Pagez;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.util.Str;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReviewableTwitask implements Reviewable {

    public static int TYPE = 8;
    public static String TYPENAME = "Twitter Question";
    public Twitask twitask;

    public ReviewableTwitask(int id){
        if (id>0){
            twitask = Twitask.get(id);
        }
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("This is an issue with a Twitter Ask.  If it's been rejected your campaign won't go live until you edit and fix it.");
        return out.toString();
    }

    public int getId() {
        if (twitask!=null){
            return twitask.getTwitaskid();
        }
        return 0;
    }

    public Date getDate(){
        return twitask.getStartdate();
    }

    public int getType() {
        return TYPE;
    }

    public String getTypeName() {
        return TYPENAME;
    }

    public int getUseridofcontentcreator() {
        if (twitask!=null){
            return twitask.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        if (twitask!=null){
            return twitask.getUserid();
        }
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        out.append("Twitter Question: "+Str.truncateString(Str.cleanForHtml(twitask.getQuestion()), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        User user = User.get(twitask.getUserid());
        out.append("<font class=\"mediumfont\">");
        out.append("This is a new Twitter Question campaign.");
        out.append("</font>");
        out.append("<br/><br/>");
        out.append("<font class=\"mediumfont\"><b>");
        out.append("Question: "+Str.cleanForHtml(twitask.getQuestion()));
        out.append("</b></font>");
        out.append("<br/><br/>");
        out.append("<font class=\"tinyfont\"><b>");
        out.append("If you approve this a new Twitter Question campaign will be launched.");
        out.append("</b></font>");
        out.append("<br/><br/>");
        return out.toString();
    }

    public void rejectByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
    }

    public void rejectBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        twitask.setIssysadminrejected(true);
        twitask.setIssysadminreviewed(true);
        twitask.setStatus(Twitask.STATUS_DRAFT);
        try{twitask.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Set to waiting for funds
        twitask.setIssysadminrejected(false);
        twitask.setIssysadminreviewed(true);
        twitask.setStatus(Twitask.STATUS_WAITINGFORFUNDS);
        try{twitask.save();}catch(Exception ex){logger.error("", ex);}
        //Process researcher money
        User user = User.get(twitask.getUserid());
        ResearcherRemainingBalanceOperations.processResearcher(Researcher.get(user.getResearcherid()));
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();

        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Twitask> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Twitask.class)
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .add(Restrictions.eq("status", Twitask.STATUS_WAITINGFORAPPROVAL))
                                           .addOrder(Order.desc("twitaskid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Twitask> it=objs.iterator(); it.hasNext();) {
            Twitask obj = it.next();
            ReviewableTwitask reviewable = new ReviewableTwitask(obj.getTwitaskid());
            out.add(reviewable);
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedByResearcher(int researcherid) {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        return out;
    }

    public ArrayList<Reviewable> getRejectedBySysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Twitask> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Twitask.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("twitaskid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Twitask> it=objs.iterator(); it.hasNext();) {
            Twitask obj = it.next();
            ReviewableTwitask reviewable = new ReviewableTwitask(obj.getTwitaskid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (twitask!=null){
            return twitask.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        return false;
    }

    public boolean getIssysadminrejected() {
        if (twitask!=null){
            return twitask.getIssysadminrejected();
        }
        return false;
    }

    public boolean supportsScoringByResearcher() {
        return false;
    }

    public boolean supportsScoringBySysadmin() {
        return true;
    }

    public void scoreByResearcher(int score) {
        Logger logger = Logger.getLogger(this.getClass().getName());
    }

    public void scoreBySysadmin(int score) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (twitask!=null){
            twitask.setScorebysysadmin(score);
            try{twitask.save();}catch(Exception ex){logger.error("", ex);}
        }
    }

    public boolean isMailCreated() {
        return true;
    }

    public boolean isApproveSupported() {
        return true;
    }

    public boolean isWarnSupported() {
        return false;
    }

    public boolean isRejectSupported() {
        return true;
    }
}