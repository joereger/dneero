package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class ResearcherSurveyList implements Serializable {

    private List surveys;

    public ResearcherSurveyList() {

    }

    public String beginView(){
        load();
        return "researchersurveylist";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        UserSession userSession = Jsf.getUserSession();
        Jsf.getUserSession().setCurrentSurveyid(0);

        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getResearcherid()>0){
            logger.debug("userSession, user and researcher not null");
            logger.debug("into loop for userSession.getUser().getResearcher().getResearcherid()="+userSession.getUser().getResearcherid());
            surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+userSession.getUser().getResearcherid()+"' order by surveyid asc").list();
        }
    }


    public List getSurveys() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getListitems");
        sort("title", true);
        return surveys;
    }

    public void setSurveys(List surveys) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setListitems");
        this.surveys = surveys;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Survey survey1 = (Survey)o1;
                Survey survey2 = (Survey)o2;
                if (column == null) {
                    return 0;
                }
                if (survey1!=null && survey2!=null && column.equals("title")) {
                    return ascending ? survey1.getTitle().compareTo(survey2.getTitle()) : survey2.getTitle().compareTo(survey1.getTitle());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (surveys != null && !surveys.isEmpty()) {
            logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(surveys, comparator);
        }
    }




}
