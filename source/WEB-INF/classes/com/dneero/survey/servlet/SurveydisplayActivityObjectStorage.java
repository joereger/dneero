package com.dneero.survey.servlet;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class SurveydisplayActivityObjectStorage {

    public static void store(SurveydisplayActivityObject sdao){
        Logger logger = Logger.getLogger(SurveydisplayActivityObjectStorage.class);
        //Find the survey
        Survey survey = null;
        if (sdao.getSurveyid()>0){
            survey = Survey.get(sdao.getSurveyid());
            if (survey==null || survey.getSurveyid()<=0){
                //Error, survey not found, don't record
                logger.debug("Surveyid="+ sdao.getSurveyid()+" not found so aborting surveyactivity save.");
                return;
            }
            survey.setPublicsurveydisplays(survey.getPublicsurveydisplays()+1);
            try{survey.save();} catch (GeneralException gex){logger.error(gex);}
        }
    }



}
