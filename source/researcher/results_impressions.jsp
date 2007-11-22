<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResultsImpressions" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
    ResearcherResultsImpressions researcherResultsImpressions=(ResearcherResultsImpressions) Pagez.getBeanMgr().get("ResearcherResultsImpressions");
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsImpressions.getSurvey().getTitle()%></font>
        <br/>
        <a href="results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Response Report</font></a>
        <a href="results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    On this page you see a list of blogs that your survey has been posted to along with the number of times it has been viewed by that blog's readers.  You can visit each blog by clicking on the Specific Page link.
    <br/><br/><br/>
    </font></div></center>

    <br/><br/>

    <%if (researcherResultsImpressions.getResearcherResultsImpressionsListitems()==null || researcherResultsImpressions.getResearcherResultsImpressionsListitems().size()==0){%>
        <font class="normalfont">None... yet.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Specific Page", "<$referertruncated$>", false, "", "tinyfont"));
            cols.add(new GridCol("Impressions Qualifying", "<$impressionspaidandtobepaid$>", false, "", "tinyfont"));
            cols.add(new GridCol("Quality Rating", "<$impressionquality$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(researcherResultsImpressions.getResearcherResultsImpressionsListitems(), cols, 50, "results_impressions.jsp?surveyid="+researcherResultsImpressions.getSurvey().getSurveyid(), "page")%>
    <%}%>




    


<%@ include file="/jsp/templates/footer.jsp" %>