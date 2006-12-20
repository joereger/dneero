package com.dneero.formbeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.money.SurveyMoneyStatus;

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
    private int maxsurveyresponses = 0;
    private int maxsurveydisplays = 0;
    private int totalsurveydisplays = 0;
    private int status = 0;
    private double spenttodate = 0;
    private double maxpossiblespend = 0;

    public ResearcherResults(){
        logger.debug("Instanciating object. Jsf.getUserSession().getCurrentSurveyid()="+Jsf.getUserSession().getCurrentSurveyid());
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }

    public String beginView(){
        logger.debug("beginView called:");
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
        logger.debug("loadSurvey called... surveyid="+surveyid);
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                try{
                    this.survey=survey;
                    SurveyMoneyStatus sms = new SurveyMoneyStatus(survey);
                    spenttodate = sms.getSpentToDateIncludingdNeeroFee();
                    maxpossiblespend = sms.getMaxPossibleSpend();
                    totalsurveyresponses = survey.getResponses().size();
                    maxsurveyresponses = survey.getNumberofrespondentsrequested();
                    maxsurveydisplays = survey.getMaxdisplaystotal();
                    totalsurveydisplays = 0;
                    status = survey.getStatus();
                    totalsurveydisplays = sms.getImpressionsToDate();
                } catch (Exception ex){
                    logger.error(ex);
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


    public double getSpenttodate() {
        return spenttodate;
    }

    public void setSpenttodate(double spenttodate) {
        this.spenttodate = spenttodate;
    }


    public double getMaxpossiblespend() {
        return maxpossiblespend;
    }

    public void setMaxpossiblespend(double maxpossiblespend) {
        this.maxpossiblespend = maxpossiblespend;
    }

    public int getMaxsurveyresponses() {
        return maxsurveyresponses;
    }

    public void setMaxsurveyresponses(int maxsurveyresponses) {
        this.maxsurveyresponses = maxsurveyresponses;
    }

    public int getMaxsurveydisplays() {
        return maxsurveydisplays;
    }

    public void setMaxsurveydisplays(int maxsurveydisplays) {
        this.maxsurveydisplays = maxsurveydisplays;
    }
}
