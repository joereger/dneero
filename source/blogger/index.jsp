<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.BloggerIndex" %>
<%@ page import="com.dneero.htmluibeans.SystemStats" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveys" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveysListitem" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/user.png\" alt=\"\" border=\"0\" width=\"128\" height=\"128\" align=\"right\"/>Social People<br/><br clear=\"all\"/>";
String navtab = "bloggers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    BloggerIndex bloggerIndex=(BloggerIndex) Pagez.getBeanMgr().get("BloggerIndex");
    SystemStats systemStats=((SystemStats) Pagez.getBeanMgr().get("SystemStats"));
    BloggerCompletedsurveys bloggerCompletedsurveys = (BloggerCompletedsurveys) Pagez.getBeanMgr().get("BloggerCompletedsurveys");
    BloggerCompletedTwitasks bloggerCompletedTwitasks = (BloggerCompletedTwitasks) Pagez.getBeanMgr().get("BloggerCompletedTwitasks");
%>
<%@ include file="/template/header.jsp" %>





    <% if (!Pagez.getUserSession().getIsloggedin() || bloggerIndex.getShowmarketingmaterial()){ %>

        <table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="70%">
                    <div style="margin: 15px;">
                        <font class="mediumfont" style="color: #999999">What're Social People?</font>
                        <br/>
                        Anybody who engages in social activity online.  Bloggers, Facebookers, Myspacers and other social network participants.  Social People is a catch-all term.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Quick Summary</font>
                        <br/>
                        We help social people make money joining conversations and posting their opinions where their peers can join in.
                        <br/><br/>
                        After filling out a questions here at this site you've already reserved some money for your effort... but haven't earned it yet.  To capture that money all you have to do is post your answers to your blog and generate some traffic for a few days (all you have to do is copy-and-paste a single line of code).  Your blog readers will see your answers along with any other thoughts you had about the conversation.  Your readers can then join the conversation, or see how others from your blog answered. If they join the conversation and get paid, we pay you a recruitment fee based on what they earn.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How it Works for Social People</font>
                        <br/>
                        You find conversations that interest you.  You answer a set of questions honestly.  You post your answers in a place where your peers can see them (we have a widget that's a simple copy and paste).  Your friends engage in the conversation.  You get paid. 
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Blogs and Conversations: So Happy Together</font>
                        <br/>
                        To post a conversation to your blog you just copy-and-paste a single line of code.  Here's the result:
                        <br/>
                        <img src="/images/survey-in-blog.gif" width="475" height="555" border="0"></img>
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Avoid Losing Any Credibility... and Support a Great Cause!</font>
                        <br/>
                        One of the big concerns from bloggers is that the money in this model presents possible bias, and that your readers will not want to feel 'monetized'. Thanks to open and sharing people like <a href="http://www.ck-blog.com/">CK</a> we decided to make donating to charity a way to shed this concern -- now you can give any of the earnings to a good cause, avoid bias... and increase the features within your posts!
                        <br/><br/>
                        With a single click you can direct earnings from any conversation to a charity of your choosing.  Learn more about the program <a href="/charity.jsp">here</a>.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How to Get Started</font>
                        <br/>
                        Sign Up is free.  We collect some basics like email and password and then send you an email message to activate your account.  After your first log in we'll collect some basic demographic information (age, gender, location, etc.) so that we can find the best opportunities for you.  We'll present you a list of conversation opportunities, you'll join conversations of your choosing and within minutes you'll see an account balance.  We pay your PayPal account whenever you accrue $20 in your account... no waiting unitl the end of calendar quarters!
                    </div>
                    <table width="100%">
                        <tr>
                            <td width="50%" align="center">
                                <div style="width: 200px;"><%=GreenRoundedButton.get("<a href=\"/blogger/bloggerfaq.jsp\"><font class=\"subnavfont\" style=\"color: #ffffff;\">Read the Blogger FAQ</font></a>")%></div>
                            </td>
                            <td width="50%" align="center">
                                <%if(!Pagez.getUserSession().getIsloggedin()){%>
                                    <div style="width: 200px;"><%=GreenRoundedButton.get("<a href=\"/registration.jsp\"><font class=\"subnavfont\" style=\"color: #ffffff;\">Sign Up Now</font></a>")%></div>
                                <%}%>
                            </td>
                        </tr>
                    </table>
               </td>
               <td valign="top" width="30%">

               </td>
           </tr>
        </table>


        <br/>


    <%}%>

    

    <% if (Pagez.getUserSession().getIsloggedin() && (Pagez.getUserSession().getUser().getBloggerid() > 0) && (!bloggerIndex.getShowmarketingmaterial())){ %>
        <%if (bloggerIndex.getMsg()!=null && !bloggerIndex.getMsg().equals("")){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont"><%=bloggerIndex.getMsg()%></font>
            </div>
        <%}%>

        <%if (bloggerIndex.getResponsependingmsg()!=null && !bloggerIndex.getResponsependingmsg().equals("")){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <img src="/images/alert.png" border="0" align="right"/>
                <font class="mediumfont"><f:verbatim escape="false"><%=bloggerIndex.getResponsependingmsg()%></f:verbatim></font>
            </div>
            <br/><br/>
        <%}%>

        <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="250" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
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
                                <a href="/blogger/index.jsp"><font class="mediumfont" style="color: #596697;">Joined Conversations</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">See how much you've earned.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/blogger/bloggercompletedtwitasks.jsp"><font class="mediumfont" style="color: #596697;">Answered Twitter Questions</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Check the status of Twitter Questions.</font>
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
                <td valign="top">
                    
                    <font class="largefont" style="color: #cccccc;">Joined Conversations</font>

                    <%
                            StringBuffer template = new StringBuffer();
                            template.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
                    "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                    "                <tr>\n" +
                    "                    <td valign=\"top\">\n" +
                    "                        <font class=\"tinyfont\"><$responsedate$></font><br/>\n" +
                    "                        <font class=\"normalfont\" style=\"font-weight: bold; color: #0000ff;\"><a href=\"/surveypostit.jsp?surveyid=<$surveyid$>\"><$surveytitle$></a></font><br/>\n" +
                    "                        <br/>\n" +
                    "                        <font class=\"tinyfont\" style=\"font-weight:bold; text-decoration: none;\"><a href=\"/blogger/impressions.jsp?surveyid=<$surveyid$>\">Impressions</a> | <a href=\"/survey.jsp?surveyid=<$surveyid$>\">Edit Answers</a></font>\n" +
                    "                    </td>\n" +
                    "                    <td valign=\"top\" width=\"225\">\n" +
                    "                        <$response.responsestatushtml$>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        </div>");
                        %>



                        <%if (bloggerCompletedsurveys.getList()==null || bloggerCompletedsurveys.getList().size()==0){%>
                            <font class="normalfont">You haven't yet joined any conversations... <a href="/publicsurveylist.jsp">find some!</a></font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                cols.add(new GridCol("", template.toString(), false, "", "tinyfont", "background: #ffffff;", ""));
                            %>
                            <%=Grid.render(bloggerCompletedsurveys.getList(), cols, 10, "/blogger/index.jsp", "page")%>
                        <%}%>


                    <%
                            StringBuffer taTemplate = new StringBuffer();
                            taTemplate.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
                    "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                    "                <tr>\n" +
                    "                    <td valign=\"top\">\n" +
                    "                        <font class=\"tinyfont\"><$twitanswer.twittercreatedate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$></font><br/>\n" +
                    "                        <font class=\"normalfont\" style=\"font-weight: bold; color: #0000ff;\"><a href=\"/twitask.jsp?twitaskid=<$twitask.twitaskid$>\"><$twitask.question$></a></font><br/>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        </div>");
                        %>

                        <br/><br/><br/>
                        <font class="largefont" style="color: #cccccc;">Twitter Questions Answered</font>
                        <%if (bloggerCompletedTwitasks.getTwitanswers()==null || bloggerCompletedTwitasks.getTwitanswers().size()==0){%>
                            <br/><font class="normalfont">You haven't yet responded to any Twitter Questions!  <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/dNeero">http://twitter.com/dNeero</a></li><li>reply to questions you see us ask</li></ol></font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                cols.add(new GridCol("", taTemplate.toString(), false, "", "tinyfont", "background: #ffffff;", ""));
                            %>
                            <%=Grid.render(bloggerCompletedTwitasks.getTwitanswers(), cols, 10, "/blogger/index.jsp", "pagetwitanswers")%>
                            <br/><a href="/blogger/bloggercompletedtwitasks.jsp"><font class="smallfont" style="font-weight: bold;">See All Twitter Questions You've Answered</font></a>
                        <%}%>

                    <br/><br/><br/>
                    <font class="tinyfont" style="color: #666666;">Conversation statuses update nightly. Remember, you must leave the conversation on your mini-feed and profile to generate clicks for 5 days in the 10 after you take it to get paid.  Days that qualify are marked green.</font>


                    <%if (bloggerCompletedsurveys.getList()!=null && bloggerCompletedsurveys.getList().size()>0){%>
                        <br/><br/>
                        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
                        Note: Earnings calculations are estimated and not final.   Final payment notification and calculation can be found on <a href="/account/accountbalance.jsp">Your Account Balance</a> page. Posting and payment status both update nightly.
                        </font></div></center>
                    <%}%>

                </td>

            </tr>
        </table>
    <% } %>

<%@ include file="/template/footer.jsp" %>