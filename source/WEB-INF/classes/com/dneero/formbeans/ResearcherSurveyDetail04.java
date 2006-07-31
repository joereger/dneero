package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.Iterator;

import com.dneero.dao.*;
import com.dneero.util.Str;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.session.UserSession;
import com.dneero.constants.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail04 {

    private int status;
    private int agemin = 13;
    private int agemax = 100;
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
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }



    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey.getSurveycriteria()!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());

            genderStr="";
            ethnicityStr="";
            maritalstatusStr="";
            incomeStr="";
            educationlevelStr="";
            stateStr="";
            cityStr="";
            professionStr="";
            blogfocusStr="";
            politicsStr="";

            status = survey.getStatus();
            agemin = survey.getSurveycriteria().getAgemin();
            agemax = survey.getSurveycriteria().getAgemax();
            if (survey.getSurveycriteria().getSurveycriteriagender()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriagender().iterator(); iterator.hasNext();) {
                    Surveycriteriagender criteria =  (Surveycriteriagender)iterator.next();
                    gender = Str.addToStringArray(gender, String.valueOf(criteria.getGender()));
                    genderStr = genderStr + GendersGet.getKeyByVal(String.valueOf(criteria.getGender())) + "<br>";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaethnicity()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaethnicity().iterator(); iterator.hasNext();) {
                    Surveycriteriaethnicity criteria =  (Surveycriteriaethnicity)iterator.next();
                    ethnicity = Str.addToStringArray(ethnicity, String.valueOf(criteria.getEthnicity()));
                    ethnicityStr = ethnicityStr + EthnicitiesGet.getKeyByVal(String.valueOf(criteria.getEthnicity())) + "<br>";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriamaritalstatus()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriamaritalstatus().iterator(); iterator.hasNext();) {
                    Surveycriteriamaritalstatus criteria =  (Surveycriteriamaritalstatus)iterator.next();
                    maritalstatus = Str.addToStringArray(maritalstatus, String.valueOf(criteria.getMaritalstatus()));
                    maritalstatusStr = maritalstatusStr + MaritalstatusesGet.getKeyByVal(String.valueOf(criteria.getMaritalstatus())) + "<br>";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaincomerange()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaincomerange().iterator(); iterator.hasNext();) {
                    Surveycriteriaincomerange criteria =  (Surveycriteriaincomerange)iterator.next();
                    income = Str.addToStringArray(income, String.valueOf(criteria.getIncomerange()));
                    incomeStr = incomeStr + IncomesGet.getKeyByVal(String.valueOf(criteria.getIncomerange())) + "<br>";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaeducationlevel()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaeducationlevel().iterator(); iterator.hasNext();) {
                    Surveycriteriaeducationlevel criteria =  (Surveycriteriaeducationlevel)iterator.next();
                    educationlevel = Str.addToStringArray(educationlevel, String.valueOf(criteria.getEducationlevel()));
                    educationlevelStr = educationlevelStr + EducationlevelsGet.getKeyByVal(String.valueOf(criteria.getEducationlevel())) + "<br>";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriastate()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriastate().iterator(); iterator.hasNext();) {
                    Surveycriteriastate criteria =  (Surveycriteriastate)iterator.next();
                    state = Str.addToStringArray(state, String.valueOf(criteria.getState()));
                    stateStr = stateStr + StatesGet.getKeyByVal(String.valueOf(criteria.getState())) + ", ";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriacity()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriacity().iterator(); iterator.hasNext();) {
                    Surveycriteriacity criteria =  (Surveycriteriacity)iterator.next();
                    city = Str.addToStringArray(city, String.valueOf(criteria.getCity()));
                    cityStr = cityStr + CitiesGet.getKeyByVal(String.valueOf(criteria.getCity())) + ", ";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaprofession()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaprofession().iterator(); iterator.hasNext();) {
                    Surveycriteriaprofession criteria =  (Surveycriteriaprofession)iterator.next();
                    profession = Str.addToStringArray(profession, String.valueOf(criteria.getProfession()));
                    professionStr = professionStr + ProfessionsGet.getKeyByVal(String.valueOf(criteria.getProfession())) + "<br>";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriablogfocus()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriablogfocus().iterator(); iterator.hasNext();) {
                    Surveycriteriablogfocus criteria =  (Surveycriteriablogfocus)iterator.next();
                    blogfocus = Str.addToStringArray(blogfocus, String.valueOf(criteria.getBlogfocus()));
                    blogfocusStr = blogfocusStr + BlogfocusesGet.getKeyByVal(String.valueOf(criteria.getBlogfocus())) + ", ";
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriapolitics()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriapolitics().iterator(); iterator.hasNext();) {
                    Surveycriteriapolitics criteria =  (Surveycriteriapolitics)iterator.next();
                    politics = Str.addToStringArray(politics, String.valueOf(criteria.getPolitics()));
                    politicsStr = politicsStr + PoliticsGet.getKeyByVal(String.valueOf(criteria.getPolitics())) + "<br>";
                }
            }
        }

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




            Surveycriteria surveycriteria = new Surveycriteria();
            if (survey.getSurveycriteria()!=null){
                logger.debug("saveSurvey() survey.getSurveycriteria().getSurveycriteriaid()=" + survey.getSurveycriteria().getSurveycriteriaid());
                surveycriteria = survey.getSurveycriteria();
            }
            surveycriteria.setSurveyid(survey.getSurveyid());
            surveycriteria.setAgemin(agemin);
            surveycriteria.setAgemax(agemax);
            survey.setSurveycriteria(surveycriteria);

            try{
                survey.save();
            } catch (GeneralException gex){
                logger.debug("createNewSurvey failed: " + gex.getErrorsAsSingleString());
                String message = "New Survey creation failed: " + gex.getErrorsAsSingleString();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                return null;
            }

    //        TreeMap genders = (TreeMap)Jsf.getManagedBean("genders");
    //        TreeMap ethnicities = (TreeMap)Jsf.getManagedBean("ethnicities");
    //        TreeMap maritalstatuses = (TreeMap)Jsf.getManagedBean("maritalstatuses");
    //        TreeMap incomes = (TreeMap)Jsf.getManagedBean("incomes");
    //        TreeMap educationlevels = (TreeMap)Jsf.getManagedBean("educationlevels");

            //Gender
            if (gender!=null && gender.length>0){
                if (survey.getSurveycriteria().getSurveycriteriagender()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriagender().iterator(); iterator.hasNext();) {
                        Surveycriteriagender criteria =  (Surveycriteriagender)iterator.next();
                        logger.debug("removing criteria.getSurveycriteriacityid(): "+criteria.getSurveycriteriagenderid()+" from survey object");
                        iterator.remove();
                    }
                }
                for (int i = 0; i < gender.length; i++) {
                    if (com.dneero.util.Num.isinteger(gender[i])){
                        Surveycriteriagender criteria = new Surveycriteriagender();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setGender(Integer.parseInt(gender[i]));
                        survey.getSurveycriteria().getSurveycriteriagender().add(criteria);
                        logger.debug("added gender: "+gender[i]);
                    }
                }
            }

            //Ethnicity
            if (ethnicity!=null && ethnicity.length>0){
                if (survey.getSurveycriteria().getSurveycriteriaethnicity()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaethnicity().iterator(); iterator.hasNext();) {
                        Surveycriteriaethnicity criteria =  (Surveycriteriaethnicity)iterator.next();
                        logger.debug("removing criteria.getSurveycriteriaethnicityid(): "+criteria.getSurveycriteriaethnicityid()+" from survey object");
                        iterator.remove();
                    }
                }
                for (int i = 0; i < ethnicity.length; i++) {
                    if (com.dneero.util.Num.isinteger(ethnicity[i])){
                        Surveycriteriaethnicity criteria = new Surveycriteriaethnicity();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setEthnicity(Integer.parseInt(ethnicity[i]));
                        survey.getSurveycriteria().getSurveycriteriaethnicity().add(criteria);
                        logger.debug("added ethnicity: "+ethnicity[i]);
                    }
                }
            }



            //Marital Status
            if (maritalstatus!=null && maritalstatus.length>0){
                if (survey.getSurveycriteria().getSurveycriteriamaritalstatus()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriamaritalstatus().iterator(); iterator.hasNext();) {
                        Surveycriteriamaritalstatus criteria =  (Surveycriteriamaritalstatus)iterator.next();
                        logger.debug("removing criteria.getSurveycriteriamaritalstatusid(): "+criteria.getSurveycriteriamaritalstatusid()+" from survey object");
                        iterator.remove();
                    }
                }
                for (int i = 0; i < maritalstatus.length; i++) {
                    if (com.dneero.util.Num.isinteger(maritalstatus[i])){
                        Surveycriteriamaritalstatus criteria = new Surveycriteriamaritalstatus();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setMaritalstatus(Integer.parseInt(maritalstatus[i]));
                        survey.getSurveycriteria().getSurveycriteriamaritalstatus().add(criteria);
                        logger.debug("added maritalstatus: "+maritalstatus[i]);
                    }
                }
            }


            //Income
            if (income!=null && income.length>0){
                if (survey.getSurveycriteria().getSurveycriteriaincomerange()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaincomerange().iterator(); iterator.hasNext();) {
                        Surveycriteriaincomerange criteria =  (Surveycriteriaincomerange)iterator.next();
                        logger.debug("removing criteria.getSurveycriteriaincomerangeid(): "+criteria.getSurveycriteriaincomerangeid()+" from survey object");
                        iterator.remove();
                    }
                }
                for (int i = 0; i < income.length; i++) {
                    if (com.dneero.util.Num.isinteger(income[i])){
                        Surveycriteriaincomerange criteria = new Surveycriteriaincomerange();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setIncomerange(Integer.parseInt(income[i]));
                        survey.getSurveycriteria().getSurveycriteriaincomerange().add(criteria);
                        logger.debug("added income: "+income[i]);
                    }
                }
            }


            //Education
            if (educationlevel!=null && educationlevel.length>0){
                if (survey.getSurveycriteria().getSurveycriteriaeducationlevel()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaeducationlevel().iterator(); iterator.hasNext();) {
                        Surveycriteriaeducationlevel criteria =  (Surveycriteriaeducationlevel)iterator.next();
                        logger.debug("removing criteria.getSurveycriteriaeducationlevelid(): "+criteria.getSurveycriteriaeducationlevelid()+" from survey object");
                        iterator.remove();
                    }
                }
                for (int i = 0; i < educationlevel.length; i++) {
                    if (com.dneero.util.Num.isinteger(educationlevel[i])){
                        Surveycriteriaeducationlevel criteria = new Surveycriteriaeducationlevel();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setEducationlevel(Integer.parseInt(educationlevel[i]));
                        survey.getSurveycriteria().getSurveycriteriaeducationlevel().add(criteria);
                        logger.debug("added educationlevel: "+educationlevel[i]);
                    }
                }
            }

            //State
            if (state!=null && state.length>0){
                if (survey.getSurveycriteria().getSurveycriteriastate()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriastate().iterator(); iterator.hasNext();) {
                        Surveycriteriastate criteria =  (Surveycriteriastate)iterator.next();
                        iterator.remove();
                    }
                }
                for (int i = 0; i < state.length; i++) {
                    if (com.dneero.util.Num.isinteger(state[i])){
                        Surveycriteriastate criteria = new Surveycriteriastate();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setState(Integer.parseInt(state[i]));
                        survey.getSurveycriteria().getSurveycriteriastate().add(criteria);
                    }
                }
            }

            //City
            if (city!=null && city.length>0){
                if (survey.getSurveycriteria().getSurveycriteriacity()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriacity().iterator(); iterator.hasNext();) {
                        Surveycriteriacity criteria =  (Surveycriteriacity)iterator.next();
                        iterator.remove();
                    }
                }
                for (int i = 0; i < city.length; i++) {
                    if (com.dneero.util.Num.isinteger(city[i])){
                        Surveycriteriacity criteria = new Surveycriteriacity();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setCity(Integer.parseInt(city[i]));
                        survey.getSurveycriteria().getSurveycriteriacity().add(criteria);
                    }
                }
            }

            //Profession
            if (profession!=null && profession.length>0){
                if (survey.getSurveycriteria().getSurveycriteriaprofession()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaprofession().iterator(); iterator.hasNext();) {
                        Surveycriteriaprofession criteria =  (Surveycriteriaprofession)iterator.next();
                        iterator.remove();
                    }
                }
                for (int i = 0; i < profession.length; i++) {
                    if (com.dneero.util.Num.isinteger(profession[i])){
                        Surveycriteriaprofession criteria = new Surveycriteriaprofession();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setProfession(Integer.parseInt(profession[i]));
                        survey.getSurveycriteria().getSurveycriteriaprofession().add(criteria);
                    }
                }
            }

            //Blog focus
            if (blogfocus!=null && blogfocus.length>0){
                if (survey.getSurveycriteria().getSurveycriteriablogfocus()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriablogfocus().iterator(); iterator.hasNext();) {
                        Surveycriteriablogfocus criteria =  (Surveycriteriablogfocus)iterator.next();
                        iterator.remove();
                    }
                }
                for (int i = 0; i < blogfocus.length; i++) {
                    if (com.dneero.util.Num.isinteger(blogfocus[i])){
                        Surveycriteriablogfocus criteria = new Surveycriteriablogfocus();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setBlogfocus(Integer.parseInt(blogfocus[i]));
                        survey.getSurveycriteria().getSurveycriteriablogfocus().add(criteria);
                    }
                }
            }

            //Politics
            if (politics!=null && politics.length>0){
                if (survey.getSurveycriteria().getSurveycriteriapolitics()!=null){
                    for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriapolitics().iterator(); iterator.hasNext();) {
                        Surveycriteriapolitics criteria =  (Surveycriteriapolitics)iterator.next();
                        iterator.remove();
                    }
                }
                for (int i = 0; i < politics.length; i++) {
                    if (com.dneero.util.Num.isinteger(politics[i])){
                        Surveycriteriapolitics criteria = new Surveycriteriapolitics();
                        criteria.setSurveycriteriaid(survey.getSurveycriteria().getSurveycriteriaid());
                        criteria.setPolitics(Integer.parseInt(politics[i]));
                        survey.getSurveycriteria().getSurveycriteriapolitics().add(criteria);
                    }
                }
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
}
