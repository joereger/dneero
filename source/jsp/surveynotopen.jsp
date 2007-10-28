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
    <ui:define name="title">Survey Not Available<br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="false"/>


<h:form>


    <center>
        <div style="width: 50%;">
            <center>
                <img src="/images/info-128.png" alt="" width="128" height="128"/>
                <br/>
                <font class="mediumfont">The survey you've selected isn't available.  It may not have launched yet.  Or it may be closed.</font>
                <br/><br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center;">
                    <h:commandLink value="Find Surveys to Take" action="#{publicSurveyList.beginView}" style="padding-left: 15px; color: #0000ff;" styleClass="mediumfont"/>
                </div>
            </center>
        </div>
    </center>






</h:form>

</ui:define>


</ui:composition>
</html>


