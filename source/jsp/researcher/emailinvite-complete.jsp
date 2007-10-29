<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Please Confirm Email Invite";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <font class="formfieldnamefont">Survey to invite people to: #{researcherEmailinviteComplete.survey.title}</font>

        <br/><br/>
        <font class="formfieldnamefont">Valid email addresses: #{researcherEmailinviteComplete.numberofrecipients}</font>

        <br/><br/>
        <font class="formfieldnamefont">List of email addresses: (read-only)</font>
        <br/>
        <f:verbatim>#{researcherEmailinviteComplete.emailaddresslisthtml}</f:verbatim>

        <br/><br/>
        <h:commandButton action="#{researcherEmailinviteComplete.complete}" value="Send Invites" styleClass="formsubmitbutton"></h:commandButton>




<%@ include file="/jsp/templates/footer.jsp" %>

