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
    <ui:define name="title">Email Activation Successful<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>
        <h:form>
            <h:outputText>Email activation was successful!  Your account is ready to roll!  You can now log in with the email address and password that you provided when you signed up.</h:outputText>
            <br/><br/>
            <h:commandButton action="#{login.beginView}" value="Please Log In" styleClass="formsubmitbutton" rendered="#{!userSession.isloggedin}"/>
        </h:form>
    </ui:define>
</ui:composition>
</html>