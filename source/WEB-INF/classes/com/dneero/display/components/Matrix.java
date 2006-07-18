package com.dneero.display.components;

import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.dao.Questionresponse;
import com.dneero.dao.Questionconfig;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Matrix implements Component {

    public static int ID = 6;
    public static String NAME = "Matrix";
    private Question question;
    private Blogger blogger;

    private String DELIM = "_X_X_----_X_X_";

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Matrix(Question question, Blogger blogger){
        this.question = question;
        this.blogger = blogger;
    }

    public String getName() {
        return Matrix.NAME;
    }

    public int getID(){
        return Matrix.ID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append(question.getQuestion());
        out.append("<br/>");

        //Get config params
        String rowsStr = "";
        String colsStr = "";
        String respondentcanselectmanyStr = "0";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("rows")){
                rowsStr = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("cols")){
                colsStr = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("respondentcanselectmany")){
                respondentcanselectmanyStr = questionconfig.getValue();
            }
        }
        String[] rows = rowsStr.split("\\n");
        String[] cols = colsStr.split("\\n");
        boolean respondentcanselectmany = false;
        if (respondentcanselectmanyStr.equals("1")){
            respondentcanselectmany = true;
        }

        //Display
        out.append("<table cellpadding=3 cellspacing=1 border=0>");

        //Header
        out.append("<tr>");
        out.append("<td valign=top>");
        out.append("");
        out.append("</td>");
        for (int j = 0; j < cols.length; j++) {
            String col = cols[j].trim();
            out.append("<td valign=top>");
            out.append(col);
            out.append("</td>");
        }
        out.append("</tr>");

        //Input boxes
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i].trim();
            out.append("<tr>");
            out.append("<td valign=top>");
            out.append(row);
            out.append("</td>");
            for (int j = 0; j < cols.length; j++) {
                String col = cols[j].trim();
                out.append("<td valign=top>");
                if (respondentcanselectmany){
                    out.append("<input type=checkbox name=\"dneero_questionid_"+question.getQuestionid()+"\" value=\""+com.dneero.util.Str.cleanForHtml(row+DELIM+col)+"\">");
                } else {
                    out.append("<input type=radio name=\"dneero_questionid_"+question.getQuestionid()+"\" value=\""+com.dneero.util.Str.cleanForHtml(row+DELIM+col)+"\">");
                }
                out.append("</td>");
            }
            out.append("</tr>");
        }

        out.append("</table>");


        return out.toString();
    }

    public String getHtmlForDisplay() {
        StringBuffer out = new StringBuffer();
        out.append(question.getQuestion());
        out.append("<br/>");

        if (blogger!=null){
            ArrayList<String> checkedboxes = new ArrayList<String>();
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"'").list();
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                checkedboxes.add(questionresponse.getValue());
            }


            //Get config params
            String rowsStr = "";
            String colsStr = "";
            String respondentcanselectmanyStr = "0";
            for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                Questionconfig questionconfig = iterator.next();
                if (questionconfig.getName().equals("rows")){
                    rowsStr = questionconfig.getValue();
                }
                if (questionconfig.getName().equals("cols")){
                    colsStr = questionconfig.getValue();
                }
                if (questionconfig.getName().equals("respondentcanselectmany")){
                    respondentcanselectmanyStr = questionconfig.getValue();
                }
            }
            String[] rows = rowsStr.split("\\n");
            String[] cols = colsStr.split("\\n");
            boolean respondentcanselectmany = false;
            if (respondentcanselectmanyStr.equals("1")){
                respondentcanselectmany = true;
            }

            //Display
            out.append("<table cellpadding=3 cellspacing=1 border=0>");

            //Header
            out.append("<tr>");
            out.append("<td valign=top>");
            out.append("");
            out.append("</td>");
            for (int j = 0; j < cols.length; j++) {
                String col = cols[j].trim();
                out.append("<td valign=top>");
                out.append(col);
                out.append("</td>");
            }
            out.append("</tr>");

            //Input boxes
            for (int i = 0; i < rows.length; i++) {
                String row = rows[i].trim();
                out.append("<tr>");
                out.append("<td valign=top>");
                out.append(row);
                out.append("</td>");
                for (int j = 0; j < cols.length; j++) {
                    String col = cols[j].trim();
                    boolean thiswaschecked = false;
                    if (checkedboxes.contains(row+DELIM+col)){
                        thiswaschecked=true;
                    }
                    out.append("<td valign=top>");
                    if (thiswaschecked){
                        out.append("X");
                    }
                    out.append("</td>");
                }
                out.append("</tr>");
            }

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
                questionresponse.setValue(requestParam.trim());

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

        //Get config params
        String rowsStr = "";
        String colsStr = "";
        String respondentcanselectmanyStr = "0";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("rows")){
                rowsStr = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("cols")){
                colsStr = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("respondentcanselectmany")){
                respondentcanselectmanyStr = questionconfig.getValue();
            }
        }
        String[] rows = rowsStr.split("\\n");
        String[] cols = colsStr.split("\\n");
        boolean respondentcanselectmany = false;
        if (respondentcanselectmanyStr.equals("1")){
            respondentcanselectmany = true;
        }

        //Get answers
        LinkedHashMap<String, Integer> answers = new LinkedHashMap();
        LinkedHashMap<String, Integer> answerCountByRow = new LinkedHashMap();
        LinkedHashMap<String, Integer> answerCountByCol = new LinkedHashMap();
        for (Iterator it = question.getQuestionresponses().iterator(); it.hasNext(); ) {
            Questionresponse questionresponse = (Questionresponse)it.next();
            if (questionresponse.getName().equals("response")){
                //Answer
                if (answers.containsKey(questionresponse.getValue())){
                    int currcount = (Integer)answers.get(questionresponse.getValue());
                    answers.put(questionresponse.getValue(), currcount+1);
                } else {
                    answers.put(questionresponse.getValue(), 1);
                }
                //Answer count by row / col
                String[] split = questionresponse.getValue().split(DELIM);
                if (split.length==2){
                    String row = split[0].trim();
                    String col = split[1].trim();
                    if (answerCountByRow.containsKey(row)){
                        int currcount = (Integer)answerCountByRow.get(row);
                        answerCountByRow.put(row, currcount+1);
                    } else {
                        answerCountByRow.put(row, 1);
                    }
                    if (answerCountByCol.containsKey(col)){
                        int currcount = (Integer)answerCountByCol.get(col);
                        answerCountByCol.put(col, currcount+1);
                    } else {
                        answerCountByCol.put(col, 1);
                    }
                } else if (split.length>2){
                    logger.error("Amazingly, found an answer where DELIM is used: questionresponse.getQuestionresponseid()="+questionresponse.getQuestionresponseid());
                }
            }
        }



        //Display
        out.append("<table cellpadding=3 cellspacing=1 border=0>");

        //Header
        out.append("<tr>");
        out.append("<td valign=top>");
        out.append("");
        out.append("</td>");
        for (int j = 0; j < cols.length; j++) {
            String col = cols[j].trim();
            out.append("<td valign=top bgcolor=#e6e6e6>");
            out.append(col);
            out.append("</td>");
        }
        out.append("<td valign=top>");
        out.append("");
        out.append("</td>");
        out.append("</tr>");

        //Input boxes
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i].trim();
            out.append("<tr>");
            out.append("<td valign=top bgcolor=#e6e6e6>");
            out.append(row);
            out.append("</td>");
            for (int j = 0; j < cols.length; j++) {
                String col = cols[j].trim();
                double percentage = 0;
                NumberFormat formatter = DecimalFormat.getInstance();
                formatter.setMaximumFractionDigits(0);
                if (answers.get(row+DELIM+col)!=null){
                    percentage = (Double.parseDouble(String.valueOf(answers.get(row+DELIM+col)))/Double.parseDouble(String.valueOf(answerCountByRow.get(row))))*100;
                }
                int count = 0;
                if (answers.get(row+DELIM+col)!=null){
                    count = answers.get(row+DELIM+col);
                }
                String bgcolor="#ffffff";
                if (count>0){
                    bgcolor="#e6e6e6";
                }
                out.append("<td valign=\"top\" bgcolor=\""+bgcolor+"\">");
                out.append("<font class=\"smallfont\">");
                out.append(""+String.valueOf(count)+"("+String.valueOf(formatter.format(percentage)) + "%)");
                out.append("</font>");
                out.append("</td>");
            }
            out.append("<td valign=\"top\">");
            out.append("<font class=\"smallfont\">");
            int count = 0;
            if (answerCountByRow.get(row)!=null){
                count = answerCountByRow.get(row);
            }
            out.append(""+String.valueOf(count)+"");
            out.append("</font>");
            out.append("</td>");
            out.append("</tr>");
        }

        //Col totals
        out.append("<tr>");
        out.append("<td valign=top>");
        out.append("");
        out.append("</td>");
        for (int j = 0; j < cols.length; j++) {
            String col = cols[j].trim();
            int count = 0;
            if (answerCountByCol.get(col)!=null){
                count = answerCountByCol.get(col);
            }
            out.append("<td valign=top>");
            out.append("<font class=\"smallfont\">");
            out.append(String.valueOf(count));
            out.append("</font>");
            out.append("</td>");
        }
        out.append("<td valign=top>");
        out.append("");
        out.append("</td>");
        out.append("</tr>");

        out.append("</table>");



        out.append("</td>");
        out.append("</tr>");


        out.append("</table>");
        return out.toString();
    }

    public String getHtmlForResultDetail(){
        return getHtmlForResult();
    }

}
