package com.dneero.htmluibeans;

import com.dneero.util.Io;
import com.dneero.util.Str;
import com.dneero.util.Util;

import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.io.FilenameUtils;

/**
 * User: Joe Reger Jr
 * Date: Nov 8, 2007
 * Time: 12:47:45 PM
 */
public class Madconverter {

    public static StringBuffer startProcessing(String filename){
        File file = new File(filename);
        return process(file, 0, new StringBuffer());
    }


    public static StringBuffer process(File in, int level, StringBuffer sb) {
        if (in.isDirectory()) {
            sb.append("<br/><b>Directory (lev: "+level+"):</b> "+in.getAbsolutePath());
            level = level + 1;
            String[] children = in.list();
            for (int i=0; i<children.length; i++) {
                process(new File(in, children[i]), level, sb);
            }
        } else if (in.isFile()){
            sb.append("<br/><b>File:</b> "+in.getAbsolutePath());
            sb.append(convertFile(in.getAbsolutePath()));
        }
        return sb;
    }



    public static StringBuffer convertFile(String filename){
        StringBuffer out = new StringBuffer();
        StringBuffer result = new StringBuffer();

        //Open file, put into String
        String htmlOrig = Io.textFileRead(filename).toString();
        result = new StringBuffer(htmlOrig);

        //<c:if
        if (1==1){
            Pattern p = Pattern.compile("<c:if test=\"(.|\\n)*?\"\\>");
            Matcher m = p.matcher(result.toString());
            result = new StringBuffer();
            while (m.find()) {
                String rawTag = m.group();
                out.append("<br>rawTag=" + rawTag + "<br>");
                String tag = rawTag.substring(11, rawTag.length()-2);
                out.append("tag=" + tag + "<br>");

                StringBuffer repl = new StringBuffer();
                String o = "<";
                repl.append("<"+"%"+" if ("+tag+"){ "+"%"+">");
                out.append("repl="+repl.toString()+"<br>");

                m.appendReplacement(result, Str.cleanForAppendreplacement(repl.toString()));
            }
            try {m.appendTail(result);} catch (Exception e) {out.append("error:"+e.getMessage());}
        }

        //#{bean.prop} tags
        if (1==1){
            Pattern p = Pattern.compile("(\\#|\\$)\\{(.|\\n)*?\\}");
            Matcher m = p.matcher(result.toString());
            result = new StringBuffer();
            while (m.find()) {
                String rawTag = m.group();
                out.append("Found rawTag=" + rawTag + "<br>");
                String tag = rawTag.substring(2, rawTag.length()-1);
                out.append("tag=" + tag + "<br>");
                String[] split = tag.split("\\.");
                if (tag.trim().indexOf(" ")<=-1){
                    out.append("split.length=" + split.length + "<br>");
                    if (split.length >= 2) {
                        //((AccountIndex)Pagez.getBeanMgr().get("AccountIndex")).getIsfirsttimelogin()
                        String beanname = split[0];
                        String beannameCap = Str.capitalizeFirstLetter(beanname);
                        out.append("beanname=" + beanname + "<br>");
                        out.append("beannameCap=" + beannameCap + "<br>");

                        StringBuffer repl = new StringBuffer();
                        String o = "<";
                        repl.append("<"+"%"+"=((" + beannameCap + ")Pagez.getBeanMgr().get(\"" + beannameCap + "\"))");
                        for (int i = 1; i < split.length; i++) {
                            String propname = split[i];
                            String propnameCap = Str.capitalizeFirstLetter(propname);
                            repl.append(".get"+propnameCap+"()");
                        }
                        repl.append("%"+">");
                        out.append("repl="+repl.toString()+"<br>");

                        m.appendReplacement(result, Str.cleanForAppendreplacement(repl.toString()));
                    }
                }
            }
            try {m.appendTail(result);} catch (Exception e) {out.append("error:"+e.getMessage());}
        }



        if (1==1){
            String a = result.toString();
            a = a.replaceAll("</c:if>", "<"+"%"+" } "+"%"+">");
            result = new StringBuffer(a);
        }

        if(1==1){
            String a = result.toString();
            a = a.replaceAll("<h:panelGrid columns=\"(.|\\n)*?\" cellpadding=\"(.|\\n)*?\" border=\"0\">", "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
            result = new StringBuffer(a);
        }

        if (1==1){
            String a = result.toString();
            a = a.replaceAll("<h:panelGroup>", "<td valign=\"top\">");
            result = new StringBuffer(a);
        }

        if (1==1){
            String a = result.toString();
            a = a.replaceAll("</h:panelGroup>", "</td>");
            result = new StringBuffer(a);
        }

        if (1==1){
            String a = result.toString();
            a = a.replaceAll("</h:panelGrid>", "</table>");
            result = new StringBuffer(a);
        }

        //Write to the file
        Io.writeTextToFile(new File(filename), result.toString());


        out.append("<br><br>");
        out.append("RESULT");
        out.append("<br><br>");

        out.append(result.toString().replaceAll("<", "&lt;").replaceAll("\n", "<br>"));

        return out;
    }


}
