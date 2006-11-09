package com.dneero.formbeans;

import com.dneero.util.*;
import com.dneero.session.UserSession;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class PublicSurveyList extends SortableList {

    private Logger logger = Logger.getLogger(PublicSurveyList.class);
    private ArrayList<BloggerSurveyListItem> surveys;

    public PublicSurveyList() {
        super("title");
        logger.debug("instanciating PublicSurveyList");
        //Default sort column

        //Go get the surveys from the database
        //surveys = HibernateUtil.getSession().createQuery("from Survey").list();

        //Can't get to this list if you're logged in because this list isn't specific to you
        if (Jsf.getUserSession().getIsloggedin()){
            try{Jsf.redirectResponse("/index.jsf"); return;} catch (Exception ex){logger.error(ex);};
        }


        surveys = new ArrayList<BloggerSurveyListItem>();
        List results = HibernateUtil.getSession().createQuery("from Survey where status='"+Survey.STATUS_OPEN+"' order by willingtopayperrespondent desc").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Survey survey = (Survey) iterator.next();

            BloggerSurveyListItem bsli = new BloggerSurveyListItem();
            bsli.setSurveyid(survey.getSurveyid());
            bsli.setTitle(survey.getTitle());
            bsli.setBloggerhasalreadytakensurvey(false);

            if (survey.getQuestions()!=null){
                bsli.setNumberofquestions(String.valueOf(survey.getQuestions().size()));
            } else {
                bsli.setNumberofquestions("0");
            }

            double maxearningNum = survey.getWillingtopayperrespondent()  +   ( (survey.getWillingtopaypercpm()*survey.getMaxdisplaysperblog())/1000 );
            bsli.setMaxearning("$"+ Str.formatForMoney(maxearningNum));

            int daysleft = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getEnddate()), Calendar.getInstance());
            if (daysleft==0){
                bsli.setDaysuntilend("Ends today!");
            } else if (daysleft==1){
                bsli.setDaysuntilend("One day left!");
            } else {
                bsli.setDaysuntilend(daysleft + " days left!");
            }

            surveys.add(bsli);
        }





    }

    public ArrayList<BloggerSurveyListItem> getSurveys() {
        //logger.debug("getSurveys");
        sort(getSort(), isAscending());
        return surveys;
    }

    public void setSurveys(ArrayList<BloggerSurveyListItem> surveys) {
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
                BloggerSurveyListItem survey1 = (BloggerSurveyListItem)o1;
                BloggerSurveyListItem survey2 = (BloggerSurveyListItem)o2;
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
