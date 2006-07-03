package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.session.UserSession;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.*;

import oracle.adf.view.faces.model.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class SurveyBackingBean {

    private int surveyid=0;
    private String title;
    private String description;
    private double willingtopayperrespondent;
    private int numberofrespondentsrequested;
    private double willingtopaypercpm;
    private int maxdisplaysperblog;
    private int maxdisplaystotal;
    private Date startdate;
    private Date enddate;
    private String surveybody;
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
    private String billingname;
    private String billingaddress1;
    private String billingaddress2;
    private String billingcity;
    private String billingstate;
    private String billingzip;
    private int billingpaymentmethod;
    private String ccnum;
    private int ccexpmonth;
    private int ccexpyear;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public SurveyBackingBean(){
        logger.debug("Instanciating object... tmpsurveyid="+surveyid);
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpSurveyid = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in param="+tmpSurveyid);
            loadSurvey(Integer.parseInt(tmpSurveyid));
        }
        return "researchersurveydetail_01";
    }

    public void loadSurvey(int surveyid){

        Survey survey = Survey.get(surveyid);
        if (survey.getSurveycriteria()!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());

            this.surveyid = survey.getSurveyid();
            title = survey.getTitle();
            description = survey.getDescription();
            willingtopayperrespondent = survey.getWillingtopayperrespondent();
            numberofrespondentsrequested = survey.getNumberofrespondentsrequested();
            willingtopaypercpm = survey.getWillingtopaypercpm();
            maxdisplaysperblog = survey.getMaxdisplaysperblog();
            maxdisplaystotal = survey.getMaxdisplaystotal();
            startdate = survey.getStartdate();
            enddate = survey.getEnddate();
            surveybody = survey.getSurveybody();
            if (survey.getResearcherbilling()!=null){
                billingname = survey.getResearcherbilling().getBillingname();
                billingaddress1 = survey.getResearcherbilling().getBillingaddress1();
                billingaddress2 = survey.getResearcherbilling().getBillingaddress2();
                billingcity = survey.getResearcherbilling().getBillingcity();
                billingstate = survey.getResearcherbilling().getBillingstate();
                billingzip = survey.getResearcherbilling().getBillingzip();
                billingpaymentmethod = survey.getResearcherbilling().getBillingpaymentmethod();
                ccnum = survey.getResearcherbilling().getCcnum();
                ccexpmonth = survey.getResearcherbilling().getCcexpmonth();
                ccexpyear = survey.getResearcherbilling().getCcexpyear();
            }

            agemin = survey.getSurveycriteria().getAgemin();
            agemax = survey.getSurveycriteria().getAgemax();
            if (survey.getSurveycriteria().getSurveycriteriagender()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriagender().iterator(); iterator.hasNext();) {
                    Surveycriteriagender criteria =  (Surveycriteriagender)iterator.next();
                    gender = Str.addToStringArray(gender, String.valueOf(criteria.getGender()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaethnicity()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaethnicity().iterator(); iterator.hasNext();) {
                    Surveycriteriaethnicity criteria =  (Surveycriteriaethnicity)iterator.next();
                    ethnicity = Str.addToStringArray(ethnicity, String.valueOf(criteria.getEthnicity()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriamaritalstatus()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriamaritalstatus().iterator(); iterator.hasNext();) {
                    Surveycriteriamaritalstatus criteria =  (Surveycriteriamaritalstatus)iterator.next();
                    maritalstatus = Str.addToStringArray(maritalstatus, String.valueOf(criteria.getMaritalstatus()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaincomerange()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaincomerange().iterator(); iterator.hasNext();) {
                    Surveycriteriaincomerange criteria =  (Surveycriteriaincomerange)iterator.next();
                    income = Str.addToStringArray(income, String.valueOf(criteria.getIncomerange()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaeducationlevel()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaeducationlevel().iterator(); iterator.hasNext();) {
                    Surveycriteriaeducationlevel criteria =  (Surveycriteriaeducationlevel)iterator.next();
                    educationlevel = Str.addToStringArray(educationlevel, String.valueOf(criteria.getEducationlevel()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriastate()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriastate().iterator(); iterator.hasNext();) {
                    Surveycriteriastate criteria =  (Surveycriteriastate)iterator.next();
                    state = Str.addToStringArray(state, String.valueOf(criteria.getState()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriacity()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriacity().iterator(); iterator.hasNext();) {
                    Surveycriteriacity criteria =  (Surveycriteriacity)iterator.next();
                    city = Str.addToStringArray(city, String.valueOf(criteria.getCity()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriaprofession()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriaprofession().iterator(); iterator.hasNext();) {
                    Surveycriteriaprofession criteria =  (Surveycriteriaprofession)iterator.next();
                    profession = Str.addToStringArray(profession, String.valueOf(criteria.getProfession()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriablogfocus()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriablogfocus().iterator(); iterator.hasNext();) {
                    Surveycriteriablogfocus criteria =  (Surveycriteriablogfocus)iterator.next();
                    blogfocus = Str.addToStringArray(blogfocus, String.valueOf(criteria.getBlogfocus()));
                }
            }
            if (survey.getSurveycriteria().getSurveycriteriapolitics()!=null){
                for (Iterator iterator = survey.getSurveycriteria().getSurveycriteriapolitics().iterator(); iterator.hasNext();) {
                    Surveycriteriapolitics criteria =  (Surveycriteriapolitics)iterator.next();
                    politics = Str.addToStringArray(politics, String.valueOf(criteria.getPolitics()));
                }
            }
        }

    }

    public String saveSurvey(){
        logger.debug("saveSurvey() called.");

        UserSession userSession = (UserSession)Jsf.getManagedBean("userSession");

        Survey survey = new Survey();
        if (surveyid>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+surveyid);
            survey = Survey.get(surveyid);
        }

        survey.setResearcherid(userSession.getUser().getResearcher().getResearcherid());
        survey.setTitle(title);
        survey.setDescription(description);
        survey.setWillingtopayperrespondent(willingtopayperrespondent);
        survey.setNumberofrespondentsrequested(numberofrespondentsrequested);
        survey.setWillingtopaypercpm(willingtopaypercpm);
        survey.setMaxdisplaysperblog(maxdisplaysperblog);
        survey.setMaxdisplaystotal(maxdisplaystotal);
        survey.setStartdate(startdate);
        survey.setEnddate(enddate);
        survey.setSurveybody(surveybody);

        try{
            logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
            survey.save();
            logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
        } catch (GeneralException gex){
            logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
            String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        if (survey.getResearcherbilling()!=null){
            survey.getResearcherbilling().setBillingname(billingname);
            survey.getResearcherbilling().setBillingaddress1(billingaddress1);
            survey.getResearcherbilling().setBillingaddress2(billingaddress2);
            survey.getResearcherbilling().setBillingcity(billingcity);
            survey.getResearcherbilling().setBillingstate(billingstate);
            survey.getResearcherbilling().setBillingzip(billingzip);
            survey.getResearcherbilling().setBillingpaymentmethod(billingpaymentmethod);
            survey.getResearcherbilling().setCcnum(ccnum);
            survey.getResearcherbilling().setCcexpmonth(ccexpmonth);
            survey.getResearcherbilling().setCcexpyear(ccexpyear);
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

        //Ethnicity
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



        //Marital Status
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


        //Income
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


        //Education
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

        //State
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

        //City
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

        //Profession
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

        //Blog focus
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

        //Politics
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


        return "success";
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWillingtopayperrespondent() {
        return willingtopayperrespondent;
    }

    public void setWillingtopayperrespondent(double willingtopayperrespondent) {
        this.willingtopayperrespondent = willingtopayperrespondent;
    }

    public int getNumberofrespondentsrequested() {
        return numberofrespondentsrequested;
    }

    public void setNumberofrespondentsrequested(int numberofrespondentsrequested) {
        this.numberofrespondentsrequested = numberofrespondentsrequested;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getSurveybody() {
        return surveybody;
    }

    public void setSurveybody(String surveybody) {
        this.surveybody = surveybody;
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

    public String[] getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String[] educationlevel) {
        this.educationlevel = educationlevel;
    }

    public String[] getIncome() {
        return income;
    }

    public void setIncome(String[] income) {
        this.income = income;
    }

    public String[] getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String[] maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String[] getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String[] ethnicity) {
        this.ethnicity = ethnicity;
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

    public double getWillingtopaypercpm() {
        return willingtopaypercpm;
    }

    public void setWillingtopaypercpm(double willingtopaypercpm) {
        this.willingtopaypercpm = willingtopaypercpm;
    }

    public int getMaxdisplaysperblog() {
        return maxdisplaysperblog;
    }

    public void setMaxdisplaysperblog(int maxdisplaysperblog) {
        this.maxdisplaysperblog = maxdisplaysperblog;
    }

    public int getMaxdisplaystotal() {
        return maxdisplaystotal;
    }

    public void setMaxdisplaystotal(int maxdisplaystotal) {
        this.maxdisplaystotal = maxdisplaystotal;
    }

    public String getBillingname() {
        return billingname;
    }

    public void setBillingname(String billingname) {
        this.billingname = billingname;
    }

    public String getBillingaddress1() {
        return billingaddress1;
    }

    public void setBillingaddress1(String billingaddress1) {
        this.billingaddress1 = billingaddress1;
    }

    public String getBillingaddress2() {
        return billingaddress2;
    }

    public void setBillingaddress2(String billingaddress2) {
        this.billingaddress2 = billingaddress2;
    }

    public String getBillingcity() {
        return billingcity;
    }

    public void setBillingcity(String billingcity) {
        this.billingcity = billingcity;
    }

    public String getBillingstate() {
        return billingstate;
    }

    public void setBillingstate(String billingstate) {
        this.billingstate = billingstate;
    }

    public String getBillingzip() {
        return billingzip;
    }

    public void setBillingzip(String billingzip) {
        this.billingzip = billingzip;
    }

    public int getBillingpaymentmethod() {
        return billingpaymentmethod;
    }

    public void setBillingpaymentmethod(int billingpaymentmethod) {
        this.billingpaymentmethod = billingpaymentmethod;
    }

    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum = ccnum;
    }

    public int getCcexpmonth() {
        return ccexpmonth;
    }

    public void setCcexpmonth(int ccexpmonth) {
        this.ccexpmonth = ccexpmonth;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear = ccexpyear;
    }
}
