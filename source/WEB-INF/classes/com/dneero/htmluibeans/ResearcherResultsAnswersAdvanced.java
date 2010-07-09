package com.dneero.htmluibeans;

import com.dneero.dao.Pl;
import com.dneero.dao.Respondentfilter;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.finders.DemographicsXML;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
    private SurveyCriteriaXML surveyCriteriaXML;
    private DemographicsXML demographicsXML;
    private ArrayList<Respondentfilter> respondentfilters;
    private String filtername="";
    private Respondentfilter respondentfilter;

    private int agemin = 13;
    private int agemax = 100;
    private int minsocialinfluencepercentile = 100;
    private int dayssincelastsurvey = 0;
    private int totalsurveystakenatleast = 0;
    private int totalsurveystakenatmost = 100000;
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
        String criteriaToPrePopulateWith = "";
        //See if the survey has criteria
        if (survey!=null){
            criteriaToPrePopulateWith = survey.getSurveycriteriaxml();
        }
        //Override with a selected filter
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            Respondentfilter respondentfilter = Respondentfilter.get((Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"))));
            criteriaToPrePopulateWith = respondentfilter.getCriteriaxml();
            filtername = respondentfilter.getName();
            this.respondentfilter = respondentfilter;
        }
        //Pre-pop
        surveyCriteriaXML = new SurveyCriteriaXML(criteriaToPrePopulateWith, Pl.get(survey.getPlid()));
        demographicsXML = surveyCriteriaXML.getDemographicsXML(); //This is set by the surveyCriteriaXML now... later it's set by the jsp
        agemin = surveyCriteriaXML.getAgemin();
        agemax = surveyCriteriaXML.getAgemax();
        minsocialinfluencepercentile = surveyCriteriaXML.getMinsocialinfluencepercentile();
        dayssincelastsurvey = surveyCriteriaXML.getDayssincelastsurvey();
        totalsurveystakenatleast = surveyCriteriaXML.getTotalsurveystakenatleast();
        totalsurveystakenatmost = surveyCriteriaXML.getTotalsurveystakenatmost();
        dneerousagemethods = surveyCriteriaXML.getDneerousagemethods();
        //Load the filters for this user
        respondentfilters = (ArrayList<Respondentfilter>)HibernateUtil.getSession().createCriteria(Respondentfilter.class)
                           .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                           .addOrder(Order.desc("respondentfilterid"))
                           .setCacheable(true)
                           .list();

    }


    public void viewThroughFilter() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Create a criteria to use with the results, start with survey as default values
        surveyCriteriaXML = new SurveyCriteriaXML(survey.getSurveycriteriaxml(), Pl.get(survey.getPlid()));
        surveyCriteriaXML.setDemographicsXML(demographicsXML); //The jsp calls demographicsXML directly to set up values
        surveyCriteriaXML.setAgemin(agemin);
        surveyCriteriaXML.setAgemax(agemax);
        surveyCriteriaXML.setMinsocialinfluencepercentile(minsocialinfluencepercentile);
        surveyCriteriaXML.setDayssincelastsurvey(dayssincelastsurvey);
        surveyCriteriaXML.setTotalsurveystakenatleast(totalsurveystakenatleast);
        surveyCriteriaXML.setTotalsurveystakenatmost(totalsurveystakenatmost);
        surveyCriteriaXML.setDneerousagemethods(dneerousagemethods);

        //Save Filter
        if (filtername!=null && !filtername.equals("")){
            if (respondentfilter==null){
                respondentfilter = new Respondentfilter();
            }
            respondentfilter.setName(filtername);
            respondentfilter.setCriteriaxml(surveyCriteriaXML.getSurveyCriteriaAsString());
            respondentfilter.setUserid(Pagez.getUserSession().getUser().getUserid());
            try{respondentfilter.save();}catch(Exception ex){logger.error(ex);}
        }
        //Generate the results
        results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>(), surveyCriteriaXML, true, false);
    }

    public void deleteFilter() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            Respondentfilter respondentfilter = Respondentfilter.get((Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"))));
            if (respondentfilter!=null && respondentfilter.canEdit(Pagez.getUserSession().getUser())){
                try{respondentfilter.delete();}catch(Exception ex){logger.error(ex);}
                initBean();
            }
        }
    }

    public void showResults() throws ValidationException {
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            Respondentfilter respondentfilter = Respondentfilter.get((Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"))));
            SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(respondentfilter.getCriteriaxml(), Pl.get(survey.getPlid()));
            filtername = respondentfilter.getName();
            this.respondentfilter = respondentfilter;
            results = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>(), surveyCriteriaXML, true, false);
        }
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



    public int getMinsocialinfluencepercentile() {
        return minsocialinfluencepercentile;
    }

    public void setMinsocialinfluencepercentile(int minsocialinfluencepercentile) {
        this.minsocialinfluencepercentile = minsocialinfluencepercentile;
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

    public DemographicsXML getDemographicsXML() {
        return demographicsXML;
    }

    public String[] getDneerousagemethods() {
        return dneerousagemethods;
    }

    public void setDneerousagemethods(String[] dneerousagemethods) {
        this.dneerousagemethods = dneerousagemethods;
    }
    
    public ArrayList<Respondentfilter> getRespondentfilters() {
        return respondentfilters;
    }

    public void setRespondentfilters(ArrayList<Respondentfilter> respondentfilters) {
        this.respondentfilters = respondentfilters;
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }

    public Respondentfilter getRespondentfilter() {
        return respondentfilter;
    }

    public void setRespondentfilter(Respondentfilter respondentfilter) {
        this.respondentfilter = respondentfilter;
    }


}