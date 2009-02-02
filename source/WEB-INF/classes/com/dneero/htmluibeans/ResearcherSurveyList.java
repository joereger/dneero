package com.dneero.htmluibeans;

import com.dneero.util.SortableList;

import com.dneero.util.Time;
import com.dneero.dao.Survey;
import com.dneero.dao.Twitask;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.CopyHibernateObject;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class ResearcherSurveyList implements Serializable {

    private List<ResearcherSurveyListitem> surveys;
    private List<ResearcherTwitaskListitem> twitasks;
    private int maxtodisplay = 10000;

    public ResearcherSurveyList() {

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        UserSession userSession = Pagez.getUserSession();
        Pagez.getUserSession().setCurrentSurveyid(0);
        surveys = new ArrayList<ResearcherSurveyListitem>();
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getResearcherid()>0){
            logger.debug("userSession, user and researcher not null");
            logger.debug("into loop for userSession.getUser().getResearcher().getResearcherid()="+userSession.getUser().getResearcherid());
            List srvys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+userSession.getUser().getResearcherid()+"' order by surveyid desc").setMaxResults(maxtodisplay).setCacheable(true).list();
            for (Iterator iterator=srvys.iterator(); iterator.hasNext();) {
                Survey srvy =(Survey) iterator.next();
                ResearcherSurveyListitem rsli = new ResearcherSurveyListitem();
                rsli.setSurvey(srvy);
                if (srvy.getStatus()==Survey.STATUS_DRAFT){
                    rsli.setStatus("Draft");
                } else if (srvy.getStatus()==Survey.STATUS_CLOSED){
                    rsli.setStatus("Closed");
                } if (srvy.getStatus()==Survey.STATUS_OPEN){
                    rsli.setStatus("Live");
                } if (srvy.getStatus()==Survey.STATUS_WAITINGFORFUNDS){
                    rsli.setStatus("Pending, Waiting for Funds");
                } if (srvy.getStatus()==Survey.STATUS_WAITINGFORSTARTDATE){
                    rsli.setStatus("Pending, Waiting for Start Date");
                }
                if (srvy.getStatus()==Survey.STATUS_DRAFT){
                    rsli.setEditorreviewlink("<a href=\"/researcher/researchersurveydetail_01.jsp?surveyid="+srvy.getSurveyid()+"\">Edit</a>");
                } else {
                    rsli.setEditorreviewlink("<a href=\"/researcher/researchersurveydetail_01.jsp?surveyid="+srvy.getSurveyid()+"\">Review</a>");
                }
                if (srvy.getStatus()==Survey.STATUS_OPEN){
                    rsli.setInvitelink("<a href=\"/researcher/emailinvite.jsp?surveyid="+srvy.getSurveyid()+"\">Invite</a>");
                } else {
                    rsli.setInvitelink("");
                }
                if (srvy.getStatus()!=Survey.STATUS_DRAFT){
                    rsli.setResultslink("<a href=\"/researcher/results.jsp?surveyid="+srvy.getSurveyid()+"\">Results</a>");
                } else {
                    rsli.setResultslink("");
                }
                rsli.setCopylink("<a href=\"/researcher/index.jsp?surveyid="+srvy.getSurveyid()+"&action=copy\">Copy</a>");
                if (srvy.getStatus()==Survey.STATUS_DRAFT){
                    rsli.setDeletelink("<a href=\"/researcher/researchersurveydelete.jsp?surveyid="+srvy.getSurveyid()+"\">Delete</a>");
                } else {
                    rsli.setDeletelink("");
                }
                surveys.add(rsli);
            }

            twitasks = new ArrayList<ResearcherTwitaskListitem>();
            List twitaskslist = HibernateUtil.getSession().createQuery("from Twitask where userid='"+userSession.getUser().getUserid()+"' order by twitaskid desc").setMaxResults(maxtodisplay).setCacheable(true).list();
            for (Iterator iterator=twitaskslist.iterator(); iterator.hasNext();) {
                Twitask twitask =(Twitask) iterator.next();
                ResearcherTwitaskListitem rsli = new ResearcherTwitaskListitem();
                rsli.setTwitask(twitask);
                if (twitask.getStatus()==Twitask.STATUS_DRAFT){
                    rsli.setStatus("Draft");
                } else if (twitask.getStatus()==Twitask.STATUS_CLOSED){
                    rsli.setStatus("Closed");
                } else if (twitask.getStatus()==Twitask.STATUS_OPEN){
                    rsli.setStatus("Live");
                } else if (twitask.getStatus()==Twitask.STATUS_WAITINGFORFUNDS){
                    rsli.setStatus("Waiting for Funds");
                } else if (twitask.getStatus()==Twitask.STATUS_WAITINGFORSTARTDATE){
                    rsli.setStatus("Waiting for Start Date");
                } else if (twitask.getStatus()==Twitask.STATUS_WAITINGFORAPPROVAL){
                    rsli.setStatus("Pending Approval");
                } else if (twitask.getStatus()==Twitask.STATUS_REJECTED){
                    rsli.setStatus("Not Approved");
                }
                if (twitask.getStatus()==Survey.STATUS_DRAFT){
                    rsli.setEditorreviewlink("<a href=\"/researcher/researchertwitaskdetail_01.jsp?twitaskid="+twitask.getTwitaskid()+"\">Edit</a>");
                } else {
                    rsli.setEditorreviewlink("<a href=\"/researcher/researchertwitaskdetail_01.jsp?twitaskid="+twitask.getTwitaskid()+"\">Review</a>");
                }
                if (twitask.getStatus()!=Survey.STATUS_DRAFT){
                    rsli.setResultslink("<a href=\"/researcher/resultstwitask.jsp?twitaskid="+twitask.getTwitaskid()+"\">Results</a>");
                    rsli.setResultslink("");
                } else {
                    rsli.setResultslink("");
                }
                rsli.setCopylink("<a href=\"/researcher/index.jsp?surveyid="+twitask.getTwitaskid()+"&action=copy\">Copy</a>");
                if (twitask.getStatus()==Survey.STATUS_DRAFT){
                    rsli.setDeletelink("<a href=\"/researcher/researchertwitaskdelete.jsp?twitaskid="+twitask.getTwitaskid()+"\">Delete</a>");
                } else {
                    rsli.setDeletelink("");
                }
                twitasks.add(rsli);
            }
        }
    }

    


    public List getSurveys() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getListitems");
        return surveys;
    }

    public void setSurveys(List surveys) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setListitems");
        this.surveys = surveys;
    }


    public int getMaxtodisplay() {
        return maxtodisplay;
    }

    public void setMaxtodisplay(int maxtodisplay) {
        this.maxtodisplay=maxtodisplay;
    }

    public List<ResearcherTwitaskListitem> getTwitasks() {
        return twitasks;
    }

    public void setTwitasks(List<ResearcherTwitaskListitem> twitasks) {
        this.twitasks=twitasks;
    }
}
