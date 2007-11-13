package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.commons.validator.EmailValidator;

import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;

import com.dneero.util.Str;
import com.dneero.dao.Researcher;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.helpers.UserInputSafe;
import com.dneero.systemprops.BaseUrl;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherEmailinvite implements Serializable {

    private UploadedFile fileupload;
    private String subject = "You've Been Invited to Take a Paid Survey";
    private String message;
    private String manuallyenteredemailaddresses;
    private int surveyiduserisinvitedto;
    private boolean researcherhasatleastonelivesurvey = true;
    private Survey survey;
    private String url;



    public ResearcherEmailinvite(){

    }




    public void initBean(){
        String tmpSurveyid = Pagez.getRequest().getParameter("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid) && Integer.parseInt(tmpSurveyid)>0){
            surveyiduserisinvitedto = Integer.parseInt(tmpSurveyid);
            survey = Survey.get(surveyiduserisinvitedto);
            url = BaseUrl.get(false) + "jsp/survey.jsp?surveyid="+survey.getSurveyid();
        }
        List results = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+Pagez.getUserSession().getUser().getResearcherid()+"' and status='"+Survey.STATUS_OPEN+"'").list();
        if (results==null || results.size()<=0){
            researcherhasatleastonelivesurvey = false;
        }
    }

    public String invite() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        TreeMap emailaddresses = new TreeMap();
        //Handle uploaded file
        if (fileupload!=null){
            logger.debug("A file has been uploaded: fileupload!=null");
            logger.debug("fileupload.getName(): "+fileupload.getName());
            logger.debug("fileupload.getSize(): "+fileupload.getSize());
            logger.debug("fileupload.getContentType(): "+fileupload.getContentType());
            if (fileupload.getContentType().indexOf("text")>-1){
                try{
                    CSVReader reader = new CSVReader(new InputStreamReader(fileupload.getInputStream()));
                    String [] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        // nextLine[] is an array of values from the line
                        //System.out.println(nextLine[0] + nextLine[1] + "etc...");
                        if (nextLine[0]!=null){
                            logger.debug("nextLine[0]="+nextLine[0]);
                            emailaddresses.put(nextLine[0], true);
                        }
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                }
            }
        }
        //Handle manually-added
        logger.debug("manuallyenteredemailaddresses="+manuallyenteredemailaddresses);
        if (manuallyenteredemailaddresses!=null && !manuallyenteredemailaddresses.equals("")){
            String[] individualemails = manuallyenteredemailaddresses.split("\\n");
            for (int i = 0; i < individualemails.length; i++) {
                String individualemail = individualemails[i];
                individualemail = individualemail.trim();
                emailaddresses.put(individualemail, true);
            }
        }
        //Put addresses into session memory
        ArrayList<String> em = new ArrayList();
        Iterator keyValuePairs = emailaddresses.entrySet().iterator();
        for (int i = 0; i < emailaddresses.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            Object value = mapentry.getValue();
            EmailValidator emv = EmailValidator.getInstance();
            if (emv.isValid(key)){
                em.add(key);
                logger.debug("Valid address: "+key);
            } else {
                logger.debug("Invalid address: "+key);
            }
        }
        Pagez.getUserSession().setEmailinviteaddresses(em);
        //Put subject and others into memory
        Pagez.getUserSession().setEmailinvitesubject(UserInputSafe.clean(subject));
        Pagez.getUserSession().setEmailinvitemessage(UserInputSafe.clean(message));
        Pagez.getUserSession().setEmailinvitesurveyiduserisinvitedto(surveyiduserisinvitedto);

        Pagez.sendRedirect("/jsp/researcher/emailinvite-complete.jsp");
        return "";
    }

    public TreeMap<String, String> getSurveyids(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        List results = HibernateUtil.getSession().createQuery("from Survey where researcherid='"+Pagez.getUserSession().getUser().getResearcherid()+"' and status='"+Survey.STATUS_OPEN+"'").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Survey survey = (Survey) iterator.next();
            out.put(String.valueOf(survey.getSurveyid()), survey.getTitle());
        }
        return out;
    }


    public UploadedFile getFileupload() {
        return fileupload;
    }

    public void setFileupload(UploadedFile fileupload) {
        this.fileupload = fileupload;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getManuallyenteredemailaddresses() {
        return manuallyenteredemailaddresses;
    }

    public void setManuallyenteredemailaddresses(String manuallyenteredemailaddresses) {
        this.manuallyenteredemailaddresses = manuallyenteredemailaddresses;
    }

    public int getSurveyiduserisinvitedto() {
        return surveyiduserisinvitedto;
    }

    public void setSurveyiduserisinvitedto(int surveyiduserisinvitedto) {
        this.surveyiduserisinvitedto = surveyiduserisinvitedto;
    }


    public boolean getResearcherhasatleastonelivesurvey() {
        return researcherhasatleastonelivesurvey;
    }

    public void setResearcherhasatleastonelivesurvey(boolean researcherhasatleastonelivesurvey) {
        this.researcherhasatleastonelivesurvey = researcherhasatleastonelivesurvey;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
