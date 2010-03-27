<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetailPostlaunch" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<br clear=\"all\"/>";
if (((ResearcherTwitaskDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherTwitaskDetailPostlaunch"))!=null && !((ResearcherTwitaskDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherTwitaskDetailPostlaunch")).getTitle().equals("")){
    pagetitle = "" +
"        <font class=\"pagetitlefont\">"+((ResearcherTwitaskDetailPostlaunch) Pagez.getBeanMgr().get("ResearcherTwitaskDetailPostlaunch")).getTitle()+"</font>\n" +
"        <br clear=\"all\"/>";
}

String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetailPostlaunch researcherTwitaskDetailPostlaunch= (ResearcherTwitaskDetailPostlaunch)Pagez.getBeanMgr().get("ResearcherTwitaskDetailPostlaunch");
%>
<%
if (researcherTwitaskDetailPostlaunch.getTwitask().getStatus()==Twitask.STATUS_OPEN){
    Pagez.getUserSession().setMessage("Your Twitter Question has been posted to your Twitter account!  We'll collect answers from your followers for 3 days and put them on this page... tell them to answer by replying!");
    Pagez.sendRedirect("/twitask.jsp?twitaskid="+researcherTwitaskDetailPostlaunch.getTwitask().getTwitaskid()+"");
    return;
}
%>
<%@ include file="/template/header.jsp" %>


    <font class="pagetitlefont" style="color: #333333;">Congratulations!  Your Twitter Question has been created!</font>
    <br/><br/>
    <font class="mediumfont">Here's what to expect next:</font>
    <br/><br/>
    <ul>
        <li>You can no longer edit your Twitter Question, but you can review it.</li>
        <li>A System Administrator must approve your Twitter Question before it goes live.</li>
        <li>We'll verify that your account balance has at least <%=researcherTwitaskDetailPostlaunch.getInitialcharge()%> which represents 20% of the maximum possible spend, <%=researcherTwitaskDetailPostlaunch.getMaxpossiblespend()%>, for your Twitter Questions.</li>
        <li>Social people will see your Twitter Question in Twitter and join in.</li>
            <ul>
                <li>Each time a social person answers your account balance will be debited according to the incentive that you've set.</li>
                <li>We will close your conversation either when the end date is reached or when the maximum number of respondents has been reached.</li>
                <li>Social people will post the conversation to their peers.</li>
            </ul>
        <li>If your account balance dips below 5% of the maximum value of all of your currently-active Twitter Questions at any given time then we will debit your account balance to get it back to the 20% mark.  For the case of conversations nearing their end we will only incorporate the final amount remaining in the calculation.  If your balance remains below the 5% we will shut down all conversations, placing them into a Waiting for Funds status wherein people can not respond to them.</li>

    </ul>

    <font class="smallfont">We appreciate your business!  If you have any questions, you can use the <a href="/account/inbox.jsp">Help/Support Ticket</a> system.</font>




    <br/><br/>




<%@ include file="/template/footer.jsp" %>