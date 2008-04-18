package com.dneero.htmluibeans;

import com.dneero.dao.Question;
import com.dneero.dao.Rankquestion;
import com.dneero.dao.Survey;

import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Apr 18, 2008
 * Time: 4:41:21 PM
 */
public class ResearcherRankDetailListitem {

    private ArrayList<Rankquestion> rankquestions = new ArrayList<Rankquestion>();
    private Question question;
    private Survey survey;

    public ArrayList<Rankquestion> getRankquestions() {
        return rankquestions;
    }

    public void setRankquestions(ArrayList<Rankquestion> rankquestions) {
        this.rankquestions = rankquestions;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
