package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.io.Serializable;

import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.dao.*;
import com.dneero.helpers.UserInputSafe;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherRankAddquestion implements Serializable {

    private Rank rank;
    private Survey survey;
    private Question question;

    public ResearcherRankAddquestion(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Pagez.getRequest().getParameter("rankid")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankid"))){
            rank = Rank.get(Integer.parseInt(Pagez.getRequest().getParameter("rankid")));
        }
        if (Pagez.getRequest().getParameter("surveyid")!=null && Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            survey = Survey.get(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
        }
        if (Pagez.getRequest().getParameter("questionid")!=null && Num.isinteger(Pagez.getRequest().getParameter("questionid"))){
            question = Question.get(Integer.parseInt(Pagez.getRequest().getParameter("questionid")));
        }
        if (rank!=null && survey!=null && question!=null){
            //@todo redirect to next page based on questiontype   
        }

    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
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