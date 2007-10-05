package com.dneero.formbeans;

import com.dneero.scheduledjobs.SystemStatsFinancial;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.util.Num;
import com.dneero.util.Str;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: Joe Reger Jr
 * Date: Oct 4, 2007
 * Time: 7:21:25 PM
 */
public class SysadminIndex implements Serializable {

    private String financialStatsHtml = "";

    public SysadminIndex(){
        load();
    }

    private void load(){
        StringBuffer fin = new StringBuffer();
        fin.append("Unpaid Responses: "+ SystemStatsFinancial.getUnpaidresponses());
        fin.append("<br/>");
        fin.append("Unpaid Responses Amt: $"+ Str.formatForMoney(SystemStatsFinancial.getUnpaidresponsesamt()));
        fin.append("<br/>");
        HashMap<Integer, Double> hm = SystemStatsFinancial.getAmtpendingbynumberofdayswithimpressions();
        fin.append("<table cellpadding=\"5\" cellspacing=\"2\" border=\"0\">");
        fin.append("<tr>");
        fin.append("<td valign=\"top\">");
        fin.append("Far From Pay");
        fin.append("</td>");

        for (int i = 0; i<=UpdateResponsePoststatus.DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD; i++){
            double amt = 0.0;
            if (hm.containsKey(i)){
                amt = hm.get(i);
            }
            fin.append("<td valign=\"top\" bgcolor=\"#ffffff\">");
            fin.append("$"+ Str.formatForMoney(amt));
            fin.append("<br/>");
            fin.append("<font class=\"tinyfont\">("+i+" dwi)</font>");
            fin.append("</td>");
        }
        fin.append("<td valign=\"top\">");
        fin.append("Close To Pay");
        fin.append("</td>");
        fin.append("</tr>");
        fin.append("</table>");
        fin.append("<br/>");
        fin.append("<font class=\"tinyfont\">(dwi=days with impressions)</font>");
        fin.append("<br/>");
        fin.append("<font class=\"tinyfont\">("+UpdateResponsePoststatus.DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD+" days req'd for pay)</font>");

        financialStatsHtml = fin.toString();
    }


    public String getFinancialStatsHtml() {
        return financialStatsHtml;
    }

    public void setFinancialStatsHtml(String financialStatsHtml) {
        this.financialStatsHtml=financialStatsHtml;
    }
}
