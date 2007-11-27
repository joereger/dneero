<%@ page import="com.dneero.util.Num" %>
<%@ page import="com.dneero.systemprops.BaseUrl" %>
<%@ page import="com.dneero.survey.servlet.SurveyFlashServlet" %>
<%@ page import="com.dneero.survey.servlet.SurveyJavascriptServlet" %>
<%@ page import="com.dneero.htmlui.UserSession" %>
<%@ page import="com.dneero.htmlui.Authorization" %>
<%
//Hide from snooping eyes... only sysadmins can play
UserSession userSession = (UserSession) session.getAttribute("userSession");
if (userSession == null || !userSession.getIsloggedin() || !Authorization.isUserSysadmin(userSession.getUser())) {
    response.sendRedirect("/");
    return;
}
%>
<%
    int surveyid = 1;
    if (request.getParameter("s") != null && Num.isinteger(request.getParameter("s"))) {
        surveyid = Integer.parseInt(request.getParameter("s"));
    }

    int userid = 1;
    if (request.getParameter("u") != null && Num.isinteger(request.getParameter("u"))) {
        userid = Integer.parseInt(request.getParameter("u"));
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>Survey Test</title>
</head>

<body>
<form action="surveytest.jsp" method="get">
    <input type="hidden" name="dpage" value="surveytest.jsp">
<font face="impact" size="+3" color="#00ff00">Crazy Eddie's Blog</font>
<br>
    <select name="u">
        <%
        for(int i=1; i<=100; i++){
            String selected = "";
            if (i==userid){
                selected = "selected";
            }
            out.print("<option value=\""+i+"\" "+selected+">Userid "+i+"</option>");
        }
        %>
    </select>
    <select name="s">
        <%
        for(int i=1; i<=100; i++){
            String selected = "";
            if (i==surveyid){
                selected = "selected";    
            }
            out.print("<option value=\""+i+"\" "+selected+">Surveyid "+i+"</option>");
        }
        %>
    </select>
    <input type="submit" class="formsubmitbutton" value="Go">
</form>
<br><br>
Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque in ipsum. Donec nisl velit, vestibulum et, eleifend scelerisque, aliquam in, ligula. Phasellus sit amet nunc. Vestibulum pellentesque velit sit amet nunc. Aliquam id ante bibendum lectus vulputate cursus. Aenean lorem. Donec consequat sapien. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Nulla egestas. Cras sit amet risus. Suspendisse adipiscing tempus orci. Nunc ac nibh ut dolor bibendum feugiat. Suspendisse rutrum aliquet lorem. In id augue et turpis sollicitudin porta. Fusce sit amet ligula. Praesent auctor. Praesent ac metus non turpis lacinia suscipit. Integer molestie felis eu dolor. Aliquam eros. Vivamus a urna sed nibh pellentesque ornare.
<br><br>
Cras eget mauris in sem pretium dignissim. Donec nec pede id neque dapibus pellentesque. Etiam ac dolor non lectus gravida malesuada. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent tortor eros, feugiat et, rhoncus sed, rutrum nec, lectus. Sed libero libero, consectetuer eget, volutpat et, vulputate quis, lorem. Donec rutrum sem quis enim. Mauris rutrum pede in lorem. Praesent blandit elit. Phasellus in quam vitae lectus interdum vulputate. Aliquam iaculis. Nam id mi id felis suscipit malesuada. Nullam pharetra. Aliquam velit dolor, accumsan sit amet, egestas in, luctus eu, felis. Nulla pulvinar. Integer sit amet nunc. Sed pellentesque pede tincidunt leo.
<br><br>
<%=SurveyFlashServlet.getEmbedSyntax(BaseUrl.get(false), surveyid, userid, 0, false, true, true)%>
<br><br>
Cras eget mauris in sem pretium dignissim. Donec nec pede id neque dapibus pellentesque. Etiam ac dolor non lectus gravida malesuada. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent tortor eros, feugiat et, rhoncus sed, rutrum nec, lectus. Sed libero libero, consectetuer eget, volutpat et, vulputate quis, lorem. Donec rutrum sem quis enim. Mauris rutrum pede in lorem. Praesent blandit elit. Phasellus in quam vitae lectus interdum vulputate. Aliquam iaculis. Nam id mi id felis suscipit malesuada. Nullam pharetra. Aliquam velit dolor, accumsan sit amet, egestas in, luctus eu, felis. Nulla pulvinar. Integer sit amet nunc. Sed pellentesque pede tincidunt leo.
<br><br>
Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque in ipsum. Donec nisl velit, vestibulum et, eleifend scelerisque, aliquam in, ligula. Phasellus sit amet nunc. Vestibulum pellentesque velit sit amet nunc. Aliquam id ante bibendum lectus vulputate cursus. Aenean lorem. Donec consequat sapien. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Nulla egestas. Cras sit amet risus. Suspendisse adipiscing tempus orci. Nunc ac nibh ut dolor bibendum feugiat. Suspendisse rutrum aliquet lorem. In id augue et turpis sollicitudin porta. Fusce sit amet ligula. Praesent auctor. Praesent ac metus non turpis lacinia suscipit. Integer molestie felis eu dolor. Aliquam eros. Vivamus a urna sed nibh pellentesque ornare.
<br><br>
Cras eget mauris in sem pretium dignissim. Donec nec pede id neque dapibus pellentesque. Etiam ac dolor non lectus gravida malesuada. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent tortor eros, feugiat et, rhoncus sed, rutrum nec, lectus. Sed libero libero, consectetuer eget, volutpat et, vulputate quis, lorem. Donec rutrum sem quis enim. Mauris rutrum pede in lorem. Praesent blandit elit. Phasellus in quam vitae lectus interdum vulputate. Aliquam iaculis. Nam id mi id felis suscipit malesuada. Nullam pharetra. Aliquam velit dolor, accumsan sit amet, egestas in, luctus eu, felis. Nulla pulvinar. Integer sit amet nunc. Sed pellentesque pede tincidunt leo.
<br><br>
<%=SurveyJavascriptServlet.getEmbedSyntax(BaseUrl.get(false), surveyid, userid, 0, false, true, true, true)%>
<br><br>
Cras eget mauris in sem pretium dignissim. Donec nec pede id neque dapibus pellentesque. Etiam ac dolor non lectus gravida malesuada. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent tortor eros, feugiat et, rhoncus sed, rutrum nec, lectus. Sed libero libero, consectetuer eget, volutpat et, vulputate quis, lorem. Donec rutrum sem quis enim. Mauris rutrum pede in lorem. Praesent blandit elit. Phasellus in quam vitae lectus interdum vulputate. Aliquam iaculis. Nam id mi id felis suscipit malesuada. Nullam pharetra. Aliquam velit dolor, accumsan sit amet, egestas in, luctus eu, felis. Nulla pulvinar. Integer sit amet nunc. Sed pellentesque pede tincidunt leo.




</body>
</html>
