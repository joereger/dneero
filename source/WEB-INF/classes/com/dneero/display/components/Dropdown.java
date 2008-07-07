package com.dneero.display.components;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.rank.NormalizedpointsUtil;
import com.dneero.rank.RankUnit;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

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

    public String getHtmlForInput(Response response) {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"formfieldnamefont\">"+Str.removeLeftBrackets(question.getQuestion())+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        }
        out.append("<br/>");

        List<Questionresponse> responses = new ArrayList<Questionresponse>();
        if (blogger!=null && response!=null){
            responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
        }

        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("options")){
                options = questionconfig.getValue();
            }
        }
        String[] optionsSplit = options.split("\\n");

        out.append("<select name=\""+ SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"questionid_"+question.getQuestionid()+"\">");
        out.append("<option value=\"\"></option>");
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];

            boolean isSelected = false;
            if (responses!=null && responses.size()>0){
                for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                    Questionresponse questionresponse = iterator.next();
                    logger.debug("questionresponse.getValue()="+questionresponse.getValue());
                    logger.debug("s="+s);
                    if (questionresponse.getValue().trim().equals(s.trim())){
                        isSelected = true;
                    }
                    logger.debug("isSelected="+isSelected);
                }
            }

            String selected = "";
            if (isSelected){
                selected = "selected";    
            }

            out.append("<option "+selected+" value=\""+ Str.cleanForHtml(s.trim())+"\">" + s.trim() + "</option>");
        }
        out.append("</select>");


        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
        StringBuffer out = new StringBuffer();
        out.append("<p class=\"questiontitle\">");
        out.append(question.getQuestion());
        out.append("</p>");

        if (blogger==null){
            logger.debug("blogger == null");
        } else {
            logger.debug("blogger.getBloggerid()="+blogger.getBloggerid());
        }
        if (response==null){
            logger.debug("response == null");
        } else {
            logger.debug("response.getResponseid()="+response.getResponseid());
        }
        if (question==null){
            logger.debug("question == null");
        } else {
            logger.debug("question.getQuestionid()="+question.getQuestionid());
        }

        List<Questionresponse> responses = new ArrayList<Questionresponse>();
        if (blogger!=null && response!=null){
            responses = HibernateUtil.getSession().createQuery("from Questionresponse where questionid='"+question.getQuestionid()+"' and bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
        }
        if (responses!=null){
            logger.debug("responses.size()="+responses.size());    
        } else {
            logger.debug("responses == null");
        }



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
            boolean isSelected = false;
            if (responses!=null && responses.size()>0){
                for (Iterator<Questionresponse> iterator = responses.iterator(); iterator.hasNext();) {
                    Questionresponse questionresponse = iterator.next();
                    logger.debug("questionresponse.getValue()="+questionresponse.getValue());
                    logger.debug("s="+s);
                    if (questionresponse.getValue().trim().equals(s.trim())){
                        isSelected = true;
                    }
                    logger.debug("isSelected="+isSelected);
                }
            }

            logger.debug("isSelected="+isSelected);
            if (isSelected){
                out.append("<p class=\"answer_highlight\">");
                out.append("&nbsp;&nbsp;&gt;");
                out.append(Str.removeLeftBrackets(s));
                out.append("</p>");
            } else {
                out.append("<p class=\"answer\">");
                out.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                out.append(Str.removeLeftBrackets(s));
                out.append("</p>");
            }
        }
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

        Iterator keyValuePairs = answers.entrySet().iterator();
        for (int i = 0; i < answers.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String answer = (String)mapentry.getKey();
            int count = (Integer)mapentry.getValue();

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
        out.append("<b>Total Respondents</b>");
        out.append("</td>");
        out.append("<td valign=\"top\" bgcolor=\"#e6e6e6\">");
        out.append(questionresponses.size());
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
        return true;
    }

    public ArrayList<RankUnit> calculateRankPoints(Rank rank, Response response) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("start processing responseid="+response.getResponseid());
        ArrayList<RankUnit> rankUnits = new ArrayList<RankUnit>();
        int maxpoints = NormalizedpointsUtil.determineMaxPoints(question.getQuestionid(), rank.getRankid());
        //Find rankings for this question
        List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                               .add(Restrictions.eq("questionid", question.getQuestionid()))
                               .add(Restrictions.eq("rankid", rank.getRankid()))
                               .setCacheable(true)
                               .list();
        for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
            Rankquestion rankquestion = rankquestionIterator.next();
            //This may create performance issues
            List<Questionresponse> questionresponses = HibernateUtil.getSession().createCriteria(Questionresponse.class)
               .add(Restrictions.eq("questionid", question.getQuestionid()))
               .add(Restrictions.eq("responseid", response.getResponseid()))
               .setCacheable(true)
               .list();
            for (Iterator<Questionresponse> questionresponseIterator = questionresponses.iterator(); questionresponseIterator.hasNext();){
                Questionresponse questionresponse = questionresponseIterator.next();
                //If the user answered in a way that gets points
                if (rankquestion.getAnswer().trim().equalsIgnoreCase(questionresponse.getValue().trim())){
                    //Hold ranking in a RankUnit object
                    RankUnit rankUnit = new RankUnit();
                    rankUnit.setPoints(rankquestion.getPoints());
                    rankUnit.setRankquestionid(rankquestion.getRankquestionid());
                    rankUnit.setResponseid(response.getResponseid());
                    rankUnit.setNormalizedpoints(NormalizedpointsUtil.calculateNormalizedPoints(rankquestion.getPoints(), maxpoints));
                    //Add to the array
                    rankUnits.add(rankUnit);
                }
            }
        }
        logger.debug("end processing responseid="+response.getResponseid());
        return rankUnits;
    }


}
