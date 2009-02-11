<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicProfile" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.helpers.NicknameHelper" %>
<%@ page import="com.dneero.helpers.IsBloggerInPanel" %>
<%@ page import="com.dneero.privatelabel.PlPeers" %>
<%
    PublicTwitask publicTwitask = new PublicTwitask(); //Manually creating bean because had problem with redirect from TwitterQuestionServlet.java
    int twitaskid = 0;
    if (Num.isinteger(request.getParameter("twitaskid"))){
        twitaskid = Integer.parseInt(request.getParameter("twitaskid"));
    }
    publicTwitask.initBean(twitaskid);
%>
<%
if (publicTwitask==null || publicTwitask.getTwitask()==null || publicTwitask.getTwitask().getTwitaskid()==0 || publicTwitask.getTwitask().getStatus()==Twitask.STATUS_DRAFT || publicTwitask.getTwitask().getStatus()==Twitask.STATUS_REJECTED || publicTwitask.getTwitask().getStatus()==Twitask.STATUS_WAITINGFORSTARTDATE){
    Pagez.getUserSession().setMessage("TA status. request.getParameter(\"twitaskid\")="+request.getParameter("twitaskid"));
    if (publicTwitask.getTwitask()==null){
        Pagez.getUserSession().setMessage("publicTwitask.getTwitask()==null");
    } else if (publicTwitask.getTwitask().getTwitaskid()==0){
        Pagez.getUserSession().setMessage("publicTwitask.getTwitask().getTwitaskid()==0");
    } else {
        Pagez.getUserSession().setMessage("other");
    }
    Pagez.sendRedirect("/notauthorized.jsp");
    return;
}
//If the twitask isn't peered with this pl
Pl plOfTwitask = Pl.get(publicTwitask.getTwitask().getPlid());
if (!PlPeers.isThereATwoWayTrustRelationship(plOfTwitask, Pagez.getUserSession().getPl())){
    Pagez.getUserSession().setMessage("Pl peering conflict.");
    Pagez.sendRedirect("/notauthorized.jsp");
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Twitter Question";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("foo")) {
        try {

        } catch (Exception ex) {
            logger.error("", ex);
            Pagez.getUserSession().setMessage("There has been an error");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


        <%
        int peoplewhoanswered = 0;
        if (publicTwitask.getTwitanswers()!=null){
            peoplewhoanswered = publicTwitask.getTwitanswers().size();
        }
        %>
        <%
        int followersexposed = NumFromUniqueResult.getInt("select sum(twitterfollowerscount) from Twitanswer where twitaskid='"+publicTwitask.getTwitask().getTwitaskid()+"'");
        %>


        <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td valign="top">
                    <%--<div class="rounded" style="background: #e6e6e6; text-align: left; padding: 10px;">--%>
                    <div style="padding: 4px; background: #e6e6e6;"><font class="normalfont" style="color: #000000; font-size: 10px; font-weight: bold;"><font style="background: #ffffff; padding: 4px; font-size: 12px; color: #666666;"><%=peoplewhoanswered%></font> people answered <font style="background: #ffffff; margin: 4px; padding: 4px;  font-size: 12px; color: #666666;"><%=followersexposed%></font> Twitter followers saw it</font></div>
                    <br/><font class="mediumfont" style="color: #666666; font-size: 30px; font-weight: bold;"><%=publicTwitask.getTwitask().getQuestion()%></font>
                    <!--</div>-->
                    <br/><br/><br/>
                    <%if (publicTwitask.getTwitanswers()==null || publicTwitask.getTwitanswers().size()==0){%>
                        <font class="normalfont">Nobody's answered... yet.  Want to answer?  <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%>">http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%></a></li><li>reply to questions you see us ask</li></ol></font>
                   <%} else {%>
                        <%
                            StringBuffer template = new StringBuffer();
                            template.append("" +
                            "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                            "                <tr>\n" +
                            "                    <td valign=\"top\" width=\"50\">\n" +
                            "                        <a href=\"http://twitter.com/<$twitanswer.twitterusername$>\"><img src=\"<$twitanswer.twitterprofileimageurl$>\" border=\"0\"/></a>\n" +
                            "                    </td>\n" +
                            "                    <td valign=\"top\">\n" +
                            "     <font class=\"normalfont\" style=\"font-weight: bold;\"><a href=\"http://twitter.com/<$twitanswer.twitterusername$>\"><$twitanswer.twitterusername$></a> </font>\n" +
                            "     <font class=\"normalfont\" style=\"\"><$twitanswer.answer$></font>\n" +
                            "     <font class=\"normalfont\" style=\"color: #cccccc;\"><$twitanswer.twittercreatedate|"+Grid.GRIDCOLRENDERER_DATETIMEAGOTEXT+"$></font>\n" +
                            "                    </td>\n" +
                            "                </tr>\n" +
                            "            </table><br/><br/>\n");

                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("", template.toString(), false, "background: #ffffff;", ""));
                        %>
                        <%=Grid.render(publicTwitask.getTwitanswers(), cols, 200, "/twitask.jsp?twitaskid="+ publicTwitask.getTwitask().getTwitaskid(), "pagetwitanswers")%>
                    <%}%>
                </td>
                <td valign="top" width="270">
                    <div class="rounded" style="background: #00ff00; text-align: left; padding: 10px;">
                        <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                            <img src="/images/twitterbird.gif" width="250" height="157" alt=""><br/>
                            <font class="mediumfont" style="color: #cccccc;">Twitter Questions</font><br/>
                            <font class="tinyfont" style="color: #000000;">
                                A powerful and quick way for companies and organizations to engage an existing and productive panel of Twitter users in social market research.
                                <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%>">http://twitter.com/<%=Pagez.getUserSession().getPl().getTwitterusername()%></a></li><li>reply to questions you see us ask</li></ol>
                            </font>
                        </div>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">Who creates Twitter Questions?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">Any company or organization can create and launch a question using their dNeero.com account.  It takes minutes.</font>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">Where is the question announced?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">The question is posted to our Twitter account where anybody following us can see it.</font>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">How is the question answered?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">From within Twitter anybody can simply click reply and type their answer.  That's it.</font>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">How is this better than just asking it on Twitter without dNeero?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">There are a number of reasons to use dNeero.com's tools:
                            <ul>
                                <li>You gain access to an existing universe of Twitter users that we've carefully cultivated.</li>
                                <li>We have demographic information on people who respond and are awarded an incentive so you can slice and dice responses according to market segment.</li>
                                <li>You can offer cash, charity or coupon incentives to get people involved.</li>
                                <li>You can limit the number of people who are able to get the award incentive.  After that, it's just too late.</li>
                                <li>You get an aggregate results page to point people to.  All answers that people provided will be listed.</li>
                                <li>You can ask multiple questions at once and we'll take care of sorting it out.  With Twitter alone you'd end up with a single list of possibly-confusing answers (in Twitter they're called @replies).</li>
                            </ul>

                        </font>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">I've already got tons of Twitter users... can I pose the question on my own Twitter account?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">We do have a private label solution.  Let's talk: <a href="mailto:info@dneero.com">info@dneero.com</a></font>
                        <br/><br/>

                    </div>

                </td>
            </tr>
        </table>








    


<%@ include file="/template/footer.jsp" %>