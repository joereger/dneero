<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Sign Up for a dNeero Account";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


        <t:div rendered="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getDisplaytempresponsesavedmessage()%>">
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
                    <d:greenRoundedButton pathtoapproot="../"><h:commandLink value="Log In" action="<%=((Login)Pagez.getBeanMgr().get("Login")).getBeginView()%>" styleClass="subnavfont" style="color: #ffffff; font-weight: bold;"/></d:greenRoundedButton>
                </div>
            </div>
        </div>
        </h:form>
        <h:form id="registrationForm">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                <font class="mediumfont" style="color: #333333">Get started creating and/or taking surveys</font>
                <br/>
                <font class="smallfont">Sign Up is free.  On this page we collect some basic information.  After this you'll start working immediately to create and/or take surveys (all accounts can do both functions).  You have three days (during which you can use your account) to activate by clicking a link that we send to your email address.  Your account is completely free to set up and explore.</font><br/><br/>

                <table cellpadding="0" cellspacing="0" border="0">

                    <td valign="top">
                        <h:outputText value="First Name" styleClass="formfieldnamefont"></h:outputText>
                    </td>
                    <td valign="top">
                        <h:inputText value="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getFirstname()%>" id="firstname" required="false" size="35" maxlength="200"></h:inputText>
                    </td>
                    <td valign="top">
                        <h:message for="firstname" styleClass="RED"></h:message>
                    </td>

                    <td valign="top">
                        <h:outputText value="Last Name" styleClass="formfieldnamefont"></h:outputText>
                    </td>
                    <td valign="top">
                        <h:inputText value="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getLastname()%>" id="lastname" required="false" size="35" maxlength="200">
                        </h:inputText>
                    </td>
                    <td valign="top">
                        <h:message for="lastname" styleClass="RED"></h:message>
                    </td>


                    <td valign="top">
                        <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
                    </td>
                    <td valign="top">
                        <h:inputText value="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getEmail()%>" id="email" required="false" size="35" maxlength="200"></h:inputText>
                    </td>
                    <td valign="top">
                        <h:message for="email" styleClass="RED"></h:message>
                    </td>


                    <td valign="top">
                        <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                    </td>
                    <td valign="top">
                        <h:inputSecret value="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getPassword()%>" id="password" required="false" size="35" maxlength="200"></h:inputSecret>
                    </td>
                    <td valign="top">
                        <h:message for="password" styleClass="RED"></h:message>
                    </td>


                    <td valign="top">
                        <h:outputText value="Verify Password" styleClass="formfieldnamefont"></h:outputText>
                    </td>
                    <td valign="top">
                        <h:inputSecret value="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getPasswordverify()%>" id="passwordverify" required="false" size="35" maxlength="200"></h:inputSecret>
                    </td>
                    <td valign="top">
                        <h:message for="passwordverify" styleClass="RED"></h:message>
                    </td>



                    <td valign="top">
                        <h:outputText value="Prove You're a Human" styleClass="formfieldnamefont"></h:outputText>
                    </td>
                    <td valign="top">
                        <div style="border: 1px solid #ccc; padding: 3px;">
                        <h:inputText value="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getJ_captcha_response()%>" id="j_captcha_response" required="false" size="35" maxlength="200"/>
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
                    </td>
                    <td valign="top">
                        <h:message for="j_captcha_response" styleClass="RED"></h:message>
                    </td>


                    <td valign="top">
                        <h:outputText value="End User License Agreement" styleClass="formfieldnamefont"></h:outputText>
                    </td>
                    <td valign="top">
                        <h:inputTextarea value="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getEula()%>" id="eula" cols="45" rows="5" required="false">
                        </h:inputTextarea>
                    </td>
                    <td valign="top">
                        <h:message for="eula" styleClass="RED"></h:message>
                    </td>



                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <h:commandButton action="<%=((Registration)Pagez.getBeanMgr().get("Registration")).getRegisterAction()%>" value="Sign Up" styleClass="formsubmitbutton" target="_top"></h:commandButton>
                    </td>
                    <td valign="top">
                    </td>

                </table>


            </div>


<%@ include file="/jsp/templates/footer.jsp" %>