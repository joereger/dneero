package com.dneero.formbeans;

import com.dneero.util.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.session.UserSession;
import com.dneero.finders.FindSurveysForBlogger;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class BloggerSurveyList extends SortableList {

    private Logger logger = Logger.getLogger(BloggerSurveyList.class);
    private ArrayList<BloggerSurveyListItem> surveys;

    public BloggerSurveyList() {
        super("title");
        logger.debug("instanciating BloggerSurveyList");
        //Default sort column

        //Go get the surveys from the database
        //surveys = HibernateUtil.getSession().createQuery("from Survey").list();

        UserSession userSession = Jsf.getUserSession();

        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            logger.debug("userSession, user and blogger not null");
            logger.debug("into loop for userSession.getUser().getBlogger().getBloggerid()="+userSession.getUser().getBlogger().getBloggerid());
            FindSurveysForBlogger finder = new FindSurveysForBlogger(userSession.getUser().getBlogger());
            surveys = new ArrayList<BloggerSurveyListItem>();
            for (Iterator iterator = finder.getSurveys().iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();
                boolean bloggerhasalreadytakensurvey = false;
                for (Iterator<Response> iterator2 = userSession.getUser().getBlogger().getResponses().iterator(); iterator2.hasNext();) {
                    Response response = iterator2.next();
                    if (response.getSurveyid()==survey.getSurveyid()){
                        bloggerhasalreadytakensurvey = true;
                    }
                }
                BloggerSurveyListItem bsli = new BloggerSurveyListItem();
                bsli.setSurveyid(survey.getSurveyid());
                bsli.setTitle(survey.getTitle());
                bsli.setBloggerhasalreadytakensurvey(bloggerhasalreadytakensurvey);

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
                //Only surveys blogger hasn't taken
                if(!bloggerhasalreadytakensurvey){
                    surveys.add(bsli);
                }

            }




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
