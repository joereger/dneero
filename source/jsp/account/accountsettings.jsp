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
    <ui:define name="title">Account Settings<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>


    <h:form id="accountSettingsForm">
            <h:messages styleClass="RED" id="all"/>
            <h:panelGrid columns="3" cellpadding="3" border="0">


                <h:panelGroup>
                    <h:outputText value="First Name" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{accountSettings.firstname}" id="firstname" required="true">
                        <f:validateLength minimum="1" maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="firstname" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Last Name" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{accountSettings.lastname}" id="lastname" required="true">
                        <f:validateLength minimum="1" maximum="255"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="lastname" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="This is used to log in." styleClass="tinyfont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{accountSettings.email}" id="email" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                        <t:validateEmail></t:validateEmail>
                    </h:inputText>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="Changing email will require re-activation." styleClass="tinyfont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="email" styleClass="RED"></h:message>
                </h:panelGroup>



                <h:panelGroup>
                    <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="Password changes are handled on a separate screen." styleClass="tinyfont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandLink value="Change Password" action="changepassword"/>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>



                <h:panelGroup>
                    <h:outputText value="Notification of New Surveys" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">Learn about new surveys before others.<br/>Of course we'll only disturb your inbox<br/>if there's actually a new survey<br/>that you qualify for.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectOneMenu value="#{accountSettings.notifyofnewsurveysbyemaileveryexdays}" id="notifyofnewsurveysbyemaileveryexdays" required="true">
                            <f:selectItems value="#{accountSettings.notificationFrequencies}"/>
                    </h:selectOneMenu>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="notifyofnewsurveysbyemaileveryexdays" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Non Critical Email" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">We only send messages that we believe<br/>you'll want to see.  However, you can opt<br/>out of everything but the most critical<br/>account-related and legal messages.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox value="#{accountSettings.allownoncriticalemails}" id="allownoncriticalemails"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Allow informative non-critical email</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="allownoncriticalemails" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Email Instant Notify" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">If you like we can instantly notify<br/>you of new surveys by email.  No need to wait<br/>for the once a day message!</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox value="#{accountSettings.instantnotifybyemailison}" id="instantnotifybyemailison"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Instantly notify me via email</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="instantnotifybyemailison" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="Twitter Instant Notify" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">If you like we can also instantly notify<br/>you of new surveys via Twitter.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox value="#{accountSettings.instantnotifybytwitterison}" id="instantnotifybytwitterison"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Instantly notify me via Twitter</font>
                    <br/>
                    <h:inputText value="#{accountSettings.instantnotifytwitterusername}" id="instantnotifytwitterusername" required="false"></h:inputText>
                    <br/>
                    <font class="tinyfont">Twitter Username.<br/>Note: this should be something like "joereger"<br/>and not your email address.<br/>Note: You must add info@dneero.com as a friend<br/>in your Twitter account or your notifications<br/>will not arrive.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="instantnotifybytwitterison" styleClass="RED"></h:message>
                    <h:message for="instantnotifytwitterusername" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value="XMPP/Jabber Instant Notify" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">If you like we can also instantly notify<br/>you of new surveys via XMPP/Jabber.<br/>This works with a Google Chat account<br/>or other Jabber servers.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox value="#{accountSettings.instantnotifyxmppison}" id="instantnotifyxmppison"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Instantly notify me via XMPP/Jabber</font>
                    <br/>
                    <h:inputText value="#{accountSettings.instantnotifyxmppusername}" id="instantnotifyxmppusername" required="false"></h:inputText>
                    <br/>
                    <font class="tinyfont">Jabber Address</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="instantnotifyxmppison" styleClass="RED"></h:message>
                    <h:message for="instantnotifyxmppusername" styleClass="RED"></h:message>
                </h:panelGroup>


                
                <h:panelGroup>
                    <h:outputText value="PayPal Address (Optional)" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">We can't pay you until we have a valid<br/>PayPal address.  You can enter one<br/>at any time.  You don't need to have one<br/>in the system to blog and earn money.<br/>Your PayPal address is the email<br/>address you use to log in<br/>to paypal.com.<br/>Don't have a PayPal account? <br/>Setup is quick, easy and secure.<br/>Click <a href='http://www.paypal.com' target='paypal' class="tinyfont"><font class="tinyfont">here</font></a> to create one.</font>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{accountSettings.paymethodpaypaladdress}" id="paymethodpaypaladdress" size="35"></h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="paymethodpaypaladdress" styleClass="RED"></h:message>
                </h:panelGroup>




                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <br/><br/>
                    <h:commandButton action="#{accountSettings.saveAction}" value="Save Settings" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>




        </h:form>

</ui:define>


</ui:composition>
</html>

