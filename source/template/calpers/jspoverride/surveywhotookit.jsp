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

    <% Survey surveyInTabs = publicSurveyWhotookit.getSurvey();%>
    <%@ include file="/template/calpers/jspoverride/surveytabs.jsp" %>

    <a href="/survey.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyWhotookit.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>

        <font class="normalfont">
            <ul>
               <li>Below you will see all fellow jammers who have joined this conversation.</li>
               <li>When you click on the name in the "Person" field, you will see a page that lists all  conversations that he/she has joined.</li>
               <li>When you click on the "[name] Answers", you will see his/her answers at the top of the  page in a You Tube like widget.</li>
            </ul>
        </font>

    <br/><br/>

    <br/>
    <%--<div id="tabs">--%>
        <%--<ul>--%>
            <%--<li><a href="#tabs-1">Who Took It?</a></li>--%>
            <%--&lt;%&ndash;<%if (!Pagez.getUserSession().getIsfacebookui()){%>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<li><a href="#tabs-2">Where It's Been Posted</a></li>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<% } %>&ndash;%&gt;--%>
        <%--</ul>--%>
        <%--<div id="tabs-1">--%>
            <center>
            <div class="rounded" style="background: #ffffff; width: 800px;">
            <%=publicSurveyWhotookit.getWhotookitHtml()%>
            </div>
            </center>
        <%--</div>--%>
        <%--<div id="tabs-2">--%>
            <%--<%=publicSurveyWhotookit.getImpressionsHtml()%>--%>
        <%--</div>--%>
    <%--</div>--%>


<script>
        $('#tabs').tabs();
</script>    




<%@ include file="/template/footer.jsp" %>


