package com.dneero.display.components;

import com.dneero.dao.Blogger;
import com.dneero.dao.Question;

import java.util.LinkedHashMap;

/**
 * User: Joe Reger Jr
 * Date: Jul 10, 2006
 * Time: 12:26:37 PM
 */
public class ComponentTypes {

    private LinkedHashMap typesaslinkedhashmap;

    public static Component getComponentByID(int ID, Question question, Blogger blogger){
        if (ID==Textbox.ID){
            return new Textbox(question, blogger);
        }
        if (ID==Essay.ID){
            return new Essay(question, blogger);
        }
        if (ID==Dropdown.ID){
            return new Dropdown(question, blogger);
        }
        if (ID==Checkboxes.ID){
            return new Checkboxes(question, blogger);
        }
        if (ID==Range.ID){
            return new Range(question, blogger);
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
        }
        return typesaslinkedhashmap;
    }

    public void setTypesaslinkedhashmap(LinkedHashMap typesaslinkedhashmap){
        this.typesaslinkedhashmap = typesaslinkedhashmap;
    }


}
