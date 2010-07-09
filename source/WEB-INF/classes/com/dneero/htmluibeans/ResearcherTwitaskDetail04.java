package com.dneero.htmluibeans;

import com.dneero.dao.Panel;
import com.dneero.dao.Pl;
import com.dneero.dao.Twitask;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.DemographicsXML;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
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
public class ResearcherTwitaskDetail04 implements Serializable {

    private String title;
    private Twitask twitask;

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
    private boolean isopentoanybody=true;

    public ResearcherTwitaskDetail04(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        if (Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get((Integer.parseInt(Pagez.getRequest().getParameter("twitaskid"))));
        }
        if (twitask!=null){
            logger.debug("Found twitask in db: twitask.getTwitaskid()="+twitask.getTwitaskid()+" twitask.getQuestion()="+twitask.getQuestion());
            title = twitask.getQuestion();
            status = twitask.getStatus();
            isopentoanybody = twitask.getIsopentoanybody();
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){
                logger.debug("twitask.canEdit(Pagez.getUserSession().getUser())="+twitask.canEdit(Pagez.getUserSession().getUser()));
                surveyCriteriaXML = new SurveyCriteriaXML(twitask.getCriteriaxml(), Pl.get(twitask.getPlid()));
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





    public void save() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("save() called.");
        if (status<=Twitask.STATUS_DRAFT){
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){


                //Do it with XML
                if (!isopentoanybody){
                    SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML(twitask.getCriteriaxml(), Pl.get(twitask.getPlid()));
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
                    twitask.setCriteriaxml(surveyCriteriaXML.getSurveyCriteriaAsString());
                }
               //Final save
                try{
                    logger.debug("save() about to save (for 2nd time) twitask.getTwitaskid()=" + twitask.getTwitaskid());
                    twitask.setIsopentoanybody(isopentoanybody);
                    twitask.save();
                    logger.debug("save() done saving (for 2nd time) twitask.getTwitaskid()=" + twitask.getTwitaskid());
                } catch (GeneralException gex){
                    logger.debug("save() failed: " + gex.getErrorsAsSingleString());
                    String message = "save() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                //Refresh
                twitask.refresh();
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

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }

    public boolean getIsopentoanybody() {
        return isopentoanybody;
    }

    public void setIsopentoanybody(boolean isopentoanybody) {
        this.isopentoanybody = isopentoanybody;
    }
}