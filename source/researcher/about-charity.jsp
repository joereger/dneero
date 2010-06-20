<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Charity Capabilities";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">Remove Any Concern Over Bias</font>
            <br/>
            Money introduces the argument that responses are biased.  While we require full disclosure and argue that since people are posting their answers in front of their peers the bias is limited, we do understand that for some campaigns you simply can't have any questionable bias.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Charity Only <%=Pagez._Surveys()%></font>
            <br/>
            With a single check box you can designate a <%=Pagez._survey()%> as Charity Only.  Doing so means that only bloggers who agree to have all of their earnings from the <%=Pagez._survey()%> donated to a charity will be able to take it. Learn more about the program <a href="/charity.jsp">here</a>.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Bloggers Can Give to Charity</font>
            <br/>
            One concern from bloggers is that the money in this model presents possible bias, and that their readers will not want to feel 'monetized'. <%=Pagez.getUserSession().getPl().getNameforui()%> allows bloggers to give any of their earnings, from any <%=Pagez._survey()%> (Charity Only or not), to a good cause, avoid bias.
        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>