package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.Jsf;
import com.dneero.util.Io;
import com.dneero.survey.servlet.SurveyJavascriptServlet;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.email.EmailSendThread;
import com.dneero.email.EmailSend;
import com.dneero.scheduledjobs.SendMassemails;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;

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

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("beginView called:");
        String tmpMassemailid = Jsf.getRequestParam("massemailid");
        if (com.dneero.util.Num.isinteger(tmpMassemailid)){
            logger.debug("beginView called: found massemailid in request param="+tmpMassemailid);
            load(Integer.parseInt(tmpMassemailid));
        } else {
            massemail = new Massemail();
            massemail.setDate(new Date());
            massemail.setStatus(Massemail.STATUS_NEW);
            massemail.setLastuseridprocessed(0);
        }
        calculatePreviews();
        return "sysadminmassemaildetail";
    }

    private void load(int massemailid){
        massemail = Massemail.get(massemailid);
        calculatePreviews();
    }

    public String save(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{massemail.save();}catch(Exception ex){logger.error(ex);}
        calculatePreviews();
        Jsf.setFacesMessage("Saved!");
        return "sysadminmassemaildetail";
    }

    public String testSend(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{massemail.save();}catch(Exception ex){logger.error(ex);}
        calculatePreviews();
        try{
            HtmlEmail email = new HtmlEmail();
            String testSentTo = Jsf.getUserSession().getUser().getEmail();
            if (testsendemailaddress!=null && !testsendemailaddress.equals("")){
                testSentTo = testsendemailaddress;
            }
            email.addTo(testSentTo);
            email.setFrom(EmailSendThread.DEFAULTFROM);
            email.setSubject(massemail.getSubject());
            email.setHtmlMsg(htmlPreview);
            email.setTextMsg(txtPreview);
            EmailSend.sendMail(email);
            Jsf.setFacesMessage("Test email sent to: "+testSentTo+"!");
        } catch (Exception e){
            Jsf.setFacesMessage("Error on send! " +e.getMessage());
            logger.error(e);
        }
        return "sysadminmassemaildetail";
    }

    private void calculatePreviews(){
        if (massemail!=null){
            String[] args = SendMassemails.getMassemailArgsFromUser(Jsf.getUserSession().getUser());
            String htmlEmailHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + "emailheader.html").toString();
            String htmlEmailFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + "emailfooter.html").toString();
            String htmlMessage = EmailTemplateProcessor.processTemplate(massemail.getHtmlmessage(), Jsf.getUserSession().getUser(), args);
            String txtMessage = EmailTemplateProcessor.processTemplate(massemail.getTxtmessage(), Jsf.getUserSession().getUser(), args);
            subjectPreview = EmailTemplateProcessor.processTemplate(massemail.getSubject(), Jsf.getUserSession().getUser(), args);
            htmlPreview = htmlEmailHeader + EmailTemplateProcessor.translateImageLinks(htmlMessage) + htmlEmailFooter;
            txtPreview = EmailTemplateProcessor.translateImageLinks(txtMessage);
        } else {
            htmlPreview="";
            txtPreview="";
        }
    }

    public String send(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        calculatePreviews();
        if (massemail.getStatus()==Massemail.STATUS_NEW){
            try{massemail.save();}catch(Exception ex){logger.error(ex);}
            SysadminMassemailSend bean = (SysadminMassemailSend)Jsf.getManagedBean("sysadminMassemailSend");
            bean.setMassemail(massemail);
            return bean.beginView();
        } else {
            Jsf.setFacesMessage("Sorry, this mass email has already been sent!");
            return "sysadminmassemaildetail";
        }
    }

    public String copy(){
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
        try{massemailCopy.save();}catch(Exception ex){logger.error(ex);}
        Jsf.setFacesMessage("Mass Email Copied!");
        SysadminMassemailList bean = (SysadminMassemailList)Jsf.getManagedBean("sysadminMassemailList");
        return bean.beginView();
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
