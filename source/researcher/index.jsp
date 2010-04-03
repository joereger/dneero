<%@ page import="com.dneero.helpers.ResearcherCreateIfNeeded" %>
<%@ page import="com.dneero.review.Reviewable" %>
<%@ page import="com.dneero.review.ReviewableUtil" %>
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
<%if (researcherSurveyList.getSurveys()==null || researcherSurveyList.getSurveys().size()==0){
    Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp");
    return;
}%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("copy")) {
        try {
            researcherIndex.copy();
            Pagez.getUserSession().setMessage("Conversation copied!");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
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
                                <a href="/researcher/researchersurveydetail_01.jsp"><font class="mediumfont" style="color: #596697;">Create a New Conversation</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create a new conversation. This step-by-step wizard will guide you through the process.  Your conversation can be up and running in a matter of minutes.</font>
                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/panels.jsp"><font class="mediumfont" style="color: #596697;">Panels</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create and manage standing panels of bloggers for longitudinal studies.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/rank-list.jsp"><font class="mediumfont" style="color: #596697;">Rankings</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create your own ranking index and use it to define/track people across multiple conversations.</font>
                            </td></tr></table>

                            <%--<br/><br/>--%>
                            <%--<table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>--%>
                                <%--<a href="/researcher/researcherdetails.jsp"><font class="mediumfont" style="color: #596697;">Update My Researcher Profile</font></a>--%>
                             <%--</td></tr>--%>
                            <%--<tr><td valign="top"></td><td valign="top">--%>
                                <%--<font class="smallfont">Help us understand your needs so that we can serve you better.</font>--%>
                            <%--</td></tr></table>--%>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researcherbilling.jsp"><font class="mediumfont" style="color: #596697;">Billing Info</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">If you're doing conversations with incentives update your billing information on this screen.</font>
                            </td></tr></table>



                        </div>
                    </div>
                </td>
                <td valign="top">

                    <% ArrayList<Reviewable> reviewables = ReviewableUtil.getPendingForResearcher(Pagez.getUserSession().getUser().getResearcherid());
                       if (reviewables!=null && reviewables.size()>0){%>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <font class="smallfont">There are currently <a href="/researcher/reviewables-turbo.jsp"><%=reviewables.size()%> pending items</a> for review.</font>
                        </div>
                   <%}%>

                    <font class="largefont hdr">Conversations You've Created</font>
                    <br/>
                    <%if (researcherSurveyList.getSurveys()==null || researcherSurveyList.getSurveys().size()==0){%>
                        <font class="normalfont">You haven't yet created any conversations. <a href="/researcher/researchersurveydetail_01.jsp">Create a New Conversation</a>.</font>
                    <%} else {%>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("Title", "<a href=\"/survey.jsp?surveyid=<$survey.surveyid$>\"><font style=\"font-weight:bold;\"><$survey.title$></font></a>", false, "", "normalfont"));
                            cols.add(new GridCol("Status", "<$status$>", false, "", "smallfont", "", ""));
                            cols.add(new GridCol("", "<$editorreviewlink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$invitelink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$resultslink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$copylink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$deletelink$>", false, "", "smallfont"));
                        %>
                        <%=Grid.render(researcherSurveyList.getSurveys(), cols, 15, "/researcher/index.jsp", "page")%>
                    <%}%>



                </td>

             </tr>
         </table>

    <% } %>



<%@ include file="/template/footer.jsp" %>