package com.dneero.ui;

import org.apache.log4j.Logger;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import com.dneero.util.Util;
import com.dneero.util.Num;
import com.dneero.util.Str;

/**
 * User: Joe Reger Jr
 * Date: Jun 22, 2006
 * Time: 5:11:08 PM
 */
public class PercentCompleteBar extends UIComponentBase {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String getFamily(){
        return "dNeeroUi";
    }

    public void encodeBegin(FacesContext context) throws IOException {
        StringBuffer mb = new StringBuffer();
        ResponseWriter writer = context.getResponseWriter();

        String currentvalue = String.valueOf(getAttributes().get("currentvalue"));
        String maximumvalue = String.valueOf(getAttributes().get("maximumvalue"));
        String mintitle = String.valueOf(getAttributes().get("mintitle"));
        String maxtitle = String.valueOf(getAttributes().get("maxtitle"));
        String widthinpixels = String.valueOf(getAttributes().get("widthinpixels"));

        if (currentvalue==null || currentvalue.equals("null")){
            currentvalue = "0";
        }
        if (maximumvalue==null || maximumvalue.equals("null")){
            maximumvalue = "100";
        }
        if (mintitle==null || mintitle.equals("null")){
            mintitle = "";
        }
        if (maxtitle==null || maxtitle.equals("null")){
            maxtitle = "";
        }
        if (widthinpixels==null || widthinpixels.equals("null")){
            widthinpixels = "300";
        }

        double currentvalueNum = .0000001;
        try{ currentvalueNum = Double.parseDouble(currentvalue);} catch (Exception ex){logger.error("",ex);}

        double maxvalueNum = .00000001;
        try{ maxvalueNum = Double.parseDouble(maximumvalue);} catch (Exception ex){logger.error("",ex);}

        int widthinpixelsNum = 0;
        try{ widthinpixelsNum = Integer.parseInt(widthinpixels);} catch (Exception ex){logger.error("",ex);}

        double percentcomplete = (currentvalueNum/maxvalueNum)*100;
        String percentcompleteStr = Str.formatNoDecimals(percentcomplete);

        double leftwidthinpixels = (currentvalueNum/maxvalueNum)*Double.parseDouble(String.valueOf(widthinpixelsNum));
        double rightwidthinpixels = widthinpixelsNum - leftwidthinpixels;


        mb.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\""+widthinpixels+"\">\n" +
                "<tr>\n" +
                "<td colspan=\"3\" nowrap>\n" +
                "<img src=\"/images/bar_green-blend.gif\" width=\""+leftwidthinpixels+"\" height=\"10\" border=\"0\"><img src=\"/images/bar_ltgrey-blend.gif\" width=\""+rightwidthinpixels+"\" height=\"10\" border=\"0\">\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td valign=\"top\" align=\"left\">\n" +
                "<font class=\"tinyfont\">"+mintitle+"</font>\n" +
                "</td>\n" +
                "<td valign=\"top\" align=\"center\">\n" +
                "<font class=\"tinyfont\">"+currentvalue+" of "+maximumvalue+" ("+percentcompleteStr+"%)</font>\n" +
                "</td>\n" +
                "<td valign=\"top\" align=\"right\">\n" +
                "<font class=\"tinyfont\">"+maxtitle+"</font>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>");

        //Output
        writer.write(mb.toString());

    }





}
