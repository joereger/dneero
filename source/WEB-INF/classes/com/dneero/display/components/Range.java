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
        StringBuffer out = new StringBuffer();

        String mintitle = "Low";
        double min = 1;
        double step = 1;
        double max = 5;
        String maxtitle = "High";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("mintitle")){
                mintitle = questionconfig.getValue();
            } else if (questionconfig.getName().equals("min")){
                min = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("step")){
                step = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("max")){
                max = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("maxtitle")){
                maxtitle = questionconfig.getValue();
            }
        }

        out.append("<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\">");
        out.append("<tr>");
        out.append("<td valign=top>");
        out.append(mintitle);
        out.append("</td>");
        boolean createdExactlyMaxRadio = false;
        for (double i = min; i<=max; i=i+step) {
            if (i==max){
                createdExactlyMaxRadio = true;
            }
            out.append("<td valign=top>");
            out.append("<input type=radio name=\"dneero_questionid_"+question.getQuestionid()+"\" value=\""+i+"\">");
            out.append("</td>");
        }
        if (!createdExactlyMaxRadio){
            out.append("<td valign=top>");
            out.append("<input type=radio name=\"dneero_questionid_"+question.getQuestionid()+"\" value=\""+max+"\">");
            out.append("</td>");
        }
        out.append("<td valign=top>");
        out.append(maxtitle);
        out.append("</td>");
        out.append("</tr>");
        out.append("</table>");

        return out.toString();
    }

    public String getHtmlForDisplay() {
        StringBuffer out = new StringBuffer();

        double response = 0;
        List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"'").list();
        for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
            Questionresponse questionresponse = iterator.next();
            response = Double.parseDouble(questionresponse.getValue());
        }

        String mintitle = "Low";
        double min = 1;
        double step = 1;
        double max = 5;
        String maxtitle = "High";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("mintitle")){
                mintitle = questionconfig.getValue();
            } else if (questionconfig.getName().equals("min")){
                min = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("step")){
                step = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("max")){
                max = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("maxtitle")){
                maxtitle = questionconfig.getValue();
            }
        }

        out.append("<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\">");
        out.append("<tr>");
        out.append("<td valign=top>");
        out.append(mintitle);
        out.append("</td>");
        boolean createdExactlyMaxRadio = false;
        for (double i = min; i<=max; i=i+step) {
            if (i==max){
                createdExactlyMaxRadio = true;
            }
            out.append("<td valign=top>");
            if (response==i){
                out.append("<b>"+i+"</b>");
            } else {
                out.append(i);
            }
            out.append("</td>");
        }
        if (!createdExactlyMaxRadio){
            out.append("<td valign=top>");
            if (response==max){
                out.append("<b>"+max+"</b>");
            } else {
                out.append(max);
            }
            out.append("</td>");
        }
        out.append("<td valign=top>");
        out.append(maxtitle);
        out.append("</td>");
        out.append("</tr>");
        out.append("</table>");

        return out.toString();
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {

    }

}
