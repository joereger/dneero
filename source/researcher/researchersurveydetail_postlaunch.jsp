<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetailPostlaunch" %>
<%String jspPageName="/researcher/researchersurveydetail_postlaunch.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<br clear=\"all\"/>";
if (((ResearcherSurveyDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch"))!=null && !((ResearcherSurveyDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch")).getTitle().equals("")){
    pagetitle = "<font class=\"pagetitlefont\">"+((ResearcherSurveyDetailPostlaunch) Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch")).getTitle()+"</font>\n" +
"        <br clear=\"all\"/>";
}

String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetailPostlaunch researcherSurveyDetailPostlaunch = (ResearcherSurveyDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch");
%>
<%
if (researcherSurveyDetailPostlaunch.getSurvey().getStatus()==Survey.STATUS_OPEN){
    Pagez.getUserSession().setMessage("Your "+Pagez._Survey()+" is launched!  Why not be the first to join?");
    Pagez.sendRedirect("/survey.jsp?surveyid="+researcherSurveyDetailPostlaunch.getSurvey().getSurveyid()+"");
    return;
}
%>
<%@ include file="/template/header.jsp" %>


    <font class="pagetitlefont" style="color: #333333;">Congratulations!  Your <%=Pagez._survey()%> has been launched!</font>
    <br/><br/>

    <%if (!researcherSurveyDetailPostlaunch.getSurvey().getIsfree()){%>
        <font class="mediumfont">Here's what to expect next:</font>
        <br/><br/>
        <ul>
            <li>You can no longer edit your <%=Pagez._survey()%>, but you can review it.</li>
            <li>We'll verify that your account balance has at least <%=researcherSurveyDetailPostlaunch.getInitialcharge()%> which represents 20% of the maximum possible spend, <%=researcherSurveyDetailPostlaunch.getMaxpossiblespend()%>, for your <%=Pagez._survey()%>.</li>
            <li>Social people will see your <%=Pagez._survey()%> and join in.</li>
                <ul>
                    <li>Each time a social person joins the <%=Pagez._survey()%> your account balance will be debited according to the incentive that you've set.</li>
                    <li>We will close your <%=Pagez._survey()%> either when the end date is reached or when the maximum number of respondents has been reached.</li>
                    <li>Social people will post the <%=Pagez._survey()%> to their peers.</li>
                </ul>
            <li>Every few hours (generally 24, but sometimes 48) we will count all of the blog impressions and debit your account for them.</li>
            <li>If your account balance dips below 5% of the maximum value of all of your currently-active <%=Pagez._surveys()%> at any given time then we will debit your account balance to get it back to the 20% mark.  For the case of <%=Pagez._surveys()%> nearing their end we will only incorporate the final amount remaining in the calculation.  If your balance remains below the 5% we will shut down all <%=Pagez._surveys()%>, placing them into a Waiting for Funds status wherein bloggers can not find them.</li>
            <li>You can watch blogger answers and impressions in real-time by going to the Results page for this <%=Pagez._survey()%>.  You get to that page by going to My <%=Pagez._Survey()%> and then clicking Results.</li>

        </ul>

        <font class="smallfont">We appreciate your business!  If you have any questions, you can use the <a href="/account/inbox.jsp">Help/Support Ticket</a> system.</font>

    <%} %>


    <%--<br/><br/>--%>
    <%--<font class="smallfont">Invite people to join:</font>--%>
    <%--<br/>--%>
    <%--<a href="/researcher/emailinvite.jsp?surveyid=<%=Pagez.getUserSession().getCurrentSurveyid()%>"><font class="normalfont">Invite People to Join</font></a>--%>

    <%--<br/><br/>--%>
    <%--<font class="smallfont">Promote:</font>--%>
    <%--<br/>--%>
    <%--<font class="smallfont"><%=researcherSurveyDetailPostlaunch.getSocialbookmarklinks()%></font>--%>

    <br/><br/>
    <font class="smallfont">Or return to your list of <%=Pagez._surveys()%>:</font>
    <br/>
    <a href="/researcher/index.jsp"><font class="mediumfont" style="color: #0000ff;">Return to List of <%=Pagez._Surveys()%></font></a>

    <br/><br/>




<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>