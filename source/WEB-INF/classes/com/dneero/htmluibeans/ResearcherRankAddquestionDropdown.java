package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.io.Serializable;

import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import com.dneero.util.GeneralException;
import com.dneero.dao.*;
import com.dneero.helpers.UserInputSafe;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherRankAddquestionDropdown implements Serializable {




    public ResearcherRankAddquestionDropdown(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());



    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

    }


}