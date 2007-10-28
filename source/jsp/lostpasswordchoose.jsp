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
    <ui:define name="title">Reset Password<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>
         <h:form id="lostpasswordchooseform">
            <h:panelGrid columns="3" cellpadding="3" border="0">

                <h:panelGroup>
                    <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputSecret value="#{lostPasswordChoose.password}" id="password" required="false"></h:inputSecret>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="password" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Verify Password" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputSecret value="#{lostPasswordChoose.passwordverify}" id="passwordverify" required="false"></h:inputSecret>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="passwordverify" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Prove You're a Human" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <div style="border: 1px solid #ccc; padding: 3px;">
                    <h:inputText value="#{lostPasswordChoose.j_captcha_response}" id="j_captcha_response" required="false"/>
                    <br/>
                    <font class="tinyfont">(type the squiggly letters that appear below)</font>
                    <br/>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td><img src="/images/clear.gif" alt="" width="1" height="100"></img></td>
                            <td style="background: url(/images/loading-captcha.gif);">
                                <img src="/images/clear.gif" alt="" width="200" height="1"></img><br/>
                                <h:graphicImage url="/jcaptcha"></h:graphicImage>
                            </td>
                        </tr>
                    </table>
                    </div>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="j_captcha_response" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{lostPasswordChoose.choosePassword}" value="Reset Password" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>

        </h:form>
    </ui:define>
</ui:composition>
</html>