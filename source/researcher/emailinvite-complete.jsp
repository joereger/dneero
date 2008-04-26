<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherEmailinviteComplete" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Please Confirm Email Invite";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherEmailinviteComplete researcherEmailinviteComplete = (ResearcherEmailinviteComplete)Pagez.getBeanMgr().get("ResearcherEmailinviteComplete");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("invite")) {
        try {
            researcherEmailinviteComplete.complete();
            Pagez.getUserSession().setMessage("Invitations sent!");
            Pagez.sendRedirect("/researcher/emailinvite-sent.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


        <form action="/researcher/emailinvite-complete.jsp" method="post">
            <input type="hidden" name="dpage" value="/researcher/emailinvite-complete.jsp">
            <input type="hidden" name="action" value="invite">
            <font class="formfieldnamefont">Conversation to invite people to: <%=researcherEmailinviteComplete.getSurvey().getTitle()%></font>

            <br/><br/>
            <font class="formfieldnamefont">Valid email addresses: <%=researcherEmailinviteComplete.getNumberofrecipients()%></font>

            <br/><br/>
            <font class="formfieldnamefont">List of email addresses: (read-only)</font>
            <br/>
            <%=researcherEmailinviteComplete.getEmailaddresslisthtml()%>

            <br/><br/>
            <input type="submit" class="formsubmitbutton" value="Send Invites">
        </form>


<%@ include file="/template/footer.jsp" %>

