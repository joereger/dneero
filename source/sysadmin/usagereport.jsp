<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminEditEula" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.CustomercareCharityReport" %>
<%@ page import="com.dneero.htmluibeans.SysadminDemographicReport" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Usage Report";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminUsageReport sysadminUsageReport = (SysadminUsageReport) Pagez.getBeanMgr().get("SysadminUsageReport");
%>
<%@ include file="/template/header.jsp" %>

    <table cellpadding='0' cellspacing='5' border='0'>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Total Users</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getTotalUsers()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Total Active Users</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getTotalActiveUsers()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Total Disabled Users</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getTotalDisabledUsers()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Users Who've Joined a Convo</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getUsersWhoHaveJoinedAConvo()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Users Who've Never Joined a Convo</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getUsersWhoHaveNeverJoinedAConvo()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Users Who've Posted</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getUsersWhoHavePosted()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Users Who've Earned a Balance</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getUsersWhoHaveBeenPaidInBalance()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Users Who've Been Paid</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getUsersWhoHaveBeenPaidInRealWorld()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Users On Latest EULA</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getUsersOnLatestEULA()%></font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">Users Logged-in In Last X Days</font>
        </td><td valign="top">
        <font class="tinyfont">
            <%
                Calendar startedLoggingLoginDatesOn = Time.dbstringtocalendar("2008-12-10 00:00:00");
                int daysSinceLoggingStarted = DateDiff.dateDiff("day", Calendar.getInstance(), startedLoggingLoginDatesOn);
            %>
            <%if (daysSinceLoggingStarted<3){%>
            Check back in a few days.
            <%}%>
            <%if (daysSinceLoggingStarted>=3){%>
            3 Days --> <%=sysadminUsageReport.getUsersLoggedInLast3Days()%> users logged in<br/>
            <%}%>
            <%if (daysSinceLoggingStarted>=7){%>
            7 Days --> <%=sysadminUsageReport.getUsersLoggedInLast7Days()%> users logged in<br/>
            <%}%>
            <%if (daysSinceLoggingStarted>=14){%>
            14 Days --> <%=sysadminUsageReport.getUsersLoggedInLast14Days()%> users logged in<br/>
            <%}%>
            <%if (daysSinceLoggingStarted>=30){%>
            30 Days --> <%=sysadminUsageReport.getUsersLoggedInLast30Days()%> users logged in<br/>
            <%}%>
            <%if (daysSinceLoggingStarted>=60){%>
            60 Days --> <%=sysadminUsageReport.getUsersLoggedInLast60Days()%> users logged in<br/>
            <%}%>
            <%if (daysSinceLoggingStarted>=90){%>
            90 Days --> <%=sysadminUsageReport.getUsersLoggedInLast90Days()%> users logged in<br/>
            <%}%>
            <%if (daysSinceLoggingStarted>=180){%>
            180 Days --> <%=sysadminUsageReport.getUsersLoggedInLast180Days()%> users logged in<br/>
            <%}%>
            <%if (daysSinceLoggingStarted>=365){%>
            365 Days --> <%=sysadminUsageReport.getUsersLoggedInLast365Days()%> users logged in<br/>
            <%}%>
        </font>
        </td></tr>

        <tr><td valign="top" align="right">
        <font class="formfieldnamefont">How Many Convos Users are Joining</font>
        </td><td valign="top">
        <font class="normalfont"><%=sysadminUsageReport.getNumberOfConvosVsUsersAsHtml()%></font>
        </td></tr>

    </table>




<%@ include file="/template/footer.jsp" %>