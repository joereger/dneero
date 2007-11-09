package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;

import com.dneero.dao.Supportissuecomm;
import com.dneero.dao.Supportissue;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Str;
import com.dneero.email.EmailTemplateProcessor;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminSupportIssueDetail implements Serializable {

    private int supportissueid;
    private String notes;
    private ArrayList<Supportissuecomm> supportissuecomms;
    private Supportissue supportissue;
    private String status;
    private User fromuser;




    public SysadminSupportIssueDetail(){
        
    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called: supportissueid="+supportissueid);
        String tmpSupportissueid = Pagez.getRequest().getParameter("supportissueid");
        if (com.dneero.util.Num.isinteger(tmpSupportissueid)){
            logger.debug("beginView called: found supportissueid in param="+tmpSupportissueid);
            Supportissue supportissue = Supportissue.get(Integer.parseInt(tmpSupportissueid));
            fromuser = User.get(supportissue.getUserid());
            if (Pagez.getUserSession().getUser()!=null && supportissue.canEdit(Pagez.getUserSession().getUser())){
                this.supportissue = supportissue;
                this.supportissueid = supportissue.getSupportissueid();
                this.status = String.valueOf(supportissue.getStatus());
                supportissuecomms = new ArrayList<Supportissuecomm>();
                for (Iterator<Supportissuecomm> iterator = supportissue.getSupportissuecomms().iterator(); iterator.hasNext();){
                    Supportissuecomm supportissuecomm = iterator.next();
                    supportissuecomms.add(supportissuecomm);
                }
            }

        } else {
            logger.debug("beginView called: NOT found supportissueid in param="+tmpSupportissueid);
        }
    }

    public String newNote(){
        Logger logger = Logger.getLogger(this.getClass().getName());
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

        if (Num.isinteger(status)){
            supportissue.setStatus(Integer.parseInt(status));
        } else{
            supportissue.setStatus(0);
        }

        try{
            supportissue.save();
        } catch (GeneralException gex){
            Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
            return null;
        }

        //Send notification email
        String[] args = new String[3];
        args[0]=supportissue.getSubject();
        args[1]=supportissuecomm.getNotes();
        if (supportissue.getStatus()==Supportissue.STATUS_OPEN){
            args[2]= "Open";
        } else if (supportissue.getStatus()==Supportissue.STATUS_CLOSED){
            args[2]= "Closed";
        } else {
            args[2]= "Working";
        }
        EmailTemplateProcessor.sendMail("dNeero Support Issue: "+ Str.truncateString(supportissue.getSubject(),100), "supportissueresponse", User.get(supportissue.getUserid()), args);

        SysadminSupportIssuesList bean = (SysadminSupportIssuesList)Jsf.getManagedBean("sysadminSupportIssuesList");
        return bean.beginView();
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getFromuser() {
        return fromuser;
    }

    public void setFromuser(User fromuser) {
        this.fromuser = fromuser;
    }
}
