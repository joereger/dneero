<%@ page import="com.dneero.dao.Incentiveaward" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.Surveyincentive" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.incentive.Incentive" %>
<%@ page import="com.dneero.incentive.IncentiveFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dneero.incentivetwit.Incentivetwit" %>
<%@ page import="com.dneero.incentivetwit.IncentivetwitFactory" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Award Detail";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Incentivetwitaward incentivetwitaward= null;
    if (Num.isinteger(request.getParameter("incentivetwitawardid"))){
        incentivetwitaward= Incentivetwitaward.get(Integer.parseInt(request.getParameter("incentivetwitawardid")));
    } else {
        Pagez.getUserSession().setMessage("That award was not found.");
        Pagez.sendRedirect("/account/awards.jsp");
        return;
    }
%>
<%@ include file="/template/header.jsp" %>

<%
Twitanswer twitanswer= Twitanswer.get(incentivetwitaward.getTwitanswerid());
Twitask twitask= Twitask.get(twitanswer.getTwitaskid());
Twitaskincentive si = Twitaskincentive.get(incentivetwitaward.getTwitaskincentiveid());
Incentivetwit incentive = IncentivetwitFactory.getById(si.getType(), si);
%>


    <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">--%>
            <%--<font class="smallfont" style="color: #000000;">These are the things that you've been awarded.</font>--%>
    <%--</div>--%>
    <%--<br/><br/>--%>

<font class="mediumfont"><b>Award:</b> <%=incentive.getShortSummary()%></font>
<%if (incentive.getShortSummary().length()<incentive.getFullSummary().length()){%>
    <br/><br/><font class="smallfont"><b>Full Summary:</b> <%=incentive.getFullSummary()%></font>
<%}%>
<br/><br/>
<font class="smallfont"><b>Instructions:</b> <%=incentive.getInstructionsAfterAward(twitanswer)%></font>
<br/><br/>
<br/><font class="tinyfont"><b>From Twitter Question:</b> <%=twitask.getQuestion()%></font>
<br/><font class="tinyfont"><b>Date:</b> <%=Time.dateformatcompactwithtime(Time.getCalFromDate(incentivetwitaward.getDate()))%></font>


<%@ include file="/template/footer.jsp" %>