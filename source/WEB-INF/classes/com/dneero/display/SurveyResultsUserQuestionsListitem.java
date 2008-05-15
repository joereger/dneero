package com.dneero.display;

import com.dneero.dao.Question;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: May 15, 2008
 * Time: 9:56:10 AM
 */
public class SurveyResultsUserQuestionsListitem {

    private User user;
    private Question question;
    private String htmlForResult;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question=question;
    }

    public String getHtmlForResult() {
        return htmlForResult;
    }

    public void setHtmlForResult(String htmlForResult) {
        this.htmlForResult=htmlForResult;
    }
}
