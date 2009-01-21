package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.HibernateUtilImpressions;


import com.dneero.util.Num;
import com.dneero.htmlui.Pagez;
import com.dneero.dbgrid.Grid;
import com.dneero.dbgrid.GridCol;
import com.dneero.cache.html.DbcacheexpirableCache;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.helpers.NicknameHelper;

import java.io.Serializable;
import java.util.*;

import org.apache.log4j.Logger;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyWhotookit implements Serializable {

    private Survey survey;
    private String whotookitHtml;
    private String impressionsHtml;


    public PublicSurveyWhotookit(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciated.");

        //Surveyid from session or url
        int surveyid = Pagez.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("s"))) {
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("s"));
        }
        Pagez.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0){
            return;
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            return;
        }

        //WhotookitHtml
        if (1==1){
            String key = "surveywhotookit.jsp-whotookitHtml-surveyid"+survey.getSurveyid();
            String group = "PublicSurveyWhotookit.java-surveyid-"+survey.getSurveyid();
            Object fromCache = DbcacheexpirableCache.get(key, group);
            if (fromCache!=null){
                try{whotookitHtml = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                whotookitHtml = generateWhotookitHtml();
                DbcacheexpirableCache.put(key, group, whotookitHtml, DbcacheexpirableCache.expireSurveyInXHrs(survey, 3));
            }
        }

        //ImpressionsHtml
        if (1==1){
            int pageimpressions = 1;
            if (Pagez.getRequest().getParameter("pageimpressions")!=null && Num.isinteger(Pagez.getRequest().getParameter("pageimpressions"))){
                pageimpressions = Integer.parseInt(Pagez.getRequest().getParameter("pageimpressions"));
            }
            String key = "surveywhotookit.jsp-impressionsHtml-surveyid"+survey.getSurveyid()+"-pageimpressions"+pageimpressions;
            String group = "PublicSurveyWhotookit.java-surveyid-"+survey.getSurveyid();
            Object fromCache = DbcacheexpirableCache.get(key, group);
            if (fromCache!=null){
                try{impressionsHtml = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                impressionsHtml = generateImpressionsHtml();
                DbcacheexpirableCache.put(key, group, impressionsHtml, DbcacheexpirableCache.expireSurveyInXHrs(survey, 12));
            }
        }



    }

    private String generateWhotookitHtml(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        StringBuffer out = new StringBuffer();
        //Load respondents, not including those whose content was rejected
        List<PublicSurveyRespondentsListitem> respondents = new ArrayList();
        List resp = HibernateUtil.getSession().createQuery("from Response where surveyid='"+survey.getSurveyid()+"' and issysadminrejected=false order by responseid desc").setCacheable(true).list();
        for (Iterator iterator = resp.iterator(); iterator.hasNext();) {
            Response response = (Response) iterator.next();
            User user = User.get(Blogger.get(response.getBloggerid()).getUserid());
            if (user.getIsenabled()){
                PublicSurveyRespondentsListitem psrli = new PublicSurveyRespondentsListitem();
                psrli.setResponse(response);
                psrli.setUser(user);
                psrli.setNameornickname(NicknameHelper.getNameOrNickname(user));
                respondents.add(psrli);
            }
        }
        if (respondents==null || respondents.size()==0){
                out.append("<font class=\"normalfont\">Nobody has joined this conversation... yet.</font>");
            } else {
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Date", "<$response.responsedate|"+ Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont"));
                cols.add(new GridCol("Person", "<a href=\"/profile.jsp?userid=<$user.userid$>\"><font class=\"normalfont\" style=\"font-weight: bold;\"><$nameornickname$></font></a>", false, "", ""));
                cols.add(new GridCol("", "<a href=\"/survey.jsp?u=<$user.userid$>&p=0&r=<$response.responseid$>\"><font class=\"tinyfont\" style=\"font-weight: bold;\"><$nameornickname$>'s Answers</font></a>", true, "", ""));
                out.append(Grid.render(respondents, cols, 500, "/surveywhotookit.jsp?surveyid="+survey.getSurveyid(), "pagewhotookit"));
            }
        return out.toString();
    }

    private String generateImpressionsHtml(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        StringBuffer out = new StringBuffer();
        //Load impressions
        List<Impression> impressions = HibernateUtilImpressions.getSession().createQuery("from Impression where surveyid='"+survey.getSurveyid()+"' and referer<>'' order by impressionstotal desc").setCacheable(true).list();

        if (impressions==null || impressions.size()==0){
            out.append("<font class=\"normalfont\">Not posted anywhere... yet.</font>");
        } else {
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Web Address", "<a href=\"<$referer$>\"><font class=\"smallfont\" style=\"font-weight: bold;\">See It!</font></a>", true, "", "tinyfont"));
            cols.add(new GridCol("Impressions", "<$impressionstotal$>", true, "", "smallfont", "", "font-weight: bold;"));
            out.append(Grid.render(impressions, cols, 100, "/surveywhotookit.jsp?surveyid="+survey.getSurveyid(), "pageimpressions"));
        }
        return out.toString();
    }

    public String getWhotookitHtml() {
        return whotookitHtml;
    }

    public void setWhotookitHtml(String whotookitHtml) {
        this.whotookitHtml=whotookitHtml;
    }

    public String getImpressionsHtml() {
        return impressionsHtml;
    }

    public void setImpressionsHtml(String impressionsHtml) {
        this.impressionsHtml=impressionsHtml;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }


}
