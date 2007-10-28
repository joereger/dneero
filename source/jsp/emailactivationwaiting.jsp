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
    <ui:define name="title">Account Awaiting Email Activation<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>
        <h:form>
            <h:outputText>Your account has not yet been activated by email.  It must be activated by email within 3 days after signup.  Please check your email inbox and click the link that we sent you to begin the activation process. (Also check your Junk Mail and/or Spam folders... sometimes email clients put the message there.)</h:outputText>
            <f:verbatim><br/><br/></f:verbatim>
            <h:commandLink action="#{emailActivationResend.beginView}" value="Click here to re-send your email activation message."></h:commandLink>
        </h:form>
    </ui:define>
</ui:composition>
</html>