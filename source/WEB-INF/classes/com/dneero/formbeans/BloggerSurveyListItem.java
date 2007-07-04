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
    private String isbloggerqualifiedstring = "";
    private String description = "";
    private int numberofrespondents=0;
    private boolean ischarityonly = false;

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


    public boolean getBloggerhasalreadytakensurvey() {
        return bloggerhasalreadytakensurvey;
    }

    public void setBloggerhasalreadytakensurvey(boolean bloggerhasalreadytakensurvey) {
        this.bloggerhasalreadytakensurvey = bloggerhasalreadytakensurvey;
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
}
