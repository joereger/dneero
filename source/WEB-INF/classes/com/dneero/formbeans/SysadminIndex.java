package com.dneero.formbeans;

import com.dneero.scheduledjobs.SystemStatsFinancial;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.util.Num;
import com.dneero.util.Str;
import com.dneero.util.Jsf;
import com.dneero.util.Io;

import java.io.Serializable;
import java.io.File;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Oct 4, 2007
 * Time: 7:21:25 PM
 */
public class SysadminIndex implements Serializable {

    private String financialStatsHtml = "";
    private String servermemory = "";

    public SysadminIndex(){
        load();
    }

    private void load(){
        StringBuffer fin = new StringBuffer();

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
        fin.append("Unpaid Responses Amt: $"+ Str.formatForMoney(SystemStatsFinancial.getUnpaidresponsesamt()));
        fin.append("<br/>");
        fin.append("Unpaid Responses: "+ SystemStatsFinancial.getUnpaidresponses());
        fin.append("<br/>");
        fin.append("<font class=\"tinyfont\">(dwi=days with impressions)</font>");
        fin.append("<br/>");
        fin.append("<font class=\"tinyfont\">("+UpdateResponsePoststatus.DAYSWITHIMPRESSIONREQUIREDINSIDEPOSTINGPERIOD+" days req'd for pay)</font>");

        financialStatsHtml = fin.toString();



        //Memory
        StringBuffer mb = new StringBuffer();
        Runtime rt = Runtime.getRuntime();


        double used = 0;
        double free = 0;
        double available = 0;

        used = rt.totalMemory()-rt.freeMemory();
        free = rt.freeMemory();
        available =  rt.maxMemory()-rt.totalMemory();

        double usedpercent = (used/rt.maxMemory()) * 100;
        double freepercent = (free/rt.maxMemory()) * 100;
        double availablepercent = (available/rt.maxMemory()) * 100;

        mb.append("<table cellpadding=0 cellspacing=1 border=4 width=100% >");
        mb.append("<tr>");
        mb.append("<td bgcolor=#ff0000 width="+(int)usedpercent+"% align=center>");
        mb.append("<font face=arial size=-2 color=#ffffff>");
        mb.append("U");
        mb.append("</font>");
        mb.append("</td>");
        mb.append("<td bgcolor=#00ff00 width="+(int)freepercent+"% align=center>");
        mb.append("<font face=arial size=-2 color=#ffffff>");
        mb.append("F");
        mb.append("</font>");
        mb.append("</td>");
        mb.append("<td bgcolor=#cccccc width="+(int)availablepercent+"% align=center>");
        mb.append("<font face=arial size=-2 color=#ffffff>");
        mb.append("A");
        mb.append("</font>");
        mb.append("</td>");
        mb.append("</tr>");
        mb.append("</table>");

        mb.append("<br>");

        mb.append((int)used + " <b>U</b>sed - "+(int)usedpercent+"%<br>");
        mb.append((int)free + " <b>F</b>ree - "+(int)freepercent+"%<br>");
        mb.append((int)available + " <b>A</b>vailable - "+(int)availablepercent+"%<br>");
        mb.append("<font class=\"smallfont\">");
        mb.append("Maximum memory available: " + rt.maxMemory() + "<br>");
        mb.append("Total memory allocated: " + rt.totalMemory() + "<br>");
        mb.append("Free memory unused: " + rt.freeMemory() + "<br>");
        mb.append("</font>");
        mb.append("<br>");
        
        servermemory = mb.toString();

        


    }

  




    public String getFinancialStatsHtml() {
        return financialStatsHtml;
    }

    public void setFinancialStatsHtml(String financialStatsHtml) {
        this.financialStatsHtml=financialStatsHtml;
    }

    public String getServermemory() {
        return servermemory;
    }

    public void setServermemory(String servermemory) {
        this.servermemory=servermemory;
    }
}
