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
    <ui:define name="title">Please Confirm Email Send</ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>

    <h:form id="emailinvitecomplete">

        <font class="formfieldnamefont">Survey to invite people to: #{researcherEmailinviteComplete.survey.title}</font>

        <br/><br/>
        <font class="formfieldnamefont">Valid email addresses: #{researcherEmailinviteComplete.numberofrecipients}</font>

        <br/><br/>
        <font class="formfieldnamefont">List of email addresses: (read-only)</font>
        <br/>
        <f:verbatim>#{researcherEmailinviteComplete.emailaddresslisthtml}</f:verbatim>

        <br/><br/>
        <h:commandButton action="#{researcherEmailinviteComplete.complete}" value="Send Invites" styleClass="formsubmitbutton"></h:commandButton>



    </h:form>

</ui:define>


</ui:composition>
</html>


