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
String pagetitle = "";
String navtab = "bloggers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    BloggerIndex bloggerIndex=(BloggerIndex) Pagez.getBeanMgr().get("BloggerIndex");
    BloggerCompletedsurveys bloggerCompletedsurveys = (BloggerCompletedsurveys) Pagez.getBeanMgr().get("BloggerCompletedsurveys");
    BloggerCompletedTwitasks bloggerCompletedTwitasks = (BloggerCompletedTwitasks) Pagez.getBeanMgr().get("BloggerCompletedTwitasks");
%>
<%@ include file="/template/header.jsp" %>




    

    <% if (Pagez.getUserSession().getIsloggedin() && (Pagez.getUserSession().getUser().getBloggerid() > 0)){ %>
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
                <td width="33%" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/publicsurveylist.jsp"><font class="mediumfont" style="color: #596697;">Find Conversations<br/>to Enter</font></a>
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
                                <a href="/blogger.jsp"><font class="mediumfont" style="color: #596697;">Blogger Basic Info</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Basic Blogger information, how the system works, etc.</font>
                            </td></tr></table>
                        </div>
                    </div>
                </td>
                <td valign="top">
                    
                    <font class="largefont hdr">Conversations You've Entered</font>

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
                            <br/><font class="normalfont">You haven't yet entered any conversations... <a href="/publicsurveylist.jsp">find some!</a></font>
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
                        <font class="largefont hdr">Twitter Questions Answered</font>
                        <%if (bloggerCompletedTwitasks.getTwitanswers()==null || bloggerCompletedTwitasks.getTwitanswers().size()==0){%>
                            <br/><font class="normalfont">You haven't yet responded to any Twitter Questions!  <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%>">http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%></a></li><li>reply to questions you see us ask</li></ol></font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                cols.add(new GridCol("", taTemplate.toString(), false, "", "tinyfont", "background: #ffffff;", ""));
                            %>
                            <%=Grid.render(bloggerCompletedTwitasks.getTwitanswers(), cols, 10, "/blogger/index.jsp", "pagetwitanswers")%>
                            <br/><a href="/blogger/bloggercompletedtwitasks.jsp"><font class="smallfont" style="font-weight: bold;">See All Twitter Questions You've Answered</font></a>
                        <%}%>

                    <%if (1==2 && bloggerCompletedsurveys.getList()!=null && bloggerCompletedsurveys.getList().size()>0){%>
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