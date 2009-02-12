package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;

import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.util.Num;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.helpers.UserInputSafe;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;


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
    private Survey survey;



    public ResearcherSurveyDetail01(){
    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey==null|| survey.getSurveyid()==0){
            beginViewNewSurvey();
        }
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                logger.debug("survey.canEdit(Pagez.getUserSession().getUser())="+survey.canEdit(Pagez.getUserSession().getUser()));
                title = survey.getTitle();
                description = survey.getDescription();
                startdate = survey.getStartdate();
                enddate = survey.getEnddate();
                status = survey.getStatus();
            }
        }
    }





    public void beginViewNewSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginViewNewSurvey() called:");
        Pagez.getUserSession().setCurrentSurveyid(0);
        survey = new Survey();
        title = "";
        survey.setTitle(title);
        description = "";
        survey.setDescription(description);
        startdate = Calendar.getInstance().getTime();
        survey.setStartdate(startdate);
        enddate = Time.AddOneMonth(Calendar.getInstance()).getTime();
        survey.setEnddate(enddate);
        survey.setDneeromarkuppercent(SurveyMoneyStatus.DEFAULTDNEEROMARKUPPERCENT);
        survey.setResellercode("");
        survey.setImpressionstotal(0);
        survey.setImpressionspaid(0);
        survey.setImpressionstobepaid(0);
        survey.setPlid(Pagez.getUserSession().getPl().getPlid());
        survey.setIsaggressiveslotreclamationon(false);
        survey.setEmbedversion(Survey.EMBEDVERSION_01);
    }







    public void saveSurvey() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        logger.debug("status="+status);
        if (Pagez.getUserSession()==null || Pagez.getUserSession().getUser()==null){
            vex.addValidationError("Please log in first.");
            throw vex;
        }
        if (startdate==null){
            logger.debug("startdate is null");
        } else {
            logger.debug("startdate="+Time.dateformatcompactwithtime(Time.getCalFromDate(startdate)));
        }
        logger.debug("title="+title);
        if (status<=Survey.STATUS_DRAFT){

            UserSession userSession = Pagez.getUserSession();

            Survey survey = new Survey();
            survey.setResearcherid(userSession.getUser().getResearcherid());
            survey.setStatus(Survey.STATUS_DRAFT);
            survey.setPublicsurveydisplays(0);
            survey.setIsresultshidden(false);
            survey.setImpressionstotal(0);
            survey.setImpressionspaid(0);
            survey.setImpressionstobepaid(0);
            survey.setIsaggressiveslotreclamationon(false);
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
//                Pagez.getUserSession().setMessage("surveyedit:startdate", "The Start Date must be today or after today.");
//                logger.debug("valdation error - startdate is in past.");
//            }
            if (startdate.after(enddate)){
                isValidData = false;
                vex.addValidationError("The End Date must be after the Start Date.");
                logger.debug("valdation error - startdate is after end date.");
            }

            if (isValidData && Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){

                survey.setResearcherid(userSession.getUser().getResearcherid());
                survey.setTitle(title);
                survey.setDescription(description);
                survey.setStartdate(startdate);
                survey.setEnddate(enddate);

                try{
                    logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                    survey.save();
                    EmbedCacheFlusher.flushCache(survey.getSurveyid());
                    userSession.setCurrentSurveyid(survey.getSurveyid());
                    logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                if (isnewsurvey){
                    //Notify sales group
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "New Conversation Started: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+")");
                    xmpp.send();
                }

                //Refresh
                survey.refresh();

                this.survey = survey;
            } else {
                throw vex;
            }

        }
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

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    
}
