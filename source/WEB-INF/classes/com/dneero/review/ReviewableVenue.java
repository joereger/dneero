package com.dneero.review;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.util.Str;
import com.dneero.util.RandomString;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Disjunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReviewableVenue implements Reviewable {

    public static int TYPE = 4;
    public static String TYPENAME = "Posting Venue";
    public Venue venue;

    public ReviewableVenue(int id){
        if (id>0){
            venue = Venue.get(id);
        }
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("There is an issue with the posting venue that you've specified. To fix this you need to specify a posting venue that fulfills the terms of the EULA.  Do this by logging in and editing your profile/demographic information.");
        return out.toString();
    }

    public int getId() {
        if (venue!=null){
            return venue.getVenueid();
        }
        return 0;
    }

    public Date getDate(){
        return new Date();
    }

    public int getType() {
        return TYPE;
    }

    public String getTypeName() {
        return TYPENAME;
    }

    public int getUseridofcontentcreator() {
        if (venue!=null){
            Blogger blogger = Blogger.get(venue.getBloggerid());
            return blogger.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        out.append("Posting Venue: "+Str.truncateString(Str.cleanForHtml(venue.getUrl()), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        Blogger blogger = Blogger.get(venue.getBloggerid());
        User user = User.get(blogger.getUserid());
        out.append("<font class=\"mediumfont\">");
        out.append("Posting Venue:");
        out.append("</font>");
        out.append("<br/><br/>");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("<a href=\"http://"+venue.getUrl()+"\" target=\"newwindow"+ RandomString.randomAlphabetic(5) +"\">");
        out.append("http://"+venue.getUrl());
        out.append("</a>");
        out.append("</font>");
        out.append("<br/>");
        out.append("<font class=\"smallfont\">");
        out.append("Click the link to review in a new window.");
        out.append("</font>");
        out.append("<br/><br/>");
        out.append("<font class=\"smallfont\">");
        out.append("Added by: "+user.getFirstname()+" "+user.getLastname());
        out.append("</font>");
        return out.toString();
    }

    public void rejectByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Blogger blogger = Blogger.get(venue.getBloggerid());
        venue.setIsresearcherrejected(true);
        venue.setIsresearcherreviewed(true);
        try{venue.save();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
    }

    public void rejectBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("rejecting venueid="+venue.getVenueid()+" by sysadmin");
        Blogger blogger = Blogger.get(venue.getBloggerid());
        venue.setIssysadminrejected(true);
        venue.setIssysadminreviewed(true);
        venue.setIsdueforreview(false);
        try{venue.save();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Blogger blogger = Blogger.get(venue.getBloggerid());
        venue.setIsresearcherrejected(false);
        venue.setIsresearcherreviewed(true);
        try{venue.save();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Blogger blogger = Blogger.get(venue.getBloggerid());
        venue.setIssysadminrejected(false);
        venue.setIssysadminreviewed(true);
        venue.setIsdueforreview(false);
        try{venue.save();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Venue> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Venue.class)
                                           .add( Restrictions.disjunction()
                                                .add(Restrictions.eq("issysadminreviewed", false))
                                                .add(Restrictions.eq("isdueforreview", true))
                                           )
                                           .add(Restrictions.eq("isactive", true))
                                           .addOrder(Order.desc("venueid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Venue> it=objs.iterator(); it.hasNext();) {
            Venue obj = it.next();
            ReviewableVenue reviewable = new ReviewableVenue(obj.getVenueid());
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
        List<Venue> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Venue.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("venueid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Venue> it=objs.iterator(); it.hasNext();) {
            Venue obj = it.next();
            ReviewableVenue reviewable = new ReviewableVenue(obj.getVenueid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        if (venue!=null){
            return venue.getIsresearcherreviewed();
        }
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (venue!=null){
            return venue.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        if (venue!=null){
            return venue.getIsresearcherrejected();
        }
        return false;
    }

    public boolean getIssysadminrejected() {
        if (venue!=null){
            return venue.getIssysadminrejected();
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

    }

    public void scoreBySysadmin(int score) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (venue!=null){
            Blogger blogger = Blogger.get(venue.getBloggerid());
            venue.setScorebysysadmin(score);
            try{venue.save();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
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