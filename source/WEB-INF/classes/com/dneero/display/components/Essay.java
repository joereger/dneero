package com.dneero.display.components;

import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
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
        out.append(question.getQuestion());
        out.append("<br/>");

        out.append("<textarea cols=\"25\" rows=\"5\" name=\"dneero_questionid_"+question.getQuestionid()+"\"></textarea>");

        return out.toString();
    }

    public String getHtmlForDisplay() {
        StringBuffer out = new StringBuffer();
        out.append(question.getQuestion());
        out.append("<br/>");

        if (blogger!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"'").list();
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
        for (Iterator it = question.getQuestionresponses().iterator(); it.hasNext(); ) {
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


}
