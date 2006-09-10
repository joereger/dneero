package com.dneero.display;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Question;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 14, 2006
 * Time: 12:34:47 PM
 */
public class SurveyResultsDisplay {

    public static String getHtmlForResults(Survey survey){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyResultsDisplay.class);
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, new Blogger());
            logger.debug("found component.getName()="+component.getName());
            out.append("<br><br>");
            out.append("<table width=100% cellpadding=0 cellspacing=0 border=0>");
            out.append("<tr>");
            out.append("<td valign=top bgcolor=#e6e6e6>");
            out.append("<b>"+question.getQuestion()+"</b>");
            out.append("</td>");
            out.append("</tr>");
            out.append("</table>");
            out.append("<table width=100% cellpadding=0 cellspacing=0 border=0>");
            out.append("<tr>");
            out.append("<td valign=top>");
            out.append(component.getHtmlForResult());
            out.append("</td>");
            out.append("</tr>");
            out.append("</table>");
        }
        return out.toString();
    }


}
