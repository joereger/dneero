package com.dneero.htmluibeans;

import com.dneero.htmlui.HtmlUiBean;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 5:28:57 AM
 */
public class Test implements HtmlUiBean {

    private String test;
    private boolean hasbeeninitialized = false;

    public Test(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("initialized");
    }

    public void initBean() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("initBean() called");
        hasbeeninitialized = true;
        test = "booyah";
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test=test;
    }

    public boolean getHasbeeninitialized() {
        return hasbeeninitialized;
    }

    public void setHasbeeninitialized(boolean hasbeeninitialized) {
        this.hasbeeninitialized=hasbeeninitialized;
    }
}
