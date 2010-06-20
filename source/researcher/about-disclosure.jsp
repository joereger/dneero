<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Ethics and Disclosure";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            
            <font class="mediumfont" style="color: #999999">Out in the Open</font>
            <br/>
            <img src="/images/redbutton-disclosure.gif" align="right"/>
            All peer postings must disclose the fact that the blogger was paid to join the <%=Pagez._survey()%>.  This ensures that the balance of trust in the blogosphere remains controlled by bloggers and their readers.  dNeero is dedicated to a free, excited and motivated blogosphere.  We want to bring researchers and marketers to the party and we want them to thrive, but we need to make sure they don't burn the couch before they go.
            <br/><br/><br clear="all"/>

            <font class="mediumfont" style="color: #999999">We Work to Reduce Bias</font>
            <br/>
            <img src="/images/redbutton-neutrality.gif" align="right"/>
            Bloggers are never required to post positive reviews.  dNeero defends the the blogger's freedom to thrash any product by forcing researchers to remain neutral.  This, of course, increases the quality of market research garnered by the process and avoids the sugging quagmire.  Paying people to be fake is not what dNeero is about.  In fact, the prospect of such companies terrifies us... they degrade the blogosphere and reduce the credibility of research done in it.  Read more about <a href="/evil.jsp">the forces affecting bloggers</a>.
            <br/><br/><br clear="all"/>

            <font class="mediumfont" style="color: #999999"><%=Pagez._Surveys()%> are Important</font>
            <br/>
            <img src="/images/redbutton-twoway.gif" align="right"/>
            Unlike other pay-for-blog schemes dNeero is a two-way process.  We'll stop short of claiming full Cluetrain credentials.  But the fact is that a researcher generates exposure and collects information from a market at the same time.  This is a first step to lowering the walls between the marketing department and the market research department.  dNeero believes that in the digital age outbound messaging and inbound communication should be united.  We didn't make this stuff up... go read Cluetrain.

        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>