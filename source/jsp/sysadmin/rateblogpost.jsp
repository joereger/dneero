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
    <ui:define name="title"></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
        <d:authorization acl="systemadmin" redirectonfail="true"/>

        <h:outputText value="No posts require review at this time." styleClass="largefont" rendered="#{!rateBlogPost.haveposttoreview}"></h:outputText>

         <h:form rendered="#{rateBlogPost.haveposttoreview}">
            <h:messages styleClass="RED"/>
            <t:saveState id="save" value="#{rateBlogPost}"/>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #00ff00;">
                <center>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td valign="top">
                                <font class="largefont">Bad</font>
                            </td>
                            <td valign="top">
                                <h:selectOneRadio value="#{rateBlogPost.quality}" id="quality" required="true" layout="lineDirection">
                                    <f:selectItems value="#{staticVariables.blogqualities}"/>
                                </h:selectOneRadio>
                            </td>
                            <td valign="top">
                                <font class="largefont">Good</font>
                            </td>
                            <td valign="top">
                                <img src="/images/clear.gif" width="10" height="1"></img>
                            </td>
                            <td valign="top">
                                <h:commandButton action="#{rateBlogPost.rateAction}" value="Rate Post" styleClass="formsubmitbutton"></h:commandButton>
                                <f:verbatim><br/></f:verbatim>
                                <h:outputText value="#{rateBlogPost.remainingtoreview} posts remaining" styleClass="tinyfont"></h:outputText>
                            </td>
                        </tr>
                    </table>
                </center>
            </div>
            <center><font class="tinyfont" style="color: #666666;">#{rateBlogPost.impression.referer}</font></center>

            <f:verbatim><br/></f:verbatim>
            <h:outputText value="#{rateBlogPost.iframestr}" escape="false"></h:outputText>

         </h:form>

    </ui:define>


</ui:composition>
</html>