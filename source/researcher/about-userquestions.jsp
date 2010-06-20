<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "User Questions";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">Users Add Questions</font>
            <br/>
            When a user joins a <%=Pagez._survey()%> they're asked a starting set of questions.  They then have to ask one additional question of their own.  People who join the <%=Pagez._survey()%> after seeing the original person's answers will have to answer that question... and add one of their own.  As the <%=Pagez._survey()%> expands to three levels each respondent will answer the three questions that were added by the three people who took it before them.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Expanding the Scope Highlights Missing Links</font>
            <br/>
            One of the most powerful things about this model is that you'll hear exactly what you're not telling your customers. Often, this is more important to crafting your ongoing message than what you know you're telling them today.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Deep, but Not Too Deep</font>
            <br/>
            For an average sized <%=Pagez._survey()%> you may expect to see three or four levels meaning that late participants answer that many additional questions.  We never want to create a situation where people have to answer 100 questions.
        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>