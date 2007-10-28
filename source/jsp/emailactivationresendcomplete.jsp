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
    <ui:define name="title">Email Activation Message Sent<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>
        <h:form>
            <h:outputText>Your email activation message has been sent.  Please check your email inbox.</h:outputText>
            <f:verbatim><br/><br/></f:verbatim>
            <h:outputText>Also, please note that all previous activation emails are now invalid... you must use the most recent one that we've sent.  This is for your security.  Thanks!</h:outputText>
        </h:form>
    </ui:define>
</ui:composition>
</html>
