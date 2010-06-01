package com.dneero.htmluibeans;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Oct 3, 2006
 * Time: 9:44:11 AM
 */
public class SurveyListItem implements Serializable {

    private int surveyid;
    private String title;
    private String earn;
    private String earncompact;
    private String maxearningCPM;
    private String daysuntilend;
    private String numberofquestions;
    private boolean loggedinuserhasalreadytakensurvey = false;
    private String hasusertakenhtml = "";
    private boolean isloggedinuserqualified = true;
    private String isbloggerqualifiedstring = "";
    private String description = "";
    private int numberofrespondents=0;
    private boolean ischarityonly = false;
    private String accessonlyhtml = "";

    public SurveyListItem(){}

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEarn() {
        return earn;
    }

    public void setEarn(String earn) {
        this.earn=earn;
    }

    public String getMaxearningCPM() {
        return maxearningCPM;
    }

    public void setMaxearningCPM(String maxearningCPM) {
        this.maxearningCPM=maxearningCPM;
    }

    public String getDaysuntilend() {
        return daysuntilend;
    }

    public void setDaysuntilend(String daysuntilend) {
        this.daysuntilend = daysuntilend;
    }

    public String getNumberofquestions() {
        return numberofquestions;
    }

    public void setNumberofquestions(String numberofquestions) {
        this.numberofquestions = numberofquestions;
    }


    public boolean getLoggedinuserhasalreadytakensurvey() {
        return loggedinuserhasalreadytakensurvey;
    }

    public void setLoggedinuserhasalreadytakensurvey(boolean loggedinuserhasalreadytakensurvey) {
        this.loggedinuserhasalreadytakensurvey = loggedinuserhasalreadytakensurvey;
    }

    public String getIsbloggerqualifiedstring() {
        return isbloggerqualifiedstring;
    }

    public void setIsbloggerqualifiedstring(String isbloggerqualifiedstring) {
        this.isbloggerqualifiedstring = isbloggerqualifiedstring;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberofrespondents() {
        return numberofrespondents;
    }

    public void setNumberofrespondents(int numberofrespondents) {
        this.numberofrespondents = numberofrespondents;
    }

    public boolean getIscharityonly() {
        return ischarityonly;
    }

    public void setIscharityonly(boolean ischarityonly) {
        this.ischarityonly = ischarityonly;
    }

    public boolean getIsloggedinuserqualified() {
        return isloggedinuserqualified;
    }

    public void setIsloggedinuserqualified(boolean isloggedinuserqualified) {
        this.isloggedinuserqualified = isloggedinuserqualified;
    }

    public String getAccessonlyhtml() {
        return accessonlyhtml;
    }

    public void setAccessonlyhtml(String accessonlyhtml) {
        this.accessonlyhtml=accessonlyhtml;
    }

    public String getEarncompact() {
        return earncompact;
    }

    public void setEarncompact(String earncompact) {
        this.earncompact=earncompact;
    }

    public String getHasusertakenhtml() {
        return hasusertakenhtml;
    }

    public void setHasusertakenhtml(String hasusertakenhtml) {
        this.hasusertakenhtml = hasusertakenhtml;
    }
}
