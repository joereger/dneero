package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

import com.dneero.finders.FindSurveysForBlogger;

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

    private ArrayList<SurveyListItem> surveys;

    public PublicOldSurveyList() {

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("instanciating PublicSurveyList");
        //If user is logged-in only show them their surveys
//        if (Pagez.getUserSession().getIsloggedin()){
//
//
//        //Otherwise, get all open surveys
//        } else {


//            FindSurveysForBlogger fsfb = null;
//            if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
//                fsfb = new FindSurveysForBlogger(Blogger.get(Pagez.getUserSession().getUser().getBloggerid()));
//            }


            surveys = new ArrayList<SurveyListItem>();
            List results = HibernateUtil.getSession().createQuery("from Survey where status='"+ Survey.STATUS_CLOSED+"' order by enddate desc").list();
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();

                SurveyListItem bsli = new SurveyListItem();
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

    public ArrayList<SurveyListItem> getSurveys() {
        //logger.debug("getListitems");
        return surveys;
    }

    public void setSurveys(ArrayList<SurveyListItem> surveys) {
        //logger.debug("setListitems");
        this.surveys = surveys;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }






}
