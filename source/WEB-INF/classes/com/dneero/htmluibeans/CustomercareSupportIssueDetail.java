package com.dneero.htmluibeans;

import org.apache.log4j.Logger;


import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Str;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.mail.MailNotify;
import com.dneero.mail.MailtypeSimple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class CustomercareSupportIssueDetail implements Serializable {

    private int mailid;
    private String notes;
    private List<Mailchild> mailchildren;
    private Mail mail;
    private String status;
    private User fromuser;
    private boolean keepflaggedforcustomercare = false;




    public CustomercareSupportIssueDetail(){
        
    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called: mailid="+ mailid);
        String tmpMailid = Pagez.getRequest().getParameter("mailid");
        if (com.dneero.util.Num.isinteger(tmpMailid)){
            logger.debug("beginView called: found mailid in param="+tmpMailid);
            Mail mail = Mail.get(Integer.parseInt(tmpMailid));
            fromuser = User.get(mail.getUserid());
            if (Pagez.getUserSession().getUser()!=null && mail.canEdit(Pagez.getUserSession().getUser())){
                this.mail= mail;
                this.mailid= mail.getMailid();
                mailchildren = HibernateUtil.getSession().createQuery("from Mailchild where mailid='"+mailid+"' order by mailchildid asc").list();
                this.mail.setIsflaggedforcustomercare(false);
                try{this.mail.save();}catch(Exception ex){logger.error("", ex);}
            }
        } else {
            logger.debug("beginView called: NOT found mailid in param="+tmpMailid);
        }
    }

    public void newNote() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        //@todo permissions - make sure user is allowed to submit a note to this issue.
        if(mailid<=0){
            logger.debug("mailid not found: "+ mailid);
            vex.addValidationError("mailid not found.");
        } else {
            mail= Mail.get(mailid);
        }

        Mailchild mailchild = new Mailchild();
        mailchild.setMailid(mailid);
        mailchild.setDate(new Date());
        mailchild.setIsfromcustomercare(true);
        mailchild.setMailtypeid(MailtypeSimple.TYPEID);
        mailchild.setVar1(notes);
        mailchild.setVar2("");
        mailchild.setVar3("");
        mailchild.setVar4("");
        mailchild.setVar5("");
        try{
            mailchild.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error saving the record.  Please try again.");
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }

        //Mark as new again
        mail.setIsread(false);
        if (!keepflaggedforcustomercare){
            mail.setIsflaggedforcustomercare(false);
        } else {
            mail.setIsflaggedforcustomercare(true);   
        }
        try{mail.save();}catch(Exception ex){logger.error("",ex);}

        try{
            mail.save();
        } catch (GeneralException gex){
            vex.addValidationError("Error saving record: "+gex.getErrorsAsSingleString());
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }

        //Send notification email but only if there are notes
        if (notes!=null && !notes.equals("")){
            MailNotify.notify(mail);
        }
    }

    public int getMailid() {
        return mailid;
    }

    public void setMailid(int mailid) {
        this.mailid=mailid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes=notes;
    }

    public List<Mailchild> getMailchildren() {
        return mailchildren;
    }

    public void setMailchildren(List<Mailchild> mailchildren) {
        this.mailchildren=mailchildren;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail=mail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public User getFromuser() {
        return fromuser;
    }

    public void setFromuser(User fromuser) {
        this.fromuser=fromuser;
    }

    public boolean getKeepflaggedforcustomercare() {
        return keepflaggedforcustomercare;
    }

    public void setKeepflaggedforcustomercare(boolean keepflaggedforcustomercare) {
        this.keepflaggedforcustomercare=keepflaggedforcustomercare;
    }
}
