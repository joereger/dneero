<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Delete Survey<br/><br/></ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form id="surveydelete">
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