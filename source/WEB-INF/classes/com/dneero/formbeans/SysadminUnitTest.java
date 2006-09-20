package com.dneero.formbeans;

import com.dneero.dao.Supportissuecomm;
import com.dneero.dao.Supportissue;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.test.TestRunner;
import com.dneero.session.Authorization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminUnitTest {

    private String results;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public SysadminUnitTest(){
        logger.debug("SysadminUnitTest instanciated.");

    }

    public String runTests(){
        if(Jsf.getUserSession().getUser()!=null){
            TestRunner testRunner = new TestRunner();
            results = testRunner.runTests();
        }
        return "sysadminunittest";    
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }


}
