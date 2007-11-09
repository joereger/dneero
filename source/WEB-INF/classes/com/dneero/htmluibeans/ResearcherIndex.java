package com.dneero.htmluibeans;

import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Questionconfig;
import com.dneero.dao.Surveypanel;
import com.dneero.dao.hibernate.CopyHibernateObject;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Mar 12, 2007
 * Time: 2:59:18 PM
 */
public class ResearcherIndex implements Serializable {

    private boolean showmarketingmaterial = false;
    private String msg = "";

    public ResearcherIndex(){
        
    }



    public void initBean(){
        if (Pagez.getRequest().getParameter("showmarketingmaterial")!=null && Pagez.getRequest().getParameter("showmarketingmaterial").equals("1")){
            showmarketingmaterial = true;
        } else {
            showmarketingmaterial = false;
        }
        ResearcherSurveyList bean = (ResearcherSurveyList)Jsf.getManagedBean("researcherSurveyList");
        bean.beginView();
    }


    public String copy(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        String tmpSurveyid = Pagez.getRequest().getParameter("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("copy(): found surveyid in request param="+tmpSurveyid);
            Survey origSurvey = Survey.get(Integer.parseInt(tmpSurveyid));
            Survey newSurvey = new Survey();
            //Copy the main survey and save it
            ArrayList fieldstoignore = new ArrayList();
            fieldstoignore.add("surveyid");
            newSurvey = (Survey) CopyHibernateObject.shallowCopyIgnoreCertainFields(origSurvey, newSurvey, fieldstoignore);
            newSurvey.setTitle(newSurvey.getTitle()+" (Copy "+ Time.dateformatcompactwithtime(Calendar.getInstance()) +")");
            newSurvey.setStatus(Survey.STATUS_DRAFT);
            try{newSurvey.save();}catch(Exception ex){logger.error("",ex);}
            //Copy the questions
            for (Iterator<Question> iterator = origSurvey.getQuestions().iterator(); iterator.hasNext();) {
                Question origQuestion = iterator.next();
                Question newQuestion = new Question();
                ArrayList fieldstoignore1 = new ArrayList();
                fieldstoignore1.add("questionid");
                newQuestion = (Question) CopyHibernateObject.shallowCopyIgnoreCertainFields(origQuestion, newQuestion, fieldstoignore1);
                newQuestion.setSurveyid(newSurvey.getSurveyid());
                try{newQuestion.save();}catch(Exception ex){logger.error("",ex);}
                //Copy the question configs
                for (Iterator<Questionconfig> iterator1 = origQuestion.getQuestionconfigs().iterator(); iterator1.hasNext();){
                    Questionconfig origQuestionconfig = iterator1.next();
                    Questionconfig newQuestionconfig = new Questionconfig();
                    ArrayList fieldstoignore2 = new ArrayList();
                    fieldstoignore2.add("questionconfigid");
                    newQuestionconfig = (Questionconfig) CopyHibernateObject.shallowCopyIgnoreCertainFields(origQuestionconfig, newQuestionconfig, fieldstoignore2);
                    newQuestionconfig.setQuestionid(newQuestion.getQuestionid());
                    try{newQuestionconfig.save();}catch(Exception ex){logger.error("",ex);}
                }
            }
            //Copy the surveypanels
            for (Iterator<Surveypanel> iterator = origSurvey.getSurveypanels().iterator(); iterator.hasNext();) {
                Surveypanel origSurveypanel = iterator.next();
                Surveypanel newSurveypanel = new Surveypanel();
                ArrayList fieldstoignore1 = new ArrayList();
                fieldstoignore1.add("surveypanelid");
                newSurveypanel = (Surveypanel) CopyHibernateObject.shallowCopyIgnoreCertainFields(origSurveypanel, newSurveypanel, fieldstoignore1);
                newSurveypanel.setSurveyid(newSurvey.getSurveyid());
                try{newSurveypanel.save();}catch(Exception ex){logger.error("",ex);}
            }
            //Refresh the survey
            try{newSurvey.refresh();}catch(Exception ex){logger.error("",ex);}
        }
        Jsf.setFacesMessage("Survey copied!");
        ResearcherIndex bean = (ResearcherIndex)Jsf.getManagedBean("researcherIndex");
        return bean.beginView();
    }

    public boolean getShowmarketingmaterial() {
        return showmarketingmaterial;
    }

    public void setShowmarketingmaterial(boolean showmarketingmaterial) {
        this.showmarketingmaterial = showmarketingmaterial;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
