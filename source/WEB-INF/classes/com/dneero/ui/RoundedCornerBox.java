package com.dneero.ui;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * User: Joe Reger Jr
 * Date: Jun 22, 2006
 * Time: 5:11:08 PM
 */
public class RoundedCornerBox extends UIComponentBase {

    public String getFamily(){
        return "dNeeroUi";
    }

    public void encodeBegin(FacesContext context) throws IOException {
        StringBuffer mb = new StringBuffer();
        ResponseWriter writer = context.getResponseWriter();

        String uniqueboxname = (String)getAttributes().get("uniqueboxname");
        String widthinpercentString = (String)getAttributes().get("widthinpercent");
        String titlecolor = (String)getAttributes().get("titlecolor");
        String subtitlecolor = (String)getAttributes().get("subtitlecolor");
        String bodycolor = (String)getAttributes().get("bodycolor");
        String bordercolor = (String)getAttributes().get("bordercolor");
        String title = (String)getAttributes().get("title");
        String subtitle = (String)getAttributes().get("subtitle");


        if (uniqueboxname==null){
            uniqueboxname = "thunder";
        }
        if (widthinpercentString==null){
            widthinpercentString = "100";
        }
        if (titlecolor==null){
            titlecolor = "000000";
        }
        if (subtitlecolor==null){
            subtitlecolor = "000000";
        }
        if (bodycolor==null){
            bodycolor = "cccccc";
        }
        if (bordercolor==null){
            bordercolor = "999999";
        }
        if (title==null){
            title = "";
        }
        if (subtitle==null){
            subtitle = "";
        }


        String x = uniqueboxname;
        int widthinpercent = 0;
        String width = "width=100%";
        if (com.dneero.util.Num.isinteger(widthinpercentString) && Integer.parseInt(widthinpercentString)>0){
            width = "width="+widthinpercent+"%";
        }

        mb.append("<!-- Start Rounded Corner Box -->\n" +
                "<style type=\"text/css\">\n" +
                "#xsnazzy"+x+" h1, #xsnazzy"+x+" h2, #xsnazzy"+x+" p {margin:0 10px; letter-spacing:1px;}\n" +
                "#xsnazzy"+x+" h1 {font-size:2.5em; color:#"+titlecolor+";}\n" +
                "#xsnazzy"+x+" h2 {font-size:2em;color:#"+subtitlecolor+"; border:0;}\n" +
                "#xsnazzy"+x+" p {padding-bottom:0.5em;}\n" +
                "#xsnazzy"+x+" h2 {padding-top:0.5em;}\n" +
                "#xsnazzy"+x+" {background: transparent; margin:1em;}\n" +
                ".xtop"+x+", .xbottom"+x+" {display:block; background:transparent; font-size:1px;}\n" +
                ".xb1"+x+", .xb2"+x+", .xb3"+x+", .xb4"+x+" {display:block; overflow:hidden;}\n" +
                ".xb1"+x+", .xb2"+x+", .xb3"+x+" {height:1px;}\n" +
                ".xb2"+x+", .xb3"+x+", .xb4"+x+" {background:#"+bodycolor+"; border-left:1px solid #"+bordercolor+"; border-right:1px solid #"+bordercolor+";}\n" +
                ".xb1"+x+" {margin:0 5px; background:#"+bordercolor+";}\n" +
                ".xb2"+x+" {margin:0 3px; border-width:0 2px;}\n" +
                ".xb3"+x+" {margin:0 2px;}\n" +
                ".xb4"+x+" {height:2px; margin:0 1px;}\n" +
                ".xboxcontent"+x+" {display:block; background:#"+bodycolor+"; border:0 solid #"+bordercolor+"; border-width:0 1px;}\n" +
                "</style>\n" +
                "<div id=\"xsnazzy"+x+"\" "+width+" >\n" +
                "<b class=\"xtop"+x+"\"><b class=\"xb1"+x+"\"></b><b class=\"xb2"+x+"\"></b><b class=\"xb3"+x+"\"></b><b class=\"xb4"+x+"\"></b></b>\n" +
                "<div class=\"xboxcontent"+x+"\">\n");
         if(title!=null && !title.equals("")){
            mb.append("<h1>"+title+"</h1>\n");
         }
         if(subtitle!=null && !subtitle.equals("")){
            mb.append("<h2>"+subtitle+"</h2>\n");
         }
         mb.append("<p>");


        //Output
        writer.write(mb.toString());

    }



    public void encodeEnd(FacesContext context) throws IOException {
        StringBuffer mb = new StringBuffer();
        ResponseWriter writer = context.getResponseWriter();

        String uniqueboxname = (String)getAttributes().get("uniqueboxname");

        String x = uniqueboxname;
        mb.append("</p>\n" +
                "</div>\n" +
                "<b class=\"xbottom\"><b class=\"xb4"+x+"\"></b><b class=\"xb3"+x+"\"></b><b class=\"xb2"+x+"\"></b><b class=\"xb1"+x+"\"></b></b>\n" +
                "</div>\n" +
                "<!-- End Rounded Corner Box -->");

        //Output
        writer.write(mb.toString());

    }

}
