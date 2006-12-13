package com.dneero.display;

import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 14, 2006
 * Time: 12:34:47 PM
 */
public class SurveyTakerDisplay {

    public static String getHtmlForSurveyTaking(Survey survey, Blogger blogger, boolean makeHttpsIfSSLIsOn){
        Logger logger = Logger.getLogger(SurveyTakerDisplay.class);
        SurveyTemplateProcessor stp = new SurveyTemplateProcessor(survey, blogger);
        return stp.getSurveyForTaking(makeHttpsIfSSLIsOn);
    }


}
