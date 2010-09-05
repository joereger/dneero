package com.dneero.display.components;

import com.dneero.dao.*;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.rank.RankUnit;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Infotext implements Component {

    public static int ID = 8;
    public static String NAME = "Info Text (Not a Question)";
    private Question question;
    private Blogger blogger;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Infotext(Question question, Blogger blogger){
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
        out.append("<br/>");

        String text = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("infotext")){
                text = questionconfig.getValue();
            }
        }
        out.append("<font class=\"normalfont\">"+text+"</font>");
        out.append("<br/>");

        return out.toString();
    }

    public String getHtmlForDisplay(Response response) {
        StringBuffer out = new StringBuffer();
        out.append("<p class=\"questiontitle\">");
        out.append(question.getQuestion());
        out.append("</p>");


        String text = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("infotext")){
                text = questionconfig.getValue();
            }
        }
        out.append("<p class=\"answer\">");
        out.append(Str.removeLeftBrackets(text));
        out.append("</p>");


        return out.toString();
    }

    public void validateAnswer(SurveyResponseParser srp) throws ComponentException {

    }

    public void processAnswer(SurveyResponseParser srp, Response response) throws ComponentException {

    }


    public String getHtmlForResult(List<Questionresponse> questionresponses){
        StringBuffer out = new StringBuffer();

        return out.toString();
    }

    public String getHtmlForJson(List<Questionresponse> questionresponses){
        return getHtmlForResult(questionresponses);
    }

    public String getHtmlForResultDetail(List<Questionresponse> questionresponses){
        return getHtmlForResult(questionresponses);
    }

    public int columnsInCsvOutput() {
        return 1;
    }


    public String[] getCsvForResult() {
        String[] out = new String[0];
        String[] tmpOut = new String[1];
        tmpOut[0]="";
        return tmpOut;
    }

    public String[] getCsvForResult(List<Questionresponse> allQuestionresponsesAllUsers) {
        return getCsvForResult();
    }

    public boolean supportsRank(){
        return false;
    }

    public ArrayList<RankUnit> calculateRankPoints(Rank rank, Response response) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<RankUnit> rankUnits = new ArrayList<RankUnit>();
        return rankUnits;
    }

    public Element getXmlForDisplay(Response response) {
        Element element = new Element("question");
        element.setAttribute("type", "infotext");
        element.setAttribute("questionid", String.valueOf(question.getQuestionid()));
        //Question
        Element quest = new Element("question");
        quest.setContent(new Text(question.getQuestion()));
        element.addContent(quest);

        String text = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("infotext")){
                text = questionconfig.getValue();
            }
        }
        Element txtEl = new Element("infotext");
        txtEl.setContent(new Text(text));
        element.addContent(txtEl);

        //Return
        return element;
    }
}