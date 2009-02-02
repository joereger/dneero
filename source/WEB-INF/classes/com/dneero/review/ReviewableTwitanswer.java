package com.dneero.review;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
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

public class ReviewableTwitanswer implements Reviewable {

    public static int TYPE = 7;
    public static String TYPENAME = "Twitter Answer";
    public Twitanswer twitanswer;
                               
    public ReviewableTwitanswer(int id){
        if (id>0){
            twitanswer = Twitanswer.get(id);
        }
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("This is an issue with a Twitter Answer.");
        return out.toString();
    }

    public int getId() {
        if (twitanswer!=null){
            return twitanswer.getTwitanswerid();
        }
        return 0;
    }

    public Date getDate(){
        return twitanswer.getResponsedate();
    }

    public int getType() {
        return TYPE;
    }

    public String getTypeName() {
        return TYPENAME;
    }

    public int getUseridofcontentcreator() {
        if (twitanswer!=null){
            return twitanswer.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        if (twitanswer!=null){
            Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
            return twitask.getUserid();
        }
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
        out.append("Twitter Answer to: "+Str.truncateString(Str.cleanForHtml(twitask.getQuestion()), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
        User user = User.get(twitanswer.getUserid());
        out.append("<font class=\"mediumfont\">");
        out.append("This is a Twitter Answer.");
        out.append("</font>");
        out.append("<br/>");
        out.append("<font class=\"mediumfont\"><b>");
        out.append("Question: "+Str.cleanForHtml(twitask.getQuestion()));
        out.append("<br/>");
        out.append("Answer: "+Str.cleanForHtml(twitanswer.getAnswer()));
        out.append("</b></font>");
        return out.toString();
    }

    public void rejectByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        twitanswer.setIsresearcherrejected(true);
        twitanswer.setIsresearcherreviewed(true);
        twitanswer.setIssysadminreviewed(false);
        try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void rejectBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        twitanswer.setIssysadminrejected(true);
        twitanswer.setIssysadminreviewed(true);
        twitanswer.setStatus(Twitanswer.STATUS_REJECTED);
        try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        twitanswer.setIsresearcherrejected(false);
        twitanswer.setIsresearcherreviewed(true);
        try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Twitask twitask = Twitask.get(twitanswer.getTwitaskid());
        //See if it's too late
        boolean istoolate = false;
        int respondentssofar = NumFromUniqueResult.getInt("select count(*) from Twitanswer where twitaskid='"+twitask.getTwitaskid()+"' and status='"+Twitanswer.STATUS_APPROVED+"'");
        if (respondentssofar>=twitask.getNumberofrespondentsrequested()){
            istoolate = true;
        }
        //Record approval
        twitanswer.setIssysadminrejected(false);
        twitanswer.setIssysadminreviewed(true);
        twitanswer.setStatus(Twitanswer.STATUS_APPROVED);
        if (istoolate){
            twitanswer.setStatus(Twitanswer.STATUS_TOOLATE);
        }
        try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        Researcher researcher = Researcher.get(researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        //@todo optimize with a single hql call
        List<Twitanswer> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                   .add(Restrictions.eq("isresearcherreviewed", false))
                                   .add(Restrictions.eq("status", Twitanswer.STATUS_PENDINGREVIEW))
                                   .addOrder(Order.desc("twitanswerid"))
                                   .setCacheable(true)
                                   .list();
        for (Iterator<Twitanswer> it=objs.iterator(); it.hasNext();) {
            Twitanswer obj = it.next();
            Twitask twitask = Twitask.get(obj.getTwitaskid());
            if (twitask.getUserid()==researcher.getUserid()){
                ReviewableTwitanswer reviewable = new ReviewableTwitanswer(obj.getTwitanswerid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Twitanswer> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .add(Restrictions.eq("status", Twitanswer.STATUS_PENDINGREVIEW))
                                           .addOrder(Order.desc("twitanswerid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Twitanswer> it=objs.iterator(); it.hasNext();) {
            Twitanswer obj = it.next();
            ReviewableTwitanswer reviewable = new ReviewableTwitanswer(obj.getTwitanswerid());
            out.add(reviewable);
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedByResearcher(int researcherid) {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        Researcher researcher = Researcher.get(researcherid);
        //@todo optimize with a single hql call
        List<Twitanswer> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                           .add(Restrictions.eq("isresearcherrejected", true))
                                           .addOrder(Order.desc("twitanswerid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Twitanswer> it=objs.iterator(); it.hasNext();) {
            Twitanswer obj = it.next();
            Twitask twitask = Twitask.get(obj.getTwitaskid());
            if (twitask.getUserid()==researcher.getUserid()){
                ReviewableTwitanswer reviewable = new ReviewableTwitanswer(obj.getTwitanswerid());
                out.add(reviewable);
            }
        }
        return out;
    }

    public ArrayList<Reviewable> getRejectedBySysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Twitanswer> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Twitanswer.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("twitanswerid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Twitanswer> it=objs.iterator(); it.hasNext();) {
            Twitanswer obj = it.next();
            ReviewableTwitanswer reviewable = new ReviewableTwitanswer(obj.getTwitanswerid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        if (twitanswer!=null){
            return twitanswer.getIsresearcherreviewed();
        }
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (twitanswer!=null){
            return twitanswer.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        if (twitanswer!=null){
            return twitanswer.getIsresearcherrejected();
        }
        return false;
    }

    public boolean getIssysadminrejected() {
        if (twitanswer!=null){
            return twitanswer.getIssysadminrejected();
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
        if (twitanswer!=null){
            twitanswer.setScorebyresearcher(score);
            try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
        }
    }

    public void scoreBySysadmin(int score) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (twitanswer!=null){
            twitanswer.setScorebysysadmin(score);
            try{twitanswer.save();}catch(Exception ex){logger.error("", ex);}
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