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
        StringBuffer out = new StringBuffer();
        ArrayList<String> charitynames = CharityUtil.getUniqueCharities();
        out.append("<table cellpadding='5' cellspacing='0' border='0'>");
        for(int yyyy=2007; yyyy<=2010; yyyy++){
            for(int quarter=1; quarter<=4; quarter++){
                out.append("<tr>");
                out.append("<td colspan='2'><b>"+yyyy+" Q"+quarter+"</b></td>");
                out.append("</tr>");
                for (Iterator<String> iterator = charitynames.iterator(); iterator.hasNext();) {
                    String charityname = iterator.next();
                    double donations = CharityCalculator.getDonations(charityname, yyyy, quarter);
                    if (donations>0){
                        out.append("<tr>");
                        out.append("<td>"+charityname+"</td>");
                        out.append("<td>$"+Str.formatForMoney(donations)+"</td>");
                        out.append("</tr>");
                    }
                }
            }
        }
        out.append("</table>");
        report = out.toString();
    }


    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
