package com.dneero.htmluibeans;

import com.dneero.twitter.TwitterUpdate;
import com.dneero.util.Time;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.dao.Survey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;


/**
 * User: Joe Reger Jr
 * Date: Sep 21, 2006
 * Time: 8:47:39 AM
 */
public class PublicIndex implements Serializable {

    private Map<String, Survey> spotlightsurveys;
    private Map<String, SurveyEnhancer> spotlightsurveyenhancers;

    public PublicIndex(){
        load();
    }

    public String beginView(){
        return "home";
    }

    private void load(){
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


    public Map<String, Survey> getSpotlightsurveys() {
        return spotlightsurveys;
    }

    public void setSpotlightsurveys(Map<String, Survey> spotlightsurveys) {
        this.spotlightsurveys = spotlightsurveys;
    }

    public Map<String, SurveyEnhancer> getSpotlightsurveyenhancers() {
        return spotlightsurveyenhancers;
    }

    public void setSpotlightsurveyenhancers(Map<String, SurveyEnhancer> spotlightsurveyenhancers) {
        this.spotlightsurveyenhancers = spotlightsurveyenhancers;
    }
}
