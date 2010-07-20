<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyRequirements" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%String jspPageName="/surveyrequirements.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
PublicSurveyRequirements publicSurveyRequirements = (PublicSurveyRequirements)Pagez.getBeanMgr().get("PublicSurveyRequirements");
%>
<%
    //If we don't have a surveyid, shouldn't be on this page
    if (publicSurveyRequirements.getSurvey() == null || publicSurveyRequirements.getSurvey().getTitle() == null || publicSurveyRequirements.getSurvey().getSurveyid()<=0) {
        Pagez.sendRedirect("/publicsurveylist.jsp");
        return;
    }
    //If the survey is draft or waiting
    if (publicSurveyRequirements.getSurvey().getStatus()<Survey.STATUS_OPEN) {
        Pagez.sendRedirect("/surveynotopen.jsp");
        return;
    }
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



    <% Survey surveyInTabs = publicSurveyRequirements.getSurvey();%>
    <%@ include file="/surveytabs.jsp" %>

    <a href="/survey.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyRequirements.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>

    <font class="mediumfont" style="color: #666666;">Requirements</font>
    <br/><br/>

    <% if (!publicSurveyRequirements.getSurvey().getIsopentoanybody()){ %>
        <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
        <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
        To join this <%=Pagez._survey()%> you must fulfill these demographic requirements.  If you answer the questions before logging in and/or signing up and then do not fulfill these requirements we will discard your answers.
        </font></div></center>
        <%=publicSurveyRequirements.getSurveyCriteriaAsHtml()%>
    <% } else { %>
        This <%=Pagez._survey()%> is open to anybody.
    <% } %>





<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>


