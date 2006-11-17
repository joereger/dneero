package com.dneero.formbeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

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
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){

                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                   .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                   .list();
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();
                    ResearcherSurveyResultsImpressionsObj robj = new ResearcherSurveyResultsImpressionsObj();
                    if (impression.getBlog()!=null){
                        robj.setBlogtitle(impression.getBlog().getTitle());
                        robj.setBlogurl(impression.getBlog().getUrl());
                    }
                    robj.setImpressionsqualifyingforpayment(impression.getImpressionsqualifyingforpayment());
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
