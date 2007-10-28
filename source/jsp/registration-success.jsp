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
    <ui:define name="title">Success!<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>

        <f:view>
            <h:outputText value="Hello, #{registration.firstname}! Registration was successful!" styleClass="mediumfont"/>
            <br/>
            <br/>
            <font class="smallfont">An email message has been sent to #{registration.email}.  You must click the link in it to activate your account within 3 days.</font>
        </f:view>

    </ui:define>

</ui:composition>
</html>
