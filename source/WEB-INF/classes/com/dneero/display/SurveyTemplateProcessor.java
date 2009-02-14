package com.dneero.display;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.util.Str;
import com.dneero.helpers.NicknameHelper;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:36:39 PM
 */
public class SurveyTemplateProcessor {

    private Survey survey;
    private Blogger blogger;
    private Response response;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public SurveyTemplateProcessor(Survey survey, Blogger blogger){
        this.survey = survey;
        this.blogger = blogger;
        if (survey!=null && blogger!=null){
            logger.debug("blogger.getBloggerid()="+blogger.getBloggerid());
            logger.debug("survey.getSurveyid()="+survey.getSurveyid());
            List results = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+blogger.getBloggerid()+"' and surveyid='"+survey.getSurveyid()+"'").list();
            logger.debug("results.size()="+results.size());
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Response resp = (Response) iterator.next();
                if (response==null){
                    response=resp;
                }
                //Choose the most recent response by this blogger... there should generally only be one
                if (response.getResponsedate().before(resp.getResponsedate())){
                    response=resp;
                }
            }
        } else {
            logger.debug("survey==null and blogger==null");
        }
        if (response==null){
            logger.debug("response==null");
        } else {
            logger.debug("response!=null responseid="+response.getResponseid());
        }
    }

    public SurveyTemplateProcessor(Survey survey, Blogger blogger, Response response){
        this.survey = survey;
        this.blogger = blogger;
        this.response = response;
    }

    public String getSurveyForTaking(boolean makeHttpsIfSSLIsOn){
        if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
            return SurveyTemplateProcessorV1.getSurveyForTaking(survey, blogger, response, makeHttpsIfSSLIsOn);
        } else if (survey.getEmbedversion()==Survey.EMBEDVERSION_02){
            return SurveyTemplateProcessorV2.getSurveyForTaking(survey, blogger, response, makeHttpsIfSSLIsOn);
        }
        return "";
    }


    public String getSurveyForDisplay(boolean makeHttpsIfSSLIsOn, boolean displayEvenIfSysadminRejected){
        if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
            return SurveyTemplateProcessorV1.getSurveyForDisplay(survey, blogger, response, makeHttpsIfSSLIsOn, displayEvenIfSysadminRejected);
        } else if (survey.getEmbedversion()==Survey.EMBEDVERSION_02){
            return SurveyTemplateProcessorV2.getSurveyForDisplay(survey, blogger, response, makeHttpsIfSSLIsOn, displayEvenIfSysadminRejected);
        }
        return "";
    }





    public static String getAutoGeneratedTemplateForSurvey(Survey survey){
        if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
            return SurveyTemplateProcessorV1.getAutoGeneratedTemplateForSurvey(survey);
        } else if (survey.getEmbedversion()==Survey.EMBEDVERSION_02){
            return SurveyTemplateProcessorV2.getAutoGeneratedTemplateForSurvey(survey);
        }
        return "";
    }

    public static String appendExtraQuestionsIfNecessary(Survey survey, String currentTemplate){
        if (survey.getEmbedversion()==Survey.EMBEDVERSION_01){
            return SurveyTemplateProcessorV1.appendExtraQuestionsIfNecessary(survey, currentTemplate);
        } else if (survey.getEmbedversion()==Survey.EMBEDVERSION_02){
            return SurveyTemplateProcessorV2.appendExtraQuestionsIfNecessary(survey, currentTemplate);
        }
        return "";
    }

    public static String getHtmlForSurveyTaking(Survey survey, Blogger blogger, boolean makeHttpsIfSSLIsOn, User userwhoreferred){
        Logger logger = Logger.getLogger(SurveyTemplateProcessor.class);
        SurveyTemplateProcessor stp = new SurveyTemplateProcessor(survey, blogger);
        return stp.getSurveyForTaking(makeHttpsIfSSLIsOn);
    }


    public Response getResponse() {
        return response;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public Survey getSurvey() {
        return survey;
    }
}
