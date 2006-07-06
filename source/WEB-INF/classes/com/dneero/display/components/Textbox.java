package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Textbox implements Component {

    private Question question;
    private Blogger blogger;

    public Textbox(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return "Textbox";
    }

    public String getHtmlForConfig() {
        return "Textbox Config Here";
    }

    public String getHtmlForInput() {
        return "Textbox";
    }

    public String getHtmlForDisplay() {
        return "Textbox";
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processConfig(HttpServletRequest request) throws ComponentException {

    }

}
