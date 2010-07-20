<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicProfile" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.helpers.NicknameHelper" %>
<%@ page import="com.dneero.helpers.IsBloggerInPanel" %>
<%@ page import="com.dneero.privatelabel.PlPeers" %>
<%String jspPageName="/twitask.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
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
String pagetitle = publicTwitask.getTwitask().getQuestion();
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
                    <%--<br/><font class="mediumfont" style="color: #666666; font-size: 30px; font-weight: bold;"><%=publicTwitask.getTwitask().getQuestion()%></font>--%>
                    <!--</div>-->
                    <br/><br/><br/>
                    <%if (publicTwitask.getTwitanswers()==null || publicTwitask.getTwitanswers().size()==0){%>
                        <font class="normalfont">Nobody's answered... yet.  Want to answer?  View <a href="http://twitter.com/<%=publicTwitask.getTwitask().getTwitterusername()%>/status/<%=publicTwitask.getTwitask().getTwitterid()%>" target="_new">the question in Twitter</a> and click Reply.</font>
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
                    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 10px;">
                        <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                            <font class="mediumfont" style="color: #333333;">Twitter Question</font><br/>
                            <br/>
                            <img src="/images/twitterbird.gif" width="250" height="157" alt=""><br/>
                            <br/><br/>
                            <font class="mediumfont" style="color: #cccccc;">Answer this Question?</font><br/>
                            <font class="tinyfont" style="color: #000000;">
                                View <a href="http://twitter.com/<%=publicTwitask.getTwitask().getTwitterusername()%>/status/<%=publicTwitask.getTwitask().getTwitterid()%>" target="_new">the question in Twitter</a> and click Reply.
                            </font>
                            <br/><br/>
                            <font class="mediumfont" style="color: #cccccc;">Link to This Page</font><br/>
                            <font class="tinyfont" style="color: #000000;">
                                <textarea rows="2" cols="30" name="linkurl" id="linkurl" style="font-size:9px; width: 100%;" onclick="javascript:document.getElementById('linkurl').select();"><%=BaseUrl.get(false, Pagez.getUserSession().getPl().getPlid())%>twitask.jsp?twitaskid=<%=publicTwitask.getTwitask().getTwitaskid()%></textarea>                                
                                Paste into an email, blog, social network, etc.
                            </font>
                        </div>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">Who creates Twitter Questions?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">Any person, company or organization can ask their Twitter followers a question.  It takes seconds.</font>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">Where is the question announced?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">The question is posted to your Twitter account where anybody following you can see it.</font>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">How is the question answered?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">From within Twitter anybody can simply click reply and type their answer.  That's it.</font>
                        <br/><br/>
                        <font class="tinyfont" style="font-color: #000000; font-weight: bold;">For how long are answers collected?</font><br/>
                        <font class="tinyfont" style="font-color: #000000;">Three days.</font>
                        <br/><br/>


                    </div>

                </td>
            </tr>
        </table>








    


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>