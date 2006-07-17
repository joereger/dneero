package com.dneero.display;

import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.util.Str;
import com.dneero.display.components.Component;
import com.dneero.display.components.ComponentTypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:36:39 PM
 */
public class SurveyTemplateProcessor {

    private Survey survey;
    private Blogger blogger;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public SurveyTemplateProcessor(Survey survey, Blogger blogger){
        this.survey = survey;
        this.blogger = blogger;
    }


    public String getSurveyForDisplay(){
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
            Component component = ct.getByTagSyntax(m.group(), blogger);
            if (component!=null){
                m.appendReplacement(out, Str.cleanForAppendreplacement(component.getHtmlForDisplay()));
            }
        }
        try{
            m.appendTail(out);
        } catch (Exception e){
            //Do nothing... just null pointer
        }
        return out.toString();
    }

    public static String getAutoGeneratedTemplateForSurvey(Survey survey){
        StringBuffer out = new StringBuffer();
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            out.append("<$question_"+question.getQuestionid()+"$>"+"\n");
            if (iterator.hasNext()){
                out.append("<br/>"+"\n");
            }
        }
        return out.toString();
    }

    public static String appendExtraQuestionsIfNecessary(Survey survey, String currentTemplate){
        StringBuffer out = new StringBuffer();
        if (currentTemplate!=null){
            out.append(currentTemplate);
        }
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            String qtag = "<$question_"+question.getQuestionid()+"$>";
            if (currentTemplate==null || currentTemplate.indexOf(qtag)<=0){
                if (out.length()>0){
                    out.append("<br/>"+"\n");
                }
                out.append("<$question_"+question.getQuestionid()+"$>"+"\n");
            }
        }
        return out.toString();
    }


}
