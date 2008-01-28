package com.dneero.htmluibeans;

import com.dneero.eula.EulaHelper;
import com.dneero.util.Time;
import com.dneero.util.GeneralException;
import com.dneero.util.Str;
import com.dneero.htmlui.ValidationException;
import com.dneero.htmlui.Pagez;
import com.dneero.dao.Eula;
import com.dneero.charity.CharityUtil;
import com.dneero.charity.CharityCalculator;
import com.dneero.charity.CharityReport;

import java.io.Serializable;
import java.util.Date;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 3:02:56 PM
 */
public class SysadminCharityReport implements Serializable {

    private String report="";

    public SysadminCharityReport(){
        //initBean();
    }



    public void initBean(){
        report = CharityReport.getFullreport();
    }


    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
