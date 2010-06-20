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
%>
<%if (bloggerCompletedsurveys.getList()==null || bloggerCompletedsurveys.getList().size()==0){
    Pagez.sendRedirect("/publicsurveylist.jsp");
    return;
}%>

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
                <font class="mediumfont"><%=bloggerIndex.getResponsependingmsg()%></font>
            </div>
            <br/><br/>
        <%}%>

        <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="33%" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/publicsurveylist.jsp"><font class="mediumfont">Find <%=Pagez._Surveys()%><br/>to Enter</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Join <%=Pagez._surveys()%> and share your answers with your social network.</font>
                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            
                            <%if(Pagez.getUserSession().getPl().getIsreferralprogramon()){%>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/blogger/bloggerearningsrevshare.jsp"><font class="mediumfont">Invite Friends</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Invite friends to join <%=Pagez._surveys()%>!</font>
                            </td></tr></table>
                            <%}%>


                        </div>
                    </div>
                </td>
                <td valign="top">
                    
                    <font class="largefont hdr"><%=Pagez._Surveys()%> You've Joined</font>

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
                            <br/><font class="normalfont">You haven't yet entered any <%=Pagez._surveys()%>... <a href="/publicsurveylist.jsp">find some!</a></font>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                cols.add(new GridCol("", template.toString(), false, "", "tinyfont", "background: #ffffff;", ""));
                            %>
                            <%=Grid.render(bloggerCompletedsurveys.getList(), cols, 10, "/blogger/index.jsp", "page")%>
                        <%}%>




                </td>

            </tr>
        </table>
    <% } %>

<%@ include file="/template/footer.jsp" %>