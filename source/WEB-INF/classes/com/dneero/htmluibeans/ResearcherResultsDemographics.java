package com.dneero.htmluibeans;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.htmlui.Pagez;
import com.dneero.reports.FieldAggregator;
import com.dneero.reports.SimpleTableOutput;
import com.dneero.util.DateDiff;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:40 AM
 */
public class ResearcherResultsDemographics implements Serializable {


    private Survey survey;
    private String html;
    private String whengenerated="";


    public ResearcherResultsDemographics(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        String key = String.valueOf(survey.getSurveyid());
        String group = "SurveyResultsDemographicReport";
        Object obj = CacheFactory.getCacheProvider().get(key, group);
        if (obj!=null && (obj instanceof ResearcherResultsDemographicsCacheitem)){
            logger.debug("found a report in the cache");
            ResearcherResultsDemographicsCacheitem ci = (ResearcherResultsDemographicsCacheitem)obj;
            int minago = DateDiff.dateDiff("minute", Calendar.getInstance(), ci.getLastupdated());
            whengenerated = minago + " minutes ago";
            if (minago==1){
                whengenerated = minago + " minute ago";
            }
            logger.debug("report is "+minago+" minutes old");
            if (minago>60){
                logger.debug("refreshing report");
                refreshReport(key, group);
                whengenerated = "0 minutes ago";
            } else {
                logger.debug("using cached report");
                html = ci.getHtml();
            }
        } else {
            logger.debug("no report in cache");
            refreshReport(key, group);
            whengenerated = "0 minutes ago";
        }
    }

    public void refreshReport(String key, String group){
        ArrayList<Blogger> bloggers = new ArrayList<Blogger>();
        if (survey!=null && survey.getSurveyid()>0){
            for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                Blogger blogger = Blogger.get(response.getBloggerid());
                bloggers.add(blogger);
            }
            if (bloggers.size()>=10){
                FieldAggregator fa = new FieldAggregator((ArrayList)bloggers);
                SimpleTableOutput sto = new SimpleTableOutput(fa);
                html = sto.getHtml();
                ResearcherResultsDemographicsCacheitem ci = new ResearcherResultsDemographicsCacheitem(sto.getHtml(), Calendar.getInstance());
                CacheFactory.getCacheProvider().put(key, group, ci);
            } else {
                html = "Only "+bloggers.size()+" people have joined.  We don't display demographics until at least 10 have joined.  This is done to protect the demographic information of individuals.";
            }
        }
    }



    public Survey getSurvey() {
        return survey;
    }

    public String getHtml() {
        return html;
    }

    public String getWhengenerated() {
        return whengenerated;
    }
}
