package com.dneero.formbeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherResults {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Survey survey;
    private int totalsurveyresponses = 0;
    private int totalsurveydisplays = 0;
    private int status = 0;

    public ResearcherResults(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            UserSession userSession = Jsf.getUserSession();
            userSession.setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researcherresults";
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                this.survey=survey;
                totalsurveyresponses = survey.getResponses().size();
                totalsurveydisplays = 0;
                status = survey.getStatus();
                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                   .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                   .list();
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();
                    totalsurveydisplays = totalsurveydisplays + impression.getImpressionsqualifyingforpayment();
                }
            }
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public int getTotalsurveyresponses() {
        return totalsurveyresponses;
    }

    public void setTotalsurveyresponses(int totalsurveyresponses) {
        this.totalsurveyresponses = totalsurveyresponses;
    }

    public int getTotalsurveydisplays() {
        return totalsurveydisplays;
    }

    public void setTotalsurveydisplays(int totalsurveydisplays) {
        this.totalsurveydisplays = totalsurveydisplays;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
