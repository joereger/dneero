<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyRequirements" %>
<%@ page import="com.dneero.dao.Survey" %>
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
String pagetitle = ((PublicSurveyRequirements)Pagez.getBeanMgr().get("PublicSurveyRequirements")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



    <font class="smallfont"><%=((PublicSurveyRequirements) Pagez.getBeanMgr().get("PublicSurveyRequirements")).getSurvey().getDescription()%></font><br/><br/><br/>


    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Share It"><span>Share It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Answers"><span>Answers</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Who's In?"><span>Who's In?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Disclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
    To join this conversation and get paid for it you must fulfill these demographic requirements.  If you answer the questions before logging in and/or signing up and then do not fulfill these requirements we will discard your answers.
    </font></div></center>
    <%=publicSurveyRequirements.getSurveyCriteriaAsHtml()%>







<%@ include file="/template/footer.jsp" %>


