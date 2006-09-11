package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.Blog;
import com.dneero.util.Jsf;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyResultsImpressions {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Survey survey;
    private ArrayList<ResearcherSurveyResultsImpressionsObj> researcherSurveyResultsImpressionsObj;


    public ResearcherSurveyResultsImpressions(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        researcherSurveyResultsImpressionsObj = new ArrayList<ResearcherSurveyResultsImpressionsObj>();
        if (survey!=null){
            logger.debug("survey.getImpressions().size()="+survey.getImpressions().size());
            if (survey.canEdit(Jsf.getUserSession().getUser())){
                for (Iterator<Impression> iterator = survey.getImpressions().iterator(); iterator.hasNext();) {
                    Impression impression = iterator.next();
                    ResearcherSurveyResultsImpressionsObj robj = new ResearcherSurveyResultsImpressionsObj();
                    if (impression.getBlog()!=null){
                        robj.setBlogtitle(impression.getBlog().getTitle());
                        robj.setBlogurl(impression.getBlog().getUrl());
                    }
                    robj.setTotalimpressions(impression.getTotalimpressions());
                    robj.setReferer(impression.getReferer());
                    researcherSurveyResultsImpressionsObj.add(robj);
                }
            }
            logger.debug("done loading survey");
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public ArrayList<ResearcherSurveyResultsImpressionsObj> getResearcherSurveyResultsImpressionsObj() {
        return researcherSurveyResultsImpressionsObj;
    }

    public void setResearcherSurveyResultsImpressionsObj(ArrayList<ResearcherSurveyResultsImpressionsObj> researcherSurveyResultsImpressionsObj) {
        this.researcherSurveyResultsImpressionsObj = researcherSurveyResultsImpressionsObj;
    }
}
