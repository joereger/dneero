package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.finders.FindResponses;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class CustomercarePanelsAddpeopleconfirm implements Serializable {

    private int panelid;
    private int surveyid;
    private int respondentfilterid;
    private Panel panel;
    private int numberofrespondents;
    private int numberofnewrespondents;
    private String bloggeridstoaddcommasep="";
    private ArrayList<Response> responses;

    public CustomercarePanelsAddpeopleconfirm(){

    }



    public void initBean(){
        if (Pagez.getRequest().getParameter("panelid")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            panelid = Integer.parseInt(Pagez.getRequest().getParameter("panelid"));
            panel = Panel.get(panelid);
        }
        if (Pagez.getRequest().getParameter("surveyid")!=null && Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        }
        if (Pagez.getRequest().getParameter("respondentfilterid")!=null && Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            respondentfilterid = Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"));
        }
        findRespondents();
    }

    private void findRespondents(){
        if (panelid>0 && surveyid>0){
            SurveyCriteriaXML scXML = null;
            if (respondentfilterid>0){
                Respondentfilter filter = Respondentfilter.get(respondentfilterid);
                scXML = new SurveyCriteriaXML(filter.getCriteriaxml());
            }
            //Go get the responses
            responses = FindResponses.find(Survey.get(surveyid), scXML);
            numberofrespondents = responses.size();
            //Figure out how many are new to the panel
            numberofnewrespondents = 0;
            Panel panel = Panel.get(panelid);
            for (Iterator<Response> responseIterator = responses.iterator(); responseIterator.hasNext();) {
                Response response = responseIterator.next();
                if (!isInPanel(panel, response.getBloggerid())){
                    numberofnewrespondents = numberofnewrespondents + 1;
                    if (numberofnewrespondents>1){
                        bloggeridstoaddcommasep = bloggeridstoaddcommasep + ",";
                    }
                    bloggeridstoaddcommasep = bloggeridstoaddcommasep + response.getBloggerid();
                }
            }
        }
    }

    private boolean isInPanel(Panel panel, int bloggerid){
        if (panel.canEdit(Pagez.getUserSession().getUser())){
            for (Iterator<Panelmembership> panelIterator = panel.getPanelmemberships().iterator(); panelIterator.hasNext();){
                Panelmembership panelmembership = panelIterator.next();
                if (panelmembership.getBloggerid()==bloggerid){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInResponses(int bloggerid){
        if (responses!=null){
            for (Iterator<Response> responseIterator = responses.iterator(); responseIterator.hasNext();) {
                Response response = responseIterator.next();
                if (response.getBloggerid()==bloggerid){
                    return true;
                }
            }
        }
        return false;
    }



    public void add() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        String[] bloggerids = bloggeridstoaddcommasep.split(",");
        Panel panel = Panel.get(panelid);
        for (int i = 0; i < bloggerids.length; i++) {
            String bloggeridStr = bloggerids[i];
            if (Num.isinteger(bloggeridStr)){
                int bloggerid = Integer.parseInt(bloggeridStr);
                if (!isInPanel(panel, bloggerid)){
                    Panelmembership panelmembership = new Panelmembership();
                    panelmembership.setBloggerid(bloggerid);
                    panelmembership.setPanelid(panelid);
                    panelmembership.setIssysadminrejected(false);
                    panelmembership.setIssysadminreviewed(false);
                    try{panelmembership.save();}catch(Exception ex){logger.error("", ex);}
                }
            }
        }
        try{panel.refresh();}catch(Exception ex){logger.error("", ex);}
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

    public int getNumberofrespondents() {
        return numberofrespondents;
    }

    public void setNumberofrespondents(int numberofrespondents) {
        this.numberofrespondents = numberofrespondents;
    }

    public String getBloggeridstoaddcommasep() {
        return bloggeridstoaddcommasep;
    }

    public void setBloggeridstoaddcommasep(String bloggeridstoaddcommasep) {
        this.bloggeridstoaddcommasep = bloggeridstoaddcommasep;
    }

    public ArrayList<Response> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<Response> responses) {
        this.responses = responses;
    }

    public int getNumberofnewrespondents() {
        return numberofnewrespondents;
    }

    public void setNumberofnewrespondents(int numberofnewrespondents) {
        this.numberofnewrespondents = numberofnewrespondents;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }
}