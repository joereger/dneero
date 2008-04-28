package com.dneero.htmluibeans;

import com.dneero.dao.Question;
import com.dneero.dao.User;
import com.dneero.display.components.def.Component;

/**
 * User: Joe Reger Jr
 * Date: Apr 27, 2008
 * Time: 8:22:51 PM
 */
public class PublicSurveyUserquestionListitem {

    private Question question;
    private User user;
    private Component component;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question=question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component=component;
    }
}
