<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reset Password";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputSecret value="<%=((LostPasswordChoose)Pagez.getBeanMgr().get("LostPasswordChoose")).getPassword()%>" id="password" required="false"></h:inputSecret>
                </td>
                <td valign="top">
                    <h:message for="password" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Verify Password" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputSecret value="<%=((LostPasswordChoose)Pagez.getBeanMgr().get("LostPasswordChoose")).getPasswordverify()%>" id="passwordverify" required="false"></h:inputSecret>
                </td>
                <td valign="top">
                    <h:message for="passwordverify" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Prove You're a Human" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <div style="border: 1px solid #ccc; padding: 3px;">
                    <h:inputText value="<%=((LostPasswordChoose)Pagez.getBeanMgr().get("LostPasswordChoose")).getJ_captcha_response()%>" id="j_captcha_response" required="false"/>
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
                </td>
                <td valign="top">
                    <h:message for="j_captcha_response" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((LostPasswordChoose)Pagez.getBeanMgr().get("LostPasswordChoose")).getChoosePassword()%>" value="Reset Password" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>



<%@ include file="/jsp/templates/footer.jsp" %>
