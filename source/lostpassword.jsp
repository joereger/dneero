<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.LostPassword" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Lost Password";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
LostPassword lostPassword = (LostPassword)Pagez.getBeanMgr().get("LostPassword");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("go")) {
        try {
            lostPassword.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            lostPassword.setJ_captcha_response(Textbox.getValueFromRequest("j_captcha_response", "Squiggly Letters", true, DatatypeString.DATATYPEID));
            lostPassword.recoverPassword();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <form action="lostpassword.jsp" method="post">
        <input type="hidden" name="action" value="go">
        <table cellpadding="0" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Email</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("email", lostPassword.getEmail(), 255, 35, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Prove You're a Human</font>
                </td>
                <td valign="top">
                    <div style="border: 1px solid #ccc; padding: 3px;">
                    <%=Textbox.getHtml("j_captcha_response", lostPassword.getJ_captcha_response(), 255, 35, "", "")%>
                    <br/>
                    <font class="tinyfont">(type the squiggly letters that appear below)</font>
                    <br/>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td><img src="/images/clear.gif" alt="" width="1" height="100"></img></td>
                            <td style="background: url(/images/loading-captcha.gif);">
                                <img src="/images/clear.gif" alt="" width="200" height="1"></img><br/>
                                <img src="/jcaptcha"/>
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
                    <input type="submit" value="Recover Password by Email">
                </td>
            </tr>

        </table>
    </form>


<%@ include file="/jsp/templates/footer.jsp" %>