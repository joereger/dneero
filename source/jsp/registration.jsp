<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Sign Up for a dNeero Account";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


        <t:div rendered="#{registration.displaytempresponsesavedmessage}">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #00ff00;">
                <table cellpadding="5">
                    <tr>
                        <td valign="top">
                            <img src="/images/redo-64.png" width="64" height="64" alt="" align="left"/>
                        </td>
                        <td valign="top">
                            <font class="mediumfont">Almost done... we've temporarily saved your survey response.</font><br/>
                            <font class="smallfont">We can't pay you until you log in or sign up and qualify by being in the correct demographic for this survey.  If you do neither of these, we won't be able to use your survey response.  If you close your browser the survey response will be discarded too.  Sign up is quick, painless and free.</font><br/>
                            <font class="smallfont"><b>Please log in or sign up below.</b></font>
                        </td>
                    </tr>
                </table>
            </div>
            <br/>
        </t:div>

        <div style="width: 250px; float: right; padding-left: 20px;">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                <font class="mediumfont" style="color: #333333">Existing Users</font><br/>
                <font class="smallfont">If you've already got a dNeero account you can simply log in.</font><br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                    <d:greenRoundedButton pathtoapproot="../"><h:commandLink value="Log In" action="#{login.beginView}" styleClass="subnavfont" style="color: #ffffff; font-weight: bold;"/></d:greenRoundedButton>
                </div>
            </div>
        </div>
        </h:form>
        <h:form id="registrationForm">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                <font class="mediumfont" style="color: #333333">Get started creating and/or taking surveys</font>
                <br/>
                <font class="smallfont">Sign Up is free.  On this page we collect some basic information.  After this you'll start working immediately to create and/or take surveys (all accounts can do both functions).  You have three days (during which you can use your account) to activate by clicking a link that we send to your email address.  Your account is completely free to set up and explore.</font><br/><br/>

                <h:panelGrid columns="3" cellpadding="3" border="0">

                    <h:panelGroup>
                        <h:outputText value="First Name" styleClass="formfieldnamefont"></h:outputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:inputText value="#{registration.firstname}" id="firstname" required="false" size="35" maxlength="200"></h:inputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:message for="firstname" styleClass="RED"></h:message>
                    </h:panelGroup>

                    <h:panelGroup>
                        <h:outputText value="Last Name" styleClass="formfieldnamefont"></h:outputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:inputText value="#{registration.lastname}" id="lastname" required="false" size="35" maxlength="200">
                        </h:inputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:message for="lastname" styleClass="RED"></h:message>
                    </h:panelGroup>


                    <h:panelGroup>
                        <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:inputText value="#{registration.email}" id="email" required="false" size="35" maxlength="200"></h:inputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:message for="email" styleClass="RED"></h:message>
                    </h:panelGroup>


                    <h:panelGroup>
                        <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:inputSecret value="#{registration.password}" id="password" required="false" size="35" maxlength="200"></h:inputSecret>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:message for="password" styleClass="RED"></h:message>
                    </h:panelGroup>


                    <h:panelGroup>
                        <h:outputText value="Verify Password" styleClass="formfieldnamefont"></h:outputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:inputSecret value="#{registration.passwordverify}" id="passwordverify" required="false" size="35" maxlength="200"></h:inputSecret>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:message for="passwordverify" styleClass="RED"></h:message>
                    </h:panelGroup>



                    <h:panelGroup>
                        <h:outputText value="Prove You're a Human" styleClass="formfieldnamefont"></h:outputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <div style="border: 1px solid #ccc; padding: 3px;">
                        <h:inputText value="#{registration.j_captcha_response}" id="j_captcha_response" required="false" size="35" maxlength="200"/>
                        <br/>
                        <font class="tinyfont">(type the squiggly letters that appear below)</font>
                        <br/>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td><img src="/images/clear.gif" alt="" width="1" height="100"></img></td>
                                <td style="background: url(/images/loading-captcha.gif);">
                                    <img src="/images/clear.gif" alt="" width="200" height="1"></img><br/>
                                    <h:graphicImage url="/jcaptcha" width="200" height="100"></h:graphicImage>
                                </td>
                            </tr>
                        </table>
                        </div>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:message for="j_captcha_response" styleClass="RED"></h:message>
                    </h:panelGroup>


                    <h:panelGroup>
                        <h:outputText value="End User License Agreement" styleClass="formfieldnamefont"></h:outputText>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:inputTextarea value="#{registration.eula}" id="eula" cols="45" rows="5" required="false">
                        </h:inputTextarea>
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:message for="eula" styleClass="RED"></h:message>
                    </h:panelGroup>



                    <h:panelGroup>
                    </h:panelGroup>
                    <h:panelGroup>
                        <br/><br/>
                        <h:commandButton action="#{registration.registerAction}" value="Sign Up" styleClass="formsubmitbutton" target="_top"></h:commandButton>
                    </h:panelGroup>
                    <h:panelGroup>
                    </h:panelGroup>

                </h:panelGrid>


            </div>


<%@ include file="/jsp/templates/footer.jsp" %>