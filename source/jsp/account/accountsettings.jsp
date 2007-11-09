<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Settings";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:messages styleClass="RED" id="all"/>
            <table cellpadding="0" cellspacing="0" border="0">


                <td valign="top">
                    <h:outputText value="First Name" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getFirstname()%>" id="firstname" required="true">
                        <f:validateLength minimum="1" maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="firstname" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Last Name" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getLastname()%>" id="lastname" required="true">
                        <f:validateLength minimum="1" maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="lastname" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="This is used to log in." styleClass="tinyfont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getEmail()%>" id="email" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                        <t:validateEmail></t:validateEmail>
                    </h:inputText>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="Changing email will require re-activation." styleClass="tinyfont"></h:outputText>
                </td>
                <td valign="top">
                    <h:message for="email" styleClass="RED"></h:message>
                </td>



                <td valign="top">
                    <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="Password changes are handled on a separate screen." styleClass="tinyfont"></h:outputText>
                </td>
                <td valign="top">
                    <h:commandLink value="Change Password" action="changepassword"/>
                </td>
                <td valign="top">
                </td>



                <td valign="top">
                    <h:outputText value="Notification of New Surveys" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">Learn about new surveys before others.<br/>Of course we'll only disturb your inbox<br/>if there's actually a new survey<br/>that you qualify for.</font>
                </td>
                <td valign="top">
                    <h:selectOneMenu value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getNotifyofnewsurveysbyemaileveryexdays()%>" id="notifyofnewsurveysbyemaileveryexdays" required="true">
                            <f:selectItems value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getNotificationFrequencies()%>"/>
                    </h:selectOneMenu>
                </td>
                <td valign="top">
                    <h:message for="notifyofnewsurveysbyemaileveryexdays" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Non Critical Email" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">We only send messages that we believe<br/>you'll want to see.  However, you can opt<br/>out of everything but the most critical<br/>account-related and legal messages.</font>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getAllownoncriticalemails()%>" id="allownoncriticalemails"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Allow informative non-critical email</font>
                </td>
                <td valign="top">
                    <h:message for="allownoncriticalemails" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Email Instant Notify" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">If you like we can instantly notify<br/>you of new surveys by email.  No need to wait<br/>for the once a day message!</font>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getInstantnotifybyemailison()%>" id="instantnotifybyemailison"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Instantly notify me via email</font>
                </td>
                <td valign="top">
                    <h:message for="instantnotifybyemailison" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Twitter Instant Notify" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">If you like we can also instantly notify<br/>you of new surveys via Twitter.</font>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getInstantnotifybytwitterison()%>" id="instantnotifybytwitterison"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Instantly notify me via Twitter</font>
                    <br/>
                    <h:inputText value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getInstantnotifytwitterusername()%>" id="instantnotifytwitterusername" required="false"></h:inputText>
                    <br/>
                    <font class="tinyfont">Twitter Username.<br/>Note: this should be something like "joereger"<br/>and not your email address.<br/>Note: You must add info@dneero.com as a friend<br/>in your Twitter account or your notifications<br/>will not arrive.</font>
                </td>
                <td valign="top">
                    <h:message for="instantnotifybytwitterison" styleClass="RED"></h:message>
                    <h:message for="instantnotifytwitterusername" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="XMPP/Jabber Instant Notify" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">If you like we can also instantly notify<br/>you of new surveys via XMPP/Jabber.<br/>This works with a Google Chat account<br/>or other Jabber servers.</font>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getInstantnotifyxmppison()%>" id="instantnotifyxmppison"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Instantly notify me via XMPP/Jabber</font>
                    <br/>
                    <h:inputText value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getInstantnotifyxmppusername()%>" id="instantnotifyxmppusername" required="false"></h:inputText>
                    <br/>
                    <font class="tinyfont">Jabber Address</font>
                </td>
                <td valign="top">
                    <h:message for="instantnotifyxmppison" styleClass="RED"></h:message>
                    <h:message for="instantnotifyxmppusername" styleClass="RED"></h:message>
                </td>


                
                <td valign="top">
                    <h:outputText value="PayPal Address (Optional)" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">We can't pay you until we have a valid<br/>PayPal address.  You can enter one<br/>at any time.  You don't need to have one<br/>in the system to blog and earn money.<br/>Your PayPal address is the email<br/>address you use to log in<br/>to paypal.com.<br/>Don't have a PayPal account? <br/>Setup is quick, easy and secure.<br/>Click <a href='http://www.paypal.com' target='paypal' class="tinyfont"><font class="tinyfont">here</font></a> to create one.</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getPaymethodpaypaladdress()%>" id="paymethodpaypaladdress" size="35"></h:inputText>
                </td>
                <td valign="top">
                    <h:message for="paymethodpaypaladdress" styleClass="RED"></h:message>
                </td>




                <td valign="top">
                </td>
                <td valign="top">
                    <br/><br/>
                    <h:commandButton action="<%=((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getSaveAction()%>" value="Save Settings" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>




<%@ include file="/jsp/templates/footer.jsp" %>


