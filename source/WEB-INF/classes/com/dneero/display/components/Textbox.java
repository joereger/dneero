package com.dneero.display.components;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.rank.RankUnit;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

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

    public Question getQuestion(){
        return question;
    }

    public String getName() {
        return NAME;
    }

    public int getID(){
        return ID;
    }

    public String getHtmlForInput(Response response) {
        StringBuffer out = new StringBuffer();
        String questionStr = question.getQuestion();
        if (question.getIsuserquestion()){
            questionStr = Str.removeLeftBrackets(question.getQuestion());
        }
        out.append("<font class=\"formfieldnamefont\">"+questionStr+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        }
        out.append("<br/>");

        String value = "";
        if (blogger!=null && response!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                value = questionresponse.getValue();
            }
        }

        out.append("<input type=\"text\" size=\"40\" maxlength=\"500\" name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"_\" value=\""+Str.cleanForHtml(value)+"\">");

        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
        StringBuffer out = new StringBuffer();
        out.append("<p class=\"questiontitle\">");
        out.append(question.getQuestion());
        out.append("</p>");
        out.append("<p class=\"answer\">");

        if (blogger!=null && response!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                out.append(Str.removeLeftBrackets(questionresponse.getValue()));
            }
        } else {
            out.append("Not answered.");
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
            if (requestParams[0]==null || requestParams[0].equals("")){
                throw new ComponentException(question.getQuestion()+" is required.");        
            }
        }
    }

    public void processAnswer(SurveyResponseParser srp, Response response) throws ComponentException {
        String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
        //HibernateUtil.getSession().createQuery("delete Questionresponse q where q.questionid='"+question.getQuestionid()+"' and q.bloggerid='"+blogger.getBloggerid()+"' and q.responseid='"+response.getResponseid()+"'").executeUpdate();
        if (requestParams!=null && requestParams.length>0){
            for (int i = 0; i < requestParams.length; i++) {
                String requestParam = requestParams[i];
                Questionresponse questionresponse = new Questionresponse();
                questionresponse.setQuestionid(question.getQuestionid());
                questionresponse.setBloggerid(blogger.getBloggerid());
                questionresponse.setName("response");
                questionresponse.setValue(requestParam);
                questionresponse.setResponseid(response.getResponseid());
                try{questionresponse.save();}catch(Exception ex){logger.error("", ex);}
            }
        }
    }

    public String getHtmlForResult(List<Questionresponse> questionresponses){
        StringBuffer out = new StringBuffer();
        out.append("<table width=\"100%\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");

        out.append("<tr>");
        out.append("<td valign=\"top\" align=\"left\" bgcolor=\"#ffffff\" colspan=\"1\">");
        out.append("<br/><br/>");
        int i = 0;
        int numToDisplay = 15;
        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
            Questionresponse questionresponse = (Questionresponse)it.next();
            if (i<=numToDisplay){
                if (questionresponse.getName().equals("response")){
                    i = i + 1;
                    Blogger blogger = Blogger.get(questionresponse.getBloggerid());
                    User user = User.get(blogger.getUserid());
                    out.append("<font class=\"tinyfont\"><b><a href=\"/results_respondents_profile.jsp?responseid="+questionresponse.getResponseid()+"\">"+ user.getNickname()+"</a></b></font>");
                    out.append("<br/>");
                    out.append(questionresponse.getValue());
                    out.append("<br/>");
                    out.append("<br/>");
                }
            }
        }
        out.append("</td>");
        out.append("<td valign=\"top\" align=\"right\" bgcolor=\"#ffffff\" colspan=4>");
        out.append("</td>");
        out.append("</tr>");


        out.append("<tr>");
        out.append("<td valign=\"top\" align=\"right\" bgcolor=\"#ffffff\" colspan=\"1\">");
        out.append("<b>Total</b>");
        out.append("</td>");
        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
        out.append(questionresponses.size());
        out.append("</td>");
        out.append("</tr>");

        int remainingResponses = questionresponses.size() - i;
        if (remainingResponses>0){
            out.append("<tr>");
            out.append("<td valign=\"top\" align=\"right\" bgcolor=\"#ffffff\" colspan=4>");
            out.append("<a href=\"/results_answers_details.jsp?questionid="+question.getQuestionid()+"\"><b>See All ("+remainingResponses+" More)</b></a>");
            out.append("</td>");
            out.append("</tr>");
        }

        out.append("</table>");
        return out.toString();
    }

//    public String getHtmlForResult(List<Questionresponse> questionresponses){
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
//
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
//            out.append("<img src=\"/images/bar_green-blend.gif\" width=\""+percentage+"%\" height=\"10\" border=\"0\">");
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
//        out.append("<a href=\"/results_answers_details.jsp?questionid="+question.getQuestionid()+"\"><b>See All</b></a>");
//        out.append("</td>");
//        out.append("</tr>");
//
//        out.append("</table>");
//        return out.toString();
//    }

    public String getHtmlForResultDetail(List<Questionresponse> questionresponses){
        StringBuffer out = new StringBuffer();
        out.append("<table width=\"100%\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");

        out.append("<tr>");
        out.append("<td valign=\"top\" bgcolor=\"#ffffff\" colspan=\"2\">");
        out.append(" ");
        out.append("</td>");
        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\" width=\"65\">");
        out.append("<b class=\"smallfont\">Response Percent</b>");
        out.append("</td>");
        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\" width=\"65\">");
        out.append("<b class=\"smallfont\">Response Total</b>");
        out.append("</td>");
        out.append("</tr>");

        //Get answers
        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
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

            double percentage = (Double.parseDouble(String.valueOf(count))/Double.parseDouble(String.valueOf(questionresponses.size())))*100;
            NumberFormat formatter = DecimalFormat.getInstance();
            formatter.setMaximumFractionDigits(0);

            out.append("<tr>");
            out.append("<td valign=\"top\" bgcolor=\"#ffffff\" width=\"130\">");
            out.append(answer);
            out.append("</td>");
            out.append("<td valign=\"top\" bgcolor=\"#ffffff\" width=\"300\">");
            out.append("<img src=\"/images/bar_green-blend.gif\" width=\""+percentage+"%\" height=\"10\" border=\"0\">");
            out.append("</td>");
            out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
            out.append(String.valueOf(formatter.format(percentage)) + "%");
            out.append("</td>");
            out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
            out.append(count);
            out.append("</td>");
            out.append("</tr>");

        }

        out.append("<tr>");
        out.append("<td valign=\"top\" align=\"right\" bgcolor=\"#ffffff\" colspan=\"3\">");
        out.append("<b>Total</b>");
        out.append("</td>");
        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
        out.append(questionresponses.size());
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
            tmp.append(s);
        }
        String[] tmpOut = new String[1];
        tmpOut[0]=tmp.toString();
        return tmpOut;
    }

    public boolean supportsRank(){
        return false;
    }

    public ArrayList<RankUnit> calculateRankPoints(Rank rank, Response response) {
        ArrayList<RankUnit> rankUnits = new ArrayList<RankUnit>();
        return rankUnits;
    }

    public Element getXmlForDisplay(Response response) {
        Element element = new Element("question");
        element.setAttribute("type", "textbox");
        element.setAttribute("questionid", String.valueOf(question.getQuestionid()));
        //Question
        Element quest = new Element("question");
        quest.setContent(new Text(question.getQuestion()));
        element.addContent(quest);
        //Answer
        Element answer = new Element("answer");
        String value = "Not answered.";
        if (blogger!=null && response!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                value = questionresponse.getValue();
            }
        }
        answer.setContent(new Text(value));
        element.addContent(answer);
        //Return
        return element;
    }
}
