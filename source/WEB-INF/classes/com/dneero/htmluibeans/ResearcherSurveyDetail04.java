package com.dneero.htmluibeans;

import com.dneero.dao.Panel;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.helpers.UserInputSafe;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.ValidationException;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Str;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail04 implements Serializable {

    private String title;
    private Survey survey;

    private String surveyCriteriaAsHtml;

    private int status;
    private int agemin = 13;
    private int agemax = 100;
    private int minsocialinfluencepercentile = 100;
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
    private String[] country;
    private String[] profession;
    private String[] blogfocus;
    private String[] politics;
    private String[] dneerousagemethods;
    private String[] panels;
    private String panelsStr;
    private String[] superpanels;
    private String superpanelsStr;
    private boolean isaccesscodeonly= false;
    private String accesscode;
    private boolean isopentoanybody=true;

    public ResearcherSurveyDetail04(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            title = survey.getTitle();
            status = survey.getStatus();
            isaccesscodeonly= survey.getIsaccesscodeonly();
            accesscode = survey.getAccesscode();
            isopentoanybody = survey.getIsopentoanybody();
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                //Do it with XML
                SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
                surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();
                agemin = surveyCriteriaXML.getAgemin();
                agemax = surveyCriteriaXML.getAgemax();
                minsocialinfluencepercentile = surveyCriteriaXML.getMinsocialinfluencepercentile();
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
                country = surveyCriteriaXML.getCountry();
                profession = surveyCriteriaXML.getProfession();
                politics = surveyCriteriaXML.getPolitics();
                dneerousagemethods = surveyCriteriaXML.getDneerousagemethods();
                blogfocus = surveyCriteriaXML.getBlogfocus();
                panels = surveyCriteriaXML.getPanelids();
                superpanels = surveyCriteriaXML.getSuperpanelids();
            }
        }
    }





    public void saveSurvey() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_DRAFT){
            UserSession userSession = Pagez.getUserSession();

            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){

                //Coupon validation
                if (isaccesscodeonly && accesscode.equals("")){
                    throw new ValidationException("If you choose to do a Access Code conversation then you must enter an Access Code.");
                }
                if (isaccesscodeonly){
                    int numsurveyswithsameaccesscode = NumFromUniqueResult.getInt("select count(*) from Survey where accesscode='"+ UserInputSafe.clean(accesscode)+"' and surveyid<>'"+survey.getSurveyid()+"'");
                    if (numsurveyswithsameaccesscode>0){
                        throw new ValidationException("Another conversation with that Access Code already exists.  Please choose another.");
                    }
                }

                //Do it with XML
                if (true){
                    SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
                    surveyCriteriaXML.setAgemin(agemin);
                    surveyCriteriaXML.setAgemax(agemax);
                    surveyCriteriaXML.setMinsocialinfluencepercentile(minsocialinfluencepercentile);
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
                    surveyCriteriaXML.setCountry(country);
                    surveyCriteriaXML.setProfession(profession);
                    surveyCriteriaXML.setBlogfocus(blogfocus);
                    surveyCriteriaXML.setPolitics(politics);
                    surveyCriteriaXML.setDneerousagemethods(dneerousagemethods);
                    surveyCriteriaXML.setPanelids(panels);
                    surveyCriteriaXML.setSuperpanelids(superpanels);
                    //Put into survey
                    survey.setCriteriaxml(surveyCriteriaXML.getSurveyCriteriaAsString());
                    surveyCriteriaAsHtml = surveyCriteriaXML.getAsHtml();
                }
               //Final save
                try{
                    logger.debug("saveSurvey() about to save (for 2nd time) survey.getSurveyid()=" + survey.getSurveyid());
                    survey.setIsaccesscodeonly(isaccesscodeonly);
                    survey.setAccesscode(accesscode);
                    survey.setIsopentoanybody(isopentoanybody);
                    survey.save();
                    EmbedCacheFlusher.flushCache(survey.getSurveyid());
                    logger.debug("saveSurvey() done saving (for 2nd time) survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                //Refresh
                survey.refresh();
            }

        }
    }

    public TreeMap<String, String> getPanelsavailable(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        List results = HibernateUtil.getSession().createQuery("from Panel where researcherid='"+Pagez.getUserSession().getUser().getResearcherid()+"'").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Panel panel = (Panel) iterator.next();
            out.put(String.valueOf(panel.getPanelid()), Str.truncateString(panel.getName(), 40));
        }
        return out;
    }

    public TreeMap<String, String> getSuperpanelsavailable(){
        String emptyStr = "";
        TreeMap<String, String> out = new TreeMap<String, String>();
        List results = HibernateUtil.getSession().createQuery("from Panel where issystempanel=true"+emptyStr).list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Panel panel = (Panel) iterator.next();
            out.put(String.valueOf(panel.getPanelid()), Str.truncateString(panel.getName(), 40));
        }
        return out;
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





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getPanels() {
        return panels;
    }

    public void setPanels(String[] panels) {
        this.panels = panels;
    }

    public String getPanelsStr() {
        return panelsStr;
    }

    public void setPanelsStr(String panelsStr) {
        this.panelsStr = panelsStr;
    }

    public int getMinsocialinfluencepercentile() {
        return minsocialinfluencepercentile;
    }

    public void setMinsocialinfluencepercentile(int minsocialinfluencepercentile) {
        this.minsocialinfluencepercentile = minsocialinfluencepercentile;
    }


    public String getSurveyCriteriaAsHtml() {
        return surveyCriteriaAsHtml;
    }

    public void setSurveyCriteriaAsHtml(String surveyCriteriaAsHtml) {
        this.surveyCriteriaAsHtml = surveyCriteriaAsHtml;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public boolean getIsaccesscodeonly() {
        return isaccesscodeonly;
    }

    public void setIsaccesscodeonly(boolean isaccesscodeonly) {
        this.isaccesscodeonly=isaccesscodeonly;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode=accesscode;
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

    public String[] getDneerousagemethods() {
        return dneerousagemethods;
    }

    public void setDneerousagemethods(String[] dneerousagemethods) {
        this.dneerousagemethods = dneerousagemethods;
    }

    public String[] getCountry() {
        return country;
    }

    public void setCountry(String[] country) {
        this.country=country;
    }

    public String[] getSuperpanels() {
        return superpanels;
    }

    public void setSuperpanels(String[] superpanels) {
        this.superpanels=superpanels;
    }

    public String getSuperpanelsStr() {
        return superpanelsStr;
    }

    public void setSuperpanelsStr(String superpanelsStr) {
        this.superpanelsStr=superpanelsStr;
    }

    public boolean getIsopentoanybody() {
        return isopentoanybody;
    }

    public void setIsopentoanybody(boolean isopentoanybody) {
        this.isopentoanybody = isopentoanybody;
    }
}
