<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.EmailActivationResend" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Re-Send";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
EmailActivationResend emailActivationResend = (EmailActivationResend)Pagez.getBeanMgr().get("EmailActivationResend");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("send")) {
        try {
            emailActivationResend.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            emailActivationResend.setEmail(Textbox.getValueFromRequest("j_captcha_response", "Squiggly Letters", true, DatatypeString.DATATYPEID));
            emailActivationResend.reSendEmail();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>

        <form action="emailactivationresend.jsp" method="post">
            <input type="hidden" name="action" value="send">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("email", emailActivationResend.getEmail(), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Prove You're a Human</font>
                    </td>
                    <td valign="top">
                        <div style="border: 1px solid #ccc; padding: 3px;">
                        <%=Textbox.getHtml("j_captcha_response", emailActivationResend.getJ_captcha_response(), 255, 35, "", "")%>
                        <br/>
                        <font class="tinyfont">(type the squiggly letters that appear below)</font>
                        <br/>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td><img src="/images/clear.gif" alt="" width="1" height="100"/></td>
                                <td style="background: url(/images/loading-captcha.gif);">
                                    <img src="/images/clear.gif" alt="" width="200" height="1"/><br/>
                                    <img src="/jcaptcha" alt=""/>
                                </td>
                            </tr>
                        </table>
                        </div>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" value="Re-Send Activation Email">
                    </td>
                </tr>

            </table>
        </form>



<%@ include file="/jsp/templates/footer.jsp" %>