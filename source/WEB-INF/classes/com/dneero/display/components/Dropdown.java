package com.dneero.display.components;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.SurveyResponseParser;

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
public class Dropdown implements Component {

    public static int ID = 3;
    public static String NAME = "Dropdown (Choose One)";
    private Question question;
    private Blogger blogger;

    Logger logger = Logger.getLogger(this.getClass().getName());

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
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        out.append("<br/>");

        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("options")){
                options = questionconfig.getValue();
            }
        }
        String[] optionsSplit = options.split("\\n");

        out.append("<select name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"\">");
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];
            out.append("<option value=\""+com.dneero.util.Str.cleanForHtml(s)+"\">" + s + "</option>");
        }
        out.append("</select>");


        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
        StringBuffer out = new StringBuffer();
        out.append("<font style=\"font-family: Arial Black, Arial Black, Gadget, sans-serif; font-size: 12px;\">");
        out.append("<b>");
        out.append(question.getQuestion());
        out.append("</b>");
        out.append("</font>");
        out.append("<br/>");
        out.append("<font style=\"font-family: Arial, Arial, Helvetica, sans-serif; font-size: 12px;\">");

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
        out.append("</font>");
        return out.toString();
    }

    public void validateAnswer(SurveyResponseParser srp) throws ComponentException {
        if (question.getIsrequired()){
            String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
            if (requestParams==null || requestParams.length<1){
                throw new ComponentException(question.getQuestion()+" is required.");
            }
        }
    }

    public void processAnswer(SurveyResponseParser srp, Response response) throws ComponentException {
        String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
        if (requestParams!=null && requestParams.length>0){
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

        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("options")){
                options = questionconfig.getValue();
            }
        }
        String[] optionsSplit = options.split("\\n");
        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];
            answers.put(s.trim(), 0);
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

    public String getHtmlForResultDetail(){
        return getHtmlForResult();
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
