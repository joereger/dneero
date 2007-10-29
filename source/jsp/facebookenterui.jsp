<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Enter Facebook App";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

            <a href="#{publicFacebookenterui.url}">Please click here to continue.</a>
        </h:form>
    </ui:define>
</ui:composition>
</html>
