<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Social Networks";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">

            <font class="mediumfont" style="color: #999999">It's Not Just About Blogging</font>
            <br/>
            This system reaches deep into the rich world of social networks.  We support posting <%=Pagez._surveys()%> into MySpace, Facebook and others.  We have a deeply-integrated Facebook App.
            <br clear="all"/><br/><br/>

            <font class="mediumfont" style="color: #999999">The Hard-to-reach Facebook Profile</font>
            <br/>
            It's very hard to get onto users' Facebook profiles.  Ads are blocked.  The key is to start a <%=Pagez._survey()%>.  By engaging users in a <%=Pagez._survey()%> they will put you right in the middle of their profiles.
            <br clear="all"/><br/><br/>

        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>