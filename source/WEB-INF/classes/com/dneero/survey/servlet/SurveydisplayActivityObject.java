package com.dneero.survey.servlet;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:11:26 PM
 */
public class SurveydisplayActivityObject implements Serializable {

    private int surveyid;

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

}
