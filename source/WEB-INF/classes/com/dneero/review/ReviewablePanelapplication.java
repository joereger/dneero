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

public class ReviewablePanelapplication implements Reviewable {

    public static int TYPE = 6;
    public static String TYPENAME = "SuperPanel Application for Membership";
    public Panelapplication panelapplication;

    public ReviewablePanelapplication(int id){
        if (id>0){
            panelapplication = Panelapplication.get(id);
        }
    }

    public String getTypeDescription(){
        StringBuffer out = new StringBuffer();
        out.append("Application for membership to a Panel.");
        return out.toString();
    }

    public int getId() {
        if (panelapplication!=null){
            return panelapplication.getPanelapplicationid();
        }
        return 0;
    }

    public Date getDate(){
        if (panelapplication!=null){
            return panelapplication.getApplicationdate();
        }
        return new Date();
    }

    public int getType() {
        return TYPE;
    }

    public String getTypeName() {
        return TYPENAME;
    }

    public int getUseridofcontentcreator() {
        if (panelapplication!=null){
            return panelapplication.getUserid();
        }
        return 0;
    }

    public int getUseridofresearcher() {
        return 0;
    }

    public String getShortSummary() {
        StringBuffer out = new StringBuffer();
        User user = User.get(panelapplication.getUserid());
        Panel panel = Panel.get(panelapplication.getPanelid());
        out.append(panel.getName()+" SuperPanel Application: "+Str.truncateString(Str.cleanForHtml(NicknameHelper.getNameOrNickname(user)), 50));
        return out.toString();
    }

    public String getFullSummary() {
        StringBuffer out = new StringBuffer();
        User user = User.get(panelapplication.getUserid());
        Blogger blogger = Blogger.get(user.getBloggerid());
        Panel panel = Panel.get(panelapplication.getPanelid());
        out.append("<font class=\"mediumfont\">");
        out.append("SuperPanel Application for Membership: "+panel.getName());
        out.append("</font>");
        out.append("<br/><br/>");

        out.append("<font class=\"smallfont\">");
        out.append("User: <a href=\"/profile.jsp?userid="+user.getUserid()+"\" target=\"newwindow"+ RandomString.randomAlphabetic(5) +"\">"+user.getFirstname()+" "+user.getLastname()+"</a>");
        out.append("</font>");
        out.append("<br/>");
        out.append("<br/>");

        out.append("<font class=\"smallfont\">");
        out.append("<b>Why they think they're qualified:</b>");
        out.append("<br/>");
        out.append(panelapplication.getApplication());
        out.append("</font>");
        out.append("<br/>");
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
        User user = User.get(panelapplication.getUserid());
        Blogger blogger = Blogger.get(user.getBloggerid());
        panelapplication.setIssysadminrejected(true);
        panelapplication.setIssysadminreviewed(true);
        try{panelapplication.delete();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
    }

    public void approveByResearcher() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
    }

    public void approveBySysadmin() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = User.get(panelapplication.getUserid());
        Blogger blogger = Blogger.get(user.getBloggerid());
        panelapplication.setIssysadminrejected(false);
        panelapplication.setIssysadminreviewed(true);
        try{panelapplication.save();blogger.refresh();}catch(Exception ex){logger.error("", ex);}
        Panelmembership panelmembership = new Panelmembership();
        panelmembership.setBloggerid(blogger.getBloggerid());
        panelmembership.setPanelid(panelapplication.getPanelid());
        panelmembership.setIssysadminrejected(false);
        panelmembership.setIssysadminreviewed(true);
        try{panelmembership.save();}catch(Exception ex){logger.error("", ex);}
    }


    public ArrayList<Reviewable> getPendingForResearcher(int researcherid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("researcherid="+researcherid);
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        return out;
    }

    public ArrayList<Reviewable> getPendingForSysadmin() {
        ArrayList<Reviewable> out = new ArrayList<Reviewable>();
        List<Panelapplication> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Panelapplication.class)
                                           .add(Restrictions.eq("issysadminreviewed", false))
                                           .addOrder(Order.desc("panelapplicationid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Panelapplication> it=objs.iterator(); it.hasNext();) {
            Panelapplication obj = it.next();
            ReviewablePanelapplication reviewable = new ReviewablePanelapplication(obj.getPanelapplicationid());
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
        List<Panelapplication> objs = (ArrayList)HibernateUtil.getSession().createCriteria(Panelapplication.class)
                                           .add(Restrictions.eq("issysadminrejected", true))
                                           .addOrder(Order.desc("panelapplicationid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Panelapplication> it=objs.iterator(); it.hasNext();) {
            Panelapplication obj = it.next();
            ReviewablePanelapplication reviewable = new ReviewablePanelapplication(obj.getPanelapplicationid());
            out.add(reviewable);
        }
        return out;
    }

    public boolean getIsresearcherreviewed() {
        return true;
    }

    public boolean getIssysadminreviewed() {
        if (panelapplication!=null){
            return panelapplication.getIssysadminreviewed();
        }
        return true;
    }

    public boolean getIsresearcherrejected() {
        return false;
    }

    public boolean getIssysadminrejected() {
        if (panelapplication!=null){
            return panelapplication.getIssysadminrejected();
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