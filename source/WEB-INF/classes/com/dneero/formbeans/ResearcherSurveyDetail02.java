package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Survey;
import com.dneero.session.UserSession;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02 {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private boolean istopon = true;
    private boolean isbottomon = false;
    private int newquestiontype = 0;
    private HashMap questionconfig = new HashMap();

    public ResearcherSurveyDetail02(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentResearcherSurveyDetailSurveyid());
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpSurveyid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researchersurveydetail_02";
    }

    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());


        }

    }

    public String saveSurvey(){
        logger.debug("saveSurvey() called.");

        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentResearcherSurveyDetailSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentResearcherSurveyDetailSurveyid());
            survey = Survey.get(userSession.getCurrentResearcherSurveyDetailSurveyid());
        }

        //survey.setResearcherid(userSession.getUser().getResearcher().getResearcherid());


        try{
            logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
            survey.save();
            logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
        } catch (GeneralException gex){
            logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
            String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }



        //Refresh
        survey.refresh();

        return "success";
    }

    public String addQuestion(){
        logger.debug("addQuestion() called");

        istopon= false;
        isbottomon = true;

        return "researchersurveydetail_02";
    }

    public String saveQuestionConfig(){
        logger.debug("saveQuestionConfig() called");

        istopon= true;
        isbottomon = false;

        if (questionconfig!=null && questionconfig.size()>0){
            logger.debug("questionconfig not null and not empty");
            Iterator keyValuePairs = questionconfig.entrySet().iterator();
            for (int i = 0; i < questionconfig.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String key = (String)mapentry.getKey();
                String value = (String)mapentry.getValue();
                logger.debug("questionconfig-> "+key+"="+value);
            }
        } else {
            logger.debug("questionconfig null or empty");
        }

        return "researchersurveydetail_02";
    }


    public boolean isIstopon() {
        return istopon;
    }

    public void setIstopon(boolean istopon) {
        this.istopon = istopon;
    }

    public boolean isIsbottomon() {
        return isbottomon;
    }

    public void setIsbottomon(boolean isbottomon) {
        this.isbottomon = isbottomon;
    }

    public int getNewquestiontype() {
        return newquestiontype;
    }

    public void setNewquestiontype(int newquestiontype) {
        this.newquestiontype = newquestiontype;
    }

    public HashMap getQuestionconfig() {
        return questionconfig;
    }

    public void setQuestionconfig(HashMap questionconfig) {
        this.questionconfig = questionconfig;
    }


}
