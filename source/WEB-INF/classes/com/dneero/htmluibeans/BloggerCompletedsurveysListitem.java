package com.dneero.htmluibeans;

import com.dneero.dao.Response;

import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerCompletedsurveysListitem implements Serializable {

    private int surveyid;
    private int responseid;
    private String surveytitle;
    private Date responsedate;
    private int totalimpressions;
    private int paidandtobepaidimpressions;
    private Response response;

    public BloggerCompletedsurveysListitem(){}

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public String getSurveytitle() {
        return surveytitle;
    }

    public void setSurveytitle(String surveytitle) {
        this.surveytitle = surveytitle;
    }

    public Date getResponsedate() {
        return responsedate;
    }

    public void setResponsedate(Date responsedate) {
        this.responsedate = responsedate;
    }


    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


    public int getTotalimpressions() {
        return totalimpressions;
    }

    public void setTotalimpressions(int totalimpressions) {
        this.totalimpressions=totalimpressions;
    }

    public int getPaidandtobepaidimpressions() {
        return paidandtobepaidimpressions;
    }

    public void setPaidandtobepaidimpressions(int paidandtobepaidimpressions) {
        this.paidandtobepaidimpressions=paidandtobepaidimpressions;
    }
}
