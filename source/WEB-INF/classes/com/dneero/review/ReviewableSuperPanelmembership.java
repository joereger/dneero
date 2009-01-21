package com.dneero.review;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.survey.servlet.SurveyAsHtml;
import com.dneero.util.Str;
import com.dneero.util.RandomString;
import com.dneero.helpers.NicknameHelper;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Disjunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReviewableSuperPanelmembership implements Reviewable {

    public static int TYPE = 5;
    public static String TYPENAME = "SuperPanel Membership";
    public Panelmembership panelmembership;

    public ReviewableSuperPanelmembership(int id){
        if (id>0){
            panelmembership = Panelmembership.get(id);
        }
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("Somebody wants you on a SuperPanel.");
        return out.toString();
    }

    public int getId() {
        if (panelmembership!=null){
            return panelmembership.getPanelmembershipid();
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
        if (panelmembership!=null){
            Blogger blogger = Blogger.get(panelmembership.getBloggerid());
            return blogger.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        Blogger blogger = Blogger.get(panelmembership.getBloggerid());
        User user = User.get(blogger.getUserid());
        out.append("SuperPanel Membership: "+Str.truncateString(Str.cleanForHtml(NicknameHelper.getNameOrNickname(user)), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        Blogger blogger = Blogger.get(panelmembership.getBloggerid());
        User user = User.get(blogger.getUserid());
        out.append("<font class=\"mediumfont\">");
        out.append("SuperPanel Membership:");
        out.append("</font>");
        out.append("<br/><br/>");

        out.append("<font class=\"smallfont\">");
        out.append("User: <a href=\"/profile.jsp?userid="+user.getUserid()+"\" target=\"newwindow"+ RandomString.randomAlphabetic(5) +"\">"+user.getFirstname()+" "+user.getLastname()+"</a>");
        out.append("</font>");
        out.append("<br/>");

        for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
            Venue venue=iterator.next();
            if (venue.getIsactive()){
                String sysadminrejected = "";
                if (venue.getIssysadminrejected()){
                    sysadminrejected = "<img src=\"/images/alert-16.png\" alt=\"Rejected\" border=\"0\">";
                } else {
                    if (venue.getIssysadminreviewed() && !venue.getIssysadminrejected()){
                        sysadminrejected = "<img src=\"/images/ok-16.png\" alt=\"Approved\" border=\"0\">";
                    }
                }
                out.append("<font class=\"formfieldnamefont\">");
                out.append("<a href=\"http://"+venue.getUrl()+"\" target=\"newwindow"+ RandomString.randomAlphabetic(5) +"\">");
                out.append("http://"+venue.getUrl());
                out.append("</a>");
                out.append(sysadminrejected);
                out.append("</font>");
                out.append("<br/>");
                out.append("<font class=\"tinyfont\">Venue Focus: "+venue.getFocus()+"</font>");
                out.append("<br/>");
            }
        }

        out.append("<font class=\"smallfont\">");
        out.append("Click the link to review in a new window.");
        out.append("</font>");
        out.append("<br/><br/>");

        return out.toString();
    }

    public void rejectByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
    }

    public void rejectBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Blogger blogger = Blogger.get(panelmembership.getBloggerid());
        panelmembership.setIssysadminrejected(true);
        panelmembership.setIssysadminreviewed(true);
        try{panelmembership.delete();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Blogger blogger = Blogger.get(panelmembership.getBloggerid());
        panelmembership.setIssysadminrejected(false);
        panelmembership.setIssysadminreviewed(true);
        try{panelmembership.save();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Panelmembership> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Panelmembership.class)
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .addOrder(Order.desc("panelmembershipid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Panelmembership> it=objs.iterator(); it.hasNext();) {
            Panelmembership obj = it.next();
            ReviewableSuperPanelmembership reviewable = new ReviewableSuperPanelmembership(obj.getPanelmembershipid());
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
        List<Panelmembership> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Panelmembership.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("panelmembershipid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Panelmembership> it=objs.iterator(); it.hasNext();) {
            Panelmembership obj = it.next();
            ReviewableSuperPanelmembership reviewable = new ReviewableSuperPanelmembership(obj.getPanelmembershipid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (panelmembership!=null){
            return panelmembership.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        return false;
    }

    public boolean getIssysadminrejected() {
        if (panelmembership!=null){
            return panelmembership.getIssysadminrejected();
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
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public boolean isMailCreated() {
        return false;
    }
}