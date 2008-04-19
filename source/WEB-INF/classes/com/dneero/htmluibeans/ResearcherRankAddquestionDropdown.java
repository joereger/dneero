package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherRankAddquestionDropdown implements Serializable {

    private Rank rank;
    private Survey survey;
    private Question question;
    private ArrayList<ResearcherRankAddquestionDropdownListitem> possibleanswers;

    public ResearcherRankAddquestionDropdown(){

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
            //Get possible answers for the dropdown
            possibleanswers = new ArrayList<ResearcherRankAddquestionDropdownListitem>();
            String options = "";
            for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                Questionconfig questionconfig = iterator.next();
                if (questionconfig.getName().equals("options")){
                    options = questionconfig.getValue();
                }
            }
            String[] optionsSplit = options.split("\\n");
            int id = 0;
            for (int i = 0; i < optionsSplit.length; i++) {
                String possans = optionsSplit[i];
                //Is there a rankquestion for this answer?
                Rankquestion rankquestionTmp = null;
                List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                                                   .add(Restrictions.eq("rankid", rank.getRankid()))
                                                   .add(Restrictions.eq("questionid", question.getQuestionid()))
                                                   .add(Restrictions.eq("answer", possans))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
                    Rankquestion rankquestion = rankquestionIterator.next();
                    rankquestionTmp = rankquestion;
                }
                //Build the list item
                ResearcherRankAddquestionDropdownListitem rraqdl = new ResearcherRankAddquestionDropdownListitem();
                rraqdl.setPossibleanswer(possans);
                rraqdl.setRankquestion(rankquestionTmp);
                id = id + 1;
                rraqdl.setId(id);
                possibleanswers.add(rraqdl);
            }
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

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<ResearcherRankAddquestionDropdownListitem> getPossibleanswers() {
        return possibleanswers;
    }

    public void setPossibleanswers(ArrayList<ResearcherRankAddquestionDropdownListitem> possibleanswers) {
        this.possibleanswers = possibleanswers;
    }
}