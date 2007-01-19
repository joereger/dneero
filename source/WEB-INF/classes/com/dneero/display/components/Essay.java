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
public class Essay implements Component {

    public static int ID = 2;
    public static String NAME = "Essay (Long Text)";
    private Question question;
    private Blogger blogger;

    Logger logger = Logger.getLogger(this.getClass().getName());

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
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        out.append("<br/>");

        out.append("<textarea cols=\"25\" rows=\"5\" name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"\"></textarea>");

        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
        StringBuffer out = new StringBuffer();
        out.append("<p style=\"font-family: Arial Black, Arial Black, Gadget, sans-serif; font-size: 12px; font-weight: bold; margin: 1px;\">");
        out.append(question.getQuestion());
        out.append("</p>");
        out.append("<p style=\"font-family: Arial, Arial, Helvetica, sans-serif; font-size: 12px; margin: 1px;\">");

        if (blogger!=null && response!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                out.append("<p>");
                out.append(questionresponse.getValue());
                out.append("</p>");
            }
        } else {
            out.append("<p>Not answered.</p>");
        }
        out.append("</p>");
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



    public String getHtmlForResult(List<Questionresponse> questionresponses){
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

        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
        answers.put("Less than 100 characters", 0);
        answers.put("Between 100 and 1000 characters", 0);
        answers.put("Between 1000 and 2000 characters", 0);
        answers.put("Between 2000 and 5000 characters", 0);
        answers.put("More than 5000 characters", 0);
        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
            Questionresponse questionresponse = (Questionresponse)it.next();
            if (questionresponse.getName().equals("response")){
                String lengthInChars = "";
                if (questionresponse.getValue().length()<100){
                    lengthInChars = "Less than 100 characters";
                } else if (questionresponse.getValue().length()>=100 && questionresponse.getValue().length()<1000){
                    lengthInChars = "Between 100 and 1000 characters";
                }  else if (questionresponse.getValue().length()>=1000 && questionresponse.getValue().length()<2000){
                    lengthInChars = "Between 1000 and 2000 characters";
                }  else if (questionresponse.getValue().length()>=2000 && questionresponse.getValue().length()<5000){
                    lengthInChars = "Between 2000 and 5000 characters";
                }  else if (questionresponse.getValue().length()>=5000){
                    lengthInChars = "More than 5000 characters";
                }

                if (answers.containsKey(lengthInChars)){
                    int currcount = (Integer)answers.get(lengthInChars);
                    answers.put(lengthInChars, currcount+1);
                } else {
                    answers.put(lengthInChars, 1);
                }
            }
        }

        Iterator keyValuePairs = answers.entrySet().iterator();
        for (int i = 0; i < answers.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String answer = (String)mapentry.getKey();
            int count = (Integer)mapentry.getValue();

            double percentage = (Double.parseDouble(String.valueOf(count))/Double.parseDouble(String.valueOf(questionresponses.size())))*100;
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
        out.append(questionresponses.size());
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=top align=right bgcolor=#ffffff colspan=4>");
        out.append("<a href='results_answers_details.jsf?questionid="+question.getQuestionid()+"'><b>All Essay Responses</b></a>");
        out.append("</td>");
        out.append("</tr>");

        out.append("</table>");
        return out.toString();
    }

    public String getHtmlForResultDetail(List<Questionresponse> questionresponses){
        StringBuffer out = new StringBuffer();
        int i = 0;
        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
            Questionresponse questionresponse = (Questionresponse)it.next();
            if (questionresponse.getName().equals("response")){
                i = i + 1;
                Blogger blogger = Blogger.get(questionresponse.getBloggerid());
                User user = User.get(blogger.getUserid());
                out.append("<b>Response from: <a href='results_respondents_profile.jsf?responseid="+questionresponse.getResponseid()+"'>"+user.getFirstname()+" "+user.getLastname()+"</a></b>");
                out.append("<br/>");
                out.append(questionresponse.getValue());
                out.append("<br/>");
                out.append("<br/>");
            }
        }
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
