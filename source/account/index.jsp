<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountIndex" %>
<%@ page import="com.dneero.htmluibeans.AccountBalance" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
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

        <%
        long unreadInboxItems = NumFromUniqueResult.getInt("select count(*) from Mail where userid='"+Pagez.getUserSession().getUser().getUserid()+"' and isread=false");
        if (unreadInboxItems>0){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                <font class="smallfont"><a href="/account/inbox.jsp"><%=unreadInboxItems%> unread messages</a> are waiting for you.</font>
            </div>
       <%}%>

        <%  long openReviewItems = NumFromUniqueResult.getInt("select count(*) from Review where useridofcontentcreator='"+Pagez.getUserSession().getUser().getUserid()+"' and (isresearcherrejected=true or isresearcherwarned=true or issysadminwarned=true or issysadminrejected=true)");
               if (openReviewItems>0){%>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="smallfont"><a href="/account/reviewables.jsp"><%=openReviewItems%> of your items</a> are flagged.</font>
                </div>
           <%}%>



       <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="33%" valign="top">
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
                                <br/><font class="tinyfont" style="color: #666666;"><a href="/account/awards.jsp">Your Awards</a></font>
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
                                <a href="/account/awards.jsp"><font class="mediumfont" style="color: #596697;">Awards</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">See the things you've been awarded.</font>
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
                                <font class="smallfont">Sell conversations and make money!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/account/inbox.jsp"><font class="mediumfont" style="color: #596697;">Inbox</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Use the Inbox messaging system to get help with your account.  Ask us anything and view our responses quickly and easily.</font>
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
                <td width="33%" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <center><a href="/blogger/index.jsp"><img src="/images/user.png" alt="" border="0" width="128" height="128"/></a></center>
                                <div><%=com.dneero.htmlui.GreenRoundedButton.get("<a href=\"/blogger/index.jsp\"><font class=\"mediumfont\" style=\"color: #ffffff;\">Social People</font></a>")%></div>
                                <%--<div style="padding: 10px;">--%>
                                    <%--<font class="smallfont"><b>Join Conversations</b></font>--%>
                                    <%--<br/>--%>
                                    <%--<font class="smallfont">You're a social person interested in joining conversations, posting your answers to your blog and making money.</font>--%>
                                    <%--<br/>--%>
                                    <%--<a href="/blogger/index.jsp"><font class="subnavfont" style="color: #596697;">Continue as a Social Person</font></a>--%>
                                <%--</div>--%>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #eeeeee;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/publicsurveylist.jsp"><font class="mediumfont" style="color: #596697;">Find Conversations to Join</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Make money joining conversations and posting your opinions where your peers can join in too.</font>
                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/blogger/bloggerearningsrevshare.jsp"><font class="mediumfont" style="color: #596697;">Earn Money Inviting Friends</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Invite friends and earn money when they join conversations!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/blogger/index.jsp"><font class="mediumfont" style="color: #596697;">Earnings from Joined Conversations</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">See how much you've earned.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/blogger/bloggerdetails.jsp"><font class="mediumfont" style="color: #596697;">Update Profile</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Your profile helps us find conversations that fit your interests.  Keep it up to date.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/blogger/bloggerfaq.jsp"><font class="mediumfont" style="color: #596697;">Blogger Frequently Asked Questions</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Get your answers here!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/blogger/index.jsp?showmarketingmaterial=1"><font class="mediumfont" style="color: #596697;">Blogger Basic Info</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Basic Blogger information, how the system works, etc.</font>
                            </td></tr></table>
                        </div>
                    </div>
                </td>
                <td width="33%" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <center><a href="/researcher/index.jsp"><img src="/images/statistic-128.png" alt="" border="0" width="128" height="128"/></a></center>
                            <div><%=com.dneero.htmlui.GreenRoundedButton.get("<a href=\"/researcher/index.jsp\"><font class=\"mediumfont\" style=\"color: #ffffff;\">Igniters</font></a>")%></div>
                            <%--<div style="padding: 10px;">--%>
                                <%--<font class="smallfont"><b>Ignite and Manage Conversations</b></font>--%>
                                <%--<br/>--%>
                                <%--<font class="smallfont">You're a researcher interested in igniting conversations, finding social people to respond to them and generating buzz for your product or service.</font>--%>
                                <%--<br/>--%>
                                <%--<a href="/researcher/index.jsp"><font class="subnavfont" style="color: #596697;">Continue as a Conversation Igniter</font></a>--%>
                            <%--</div>--%>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #eeeeee;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researchersurveydetail_01.jsp"><font class="mediumfont" style="color: #596697;">Start a New Conversation</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Ignite a new conversation. This step-by-step wizard will guide you through the process.  Your conversation can be up and running in minutes.</font>
                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/panels.jsp"><font class="mediumfont" style="color: #596697;">Panels</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create and manage standing panels of bloggers for longitudinal studies.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/rank-list.jsp"><font class="mediumfont" style="color: #596697;">Rankings</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create your own ranking index and use it to define/track people across multiple conversations.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researcherdetails.jsp"><font class="mediumfont" style="color: #596697;">Update My Researcher Profile</font></a>
                             </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Help us understand your needs so that we can serve you better.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researcherbilling.jsp"><font class="mediumfont" style="color: #596697;">Billing Info</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Update your billing information on this screen.</font>
                            </td></tr></table>


                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researcherfaq.jsp"><font class="mediumfont" style="color: #596697;">Researcher Frequently Asked Questions</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Get your answers here!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/index.jsp?showmarketingmaterial=1"><font class="mediumfont" style="color: #596697;">Researcher Basic Info</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Basic Researcher information, how the system works, etc.</font>
                            </td></tr></table>
                        </div>
                    </div>
                </td>




            </tr>
        </table>



<%@ include file="/template/footer.jsp" %>


