<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SystemProps... Be Careful!!!";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>




            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value="Base Url" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">The base url that this instance is installed at.  No http:// and no trailing slash!  Ex: www.dneero.com</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getBaseurl()%>" id="baseurl" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="baseurl" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="SendXMPP" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1.  Whether or not to send XMPP notifications from this installation.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getSendxmpp()%>" id="sendxmpp" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="sendxmpp" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="SmtpOutboundServer" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">Smtp server name or ip address to use to send email.  Default is 'localhost'</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getSmtpoutboundserver()%>" id="smtpoutboundserver" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="smtpoutboundserver" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="IsEverythingPasswordProtected" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1. Whether or not a password is required to see anything.  Used for beta capability.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getIseverythingpasswordprotected()%>" id="iseverythingpasswordprotected" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="iseverythingpasswordprotected" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="IsBeta" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1.  Whether the system is operating in BETA mode or not.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getIsbeta()%>" id="isbeta" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="isbeta" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="PayPalApiUsername" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal API username.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getPaypalapiusername()%>" id="paypalapiusername" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="paypalapiusername" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="PayPalApiPassword" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal API password.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getPaypalapipassword()%>" id="paypalapipassword" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="paypalapipassword" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="PayPalSignature" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal signature.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getPaypalsignature()%>" id="paypalsignature" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="paypalsignature" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="PayPalEnvironment" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal environment.  I.e. 'sandbox' or 'live'</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getPaypalenvironment()%>" id="paypalenvironment" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="paypalenvironment" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="IsSSLOn" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1.  Whether SSL is installed at the server level for this install.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getIssslon()%>" id="issslon" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="issslon" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Facebook App Name" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">joestest or dneero</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getFacebook_app_name()%>" id="facebook_app_name" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="facebook_app_name" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Facebook API Key" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">joestest is dece0e9c9bc48fa1078cbc5a0680cea3</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getFacebook_api_key()%>" id="facebook_api_key" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="facebook_api_key" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Facebook API Secret" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">joestest is fde4c4950c909948fe3ada5676a19d2a</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getFacebook_api_secret()%>" id="facebook_api_secret" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="facebook_api_secret" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                </td>
                <td valign="top">
                    <br/><br/>
                    <h:commandButton action="<%=((SystemProps)Pagez.getBeanMgr().get("SystemProps")).getSaveProps()%>" value="Save System Props" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>



<%@ include file="/jsp/templates/footer.jsp" %>