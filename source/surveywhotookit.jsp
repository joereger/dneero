<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyWhotookit" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dao.Survey" %>
<%String jspPageName="/surveywhotookit.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
PublicSurveyWhotookit publicSurveyWhotookit = (PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit");
%>
<%
    //If we don't have a surveyid, shouldn't be on this page
    if (publicSurveyWhotookit.getSurvey() == null || publicSurveyWhotookit.getSurvey().getTitle() == null || publicSurveyWhotookit.getSurvey().getSurveyid()<=0) {
        Pagez.sendRedirect("/publicsurveylist.jsp");
        return;
    }
    //If the survey is draft or waiting
    if (publicSurveyWhotookit.getSurvey().getStatus()<Survey.STATUS_OPEN) {
        Pagez.sendRedirect("/surveynotopen.jsp");
        return;
    }
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab="home";
String acl="public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <% Survey surveyInTabs = publicSurveyWhotookit.getSurvey();%>
    <%@ include file="/surveytabs.jsp" %>

    <a href="/survey.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyWhotookit.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="150">
                <img src="/images/users-128.png" width="32" height="32"/>
            </td>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                People who joined the <%=Pagez._survey()%> and where it's been posted.
                </font></div></center>
            </td>
        </tr>
    </table>

    <br/>
    <div id="tabs">
        <ul>
            <li><a href="#tabs-1">Who Took It?</a></li>
            <%if (!Pagez.getUserSession().getIsfacebookui()){%>
            <li><a href="#tabs-2">Where It's Been Posted</a></li>
            <% } %>
        </ul>
        <div id="tabs-1">
            <%=publicSurveyWhotookit.getWhotookitHtml()%>
        </div>
        <div id="tabs-2">
            <%=publicSurveyWhotookit.getImpressionsHtml()%>
    </div>


<script>
        $('#tabs').tabs();
</script>    




<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>


