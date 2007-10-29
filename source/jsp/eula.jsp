<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Terms of Use and Privacy Statement";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

            <center>
            <h:inputTextarea value="#{publicEula.eula}" id="eula" cols="80" rows="15" required="true"></h:inputTextarea>
            </center>
        </h:form>
    </ui:define>
</ui:composition>
</html>
