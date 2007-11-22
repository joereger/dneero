<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResultsRespondents" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
    ResearcherResultsRespondents researcherResultsRespondents=(ResearcherResultsRespondents) Pagez.getBeanMgr().get("ResearcherResultsRespondents");
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsRespondents.getSurvey().getTitle()%></font>
        <br/>
        <a href="results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Response Report</font></a>
        <a href="results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>

    <%if (researcherResultsRespondents.getList()==null || researcherResultsRespondents.getList().size()==0){%>
        <font class="normalfont">None... yet.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Date", "<$responsedate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "smallfont"));
            cols.add(new GridCol("Name", "<$firstname$> <$lastname$>", false, "", "smallfont"));
            cols.add(new GridCol("", "<a href=\"/jsp/profile.jsp?bloggerid=<$bloggerid$>\">Profile</a>", false, "", "smallfont"));
            cols.add(new GridCol("", "<a href=\"/jsp/profileanswers.jsp?surveyid="+researcherResultsRespondents.getSurvey().getSurveyid()+"&bloggerid=<$bloggerid$>\">Answers</a>", false, "", "smallfont"));
            cols.add(new GridCol("", "<a href=\"/jsp/profileimpressions.jsp?responseid=<$responseid$>\">Impressions</a>", false, "", "smallfont"));
        %>
        <%=Grid.render(researcherResultsRespondents.getList(), cols, 50, "results_respondents.jsp?surveyid=" + researcherResultsRespondents.getSurvey().getSurveyid(), "page")%>
    <%}%>




    


<%@ include file="/jsp/templates/footer.jsp" %>