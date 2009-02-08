<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.AccountSettings" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Settings";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
AccountSettings accountSettings = (AccountSettings) Pagez.getBeanMgr().get("AccountSettings");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
    try {
        accountSettings.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
        accountSettings.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
        accountSettings.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
        accountSettings.setNotifyofnewsurveysbyemaileveryexdays(Dropdown.getIntFromRequest("notifyofnewsurveysbyemaileveryexdays", "Notify Every X Days", true));
        accountSettings.setAllownoncriticalemails(CheckboxBoolean.getValueFromRequest("allownoncriticalemails"));
        accountSettings.setInstantnotifybyemailison(CheckboxBoolean.getValueFromRequest("instantnotifybyemailison"));
        accountSettings.setInstantnotifybytwitterison(CheckboxBoolean.getValueFromRequest("instantnotifybytwitterison"));
        accountSettings.setInstantnotifytwitterusername(Textbox.getValueFromRequest("instantnotifytwitterusername", "Twitter Username", false, DatatypeString.DATATYPEID));
        accountSettings.setInstantnotifyxmppison(CheckboxBoolean.getValueFromRequest("instantnotifyxmppison"));
        accountSettings.setInstantnotifyxmppusername(Textbox.getValueFromRequest("instantnotifyxmppusername", "XMPP/Jabber Username", false, DatatypeString.DATATYPEID));
        accountSettings.setPaymethodpaypaladdress(Textbox.getValueFromRequest("paymethodpaypaladdress", "PayPal Address", false, DatatypeString.DATATYPEID));
        accountSettings.setNickname(Textbox.getValueFromRequest("nickname", "Nickname", true, DatatypeString.DATATYPEID));
        accountSettings.saveAction();
        Pagez.getUserSession().setMessage("Settings saved.");
    } catch (com.dneero.htmlui.ValidationException vex) {
        Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
    }
}
%>
<%@ include file="/template/header.jsp" %>

    <br/><br/>
    <form action="/account/accountsettings.jsp" method="post">
        <input type="hidden" name="dpage" value="/account/accountsettings.jsp">
        <input type="hidden" name="action" value="save">

            <table cellpadding="8" cellspacing="0" border="0">

                <tr>
                    <td valign="top" width="35%">
                        <font class="formfieldnamefont">First Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("firstname", accountSettings.getFirstname(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Last Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("lastname", accountSettings.getLastname(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Nickname</font>
                            <br/>
                            <font class="tinyfont">Only letters and numbers, no spaces.  This will be your public name that others can see.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("nickname", accountSettings.getNickname(), 255, 20, "", "")%>
                        </td>
                    </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email</font>
                        <br/>
                        <font class="tinyfont">This is used to log in.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("email", accountSettings.getEmail(), 255, 20, "", "")%>
                        <br/>
                        <font class="tinyfont">Changing email will require re-activation</font>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Password</font>
                        <br/>
                        <font class="tinyfont">Password changes are handled on a separate screen.</font>
                    </td>
                    <td valign="top">
                        <a href="/account/changepassword.jsp"><font class="smallfont">Change Password</font></a>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Notification of New Conversations<br/>Every X Days</font>
                        <br/>
                        <font class="tinyfont">Learn about new conversations before others. Of course we'll only disturb your inbox if there's actually a new conversation that you qualify for.</font>
                    </td>
                    <td valign="top">
                        <%=Dropdown.getHtml("notifyofnewsurveysbyemaileveryexdays",String.valueOf(accountSettings.getNotifyofnewsurveysbyemaileveryexdays()), ((AccountSettings)Pagez.getBeanMgr().get("AccountSettings")).getNotificationFrequencies(), "","")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Non Critical Email</font>
                        <br/>
                        <font class="tinyfont">We only send messages that we believe you'll want to see.  However, you can opt out of everything but the most critical account-related and legal messages.</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("allownoncriticalemails", accountSettings.getAllownoncriticalemails(), "", "")%>
                        <font class="formfieldnamefont">Allow informative non-critical email</font>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email Instant Notify</font>
                        <br/>
                        <font class="tinyfont">If you like we can instantly notify you of new conversations by email.  No need to wait for the once a day message!</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("instantnotifybyemailison", accountSettings.getInstantnotifybyemailison(), "", "")%>
                        <font class="formfieldnamefont">Instantly notify me via email</font>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Twitter Username (Optional)</font>
                        <br/>
                        <font class="tinyfont">Allows you to reply to Twitter Questions and if you like we can also instantly notify you of new conversations via Twitter. Follow us <a href="http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%>">here</a>.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("instantnotifytwitterusername", accountSettings.getInstantnotifytwitterusername(), 255, 20, "", "")%>
                        <br/>
                        <font class="tinyfont">Note: this should be something like "joereger" and not your email address.</font>
                        <br/>
                        <%=CheckboxBoolean.getHtml("instantnotifybytwitterison", accountSettings.getInstantnotifybytwitterison(), "", "")%>
                        <font class="formfieldnamefont">Instantly notify me via Twitter</font>
                        <br/>


                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">XMPP/Jabber Address (Optional)</font>
                        <br/>
                        <font class="tinyfont">If you like we can also instantly notify you of new conversations via XMPP/Jabber. This works with a Google Chat account or other Jabber servers.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("instantnotifyxmppusername", accountSettings.getInstantnotifyxmppusername(), 255, 20, "", "")%>
                        <br/>
                        <%=CheckboxBoolean.getHtml("instantnotifyxmppison", accountSettings.getInstantnotifyxmppison(), "", "")%>
                        <font class="formfieldnamefont">Instantly notify me via XMPP/Jabber</font>
                        <br/>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">PayPal Address (Optional)</font>
                        <br/>
                        <font class="tinyfont">We can't pay you until we have a valid<br/>PayPal address.  You can enter one<br/>at any time.  You don't need to have one<br/>in the system to blog and earn money.<br/>Your PayPal address is the email<br/>address you use to log in<br/>to paypal.com.<br/>Don't have a PayPal account? <br/>Setup is quick, easy and secure.<br/>Click <a href="http://www.paypal.com" target="paypal" class="tinyfont"><font class="tinyfont">here</font></a> to create one.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("paymethodpaypaladdress", accountSettings.getPaymethodpaypaladdress(), 255, 20, "", "")%>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton" value="Save Settings">
                    </td>
                </tr>

            </table>

    </form>


<%@ include file="/template/footer.jsp" %>


