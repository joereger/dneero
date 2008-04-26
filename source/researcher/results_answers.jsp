<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResultsAnswers" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResultsAnswersCsv" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherResultsAnswers researcherResultsAnswers = (ResearcherResultsAnswers) Pagez.getBeanMgr().get("ResearcherResultsAnswers");
%>
<%@ include file="/template/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsAnswers.getSurvey().getTitle()%></font>
        <br/>
        <a href="/researcher/results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="/researcher/results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Results</font></a>
        <a href="/researcher/results_answers_advanced.jsp"style="padding-left: 15px;"><font class="subnavfont">Filter Results</font></a>
        <a href="/researcher/results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="/researcher/results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="/researcher/results_demographics.jsp"style="padding-left: 15px;"><font class="subnavfont">Demographics</font></a>
        <a href="/researcher/results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>
    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    This screen shows results for all respondents.  You can also <a href="/researcher/results_answers_advanced.jsp?surveyid=<%=researcherResultsAnswers.getSurvey().getSurveyid()%>">create filters</a> or <a href="/researcher/panels-addpeople.jsp?surveyid=<%=researcherResultsAnswers.getSurvey().getSurveyid()%>&showonly=addbysurvey">add these respondents to a panel</a>.
    </font></div></center>
    <br/>
    <%=researcherResultsAnswers.getResults()%>
    
    <br/><br/>
    <%//@todo download results as csv page%>
    <!--<a href="/researcher/results_csv.jsp">Download Results as CSV</a>-->
    


<%@ include file="/template/footer.jsp" %>