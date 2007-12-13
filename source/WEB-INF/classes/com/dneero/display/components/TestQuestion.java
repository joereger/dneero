package com.dneero.display.components;

import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.SurveyResponseParser;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class TestQuestion implements Component {

    public static int ID = 7;
    public static String NAME = "Qualifying Question";
    private Question question;
    private Blogger blogger;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public TestQuestion(Question question, Blogger blogger){
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
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        out.append(" ");
        out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        out.append("<br/>");
        out.append("<font class=\"tinyfont\" style=\"color: #ff0000;\">You must answer this question correctly to qualify for this survey.</font>");
        out.append("<br/>");
        out.append("<input type=\"text\" size=\"20\" maxlength=\"255\" name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"\">");

        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
//        StringBuffer out = new StringBuffer();
//        out.append("<p class=\"questiontitle\">");
//        out.append(question.getQuestion());
//        out.append("</p>");
//        out.append("<p class=\"answer\">");
//
//        if (blogger!=null && response!=null){
//            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
//            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
//                Questionresponse questionresponse = iterator.next();
//                out.append(questionresponse.getValue());
//            }
//        } else {
//            out.append("Not answered.");
//        }
//        out.append("</p>");
//        return out.toString();
        return "";
    }

    public void validateAnswer(SurveyResponseParser srp) throws ComponentException {
            String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
            if (requestParams==null || requestParams.length<1){
                throw new ComponentException(question.getQuestion()+" is required.");
            }
            if (requestParams[0]==null || requestParams[0].equals("")){
                throw new ComponentException(question.getQuestion()+" is required.");
            }
            //Validate that they've answered correctly
            String answermustcontain = "";
            for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                Questionconfig questionconfig = iterator.next();
                if (questionconfig.getName().equals("answermustcontain")){
                    answermustcontain = questionconfig.getValue();
                }
            }

            String useranswer = requestParams[0];
            if (useranswer.toLowerCase().trim().indexOf(answermustcontain.toLowerCase().trim())<=-1){
                throw new ComponentException("Sorry, you must answer '"+question.getQuestion()+"' correctly.");
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
                //question.getQuestionresponses().add(questionresponse);
                try{questionresponse.save();}catch(Exception ex){logger.error("", ex);}
                addedAResponse = true;
            }
//            if (addedAResponse){
//                try{
//                    logger.debug("processAnswer() about to save question.getQuestionid()=" + question.getQuestionid());
//                    question.save();
//                    logger.debug("processAnswer() done saving question.getQuestionid()=" + question.getQuestionid());
//                } catch (GeneralException gex){
//                    logger.debug("processAnswer() failed: " + gex.getErrorsAsSingleString());
//                }
//            }
        }
    }

    public String getHtmlForResult(List<Questionresponse> questionresponses){
//        StringBuffer out = new StringBuffer();
//        out.append("<table width=\"100%\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");
//
//        out.append("<tr>");
//        out.append("<td valign=\"top\" bgcolor=\"#ffffff\" colspan=\"2\">");
//        out.append(" ");
//        out.append("</td>");
//        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\" width=\"65\">");
//        out.append("<b class=\"smallfont\">Response Percent</b>");
//        out.append("</td>");
//        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\" width=\"65\">");
//        out.append("<b class=\"smallfont\">Response Total</b>");
//        out.append("</td>");
//        out.append("</tr>");
//
//        //Get answers
//        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
//        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
//            Questionresponse questionresponse = (Questionresponse)it.next();
//            if (questionresponse.getName().equals("response")){
//                if (answers.containsKey(questionresponse.getValue().trim())){
//                    int currcount = (Integer)answers.get(questionresponse.getValue().trim());
//                    answers.put(questionresponse.getValue(), currcount+1);
//                } else {
//                    answers.put(questionresponse.getValue(), 1);
//                }
//            }
//        }
//        //Display
//        int maxtodisplay = 10;
//        if (answers.size()<maxtodisplay){
//            maxtodisplay = answers.size();
//        }
//        Iterator keyValuePairs = answers.entrySet().iterator();
//        for (int i = 0; i < maxtodisplay; i++){
//            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
//            String answer = (String)mapentry.getKey();
//            int count = (Integer)mapentry.getValue();
//
//            double percentage = 0;
//            if (count>0 && questionresponses!=null && questionresponses.size()>0){
//                percentage = (Double.parseDouble(String.valueOf(count))/Double.parseDouble(String.valueOf(questionresponses.size())))*100;
//            }
//            NumberFormat formatter = DecimalFormat.getInstance();
//            formatter.setMaximumFractionDigits(0);
//
//            out.append("<tr>");
//            out.append("<td valign=\"top\" bgcolor=\"#ffffff\" width=\"130\">");
//            out.append(answer);
//            out.append("</td>");
//            out.append("<td valign=\"top\" bgcolor=\"#ffffff\" width=\"300\">");
//            out.append("<img src=\"/images/bar_dkgrey-blend.gif\" width=\""+percentage+"%\" height=\"10\" border=\"0\">");
//            out.append("</td>");
//            out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
//            out.append(String.valueOf(formatter.format(percentage)) + "%");
//            out.append("</td>");
//            out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
//            out.append(count);
//            out.append("</td>");
//            out.append("</tr>");
//
//        }
//
//        out.append("<tr>");
//        out.append("<td valign=\"top\" align=\"right\" bgcolor=\"#ffffff\" colspan=\"3\">");
//        out.append("<b>Total</b>");
//        out.append("</td>");
//        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
//        out.append(questionresponses.size());
//        out.append("</td>");
//        out.append("</tr>");
//
//        out.append("<tr>");
//        out.append("<td valign=\"top\" align=\"right\" bgcolor=\"#ffffff\" colspan=4>");
//        out.append("<a href=\"/results_answers_details.jsp?questionid="+question.getQuestionid()+"\"><b>All Responses</b></a>");
//        out.append("</td>");
//        out.append("</tr>");
//
//        out.append("</table>");
//        return out.toString();
        return "";
    }

    public String getHtmlForResultDetail(List<Questionresponse> questionresponses){
//        StringBuffer out = new StringBuffer();
//        out.append("<table width=\"100%\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");
//
//        out.append("<tr>");
//        out.append("<td valign=\"top\" bgcolor=\"#ffffff\" colspan=\"2\">");
//        out.append(" ");
//        out.append("</td>");
//        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\" width=\"65\">");
//        out.append("<b class=\"smallfont\">Response Percent</b>");
//        out.append("</td>");
//        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\" width=\"65\">");
//        out.append("<b class=\"smallfont\">Response Total</b>");
//        out.append("</td>");
//        out.append("</tr>");
//
//        //Get answers
//        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
//        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
//            Questionresponse questionresponse = (Questionresponse)it.next();
//            if (questionresponse.getName().equals("response")){
//                if (answers.containsKey(questionresponse.getValue())){
//                    int currcount = (Integer)answers.get(questionresponse.getValue());
//                    answers.put(questionresponse.getValue(), currcount+1);
//                } else {
//                    answers.put(questionresponse.getValue(), 1);
//                }
//            }
//        }
//
//
//        //Display
//        Iterator keyValuePairs = answers.entrySet().iterator();
//        for (int i = 0; i < answers.size(); i++){
//            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
//            String answer = (String)mapentry.getKey();
//            int count = (Integer)mapentry.getValue();
//
//            double percentage = (Double.parseDouble(String.valueOf(count))/Double.parseDouble(String.valueOf(questionresponses.size())))*100;
//            NumberFormat formatter = DecimalFormat.getInstance();
//            formatter.setMaximumFractionDigits(0);
//
//            out.append("<tr>");
//            out.append("<td valign=\"top\" bgcolor=\"#ffffff\" width=\"130\">");
//            out.append(answer);
//            out.append("</td>");
//            out.append("<td valign=\"top\" bgcolor=\"#ffffff\" width=\"300\">");
//            out.append("<img src=\"/images/bar_dkgrey-blend.gif\" width=\""+percentage+"%\" height=\"10\" border=\"0\">");
//            out.append("</td>");
//            out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
//            out.append(String.valueOf(formatter.format(percentage)) + "%");
//            out.append("</td>");
//            out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
//            out.append(count);
//            out.append("</td>");
//            out.append("</tr>");
//
//        }
//
//        out.append("<tr>");
//        out.append("<td valign=\"top\" align=\"right\" bgcolor=\"#ffffff\" colspan=\"3\">");
//        out.append("<b>Total</b>");
//        out.append("</td>");
//        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
//        out.append(questionresponses.size());
//        out.append("</td>");
//        out.append("</tr>");
//
//        out.append("</table>");
//        return out.toString();
        return "";
    }

    public int columnsInCsvOutput() {
        return 0;
    }

    public String[] getCsvForResult() {
//        String[] out = new String[0];
//        if (blogger!=null){
//            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"'").list();
//            out = new String[responses.size()];
//            int i = 0;
//            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
//                Questionresponse questionresponse = iterator.next();
//                out[i]=questionresponse.getValue();
//                i=i+1;
//            }
//        }
//        //return out;
//        //For early simplicity I'm just going to concatenate these with a semicolon to take up one cell
//        StringBuffer tmp = new StringBuffer();
//        for (int i = 0; i < out.length; i++) {
//            String s = out[i];
//            tmp.append(s + ";");
//        }
//        String[] tmpOut = new String[1];
//        tmpOut[0]=tmp.toString();
//        return tmpOut;
        return new String[0];
    }

}
