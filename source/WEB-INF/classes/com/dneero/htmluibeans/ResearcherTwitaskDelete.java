package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Calendar;
import java.io.Serializable;


import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.dao.Survey;
import com.dneero.dao.Twitask;
import com.dneero.xmpp.SendXMPPMessage;


/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherTwitaskDelete implements Serializable {

    private Twitask twitask;

    public ResearcherTwitaskDelete(){

    }




    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get((Integer.parseInt(Pagez.getRequest().getParameter("twitaskid"))));
        }
        if (twitask!=null){
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){

            } else {
                twitask = null;
            }
        }

    }

    public void deleteTwitask() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        UserSession userSession = Pagez.getUserSession();
        logger.debug("deleteTwitask() called. status="+twitask.getStatus() + " userSession.getCurrentSurveyid()="+userSession.getCurrentSurveyid());
        if (twitask.getStatus()==Twitask.STATUS_DRAFT){
                logger.debug("deleteTwitask() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                twitask.refresh();
                try{
                    twitask.delete();
                } catch (Exception gex){
                    logger.error(gex);
                    String message = "deleteTwitask() failed: " + gex.getMessage();
                    vex.addValidationError("Sorry, there was an error. Please click the Delete button again.");
                    throw vex;
                }
        } else {
            vex.addValidationError("Twitter Question could not be deleted because it is not in draft mode.");
            logger.debug("Not deleting because status!=Survey.STATUS_DRAFT");
            throw vex;
        }
    }

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }
}