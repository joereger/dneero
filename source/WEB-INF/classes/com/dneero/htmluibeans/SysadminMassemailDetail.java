package com.dneero.htmluibeans;

import com.dneero.dao.Massemail;
import com.dneero.email.EmailSend;
import com.dneero.email.EmailSendThread;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.scheduledjobs.SendMassemails;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.util.Io;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminMassemailDetail implements Serializable {

    private Massemail massemail;
    private String htmlPreview;
    private String txtPreview;
    private String subjectPreview;
    private String testsendemailaddress;

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("beginView called:");
        String tmpMassemailid = Pagez.getRequest().getParameter("massemailid");
        if (com.dneero.util.Num.isinteger(tmpMassemailid) && Integer.parseInt(tmpMassemailid)>0){
            logger.debug("beginView called: found massemailid in request param="+tmpMassemailid);
            load(Integer.parseInt(tmpMassemailid));
        } else {
            massemail = new Massemail();
            massemail.setDate(new Date());
            massemail.setStatus(Massemail.STATUS_NEW);
            massemail.setLastuseridprocessed(0);
        }
        calculatePreviews();
    }

    private void load(int massemailid){
        massemail = Massemail.get(massemailid);
        calculatePreviews();
    }

    public void save() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{massemail.save();}catch(Exception ex){logger.error("",ex);}
        calculatePreviews();
    }

    public void testSend() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{massemail.save();}catch(Exception ex){logger.error("",ex);}
        calculatePreviews();
        try{
            HtmlEmail email = new HtmlEmail();
            String testSentTo = Pagez.getUserSession().getUser().getEmail();
            if (testsendemailaddress!=null && !testsendemailaddress.equals("")){
                testSentTo = testsendemailaddress;
            }
            email.addTo(testSentTo);
            email.setFrom(EmailSendThread.DEFAULTFROM);
            email.setSubject(massemail.getSubject());
            email.setHtmlMsg(htmlPreview);
            email.setTextMsg(txtPreview);
            EmailSend.sendMail(email);
        } catch (Exception e){
            vex.addValidationError("Error on send! " +e.getMessage());
            logger.error("", e);
            throw vex;
        }
    }

    private void calculatePreviews(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (massemail!=null){
            String[] args = SendMassemails.getMassemailArgsFromUser(Pagez.getUserSession().getUser());
            String htmlEmailHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "template" + java.io.File.separator + "default" + java.io.File.separator + "emailheader.html").toString();
            String htmlEmailFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "template" + java.io.File.separator + "default" + java.io.File.separator + "emailfooter.html").toString();
            String htmlMessage = EmailTemplateProcessor.processTemplate(massemail.getHtmlmessage(), Pagez.getUserSession().getUser(), args);
            String txtMessage = EmailTemplateProcessor.processTemplate(massemail.getTxtmessage(), Pagez.getUserSession().getUser(), args);
            subjectPreview = EmailTemplateProcessor.processTemplate(massemail.getSubject(), Pagez.getUserSession().getUser(), args);
            //htmlPreview = htmlEmailHeader + EmailTemplateProcessor.translateImageLinks(htmlMessage) + htmlEmailFooter;
            //txtPreview = EmailTemplateProcessor.translateImageLinks(txtMessage);
            htmlPreview = htmlEmailHeader + htmlMessage + htmlEmailFooter;
            txtPreview = txtMessage;
        } else {
            htmlPreview="";
            txtPreview="";
        }
    }

    public void send() throws ValidationException{
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        calculatePreviews();
        if (massemail.getStatus()==Massemail.STATUS_NEW){
            try{massemail.save();}catch(Exception ex){logger.error("",ex);}
            //@todo setMassemail on SysadminMassemailSend bean
        } else {
            vex.addValidationError("Sorry, this mass email has already been sent!");
            throw vex;
        }
    }

    public void copy() throws ValidationException{
        Logger logger = Logger.getLogger(this.getClass().getName());
        Massemail massemailCopy = new Massemail();
        massemailCopy.setDate(new Date());
        massemailCopy.setHtmlmessage(massemail.getHtmlmessage());
        massemailCopy.setIssenttobloggers(massemail.getIssenttobloggers());
        massemailCopy.setIssenttoresearchers(massemail.getIssenttoresearchers());
        massemailCopy.setIssenttouserswhooptoutofnoncriticalemails(massemail.getIssenttouserswhooptoutofnoncriticalemails());
        massemailCopy.setLastuseridprocessed(0);
        massemailCopy.setStatus(Massemail.STATUS_NEW);
        massemailCopy.setSubject(massemail.getSubject());
        massemailCopy.setTxtmessage(massemail.getTxtmessage());
        try{massemailCopy.save();}catch(Exception ex){logger.error("",ex);}
    }


    public Massemail getMassemail() {
        return massemail;
    }

    public void setMassemail(Massemail massemail) {
        this.massemail = massemail;
    }

    public String getHtmlPreview() {
        return htmlPreview;
    }

    public void setHtmlPreview(String htmlPreview) {
        this.htmlPreview = htmlPreview;
    }

    public String getTxtPreview() {
        return txtPreview;
    }

    public void setTxtPreview(String txtPreview) {
        this.txtPreview = txtPreview;
    }

    public String getSubjectPreview() {
        return subjectPreview;
    }

    public void setSubjectPreview(String subjectPreview) {
        this.subjectPreview = subjectPreview;
    }

    public String getTestsendemailaddress() {
        return testsendemailaddress;
    }

    public void setTestsendemailaddress(String testsendemailaddress) {
        this.testsendemailaddress = testsendemailaddress;
    }
}
