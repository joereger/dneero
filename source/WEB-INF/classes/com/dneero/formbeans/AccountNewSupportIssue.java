package com.dneero.formbeans;

import com.dneero.dao.Supportissue;
import com.dneero.dao.Supportissuecomm;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 28, 2006
 * Time: 8:39:38 AM
 */
public class AccountNewSupportIssue {

    private String subject;
    private String notes;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String newIssue(){

        Supportissue supportissue = new Supportissue();
        supportissue.setStatus(Supportissue.STATUS_OPEN);
        supportissue.setSubject(subject);
        supportissue.setDatetime(new Date());
        supportissue.setUserid(Jsf.getUserSession().getUser().getUserid());
        try{
            supportissue.save();
        } catch (GeneralException gex){
            Jsf.setFacesMessage("Sorry, there was an error: " + gex.getErrorsAsSingleString());
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        Supportissuecomm supportissuecomm = new Supportissuecomm();
        supportissuecomm.setSupportissueid(supportissue.getSupportissueid());
        supportissuecomm.setDatetime(new Date());
        supportissuecomm.setIsfromdneeroadmin(false);
        supportissuecomm.setNotes(notes);
        supportissue.getSupportissuecomms().add(supportissuecomm);
        try{
            supportissue.save();
        } catch (GeneralException gex){
            Jsf.setFacesMessage("Sorry, there was an error: " + gex.getErrorsAsSingleString());
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            return null;
        }
        return "accountsupportissuenewdone";
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
