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
        if (survey.getTemplate().length()>0){
            logger.debug("survey.getTemplate().length()>0 = true");
            return applyTemplate();
        } else {
            logger.debug("survey.getTemplate().length()>0 = false");
            StringBuffer out = new StringBuffer();
            for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
                Question question = iterator.next();
                logger.debug("found question.getQuestionid()="+question.getQuestionid());
                Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
                logger.debug("found component.getName()="+component.getName());
                out.append(component.getHtmlForDisplay());
                if (iterator.hasNext()){
                    out.append("<br/>");
                }
            }
            return out.toString();
        }
    }

    public String applyTemplate(){
        StringBuffer out = new StringBuffer();
        String template = survey.getTemplate();


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


}
