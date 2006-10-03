package com.dneero.display;

import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 14, 2006
 * Time: 12:34:47 PM
 */
public class SurveyTakerDisplay {

    public static String getHtmlForSurveyTaking(Survey survey, Blogger blogger){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyTakerDisplay.class);
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
            logger.debug("found component.getName()="+component.getName());
            out.append(component.getHtmlForInput());
            if (iterator.hasNext()){
                out.append("<br/><br/>");
            }
        }
        return out.toString();
    }


}
