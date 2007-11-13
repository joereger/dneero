package com.dneero.htmluibeans;

import com.dneero.dao.Survey;

/**
 * User: Joe Reger Jr
 * Date: Nov 12, 2007
 * Time: 6:04:15 PM
 */
public class ResearcherSurveyListitem {

    private Survey survey;
    private String status;
    private String editorreviewlink;
    private String invitelink;
    private String resultslink;
    private String copylink;
    private String deletelink;


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public String getEditorreviewlink() {
        return editorreviewlink;
    }

    public void setEditorreviewlink(String editorreviewlink) {
        this.editorreviewlink=editorreviewlink;
    }

    public String getInvitelink() {
        return invitelink;
    }

    public void setInvitelink(String invitelink) {
        this.invitelink=invitelink;
    }

    public String getResultslink() {
        return resultslink;
    }

    public void setResultslink(String resultslink) {
        this.resultslink=resultslink;
    }

    public String getCopylink() {
        return copylink;
    }

    public void setCopylink(String copylink) {
        this.copylink=copylink;
    }

    public String getDeletelink() {
        return deletelink;
    }

    public void setDeletelink(String deletelink) {
        this.deletelink=deletelink;
    }
}
