package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;

/**
 * Blogger generated by hbm2java
 */

public class Question extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
     private int questionid;
     private int surveyid;
     private String question;
     private int componenttype;
     private boolean isrequired;
     private Set<Questionconfig> questionconfigs = new HashSet<Questionconfig>();
     private Set<Questionresponse> questionresponses = new HashSet<Questionresponse>();



    public static Question get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Question");
        try {
            logger.debug("Question.get(" + id + ") called.");
            Question obj = (Question) HibernateUtil.getSession().get(Question.class, id);
            if (obj == null) {
                logger.debug("Question.get(" + id + ") returning new instance because hibernate returned null.");
                return new Question();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Question", ex);
            return new Question();
        }
    }

    // Constructors

    /** default constructor */
    public Question() {
    }


    public boolean canRead(User user){
        Survey survey = Survey.get(surveyid);
        if (user.getUserid()==Researcher.get(survey.getResearcherid()).getUserid()){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }



    // Property accessors

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getComponenttype() {
        return componenttype;
    }

    public void setComponenttype(int componenttype) {
        this.componenttype = componenttype;
    }

    public boolean getIsrequired() {
        return isrequired;
    }

    public void setIsrequired(boolean isrequired) {
        this.isrequired = isrequired;
    }

    public Set<Questionconfig> getQuestionconfigs() {
        return questionconfigs;
    }

    public void setQuestionconfigs(Set<Questionconfig> questionconfigs) {
        this.questionconfigs = questionconfigs;
    }

    public Set<Questionresponse> getQuestionresponses() {
        return questionresponses;
    }

    public void setQuestionresponses(Set<Questionresponse> questionresponses) {
        this.questionresponses = questionresponses;
    }
}
