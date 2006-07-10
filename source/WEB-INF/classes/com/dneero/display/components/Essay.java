package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Essay implements Component {

    public static int ID = 2;
    public static String NAME = "Essay (Long Text)";
    private Question question;
    private Blogger blogger;


    public Essay(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return Essay.NAME;
    }

    public int getID(){
        return Essay.ID;
    }

    public String getHtmlForInput() {
        return "Essay";
    }

    public String getHtmlForDisplay() {
        return "Essay";
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {

    }


}
