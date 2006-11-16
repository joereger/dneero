package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.session.UserSession;
import com.dneero.display.components.*;
import com.dneero.display.SurveyTakerDisplay;

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
    private String surveyForTakers;
    private int status;
    private String title;


    public ResearcherSurveyDetail02(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            title = survey.getTitle();
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger());
                status = survey.getStatus();
            }
        }
    }

    public String saveSurveyAsDraft(){
        String save = saveSurvey();
        if (save!=null && save.equals("success")){
            return "researchersurveylist";
        } else {
            return save;
        }
    }

    public String previousStep(){
        String save = saveSurvey();
        if (save!=null && save.equals("success")){
            return "researchersurveydetail_01";
        } else {
            return save;
        }
    }

    public String saveSurvey(){
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_WAITINGFORSTARTDATE){
            UserSession userSession = Jsf.getUserSession();

            Survey survey = new Survey();
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
            }



            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
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
                loadSurvey(survey.getSurveyid());
            }
        }
        return "success";
    }

    public String beginEdit(){
        logger.debug("beginEdit() called");

        int componenttype = 0;
        String tmpComponenttype = Jsf.getRequestParam("componenttype");
        if (com.dneero.util.Num.isinteger(tmpComponenttype)){
            logger.debug("beginEdit called: found componenttype in param="+tmpComponenttype);
            componenttype = Integer.parseInt(tmpComponenttype);
        }

        if (componenttype==Textbox.ID){
            return "researchersurveydetail_02_textbox";
        }
        if (componenttype==Essay.ID){
            return "researchersurveydetail_02_essay";
        }
        if (componenttype==Dropdown.ID){
            return "researchersurveydetail_02_dropdown";
        }
        if (componenttype==Checkboxes.ID){
            return "researchersurveydetail_02_checkboxes";
        }
        if (componenttype==Range.ID){
            return "researchersurveydetail_02_range";
        }
        if (componenttype==Matrix.ID){
            return "researchersurveydetail_02_matrix";
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
        if (newquestioncomponenttype==Matrix.ID){
            return "researchersurveydetail_02_matrix";
        }

        return "researchersurveydetail_02_textbox";
    }

    public String deleteQuestion(){
        String tmpQuestionid = Jsf.getRequestParam("questionid");
        int questionid = 0;
        if (com.dneero.util.Num.isinteger(tmpQuestionid)){
            logger.debug("deleteQuestion called: found questionid in param="+tmpQuestionid);
            questionid = Integer.parseInt(tmpQuestionid);
        }


        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
            survey = Survey.get(userSession.getCurrentSurveyid());
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

    public String getSurveyForTakers() {
        return surveyForTakers;
    }

    public void setSurveyForTakers(String surveyForTakers) {
        this.surveyForTakers = surveyForTakers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
