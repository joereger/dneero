<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetailPostlaunch" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-06.gif\" align=\"right\" width=\"350\" height=\"73\"/><br clear=\"all\"/>";
if (((ResearcherSurveyDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch"))!=null && !((ResearcherSurveyDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch")).getTitle().equals("")){
    pagetitle = "<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
"        <font class=\"pagetitlefont\">"+((ResearcherSurveyDetailPostlaunch) Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch")).getTitle()+"</font>\n" +
"        <br clear=\"all\"/>";
}

String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetailPostlaunch researcherSurveyDetailPostlaunch = (ResearcherSurveyDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherSurveyDetailPostlaunch");
%>
<%@ include file="/template/header.jsp" %>


    <font class="pagetitlefont" style="color: #333333;">Congratulations!  Your conversation has been ignited!</font>
    <br/><br/>
    <font class="mediumfont">Here's what to expect next:</font>
    <br/><br/>
    <ul>
        <li>You can no longer edit your conversation, but you can review it.</li>
        <li>We'll verify that your account balance has at least <%=researcherSurveyDetailPostlaunch.getInitialcharge()%> which represents 20% of the maximum possible spend, <%=researcherSurveyDetailPostlaunch.getMaxpossiblespend()%>, for your conversation.</li>
        <li>Social people will see your conversation and join in.</li>
            <ul>
                <li>Each time a social person joins the conversation your account balance will be debited according to the incentive that you've set.</li>
                <li>We will close your conversation either when the end date is reached or when the maximum number of respondents has been reached.</li>
                <li>Social people will post the conversation to their peers.</li>
            </ul>
        <li>Every few hours (generally 24, but sometimes 48) we will count all of the blog impressions and debit your account for them.</li>
        <li>If your account balance dips below 5% of the maximum value of all of your currently-active conversations at any given time then we will debit your account balance to get it back to the 20% mark.  For the case of conversations nearing their end we will only incorporate the final amount remaining in the calculation.  If your balance remains below the 5% we will shut down all conversations, placing them into a Waiting for Funds status wherein bloggers can not find them.</li>
        <li>You can watch blogger answers and impressions in real-time by going to the Results page for this conversation.  You get to that page by going to My Conversations and then clicking Results.</li>

    </ul>

    <font class="smallfont">We appreciate your business!  If you have any questions, you can use the <a href="/account/inbox.jsp">Help/Support Ticket</a> system.</font>

    <br/><br/>
    <font class="smallfont">Invite people to join your conversation:</font>
    <br/>
    <a href="/researcher/emailinvite.jsp?surveyid=<%=Pagez.getUserSession().getCurrentSurveyid()%>"><font class="normalfont">Invite People to Join this Conversation</font></a>

    <br/><br/>
    <font class="smallfont">Promote your conversation:</font>
    <br/>
    <font class="smallfont"><%=researcherSurveyDetailPostlaunch.getSocialbookmarklinks()%></font>



    <br/><br/>
    <font class="smallfont">Or return to your list of conversations:</font>
    <br/>
    <a href="/researcher/index.jsp"><font class="mediumfont" style="color: #0000ff;">Return to List of Conversations</font></a>

    <br/><br/>




<%@ include file="/template/footer.jsp" %>