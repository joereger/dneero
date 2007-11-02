<%@ page import="com.dneero.util.Io" %>
<%@ page import="com.dneero.systemprops.WebAppRootDir" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="org.apache.log4j.Logger" %>
<%
    Logger logger = Logger.getLogger(this.getClass().getName());
    StringBuffer result = new StringBuffer();

    //Open file, put into String
    String htmlOrig = Io.textFileRead("C:/temp/systemprops.jsp").toString();

    //<c:if
    if (1==1){
        Pattern p = Pattern.compile("<c:if test=\"(.|\\n)*?\"\\>");
        Matcher m = p.matcher(htmlOrig);
        while (m.find()) {
            String rawTag = m.group();
            out.println("<br>rawTag=" + rawTag + "<br>");
            String tag = rawTag.substring(11, rawTag.length()-2);
            out.println("tag=" + tag + "<br>");

            StringBuffer repl = new StringBuffer();
            String o = "<";
            repl.append("<"+"%"+" if ("+tag+"){ "+"%"+">");
            out.println("repl="+repl.toString()+"<br>");

            m.appendReplacement(result, Str.cleanForAppendreplacement(repl.toString()));
        }
        try {m.appendTail(result);} catch (Exception e) {out.println("error:"+e.getMessage());}
    }

    //#{bean.prop} tags
    if (1==1){
        Pattern p = Pattern.compile("(\\#|\\$)\\{(.|\\n)*?\\}");
        Matcher m = p.matcher(htmlOrig);
        while (m.find()) {
            String rawTag = m.group();
            out.println("Found rawTag=" + rawTag + "<br>");
            String tag = rawTag.substring(2, rawTag.length()-1);
            out.println("tag=" + tag + "<br>");
            String[] split = tag.split("\\.");
            if (tag.indexOf(" ")<-1){
                out.println("split.length=" + split.length + "<br>");
                if (split.length >= 2) {
                    //((AccountIndex)Pagez.getBeanMgr().get("AccountIndex")).getIsfirsttimelogin()
                    String beanname = split[0];
                    String beannameCap = Str.capitalizeFirstLetter(beanname);
                    out.println("beanname=" + beanname + "<br>");
                    out.println("beannameCap=" + beannameCap + "<br>");

                    StringBuffer repl = new StringBuffer();
                    String o = "<";
                    repl.append("<"+"%"+"=((" + beannameCap + ")Pagez.getBeanMgr().get(\"" + beannameCap + "\"))");
                    for (int i = 1; i < split.length; i++) {
                        String propname = split[i];
                        String propnameCap = Str.capitalizeFirstLetter(propname);
                        repl.append(".get"+propnameCap+"()");
                    }
                    repl.append("%"+">");
                    out.println("repl="+repl.toString()+"<br>");

                    m.appendReplacement(result, Str.cleanForAppendreplacement(repl.toString()));
                }
            }
        }
        try {m.appendTail(result);} catch (Exception e) {out.println("error:"+e.getMessage());}
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


    out.print("<br><br>");
    out.print("RESULT");
    out.print("<br><br>");

    out.print(result.toString().replaceAll("<", "&lt;").replaceAll("\n", "<br>"));


%>