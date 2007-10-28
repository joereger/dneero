<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"
      xmlns:c="http://java.sun.com/jstl/core"

      >
<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Social Surveys<br/><br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>

    <!--<script type="text/javascript">
    this.location.href="#{publicFacebookLandingPage.urltoredirectto}";
    </script>-->
    <a href="#{publicFacebookLandingPage.urltoredirectto}">Click here please.</a>
    <br/>
    <br/>
    userSession.isfacebookui=#{userSession.isfacebookui}


</ui:define>
</ui:composition>



</html>