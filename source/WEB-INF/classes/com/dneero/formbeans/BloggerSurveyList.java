package com.dneero.formbeans;

import com.dneero.util.*;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.session.UserSession;
import com.dneero.finders.FindSurveysForBlogger;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class BloggerSurveyList implements Serializable {

    private ArrayList<BloggerSurveyListItem> surveys;

    public BloggerSurveyList() {



    }

    public String beginView(){
        load();
        return "bloggersurveylist";
    }

    private void load(){

        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("instanciating");
        //Default sort column

        //Go get the surveys from the database
        //surveys = HibernateUtil.getSession().createQuery("from Survey").list();

        UserSession userSession = Jsf.getUserSession();

        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            logger.debug("userSession, user and blogger not null");
            logger.debug("into loop for Blogger.get(userSession.getUser().getBloggerid())="+Blogger.get(userSession.getUser().getBloggerid()));
            FindSurveysForBlogger finder = new FindSurveysForBlogger(Blogger.get(userSession.getUser().getBloggerid()));
            surveys = new ArrayList<BloggerSurveyListItem>();
            for (Iterator iterator = finder.getSurveys().iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();
                boolean loggedinuserhasalreadytakensurvey = false;
                for (Iterator<Response> iterator2 = Blogger.get(userSession.getUser().getBloggerid()).getResponses().iterator(); iterator2.hasNext();) {
                    Response response = iterator2.next();
                    if (response.getSurveyid()==survey.getSurveyid()){
                        loggedinuserhasalreadytakensurvey = true;
                    }
                }
                BloggerSurveyListItem bsli = new BloggerSurveyListItem();
                bsli.setSurveyid(survey.getSurveyid());
                bsli.setTitle(survey.getTitle());
                bsli.setLoggedinuserhasalreadytakensurvey(loggedinuserhasalreadytakensurvey);

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
                if(!loggedinuserhasalreadytakensurvey){
                    surveys.add(bsli);
                }

            }
        }
    }

    public ArrayList<BloggerSurveyListItem> getSurveys() {
        //logger.debug("getListitems");
        //sort("title", true);
        return surveys;
    }

    public void setSurveys(ArrayList<BloggerSurveyListItem> surveys) {
        //logger.debug("setListitems");
        this.surveys = surveys;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
//        //logger.debug("sort called");
//        Comparator comparator = new Comparator() {
//            public int compare(Object o1, Object o2) {
//                BloggerSurveyListItem survey1 = (BloggerSurveyListItem)o1;
//                BloggerSurveyListItem survey2 = (BloggerSurveyListItem)o2;
//                if (column == null) {
//                    return 0;
//                }
//                if (column.equals("title")) {
//                    return ascending ? survey1.getTitle().compareTo(survey2.getTitle()) : survey2.getTitle().compareTo(survey1.getTitle());
//                } else {
//                    return 0;
//                }
//            }
//        };
//
//        //sort and also set our model with the new sort, since using DataTable with
//        //ListDataModel on front end
//        if (surveys != null && !surveys.isEmpty()) {
//            //logger.debug("sorting surveys and initializing ListDataModel");
//            Collections.sort(surveys, comparator);
//        }
    }




}
