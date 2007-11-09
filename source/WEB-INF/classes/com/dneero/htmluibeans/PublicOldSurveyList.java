package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import com.dneero.dao.Blogger;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class PublicOldSurveyList implements Serializable {

    private ArrayList<BloggerSurveyListItem> surveys;

    public PublicOldSurveyList() {

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("instanciating PublicSurveyList");
        //If user is logged-in only show them their surveys
//        if (Pagez.getUserSession().getIsloggedin()){
//
//            BloggerSurveyList bsl = new BloggerSurveyList();
//            surveys = bsl.getSurveys();
//
//        //Otherwise, get all open surveys
//        } else {


            FindSurveysForBlogger fsfb = null;
            if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                fsfb = new FindSurveysForBlogger(Blogger.get(Pagez.getUserSession().getUser().getBloggerid()));
            }


            surveys = new ArrayList<BloggerSurveyListItem>();
            List results = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_CLOSED+"' order by enddate desc").list();
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();

                BloggerSurveyListItem bsli = new BloggerSurveyListItem();
                bsli.setSurveyid(survey.getSurveyid());
                bsli.setTitle(survey.getTitle());
                bsli.setDescription(survey.getDescription());
                bsli.setNumberofrespondents(survey.getResponses().size());

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

                //See if user has taken survey
//                bsli.setLoggedinuserhasalreadytakensurvey(false);
//                if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
//                    Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
//                    for (Iterator<Response> iterator2 = blogger.getResponses().iterator(); iterator2.hasNext();) {
//                        Response response = iterator2.next();
//                        if (response.getSurveyid()==survey.getSurveyid()){
//                            bsli.setLoggedinuserhasalreadytakensurvey(true);
//                        }
//                    }
//                }

                //See if user is qualified
//                if (!bsli.getLoggedinuserhasalreadytakensurvey()){
//                    if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
//                        //Iterate surveys this blogger qualifies for
//                        boolean bloggerqualifies = false;
//                        for (Iterator iter = fsfb.getSurveys().iterator(); iter.hasNext();) {
//                            Survey tmpSurvey = (Survey) iter.next();
//                            if (tmpSurvey.getSurveyid()==survey.getSurveyid()){
//                                bloggerqualifies=true;
//                            }
//                        }
//                        if (bloggerqualifies){
//                            bsli.setIsbloggerqualifiedstring("Yes");
//                        } else {
//                            bsli.setIsbloggerqualifiedstring("No");
//                        }
//                    } else {
//                        bsli.setIsbloggerqualifiedstring("Unknown");
//                    }
//                } else {
//                    bsli.setIsbloggerqualifiedstring("Already Taken");
//                }


                surveys.add(bsli);
            }

        //}
    }

    public ArrayList<BloggerSurveyListItem> getSurveys() {
        //logger.debug("getListitems");
        sort("title", true);
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
