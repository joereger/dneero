package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.incentive.Incentive;

/**
 * User: Joe Reger Jr
 * Date: Aug 1, 2008
 * Time: 2:05:19 PM
 */
public class AccountAwardsListitem {

    private Incentiveaward incentiveaward;
    private Response response;
    private Survey survey;
    private Surveyincentive surveyincentive;
    private Incentive incentive;
    private User user;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response=response;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public Surveyincentive getSurveyincentive() {
        return surveyincentive;
    }

    public void setSurveyincentive(Surveyincentive surveyincentive) {
        this.surveyincentive=surveyincentive;
    }

    public Incentive getIncentive() {
        return incentive;
    }

    public void setIncentive(Incentive incentive) {
        this.incentive=incentive;
    }

    public Incentiveaward getIncentiveaward() {
        return incentiveaward;
    }

    public void setIncentiveaward(Incentiveaward incentiveaward) {
        this.incentiveaward=incentiveaward;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }
}
