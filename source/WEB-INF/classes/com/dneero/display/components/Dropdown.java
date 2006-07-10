package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Dropdown implements Component {

    public static int ID = 3;
    public static String NAME = "Dropdown (Choose One)";
    private Question question;
    private Blogger blogger;


    public Dropdown(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return Dropdown.NAME;
    }

    public int getID(){
        return Dropdown.ID;
    }

    public String getHtmlForInput() {
        return "Dropdown";
    }

    public String getHtmlForDisplay() {
        return "Dropdown";
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {

    }

}
