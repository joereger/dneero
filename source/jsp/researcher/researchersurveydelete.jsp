<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Delete Survey";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:messages styleClass="RED"/>
    <t:saveState id="save" value="#{researcherSurveyDelete}"/>
    <font class="mediumfont">Are you sure you want to delete the survey<br/>"${researcherSurveyDelete.title}"?</font>
    <br/><br/>
    <h:commandButton action="#{researcherSurveyDelete.deleteSurvey}" value="Yes, Delete this Survey" styleClass="formsubmitbutton"/>
    <br/><br/>
    <h:commandLink value="Nevermind, Don't Delete this Survey" action="#{researcherIndex.beginView}" styleClass="subnavfont"/>

</h:form>

</ui:define>


</ui:composition>
</html>