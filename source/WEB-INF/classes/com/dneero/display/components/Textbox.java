package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.dao.Questionresponse;
import com.dneero.dao.Response;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;

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

    Logger logger = Logger.getLogger(this.getClass().getName());

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
        out.append(question.getQuestion());
        out.append("<br/>");

        out.append("<input type=text size=\"20\" maxlength=\"255\" name=\"dneero_questionid_"+question.getQuestionid()+"\">");

        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
        StringBuffer out = new StringBuffer();
        out.append(question.getQuestion());
        out.append("<br/>");

        if (blogger!=null && response!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                out.append(questionresponse.getValue());
                if (iterator.hasNext()){
                    out.append("<br/>");
                }
            }
        } else {
            out.append("Not answered.");
        }

        return out.toString();
    }

    public void validateAnswer(HttpServletRequest request) throws ComponentException {
        if (question.getIsrequired()){
            String[] requestParams = request.getParameterValues("dneero_questionid_"+question.getQuestionid());
            if (requestParams==null){
                throw new ComponentException(question.getQuestion()+" is required.");
            }
        }
    }

    public void processAnswer(HttpServletRequest request, Response response) throws ComponentException {
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
                questionresponse.setResponseid(response.getResponseid());

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
        out.append("<table width=100% cellpadding=3 cellspacing=1 border=0>");

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

        //Get answers
        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
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

        


        //Display
        int maxtodisplay = 10;
        if (answers.size()<maxtodisplay){
            maxtodisplay = answers.size();
        }
        Iterator keyValuePairs = answers.entrySet().iterator();
        for (int i = 0; i < maxtodisplay; i++){
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
        out.append("<td valign=top align=right bgcolor=#ffffff colspan=3>");
        out.append("<b>Total</b>");
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6>");
        out.append(question.getQuestionresponses().size());
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=top align=right bgcolor=#ffffff colspan=4>");
        out.append("<a href='researchersurveyresults_questiondetail.jsf?questionid="+question.getQuestionid()+"'><b>All Responses</b></a>");
        out.append("</td>");
        out.append("</tr>");

        out.append("</table>");
        return out.toString();
    }

    public String getHtmlForResultDetail(){
        StringBuffer out = new StringBuffer();
        out.append("<table width=100% cellpadding=3 cellspacing=1 border=0>");

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

        //Get answers
        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
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




        //Display
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
        out.append("<td valign=top align=right bgcolor=#ffffff colspan=3>");
        out.append("<b>Total</b>");
        out.append("</td>");
        out.append("<td valign=top bgcolor=#e6e6e6>");
        out.append(question.getQuestionresponses().size());
        out.append("</td>");
        out.append("</tr>");

        out.append("</table>");
        return out.toString();
    }

    public int columnsInCsvOutput() {
        return 1;
    }

    public String[] getCsvForResult() {
        String[] out = new String[0];
        if (blogger!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"'").list();
            out = new String[responses.size()];
            int i = 0;
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                out[i]=questionresponse.getValue();
                i=i+1;
            }
        }
        //return out;
        //For early simplicity I'm just going to concatenate these with a semicolon to take up one cell
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < out.length; i++) {
            String s = out[i];
            tmp.append(s + ";");
        }
        String[] tmpOut = new String[1];
        tmpOut[0]=tmp.toString();
        return tmpOut;
    }

}
