package com.dneero.incentive;

import com.dneero.dao.Surveyincentive;
import com.dneero.dao.Surveyincentiveoption;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 2:58:29 PM
 */
public class IncentiveOptionsUtil {

    public static String getValue(Surveyincentive surveyincentive, String name){
        if (surveyincentive!=null && surveyincentive.getSurveyincentiveoptions()!=null){
            for (Iterator<Surveyincentiveoption> iterator=surveyincentive.getSurveyincentiveoptions().iterator(); iterator.hasNext();){
                Surveyincentiveoption surveyincentiveoption=iterator.next();
                if (surveyincentiveoption.getName().equals(name)){
                    return surveyincentiveoption.getValue();
                }
            }
        }
        return "";
    }

    public static void saveValue(Surveyincentive surveyincentive, String name, String value){
        Logger logger = Logger.getLogger(IncentiveOptionsUtil.class);
        //See if an option with this value already exists
        if (surveyincentive!=null && surveyincentive.getSurveyincentiveoptions()!=null){
            for (Iterator<Surveyincentiveoption> iterator=surveyincentive.getSurveyincentiveoptions().iterator(); iterator.hasNext();){
                Surveyincentiveoption surveyincentiveoption=iterator.next();
                if (surveyincentiveoption.getName().equals(name)){
                    logger.debug("Found Surveyincentiveoption to save value="+value+" surveyincentiveoption.getValue()="+surveyincentiveoption.getValue()+" to surveyincentiveid="+surveyincentive.getSurveyincentiveid() +" surveyincentiveoptionid="+surveyincentiveoption.getSurveyincentiveoptionid());
                    surveyincentiveoption.setValue(value);
                    try{surveyincentiveoption.save();surveyincentive.refresh();}catch(Exception ex){logger.error("", ex);}
                    return;
                }
            }
//            if (sioToEdit!=null){
//                logger.debug("Found Surveyincentiveoption to save value="+value+" sioToEdit.getValue()="+sioToEdit.getValue()+" to surveyincentiveid="+surveyincentive.getSurveyincentiveid() +" sioToEdit="+sioToEdit.getSurveyincentiveoptionid());
//                sioToEdit.setValue(value);
//                try{sioToEdit.save();surveyincentive.refresh();}catch(Exception ex){logger.error("", ex);}
//                return;
//            }
        }
        //Otherwise, create one
        logger.debug("Creating a new Surveyincentiveoption()");
        Surveyincentiveoption surveyincentiveoption = new Surveyincentiveoption();
        surveyincentiveoption.setSurveyincentiveid(surveyincentive.getSurveyincentiveid());
        surveyincentiveoption.setName(name);
        surveyincentiveoption.setValue(value);
        try{surveyincentiveoption.save();surveyincentive.refresh();}catch(Exception ex){logger.error("", ex);}
        return;
    }


}
