<%@ page import="com.dneero.htmlui.DatatypeString" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.Textbox" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%@ page import="com.dneero.htmluibeans.EmailActivationResend" %>
<%@ page import="com.dneero.util.RandomString" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Enter Email Address to Resend Activation Message";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
EmailActivationResend emailActivationResend = (EmailActivationResend)Pagez.getBeanMgr().get("EmailActivationResend");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("send")) {
        try {
            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6LeIqAQAAAAAALFIlYeWpO4tV_mGwfssSd7nAiul", "6LeIqAQAAAAAAE9cMX9WGmGKEgQfXl-8PAPYmJyn", false);
            ReCaptchaResponse capResp = captcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
            if (capResp.isValid()) {
                emailActivationResend.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
                emailActivationResend.reSendEmail();
                Pagez.sendRedirect("/emailactivationresendcomplete.jsp");
                return;
            } else {
                Pagez.getUserSession().setMessage("Sorry, you need to type the squiggly letters properly.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>
        <br/><br/>
        <form action="/emailactivationresend.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/emailactivationresend.jsp">
            <input type="hidden" name="action" value="send">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont"></font>
                    </td>
                    <td valign="top">
                        <font class="formfieldnamefont">Email Address</font>
                        <br/>
                        <%=Textbox.getHtml("email", emailActivationResend.getEmail(), 255, 35, "", "font-size: 25px;")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont"></font>
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <%
                        ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6LeIqAQAAAAAALFIlYeWpO4tV_mGwfssSd7nAiul", "6LeIqAQAAAAAAE9cMX9WGmGKEgQfXl-8PAPYmJyn", false);
                        String captchaScript = captcha.createRecaptchaHtml(request.getParameter("error"), null);
                        out.print(captchaScript);
                        %>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Re-Send Activation Email">
                    </td>
                </tr>

            </table>
        </form>



<%@ include file="/template/footer.jsp" %>
