package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Response;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 13, 2007
 * Time: 9:42:37 AM
 */
public class ResearcherBloggerProfileListitem implements Serializable {

    private Survey survey;
    private Response response;

    public ResearcherBloggerProfileListitem(){}


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
}
