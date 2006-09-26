package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;

import com.dneero.dao.*;
import com.dneero.util.Str;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.util.Util;
import com.dneero.session.UserSession;
import com.dneero.constants.*;
import com.dneero.finders.SurveyCriteriaXML;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail04 {

    private int status;
    private int agemin = 13;
    private int agemax = 100;
    private int blogquality = 0;
    private int blogquality90days = 0;
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
    private String genderStr;
    private String ethnicityStr;
    private String maritalstatusStr;
    private String incomeStr;
    private String educationlevelStr;
    private String stateStr;
    private String cityStr;
    private String professionStr;
    private String blogfocusStr;
    private String politicsStr;




    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherSurveyDetail04(){
        logger.debug("Instanciating object.");
        //preSelectAll();
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }

//    private void preSelectAll(){
//        gender = convertToArray((TreeMap)Jsf.getManagedBean("genders"));
//        ethnicity = convertToArray((LinkedHashMap)Jsf.getManagedBean("ethnicities"));
//        maritalstatus = convertToArray((LinkedHashMap)Jsf.getManagedBean("maritalstatuses"));
//        income = convertToArray((LinkedHashMap)Jsf.getManagedBean("incomes"));
//        educationlevel = convertToArray((LinkedHashMap)Jsf.getManagedBean("educationlevels"));
//        state = convertToArray((LinkedHashMap)Jsf.getManagedBean("states"));
//        city = convertToArray((LinkedHashMap)Jsf.getManagedBean("cities"));
//        profession = convertToArray((TreeMap)Jsf.getManagedBean("professions"));
//        blogfocus = convertToArray((TreeMap)Jsf.getManagedBean("blogfocuses"));
//        politics = convertToArray((LinkedHashMap)Jsf.getManagedBean("politics"));
//    }
//
//    private String[] convertToArray(TreeMap tmap){
//        String[] out = new String[0];
//        if (tmap!=null){
//            out = new String[tmap.size()];
//            Iterator keyValuePairs = tmap.entrySet().iterator();
//            for (int i = 0; i < tmap.size(); i++){
//                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
//                Object key = mapentry.getKey();
//                String value = (String)mapentry.getValue();
//                out[i] = value;
//            }
//        }
//        return out;
//    }
//
//    private String[] convertToArray(LinkedHashMap tmap){
//        String[] out = new String[0];
//        if (tmap!=null){
//            out = new String[tmap.size()];
//            Iterator keyValuePairs = tmap.entrySet().iterator();
//            for (int i = 0; i < tmap.size(); i++){
//                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
//                Object key = mapentry.getKey();
//                String value = (String)mapentry.getValue();
//                out[i] = value;
//            }
//        }
//        return out;
//    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        status = survey.getStatus();
        if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
            //Do it with XML
            SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
            agemin = surveyCriteriaXML.getAgemin();
            agemax = surveyCriteriaXML.getAgemax();
            blogquality = surveyCriteriaXML.getBlogquality();
            blogquality90days = surveyCriteriaXML.getBlogquality90days();
            gender = surveyCriteriaXML.getGender();
            genderStr = arrayToString(gender, "<br>");
            ethnicity = surveyCriteriaXML.getEthnicity();
            ethnicityStr = arrayToString(ethnicity, "<br>");
            maritalstatus = surveyCriteriaXML.getMaritalstatus();
            maritalstatusStr = arrayToString(maritalstatus, "<br>");
            income = surveyCriteriaXML.getIncome();
            incomeStr = arrayToString(income, "<br>");
            educationlevel = surveyCriteriaXML.getEducationlevel();
            educationlevelStr = arrayToString(educationlevel, "<br>");
            state = surveyCriteriaXML.getState();
            stateStr = arrayToString(state, ", ");
            city = surveyCriteriaXML.getCity();
            cityStr = arrayToString(city, ", ");
            profession = surveyCriteriaXML.getProfession();
            professionStr = arrayToString(profession, "<br>");
            politics = surveyCriteriaXML.getPolitics();
            politicsStr = arrayToString(politics, "<br>");
            blogfocus = surveyCriteriaXML.getBlogfocus();
            blogfocusStr = arrayToString(blogfocus, ", ");
        }

    }

    private String arrayToString(String[] array, String delimiter){
        StringBuffer out = new StringBuffer();
        if (array!=null){
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                out.append(s);
                if (i<array.length){
                    out.append(delimiter);
                }
            }
        }
        return out.toString();
    }



    public String saveSurvey(){
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_WAITINGFORSTARTDATE){
            UserSession userSession = Jsf.getUserSession();

            Survey survey = new Survey();
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
            }

            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){

                //Do it with XML
                if (true){
                    SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
                    surveyCriteriaXML.setAgemin(agemin);
                    surveyCriteriaXML.setAgemax(agemax);
                    surveyCriteriaXML.setBlogquality(blogquality);
                    surveyCriteriaXML.setBlogquality90days(blogquality90days);
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

                    survey.setCriteriaxml(surveyCriteriaXML.getSurveyCriteriaAsString());
                }

               //Final save
                try{
                    logger.debug("saveSurvey() about to save (for 2nd time) survey.getSurveyid()=" + survey.getSurveyid());
                    survey.save();
                    logger.debug("saveSurvey() done saving (for 2nd time) survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }

                //Refresh
                survey.refresh();
            }

        }

        return "success";
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public String getEthnicityStr() {
        return ethnicityStr;
    }

    public void setEthnicityStr(String ethnicityStr) {
        this.ethnicityStr = ethnicityStr;
    }

    public String getMaritalstatusStr() {
        return maritalstatusStr;
    }

    public void setMaritalstatusStr(String maritalstatusStr) {
        this.maritalstatusStr = maritalstatusStr;
    }

    public String getIncomeStr() {
        return incomeStr;
    }

    public void setIncomeStr(String incomeStr) {
        this.incomeStr = incomeStr;
    }

    public String getEducationlevelStr() {
        return educationlevelStr;
    }

    public void setEducationlevelStr(String educationlevelStr) {
        this.educationlevelStr = educationlevelStr;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getCityStr() {
        return cityStr;
    }

    public void setCityStr(String cityStr) {
        this.cityStr = cityStr;
    }

    public String getProfessionStr() {
        return professionStr;
    }

    public void setProfessionStr(String professionStr) {
        this.professionStr = professionStr;
    }

    public String getBlogfocusStr() {
        return blogfocusStr;
    }

    public void setBlogfocusStr(String blogfocusStr) {
        this.blogfocusStr = blogfocusStr;
    }

    public String getPoliticsStr() {
        return politicsStr;
    }

    public void setPoliticsStr(String politicsStr) {
        this.politicsStr = politicsStr;
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
}
