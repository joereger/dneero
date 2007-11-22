<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.facebook.FacebookSurveyThatsBeenTaken" %>
<%@ page import="com.dneero.facebook.FacebookSurveyTaker" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyFacebookFriendListitem" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Social Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicSurveyList publicSurveyList = (PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList");
%>
<%@ include file="/template/header.jsp" %>



    <% if (Pagez.getUserSession().getIsfacebookui() && publicSurveyList.isFacebookjustaddedapp()){ %>
        <!--/*
          *
          *  If this tag is being served on a secure (SSL) page, you must replace
          *  'http://www.trianads.com/adserver/www/delivery/.. .'
          * with
          *  'https://www.trianads.com/adserver/www/delivery/...'
          *
          *  To help prevent caching of this tracker beacon, if possible,
          *  Replace %%RANDOM_NUMBER%% with a randomly generated number (or timestamp)
          *
          *  Place this code at the top of your post-add URL page, just after the <body> tag.
          *
          */-->
        <div id='m3_tracker_4' style='position: absolute; left: 0px; top: 0px; visibility: hidden;'><img src=' http://www.trianads.com/adserver/www/delivery/ti.php?trackerid=4&amp;cb=<%=publicSurveyList.getRndstr()%>' width='0' height='0' alt='' /></div>
    <% } %>


    <table cellpadding="0" border="0" width="100%">
        <tr>
            <td valign="top">
                <%if (publicSurveyList.getSurveys()==null || publicSurveyList.getSurveys().size()==0){%>
                    <font class="normalfont">There are currently no surveys listed.  Please check back soon as we're always adding new ones!</font>
                <%} else {%>
                    <%
                        StringBuffer srv = new StringBuffer();
                        srv.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
"                        <table cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
"                            <tr>\n" +
"                                <td>\n" +
"                                    <a href=\"survey.jsp?surveyid=<$surveyid$>\"><font class=\"normalfont\" style=\"text-decoration: none; font-weight: bold; color: #0000ff;\"><$title$></font></a>\n"+
"                                    <font class=\"tinyfont\"><$description$></font><br/><br/>\n" +
"                                    <font class=\"tinyfont\"><b><$daysuntilend$></b></font>\n" +
"                                </td>\n" +
"                                <td width=\"25%\">\n" +
"                                    <div class=\"rounded\" style=\"background: #ffffff; padding: 10px;\">\n" +
"                                        <center>\n" +
"                                            <font class=\"tinyfont\"><b>Earn Up To</b></font><br/>\n" +
"                                            <font class=\"mediumfont\"><$maxearning$></font>\n" +
"                                        </center>\n" +
"                                    </div>\n" +
"                                </td>\n" +
"                            </tr>\n" +
"                        </table>\n" +
"                    </div>");
                        ArrayList<GridCol> cols=new ArrayList<GridCol>();
                        cols.add(new GridCol("", srv.toString(), false, "", "", "background: #ffffff;", ""));
                    %>
                    <%=Grid.render(publicSurveyList.getSurveys(), cols, 100, "publicsurveylist.jsp", "pagesurveys")%>
                <%}%>
                
            </td>

            <% if (Pagez.getUserSession().getIsfacebookui()){ %>
                <td valign="top" width="50%" style="padding-top: 6px;">
                    <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                        <% if (1==1 || Pagez.getUserSession().getIsloggedin()){ %>
                            <div class="rounded" style="background: #ffffff; padding: 10px;">
                                <font class="formfieldnamefont" style="color: #666666;">Current Balance:</font>
                                <br/><font class="largefont" style="color: #cccccc;"><%=publicSurveyList.getAccountBalance().getCurrentbalance()%></font>
                                <% if (publicSurveyList.getAccountBalance()!=null){ %>
                                    <% if (publicSurveyList.getAccountBalance().getPendingearningsDbl()>0){ %>
                                        <br/><a href="/blogger/bloggercompletedsurveys.jsp"><font class="formfieldnamefont" style="color: #0000ff;">Pending: <%=publicSurveyList.getAccountBalance().getPendingearnings()%></font></a>
                                    <% } %>
                                <% } %>
                                <br/><font class="tinyfont" style="color: #666666; font-weight: bold;">(yes, we're talking real world money here)</font>
                            </div>
                            <br/><br/>
                        <% } %>




                        <font class="mediumfont">Surveys Friends Have Taken:</font><br/>
                        <% if (publicSurveyList.getFacebookSurveyThatsBeenTakens()==null || publicSurveyList.getFacebookSurveyThatsBeenTakens().size()==0){ %>
                            <font class="tinyfont">Your friends haven't taken any surveys yet.</font>
                        <% } else { %>
                            <%
                                for (Iterator<FacebookSurveyThatsBeenTaken> iterator=publicSurveyList.getFacebookSurveyThatsBeenTakens().iterator(); iterator.hasNext();){
                                    FacebookSurveyThatsBeenTaken facebookSurveyThatsBeenTaken=iterator.next();
                                    %>
                                    <a href="survey.jsp?surveyid=<%=facebookSurveyThatsBeenTaken.getSurvey().getSurveyid()%>"><font class="normalfont" style="font-weight: bold; color: #0000ff;"><%=facebookSurveyThatsBeenTaken.getSurvey().getTitle()%></font></a><br/>
                                    <font class="smallfont">
                                        How they answered:
                                        <%
                                            for (Iterator<FacebookSurveyTaker> iterator1=facebookSurveyThatsBeenTaken.getFacebookSurveyTakers().iterator(); iterator1.hasNext();){
                                                FacebookSurveyTaker facebookSurveyTaker=iterator1.next();
                                                %>
                                                <a href="survey.jsp?surveyid=<%=facebookSurveyThatsBeenTaken.getSurvey().getSurveyid()%>&userid=<%=facebookSurveyTaker.getUserid()%>&responseid=<%=facebookSurveyTaker.getResponseid()%>"><font class=""><%=facebookSurveyTaker.getFacebookUser().getFirst_name()%> <%=facebookSurveyTaker.getFacebookUser().getLast_name()%></font></a>
                                                <%
                                            }
                                            %>
                                    </font><br/><br/>
                                    <%
                                }

                            %>
                        <%}%>

                        <br/><br/>
                        <font class="mediumfont">Friends on dNeero:</font><br/>
                        <% if (publicSurveyList.getFacebookuserswhoaddedapp()==null || publicSurveyList.getFacebookuserswhoaddedapp().size()==0){ %>
                            <font class="tinyfont">None, yet.</font><br/>
                        <% } else { %>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                <%
                                    for (Iterator<PublicSurveyFacebookFriendListitem> iterator=publicSurveyList.getFacebookuserswhoaddedapp().iterator(); iterator.hasNext();){
                                        PublicSurveyFacebookFriendListitem publicSurveyFacebookFriendListitem=iterator.next();
                                        %>
                                        <td valign="top">
                                            <img src="<%=publicSurveyFacebookFriendListitem.getFacebookUser().getPic_square()%>" width="50" height="50" border="0" align="middle" alt=""/><br/>
                                            <font class="tinyfont" style="font-weight: bold;"><%=publicSurveyFacebookFriendListitem.getFacebookUser().getFirst_name()%> <%=publicSurveyFacebookFriendListitem.getFacebookUser().getLast_name()%></font><br/><br/>
                                        </td>
                                        <%
                                    }
                                %>
                                </tr>
                            </table>
                        <%}%>
                        <br/>
                        <center><a href="<%=publicSurveyList.getInvitefriendsurl()%>" target="top"><font class="normalfont" style="font-weight: bold; color: #0000ff;">Invite Friends Who Aren't On dNeero</font></a></center>
                        <br/><br/>
                    </div>
                </td>
            <% } %>

        </tr>
    </table>



<%@ include file="/template/footer.jsp" %>



