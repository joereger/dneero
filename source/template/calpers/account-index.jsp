<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountIndex" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
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
    Pagez.getUserSession().setWhereToRedirectToAfterSignup("");
    Pagez.sendRedirect("/blogger/index.jsp");
    return;
}
%>
<%
if (Pagez.getUserSession().getWhereToRedirectToAfterSignup()!=null && !Pagez.getUserSession().getWhereToRedirectToAfterSignup().equals("")){
    String whereToRedirectToAfterSignup = Pagez.getUserSession().getWhereToRedirectToAfterSignup();
    Pagez.getUserSession().setWhereToRedirectToAfterSignup("");
    Pagez.sendRedirect(whereToRedirectToAfterSignup);
    return;
}
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



       <%if (!accountIndex.getMsg().equals("")) {%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont"><%=accountIndex.getMsg()%></font>
            </div>
       <%}%>





       <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="33%" valign="top">
                    <br/><br/><br/>
                    <center>
                    <font class="largefont hdr">Find <%=Pagez._Surveys()%> to Join</font>
                    <br/><br/>
                    <a href="/"><img src="/images/play-128.png" alt="" width="128" height="128" border="0"/></a>
                    <br/>
                    <font class="smallfont">See a <a href="/">list of active conversations</a> and the Values Super Jam Blog.</font>
                    </center>
                </td>
                <td width="67%" valign="top">
                    <%if (Pagez.getUserSession().getUser().getBloggerid()>0){%>
                        <br/><br/><br/>
                        <font class="largefont hdr"><%=Pagez._Surveys()%> You've Joined</font>
                        <br/>
                        <%
                            BloggerCompletedsurveys bloggerCompletedsurveys = new BloggerCompletedsurveys();
                            bloggerCompletedsurveys.setMaxtodisplay(25);
                            bloggerCompletedsurveys.initBean();
                            StringBuffer template = new StringBuffer();

                            template.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
    "                        <table cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
    "                            <tr>");
                                     template.append("<td width=\"10%\">");
                                     template.append("<img src=\"/images/ok-32.png\" alt=\"\" width=\"32\" height=\"32\"/><br/><font class=\"tinyfont\"><b>Joined</b></font>");
                                     template.append("</td>");
                                     template.append("<td>\n" +
    "                                    <a href=\"/survey.jsp?surveyid=<$surveyid$>\"><font class=\"normalfont\" style=\"text-decoration: none; font-weight: bold; color: #0000ff;\"><$surveytitle$></font></a>\n"+
    "                                    <br/><font class=\"tinyfont\"><b><a href=\"/survey.jsp?surveyid=<$surveyid$>\">Edit Your Answers</a></b></font>\n" +
    "                                </td>");
                             template.append("</tr>\n" +
    "                        </table>\n" +
    "                    </div>");
                        %>
                        <%if (bloggerCompletedsurveys.getList()==null || bloggerCompletedsurveys.getList().size()==0){%>
                            <font class="smallfont">You haven't joined any <%=Pagez._surveys()%>.</font>
                            <br/><a href="/publicsurveylist.jsp"><font class="smallfont" style="font-weight: bold;">Find <%=Pagez._Surveys()%> to Join</font></a>
                        <%} else {%>
                            <%
                                ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                cols.add(new GridCol("", template.toString(), false, "", "", "background:#ffffff;", ""));
                            %>
                            <%=Grid.render(bloggerCompletedsurveys.getList(), cols, 25, "/account/index.jsp", "pageyourconvos")%>
                        <%}%>
                    <%}%>



                </td>
            </tr>
        </table>



<%@ include file="/template/footer.jsp" %>


