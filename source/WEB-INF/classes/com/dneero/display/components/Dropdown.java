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
        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("options")){
                options = questionconfig.getValue();
            }
        }

        String[] optionsSplit = options.split("\\n");

        StringBuffer out = new StringBuffer();
        out.append("<select name=\"dneero_questionid_"+question.getQuestionid()+"\">");
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];
            out.append("<option value=\""+com.dneero.util.Str.cleanForHtml(s)+"\">" + s + "</option>");
        }
        out.append("</select>");


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
