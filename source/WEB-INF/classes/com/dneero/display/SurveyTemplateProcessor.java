package com.dneero.display;

import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Str;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.systemprops.BaseUrl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

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

    public String getSurveyForTaking(boolean makeHttpsIfSSLIsOn){
        StringBuffer out = new StringBuffer();
        String template = survey.getTemplate();
        if (template==null){
            template = "";
        }
        if (template.equals("")){
            template = getAutoGeneratedTemplateForSurvey(survey);
        }
        template = appendExtraQuestionsIfNecessary(survey, template);

        Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
        Matcher m = p.matcher(template);
        while(m.find()) {
            ComponentTypes ct = new ComponentTypes();
            Component component = ct.getByTagSyntax(m.group(), blogger, survey);
            if (component!=null){
                m.appendReplacement(out, Str.cleanForAppendreplacement(component.getHtmlForInput() + "<br>"));
            }
        }
        try{
            m.appendTail(out);
        } catch (Exception e){
            //Do nothing... just null pointer
        }
        return "<div style=\"background : #ffffff; border: 5px solid #cccccc; padding : 5px; width : 425px; overflow : auto;\">"+out.toString()+"</div>";
    }


    public String getSurveyForDisplay(boolean makeHttpsIfSSLIsOn){
        StringBuffer out = new StringBuffer();
        String template = survey.getTemplate();
        if (template==null){
            template = "";
        }
        if (template.equals("")){
            template = getAutoGeneratedTemplateForSurvey(survey);
        }
        template = appendExtraQuestionsIfNecessary(survey, template);

        Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
        Matcher m = p.matcher(template);
        while(m.find()) {
            ComponentTypes ct = new ComponentTypes();
            Component component = ct.getByTagSyntax(m.group(), blogger, survey);
            if (component!=null){
                m.appendReplacement(out, "<p>"+Str.cleanForAppendreplacement(component.getHtmlForDisplay(response) + "</p>"));
            }
        }
        try{
            m.appendTail(out);
        } catch (Exception e){
            //Do nothing... just null pointer
        }
        return out.toString().trim();
    }



    public static String getAutoGeneratedTemplateForSurvey(Survey survey){
        StringBuffer out = new StringBuffer();
        HibernateUtil.getSession().saveOrUpdate(survey);
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            out.append("\n"+"<p>");
            out.append("<$question_"+question.getQuestionid()+"$>");
            out.append("</p>");
        }
        return out.toString();
    }

    public static String appendExtraQuestionsIfNecessary(Survey survey, String currentTemplate){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyTemplateProcessor.class);
        if (currentTemplate!=null){
            out.append(currentTemplate);
        }
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            String qtag = "<$question_"+question.getQuestionid()+"$>";
            if (currentTemplate==null || currentTemplate.indexOf(qtag)<0){
                out.append("\n"+"<p>");
                out.append("<$question_"+question.getQuestionid()+"$>");
                out.append("</p>");
            }
        }
        return out.toString();
    }


}
