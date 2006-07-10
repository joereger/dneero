package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.session.UserSession;
import com.dneero.display.components.*;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02 {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private int newquestioncomponenttype;


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

    public String beginEdit(){
        logger.debug("beginEdit() called");

        int componenttype = 0;
        String tmpComponenttype = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("componenttype");
        if (com.dneero.util.Num.isinteger(tmpComponenttype)){
            logger.debug("beginEdit called: found componenttype in param="+tmpComponenttype);
            componenttype = Integer.parseInt(tmpComponenttype);
        }

        if (componenttype== Textbox.ID){
            return "researchersurveydetail_02_textbox";
        }
        if (componenttype== Essay.ID){
            return "researchersurveydetail_02_essay";
        }
        if (componenttype== Dropdown.ID){
            return "researchersurveydetail_02_dropdown";
        }
        if (componenttype== Checkboxes.ID){
            return "researchersurveydetail_02_checkboxes";
        }
        if (componenttype== Range.ID){
            return "researchersurveydetail_02_range";
        }

        Jsf.setFacesMessage("Couldn't find componenttype="+componenttype);
        return "researchersurveydetail_02";
    }


    public String addQuestion(){
        logger.debug("addQuestion() called");

        if (newquestioncomponenttype== Textbox.ID){
            return "researchersurveydetail_02_textbox";
        }
        if (newquestioncomponenttype== Essay.ID){
            return "researchersurveydetail_02_essay";
        }
        if (newquestioncomponenttype== Dropdown.ID){
            return "researchersurveydetail_02_dropdown";
        }
        if (newquestioncomponenttype== Checkboxes.ID){
            return "researchersurveydetail_02_checkboxes";
        }
        if (newquestioncomponenttype== Range.ID){
            return "researchersurveydetail_02_range";
        }

        return "researchersurveydetail_02_textbox";
    }

    public String deleteQuestion(){
        String tmpQuestionid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("questionid");
        int questionid = 0;
        if (com.dneero.util.Num.isinteger(tmpQuestionid)){
            logger.debug("deleteQuestion called: found questionid in param="+tmpQuestionid);
            questionid = Integer.parseInt(tmpQuestionid);
        }


        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentResearcherSurveyDetailSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentResearcherSurveyDetailSurveyid());
            survey = Survey.get(userSession.getCurrentResearcherSurveyDetailSurveyid());
        }

        //survey.setResearcherid(userSession.getUser().getResearcher().getResearcherid());
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question1 = iterator.next();
            if (question1.getQuestionid()==questionid){
                iterator.remove();
            }
        }

        try{
            logger.debug("deleteQuestion() about to save survey.getSurveyid()=" + survey.getSurveyid());
            survey.save();
            logger.debug("deleteQuestion() done saving survey.getSurveyid()=" + survey.getSurveyid());
        } catch (GeneralException gex){
            logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
            String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        //Refresh
        survey.refresh();

        //Reset list
        ResearcherSurveyQuestionList rsql = (ResearcherSurveyQuestionList)Jsf.getManagedBean("researcherSurveyQuestionList");
        rsql.load();

        return "researchersurveydetail_02";
    }

    public int getNewquestioncomponenttype() {
        return newquestioncomponenttype;
    }

    public void setNewquestioncomponenttype(int newquestioncomponenttype) {
        this.newquestioncomponenttype = newquestioncomponenttype;
    }


}
