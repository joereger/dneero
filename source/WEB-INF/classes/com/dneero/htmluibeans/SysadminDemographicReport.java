package com.dneero.htmluibeans;

import com.dneero.charity.CharityReport;
import com.dneero.reports.FieldAggregator;
import com.dneero.reports.SimpleTableOutput;
import com.dneero.dao.hibernate.HibernateUtil;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 3:02:56 PM
 */
public class SysadminDemographicReport implements Serializable {

    private String report="";

    public SysadminDemographicReport(){

    }



    public void initBean(){
        List bloggers = HibernateUtil.getSession().createQuery("from Blogger").list();
        FieldAggregator fa = new FieldAggregator((ArrayList)bloggers);
        SimpleTableOutput sto = new SimpleTableOutput(fa);
        report = sto.getHtml();
    }


    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
