package com.dneero.htmluibeans;


import com.dneero.dao.Question;
import com.dneero.dao.Questionconfig;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.CopyHibernateObject;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Mar 12, 2007
 * Time: 2:59:18 PM
 */
public class ResearcherIndex implements Serializable {

    private String msg = "";

    public ResearcherIndex(){
        
    }



    public void initBean(){

    }


    public void copy() throws ValidationException {
        ValidationException vex = new ValidationException();
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
            //@todo don't copy user questions
            //Refresh the survey
            try{newSurvey.refresh();}catch(Exception ex){logger.error("",ex);}
        }
        initBean();

    }

  


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
