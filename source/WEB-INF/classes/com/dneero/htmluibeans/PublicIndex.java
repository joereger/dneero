package com.dneero.htmluibeans;

import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import com.dneero.htmlui.Pagez;
import com.dneero.cache.providers.CacheFactory;
import com.dneero.cachedstuff.RecentSurveyResponses;
import com.dneero.cachedstuff.GetCachedStuff;

import java.io.Serializable;
import java.util.*;

import org.hibernate.criterion.Restrictions;


/**
 * User: Joe Reger Jr
 * Date: Sep 21, 2006
 * Time: 8:47:39 AM
 */
public class PublicIndex implements Serializable {


    public PublicIndex(){

    }



    public void initBean(){


        


    }

    public void enterAccessCode() throws ValidationException {
        if (Pagez.getUserSession().getAccesscode()!=null && !Pagez.getUserSession().getAccesscode().equals("")){
            List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                               .add(Restrictions.eq("isaccesscodeonly", true))
                                               .add(Restrictions.eq("accesscode", Pagez.getUserSession().getAccesscode()))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Survey> iterator=surveys.iterator(); iterator.hasNext();) {
                Survey survey=iterator.next();
                if (survey.getIsaccesscodeonly()){
                    if (survey.getAccesscode().equals(Pagez.getUserSession().getAccesscode())){
                        Pagez.sendRedirect("/survey.jsp?surveyid="+survey.getSurveyid());
                        return;
                    }
                }
            }
        }
    }

    public void refreshSpotlightSurveys(){
        Map<String, Survey> spotlightsurveys;
        Map<String, SurveyEnhancer> spotlightsurveyenhancers;
        spotlightsurveys = new HashMap<String, Survey>();
        int[] spotlightsurveyids = com.dneero.scheduledjobs.SystemStats.getSpotlightsurveys();
        for (int i = 0; i < spotlightsurveyids.length; i++) {
            int surveyid = spotlightsurveyids[i];
            if (surveyid>0){
                Survey survey = Survey.get(surveyid);
                spotlightsurveys.put(String.valueOf(i), survey);
            } else {
                spotlightsurveys.put(String.valueOf(i), new Survey());
            }
        }
        spotlightsurveyenhancers = com.dneero.scheduledjobs.SystemStats.getSpotlightsurveyenhancers();
    }



}
