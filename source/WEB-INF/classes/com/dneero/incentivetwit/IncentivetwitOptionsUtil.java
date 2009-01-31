package com.dneero.incentivetwit;

import com.dneero.dao.Surveyincentive;
import com.dneero.dao.Surveyincentiveoption;
import com.dneero.dao.Twitaskincentive;
import com.dneero.dao.Twitaskincentiveoption;
import com.dneero.incentive.IncentiveOptionsUtil;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 2:58:29 PM
 */
public class IncentivetwitOptionsUtil {

    public static String getValue(Twitaskincentive twitaskincentive, String name){
        if (twitaskincentive!=null && twitaskincentive.getTwitaskincentiveoptions()!=null){
            for (Iterator<Twitaskincentiveoption> iterator=twitaskincentive.getTwitaskincentiveoptions().iterator(); iterator.hasNext();){
                Twitaskincentiveoption twitaskincentiveoption=iterator.next();
                if (twitaskincentiveoption.getName().equals(name)){
                    return twitaskincentiveoption.getValue();
                }
            }
        }
        return "";
    }

    public static void saveValue(Twitaskincentive twitaskincentive, String name, String value){
        Logger logger = Logger.getLogger(IncentivetwitOptionsUtil.class);
        //See if an option with this value already exists
        if (twitaskincentive!=null && twitaskincentive.getTwitaskincentiveoptions()!=null){
            for (Iterator<Twitaskincentiveoption> iterator=twitaskincentive.getTwitaskincentiveoptions().iterator(); iterator.hasNext();){
                Twitaskincentiveoption surveyincentiveoption=iterator.next();
                if (surveyincentiveoption.getName().equals(name)){
                    logger.debug("Found Surveyincentiveoption to save value="+value+" surveyincentiveoption.getValue()="+surveyincentiveoption.getValue()+" to surveyincentiveid="+twitaskincentive.getTwitaskincentiveid() +" surveyincentiveoptionid="+surveyincentiveoption.getTwitaskincentiveoptionid());
                    surveyincentiveoption.setValue(value);
                    try{surveyincentiveoption.save();twitaskincentive.refresh();}catch(Exception ex){logger.error("", ex);}
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
        Twitaskincentiveoption surveyincentiveoption = new Twitaskincentiveoption();
        surveyincentiveoption.setTwitaskincentiveid(twitaskincentive.getTwitaskincentiveid());
        surveyincentiveoption.setName(name);
        surveyincentiveoption.setValue(value);
        try{surveyincentiveoption.save();twitaskincentive.refresh();}catch(Exception ex){logger.error("", ex);}
        return;
    }


}