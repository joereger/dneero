package com.dneero.htmluibeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Question;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 13, 2007
 * Time: 9:42:37 AM
 */
public class PublicProfileListitem implements Serializable {

    private Survey survey;
    private Response response;
    private Question userquestion;

    public PublicProfileListitem(){}


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Question getUserquestion() {
        return userquestion;
    }

    public void setUserquestion(Question userquestion) {
        this.userquestion=userquestion;
    }
}
