package com.dneero.htmluibeans;

import com.dneero.dao.Mail;
import com.dneero.dao.Mailchild;

import com.dneero.util.GeneralException;
import com.dneero.util.ErrorDissect;
import com.dneero.util.Time;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.helpers.UserInputSafe;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.mail.Mailtype;
import com.dneero.mail.MailtypeFactory;
import com.dneero.mail.MailtypeSimple;

import java.util.Date;
import java.util.Iterator;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 28, 2006
 * Time: 8:39:38 AM
 */
public class AccountNewInboxMessage implements Serializable {

    private String subject;
    private String notes;

    public AccountNewInboxMessage(){}

    public void initBean(){

    }

    public void newIssue() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        Mail mail = new Mail();
        mail.setIsflaggedforcustomercare(true);
        mail.setSubject(subject);
        mail.setDate(new Date());
        mail.setUserid(Pagez.getUserSession().getUser().getUserid());
        mail.setIsread(true);
        try{
            mail.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error.");
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }

        Mailchild mailchild = new Mailchild();
        mailchild.setMailid(mail.getMailid());
        mailchild.setDate(new Date());
        mailchild.setIsfromcustomercare(false);
        mailchild.setMailtypeid(MailtypeSimple.TYPEID);
        mailchild.setVar1(notes);
        mailchild.setVar2("");
        mailchild.setVar3("");
        mailchild.setVar4("");
        mailchild.setVar5("");
        try{
            mailchild.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error.");
            logger.debug("newIssue failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }



        //Notify customer care group
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Message: "+mail.getSubject()+"  ("+Pagez.getUserSession().getUser().getEmail()+") "+notes);
        xmpp.send();

        //Send email to sysadmin
        try{
            StringBuffer body = new StringBuffer();
            body.append("<b>From "+Pagez.getUserSession().getUser().getFirstname()+" "+Pagez.getUserSession().getUser().getLastname()+"</b>");
            body.append("<br>");
            body.append(Time.dateformatfordb(Time.getCalFromDate(mailchild.getDate())));
            body.append("<br>");
            Mailtype mt = MailtypeFactory.get(mailchild.getMailtypeid());
            body.append(mt.renderToHtml(mailchild));
            body.append("<br><br>");
            EmailTemplateProcessor.sendGenericEmail("regerj@gmail.com", "Message: "+mail.getSubject(), body.toString());
        } catch (Exception ex){
            logger.error("",ex);
        }

        //Reset notes
        notes = "";

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
