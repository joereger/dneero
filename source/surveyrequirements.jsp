<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyRequirements" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicSurveyRequirements)Pagez.getBeanMgr().get("PublicSurveyRequirements")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicSurveyRequirements publicSurveyRequirements = (PublicSurveyRequirements)Pagez.getBeanMgr().get("PublicSurveyRequirements");
%>
<%@ include file="/template/header.jsp" %>



    <font class="smallfont"><%=((PublicSurveyRequirements) Pagez.getBeanMgr().get("PublicSurveyRequirements")).getSurvey().getDescription()%></font><br/><br/><br/>


    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurveyRequirements.getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
    To take this survey and get paid for it you must fulfill these demographic requirements.  If you take the survey before logging in and/or signing up and then do not fulfill these requirements we will discard your answers.
    </font></div></center>
    <%=publicSurveyRequirements.getSurveyCriteriaAsHtml()%>







<%@ include file="/template/footer.jsp" %>


