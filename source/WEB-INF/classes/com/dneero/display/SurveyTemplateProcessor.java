package com.dneero.display;

import com.dneero.dao.Survey;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:36:39 PM
 */
public class SurveyTemplateProcessor {

    private Survey survey;

    public SurveyTemplateProcessor(Survey survey){
        this.survey = survey;
    }

    public String getSurveyForDisplay(){
        //@todo get questions, apply template.
        return "";
    }


}
