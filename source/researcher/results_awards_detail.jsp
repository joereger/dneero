<%@ page import="com.dneero.dao.Incentiveaward" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.Surveyincentive" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResultsAnswers" %>
<%@ page import="com.dneero.incentive.Incentive" %>
<%@ page import="com.dneero.incentive.IncentiveFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%String jspPageName="/researcher/results_awards_detail.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherResults researcherResults = (ResearcherResults) Pagez.getBeanMgr().get("ResearcherResults");
%>
<%
    Incentiveaward incentiveaward = null;
    if (Num.isinteger(request.getParameter("incentiveawardid"))){
        incentiveaward = Incentiveaward.get(Integer.parseInt(request.getParameter("incentiveawardid")));
    } else {
        Pagez.getUserSession().setMessage("That award was not found.");
        Pagez.sendRedirect("/researcher/results_awards.jsp");
        return;
    }
%>
<%@ include file="/template/header.jsp" %>

<div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResults.getSurvey().getTitle()%></font>
        <br/>
        <a href="/researcher/results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="/researcher/results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Results</font></a>
        <a href="/researcher/results_answers_userquestions.jsp"style="padding-left: 15px;"><font class="subnavfont">User Questions</font></a>
        <a href="/researcher/results_answers_advanced.jsp"style="padding-left: 15px;"><font class="subnavfont">Filter Results</font></a>
        <a href="/researcher/results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="/researcher/results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="/researcher/results_awards.jsp"style="padding-left: 15px;"><font class="subnavfont">Awards</font></a>
        <a href="/researcher/results_demographics.jsp"style="padding-left: 15px;"><font class="subnavfont">Demographics</font></a>
        <a href="/researcher/results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>



<%
Response resp = Response.get(incentiveaward.getResponseid());
Survey survey = Survey.get(resp.getSurveyid());
Surveyincentive si = Surveyincentive.get(incentiveaward.getSurveyincentiveid());
Incentive incentive = IncentiveFactory.getById(si.getType(), si);
%>


    <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">--%>
            <%--<font class="smallfont" style="color: #000000;">These are the things that you've been awarded.</font>--%>
    <%--</div>--%>
    <%--<br/><br/>--%>

<font class="mediumfont"><b>Award:</b> <%=incentive.getShortSummary()%></font>
<%if (incentive.getShortSummary().length()<incentive.getFullSummary().length()){%>
    <br/><font class="smallfont"><b>Full Summary:</b> <%=incentive.getFullSummary()%></font>
<%}%>
<br/><br/>
<font class="smallfont"><b>Instructions:</b> <%=incentive.getInstructionsAfterAward(resp)%></font>
<br/><br/>
<br/><font class="tinyfont"><b>From Survey:</b> <%=survey.getTitle()%></font>
<br/><font class="tinyfont"><b>Date:</b> <%=Time.dateformatcompactwithtime(Time.getCalFromDate(incentiveaward.getDate()))%></font>
<br/>
<br/><font class="tinyfont"><b>1:</b> <%=incentiveaward.getMisc1()%></font>
<br/><font class="tinyfont"><b>1:</b> <%=incentiveaward.getMisc2()%></font>
<br/><font class="tinyfont"><b>1:</b> <%=incentiveaward.getMisc3()%></font>
<br/><font class="tinyfont"><b>1:</b> <%=incentiveaward.getMisc4()%></font>
<br/><font class="tinyfont"><b>1:</b> <%=incentiveaward.getMisc5()%></font>
    


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>