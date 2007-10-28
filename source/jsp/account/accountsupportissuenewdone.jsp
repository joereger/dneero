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
    <ui:define name="title">Support: New Issue Successfully Created<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>


    <h:form>

        <h:outputText>Success!  We will respond soon!</h:outputText>
        <f:verbatim><br/><br/></f:verbatim>
        <h:commandLink action="#{accountSupportIssuesList.beginView}" value="Support Issues List"></h:commandLink>

    </h:form>

</ui:define>


</ui:composition>
</html>

