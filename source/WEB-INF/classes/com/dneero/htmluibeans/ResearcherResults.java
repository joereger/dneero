package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Survey;
import com.dneero.dao.Impression;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.util.Jsf;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.money.SurveyMoneyStatus;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherResults implements Serializable {

    private Survey survey;
    private int totalsurveyresponses = 0;
    private int maxsurveyresponses = 0;
    private int maxsurveydisplays = 0;
    private int totalsurveydisplays = 0;
    private int status = 0;
    private double spenttodate = 0;
    private double maxpossiblespend = 0;

    public ResearcherResults(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        Survey survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }

        if (survey!=null){
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                try{
                    this.survey=survey;
                    SurveyMoneyStatus sms = new SurveyMoneyStatus(survey);
                    spenttodate = sms.getSpentToDateIncludingdNeeroFee();
                    maxpossiblespend = sms.getMaxPossibleSpend();
                    totalsurveyresponses = survey.getResponses().size();
                    maxsurveyresponses = survey.getNumberofrespondentsrequested();
                    maxsurveydisplays = survey.getMaxdisplaystotal();
                    totalsurveydisplays = NumFromUniqueResult.getInt("select sum(impressionstotal) from Impression where surveyid='"+survey.getSurveyid()+"'");
                    status = survey.getStatus();
                    totalsurveydisplays = sms.getImpressionsToDate();
                } catch (Exception ex){
                    logger.error("",ex);
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
