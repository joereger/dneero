package com.dneero.htmluibeans;

import com.dneero.dao.Survey;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Time;
import com.dneero.xmpp.SendXMPPMessage;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

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
    private int embedversion = Survey.EMBEDVERSION_02;
    private Survey survey;
    private boolean isopentoanybody=true;
    private boolean isfree=true;
    private boolean isuserrequiredtoaddquestion=false;
    private boolean isanonymousresponseallowed=false;
    private String customvar1;
    private String customvar2;
    private String customvar3;



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
                embedversion = survey.getEmbedversion();
                isfree = survey.getIsfree();
                isopentoanybody = survey.getIsopentoanybody();
                customvar1 = survey.getCustomvar1();
                customvar2 = survey.getCustomvar2();
                customvar3 = survey.getCustomvar3();
                isuserrequiredtoaddquestion = survey.getIsuserrequiredtoaddquestion();
                logger.debug("initbean() isuserrequiredtoaddquestion="+isuserrequiredtoaddquestion);
                logger.debug("initbean() isfree="+isfree);
                logger.debug("initbean() isopentoanybody="+isopentoanybody);

            }
            if (survey!=null && survey.getSurveyid()>0 && !survey.canEdit(Pagez.getUserSession().getUser())){
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            }
        }
        //Pull title/description from home page
        if (survey!=null && survey.getSurveyid()==0){
            if (Pagez.getUserSession().getSurveyTitleFromHomepage()!=null){
                title = Pagez.getUserSession().getSurveyTitleFromHomepage();
                Pagez.getUserSession().setSurveyTitleFromHomepage("");
            }
            if (Pagez.getUserSession().getSurveyDescriptionFromHomepage()!=null){
                description = Pagez.getUserSession().getSurveyDescriptionFromHomepage();
                Pagez.getUserSession().setSurveyDescriptionFromHomepage("");
            }
        }
        //Anon
        isanonymousresponseallowed =  Pagez.getUserSession().getPl().getIsanonymousresponseallowed();
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
        survey.setEmbedversion(Survey.EMBEDVERSION_02);
        survey.setEmbedflash(true);
        survey.setEmbedjavascript(true);
        survey.setEmbedlink(true);
        survey.setIsfree(false);
        survey.setIsopentoanybody(false);
        survey.setIshighquality(false);
        survey.setIsfree(true);
        survey.setIsopentoanybody(true);
        survey.setIshiddenfromhomepage(false);
        survey.setIsuserrequiredtoaddquestion(true);
        survey.setIsanonymousresponseallowed(false);
        survey.setCustomvar1("");
        survey.setCustomvar2("");
        survey.setCustomvar3("");
    }


    public void editLaunched() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Pagez.getUserSession().getIsEditLaunchedSurveys()){
            survey.setStatus(Survey.STATUS_DRAFT);
            try{
                survey.save();
                EmbedCacheFlusher.flushCache(survey.getSurveyid());
            } catch (GeneralException gex){
                logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString()); 
            }
        }
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
            survey.setPlid(Pagez.getUserSession().getPl().getPlid());
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

            if (isanonymousresponseallowed && !isopentoanybody){
                isValidData = false;
                vex.addValidationError("Surveys that permit anonymous responses can't limit who can participate.");
                logger.debug("valdation error - isanonymousresponseallowed && !isopentoanybody");
                isopentoanybody = false;
            }

            if (isanonymousresponseallowed && !isfree){
                isValidData = false;
                vex.addValidationError("Surveys that permit anonymous responses can't provide incentives to participants.");
                logger.debug("valdation error - isanonymousresponseallowed && !isfree");
                isfree = false;
            }

            if (isValidData && Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){

                survey.setResearcherid(userSession.getUser().getResearcherid());
                survey.setTitle(title);
                survey.setDescription(description);
                survey.setStartdate(startdate);
                survey.setEnddate(enddate);
                survey.setEmbedversion(embedversion);
                survey.setIsfree(isfree);
                survey.setIsopentoanybody(isopentoanybody);
                survey.setIsuserrequiredtoaddquestion(isuserrequiredtoaddquestion);
                survey.setIsanonymousresponseallowed(isanonymousresponseallowed);
                survey.setCustomvar1(customvar1);
                survey.setCustomvar2(customvar2);
                survey.setCustomvar3(customvar3);
                logger.debug("saveSurvey() isuserrequiredtoaddquestion="+isuserrequiredtoaddquestion);
                logger.debug("saveSurvey() isfree="+isfree);
                logger.debug("saveSurvey() isopentoanybody="+isopentoanybody);
                logger.debug("saveSurvey() isanonymousresponseallowed="+isanonymousresponseallowed);
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
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "New Survey Started: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+")");
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

    public TreeMap<String, String> getEmbedVersions(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put(String.valueOf(Survey.EMBEDVERSION_01), "EMBEDVERSION_01 (Old School)");
        out.put(String.valueOf(Survey.EMBEDVERSION_02), "EMBEDVERSION_02 (Recommended)");
        return out;
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

    public int getEmbedversion() {
        return embedversion;
    }

    public void setEmbedversion(int embedversion) {
        this.embedversion=embedversion;
    }

    public boolean getIsopentoanybody() {
        return isopentoanybody;
    }

    public void setIsopentoanybody(boolean isopentoanybody) {
        this.isopentoanybody = isopentoanybody;
    }

    public boolean getIsfree() {
        return isfree;
    }

    public void setIsfree(boolean isfree) {
        this.isfree = isfree;
    }

    public boolean getIsuserrequiredtoaddquestion() {
        return isuserrequiredtoaddquestion;
    }

    public void setIsuserrequiredtoaddquestion(boolean isuserrequiredtoaddquestion) {
        this.isuserrequiredtoaddquestion = isuserrequiredtoaddquestion;
    }

    public boolean getIsanonymousresponseallowed() {
        return isanonymousresponseallowed;
    }

    public void setIsanonymousresponseallowed(boolean isanonymousresponseallowed) {
        this.isanonymousresponseallowed = isanonymousresponseallowed;
    }


    public String getCustomvar1() {
        return customvar1;
    }

    public void setCustomvar1(String customvar1) {
        this.customvar1 = customvar1;
    }

    public String getCustomvar2() {
        return customvar2;
    }

    public void setCustomvar2(String customvar2) {
        this.customvar2 = customvar2;
    }

    public String getCustomvar3() {
        return customvar3;
    }

    public void setCustomvar3(String customvar3) {
        this.customvar3 = customvar3;
    }
}
