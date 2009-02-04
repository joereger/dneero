<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveys" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveysListitem" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Twitter Questions You've Answered";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/template/auth.jsp" %>
<%
    BloggerCompletedTwitasks bloggerCompletedTwitasks = (BloggerCompletedTwitasks) Pagez.getBeanMgr().get("BloggerCompletedTwitasks");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("handleaward")) {
        try {
            if (request.getParameter("twitanswerid")!=null && Num.isinteger(request.getParameter("twitanswerid"))){
                Twitanswer twitanswer = Twitanswer.get(Integer.parseInt(request.getParameter("twitanswerid")));
                if (twitanswer.getUserid()==Pagez.getUserSession().getUser().getUserid()){
                    //Note that this same if statement appears on BloggerCompletedTwitasks.java and bloggercompletedtwitasks.jsp
                    if (twitanswer.getStatus()==Twitanswer.STATUS_APPROVED && !twitanswer.getIspaid() && !twitanswer.getIssysadminrejected() && twitanswer.getIscriteriaxmlqualified()){
                        //Set the coupon stuff
                        String charityname = "";
                        if (request.getParameter("charity-charityname")!=null){
                            charityname = request.getParameter("charity-charityname");  
                        }
                        twitanswer.setCharityname(charityname);
                        twitanswer.setIsforcharity(false);
                        if (request.getParameter("charity-isforcharity")!=null && request.getParameter("charity-isforcharity").equals("1")){
                            twitanswer.setIsforcharity(true);
                        }
                        try{twitanswer.save();}catch(Exception ex){logger.error("",ex);}
                        //Award the incentive
                        twitanswer.getIncentive().doAwardIncentive(twitanswer);
                        //Update paid status
                        twitanswer.setIspaid(true);
                        try{twitanswer.save();}catch(Exception ex){logger.error("",ex);}
                        //Refresh the bean
                        bloggerCompletedTwitasks.initBean();
                    }
                }
            }
            Pagez.getUserSession().setMessage("Done!  Thanks!");
        } catch (Exception ex) {
            Pagez.getUserSession().setMessage("Sorry, there was an error.");
            logger.error("", ex);
        }
    }
%>
<%@ include file="/template/header.jsp" %>

   <%
            StringBuffer taTemplate = new StringBuffer();
            taTemplate.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
    "            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
    "                <tr>\n" +
    "                    <td valign=\"top\">\n" +
    "                        <font class=\"tinyfont\"><$twitanswer.twittercreatedate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$></font><br/>\n" +
    "                        <font class=\"mediumfont\" style=\"font-weight: bold; color: #00ff00;\"><a href=\"/twitask.jsp?twitaskid=<$twitask.twitaskid$>\"><$twitask.question$></a></font><br/>\n" +
    "                        <font class=\"normalfont\" style=\"font-weight: bold;\">Your Answer: <$twitanswer.answer$></font><br/>\n" +
    "                        <font class=\"tinyfont\" style=\"font-weight:bold;\">Incentive: <$earningsTxt$></font><br/>\n" +
    "                        <font class=\"tinyfont\" style=\"font-weight:bold;\">Status: <$statusTxt$></font><br/>\n" +
    "                        <font class=\"tinyfont\" style=\"font-weight:bold;\"><$htmlpayform$></font>\n" +
    "                    </td>\n" +
    "                </tr>\n" +
    "            </table>\n" +
    "        </div>");
        %>

        <br/><br/>
        <%if (bloggerCompletedTwitasks.getTwitanswers()==null || bloggerCompletedTwitasks.getTwitanswers().size()==0){%>
            <br/><font class="normalfont">You haven't yet responded to any Twitter Questions!  <ol><li>Add your <a href="http://twitter.com">Twitter</a> username to <a href="/account/accountsettings.jsp">your account</a></li><li>follow us at <a href="http://twitter.com/dNeero">http://twitter.com/dNeero</a></li><li>reply to questions you see us ask</li></ol></font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                cols.add(new GridCol("", taTemplate.toString(), false, "", "tinyfont", "background: #ffffff;", ""));
            %>
            <%=Grid.render(bloggerCompletedTwitasks.getTwitanswers(), cols, 100, "/blogger/bloggercompletedtwitasks.jsp", "pagetwitanswers")%>
        <%}%>

<%@ include file="/template/footer.jsp" %>

