<%@ page import="com.dneero.helpers.ResearcherCreateIfNeeded" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "researchers";
String acl = "public";
%>
<%
    ResearcherCreateIfNeeded.createIfNecessary();
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherIndex researcherIndex=(ResearcherIndex) Pagez.getBeanMgr().get("ResearcherIndex");
    ResearcherSurveyList researcherSurveyList=(ResearcherSurveyList) Pagez.getBeanMgr().get("ResearcherSurveyList");
%>
<%if (researcherSurveyList.getTwitasks()==null || researcherSurveyList.getTwitasks().size()==0){
    Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp");
    return;
}%>
<%@ include file="/template/header.jsp" %>




    <% if (Pagez.getUserSession().getIsloggedin() && (Pagez.getUserSession().getUser().getResearcherid()>0)){ %>
        <%if (researcherIndex.getMsg()!=null && !researcherIndex.getMsg().equals("")){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont"><%=researcherIndex.getMsg()%></font>
            </div>
        <%}%>

        <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="250" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researchertwitaskdetail_01.jsp"><font class="mediumfont" style="color: #596697;">Ask a Twitter Question</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Ask Twitter users a question. This step-by-step wizard will guide you through the process.  Your Twitter Question can be ready to roll in minutes.</font>
                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/about-faq.jsp"><font class="mediumfont" style="color: #596697;">Frequently Asked Questions</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Get your answers here!</font>
                            </td></tr></table>

                        </div>
                    </div>
                </td>
                <td valign="top">

                    <font class="largefont hdr">Twitter Questions You've Asked</font>
                    <br/>
                    <%if (researcherSurveyList.getTwitasks()==null || researcherSurveyList.getTwitasks().size()==0){%>
                        <font class="normalfont">You haven't yet created any Twitter Questions. <a href="/researcher/researchertwitaskdetail_01.jsp">Ask one now!</a></font>
                    <%} else {%>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("Title", "<a href=\"/twitask.jsp?twitaskid=<$twitask.twitaskid$>\"><font style=\"font-weight:bold;\"><$twitask.question$></font></a>", false, "", "normalfont"));
                            cols.add(new GridCol("Status", "<$status$>", false, "", "smallfont", "", ""));
                            cols.add(new GridCol("", "<$editorreviewlink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$resultslink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$deletelink$>", false, "", "smallfont"));
                        %>
                        <%=Grid.render(researcherSurveyList.getTwitasks(), cols, 15, "/researcher/index-twitask.jsp", "pagetwitask")%>
                    <%}%>

                </td>

             </tr>
         </table>

    <% } %>



<%@ include file="/template/footer.jsp" %>