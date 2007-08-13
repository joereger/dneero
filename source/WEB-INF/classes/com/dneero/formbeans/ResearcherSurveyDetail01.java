package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.session.UserSession;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.helpers.UserInputSafe;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail01 implements Serializable {

    private String title;
    private String description;
    private Date startdate;
    private Date enddate;
    private int status;


    public ResearcherSurveyDetail01(){
        //@todo Refactor Non-Empty Constructor
        load();
    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView() called:");
        load();
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            UserSession userSession = Jsf.getUserSession();
            userSession.setCurrentSurveyid(Integer.parseInt(tmpSurveyid));
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researchersurveydetail_01";
    }

    private void load(){
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }



    public String beginViewNewSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginViewNewSurvey() called:");
        Jsf.getUserSession().setCurrentSurveyid(0);
        title = "";
        description = "";
        startdate = Calendar.getInstance().getTime();
        enddate = Time.AddOneMonth(Calendar.getInstance()).getTime();
        return "researchersurveydetail_01";
    }



    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called for surveyid="+surveyid);
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                logger.debug("survey.canEdit(Jsf.getUserSession().getUser())="+survey.canEdit(Jsf.getUserSession().getUser()));
                title = survey.getTitle();
                description = survey.getDescription();
                startdate = survey.getStartdate();
                enddate = survey.getEnddate();
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

    public String saveSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        logger.debug("status="+status);
        if (Jsf.getUserSession()==null || Jsf.getUserSession().getUser()==null){
            return null;   
        }
        if (startdate==null){
            logger.debug("startdate is null");
        } else {
            logger.debug("startdate="+Time.dateformatcompactwithtime(Time.getCalFromDate(startdate)));
        }
        logger.debug("title="+title);
        if (status<=Survey.STATUS_DRAFT){

            UserSession userSession = Jsf.getUserSession();

            Survey survey = new Survey();
            survey.setResearcherid(userSession.getUser().getResearcherid());
            survey.setStatus(Survey.STATUS_DRAFT);
            survey.setPublicsurveydisplays(0);
            survey.setIsresultshidden(false);
            boolean isnewsurvey = true;
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
                isnewsurvey = false;
            }

            //Validation
            boolean isValidData = true;
//            Calendar beforeMinusDay = Time.xDaysAgoStart(Calendar.getInstance(), 0);
//            if (startdate.before(beforeMinusDay.getTime())){
//                isValidData = false;
//                Jsf.setFacesMessage("surveyedit:startdate", "The Start Date must be today or after today.");
//                logger.debug("valdation error - startdate is in past.");
//            }
            if (startdate.after(enddate)){
                isValidData = false;
                Jsf.setFacesMessage("surveyedit:enddate", "The End Date must be after the Start Date.");
                logger.debug("valdation error - startdate is after end date.");
            }

            if (isValidData && Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){

                survey.setResearcherid(userSession.getUser().getResearcherid());
                survey.setTitle(UserInputSafe.clean(title));
                survey.setDescription(UserInputSafe.clean(description));
                survey.setStartdate(startdate);
                survey.setEnddate(enddate);

                try{
                    logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                    survey.save();
                    userSession.setCurrentSurveyid(survey.getSurveyid());
                    logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }

                if (isnewsurvey){
                    //Notify sales group
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "New dNeero Survey Started: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+")");
                    xmpp.send();
                }

                //Refresh
                survey.refresh();
            } else {
                return null;
            }

        }

        ResearcherSurveyDetail02 bean = (ResearcherSurveyDetail02)Jsf.getManagedBean("researcherSurveyDetail02");
        return bean.beginView();
        //return "researchersurveydetail_02";
    }

    public String getTitle() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setTitle() called:"+title);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setStartdate() called: "+Time.dateformatcompactwithtime(Time.getCalFromDate(startdate)));
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
