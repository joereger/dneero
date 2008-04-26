<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Network Demographics";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            These demographics tell you what the current network of dNeero users looks like.
            <br clear="all"/><br/><br/>
            <font class="mediumfont" style="color: #999999">The Hard-to-Reach Young Technology-Savvy Demographic</font>
            <br/>
            <img src="/images/demographics-by-age.jpg" alt="" align="right" width="200">
            In today's hyper-connected world you'd think the youth market would be simple to reach.  But they're not.  While they are the early adopters of technological channels of communication, they also actively avoid one-way advertising and focus on personal recommendations and two-way engagements.  dNeero's users are mostly within this youth demographic and seek empowering two-way relationships with the companies they join conversations from.
            <br clear="all"/><br/><br/>
            <font class="mediumfont" style="color: #999999">The Educated Masses: Over 70% are In College or Graduated</font>
            <br/>
            <img src="/images/demographics-by-education.jpg" alt="" align="right" width="200">
            You don't want to reach college grads because they'll statistically earn more.  You want to reach them because they're more connected.  College social scenes thrive on technology connection.  People bounce ideas off of one another all the time. One quarter of dNeero users are in college.  Over 45% have graduated and 11% have an advanced degree.
            <br clear="all"/><br/><br/>
            <font class="mediumfont" style="color: #999999">The Political Spectrum</font>
            <br/>
            <img src="/images/demographics-by-politics.jpg" alt="" align="right" width="200">
            dNeero users represent the far reach of the political spectrum.
            <br clear="all"/><br/><br/>
            <font class="mediumfont" style="color: #999999">Other Demographics</font>
            <br/>
            When dNeero users sign up they fill out a number of demographic data points.  Learn more about <a href="/researcher/about-segmentation.jsp">Segmentation and Targeting</a>.
            <br clear="all"/><br/><br/>
        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>