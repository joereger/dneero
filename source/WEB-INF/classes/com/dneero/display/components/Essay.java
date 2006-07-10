package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.dao.Questionconfig;
import com.dneero.dao.Questionresponse;
import com.dneero.dao.hibernate.HibernateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

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
        StringBuffer out = new StringBuffer();
        out.append("<textarea cols=\"25\" rows=\"5\" name=\"dneero_questionid_"+question.getQuestionid()+"\"></textarea>");

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
