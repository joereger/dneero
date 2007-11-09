<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Please Confirm Email Invite";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <font class="formfieldnamefont">Survey to invite people to: <%=((ResearcherEmailinviteComplete)Pagez.getBeanMgr().get("ResearcherEmailinviteComplete")).getSurvey().getTitle()%></font>

        <br/><br/>
        <font class="formfieldnamefont">Valid email addresses: <%=((ResearcherEmailinviteComplete)Pagez.getBeanMgr().get("ResearcherEmailinviteComplete")).getNumberofrecipients()%></font>

        <br/><br/>
        <font class="formfieldnamefont">List of email addresses: (read-only)</font>
        <br/>
        <f:verbatim><%=((ResearcherEmailinviteComplete)Pagez.getBeanMgr().get("ResearcherEmailinviteComplete")).getEmailaddresslisthtml()%></f:verbatim>

        <br/><br/>
        <h:commandButton action="<%=((ResearcherEmailinviteComplete)Pagez.getBeanMgr().get("ResearcherEmailinviteComplete")).getComplete()%>" value="Send Invites" styleClass="formsubmitbutton"></h:commandButton>




<%@ include file="/jsp/templates/footer.jsp" %>

