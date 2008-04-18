package com.dneero.htmluibeans;

import com.dneero.dao.Panel;
import com.dneero.dao.Respondentfilter;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class ResearcherPanelsAddpeople implements Serializable {

    private int panelid=0;
    private int surveyid=0;
    private int respondentfilterid=0;

    public ResearcherPanelsAddpeople(){

    }



    public void initBean(){
        if (Pagez.getRequest().getParameter("panelid")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            panelid = Integer.parseInt(Pagez.getRequest().getParameter("panelid"));
        }
        if (Pagez.getRequest().getParameter("surveyid")!=null && Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        }
        if (Pagez.getRequest().getParameter("respondentfilterid")!=null && Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            respondentfilterid = Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"));
        }
    }



    public void add() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public TreeMap<String, String> getSurveys(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        //out.put("0", "0 - Undefined");
        //@todo allow to choose users across all surveys???
        List<Survey> surveyd = HibernateUtil.getSession().createCriteria(Survey.class)
                                           .add(Restrictions.eq("researcherid", Pagez.getUserSession().getUser().getResearcherid()))
                                           .addOrder(Order.desc("surveyid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Survey> surveyIterator = surveyd.iterator(); surveyIterator.hasNext();) {
            Survey survey = surveyIterator.next();
            out.put(String.valueOf(survey.getSurveyid()), Str.truncateString(survey.getTitle(), 60));
        }
        return out;
    }

    public TreeMap<String, String> getPanels(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        //out.put("0", "0 - Undefined");
        List<Panel> panels = HibernateUtil.getSession().createCriteria(Panel.class)
                                           .add(Restrictions.eq("researcherid", Pagez.getUserSession().getUser().getResearcherid()))
                                           .addOrder(Order.desc("panelid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Panel> panelIterator = panels.iterator(); panelIterator.hasNext();) {
            Panel panel = panelIterator.next();
            out.put(String.valueOf(panel.getPanelid()), Str.truncateString(panel.getName(), 60));
        }
        return out;
    }

    public TreeMap<String, String> getRespondentfilters(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put("0", "Apply no Filter, Use All");
        List<Respondentfilter> respondentfilters = HibernateUtil.getSession().createCriteria(Respondentfilter.class)
                                           .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                           .addOrder(Order.desc("respondentfilterid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Respondentfilter> filterIterator = respondentfilters.iterator(); filterIterator.hasNext();) {
            Respondentfilter filter = filterIterator.next();
            out.put(String.valueOf(filter.getRespondentfilterid()), Str.truncateString(filter.getName(), 60));
        }
        return out;
    }

    public int getPanelid() {
        return panelid;
    }

    public void setPanelid(int panelid) {
        this.panelid = panelid;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getRespondentfilterid() {
        return respondentfilterid;
    }

    public void setRespondentfilterid(int respondentfilterid) {
        this.respondentfilterid = respondentfilterid;
    }
}