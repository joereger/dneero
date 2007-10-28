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
    <ui:define name="title">
        <img src="/images/process-train-survey-01.gif" align="right" width="350" height="73"></img>
        <h:outputText value="New Survey" styleClass="pagetitlefont" rendered="${researcherSurveyDetail01.title eq ''}"/>
        <h:outputText value="${researcherSurveyDetail01.title}" styleClass="pagetitlefont" rendered="${researcherSurveyDetail01.title ne ''}"/>
        <br clear="all"/>
    </ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form id="surveyedit">
    <h:inputHidden name="surveyid" value="#{userSession.currentSurveyid}"/>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    On this page you set the very general parameters for the survey.  Choose a title that'll get people interested.  Give enough information in the description for them to understand what you're trying to understand.  Your start date must be today or in the future.  You want to keep the survey open long enough to attract bloggers and have them fill out the survey... a month is a good starting point.
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>

    <h:panelGrid columns="3" cellpadding="3" border="0">

        <h:panelGroup>
            <h:outputText value="Survey Title" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail01.title}" size="50" id="title" required="true" rendered="#{researcherSurveyDetail01.status le 1}">
                <f:validateLength minimum="3" maximum="200"></f:validateLength>
            </h:inputText>
            <h:outputText value="#{researcherSurveyDetail01.title}" rendered="#{researcherSurveyDetail01.status ge 2}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="title" styleClass="RED"></h:message>
        </h:panelGroup>



        <h:panelGroup>
            <h:outputText value="Description" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputTextarea value="#{researcherSurveyDetail01.description}" id="description" cols="45" required="true" rendered="#{researcherSurveyDetail01.status le 1}">
                <f:validateLength minimum="3" maximum="50000"></f:validateLength>
            </h:inputTextarea>
            <h:outputText value="#{researcherSurveyDetail01.description}" rendered="#{researcherSurveyDetail01.status ge 2}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="description" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Start Date" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <t:inputDate value="#{researcherSurveyDetail01.startdate}"  type="both" popupCalendar="true" id="startdate" required="true" rendered="#{researcherSurveyDetail01.status le 1}"></t:inputDate>
            <h:outputText value="#{researcherSurveyDetail01.startdate}" rendered="#{researcherSurveyDetail01.status ge 2}"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
            <br/>
            <font class="tinyfont">Click the ... button for a pop-up calendar.</font>
            <br/>
            <font class="tinyfont">(format is: dd month yyyy hour minute)</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="startdate" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="End Date" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <t:inputDate value="#{researcherSurveyDetail01.enddate}" type="both" popupCalendar="true" id="enddate" required="true" rendered="#{researcherSurveyDetail01.status le 1}"></t:inputDate>
            <h:outputText value="#{researcherSurveyDetail01.enddate}" rendered="#{researcherSurveyDetail01.status ge 2}"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
            <br/>
            <font class="tinyfont">Click the ... button for a pop-up calendar.</font>
            <br/>
            <font class="tinyfont">(format is: dd month yyyy hour minute)</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="enddate" styleClass="RED"></h:message>
        </h:panelGroup>

    </h:panelGrid>

    <div class="surveyeditbuttonbox"><div class="surveyeditpreviousbutton"></div><div class="surveyeditnextbutton"><h:commandButton action="#{researcherSurveyDetail01.saveSurvey}" value="Next Step" styleClass="formsubmitbutton"></h:commandButton></div></div>

</h:form>

</ui:define>


</ui:composition>
</html>