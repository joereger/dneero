package com.dneero.display;

import com.dneero.htmluibeans.ResearcherRankPeopleListitem;

import java.util.Comparator;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2008
 * Time: 11:40:45 AM
 */
public class SurveyResultsUserQuestionsComparator implements Comparator {

    public int compare(Object obj1, Object obj2) throws ClassCastException {
        if (!(obj1 instanceof SurveyResultsUserQuestionsListitem) || !(obj2 instanceof SurveyResultsUserQuestionsListitem)){
            throw new ClassCastException("A SurveyResultsUserQuestionsListitem object expected.");
        }
        SurveyResultsUserQuestionsListitem sruqli1 = (SurveyResultsUserQuestionsListitem)obj1;
        SurveyResultsUserQuestionsListitem sruqli2 = (SurveyResultsUserQuestionsListitem)obj2;

        int numberofresponses1 = 0;
        if (sruqli1!=null && sruqli1.getQuestion()!=null && sruqli1.getQuestion().getQuestionresponses()!=null){
            numberofresponses1 = sruqli1.getQuestion().getQuestionresponses().size();
        }

        int numberofresponses2 = 0;
        if (sruqli2!=null && sruqli2.getQuestion()!=null && sruqli2.getQuestion().getQuestionresponses()!=null){
            numberofresponses2 = sruqli2.getQuestion().getQuestionresponses().size();
        }

        return numberofresponses2 - numberofresponses1;
    }
}