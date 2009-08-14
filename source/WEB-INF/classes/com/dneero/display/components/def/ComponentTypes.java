package com.dneero.display.components.def;

import com.dneero.dao.Blogger;
import com.dneero.dao.Question;
import com.dneero.dao.Survey;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.*;

import java.util.*;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * User: Joe Reger Jr
 * Date: Jul 10, 2006
 * Time: 12:26:37 PM
 */
public class ComponentTypes {

    private LinkedHashMap typesaslinkedhashmap;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public static Component getComponentByType(int typeID, Question question, Blogger blogger){
        if (blogger==null){
            blogger = new Blogger();
        }
        if (typeID== Textbox.ID){
            return new Textbox(question, blogger);
        }
        if (typeID== Essay.ID){
            return new Essay(question, blogger);
        }
        if (typeID== Dropdown.ID){
            return new Dropdown(question, blogger);
        }
        if (typeID== Checkboxes.ID){
            return new Checkboxes(question, blogger);
        }
        if (typeID== Range.ID){
            return new Range(question, blogger);
        }
        if (typeID== Matrix.ID){
            return new Matrix(question, blogger);
        }
        if (typeID== TestQuestion.ID){
            return new TestQuestion(question, blogger);
        }
        if (typeID== Infotext.ID){
            return new Infotext(question, blogger);
        }
        return null;
    }

    public LinkedHashMap getTypesaslinkedhashmap(){
        if (typesaslinkedhashmap==null){
            typesaslinkedhashmap = new LinkedHashMap();
            typesaslinkedhashmap.put(Textbox.NAME, Textbox.ID);
            typesaslinkedhashmap.put(Essay.NAME, Essay.ID);
            typesaslinkedhashmap.put(Dropdown.NAME, Dropdown.ID);
            typesaslinkedhashmap.put(Checkboxes.NAME, Checkboxes.ID);
            typesaslinkedhashmap.put(Range.NAME, Range.ID);
            typesaslinkedhashmap.put(Matrix.NAME, Matrix.ID);
            typesaslinkedhashmap.put(TestQuestion.NAME, TestQuestion.ID);
            typesaslinkedhashmap.put(Infotext.NAME, Infotext.ID);
        }
        return typesaslinkedhashmap;
    }

    public TreeMap<String, String> getTypes(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        LinkedHashMap lhm = getTypesaslinkedhashmap();
        Iterator keyValuePairs = lhm.entrySet().iterator();
        for (int i = 0; i < lhm.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String value = String.valueOf(mapentry.getValue());
            out.put(value, key);
        }
        return out;
    }

    public void setTypesaslinkedhashmap(LinkedHashMap typesaslinkedhashmap){
        this.typesaslinkedhashmap = typesaslinkedhashmap;
    }

    public Component getByTagSyntax(String tag, Blogger blogger, Survey survey){
        //Get questionid from <$question_22334$>
        String qidStr = tag.substring(11, tag.length()-2);
        logger.debug("tag="+tag+" qidStr="+qidStr);
        if (com.dneero.util.Num.isinteger(qidStr)){
            Question question = Question.get(Integer.parseInt(qidStr));
            if (question.getSurveyid()==survey.getSurveyid()){
                return getComponentByType(question.getComponenttype(), question, blogger);
            }
        }
        return null;
    }

    public Component getByElement(Element element, Blogger blogger, Survey survey){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (element==null){
            return null;
        }
        //Handle questions
        if (element.getName().equals("question")){
            logger.debug("element.getName().equals(\"question\")");
            logger.debug("element.getAttribute(\"questionid\").getValue()="+element.getAttribute("questionid").getValue());
            if (com.dneero.util.Num.isinteger(element.getAttribute("questionid").getValue())){
                Question question = Question.get(Integer.parseInt(element.getAttribute("questionid").getValue()));
                if (question.getSurveyid()==survey.getSurveyid()){
                    logger.debug("returning getComponentByType question.getComponenttype()="+question.getComponenttype()+" question.getQuestionid()="+question.getQuestionid());
                    return getComponentByType(question.getComponenttype(), question, blogger);
                }
            }
        }
        //Handle other types of components
        if (element.getName().equals("video")){
            logger.debug("element.getName().equals(\"video\")");
        }
        if (element.getName().equals("text")){
            logger.debug("element.getName().equals(\"text\")");
        }
        logger.debug("returning null component");
        return null;
    }


}
