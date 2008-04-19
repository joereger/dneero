package com.dneero.htmluibeans;

import com.dneero.dao.Question;
import com.dneero.dao.Rank;
import com.dneero.dao.Rankquestion;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
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
public class ResearcherRankDetail implements Serializable {

    private ArrayList<ResearcherRankDetailListitem> rdlis = new ArrayList<ResearcherRankDetailListitem>();
    private Rank rank;
    private ArrayList<Question> questions = new ArrayList<Question>();

    public ResearcherRankDetail(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Pagez.getRequest().getParameter("rankid")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankid"))){
            rank = Rank.get(Integer.parseInt(Pagez.getRequest().getParameter("rankid")));
        }
        if (rank!=null){
            rdlis = new ArrayList<ResearcherRankDetailListitem>();
            List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                                               .add(Restrictions.eq("rankid", rank.getRankid()))
                                               .addOrder(Order.desc("rankquestionid"))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
                Rankquestion rankquestion = rankquestionIterator.next();
                Question question = Question.get(rankquestion.getQuestionid());
                Survey survey = Survey.get(question.getSurveyid());

                if (!questions.contains(question)){
                    questions.add(question);
                }

                ResearcherRankDetailListitem rdli = new ResearcherRankDetailListitem();
                rdli.getRankquestions().add(rankquestion);
                rdli.setQuestion(question);
                rdli.setSurvey(survey);
                rdlis.add(rdli);
            }

        }

    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public ArrayList<ResearcherRankDetailListitem> getRdlis() {
        return rdlis;
    }

    public void setRdlis(ArrayList<ResearcherRankDetailListitem> rdlis) {
        this.rdlis = rdlis;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}