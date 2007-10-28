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
    <ui:define name="title">Password Required to Send<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
    <d:authorization acl="systemadmin" redirectonfail="true"/>



<h:form>
    <h:messages styleClass="RED"/>
    <t:saveState id="save" value="#{sysadminMassemailSend}"/>
    <font class="largefont">
    Are you sure you want to do this crazy thing?  Have you spell-checked everything?  Are you 100% certain that you didn't screw something up?  We're talking about a lot of outbound email here.    
    </font>
    <br/><br/>
    <font class="formfieldnamefont">Please enter the send password:</font>   
    <br/>
    <h:inputSecret value="#{sysadminMassemailSend.password}" id="password" required="true"></h:inputSecret>
    <br/>
    <h:commandButton action="#{sysadminMassemailSend.send}" value="Send this Mass Email" styleClass="formsubmitbutton"></h:commandButton>
    


</h:form>

</ui:define>


</ui:composition>
</html>



