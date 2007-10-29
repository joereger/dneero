<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "One-Time Researcher Configuration Complete!";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:form>
            <font class="smallfont">Your researcher profile is created and ready to roll.  In the future you'll be able to skip this step.</font>
            <br/><br/>
            <h:commandLink value="Click Here to Continue" action="#{researcherIndex.beginView}" style="color: #0000ff;" styleClass="mediumfont"/>
        </h:form>
    </ui:define>


</ui:composition>
</html>