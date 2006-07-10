package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Checkboxes implements Component {

    public static int ID = 4;
    public static String NAME = "Checkboxes (Choose Multiple)";
    private Question question;
    private Blogger blogger;


    public Checkboxes(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return Checkboxes.NAME;
    }

    public int getID(){
        return Checkboxes.ID;
    }

    public String getHtmlForInput() {
        return "Checkboxes";
    }

    public String getHtmlForDisplay() {
        return "Checkboxes";
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {

    }

}
