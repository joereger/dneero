package com.dneero.display.components.def;

import com.dneero.dao.Blogger;
import com.dneero.dao.Question;
import com.dneero.dao.Response;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.*;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 10, 2006
 * Time: 12:26:37 PM
 */
public class ComponentTypes {

    private LinkedHashMap typesaslinkedhashmap;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public static Component getComponentByID(int ID, Question question, Blogger blogger){
        if (ID== Textbox.ID){
            return new Textbox(question, blogger);
        }
        if (ID== Essay.ID){
            return new Essay(question, blogger);
        }
        if (ID== Dropdown.ID){
            return new Dropdown(question, blogger);
        }
        if (ID== Checkboxes.ID){
            return new Checkboxes(question, blogger);
        }
        if (ID== Range.ID){
            return new Range(question, blogger);
        }
        if (ID== Matrix.ID){
            return new Matrix(question, blogger);
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
        }
        return typesaslinkedhashmap;
    }

    public void setTypesaslinkedhashmap(LinkedHashMap typesaslinkedhashmap){
        this.typesaslinkedhashmap = typesaslinkedhashmap;
    }

    public Component getByTagSyntax(String tag, Blogger blogger){
        //Get questionid from <$question_22334$>
        String qidStr = tag.substring(11, tag.length()-2);
        logger.debug("tag="+tag+" qidStr="+qidStr);
        if (com.dneero.util.Num.isinteger(qidStr)){
            Question question = Question.get(Integer.parseInt(qidStr));
            return getComponentByID(question.getComponenttype(), question, blogger);
        }
        return null;
    }


}
