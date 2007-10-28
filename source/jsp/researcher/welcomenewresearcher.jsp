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
    <ui:define name="title">One-Time Researcher Configuration Complete!</ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
        <d:authorization acl="researcher" redirectonfail="true"/>
        <h:form>
            <font class="smallfont">Your researcher profile is created and ready to roll.  In the future you'll be able to skip this step.</font>
            <br/><br/>
            <h:commandLink value="Click Here to Continue" action="#{researcherIndex.beginView}" style="color: #0000ff;" styleClass="mediumfont"/>
        </h:form>
    </ui:define>


</ui:composition>
</html>