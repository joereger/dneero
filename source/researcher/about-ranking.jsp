<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Custom Rankings";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">What are Rankings?</font>
            <br/>
             Rankings allow you to choose certain questions as indicators of a quality that you want to track. For example, you could create an "Environmentally Friendly" Ranking and when people answer the question "Do you care about the environment?" with a "Yes" you assign them 50 points. 
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Identify Users Across Many Conversations</font>
            <br/>
            You can use Rankings to measure across many conversations.  A conversation here generates a few points... a conversation there a few. In this way you get a sense of how a person responds over time to many different things.  You're using your dialog to learn.  And you're using the Ranking to quantify.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Build Panels with Rankings</font>
            <br/>
            As people score points you can skim those in the top 10% of Ranking score and put them into a Panel which allows you to create conversations that only they can see and take. In this way you're not only targeting but engaging your audience.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Ranking Strength = Average Normalized Points</font>
            <br/>
            You get an ordered list of people who have a rank. Along with the number of points they have is a Ranking Strength. Ranking Strength is the average of normalized points awarded for each question. This is done because there's a difference between building many points over time with lowly Ranking-correlated answerd and quickly building many points with a few highly-Ranking-correlated answers. In short, people with a higher Ranking Strength are more highly correlated to the qualities that you're measuring with your Ranking.
        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>