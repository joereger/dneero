package com.dneero.survey.servlet;

import org.apache.log4j.Logger;

import java.net.URLEncoder;

import com.dneero.systemprops.BaseUrl;
import com.dneero.dao.Survey;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jan 10, 2007
 * Time: 12:33:31 PM
 */
public class EmbedInHtmlSyntax {



    public static String getJs(String baseurl, int surveyid, int userid, boolean ispreview){
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }
        String urlofsurvey = baseurl+"s?s="+surveyid+"&u="+userid+"&ispreview="+ispreviewStr;
        return "<script src=\""+urlofsurvey+"\"></script>";
    }

    public static String getFlash(String baseurl, int surveyid, int userid, boolean ispreview){
        Logger logger = Logger.getLogger(EmbedInHtmlSyntax.class);
        String out = "";
        String ispreviewStr = "0";
        if (ispreview){
            ispreviewStr = "1";
        }
        if (baseurl.equals("")){
            baseurl = "/";
        }

        StringBuffer surveyashtml = new StringBuffer();
        surveyashtml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        surveyashtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        surveyashtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
        surveyashtml.append("<head>");
        surveyashtml.append("<title>dNeero Survey</title>");
        surveyashtml.append("</head>");
        surveyashtml.append("<body>");
        surveyashtml.append(SurveyAsHtml.getHtml(Survey.get(surveyid), User.get(userid), false));
        surveyashtml.append("</body>");
        surveyashtml.append("</html>");
        
        //String surveyashtmlquotes = surveyashtml.toString().replaceAll("\\\"","\\\\\"");
        String surveyashtmlquotes = surveyashtml.toString();
        logger.debug("surveyashtmlquotes = "+surveyashtmlquotes);
       

        String surveyashtmlencoded = surveyashtmlquotes;
        try{surveyashtmlencoded = URLEncoder.encode(surveyashtmlquotes, "UTF-8");}catch(Exception ex){logger.error(ex); surveyashtmlencoded = surveyashtmlquotes;}

        String baseurlencoded = BaseUrl.get(false);
        try{baseurlencoded = URLEncoder.encode(baseurlencoded, "UTF-8");}catch(Exception ex){logger.error(ex); baseurlencoded = BaseUrl.get(false);}


        String urlofmovie = baseurl+"flashviewer/dneerosurvey.swf?s="+surveyid+"&u="+userid+"&ispreview="+ispreviewStr+"&baseurl="+baseurlencoded;

        out = "<!-- Start dNeero Survey --><object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\"425\" height=\"300\" id=\"dneeroflashviewer\" align=\"middle\">" +
              "<param name=\"allowScriptAccess\" value=\"never\" />" +
              "<param name=\"movie\" value=\""+urlofmovie+"\"/>"+
              "<param name=\"FlashVars\" value=\"surveyashtml="+surveyashtmlencoded+"\"/>"+
              "<param name=\"quality\" value=\"high\" /><param name=\"bgcolor\" value=\"#ffffff\" />"+
              "<embed src=\""+urlofmovie+"\" FlashVars=\"surveyashtml="+surveyashtmlencoded+"\" quality=\"high\" bgcolor=\"#ffffff\" width=\"425\" height=\"300\" name=\"dneeroflashviewer\" align=\"middle\" allowScriptAccess=\"never\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />" +
              "</object><!-- End dNeero Survey -->";
        return out;
    }


}
