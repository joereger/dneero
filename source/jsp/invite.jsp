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
    <ui:define name="title">Welcome!<br/><br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>


        <h:form>
            <h:outputText styleClass="largefont" value="#{inviteLandingPage.referredby} has invited you to make money with your blog!" rendered="#{!empty inviteLandingPage.referredby}"></h:outputText>
            <h:outputText styleClass="largefont" value="You've been invited to make money with your blog!" rendered="#{empty inviteLandingPage.referredby}"></h:outputText>
            <br/>
            <h:commandLink action="#{registration.beginView}" value="Click here to Sign Up!" styleClass="mediumfont"></h:commandLink>
        </h:form>


</ui:define>


</ui:composition>
</html>