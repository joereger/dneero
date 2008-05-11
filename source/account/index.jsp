<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountIndex" %>
<%@ page import="com.dneero.htmluibeans.AccountBalance" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Your Account";
String navtab = "youraccount";
String acl = "account";
%>
<%
AccountIndex accountIndex = (AccountIndex) Pagez.getBeanMgr().get("AccountIndex");
%>
<%
if (accountIndex.getUserhasresponsependings()){
    Pagez.sendRedirect("/blogger/index.jsp");
    return;
}
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



       <%if (!accountIndex.getMsg().equals("")) {%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont"><%=((AccountIndex)Pagez.getBeanMgr().get("AccountIndex")).getMsg()%></font>
            </div>
       <%}%>

       <%if (Pagez.getRequest().getParameter("msg")!=null && Pagez.getRequest().getParameter("msg").equals("autologin")){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont">Your previous session timed out so you've been logged-in automatically!</font>
            </div>
       <%}%>

       <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="250" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/account/accountbalance.jsp"><font class="mediumfont" style="color: #596697;">Account Balance</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">See the earnings and charges for your account.</font><br/>
                                <br/><font class="formfieldnamefont" style="color: #666666;">Current Balance:</font>
                                <br/><font class="largefont" style="color: #cccccc;"><%=accountIndex.getCurrentbalance()%></font>
                                <% if (accountIndex.getPendingearningsDbl()>0){ %>
                                    <br/><font class="formfieldnamefont" style="color: #666666;">Pending: <%=accountIndex.getPendingearnings()%></font>
                                    <br/><font class="tinyfont" style="color: #666666;">Remember, your conversations must generate impressions for 10 days after you take them to get paid.</font><br/>
                                <% } %>
                                <br/><font class="tinyfont" style="color: #666666;"><a href="/account/balancefaq.jsp">Balance Questions?</a></font>

                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/account/accountsettings.jsp"><font class="mediumfont" style="color: #596697;">Account Settings</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Change the name and/or email on your account.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/account/changepassword.jsp"><font class="mediumfont" style="color: #596697;">Change Your Password</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Change the password that you use to log in to your account.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/account/reseller.jsp"><font class="mediumfont" style="color: #596697;">Reseller Program</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Sell dNeero conversations and make money!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/account/accountsupportissueslist.jsp"><font class="mediumfont" style="color: #596697;">Help/Support</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Use the built-in issue and question tracking system to get help with your account.  Ask us anything and view our responses quickly and easily.</font>
                            </td></tr></table>
                        </div>
                    </div>

                    <br/>
                    <%if (!Pagez.getUserSession().getUser().getIsactivatedbyemail()){%>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <font class="mediumfont">Your account has not yet been activated by email.</font>
                            <br/>
                            <font class="smallfont">You must activate within 3 days of signup.  Check your email inbox for an activation message.  If you've lost that message... no problem: <img src="/images/clear.gif" width="2" height="1"/><a href="/emailactivationresend.jsp">re-send it</a>.</font>
                        </div>
                   <%}%>
                   <%if (!Pagez.getUserSession().getUser().getPaymethodpaypaladdress().equals("")){%>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <font class="mediumfont">You must enter a PayPal address to get paid.</font>
                            <br/>
                            <font class="smallfont">Before we can pay you we must have a valid PayPal address.  Enter one: <img src="/images/clear.gif" width="2" height="1"/><a href="/account/accountsettings.jsp">here</a>.</font>
                        </div>
                   <%}%>
                </td>
                <td valign="top">

                    <table cellpadding="20" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td width="50%" align="left" valign="top">
                                <center><a href="/blogger/index.jsp"><img src="/images/user.png" alt="" border="0" width="128" height="128"/></a></center>
                                <div><%=com.dneero.htmlui.GreenRoundedButton.get("<a href=\"/blogger/index.jsp\"><font class=\"mediumfont\" style=\"color: #ffffff;\">People</font></a>")%></div>
                                <div style="padding: 10px;">
                                    <font class="smallfont"><b>Join Conversations</b></font>
                                    <br/>
                                    <font class="smallfont">You're a social person interested in joining conversations, posting your answers to your blog and making money.</font>
                                    <br/>
                                    <a href="/blogger/index.jsp"><font class="subnavfont" style="color: #596697;">Continue as a Social Person</font></a>
                                </div>
                            </td>
                            <td width="50%" align="left" valign="top">
                                <center><a href="/researcher/index.jsp"><img src="/images/statistic-128.png" alt="" border="0" width="128" height="128"/></a></center>
                                <div><%=com.dneero.htmlui.GreenRoundedButton.get("<a href=\"/researcher/index.jsp\"><font class=\"mediumfont\" style=\"color: #ffffff;\">Igniters</font></a>")%></div>
                                <div style="padding: 10px;">
                                    <font class="smallfont"><b>Ignite and Manage Conversations</b></font>
                                    <br/>
                                    <font class="smallfont">You're a researcher interested in igniting conversations, finding social people to respond to them and generating buzz for your product or service.</font>
                                    <br/>
                                    <a href="/researcher/index.jsp"><font class="subnavfont" style="color: #596697;">Continue as a Conversation Igniter</font></a>
                                </div>
                            </td>
                        </tr>
                    </table>


                    
                </td>
            </tr>
        </table>



<%@ include file="/template/footer.jsp" %>


