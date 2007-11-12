<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Lost Password";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((LostPassword)Pagez.getBeanMgr().get("LostPassword")).getEmail()%>" id="email" required="false"></h:inputText>
                </td>
                <td valign="top">
                    <h:message for="email" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="Prove You're a Human" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <div style="border: 1px solid #ccc; padding: 3px;">
                    <h:inputText value="<%=((LostPassword)Pagez.getBeanMgr().get("LostPassword")).getJ_captcha_response()%>" id="j_captcha_response" required="false"/>
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
                    <h:commandButton action="<%=((LostPassword)Pagez.getBeanMgr().get("LostPassword")).getRecoverPassword()%>" value="Recover Password by Email" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>



<%@ include file="/jsp/templates/footer.jsp" %>
