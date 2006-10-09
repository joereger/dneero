package com.dneero.ui;

import org.apache.log4j.Logger;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import com.dneero.util.Str;

/**
 * User: Joe Reger Jr
 * Date: Jun 22, 2006
 * Time: 5:11:08 PM
 */
public class GreenRoundedButton extends UIComponentBase {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String getFamily(){
        return "dNeeroUi";
    }

    public void encodeBegin(FacesContext context) throws IOException {
        StringBuffer mb = new StringBuffer();
        ResponseWriter writer = context.getResponseWriter();

        String pathtoapproot = (String)getAttributes().get("pathtoapproot");

        if (pathtoapproot==null){
            pathtoapproot = "";
        }



        mb.append("<table cellpadding=0 cellspacing=0 width=100% border=0>");
        mb.append("<tr>");
        mb.append("<td valign=\"center\" background='"+pathtoapproot+"/images/accordion/greenbar-leftcap.gif' align=left width=13>");
        mb.append("<img src='"+pathtoapproot+"images/clear.gif' height=41 width=1 border=0>");
        mb.append("</td>");
        mb.append("<td valign=\"center\" background='"+pathtoapproot+"/images/accordion/greenbar-center.gif' align=center>");
        mb.append("<div style=\"line-height:41px;\">");

        //Output
        writer.write(mb.toString());

    }


    public void encodeEnd(FacesContext context) throws IOException {
        StringBuffer mb = new StringBuffer();
        ResponseWriter writer = context.getResponseWriter();

        String pathtoapproot = (String)getAttributes().get("pathtoapproot");

        if (pathtoapproot==null){
            pathtoapproot = "";
        }




        mb.append("</div>");
        mb.append("</td>");
        mb.append("<td valign=center background='"+pathtoapproot+"/images/accordion/greenbar-rightcap.gif' align=right width=13>");
        mb.append("<img src='"+pathtoapproot+"/images/clear.gif' height=1 width=1 border=0>");
        mb.append("</td>");
        mb.append("</tr>");
        mb.append("</table>");

        //Output
        writer.write(mb.toString());

    }





}
