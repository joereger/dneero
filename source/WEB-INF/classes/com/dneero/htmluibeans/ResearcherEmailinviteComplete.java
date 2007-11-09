package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.Num;
import com.dneero.dao.User;
import com.dneero.dao.Survey;
import com.dneero.dao.Emailinvitebatch;
import com.dneero.dao.Emailinvitebatchaddress;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.systemprops.BaseUrl;

import java.util.Iterator;
import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherEmailinviteComplete implements Serializable {

    private String emailaddresslisthtml="";
    private int numberofrecipients = 0;
    private Survey survey;


    public ResearcherEmailinviteComplete(){

    }

    public String beginView(){
        load();
        return "researcheremailinvitecomplete";
    }

    private void load(){
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getEmailinviteaddresses()!=null){
            StringBuffer sb = new StringBuffer();
            sb.append("<textarea cols=\"30\" rows=\"10\">");
            for (Iterator it = Pagez.getUserSession().getEmailinviteaddresses().iterator(); it.hasNext(); ) {
                String emailaddress = (String)it.next();
                sb.append(emailaddress + "\n");
            }
            sb.append("</textarea>");
            emailaddresslisthtml = sb.toString();
            numberofrecipients = Pagez.getUserSession().getEmailinviteaddresses().size();
            survey = Survey.get(Pagez.getUserSession().getEmailinvitesurveyiduserisinvitedto());
        }
    }
    
    public String complete(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getEmailinviteaddresses()!=null && Pagez.getUserSession().getEmailinviteaddresses().size()>0){
            //Load the survey user's invited to
            if (Pagez.getUserSession().getEmailinvitesurveyiduserisinvitedto()>0){
                Survey survey = Survey.get(Pagez.getUserSession().getEmailinvitesurveyiduserisinvitedto());
                SurveyEnhancer sh = new SurveyEnhancer(survey);
                //Get vars from session
                String subject = Pagez.getUserSession().getEmailinvitesubject();
                if (subject==null || subject.equals("")){
                    subject = "You've Been Invited to Take a Paid Survey";
                }
                String message = Pagez.getUserSession().getEmailinvitemessage();
                if (message==null){
                    message = "";
                }
                //Create the email batch in db
                Emailinvitebatch emailinvitebatch = new Emailinvitebatch();
                emailinvitebatch.setResearcherid(Pagez.getUserSession().getUser().getResearcherid());
                emailinvitebatch.setSurveyiduserisinvitedto(Pagez.getUserSession().getEmailinvitesurveyiduserisinvitedto());
                emailinvitebatch.setSubject(subject);
                emailinvitebatch.setMessage(message);
                emailinvitebatch.setDate(new Date());
                try{emailinvitebatch.save();}catch(Exception ex){logger.error("",ex);}
                //Iterate email addresses and send
                for (Iterator it = Pagez.getUserSession().getEmailinviteaddresses().iterator(); it.hasNext(); ) {
                    String emailaddress = (String)it.next();
                    String url = BaseUrl.get(false) + "survey.jsf?surveyid="+survey.getSurveyid();
                    //Create the args array to hold the dynamic portions of the email
                    String[] args = new String[10];
                    args[0] = sh.getMaxearning();
                    args[1] = "<a href='"+url+"'>"+survey.getTitle()+"</a>";
                    args[2] = message;
                    args[3] = url;
                    args[4] = survey.getTitle();
                    args[5] = survey.getDescription();
                    //Send the email
                    EmailTemplateProcessor.sendMail(subject, "researcherinviteemail", null, args, emailaddress,  Pagez.getUserSession().getUser().getEmail());
                    //Record address into batch in db
                    Emailinvitebatchaddress emailinvitebatchaddress = new Emailinvitebatchaddress();
                    emailinvitebatchaddress.setEmailinvitebatchid(emailinvitebatch.getEmailinvitebatchid());
                    emailinvitebatchaddress.setEmail(emailaddress);
                    emailinvitebatchaddress.setSentdate(new Date());
                    try{emailinvitebatchaddress.save();}catch(Exception ex){logger.error("",ex);}
                }
                //Reset vars to conserve memory
                Pagez.getUserSession().setEmailinviteaddresses(null);
                Pagez.getUserSession().setEmailinvitemessage("");
                Pagez.getUserSession().setEmailinvitesubject("");
                Pagez.getUserSession().setEmailinvitesurveyiduserisinvitedto(0);
            }
        }
        return "researcheremailinvitesent";
    }


    public String getEmailaddresslisthtml() {
        return emailaddresslisthtml;
    }

    public void setEmailaddresslisthtml(String emailaddresslisthtml) {
        this.emailaddresslisthtml = emailaddresslisthtml;
    }


    public int getNumberofrecipients() {
        return numberofrecipients;
    }

    public void setNumberofrecipients(int numberofrecipients) {
        this.numberofrecipients = numberofrecipients;
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
