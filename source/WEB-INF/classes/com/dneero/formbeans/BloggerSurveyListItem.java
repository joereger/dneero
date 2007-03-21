package com.dneero.formbeans;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Oct 3, 2006
 * Time: 9:44:11 AM
 */
public class BloggerSurveyListItem implements Serializable {

    private int surveyid;
    private String title;
    private String maxearning;
    private String daysuntilend;
    private String numberofquestions;
    private boolean bloggerhasalreadytakensurvey = false;

    public BloggerSurveyListItem(){}

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

    public String getMaxearning() {
        return maxearning;
    }

    public void setMaxearning(String maxearning) {
        this.maxearning = maxearning;
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


    public boolean isBloggerhasalreadytakensurvey() {
        return bloggerhasalreadytakensurvey;
    }

    public void setBloggerhasalreadytakensurvey(boolean bloggerhasalreadytakensurvey) {
        this.bloggerhasalreadytakensurvey = bloggerhasalreadytakensurvey;
    }
}
