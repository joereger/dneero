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
public class ResearcherTwitaskDetail01 implements Serializable {

    private String question;
    private Date startdate;
    private int status;
    private Twitask twitask;



    public ResearcherTwitaskDetail01(){
    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get((Integer.parseInt(Pagez.getRequest().getParameter("twitaskid"))));
        }
        if (twitask==null || twitask.getTwitaskid()==0){
            beginViewNewSurvey();
        }
        if (twitask!=null){
            logger.debug("Found twitask in db: twitask.getTwitaskid()="+twitask.getTwitaskid()+" twitask.getQuestion()="+twitask.getQuestion());
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){
                logger.debug("twitask.canEdit(Pagez.getUserSession().getUser())="+twitask.canEdit(Pagez.getUserSession().getUser()));
                question = twitask.getQuestion();
                startdate = twitask.getStartdate();
                status = twitask.getStatus();
            }
        }
    }





    public void beginViewNewSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginViewNewSurvey() called:");
        Pagez.getUserSession().setCurrentSurveyid(0);
        twitask = new Twitask();
        question = "";
        twitask.setQuestion(question);
        startdate = Calendar.getInstance().getTime();
        twitask.setStartdate(startdate);
        twitask.setSenttotwitterdate(Time.xYearsAgoEnd(Calendar.getInstance(), 20).getTime());
        twitask.setClosedintwitterdate(Time.xYearsAgoEnd(Calendar.getInstance(), 20).getTime());
        twitask.setDneeromarkuppercent(SurveyMoneyStatus.DEFAULTDNEEROMARKUPPERCENT);
        twitask.setResellercode("");
        twitask.setPlid(Pagez.getUserSession().getPl().getPlid());
        twitask.setCharitycustom("");
        twitask.setIscharityonly(false);
        twitask.setCharityonlyallowcustom(false);
        twitask.setCriteriaxml("");
        twitask.setScorebysysadmin(0);
    }







    public void save() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("save() called.");
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
        if (status<=Twitask.STATUS_DRAFT){

            UserSession userSession = Pagez.getUserSession();

            //Twitask twitask = new Twitask();
            twitask.setUserid(userSession.getUser().getUserid());
            twitask.setStatus(Twitask.STATUS_DRAFT);
            twitask.setCharitycustom("");
            twitask.setCharitycustomurl("");
            twitask.setCharityonlyallowcustom(false);
            twitask.setCriteriaxml("");
            twitask.setDneeromarkuppercent(SurveyMoneyStatus.DEFAULTDNEEROMARKUPPERCENT);
            twitask.setIscharityonly(false);
            twitask.setIssysadminrejected(false);
            twitask.setIssysadminreviewed(false);
            twitask.setNumberofrespondentsrequested(500);
            twitask.setPlid(Pagez.getUserSession().getPl().getPlid());
            twitask.setResellercode("");
            twitask.setScorebysysadmin(0);
            twitask.setSenttotwitterdate(Time.xYearsAgoEnd(Calendar.getInstance(), 20).getTime());
            twitask.setClosedintwitterdate(Time.xYearsAgoEnd(Calendar.getInstance(), 20).getTime());
            twitask.setTwitterid(0);

            boolean isnewtwitask = true;

            //Validation
            boolean isValidData = true;

            if (isValidData && Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){

                twitask.setUserid(userSession.getUser().getUserid());
                twitask.setQuestion(question);
                twitask.setStartdate(startdate);
                try{
                    logger.debug("save() about to save twitask.getTwitaskid()=" + twitask.getTwitaskid());
                    twitask.save();
                    logger.debug("save() done saving twitask.getTwitaskid()=" + twitask.getTwitaskid());
                } catch (GeneralException gex){
                    logger.debug("save() failed: " + gex.getErrorsAsSingleString());
                    String message = "save() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                if (isnewtwitask){
                    //Notify sales group
                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "New dNeero Twitask Started: "+ twitask.getQuestion()+" (twitaskid="+twitask.getTwitaskid()+")");
                    xmpp.send();
                }

                //Refresh
                twitask.refresh();

                this.twitask = twitask;
            } else {
                throw vex;
            }

        }
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question=question;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate=startdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }
}