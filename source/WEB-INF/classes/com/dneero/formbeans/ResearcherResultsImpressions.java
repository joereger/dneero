package com.dneero.formbeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:40 AM
 */
public class ResearcherResultsImpressions implements Serializable {


    private Survey survey;
    private ArrayList<ResearcherResultsImpressionsListitem> researcherResultsImpressionsListitems;


    public ResearcherResultsImpressions(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        survey = Survey.get(surveyid);
        researcherResultsImpressionsListitems = new ArrayList<ResearcherResultsImpressionsListitem>();
        if (survey!=null){
            logger.debug("survey.getImpressions().size()="+survey.getImpressions().size());
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                   .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                   .list();
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();
                    ResearcherResultsImpressionsListitem robj = new ResearcherResultsImpressionsListitem();
                    robj.setImpressionid(impression.getImpressionid());
                    robj.setImpressionsqualifyingforpayment(impression.getImpressionsqualifyingforpayment());
                    robj.setReferer(impression.getReferer());
                    if (impression.getBlog()!=null){
                        robj.setBlogtitle(impression.getBlog().getTitle());
                        robj.setBlogurl(impression.getBlog().getUrl());
                    }
                    researcherResultsImpressionsListitems.add(robj);
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


    public ArrayList<ResearcherResultsImpressionsListitem> getResearcherResultsImpressionsListitems() {
        return researcherResultsImpressionsListitems;
    }

    public void setResearcherResultsImpressionsListitems(ArrayList<ResearcherResultsImpressionsListitem> researcherResultsImpressionsListitems) {
        this.researcherResultsImpressionsListitems = researcherResultsImpressionsListitems;
    }
}
