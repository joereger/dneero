package com.dneero.charity;

import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jan 27, 2008
 * Time: 8:15:14 PM
 */
public class CharityReport {

    private static Calendar lastrefreshed;
    private static String fullreport = "";
    private static String totalsreport = "";
    private static double totaldonations = 0;
    private static TreeMap<String, Double> totalsByCharity = new TreeMap<String, Double>();

    public CharityReport(){

    }

    private static void checkRefresh(){
        //Essentially a 12 hr cache
        Logger logger = Logger.getLogger(CharityReport.class);
        logger.debug("DateDiff.dateDiff(\"minute\", Calendar.getInstance(), lastrefreshed)="+DateDiff.dateDiff("minute", Calendar.getInstance(), lastrefreshed));
        if (lastrefreshed==null || DateDiff.dateDiff("minute", Calendar.getInstance(), lastrefreshed)>720){
            refresh();
        }
    }

    public static void refresh(){
        lastrefreshed = Calendar.getInstance();
        synchronized(lastrefreshed){
            StringBuffer fullreportSb = new StringBuffer();
            StringBuffer totalsreportSb = new StringBuffer();
            ArrayList<String> charitynames = CharityUtil.getUniqueCharities();
            TreeMap<String, Double> donationTotalsByCharity = new TreeMap<String, Double>();
            double totalDonations = 0;
            fullreportSb.append("<table cellpadding='2' cellspacing='0' border='0'>");
            totalsreportSb.append("<table cellpadding='2' cellspacing='0' border='0'>");
            for(int yyyy=2007; yyyy<=2010; yyyy++){
                for(int quarter=1; quarter<=4; quarter++){
                    boolean havebuiltHeaderForQuarter = false;
                    for (Iterator<String> iterator = charitynames.iterator(); iterator.hasNext();) {
                        String charityname = iterator.next();
                        double donations = CharityCalculator.getDonations(charityname, yyyy, quarter);
                        //Add to the overall total
                        totalDonations = totalDonations + donations;
                        //Add to the total for this charity
                        if (donationTotalsByCharity.containsKey(charityname)){
                            donationTotalsByCharity.put(charityname, donationTotalsByCharity.get(charityname)+donations);
                        } else {
                            donationTotalsByCharity.put(charityname,  donations);
                        }
                        //Create the table
                        if (donations>0){
                            //Add the quarter header row if necessary
                            if (!havebuiltHeaderForQuarter){
                                havebuiltHeaderForQuarter = true;
                                fullreportSb.append("<tr>");
                                fullreportSb.append("<td colspan='2'><b>"+yyyy+" Q"+quarter+"</b></td>");
                                fullreportSb.append("</tr>");
                            }
                            fullreportSb.append("<tr>");
                            fullreportSb.append("<td><font class='smallfont'>"+charityname+"</font></td>");
                            fullreportSb.append("<td align='right'><font class='smallfont'>$"+ Str.formatForMoney(donations)+"</font></td>");
                            fullreportSb.append("</tr>");
                        }
                    }
                }
            }
            //Per-charity total
            if (donationTotalsByCharity.size()>0){
                fullreportSb.append("<tr>");
                fullreportSb.append("<td colspan='2'><b>Totals</b></td>");
                fullreportSb.append("</tr>");
//                totalsreportSb.append("<tr>");
//                totalsreportSb.append("<td colspan='2'><b>Totals</b></td>");
//                totalsreportSb.append("</tr>");
                Iterator keyValuePairs = donationTotalsByCharity.entrySet().iterator();
                for (int i = 0; i < donationTotalsByCharity.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    String key = (String)mapentry.getKey();
                    double value = (Double)mapentry.getValue();
                    fullreportSb.append("<tr>");
                    fullreportSb.append("<td><font class='smallfont'>"+key+"</font></td>");
                    fullreportSb.append("<td align='right'><font class='smallfont'>$"+ Str.formatForMoney(value)+"</font></td>");
                    fullreportSb.append("</tr>");
                    totalsreportSb.append("<tr>");
                    totalsreportSb.append("<td><font class='smallfont'>"+key+"</font></td>");
                    totalsreportSb.append("<td align='right'><font class='smallfont'>$"+ Str.formatForMoney(value)+"</font></td>");
                    totalsreportSb.append("</tr>");
                }
            }
            //Overall total
            if (totalDonations>0){
                fullreportSb.append("<tr>");
                fullreportSb.append("<td><b>Total All Charities</b></td>");
                fullreportSb.append("<td align='right'>$"+ Str.formatForMoney(totalDonations)+"</td>");
                fullreportSb.append("</tr>");
                totalsreportSb.append("<tr>");
                totalsreportSb.append("<td><b>Total All Charities</b></td>");
                totalsreportSb.append("<td align='right'>$"+ Str.formatForMoney(totalDonations)+"</td>");
                totalsreportSb.append("</tr>");
            }
            fullreportSb.append("</table>");
            totalsreportSb.append("</table>");

            //Save into the object
            fullreport = fullreportSb.toString();
            totalsreport = totalsreportSb.toString();
            totaldonations  = totalDonations;
            totalsByCharity = donationTotalsByCharity;
            lastrefreshed = Calendar.getInstance();
        }
    }




    public static Calendar getLastrefreshed() {
        checkRefresh();
        return lastrefreshed;
    }


    public static String getFullreport() {
        checkRefresh();
        return fullreport;
    }


    public static String getTotalsreport() {
        checkRefresh();
        return totalsreport;
    }


    public static double getTotaldonations() {
        checkRefresh();
        return totaldonations;
    }


    public static TreeMap<String, Double> getTotalsByCharity() {
        checkRefresh();
        return totalsByCharity;
    }

}
