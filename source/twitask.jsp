<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicProfile" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.helpers.NicknameHelper" %>
<%@ page import="com.dneero.helpers.IsBloggerInPanel" %>
<%
    PublicTwitask publicTwitask=(PublicTwitask) Pagez.getBeanMgr().get("PublicTwitask");
%>
<%
if (publicTwitask==null || publicTwitask.getTwitask()==null || publicTwitask.getTwitask().getTwitaskid()==0 || publicTwitask.getTwitask().getStatus()==Twitask.STATUS_DRAFT || publicTwitask.getTwitask().getStatus()==Twitask.STATUS_REJECTED || publicTwitask.getTwitask().getStatus()==Twitask.STATUS_WAITINGFORSTARTDATE){
    Pagez.sendRedirect("/notauthorized.jsp");
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Twitter Question:<br/>"+publicTwitask.getTwitask().getQuestion();
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





       <br/>
        <table cellpadding="10" cellspacing="0" border="0">
            <tr>
                <td valign="top" width="75%">
                    <%if (publicTwitask.getTwitanswers()==null || publicTwitask.getTwitanswers().size()==0){%>
                        <font class="normalfont">Nobody's answered... yet.  Want to answer?  <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/dNeero">http://twitter.com/dNeero</a></li><li>reply to questions you see us ask</li></ol></font>
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
                <td valign="top">
                    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                        <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                            <font class="mediumfont" style="font-color: #cccccc;">Twitter Questions</font><br/>
                            <font class="tinyfont" style="font-color: #000000;">
                                A powerful and quick way for companies and organizations to engage an existing productive panel of Twitter users in social market research.
                                <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/dNeero">http://twitter.com/dNeero</a></li><li>reply to questions you see us ask</li></ol>
                            </font>
                        </div>
                        <br/><br/>


                    </div>

                </td>
            </tr>
        </table>








    


<%@ include file="/template/footer.jsp" %>