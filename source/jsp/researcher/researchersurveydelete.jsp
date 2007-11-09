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
    <font class="mediumfont">Are you sure you want to delete the survey<br/>"<%=((ResearcherSurveyDelete)Pagez.getBeanMgr().get("ResearcherSurveyDelete")).getTitle()%>"?</font>
    <br/><br/>
    <h:commandButton action="<%=((ResearcherSurveyDelete)Pagez.getBeanMgr().get("ResearcherSurveyDelete")).getDeleteSurvey()%>" value="Yes, Delete this Survey" styleClass="formsubmitbutton"/>
    <br/><br/>
    <h:commandLink value="Nevermind, Don't Delete this Survey" action="<%=((ResearcherIndex)Pagez.getBeanMgr().get("ResearcherIndex")).getBeginView()%>" styleClass="subnavfont"/>



<%@ include file="/jsp/templates/footer.jsp" %>