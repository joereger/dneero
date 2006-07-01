package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.dao.Survey;
import com.dneero.dao.Researcher;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;
import com.dneero.finders.FindSurveysForBlogger;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class ResearcherSurveyList extends SortableList {

    private Logger logger = Logger.getLogger(UserList.class);
    private List surveys;

    public ResearcherSurveyList() {
        //Default sort column
        super("title");
        //Go get the surveys from the database
        //surveys = HibernateUtil.getSession().createQuery("from Survey").list();

        UserSession userSession = (UserSession) Jsf.getManagedBean("userSession");

        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getResearcher()!=null){
            logger.debug("userSession, user and researcher not null");
            logger.debug("into loop for userSession.getUser().getResearcher().getResearcherid()="+userSession.getUser().getResearcher().getResearcherid());
            surveys = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+userSession.getUser().getResearcher().getResearcherid()+"'").list();
        }


    }

    public List getSurveys() {
        //logger.debug("getSurveys");
        sort(getSort(), isAscending());
        return surveys;
    }

    public void setSurveys(List surveys) {
        //logger.debug("setSurveys");
        this.surveys = surveys;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Survey survey1 = (Survey)o1;
                Survey survey2 = (Survey)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? survey1.getTitle().compareTo(survey2.getTitle()) : survey2.getTitle().compareTo(survey1.getTitle());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (surveys != null && !surveys.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(surveys, comparator);
        }
    }




}
