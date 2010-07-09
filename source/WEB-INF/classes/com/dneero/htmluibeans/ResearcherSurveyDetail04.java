package com.dneero.htmluibeans;

import com.dneero.dao.Panel;
import com.dneero.dao.Pl;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.finders.DemographicsXML;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.helpers.UserInputSafe;
import com.dneero.htmlui.Pagez;
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

    private SurveyCriteriaXML surveyCriteriaXML;
    private DemographicsXML demographicsXML;

    private int status;
    private int agemin = 13;
    private int agemax = 100;
    private int minsocialinfluencepercentile = 100;
    private int dayssincelastsurvey = 0;
    private int totalsurveystakenatleast = 0;
    private int totalsurveystakenatmost = 100000;
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
        logger.debug("initBean called");
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
                surveyCriteriaXML = new SurveyCriteriaXML(survey.getSurveycriteriaxml(), Pl.get(survey.getPlid()));
                demographicsXML = surveyCriteriaXML.getDemographicsXML(); //This is set by the surveyCriteriaXML now... later it's set by the jsp
                agemin = surveyCriteriaXML.getAgemin();
                agemax = surveyCriteriaXML.getAgemax();
                minsocialinfluencepercentile = surveyCriteriaXML.getMinsocialinfluencepercentile();
                dayssincelastsurvey = surveyCriteriaXML.getDayssincelastsurvey();
                totalsurveystakenatleast = surveyCriteriaXML.getTotalsurveystakenatleast();
                totalsurveystakenatmost = surveyCriteriaXML.getTotalsurveystakenatmost();
                dneerousagemethods = surveyCriteriaXML.getDneerousagemethods();
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
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){

                //Coupon validation
                if (isaccesscodeonly && accesscode.equals("")){
                    throw new ValidationException("If you choose Access Code then you must enter an Access Code.");
                }
                if (isaccesscodeonly){
                    int numsurveyswithsameaccesscode = NumFromUniqueResult.getInt("select count(*) from Survey where accesscode='"+ UserInputSafe.clean(accesscode)+"' and surveyid<>'"+survey.getSurveyid()+"'");
                    if (numsurveyswithsameaccesscode>0){
                        throw new ValidationException("That Access Code already exists in the system.  Please choose another.");
                    }
                }

                //Do it with XML
                if (true){
                    surveyCriteriaXML = new SurveyCriteriaXML(survey.getSurveycriteriaxml(), Pl.get(survey.getPlid()));
                    surveyCriteriaXML.setDemographicsXML(demographicsXML); //The jsp calls demographicsXML directly to set up values
                    surveyCriteriaXML.setAgemin(agemin);
                    surveyCriteriaXML.setAgemax(agemax);
                    surveyCriteriaXML.setMinsocialinfluencepercentile(minsocialinfluencepercentile);
                    surveyCriteriaXML.setDayssincelastsurvey(dayssincelastsurvey);
                    surveyCriteriaXML.setTotalsurveystakenatleast(totalsurveystakenatleast);
                    surveyCriteriaXML.setTotalsurveystakenatmost(totalsurveystakenatmost);
                    surveyCriteriaXML.setDneerousagemethods(dneerousagemethods);
                    surveyCriteriaXML.setPanelids(panels);
                    surveyCriteriaXML.setSuperpanelids(superpanels);
                    //Put into survey
                    survey.setCriteriaxml(surveyCriteriaXML.getSurveyCriteriaAsString());
                    survey.setSurveycriteriaxml(surveyCriteriaXML.getSurveyCriteriaAsString());
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

    public DemographicsXML getDemographicsXML() {
        return demographicsXML;
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
