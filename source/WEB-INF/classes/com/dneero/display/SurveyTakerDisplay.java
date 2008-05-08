package com.dneero.display;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jul 14, 2006
 * Time: 12:34:47 PM
 */
public class SurveyTakerDisplay {

    public static String getHtmlForSurveyTaking(Survey survey, Blogger blogger, boolean makeHttpsIfSSLIsOn, User userwhoreferred){
        Logger logger = Logger.getLogger(SurveyTakerDisplay.class);
        SurveyTemplateProcessor stp = new SurveyTemplateProcessor(survey, blogger);
        return stp.getSurveyForTaking(makeHttpsIfSSLIsOn);
    }

    


}
