package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.*;
import com.dneero.dao.Survey;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetailPostlaunch implements Serializable {


    private String title;
    private int status;
    private String startdate;
    private String enddate;
    private String maxpossiblespend = "0";
    private String initialcharge = "0";
    private String willingtopayperrespondent = "0";



    public ResearcherSurveyDetailPostlaunch(){

    }

    public String beginView(){
        load();
        return "researchersurveydetail_postlaunch";
    }

    private void load(){
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }



    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            title = survey.getTitle();
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                status = survey.getStatus();

                startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getStartdate()));
                enddate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getEnddate()));
                willingtopayperrespondent = "$"+Str.formatForMoney(survey.getWillingtopayperrespondent());

                SurveyMoneyStatus sms = new SurveyMoneyStatus(survey);
                maxpossiblespend = "$"+Str.formatForMoney(sms.getMaxPossibleSpend());
                initialcharge = "$"+Str.formatForMoney(sms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100));




            }
        }

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getMaxpossiblespend() {
        return maxpossiblespend;
    }

    public void setMaxpossiblespend(String maxpossiblespend) {
        this.maxpossiblespend = maxpossiblespend;
    }


    public String getInitialcharge() {
        return initialcharge;
    }

    public void setInitialcharge(String initialcharge) {
        this.initialcharge = initialcharge;
    }


    public String getWillingtopayperrespondent() {
        return willingtopayperrespondent;
    }

    public void setWillingtopayperrespondent(String willingtopayperrespondent) {
        this.willingtopayperrespondent = willingtopayperrespondent;
    }
}
