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
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail02 implements Serializable {

    private int newquestioncomponenttype = Textbox.ID;
    private String surveyForTakers;
    private int status;
    private String title;


    public ResearcherSurveyDetail02(){

    }

    public String beginView(){
        load();
        return "researchersurveydetail_02";
    }

    private void load(){
        ResearcherSurveyQuestionList bean = (ResearcherSurveyQuestionList)Jsf.getManagedBean("researcherSurveyQuestionList");
        bean.beginView();
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
                surveyForTakers = SurveyTakerDisplay.getHtmlForSurveyTaking(survey, new Blogger(), true);
                status = survey.getStatus();
            }
        }
    }

    public String saveSurveyAsDraft(){
        String save = saveSurvey();
        if (save!=null){
            ResearcherIndex bean = (ResearcherIndex)Jsf.getManagedBean("researcherIndex");
            return bean.beginView();
        } else {
            return save;
        }
    }

    public String previousStep(){
        String save = saveSurvey();
        if (save!=null){
            ResearcherSurveyDetail01 bean = (ResearcherSurveyDetail01)Jsf.getManagedBean("researcherSurveyDetail01");
            return bean.beginView();
            //return "researchersurveydetail_01";
        } else {
            return save;
        }
    }

    public String saveSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        load();
        if (status<=Survey.STATUS_DRAFT){
            UserSession userSession = Jsf.getUserSession();

            Survey survey = new Survey();
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
            }

            if (survey.getQuestions()==null || survey.getQuestions().size()==0){
                Jsf.setFacesMessage("You must add at least one question to continue.");
                return null;   
            }

            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                //survey.setResearcherid(userSession.getUser().getResearcherid());
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

        ResearcherSurveyDetail03 bean = (ResearcherSurveyDetail03)Jsf.getManagedBean("researcherSurveyDetail03");
        return bean.beginView();
        //return "researchersurveydetail_03";
    }

    public String beginEdit(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginEdit() called");
        load();
        int componenttype = 0;
        String tmpComponenttype = Jsf.getRequestParam("componenttype");
        if (com.dneero.util.Num.isinteger(tmpComponenttype)){
            logger.debug("beginEdit called: found componenttype in param="+tmpComponenttype);
            componenttype = Integer.parseInt(tmpComponenttype);
        }

        if (componenttype==Textbox.ID){
            ResearcherSurveyDetail02textbox bean = (ResearcherSurveyDetail02textbox)Jsf.getManagedBean("researcherSurveyDetail02textbox");
            return bean.beginView();
            //return "researchersurveydetail_02_textbox";
        }
        if (componenttype==Essay.ID){
            ResearcherSurveyDetail02essay bean = (ResearcherSurveyDetail02essay)Jsf.getManagedBean("researcherSurveyDetail02essay");
            return bean.beginView();
            //return "researchersurveydetail_02_essay";
        }
        if (componenttype==Dropdown.ID){
            ResearcherSurveyDetail02dropdown bean = (ResearcherSurveyDetail02dropdown)Jsf.getManagedBean("researcherSurveyDetail02dropdown");
            return bean.beginView();
            //return "researchersurveydetail_02_dropdown";
        }
        if (componenttype==Checkboxes.ID){
            ResearcherSurveyDetail02checkboxes bean = (ResearcherSurveyDetail02checkboxes)Jsf.getManagedBean("researcherSurveyDetail02checkboxes");
            return bean.beginView();
            //return "researchersurveydetail_02_checkboxes";
        }
        if (componenttype==Range.ID){
            ResearcherSurveyDetail02range bean = (ResearcherSurveyDetail02range)Jsf.getManagedBean("researcherSurveyDetail02range");
            return bean.beginView();
            //return "researchersurveydetail_02_range";
        }
        if (componenttype==Matrix.ID){
            ResearcherSurveyDetail02matrix bean = (ResearcherSurveyDetail02matrix)Jsf.getManagedBean("researcherSurveyDetail02matrix");
            return bean.beginView();
            //return "researchersurveydetail_02_matrix";
        }

        Jsf.setFacesMessage("Couldn't find componenttype="+componenttype);
        ResearcherSurveyDetail02 bean = (ResearcherSurveyDetail02)Jsf.getManagedBean("researcherSurveyDetail02");
        return bean.beginView();
        //return "researchersurveydetail_02";
    }


    public String addQuestion(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("addQuestion() called");
        load();
        if (newquestioncomponenttype == Textbox.ID){
            ResearcherSurveyDetail02textbox bean = (ResearcherSurveyDetail02textbox)Jsf.getManagedBean("researcherSurveyDetail02textbox");
            return bean.beginView();
            //return "researchersurveydetail_02_textbox";
        }
        if (newquestioncomponenttype == Essay.ID){
            ResearcherSurveyDetail02essay bean = (ResearcherSurveyDetail02essay)Jsf.getManagedBean("researcherSurveyDetail02essay");
            return bean.beginView();
            //return "researchersurveydetail_02_essay";
        }
        if (newquestioncomponenttype == Dropdown.ID){
            ResearcherSurveyDetail02dropdown bean = (ResearcherSurveyDetail02dropdown)Jsf.getManagedBean("researcherSurveyDetail02dropdown");
            return bean.beginView();
            //return "researchersurveydetail_02_dropdown";
        }
        if (newquestioncomponenttype == Checkboxes.ID){
            ResearcherSurveyDetail02checkboxes bean = (ResearcherSurveyDetail02checkboxes)Jsf.getManagedBean("researcherSurveyDetail02checkboxes");
            return bean.beginView();
            //return "researchersurveydetail_02_checkboxes";
        }
        if (newquestioncomponenttype == Range.ID){
            ResearcherSurveyDetail02range bean = (ResearcherSurveyDetail02range)Jsf.getManagedBean("researcherSurveyDetail02range");
            return bean.beginView();
            //return "researchersurveydetail_02_range";
        }
        if (newquestioncomponenttype ==Matrix.ID){
            ResearcherSurveyDetail02matrix bean = (ResearcherSurveyDetail02matrix)Jsf.getManagedBean("researcherSurveyDetail02matrix");
            return bean.beginView();
            //return "researchersurveydetail_02_matrix";
        }

        ResearcherSurveyDetail02textbox bean = (ResearcherSurveyDetail02textbox)Jsf.getManagedBean("researcherSurveyDetail02textbox");
        return bean.beginView();
        //return "researchersurveydetail_02_textbox";
    }

    public String deleteQuestion(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        load();
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

        //survey.setResearcherid(userSession.getUser().getResearcherid());
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question1 = iterator.next();
            if (question1.getQuestionid()==questionid){
                iterator.remove();
            }
        }

        //Have to delete the question tag from the template
        logger.debug("About to update template to remove deleted question from the template. Looking for <$question_"+questionid+"$>");
        if (survey.getTemplate()!=null && survey.getTemplate().indexOf("<$question_"+questionid+"$>")>-1){
            logger.debug("found an instance of <$question_"+questionid+"$>");
            String template = survey.getTemplate();
            template = template.replaceAll("<p><\\$question_"+questionid+"\\$></p>", "");
            template = template.replaceAll("<\\$question_"+questionid+"\\$>", "");
            logger.debug(template);
            survey.setTemplate(template);
        } else {
            if (survey.getTemplate()==null){
                logger.debug("none found, survey.getTemplate() is null");   
            } else {
                logger.debug("none found, survey.getTemplate().indexOf(\"<$question_"+questionid+"$>\")="+survey.getTemplate().indexOf("<$question_"+questionid+"$>"));
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

        ResearcherSurveyDetail02 bean = (ResearcherSurveyDetail02)Jsf.getManagedBean("researcherSurveyDetail02");
        return bean.beginView();
        //return "researchersurveydetail_02";
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
