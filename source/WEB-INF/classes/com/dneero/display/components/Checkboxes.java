package com.dneero.display.components;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.SurveyResponseParser;
import com.dneero.rank.RankUnit;
import com.dneero.rank.NormalizedpointsUtil;

import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Checkboxes implements Component {

    public static int ID = 4;
    public static String NAME = "Checkboxes (Choose Multiple)";
    private Question question;
    private Blogger blogger;

    Logger logger = Logger.getLogger(this.getClass().getName());


    public Checkboxes(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return Checkboxes.NAME;
    }

    public int getID(){
        return Checkboxes.ID;
    }



    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        }
        out.append("<br/>");

        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("options")){
                options = questionconfig.getValue();
            }
        }
        String[] optionsSplit = options.split("\\n");
        //@todo test checkbox because i don't think that the hashmap holding the values properly handles multiple values for the same name
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];
            out.append("<input type=\"checkbox\" name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"\" value=\""+com.dneero.util.Str.cleanForHtml(s)+"\">" + s);
            if (optionsSplit.length>i+1){
                out.append("<br/>");
            }
        }


        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
        StringBuffer out = new StringBuffer();
        out.append("<p class=\"questiontitle\">");
        out.append(question.getQuestion());
        out.append("</p>");

        List<Questionresponse> responses = new ArrayList<Questionresponse>();
        if (blogger!=null && response!=null){
            responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
        }

        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().trim().equals("options")){
                options = questionconfig.getValue();
            }
        }
        String[] optionsSplit = options.split("\\n");
        //@todo test checkbox because i don't think that the hashmap holding the values properly handles multiple values for the same name
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];
            boolean isSelected = false;
            if (responses!=null && responses.size()>0){
                for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                    Questionresponse questionresponse = iterator.next();
                    if (questionresponse.getValue().trim().equals(s.trim())){
                        isSelected = true;
                    }
                }
            }
            if (isSelected){
                out.append("<p class=\"answer_highlight\">");
                out.append("&nbsp;&nbsp;&gt;");
                out.append(s);
                out.append("</p>");
            } else {
                out.append("<p class=\"answer\">");
                out.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                out.append(s);
                out.append("</p>");
            }
        }
        //out.append("</p>");
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
                try{questionresponse.save();}catch(Exception ex){logger.error("", ex);}
                //question.getQuestionresponses().add(questionresponse);
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

        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
            Questionresponse questionresponse = (Questionresponse)it.next();
            if (questionresponse.getName().equals("response")){
                if (answers.containsKey(questionresponse.getValue().trim())){
                    int currcount = (Integer)answers.get(questionresponse.getValue().trim());
                    answers.put(questionresponse.getValue(), currcount+1);
                } else {
                    answers.put(questionresponse.getValue(), 1);
                }
            }
        }

        int counttotal = 0;
        Iterator keyValuePairs = answers.entrySet().iterator();
        for (int i = 0; i < answers.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String answer = (String)mapentry.getKey();
            int count = (Integer)mapentry.getValue();
            counttotal = counttotal + count;

            double percentage = 0;
            if (count>0 && questionresponses!=null && questionresponses.size()>0){
                percentage = (Double.parseDouble(String.valueOf(count))/Double.parseDouble(String.valueOf(questionresponses.size())))*100;
            }
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
        out.append("<b>Total Selections</b>");
        out.append("</td>");
        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
        out.append(counttotal);
        out.append("</td>");
        out.append("</tr>");

        out.append("</table>");
        return out.toString();
    }

    public String getHtmlForResultDetail(List<Questionresponse> questionresponses){
        return getHtmlForResult(questionresponses);
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

    public boolean supportsRank(){
        return false;
    }

    public ArrayList<RankUnit> calculateRankPoints(Rank rank, Response response) {
        ArrayList<RankUnit> rankUnits = new ArrayList<RankUnit>();
        return rankUnits;
    }

}
