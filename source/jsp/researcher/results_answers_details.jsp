<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsAnswersDetails.getSurvey().getTitle()%></font>
        <br/>
        <a href="results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Response Report</font></a>
        <a href="results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>

<font class="mediumfont" style="color: #cccccc;">Question: <%=((ResearcherResultsAnswersDetails)Pagez.getBeanMgr().get("ResearcherResultsAnswersDetails")).getQuestion().getQuestion()%></font>
<br/><br/>

<f:verbatim><%=((ResearcherResultsAnswersDetails)Pagez.getBeanMgr().get("ResearcherResultsAnswersDetails")).getResults()%></f:verbatim>

    


<%@ include file="/jsp/templates/footer.jsp" %>