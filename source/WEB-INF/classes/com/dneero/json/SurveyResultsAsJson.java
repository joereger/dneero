package com.dneero.json;

import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.display.SurveyResultsJson;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Sep 5, 2010
 * Time: 7:52:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SurveyResultsAsJson {

    //http://code.google.com/p/json-simple/wiki/EncodingExamples

    public static Map getSurveyJson(Survey survey){
        Logger logger = Logger.getLogger(SurveyResultsAsJson.class);
        Map s = new HashMap();
        s.put("surveyid", survey.getSurveyid());
        s.put("title", survey.getTitle());
        int respondents = NumFromUniqueResult.getInt("select count(*) from Response where surveyid='"+ survey.getSurveyid()+"' and isresearcherrejected=false and issysadminrejected=false");
        s.put("numresponses", respondents);
        s.put("impressionstotal", survey.getImpressionstotal());
        s.put("results", SurveyResultsJson.getResults(survey, null, 0, new ArrayList<Integer>(), null, true, false));
        boolean isopen = false;
        if (survey.getStatus()==Survey.STATUS_OPEN){ isopen = true; }
        s.put("isopen", isopen);
        return s;
    }




}

