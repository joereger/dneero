<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SystemProps... Be Careful!!!";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>




            <h:messages styleClass="RED"/>
            <h:panelGrid columns="3" cellpadding="3" border="0">

                <h:panelGroup>
                    <h:outputText value="Base Url" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">The base url that this instance is installed at.  No http:// and no trailing slash!  Ex: www.dneero.com</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.baseurl}" id="baseurl" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="baseurl" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="SendXMPP" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1.  Whether or not to send XMPP notifications from this installation.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.sendxmpp}" id="sendxmpp" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="sendxmpp" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="SmtpOutboundServer" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">Smtp server name or ip address to use to send email.  Default is 'localhost'</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.smtpoutboundserver}" id="smtpoutboundserver" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="smtpoutboundserver" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="IsEverythingPasswordProtected" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1. Whether or not a password is required to see anything.  Used for beta capability.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.iseverythingpasswordprotected}" id="iseverythingpasswordprotected" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="iseverythingpasswordprotected" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="IsBeta" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1.  Whether the system is operating in BETA mode or not.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.isbeta}" id="isbeta" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="isbeta" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="PayPalApiUsername" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal API username.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.paypalapiusername}" id="paypalapiusername" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="paypalapiusername" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="PayPalApiPassword" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal API password.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.paypalapipassword}" id="paypalapipassword" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="paypalapipassword" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="PayPalSignature" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal signature.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.paypalsignature}" id="paypalsignature" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="paypalsignature" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="PayPalEnvironment" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">PayPal environment.  I.e. 'sandbox' or 'live'</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.paypalenvironment}" id="paypalenvironment" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="paypalenvironment" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="IsSSLOn" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1.  Whether SSL is installed at the server level for this install.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.issslon}" id="issslon" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="issslon" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Facebook App Name" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">joestest or dneero</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.facebook_app_name}" id="facebook_app_name" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="facebook_app_name" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Facebook API Key" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">joestest is dece0e9c9bc48fa1078cbc5a0680cea3</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.facebook_api_key}" id="facebook_api_key" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="facebook_api_key" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Facebook API Secret" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">joestest is fde4c4950c909948fe3ada5676a19d2a</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{systemProps.facebook_api_secret}" id="facebook_api_secret" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="facebook_api_secret" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <br/><br/>
                    <h:commandButton action="#{systemProps.saveProps}" value="Save System Props" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>



<%@ include file="/jsp/templates/footer.jsp" %>