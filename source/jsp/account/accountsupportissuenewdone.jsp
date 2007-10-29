<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support: New Issue Created";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:outputText>Success!  We will respond soon!</h:outputText>
        <f:verbatim><br/><br/></f:verbatim>
        <h:commandLink action="#{accountSupportIssuesList.beginView}" value="Support Issues List"></h:commandLink>

<%@ include file="/jsp/templates/footer.jsp" %>

