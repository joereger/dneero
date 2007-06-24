package com.dneero.test;

import com.dneero.survey.servlet.SurveydisplayActivityObject;
import com.dneero.dao.Survey;

import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jun 21, 2007
 * Time: 11:19:47 AM
 */
public class HibernateStaticMemoryClustering {

    private static ArrayList<Survey> surveys = new ArrayList<Survey>();
    private static int[] surveyids = new int[0];


    public static ArrayList<Survey> getSurveys() {
        return surveys;
    }

    public static void setSurveys(ArrayList<Survey> surveys) {
        HibernateStaticMemoryClustering.surveys = surveys;
    }

    public static int[] getSurveyids() {
        return surveyids;
    }

    public static void setSurveyids(int[] surveyids) {
        HibernateStaticMemoryClustering.surveyids = surveyids;
    }
}
