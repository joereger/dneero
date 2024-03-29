<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%String jspPageName="/researcher/about-segmentation.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Segmentation and Targeting";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">It's Easy to Target Your <%=Pagez._Survey()%></font>
            <br/>
            When people sign up to join <%=Pagez._surveys()%> they tell us a number of things about themselves.  When you create a <%=Pagez._survey()%> you can limit it to certain demographics.  People who don't fit your target can't take it.  The current list of demographic fields is:
            <ul>
                <li>Age</li>
                <li>Gender</li>
                <li>Ethnicity</li>
                <li>Marital Status</li>
                <li>Income</li>
                <li>Education</li>
                <li>City, State</li>
                <li>Profession</li>
                <li>Interest</li>
                <li>Politics</li>
            </ul>
            <br clear="all"/><br/><br/>

            <font class="mediumfont" style="color: #999999">Access Code</font>
            <br/>
            Access Code Only <%=Pagez._surveys()%> require that everybody who takes the <%=Pagez._survey()%> first enter an access code that you somehow communicate to them. In this way you can limit and control who join your <%=Pagez._survey()%>. Great for point-of-sale and real-world ties to the online world.
            <br clear="all"/><br/><br/>

            <font class="mediumfont" style="color: #999999">Standing Panels for Longitudinal Studies</font>
            <br/>
            It's easy to build a panel of people to join repeated <%=Pagez._surveys()%>.  In this way you can measure a fixed group's opinion over time.
            <br clear="all"/><br/><br/>

            <font class="mediumfont" style="color: #999999">Target Facebook Only, Blogger Only or Both</font>
            <br/>
            Sometimes you only want your campaign executed in certain places.  No problem.  With a couple clicks you'll limit respondents to Facebook, the blogosphere or leave it open to everybody.
            <br clear="all"/><br/><br/>

        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
            <img src="/images/segmentation-fullscreen.jpg" alt="" align="right" width="200">
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>