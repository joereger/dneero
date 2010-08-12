package com.dneero.survey.servlet;

import com.dneero.cache.html.DbcacheexpirableCache;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Aug 11, 2010
 * Time: 11:08:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class SurveyDisplayCacheFlusher {

    public static void flush(int surveyid){
        DbcacheexpirableCache.flush("PublicSurveyResults.java-surveyid-"+surveyid);
        DbcacheexpirableCache.flush("PublicSurveyWhotookit.java-surveyid-"+surveyid);
  
    }


}
