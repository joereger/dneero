package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.dao.Questionresponse;
import com.dneero.dao.hibernate.HibernateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Textbox implements Component {

    public static int ID = 1;
    public static String NAME = "Textbox (Short Text)";
    private Question question;
    private Blogger blogger;


    public Textbox(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return NAME;
    }

    public int getID(){
        return ID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<input type=text size=\"20\" maxlength=\"255\" name=\"dneero_questionid_"+question.getQuestionid()+"\">");

        return out.toString();
    }

    public String getHtmlForDisplay() {
        StringBuffer out = new StringBuffer();

        List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"'").list();
        for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
            Questionresponse questionresponse = iterator.next();
            out.append(questionresponse.getValue());
            if (iterator.hasNext()){
                out.append("<br/>");
            }
        }

        return out.toString();
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {

    }

}
