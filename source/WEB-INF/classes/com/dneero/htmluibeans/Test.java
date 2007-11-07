package com.dneero.htmluibeans;

import com.dneero.htmlui.HtmlUiBean;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 5:28:57 AM
 */
public class Test implements HtmlUiBean {

    private String textbox;
    private String textarea;
    private String dropdown;
    private ArrayList<String> checkboxesvalues;
    private boolean hasbeeninitialized = false;

    public Test(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("initialized");
    }

    public void initBean() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("initBean() called");
        hasbeeninitialized = true;
        textbox = "booyah";
        dropdown = "b";
        checkboxesvalues = new ArrayList<String>();
        checkboxesvalues.add("bradley");
        checkboxesvalues.add("charcoal");
        textarea="megawoowoo";
    }

    public void save(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Saving...");
        logger.debug("textbox="+textbox);
        logger.debug("textarea="+textarea);
        logger.debug("dropdown="+dropdown);
        for (Iterator<String> iterator = checkboxesvalues.iterator(); iterator.hasNext();) {
            String s = iterator.next();
            logger.debug("checkboxesvalues: "+s);
        }
        logger.debug("Done saving.");
    }


    public String getTextbox() {
        return textbox;
    }

    public void setTextbox(String textbox) {
        this.textbox = textbox;
    }

    public String getDropdown() {
        return dropdown;
    }

    public void setDropdown(String dropdown) {
        this.dropdown = dropdown;
    }

    public boolean getHasbeeninitialized() {
        return hasbeeninitialized;
    }

    public void setHasbeeninitialized(boolean hasbeeninitialized) {
        this.hasbeeninitialized=hasbeeninitialized;
    }

    public ArrayList<String> getCheckboxesvalues() {
        return checkboxesvalues;
    }

    public void setCheckboxesvalues(ArrayList<String> checkboxesvalues) {
        this.checkboxesvalues = checkboxesvalues;
    }

    public String getTextarea() {
        return textarea;
    }

    public void setTextarea(String textarea) {
        this.textarea = textarea;
    }
}
