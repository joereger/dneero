<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Note Submitted";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:outputText>Success!  Your comment has been added.</h:outputText>
        <f:verbatim><br/><br/></f:verbatim>
        <h:commandLink action="#{sysadminSupportIssueDetail.beginView}">
            <h:outputText value="Continue" escape="false" />
            <f:param name="supportissueid" value="#{sysadminSupportIssueDetail.supportissueid}" />
        </h:commandLink>



<%@ include file="/jsp/templates/footer.jsp" %>


