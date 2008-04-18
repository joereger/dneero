package com.dneero.rank;

import com.dneero.dao.Rank;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 18, 2008
 * Time: 9:59:31 AM
 */
public class RankForSurvey {

    public static void processAndSave(Survey survey){
        for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            RankForResponse.processAndSave(response);
        }
    }

    public static ArrayList<RankUnit> calculateSpecificRank(Rank rank, Survey survey){
        ArrayList<RankUnit> rankUnits = new ArrayList<RankUnit>();
        for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            rankUnits.addAll(RankForResponse.calculateSpecificRank(rank, response));
        }
        return rankUnits;
    }


}
