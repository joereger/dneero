<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support: New Note Submitted";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:outputText>Success!  Your comment has been added.  We will respond soon!</h:outputText>
        <f:verbatim><br/><br/></f:verbatim>
        <h:commandLink action="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getBeginView()%>">
            <h:outputText value="Continue" escape="false" />
            <f:param name="supportissueid" value="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getSupportissueid()%>" />
        </h:commandLink>

<%@ include file="/jsp/templates/footer.jsp" %>

