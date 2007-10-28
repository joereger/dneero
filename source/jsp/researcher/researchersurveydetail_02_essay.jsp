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
        <img src="/images/process-train-survey-02.gif" align="right" width="350" height="73"></img>
        <h:outputText value="${researcherSurveyDetail02essay.title}" styleClass="pagetitlefont" rendered="${researcherSurveyDetail02essay.title ne ''}"/>
        <br clear="all"/>
    </ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form>

    <h:inputHidden name="surveyid" value="#{userSession.currentSurveyid}" />
    <h:inputHidden name="questionid" value="#{researcherSurveyDetail02essay.questionid}" />

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Type a question in the Question box.  Because this is an Essay question type, respondents will be able to type a free-text response to this question.
    <br/><br/>
    Here's what an Essay question looks like:
    <center><img src="../images/questiontypes/essay.gif" border="0"></img></center>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>



    <!-- Start Bottom -->

    <h:panelGrid columns="3" cellpadding="3" border="0">

        <h:panelGroup>
            <h:outputText value="Question Type"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="Essay"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>




        <h:panelGroup>
            <h:outputText value="Question" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail02essay.question}" id="question" required="true">
                <f:validateLength minimum="3" maximum="254"></f:validateLength>
            </h:inputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="question" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Is Required?" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectBooleanCheckbox value="#{researcherSurveyDetail02essay.isrequired}" id="isrequired" required="true"></h:selectBooleanCheckbox>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="isrequired" styleClass="RED"></h:message>
        </h:panelGroup>




        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
            <h:commandButton action="#{researcherSurveyDetail02essay.saveQuestion}" value="Save Question and Continue" styleClass="formsubmitbutton"></h:commandButton>
            <br/><br/><h:commandLink value="Nevermind, Take me Back" styleClass="tinyfont" action="#{researcherSurveyDetail02.beginView}" immediate="true"/>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

    </h:panelGrid>

    <!-- End Bottom -->

</h:form>

</ui:define>


</ui:composition>
</html>