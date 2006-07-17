package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.dao.Questionconfig;
import com.dneero.dao.Questionresponse;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;

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

    Logger logger = Logger.getLogger(this.getClass().getName());

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
        out.append(question.getQuestion());
        out.append("<br/>");


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
            out.append("<br>");
            out.append(i);
            out.append("</td>");
        }
        if (!createdExactlyMaxRadio){
            out.append("<td valign=top>");
            out.append("<input type=radio name=\"dneero_questionid_"+question.getQuestionid()+"\" value=\""+max+"\">");
            out.append("<br>");
            out.append(max);
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
        out.append(question.getQuestion());
        out.append("<br/>");

        if (blogger!=null){

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
        } else {
            out.append("Not answered.");
        }

        return out.toString();
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {

    }

    public void processAnswer(HttpServletRequest request) throws ComponentException {
        String[] requestParams = request.getParameterValues("dneero_questionid_"+question.getQuestionid());
        if (requestParams!=null){
            boolean addedAResponse = false;
            for (int i = 0; i < requestParams.length; i++) {
                String requestParam = requestParams[i];
                Questionresponse questionresponse = new Questionresponse();
                questionresponse.setQuestionid(question.getQuestionid());
                questionresponse.setBloggerid(blogger.getBloggerid());
                questionresponse.setName("response");
                questionresponse.setValue(requestParam);

                question.getQuestionresponses().add(questionresponse);
                addedAResponse = true;
            }
            if (addedAResponse){
                try{
                    logger.debug("processAnswer() about to save question.getQuestionid()=" + question.getQuestionid());
                    question.save();
                    logger.debug("processAnswer() done saving question.getQuestionid()=" + question.getQuestionid());
                } catch (GeneralException gex){
                    logger.debug("processAnswer() failed: " + gex.getErrorsAsSingleString());
                }
            }
        }
    }

    public String getHtmlForResult(){
        StringBuffer out = new StringBuffer();
        out.append("<table width=100% cellpadding=3 cellspacing=0 border=0>");

        out.append("<tr>");
        out.append("<td valign=top bgcolor=#ffffff colspan=2>");
        out.append(" ");
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6 width=65>");
        out.append("<b class=smallfont>Response Percent</b>");
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6 width=65>");
        out.append("<b class=smallfont>Response Total</b>");
        out.append("</td>");
        out.append("</tr>");



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

        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
        boolean createdExactlyMaxRadio = false;
        for (double i = min; i<=max; i=i+step) {
            if (i==max){
                createdExactlyMaxRadio = true;
            }
            answers.put(String.valueOf(i), 0);
        }
        if (!createdExactlyMaxRadio){
            answers.put(String.valueOf(max), 0);
        }


        for (Iterator it = question.getQuestionresponses().iterator(); it.hasNext(); ) {
            Questionresponse questionresponse = (Questionresponse)it.next();
            if (questionresponse.getName().equals("response")){
                if (answers.containsKey(questionresponse.getValue())){
                    int currcount = (Integer)answers.get(questionresponse.getValue());
                    answers.put(questionresponse.getValue(), currcount+1);
                } else {
                    answers.put(questionresponse.getValue(), 1);
                }
            }
        }

        out.append("<tr>");
        out.append("<td valign=top bgcolor=#ffffff colspan=2>");
        out.append(mintitle);
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6>");
        out.append(" ");
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6>");
        out.append(" ");
        out.append("</td>");
        out.append("</tr>");

        Iterator keyValuePairs = answers.entrySet().iterator();
        for (int i = 0; i < answers.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String answer = (String)mapentry.getKey();
            int count = (Integer)mapentry.getValue();

            double percentage = (Double.parseDouble(String.valueOf(count))/Double.parseDouble(String.valueOf(question.getQuestionresponses().size())))*100;
            NumberFormat formatter = DecimalFormat.getInstance();
            formatter.setMaximumFractionDigits(0);

            out.append("<tr>");
            out.append("<td valign=top bgcolor=#ffffff width=130>");
            out.append(answer);
            out.append("</td>");
            out.append("<td valign=top bgcolor=#ffffff width=300>");
            out.append("<img src='/images/bar_dkgrey-blend.gif' width='"+percentage+"%' height='10' border=0>");
            out.append("</td>");
            out.append("<td valign=top bgcolor=#e6e6e6>");
            out.append(String.valueOf(formatter.format(percentage)) + "%");
            out.append("</td>");
            out.append("<td valign=top bgcolor=#e6e6e6>");
            out.append(count);
            out.append("</td>");
            out.append("</tr>");

        }

        out.append("<tr>");
        out.append("<td valign=top bgcolor=#ffffff colspan=2>");
        out.append(maxtitle);
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6>");
        out.append(" ");
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6>");
        out.append(" ");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=top align=right bgcolor=#ffffff colspan=3>");
        out.append("<b>Total Respondents</b>");
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6>");
        out.append(question.getQuestionresponses().size());
        out.append("</td>");
        out.append("</tr>");

        out.append("</table>");
        return out.toString();
    }

}
