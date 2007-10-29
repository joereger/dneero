<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Welcome to dNeero!";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:outputText styleClass="largefont" value="#{inviteLandingPage.referredby} has invited you to make money with your blog!" rendered="#{!empty inviteLandingPage.referredby}"></h:outputText>
            <h:outputText styleClass="largefont" value="You've been invited to make money with your blog!" rendered="#{empty inviteLandingPage.referredby}"></h:outputText>
            <br/>
            <h:commandLink action="#{registration.beginView}" value="Click here to Sign Up!" styleClass="mediumfont"></h:commandLink>
        </h:form>




<%@ include file="/jsp/templates/footer.jsp" %>