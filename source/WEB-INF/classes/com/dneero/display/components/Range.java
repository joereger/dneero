package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Range implements Component {

    public static int ID = 5;
    public static String NAME = "Range (i.e. 1-10)";
    private Question question;
    private Blogger blogger;


    public Range(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return Range.NAME;
    }

    public int getID(){
        return Range.ID;
    }

    public String getHtmlForInput() {
        return "Range";
    }

    public String getHtmlForDisplay() {
        return "Range";
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {

    }

}
