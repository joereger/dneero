package com.dneero.htmluibeans;

import com.dneero.dao.Survey;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.display.SurveyResultsDisplay;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:06 AM
 */
public class ResearcherResultsAnswersAdvanced implements Serializable {

    private Survey survey;
    private String results;
    private String surveyCriteriaAsHtml;

    private int agemin = 13;
    private int agemax = 100;
    private int blogquality = 0;
    private int blogquality90days = 0;
    private int minsocialinfluencepercentile = 100;
    private int minsocialinfluencepercentile90days = 100;
    private int dayssincelastsurvey = 0;
    private int totalsurveystakenatleast = 0;
    private int totalsurveystakenatmost = 100000;
    private String[] gender;
    private String[] ethnicity;
    private String[] maritalstatus;
    private String[] income;
    private String[] educationlevel;
    private String[] state;
    private String[] city;
    private String[] profession;
    private String[] blogfocus;
    private String[] politics;
    private String[] dneerousagemethods;

    public ResearcherResultsAnswersAdvanced(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            //results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>());
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                //Get the survey's default criteria
                SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
                surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();
                agemin = surveyCriteriaXML.getAgemin();
                agemax = surveyCriteriaXML.getAgemax();
                blogquality = surveyCriteriaXML.getBlogquality();
                blogquality90days = surveyCriteriaXML.getBlogquality90days();
                minsocialinfluencepercentile = surveyCriteriaXML.getMinsocialinfluencepercentile();
                minsocialinfluencepercentile90days = surveyCriteriaXML.getMinsocialinfluencepercentile90days();
                dayssincelastsurvey = surveyCriteriaXML.getDayssincelastsurvey();
                totalsurveystakenatleast = surveyCriteriaXML.getTotalsurveystakenatleast();
                totalsurveystakenatmost = surveyCriteriaXML.getTotalsurveystakenatmost();
                gender = surveyCriteriaXML.getGender();
                ethnicity = surveyCriteriaXML.getEthnicity();
                maritalstatus = surveyCriteriaXML.getMaritalstatus();
                income = surveyCriteriaXML.getIncome();
                educationlevel = surveyCriteriaXML.getEducationlevel();
                state = surveyCriteriaXML.getState();
                city = surveyCriteriaXML.getCity();
                profession = surveyCriteriaXML.getProfession();
                politics = surveyCriteriaXML.getPolitics();
                dneerousagemethods = surveyCriteriaXML.getDneerousagemethods();
                blogfocus = surveyCriteriaXML.getBlogfocus();
            }
        }
    }


    public void find() throws ValidationException {
        //Create a criteria to use with the results
        SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
        surveyCriteriaXML.setAgemin(agemin);
        surveyCriteriaXML.setAgemax(agemax);
        surveyCriteriaXML.setBlogquality(blogquality);
        surveyCriteriaXML.setBlogquality90days(blogquality90days);
        surveyCriteriaXML.setMinsocialinfluencepercentile(minsocialinfluencepercentile);
        surveyCriteriaXML.setMinsocialinfluencepercentile90days(minsocialinfluencepercentile90days);
        surveyCriteriaXML.setDayssincelastsurvey(dayssincelastsurvey);
        surveyCriteriaXML.setTotalsurveystakenatleast(totalsurveystakenatleast);
        surveyCriteriaXML.setTotalsurveystakenatmost(totalsurveystakenatmost);
        surveyCriteriaXML.setGender(gender);
        surveyCriteriaXML.setEthnicity(ethnicity);
        surveyCriteriaXML.setMaritalstatus(maritalstatus);
        surveyCriteriaXML.setIncome(income);
        surveyCriteriaXML.setEducationlevel(educationlevel);
        surveyCriteriaXML.setState(state);
        surveyCriteriaXML.setCity(city);
        surveyCriteriaXML.setProfession(profession);
        surveyCriteriaXML.setBlogfocus(blogfocus);
        surveyCriteriaXML.setPolitics(politics);
        surveyCriteriaXML.setDneerousagemethods(dneerousagemethods);
        surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();
        //Now do something
        results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>(), surveyCriteriaXML);
    }



    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getSurveyCriteriaAsHtml() {
        return surveyCriteriaAsHtml;
    }

    public void setSurveyCriteriaAsHtml(String surveyCriteriaAsHtml) {
        this.surveyCriteriaAsHtml = surveyCriteriaAsHtml;
    }

    public int getAgemin() {
        return agemin;
    }

    public void setAgemin(int agemin) {
        this.agemin = agemin;
    }

    public int getAgemax() {
        return agemax;
    }

    public void setAgemax(int agemax) {
        this.agemax = agemax;
    }

    public int getBlogquality() {
        return blogquality;
    }

    public void setBlogquality(int blogquality) {
        this.blogquality = blogquality;
    }

    public int getBlogquality90days() {
        return blogquality90days;
    }

    public void setBlogquality90days(int blogquality90days) {
        this.blogquality90days = blogquality90days;
    }

    public int getMinsocialinfluencepercentile() {
        return minsocialinfluencepercentile;
    }

    public void setMinsocialinfluencepercentile(int minsocialinfluencepercentile) {
        this.minsocialinfluencepercentile = minsocialinfluencepercentile;
    }

    public int getMinsocialinfluencepercentile90days() {
        return minsocialinfluencepercentile90days;
    }

    public void setMinsocialinfluencepercentile90days(int minsocialinfluencepercentile90days) {
        this.minsocialinfluencepercentile90days = minsocialinfluencepercentile90days;
    }

    public int getDayssincelastsurvey() {
        return dayssincelastsurvey;
    }

    public void setDayssincelastsurvey(int dayssincelastsurvey) {
        this.dayssincelastsurvey = dayssincelastsurvey;
    }

    public int getTotalsurveystakenatleast() {
        return totalsurveystakenatleast;
    }

    public void setTotalsurveystakenatleast(int totalsurveystakenatleast) {
        this.totalsurveystakenatleast = totalsurveystakenatleast;
    }

    public int getTotalsurveystakenatmost() {
        return totalsurveystakenatmost;
    }

    public void setTotalsurveystakenatmost(int totalsurveystakenatmost) {
        this.totalsurveystakenatmost = totalsurveystakenatmost;
    }

    public String[] getGender() {
        return gender;
    }

    public void setGender(String[] gender) {
        this.gender = gender;
    }

    public String[] getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String[] ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String[] getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String[] maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String[] getIncome() {
        return income;
    }

    public void setIncome(String[] income) {
        this.income = income;
    }

    public String[] getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String[] educationlevel) {
        this.educationlevel = educationlevel;
    }

    public String[] getState() {
        return state;
    }

    public void setState(String[] state) {
        this.state = state;
    }

    public String[] getCity() {
        return city;
    }

    public void setCity(String[] city) {
        this.city = city;
    }

    public String[] getProfession() {
        return profession;
    }

    public void setProfession(String[] profession) {
        this.profession = profession;
    }

    public String[] getBlogfocus() {
        return blogfocus;
    }

    public void setBlogfocus(String[] blogfocus) {
        this.blogfocus = blogfocus;
    }

    public String[] getPolitics() {
        return politics;
    }

    public void setPolitics(String[] politics) {
        this.politics = politics;
    }

    public String[] getDneerousagemethods() {
        return dneerousagemethods;
    }

    public void setDneerousagemethods(String[] dneerousagemethods) {
        this.dneerousagemethods = dneerousagemethods;
    }
}