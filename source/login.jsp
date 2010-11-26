<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.Login" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.systemprops.SystemProperty" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%String jspPageName="/login.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>


<%
Login login = (Login) Pagez.getBeanMgr().get("Login");
%>
<%
Registration registration = (Registration)Pagez.getBeanMgr().get("Registration");
%>
<%
if (request.getParameter("whereToRedirectToAfterSignup")!=null) {
    Pagez.getUserSession().setWhereToRedirectToAfterSignup(request.getParameter("whereToRedirectToAfterSignup"));
}
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("login")) {
        try {
            login.setEmail(com.dneero.htmlui.Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            login.setPassword(com.dneero.htmlui.Textbox.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            login.setKeepmeloggedin(CheckboxBoolean.getValueFromRequest("keepmeloggedin"));
            login.login();
            //Redir if https is on
            String keepmeloggedinStr = "";
            if (login.getKeepmeloggedin()){
                keepmeloggedinStr = "&keepmeloggedin=1";
            }
            if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")) {
                //Is SSL
                try {
                    if (Num.isinteger(request.getParameter("redirtosurveyid"))) {
                        //Redir to a specific survey after link-login
                        logger.debug("redirecting to https survey - " + "/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                        Pagez.sendRedirect(BaseUrl.get(true) + "/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                        return;
                    } else {
                        //Normal login
                        logger.debug("redirecting to https - " + BaseUrl.get(true) + "account/index.jsp?"+keepmeloggedinStr);
                        Pagez.sendRedirect(BaseUrl.get(true) + "account/index.jsp?"+keepmeloggedinStr);
                        return;
                    }
                } catch (Exception ex) {
                    //Default error play
                    logger.error("", ex);
                    Pagez.sendRedirect("/account/index.jsp"+keepmeloggedinStr);
                    return;
                }
            } else {
                //Not SSL
                if (Num.isinteger(request.getParameter("redirtosurveyid"))) {
                    //Redir to a specific survey after link-login
                    logger.debug("redirecting to http - "+"/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                    Pagez.sendRedirect("/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                    return;
                } else {
                    //Normal login
                    logger.debug("redirecting to http - /account/index.jsp?"+keepmeloggedinStr);
                    Pagez.sendRedirect("/account/index.jsp?"+keepmeloggedinStr);
                    return;
                }
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("loginanonymously")) {
        try {
            login.loginAnonymously();
            //Redir if https is on
            String keepmeloggedinStr = "";
            if (true){
                keepmeloggedinStr = "&keepmeloggedin=1";
            }
            if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")) {
                //Is SSL
                try {
                    if (Num.isinteger(request.getParameter("redirtosurveyid"))) {
                        //Redir to a specific survey after link-login
                        logger.debug("redirecting to https survey - " + "/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                        Pagez.sendRedirect(BaseUrl.get(true) + "/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                        return;
                    } else {
                        //Normal login
                        logger.debug("redirecting to https - " + BaseUrl.get(true) + "account/index.jsp?"+keepmeloggedinStr);
                        Pagez.sendRedirect(BaseUrl.get(true) + "account/index.jsp?"+keepmeloggedinStr);
                        return;
                    }
                } catch (Exception ex) {
                    //Default error play
                    logger.error("", ex);
                    Pagez.sendRedirect("/account/index.jsp"+keepmeloggedinStr);
                    return;
                }
            } else {
                //Not SSL
                if (Num.isinteger(request.getParameter("redirtosurveyid"))) {
                    //Redir to a specific survey after link-login
                    logger.debug("redirecting to http - "+"/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                    Pagez.sendRedirect("/survey.jsp?surveyid="+request.getParameter("redirtosurveyid")+"&keepmeloggedin=1");
                    return;
                } else {
                    //Normal login
                    logger.debug("redirecting to http - /account/index.jsp?"+keepmeloggedinStr);
                    Pagez.sendRedirect("/account/index.jsp?"+keepmeloggedinStr);
                    return;
                }
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
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
                if (Pagez.getUserSession().getPl().getIsemailactivationrequired()){
                    Pagez.getUserSession().setMessage("Your account has been created.  We've sent you a confirmation email.");
                } else {
                    Pagez.getUserSession().setMessage("Your account has been created.");
                }
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
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("logout")) {
        try {
            login.logout();
            Pagez.getUserSession().setMessage("You have been logged out.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


        <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getIsanonymous() && (request.getParameter("combineanon")==null || !request.getParameter("combineanon").equals("1"))){%>
            <font class="smallfont">You're currently logged-in as an Anonymous user.</font>
            <font class="mediumfont">What would you like to do with the Anonymous user's survey responses?</font>
            <br/><font class="normalfont"><a href="/login.jsp?combineanon=1">Move the survey response to my new account (or my existing account.)</a></font>
            <br/><font class="normalfont"><a href="/login.jsp?action=logout">Ignore them.  I'm logging in or creating a new account.</a></font>
        <% } else { %>

            <%if (registration.getDisplaytempresponsesavedmessage()){%>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                    <table cellpadding="5">
                        <tr>
                            <td valign="top">
                                <img src="/images/redo-64.png" width="64" height="64" alt="" align="left"/>
                            </td>
                            <td valign="top">
                                <font class="mediumfont">We've temporarily saved your response.</font><br/>
                            </td>
                        </tr>
                    </table>
                </div>
                <br/>
            <%}%>





            <table cellpadding="10" cellspacing="0" border="0">
                <tr>
                    <%--<%if (Pagez.getUserSession().getPl().getIsanonymousresponseallowed()){%>--%>
                        <%--<%--%>
                        <%--boolean showAnonLogin = false;--%>
                        <%--Survey survey = Survey.get(Pagez.getUserSession().getPendingSurveyResponseSurveyid());--%>
                        <%--if (survey!=null && survey.getSurveyid()==Pagez.getUserSession().getPendingSurveyResponseSurveyid()){--%>
                            <%--if (survey.getIsanonymousresponseallowed()){--%>
                                <%--showAnonLogin = true;--%>
                            <%--}--%>
                        <%--}--%>
                        <%--%>--%>
                        <%--<%if (showAnonLogin){%>--%>
                            <%--<td width="" valign="top">--%>
                                <%--<div class="rounded" style="background: #e6e6e6;">--%>
                                    <%--<font class="pagetitlefont">Anonymous</font>--%>
                                    <%--<br/><br/>--%>
                                    <%--<form action="/login.jsp" method="post" class="niceform">--%>
                                        <%--<input type="hidden" name="dpage" value="/login.jsp">--%>
                                        <%--<input type="hidden" name="action" value="loginanonymously">--%>
                                        <%--<font class="tinyfont">I agree to the terms of the<br/><a href="/eula.jsp" target="_new">End User License Agreement</a></font>--%>
                                        <%--<br/>--%>
                                        <%--<input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Stay Anonymous">--%>
                                    <%--</form>--%>
                                <%--</div>--%>
                            <%--</td>--%>
                        <%--<%}%>--%>
                    <%--<%}%>--%>
                    <td width="50%" valign="top">
                        <div class="rounded" style="background: #e6e6e6;">
                            <font class="pagetitlefont">Existing Users</font>
                            <br/><br/>
                            <form action="/login.jsp" method="post" class="niceform">
                                <input type="hidden" name="dpage" value="/login.jsp">
                                <input type="hidden" name="action" value="login">

                                    <table cellpadding="5" cellspacing="0" border="0">

                                        <tr>
                                            <td valign="top">
                                                <font class="formfieldnamefont">Email</font>
                                            </td>
                                            <td valign="top">
                                                <%=Textbox.getHtml("email", "", 255, 20, "", "")%>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td valign="top">
                                                <font class="formfieldnamefont">Password</font>
                                            </td>
                                            <td valign="top">
                                                <%=TextboxSecret.getHtml("password", "", 255, 20, "", "")%>
                                                <br/>
                                                <a href="/lostpassword.jsp"><font class="tinyfont" style="color: #000000;">Lost your password?</font></a>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td valign="top">
                                            </td>
                                            <td valign="top">
                                                <%=CheckboxBoolean.getHtml("keepmeloggedin", false, "", "")%>
                                                <font class="formfieldnamefont">Stay Logged In?</font>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td valign="top">
                                            </td>
                                            <td valign="top">
                                                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Log In">
                                            </td>
                                        </tr>

                                    </table>

                            </form>
                        </div>
                    </td>
                    <td width="50%" valign="top">
                        <div class="rounded" style="background: #e6e6e6;">
                            <font class="pagetitlefont">New Users</font>
                            <br/><br/>
                            <form action="/login.jsp" method="post" class="niceform">
                                <input type="hidden" name="dpage" value="/login.jsp">
                                <input type="hidden" name="action" value="register">
                                    <table cellpadding="5" cellspacing="0" border="0">


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
                                                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Create an Account">
                                                <br/><br/>
                                            </td>
                                        </tr>

                                    </table>

                            </form>
                        </div>
                    </td>
                </tr>
            </table>
        <%}%>

<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>
