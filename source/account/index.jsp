<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountIndex" %>
<%@ page import="com.dneero.htmluibeans.AccountBalance" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
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
                                <br/><font class="largefont hdr"><%=accountIndex.getCurrentbalance()%></font>
                                <% if (accountIndex.getPendingearningsDbl()>0){ %>
                                    <br/><font class="formfieldnamefont" style="color: #666666;">Pending: <%=accountIndex.getPendingearnings()%></font>
                                    <br/><font class="tinyfont" style="color: #666666;">Remember, your conversations must generate impressions for 10 days after you take them to get paid.</font><br/>
                                <% } %>
                                <br/><font class="tinyfont" style="color: #666666;"><a href="/account/balancefaq.jsp">Balance Questions?</a></font>
                                <br/><font class="tinyfont" style="color: #666666;"><a href="/account/awards.jsp">Your Awards</a></font>
                            </td></tr></table>
                        </div>

                        <%if (Pagez.getRequest().getParameter("msg")!=null && Pagez.getRequest().getParameter("msg").equals("autologin")){%>
                            <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                                <font class="smallfont">Your previous session timed out so you've been logged-in automatically!</font>
                            </div>
                       <%}%>

                        <%if (!Pagez.getUserSession().getUser().getIsactivatedbyemail()){%>
                            <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                                <font class="smallfont" style="font-weight: bold;">Your account has not yet been activated by email.</font>
                                <br/>
                                <font class="smallfont">You must activate within 3 days of signup.  Check your email inbox for an activation message.  If you've lost that message... no problem: <a href="/emailactivationresend.jsp">re-send it</a>.</font>
                            </div>
                       <%}%>

                        <%
                        long unreadInboxItems = NumFromUniqueResult.getInt("select count(*) from Mail where userid='"+Pagez.getUserSession().getUser().getUserid()+"' and isread=false");
                        if (unreadInboxItems>0){%>
                            <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                                <font class="smallfont"><a href="/account/inbox.jsp"><%=unreadInboxItems%> unread messages</a></font>
                            </div>
                       <%}%>

                        <%long openReviewItems = NumFromUniqueResult.getInt("select count(*) from Review where useridofcontentcreator='"+Pagez.getUserSession().getUser().getUserid()+"' and (isresearcherrejected=true or isresearcherwarned=true or issysadminwarned=true or issysadminrejected=true)");
                               if (openReviewItems>0){%>
                                <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                                    <font class="smallfont"><a href="/account/reviewables.jsp"><%=openReviewItems%> of your items</a> were flagged.</font>
                                </div>
                        <%}%>

                        <%if (!Pagez.getUserSession().getUser().getPaymethodpaypaladdress().equals("")){%>
                            <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                                <font class="smallfont" style="font-weight: bold;">Enter a PayPal address.</font>
                                <br/>
                                <font class="smallfont">Before we can pay you we must have a valid PayPal address.<br/><a href="/account/accountsettings.jsp">Enter it Here</a></font>
                            </div>
                       <%}%>

                        <%if (Pagez.getUserSession().getUser()!=null){%>
                            <div class="rounded" style="padding: 10px; margin: 5px; background: #ffffff;">
                                <a href="/profile.jsp?userid=<%=Pagez.getUserSession().getUser().getUserid()%>"><font class="mediumfont"><b>Your Public Profile</b></font></a>
                                <br/><font class="smallfont">You can <a href="/account/accountsettings.jsp">choose a nickname</a> or <a href="/blogger/bloggerdetails.jsp">edit posting venues</a></font>
                            </div>
                       <%}%>

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
                </td>
                <td width="66%" valign="top">
                    <%--<div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">--%>
                    <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">--%>

                    <table cellpadding="10" cellspacing="0" border="0">
                        <tr>
                            <td valign="top" width="50%">
                                <div class="rounded" style="background: #e6e6e6;">
                                    <form action="/blogger/index.jsp" method="get">
                                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Enter Conversations">
                                    </form>
                                </div>
                            </td>
                            <td valign="top" width="50%">
                                <div class="rounded" style="background: #e6e6e6;">
                                    <form action="/researcher/index.jsp" method="get">
                                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Create Conversations">
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </table>


                    <%if (Pagez.getUserSession().getUser().getBloggerid()>0){%>
                        <font class="largefont hdr">Conversations for You</font>
                        <br/>
                        <%
                        PublicSurveyList publicSurveyList = new PublicSurveyList();
                        publicSurveyList.setMaxtodisplay(3);
                        publicSurveyList.initBean();
                        %>
                        <%if (publicSurveyList.getSurveys()==null || publicSurveyList.getSurveys().size()==0){%>
                            <font class="smallfont">No conversations right now.  Please check back soon... we're always adding new ones!</font>
                        <%} else {%>
                            <%
                                StringBuffer srv = new StringBuffer();
                                srv.append("" +
                                "     <table cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                                "         <tr>\n" +
                                "             <td valign=\"top\">\n" +
                                "                 <a href=\"/survey.jsp?surveyid=<$surveyid$>\"><font class=\"normalfont\" style=\"text-decoration: none; font-weight: bold;\"><$title$></font></a>\n"+
                                "                 <br/><font class=\"tinyfont\"><$description$></font>\n" +
                                "                 <br/><$accessonlyhtml$> <font class=\"tinyfont\"><b><$daysuntilend$></b></font>\n" +
                                "             </td>\n" +
                                "             <td valign=\"top\" style=\"text-align: right;\">\n" +
                                "                 <font class=\"normalfont\"><b><$earncompact$></b></font>\n" +
                                "             </td>\n" +
                                "         </tr>\n" +
                                "     </table>\n" +
                                "                    ");
                                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                                cols.add(new GridCol("", srv.toString(), false, "", "", "background: #ffffff;", ""));
                            %>
                            <%=Grid.render(publicSurveyList.getSurveys(), cols, 100, "/account/index.jsp", "pagesurveys")%>
                            <br/><a href="/publicsurveylist.jsp"><font class="smallfont" style="font-weight: bold;">See All Conversations for You</font></a>
                        <%}%>
                    <%} else {%>
                        <font class="largefont hdr">Conversations for You</font>
                        <br/><a href="/blogger/index.jsp"><font class="smallfont" style="font-weight: bold;">Find Conversations to Enter</font></a>
                    <%}%>
                    <!--</div>-->
                    <!--</div>-->

                    <%if (Pagez.getUserSession().getUser().getBloggerid()>0){%>
                        <br/><br/><br/>
                        <%--<div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">--%>
                        <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">--%>
                        <font class="largefont hdr">Conversations You've Joined</font>
                        <br/>
                        <%
                            BloggerCompletedsurveys bloggerCompletedsurveys = new BloggerCompletedsurveys();
                            bloggerCompletedsurveys.setMaxtodisplay(3);
                            bloggerCompletedsurveys.initBean();
                            StringBuffer template = new StringBuffer();
                            template.append("" +
                        "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                        "                <tr>\n" +
                        "                    <td valign=\"top\">\n" +
                        "     <font class=\"normalfont\" style=\"font-weight: bold;\"><a href=\"/surveypostit.jsp?surveyid=<$surveyid$>\"><$surveytitle$></a></font><br/>\n" +
                        "     <font class=\"tinyfont\" style=\"font-weight:bold; text-decoration: none;\"><a href=\"/blogger/impressions.jsp?surveyid=<$surveyid$>\">Impressions</a> | <a href=\"/survey.jsp?surveyid=<$surveyid$>\">Edit Answers</a></font>\n" +
                        "                    </td>\n" +
                        "                    <td valign=\"top\" width=\"200\">\n" +
                        "     <$response.responsestatushtml$>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "            </table>\n");
                        %>
                        <%if (bloggerCompletedsurveys.getList()==null || bloggerCompletedsurveys.getList().size()==0){%>
                            <font class="smallfont">You haven't joined any conversations.</font>
                            <br/><a href="/publicsurveylist.jsp"><font class="smallfont" style="font-weight: bold;">Find Conversations to Join</font></a>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                cols.add(new GridCol("", template.toString(), false, "", "tinyfont", "background: #ffffff;", ""));
                            %>
                            <%=Grid.render(bloggerCompletedsurveys.getList(), cols, 3, "/account/index.jsp", "pageyourconvos")%>
                            <br/><a href="/blogger/index.jsp"><font class="smallfont" style="font-weight: bold;">See All Conversations You've Joined</font></a>
                        <%}%>
                        <!--</div>-->
                        <!--</div>-->
                    <%}%>


                    <%if (1==1){%>
                        <br/><br/><br/>
                        <%--<div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">--%>
                        <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">--%>
                        <font class="largefont hdr">Twitter Questions</font>
                        <br/>
                        <%
                            BloggerCompletedTwitasks bloggerCompletedTwitasks = new BloggerCompletedTwitasks();
                            bloggerCompletedTwitasks.setMaxtodisplay(3);
                            bloggerCompletedTwitasks.initBean();
                            StringBuffer template = new StringBuffer();
                            template.append("" +
                            "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                            "                <tr>\n" +
                            "                    <td valign=\"top\">\n" +
                            "     <font class=\"normalfont\" style=\"font-weight: bold;\"><a href=\"/twitask.jsp?twitaskid=<$twitask.twitaskid$>\"><$twitask.question$></a></font><br/>\n" +
                            "     <font class=\"tinyfont\" style=\"font-weight:bold; text-decoration: none;\"><$twitanswer.answer$></font>\n" +
                            "                    </td>\n" +
                            "                    <td valign=\"top\" width=\"40%\">\n" +
                            "     \n" +
                            "                    </td>\n" +
                            "                </tr>\n" +
                            "            </table>\n");
                            
                        %>
                        <%if (bloggerCompletedTwitasks.getTwitanswers()==null || bloggerCompletedTwitasks.getTwitanswers().size()==0){%>
                            <font class="smallfont">You haven't answered any Twitter Questions!  <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%>">http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%></a></li><li>reply to questions you see us ask</li><li>make sure your social person <a href="/blogger/bloggerdetails.jsp">profile</a> is active</li></ol></font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                cols.add(new GridCol("", template.toString(), false, "", "tinyfont", "background: #ffffff;", ""));
                            %>
                            <%=Grid.render(bloggerCompletedTwitasks.getTwitanswers(), cols, 5, "/account/index.jsp", "pagetwitasks")%>
                            <br/><a href="/blogger/bloggercompletedtwitasks.jsp"><font class="smallfont" style="font-weight: bold;">See All Twitter Questions You've Answered</font></a>
                            <br/><br/><font class="tinyfont" style="font-weight: bold;">Congratulations!  Special Access Code: dtwitrks01</font>
                        <%}%>
                        <!--</div>-->
                        <!--</div>-->
                    <%}%>

                    <%--<div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">--%>
                    <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">--%>
                    <br/><br/><br/>
                    <%
                    ResearcherSurveyList researcherSurveyList = null;
                    %>
                    <%if (Pagez.getUserSession().getUser().getResearcherid()>0){%>
                        <font class="largefont hdr">Conversations You've Created</font>
                        <br/>
                        <%
                        researcherSurveyList = new ResearcherSurveyList();
                        researcherSurveyList.setMaxtodisplay(3);
                        researcherSurveyList.initBean();
                        %>
                        <%if (researcherSurveyList.getSurveys()==null || researcherSurveyList.getSurveys().size()==0){%>
                            <font class="smallfont">You haven't yet created any conversations.</font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                                cols.add(new GridCol("", "<a href=\"/survey.jsp?surveyid=<$survey.surveyid$>\"><font style=\"font-weight:bold;\"><$survey.title$></font></a>", false, "background: #ffffff;", "normalfont"));
                                cols.add(new GridCol("", "<$status$>", false, "", "smallfont", "background: #ffffff;", ""));
                                cols.add(new GridCol("", "<$editorreviewlink$>", false, "background: #ffffff;", "smallfont"));
                                cols.add(new GridCol("", "<$invitelink$>", false, "background: #ffffff;", "smallfont"));
                                cols.add(new GridCol("", "<$resultslink$>", false, "background: #ffffff;", "smallfont"));
                                cols.add(new GridCol("", "<$copylink$>", false, "background: #ffffff;", "smallfont"));
                                cols.add(new GridCol("", "<$deletelink$>", false, "background: #ffffff;", "smallfont"));
                            %>
                            <%=Grid.render(researcherSurveyList.getSurveys(), cols, 50, "/account/index.jsp", "pageresearcherconvos")%>
                            <br/><a href="/researcher/index.jsp"><font class="smallfont" style="font-weight: bold;">See All Conversations You've Created</font></a><font class="smallfont"> or </font><a href="/researcher/researchersurveydetail_01.jsp"><font class="smallfont" style="font-weight: bold;">Create a New One</font></a>
                        <%}%>
                    <%} else {%>
                        <font class="largefont hdr">Conversations You've Created</font>
                        <br/><a href="/researcher/index.jsp"><font class="smallfont" style="font-weight: bold;">Get Started Creating a Conversation</font></a>
                    <%}%>
                    <!--</div>-->
                    <!--</div>-->



                    <%--<div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">--%>
                    <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">--%>
                    <br/><br/><br/>
                    <%if (Pagez.getUserSession().getUser().getResearcherid()>0){%>
                        <font class="largefont hdr">Twitter Questions You've Asked</font>
                        <br/>
                        <%if (researcherSurveyList.getSurveys()==null || researcherSurveyList.getSurveys().size()==0){%>
                            <font class="normalfont">You haven't yet asked any Twitter Questions. <a href="/researcher/researchertwitaskdetail_01.jsp">Ask one now!</a></font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                                cols.add(new GridCol("", "<a href=\"/twitask.jsp?twitaskid=<$twitask.twitaskid$>\"><font style=\"font-weight:bold;\"><$twitask.question$></font></a>", false, "background: #ffffff;", "normalfont"));
                                cols.add(new GridCol("", "<$status$>", false, "background: #ffffff;", "smallfont", "", ""));
                                cols.add(new GridCol("", "<$editorreviewlink$>", false, "background: #ffffff;", "smallfont"));
                                cols.add(new GridCol("", "<$resultslink$>", false, "background: #ffffff;", "smallfont"));
                                cols.add(new GridCol("", "<$deletelink$>", false, "background: #ffffff;", "smallfont"));
                            %>
                            <%=Grid.render(researcherSurveyList.getTwitasks(), cols, 15, "/account/index.jsp", "pagetwitaskcreated")%>
                        <%}%>
                    <%} else {%>
                        <font class="largefont hdr">Twitter Questions You've Asked</font>
                        <br/><a href="/researcher/index.jsp"><font class="smallfont" style="font-weight: bold;">Get Started as a Conversation Creator</font></a>
                    <%}%>
                    <!--</div>-->
                    <!--</div>-->


                </td>
            </tr>
        </table>



<%@ include file="/template/footer.jsp" %>


