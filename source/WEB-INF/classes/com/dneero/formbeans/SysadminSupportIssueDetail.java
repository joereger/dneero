package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;

import com.dneero.dao.Blog;
import com.dneero.dao.Supportissuecomm;
import com.dneero.dao.Supportissue;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminSupportIssueDetail {

    private int supportissueid;
    private String notes;
    private ArrayList<Supportissuecomm> supportissuecomms;
    private Supportissue supportissue;
    private int status;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public SysadminSupportIssueDetail(){
        logger.debug("SysadminSupportIssueDetail instanciated.");
    }


    public String beginView(){
        logger.debug("beginView called: supportissueid="+supportissueid);
        String tmpSupportissueid = Jsf.getRequestParam("supportissueid");
        if (com.dneero.util.Num.isinteger(tmpSupportissueid)){
            logger.debug("beginView called: found supportissueid in param="+tmpSupportissueid);
            Supportissue supportissue = Supportissue.get(Integer.parseInt(tmpSupportissueid));
            this.supportissue = supportissue;
            this.supportissueid = supportissue.getSupportissueid();
            this.status = supportissue.getStatus();
            supportissuecomms = new ArrayList<Supportissuecomm>();
            for (Iterator<Supportissuecomm> iterator = supportissue.getSupportissuecomms().iterator(); iterator.hasNext();){
                Supportissuecomm supportissuecomm = iterator.next();
                supportissuecomms.add(supportissuecomm);
            }

        } else {
            logger.debug("beginView called: NOT found supportissueid in param="+tmpSupportissueid);
        }
        return "sysadminsupportissuedetail";
    }

    public String newNote(){

        if(supportissueid<=0){
            logger.debug("supportissueid not found: "+supportissueid);
            return "";
        } else {
            supportissue = Supportissue.get(supportissueid);
        }

        Supportissuecomm supportissuecomm = new Supportissuecomm();
        supportissuecomm.setSupportissueid(supportissueid);
        supportissuecomm.setDatetime(new Date());
        supportissuecomm.setIsfromdneeroadmin(true);
        supportissuecomm.setNotes(notes);

        supportissue.getSupportissuecomms().add(supportissuecomm);

        try{
            supportissuecomm.save();
        } catch (GeneralException gex){
            Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        supportissue.setStatus(status);

        try{
            supportissue.save();
        } catch (GeneralException gex){
            Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        return "sysadminsupportissuenewnotedone";
    }

    public int getSupportissueid() {
        return supportissueid;
    }

    public void setSupportissueid(int supportissueid) {
        this.supportissueid = supportissueid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Supportissue getSupportissue() {
        return supportissue;
    }

    public void setSupportissue(Supportissue supportissue) {
        this.supportissue = supportissue;
    }

    public ArrayList<Supportissuecomm> getSupportissuecomms() {
        return supportissuecomms;
    }

    public void setSupportissuecomms(ArrayList<Supportissuecomm> supportissuecomms) {
        this.supportissuecomms = supportissuecomms;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
