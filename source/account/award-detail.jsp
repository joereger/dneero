<%@ page import="com.dneero.dao.Incentiveaward" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.Surveyincentive" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountAwardsListitem" %>
<%@ page import="com.dneero.incentive.Incentive" %>
<%@ page import="com.dneero.incentive.IncentiveFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Award Detail";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Incentiveaward incentiveaward = null;
    if (Num.isinteger(request.getParameter("incentiveawardid"))){
        incentiveaward = Incentiveaward.get(Integer.parseInt(request.getParameter("incentiveawardid")));
    } else {
        Pagez.getUserSession().setMessage("That award was not found.");
        Pagez.sendRedirect("/account/awards.jsp");
        return;
    }
%>
<%@ include file="/template/header.jsp" %>

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


<%@ include file="/template/footer.jsp" %>