package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class ResearcherSurveyQuestionList extends SortableList implements Serializable {

    private ArrayList questions = new ArrayList();

    public ResearcherSurveyQuestionList() {
        //Default sort column
        super("questionid");
        load();
    }

    public void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        questions = new ArrayList();
        Survey survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
        }
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            questions.add(question);
        }
    }

    public List getQuestions() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getQuestions");
        sort(getSort(), isAscending());
        return questions;
    }

    public void setQuestions(List questions) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setQuestions");
        this.questions = (ArrayList)questions;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Question question1 = (Question)o1;
                Question question2 = (Question)o2;
                if (column == null) {
                    return 0;
                }
                if (question1!=null && question2!=null && column.equals("questionid")) {
                    return ascending ? question1.getQuestionid()-question2.getQuestionid() : question2.getQuestionid()-question1.getQuestionid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (questions != null && !questions.isEmpty()) {
            logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(questions, comparator);
        }
    }




}
