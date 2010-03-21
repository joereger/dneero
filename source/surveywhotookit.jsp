<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyWhotookit" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dao.Survey" %>
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


    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Share It"><span>Share It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Answers"><span>Answers</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Who's In?"><span>Who's In?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Disclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>

    <a href="/survey.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyWhotookit.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="150">
                <img src="/images/users-128.png" width="128" height="128"/>
            </td>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                People who joined the conversation and where it's been posted.
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




<%@ include file="/template/footer.jsp" %>


