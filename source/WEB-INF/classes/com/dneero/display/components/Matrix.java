package com.dneero.display.components;

import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.SurveyResponseParser;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Str;
import com.dneero.util.Util;
import com.dneero.rank.RankUnit;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Text;

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

    public Question getQuestion(){
        return question;
    }

    public String getName() {
        return Matrix.NAME;
    }

    public int getID(){
        return Matrix.ID;
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

        List<Questionresponse> responses = new ArrayList<Questionresponse>();
        ArrayList<String> checkedboxes = new ArrayList<String>();
        if (blogger!=null && response!=null){
            responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            checkedboxes = new ArrayList<String>();
            for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                Questionresponse questionresponse = iterator.next();
                checkedboxes.add(questionresponse.getValue());
            }
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
        out.append("<table cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");

        //Header
        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("");
        out.append("</td>");
        for (int j = 0; j < cols.length; j++) {
            String col = cols[j].trim();
            out.append("<td align=\"center\" valign=\"top\">");
            out.append(col);
            out.append("</td>");
        }
        out.append("</tr>");

        //Input boxes
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i].trim();
            out.append("<tr>");
            out.append("<td align=\"left\" valign=\"top\">");
            out.append(row);
            out.append("</td>");
            for (int j = 0; j < cols.length; j++) {
                String col = cols[j].trim();

                boolean thiswaschecked = false;
                if (checkedboxes.contains(row+DELIM+col)){
                    thiswaschecked=true;
                }

                out.append("<td align=\"center\" valign=\"top\">");
                if (respondentcanselectmany){
                    String checked = "";
                    if (thiswaschecked){
                        checked = " checked ";
                    }
                    out.append("<input type=\"checkbox\" "+checked+" name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"_row_"+Str.cleanForHtml(row)+"\" value=\""+com.dneero.util.Str.cleanForHtml(row+DELIM+col)+"\">");
                } else {
                    String checked = "";
                    if (thiswaschecked){
                        checked = " checked ";
                    }
                    out.append("<input type=\"radio\" "+checked+" name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"_row_"+Str.cleanForHtml(row)+"\" value=\""+com.dneero.util.Str.cleanForHtml(row+DELIM+col)+"\">");
                }
                out.append("</td>");
            }
            out.append("</tr>");
        }

        out.append("</table>");


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
            ArrayList<String> checkedboxes = new ArrayList<String>();
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
            out.append("<table cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");

            //Header
            out.append("<tr>");
            out.append("<td valign=\"top\">");
            out.append("");
            out.append("</td>");
            for (int j = 0; j < cols.length; j++) {
                String col = cols[j].trim();
                out.append("<td align=\"center\" valign=\"top\">");
                out.append("<center>"+col+"</center>");
                out.append("</td>");
            }
            out.append("</tr>");

            //Input boxes
            for (int i = 0; i < rows.length; i++) {
                String row = rows[i].trim();
                out.append("<tr>");
                out.append("<td align=\"left\" valign=\"top\">");
                out.append(row);
                out.append("</td>");
                for (int j = 0; j < cols.length; j++) {
                    String col = cols[j].trim();
                    boolean thiswaschecked = false;
                    if (checkedboxes.contains(row+DELIM+col)){
                        thiswaschecked=true;
                    }
                    out.append("<td align=\"center\" valign=\"top\">");
                    if (thiswaschecked){
                        out.append("<center>X</center>");
                    }
                    out.append("</td>");
                }
                out.append("</tr>");
            }

            out.append("</table>");


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
        logger.debug("made it to processAnswer");
        String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
        //HibernateUtil.getSession().createQuery("delete Questionresponse q where q.questionid='"+question.getQuestionid()+"' and q.bloggerid='"+blogger.getBloggerid()+"' and q.responseid='"+response.getResponseid()+"'").executeUpdate();
        if (requestParams!=null && requestParams.length>0){
            for (int i = 0; i < requestParams.length; i++) {
                String requestParam = requestParams[i];
                Questionresponse questionresponse = new Questionresponse();
                questionresponse.setQuestionid(question.getQuestionid());
                questionresponse.setBloggerid(blogger.getBloggerid());
                questionresponse.setName("response");
                questionresponse.setValue(requestParam.trim());
                questionresponse.setResponseid(response.getResponseid());
                try{questionresponse.save();}catch(Exception ex){logger.error("", ex);}
            }
        }
    }

    public String getHtmlForResult(List<Questionresponse> questionresponses){
        StringBuffer out = new StringBuffer();
        out.append("<table width=\"100%\" cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");


        out.append("<tr>");
        out.append("<td valign=\"top\" bgcolor=\"#ffffff\" colspan=\"2\">");

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
        for (Iterator it = questionresponses.iterator(); it.hasNext(); ) {
            Questionresponse questionresponse = (Questionresponse)it.next();
            if (questionresponse.getName().equals("response")){
                //Answer
               if (answers.containsKey(questionresponse.getValue().trim())){
                    int currcount = (Integer)answers.get(questionresponse.getValue().trim());
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
        out.append("<table cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");

        //Header
        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("");
        out.append("</td>");
        for (int j = 0; j < cols.length; j++) {
            String col = cols[j].trim();
            out.append("<td align=\"center\" valign=\"top\" bgcolor=\"#e6e6e6\">");
            out.append(col);
            out.append("</td>");
        }
        out.append("<td valign=\"top\">");
        out.append("");
        out.append("</td>");
        out.append("</tr>");

        //Input boxes
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i].trim();
            out.append("<tr>");
            out.append("<td align=\"center\" valign=\"top\" bgcolor=\"#e6e6e6\">");
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
                out.append("<td align=\"center\" valign=\"top\" bgcolor=\""+bgcolor+"\">");
                out.append("<font class=\"smallfont\">");
                out.append(""+String.valueOf(count)+"("+String.valueOf(formatter.format(percentage)) + "%)");
                out.append("</font>");
                out.append("</td>");
            }
            out.append("<td align=\"center\" valign=\"top\">");
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
        out.append("<td valign=\"top\">");
        out.append("");
        out.append("</td>");
        for (int j = 0; j < cols.length; j++) {
            String col = cols[j].trim();
            int count = 0;
            if (answerCountByCol.get(col)!=null){
                count = answerCountByCol.get(col);
            }
            out.append("<td align=\"center\" valign=\"top\">");
            out.append("<font class=\"smallfont\">");
            out.append(String.valueOf(count));
            out.append("</font>");
            out.append("</td>");
        }
        out.append("<td valign=\"top\">");
        out.append("");
        out.append("</td>");
        out.append("</tr>");

        out.append("</table>");



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

    public Element getXmlForDisplay(Response response) {
        Element element = new Element("question");
        element.setAttribute("type", "matrix");
        element.setAttribute("questionid", String.valueOf(question.getQuestionid()));
        //Question
        Element quest = new Element("question");
        quest.setContent(new Text(question.getQuestion()));
        element.addContent(quest);
        //Get responses
        if (blogger!=null && response!=null){
            List<Questionresponse> responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            ArrayList<String> checkedboxes = new ArrayList<String>();
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
                element.setAttribute("respondentcanselectmany", "true");
            } else {
                element.setAttribute("respondentcanselectmany", "false");
            }
            //Rows
            Element rowsEl = new Element("rows");
            for (int i = 0; i < rows.length; i++) {
                String row = rows[i].trim();
                Element rowEl = new Element("row");
                rowEl.setContent(new Text(row));
                rowsEl.addContent(rowEl);
            }
            element.addContent(rowsEl);
            //Cols
            Element colsEl = new Element("cols");
            for (int i = 0; i < cols.length; i++) {
                String col = cols[i].trim();
                Element colEl = new Element("col");
                colEl.setContent(new Text(col));
                colsEl.addContent(colEl);
            }
            element.addContent(colsEl);
            //Checked
            Element checkedEl = new Element("checked");
            for (Iterator it = checkedboxes.iterator(); it.hasNext(); ) {
                String checked = (String)it.next();
                String[] split = checked.split(DELIM);
                if (split.length>=2){
                    Element rowcolpair = new Element("rowcolpair");
                    rowcolpair.setAttribute("row", split[0]);
                    rowcolpair.setAttribute("col", split[1]);
                    checkedEl.addContent(rowcolpair);
                }
            }
            element.addContent(checkedEl);
        }
        //Return
        return element;

    }

}
