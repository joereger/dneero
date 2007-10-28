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
    <ui:define name="title">Survey Results<br/><br/></ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form>

    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont">#{researcherResultsAnswers.survey.title}</font>
        <br/>
        <h:commandLink value="Results Main" action="#{researcherResults.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Response Report" action="#{researcherResultsAnswers.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Impressions" action="#{researcherResultsImpressions.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Respondents" action="#{researcherResultsRespondents.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Financial Status" action="#{researcherResultsFinancial.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
    </div>
    <br/><br/>

    <f:verbatim>#{researcherResultsAnswers.results}</f:verbatim>

    <h:commandButton action="#{researcherResultsAnswersCsv.getCsv}" value="Download as CSV" styleClass="formsubmitbutton"></h:commandButton>

    
</h:form>

</ui:define>


</ui:composition>
</html>