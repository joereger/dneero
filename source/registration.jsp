<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.Registration" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.systemprops.SystemProperty" %>
<%@ page import="com.dneero.util.RandomString" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%String jspPageName="/registration.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Create an Account";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
Registration registration = (Registration)Pagez.getBeanMgr().get("Registration");
%>
<%
if (request.getParameter("whereToRedirectToAfterSignup")!=null) {
    Pagez.getUserSession().setWhereToRedirectToAfterSignup(request.getParameter("whereToRedirectToAfterSignup"));
}
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("register")) {
        try {
            registration.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            //registration.setEula(Textarea.getValueFromRequest("eula", "Eula", true));
            //registration.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
            //registration.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
            registration.setPassword(TextboxSecret.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            //registration.setPasswordverify(TextboxSecret.getValueFromRequest("passwordverify", "Password Verify", true, DatatypeString.DATATYPEID));
            registration.setNickname(Textbox.getValueFromRequest("nickname", "Nickname", true, DatatypeString.DATATYPEID));

            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6Le0RgwAAAAAAG5hYkFqs3xMIAIHIwreTJsPkFGg", "6Le0RgwAAAAAACMo7qCUbKxrPGpB6pgrITwOshzd", false);
            ReCaptchaResponse capResp = captcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
            if (capResp.isValid()) {
                registration.registerAction();
                Pagez.getUserSession().setMessage("Welcome!  We've sent you a confirmation email (you have 3 days to confirm.)");
                //Note that Pagez.getUserSession().getWhereToRedirectToAfterSignup() is handled in /account/index.jsp because survey processing must supercede
                //Redir if https is on
                if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")) {
                    try {
                        logger.debug("redirecting to https - " + BaseUrl.get(true) + "account/index.jsp");
                        Pagez.sendRedirect(BaseUrl.get(true) + "account/index.jsp");
                        return;
                    } catch (Exception ex) {
                        logger.error("", ex);
                        //@todo setIsfirsttimelogin(true) on AccountIndex bean
                        Pagez.sendRedirect("/account/index.jsp");
                        return;
                    }
                } else {
                    //@todo setIsfirsttimelogin(true) on AccountIndex bean
                    Pagez.sendRedirect("/account/index.jsp");
                    return;
                }
            } else {
                Pagez.getUserSession().setMessage("Sorry, you need to type the squiggly letters properly.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


        <font class="tinyfont"><a href="/login.jsp" target="_new">(already have an account? log in here.)</a></font>
        <br/><br/>


        <%if (registration.getDisplaytempresponsesavedmessage()){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                <table cellpadding="5">
                    <tr>
                        <td valign="top">
                            <img src="/images/redo-64.png" width="64" height="64" alt="" align="left"/>
                        </td>
                        <td valign="top">
                            <font class="mediumfont">Almost done... we've temporarily saved your response.</font><br/>
                            <font class="smallfont"><b>Please log in or sign up below.</b></font>
                        </td>
                    </tr>
                </table>
            </div>
            <br/>
        <%}%>

        <%--<div style="width: 250px; float: right; padding-left: 20px;">--%>
            <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">--%>
                <%--<font class="mediumfont" style="color: #333333">Existing Users</font><br/>--%>
                <%--<font class="smallfont">If you've already got an account you can simply log in.</font><br/>--%>
                    <%--<a class="sexybutton" href="/login.jsp"><span><span>Log In</span></span></a>--%>
            <%--</div>--%>
            <%--<br/>--%>
            <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">--%>
                <%--<font class="mediumfont" style="color: #333333">Facebook??</font><br/>--%>
                <%--<font class="smallfont">We've got a Facebook App for you.</font><br/>--%>
                    <%--<a class="sexybutton" href="http://apps.facebook.com/dneerosocialsurveys/"><span><span>Facebook App</span></span></a>--%>
            <%--</div>--%>
        <%--</div>--%>
        <form action="/registration.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/registration.jsp">
            <input type="hidden" name="action" value="register">
            <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">--%>
                <br/><br/>
                <table cellpadding="5" cellspacing="0" border="0">

                    <%--<tr>--%>
                        <%--<td valign="top" width="33%" align="right">--%>
                            <%--<font class="formfieldnamefont">First, Last Name</font>--%>
                        <%--</td>--%>
                        <%--<td valign="top">--%>
                            <%--<%=Textbox.getHtml("firstname", registration.getFirstname(), 255, 10, "", "width:151px;")%><%=Textbox.getHtml("lastname", registration.getLastname(), 255, 10, "", "width:151px;")%>--%>
                        <%--</td>--%>
                    <%--</tr>--%>


                    <tr>
                        <td valign="top" align="right">
                            <font class="formfieldnamefont">Nickname</font>
                            <br/>
                            <font class="tinyfont"></font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("nickname", registration.getNickname(), 255, 35, "", "width:308px;")%>
                            <br/>
                            <font class="tinyfont">Will be shared publicly. Letters & numbers only. No spaces or other characters.</font>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top" align="right">
                            <font class="formfieldnamefont">Email</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("email", registration.getEmail(), 255, 35, "", "width:308px;")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top" align="right">
                            <font class="formfieldnamefont">Password</font>
                        </td>
                        <td valign="top">
                            <%=TextboxSecret.getHtml("password", registration.getPassword(), 255, 35, "", "width:308px;")%>
                        </td>
                    </tr>

                    <%--<tr>--%>
                        <%--<td valign="top">--%>
                            <%--<font class="formfieldnamefont">Password Verify</font>--%>
                        <%--</td>--%>
                        <%--<td valign="top">--%>
                            <%--<%=TextboxSecret.getHtml("passwordverify", registration.getPasswordverify(), 255, 35, "", "")%>--%>
                        <%--</td>--%>
                    <%--</tr>--%>



                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont"></font>
                        </td>
                        <td valign="top">
                            <%
                            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6Le0RgwAAAAAAG5hYkFqs3xMIAIHIwreTJsPkFGg", "6Le0RgwAAAAAACMo7qCUbKxrPGpB6pgrITwOshzd", false);
                            String captchaScript = captcha.createRecaptchaHtml(request.getParameter("error"), null);
                            out.print(captchaScript);
                            %>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont"></font>
                        </td>
                        <td valign="top">
                            <font class="tinyfont">I agree to the terms of the <a href="/eula.jsp" target="_new">End User License Agreement</a></font>
                        </td>
                    </tr>


                    <tr>
                        <td valign="top">
                        </td>
                        <td valign="top">
                            <br/><br/>
                            <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Sign Up">
                            <br/><br/>
                        </td>
                    </tr>

                </table>
            <%--</div>--%>
        </form>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>