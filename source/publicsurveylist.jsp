<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.facebook.FacebookSurveyThatsBeenTaken" %>
<%@ page import="com.dneero.facebook.FacebookSurveyTaker" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyFacebookFriendListitem" %>
<%@ page import="com.dneero.facebook.FacebookApiWrapper" %>
<%@ page import="com.dneero.facebook.FacebookUser" %>
<%@ page import="com.dneero.systemprops.SystemProperty" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.PublicIndex" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Conversations";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicSurveyList publicSurveyList = (PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList");
%>
<%
    if (request.getParameter("accesscode")!=null && !request.getParameter("accesscode").equals("")) {
        Pagez.getUserSession().setAccesscode(request.getParameter("accesscode"));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("enteraccesscode")) {
        try {
            PublicIndex publicIndex=new PublicIndex();
            publicIndex.enterAccessCode();
            Pagez.getUserSession().setMessage("Sorry, no conversations were found for that Access Code.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



    <% if (Pagez.getUserSession()!=null && Pagez.getUserSession().getIsfacebookui() && publicSurveyList.isFacebookjustaddedapp()){ %>
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
        <!--<div id='m3_tracker_4' style='position: absolute; left: 0px; top: 0px; visibility: hidden;'><img src=' http://www.trianads.com/adserver/www/delivery/ti.php?trackerid=4&amp;cb=<%=publicSurveyList.getRndstr()%>' width='0' height='0' alt='' /></div>-->
        <fb:iframe src="http://www.socialmedia.com/facebook/ppi.php?pubid=6e53651007583a6e10472e959e27fb67" border="0" width="1" height="1" scrolling="no" frameborder="0"/>
    <% } %>


    <table cellpadding="0" border="0" width="100%">
        <tr>
            <td valign="top">
                <%if (publicSurveyList.getSurveys()==null || publicSurveyList.getSurveys().size()==0){%>
                    <font class="normalfont">We've got nothin' for ya!  Please check back soon... we're always adding new conversations!</font>
                <%} else {%>
                    <%
                        StringBuffer srv = new StringBuffer();
                        srv.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
"                        <table cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
"                            <tr>\n" +
"                                <td>\n" +
"                                    <a href=\"/survey.jsp?surveyid=<$surveyid$>\"><font class=\"normalfont\" style=\"text-decoration: none; font-weight: bold; color: #0000ff;\"><$title$></font></a>\n"+
"                                    <font class=\"normalfont\"><$description$></font><br/><br/>\n" +
"                                    <$accessonlyhtml$> <font class=\"tinyfont\"><b><$daysuntilend$></b></font>\n" +
"                                </td>\n" +
"                                <td width=\"30%\">\n" +
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
                    <%=Grid.render(publicSurveyList.getSurveys(), cols, 100, "/publicsurveylist.jsp", "pagesurveys")%>
                <%}%>
                
            </td>

            <% if (Pagez.getUserSession()!=null && Pagez.getUserSession().getIsfacebookui()){ %>
                <td valign="top" width="50%" style="padding-top: 5px;">
                    <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                        <% if (1==1 || Pagez.getUserSession().getIsloggedin()){ %>
                            <div class="rounded" style="background: #ffffff; padding: 10px;">
                                <font class="formfieldnamefont" style="color: #666666;">Current Balance:</font>
                                <br/><font class="largefont" style="color: #cccccc;"><%=publicSurveyList.getCurrentbalance()%></font>
                                <% if (publicSurveyList!=null){ %>
                                    <% if (publicSurveyList.getPendingearningsDbl()>0){ %>
                                        <br/><a href="/blogger/bloggercompletedsurveys.jsp"><font class="formfieldnamefont" style="color: #0000ff;">Pending: <%=publicSurveyList.getPendingearnings()%></font></a>
                                    <% } %>
                                <% } %>
                                <br/><font class="tinyfont" style="color: #666666; font-weight: bold;">(yes, we're talking real world money here)</font>
                                <br/><font class="tinyfont" style="color: #666666;"><a href="/account/balancefaq.jsp">Balance Questions?</a></font>
                            </div>
                            <br/>
                        <% } %>

                        <%--<br/><br/>--%>
                        <%--<font class="mediumfont">Surveys Friends Have Taken:</font><br/>--%>
                        <%--<% if (publicSurveyList.getFacebookSurveyThatsBeenTakens()==null || publicSurveyList.getFacebookSurveyThatsBeenTakens().size()==0){ %>--%>
                            <%--<font class="tinyfont">Your friends haven't joined any conversations yet.</font>--%>
                        <%--<% } else { %>--%>
                            <%--<%--%>
                                <%--for (Iterator<FacebookSurveyThatsBeenTaken> iterator=publicSurveyList.getFacebookSurveyThatsBeenTakens().iterator(); iterator.hasNext();){--%>
                                    <%--FacebookSurveyThatsBeenTaken facebookSurveyThatsBeenTaken=iterator.next();--%>
                                    <%--%>--%>
                                    <%--<a href="/survey.jsp?surveyid=<%=facebookSurveyThatsBeenTaken.getSurvey().getSurveyid()%>"><font class="normalfont" style="font-weight: bold; color: #0000ff;"><%=facebookSurveyThatsBeenTaken.getSurvey().getTitle()%></font></a><br/>--%>
                                    <%--<font class="smallfont">--%>
                                        <%--How they answered:--%>
                                        <%--<%--%>
                                            <%--for (Iterator<FacebookSurveyTaker> iterator1=facebookSurveyThatsBeenTaken.getFacebookSurveyTakers().iterator(); iterator1.hasNext();){--%>
                                                <%--FacebookSurveyTaker facebookSurveyTaker=iterator1.next();--%>
                                                <%--%>--%>
                                                <%--<a href="/survey.jsp?surveyid=<%=facebookSurveyThatsBeenTaken.getSurvey().getSurveyid()%>&userid=<%=facebookSurveyTaker.getUserid()%>&responseid=<%=facebookSurveyTaker.getResponseid()%>"><font class=""><%=facebookSurveyTaker.getFacebookUser().getFirst_name()%> <%=facebookSurveyTaker.getFacebookUser().getLast_name()%></font></a>--%>
                                                <%--<%--%>
                                            <%--}--%>
                                            <%--%>--%>
                                    <%--</font><br/><br/>--%>
                                    <%--<%--%>
                                <%--}--%>

                            <%--%>--%>
                        <%--<%}%>--%>

                        <%--<br/><br/>--%>
                        <%--<font class="mediumfont">Friends on dNeero:</font><br/>--%>
                        <%--<% if (publicSurveyList.getFacebookuserswhoaddedapp()==null || publicSurveyList.getFacebookuserswhoaddedapp().size()==0){ %>--%>
                            <%--<font class="tinyfont">None, yet.</font><br/>--%>
                        <%--<% } else { %>--%>
                            <%--<table cellpadding="0" cellspacing="0" border="0">--%>
                                <%--<%--%>
                                    <%--int colsperrow = 4;--%>
                                    <%--int col = 1;--%>
                                    <%--for (Iterator<PublicSurveyFacebookFriendListitem> iterator=publicSurveyList.getFacebookuserswhoaddedapp().iterator(); iterator.hasNext();){--%>
                                        <%--PublicSurveyFacebookFriendListitem publicSurveyFacebookFriendListitem=iterator.next();--%>
                                        <%--%>--%>
                                        <%--<%if (col==1){%><tr><%}%>--%>
                                        <%--<td valign="top">--%>
                                            <%--<img src="<%=publicSurveyFacebookFriendListitem.getFacebookUser().getPic_square()%>" width="50" height="50" border="0" align="middle" alt=""/><br/>--%>
                                            <%--<font class="tinyfont" style="font-weight: bold;"><%=publicSurveyFacebookFriendListitem.getFacebookUser().getFirst_name()%> <%=publicSurveyFacebookFriendListitem.getFacebookUser().getLast_name()%></font><br/><br/>--%>
                                        <%--</td>--%>
                                        <%--<%--%>
                                        <%--if (col==colsperrow){--%>
                                            <%--%></tr><%--%>
                                            <%--col = 0;--%>
                                        <%--}--%>
                                        <%--col = col + 1;--%>
                                    <%--}--%>
                                <%--%>--%>
                            <%--</table>--%>
                        <%--<%}%>--%>

                        <br/><br/>
                        <div class="rounded" style="background: #ffffff; padding: 10px;">
                            <form action="/publicsurveylist.jsp" method="post">
                                <input type="hidden" name="action" value="enteraccesscode">
                                <font class="normalfont"><b>Got an Access Code?</b></font>
                                <%=Textbox.getHtml("accesscode", Pagez.getUserSession().getAccesscode(), 255, 10, "", "")%>
                                <input type="submit" class="formsubmitbutton" value="Go">
                            </form>
                        </div>
                    </div>
                </td>
            <% } %>

        </tr>
    </table>

    
    <%--<br/>--%>
        <%--<%--%>
            <%--//Will need this throughout the page--%>
            <%--ArrayList<FacebookUser> friends=Pagez.getUserSession().getFacebookFriends();--%>
            <%--//Create comma-separated list of friends who have app installed--%>
            <%--StringBuffer commaSepFriendsAlreadyUsingApp=new StringBuffer();--%>
            <%--ArrayList<FacebookUser> friendsUsingApp=new ArrayList<FacebookUser>();--%>
            <%--if (friends != null) {--%>
                <%--for (Iterator it=friends.iterator(); it.hasNext();) {--%>
                    <%--FacebookUser facebookUser=(FacebookUser) it.next();--%>
                    <%--if (facebookUser.getHas_added_app()) {--%>
                        <%--friendsUsingApp.add(facebookUser);--%>
                        <%--if (commaSepFriendsAlreadyUsingApp.length()>0) {--%>
                            <%--commaSepFriendsAlreadyUsingApp.append(",");--%>
                        <%--}--%>
                        <%--commaSepFriendsAlreadyUsingApp.append(facebookUser.getUid());--%>
                    <%--}--%>
                <%--}--%>
            <%--}--%>
        <%--%>--%>

    <%--<fb:request-form--%>
        <%--action="http://apps.facebook.com/<%=SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)%>/?dpage=/publicsurveylist.jsp"--%>
        <%--method="POST"--%>
        <%--invite="true"--%>
        <%--type="dNeero Survey"--%>
        <%--content="You've been invited to the social survey app called dNeero that allows you to earn real money taking surveys and sharing your answers with your friends. <fb:req-choice url='http://www.facebook.com/add.php?api_key=<%=SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY)%>' label='Check out dNeero!' />--%>
    <%--">--%>
        <%--<fb:multi-friend-selector--%>
            <%--showborder="false"--%>
            <%--actiontext="Invite friends to dNeero."--%>
            <%--exclude_ids="<%=commaSepFriendsAlreadyUsingApp.toString()%>"--%>
            <%--rows="3"--%>
            <%--max="20"--%>
            <%--/>--%>
    <%--</fb:request-form>--%>




<%@ include file="/template/footer.jsp" %>



