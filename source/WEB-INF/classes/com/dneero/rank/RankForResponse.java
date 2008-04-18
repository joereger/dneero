package com.dneero.rank;

import com.dneero.dao.Question;
import com.dneero.dao.Rank;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 18, 2008
 * Time: 10:01:22 AM
 */
public class RankForResponse {

    public static void processAndSave(Response response){
        Survey survey = Survey.get(response.getSurveyid());
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            RankForQuestion.processAndSave(question, response);
        }
    }

    public static ArrayList<RankUnit> calculateSpecificRank(Rank rank, Response response){
        ArrayList<RankUnit> rankUnits = new ArrayList<RankUnit>();
        Survey survey = Survey.get(response.getSurveyid());
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            rankUnits.addAll(RankForQuestion.calculatePointsForSpecificRank(rank, question, response));
        }
        return rankUnits;
    }





}
