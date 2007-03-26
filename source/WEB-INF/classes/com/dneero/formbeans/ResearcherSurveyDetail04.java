package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import java.util.*;
import java.io.Serializable;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Str;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.session.UserSession;
import com.dneero.finders.SurveyCriteriaXML;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail04 implements Serializable {

    private String title;

    private int status;
    private int agemin = 13;
    private int agemax = 100;
    private int blogquality = 0;
    private int blogquality90days = 0;
    private int minsocialinfluencepercentile = 100;
    private int minsocialinfluencepercentile90days = 100;
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

    private String[] panels;
    private String panelsStr;



    public ResearcherSurveyDetail04(){

    }

    public String beginView(){
        load();
        return "researchersurveydetail_04";
    }

    private void load(){
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }

    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            title = survey.getTitle();
            status = survey.getStatus();
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                //Do it with XML
                SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(survey.getCriteriaxml());
                agemin = surveyCriteriaXML.getAgemin();
                agemax = surveyCriteriaXML.getAgemax();
                blogquality = surveyCriteriaXML.getBlogquality();
                blogquality90days = surveyCriteriaXML.getBlogquality90days();
                minsocialinfluencepercentile = surveyCriteriaXML.getMinsocialinfluencepercentile();
                minsocialinfluencepercentile90days = surveyCriteriaXML.getMinsocialinfluencepercentile90days();
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
                //Load panels
                List results = HibernateUtil.getSession().createQuery("from Surveypanel where surveyid='"+Jsf.getUserSession().getCurrentSurveyid()+"'").list();
                panels = new String[results.size()];
                int i = 0;
                for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                    Surveypanel surveypanel = (Surveypanel) iterator.next();
                    panels[i]=String.valueOf(surveypanel.getPanelid());
                    i=i+1;
                }
            }
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

    public String saveSurveyAsDraft(){
        String save = saveSurvey();
        if (save!=null){
            return "researchersurveylist";
        } else {
            return save;
        }
    }

    public String previousStep(){
        String save = saveSurvey();
        if (save!=null && save.equals("success")){
            ResearcherSurveyDetail03 bean = (ResearcherSurveyDetail03)Jsf.getManagedBean("researcherSurveyDetail03");
            return bean.beginView();
            //return "researchersurveydetail_03";
        } else {
            return save;
        }
    }

    public String saveSurvey(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_DRAFT){
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
                    surveyCriteriaXML.setMinsocialinfluencepercentile(minsocialinfluencepercentile);
                    surveyCriteriaXML.setMinsocialinfluencepercentile90days(minsocialinfluencepercentile90days);
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

                //Save panels
                if (true){
                    //Delete those that aren't in the
                    ArrayList<Integer> surveypanelstodelete = new ArrayList<Integer>();
                    List surveypanels = HibernateUtil.getSession().createQuery("from Surveypanel where surveyid='"+Jsf.getUserSession().getCurrentSurveyid()+"'").list();
                    for (Iterator iterator = surveypanels.iterator(); iterator.hasNext();) {
                        Surveypanel surveypanel = (Surveypanel) iterator.next();
                        //Iterate panels chosen on UI
                        boolean ischosen = false;
                        if (panels!=null){
                            for (int i = 0; i < panels.length; i++) {
                                String p = panels[i];
                                if (Num.isinteger(p)){
                                    if (Integer.parseInt(p)==surveypanel.getPanelid()){
                                        ischosen = true;
                                    }
                                }
                            }
                        }
                        if (!ischosen){
                            surveypanelstodelete.add(surveypanel.getSurveypanelid());
                        }
                    }
                    //Do the deleting
                    for (Iterator<Integer> iterator = surveypanelstodelete.iterator(); iterator.hasNext();) {
                        Integer surveypanelid = iterator.next();
                        try{Surveypanel.get(surveypanelid).delete();}catch(Exception ex){logger.error(ex);}
                    }
                    //Find panelids to add
                    ArrayList<Integer> panelstoadd = new ArrayList<Integer>();
                    if (panels!=null){
                        for (int i = 0; i < panels.length; i++) {
                            String p = panels[i];
                            if (Num.isinteger(p)){
                                //Search database for this listing
                                List sps = HibernateUtil.getSession().createQuery("from Surveypanel where surveyid='"+Jsf.getUserSession().getCurrentSurveyid()+"' and panelid='"+p+"'").list();
                                if (sps!=null && sps.size()>0){

                                } else {
                                    panelstoadd.add(Integer.parseInt(p));
                                }
                            }
                        }
                    }
                    //Do the adding
                    for (Iterator<Integer> iterator = panelstoadd.iterator(); iterator.hasNext();) {
                        Integer panelid = iterator.next();
                        Surveypanel sp = new Surveypanel();
                        sp.setSurveyid(Jsf.getUserSession().getCurrentSurveyid());
                        sp.setPanelid(panelid);
                        try{sp.save();}catch(Exception ex){logger.error(ex);}
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

        }
        ResearcherSurveyDetail05 bean = (ResearcherSurveyDetail05)Jsf.getManagedBean("researcherSurveyDetail05");
        return bean.beginView();
        //return "researchersurveydetail_05";
    }

    public List getPanelsavailable(){
        ArrayList out = new ArrayList();
        List results = HibernateUtil.getSession().createQuery("from Panel where researcherid='"+Jsf.getUserSession().getUser().getResearcherid()+"'").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Panel panel = (Panel) iterator.next();
            SelectItem item = new SelectItem(String.valueOf(panel.getPanelid()), Str.truncateString(panel.getName(), 40));
            out.add(item);
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

    public int getMinsocialinfluencepercentile90days() {
        return minsocialinfluencepercentile90days;
    }

    public void setMinsocialinfluencepercentile90days(int minsocialinfluencepercentile90days) {
        this.minsocialinfluencepercentile90days = minsocialinfluencepercentile90days;
    }
}
