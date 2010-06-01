<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyDiscuss" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dao.Survey" %>
<%
PublicSurveyDiscuss publicSurveyDiscuss = (PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss");
%>
<%
    //If we don't have a surveyid, shouldn't be on this page
    if (publicSurveyDiscuss.getSurvey() == null || publicSurveyDiscuss.getSurvey().getTitle() == null || publicSurveyDiscuss.getSurvey().getSurveyid()<=0) {
        Pagez.sendRedirect("/publicsurveylist.jsp");
        return;
    }
//If the survey is draft or waiting
    if (publicSurveyDiscuss.getSurvey().getStatus()<Survey.STATUS_OPEN) {
        Pagez.sendRedirect("/surveynotopen.jsp");
        return;
    }
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("postcomment")) {
        try {
            publicSurveyDiscuss.setDiscussSubject(Textbox.getValueFromRequest("discusssubject", "Subject", true, DatatypeString.DATATYPEID));
            publicSurveyDiscuss.setDiscussComment(Textarea.getValueFromRequest("discusscomment", "Comment", true));
            publicSurveyDiscuss.newComment();
            Pagez.getUserSession().setMessage("Your comment has been posted!");
            publicSurveyDiscuss.initBean();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <% Survey surveyInTabs = publicSurveyDiscuss.getSurvey();%>
    <%@ include file="/surveytabs.jsp" %>

    <a href="/survey.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyDiscuss.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>

    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                Discuss this conversation here.  Thoughts on the questions?  Thoughts on the results?  Thoughts on the people who are posting it to their blogs? Thoughts on anything else related to this conversation?
                </font></div></center>
                <% if (!Pagez.getUserSession().getIsloggedin()){ %>
                    <br/><br/>
                    <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                        <font class="mediumfont">You must be logged-in to take part in the discussion.</font>
                    <% } else { %>
                        <font class="mediumfont">You must join at least one conversation before you can take part in the discussion.</font>
                    <% } %>
                <% } %>
            </td>
            <td valign="top" width="150">
                <img src="/images/wireless-green.png" width="32" height="32"/>
            </td>
        </tr>
    </table>


    <%if (publicSurveyDiscuss.getSurveydiscusses()==null || publicSurveyDiscuss.getSurveydiscusses().size()==0){%>

    <%} else {%>
        <%
            StringBuffer co = new StringBuffer();
            co.append("<font class=\"tinyfont\"><$surveydiscuss.date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$></font>");
            co.append("<br/>");
            co.append("<font class=\"normalfont\"><$surveydiscuss.subject$></font>");
            co.append("<br/>");
            co.append("<font class=\"smallfont\"><$surveydiscuss.comment$></font>");
            co.append("<br/><br/>");
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("", "<img src=\"/images/user-48.png\" width=\"48\" height=\"48\"/>", false, "", ""));
            cols.add(new GridCol("", "<a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.nickname$></a>", false, "", "normalfont", "", "font-weight: bold;"));
            cols.add(new GridCol("", co.toString(), false, "", ""));
        %>
        <%=Grid.render(publicSurveyDiscuss.getSurveydiscusses(), cols, 50, "/surveydiscuss.jsp", "page")%>
    <%}%>



    <br/><br/>
    <% if (Pagez.getUserSession().getIsloggedin()){ %>
        <font class="mediumfont">Post a Comment!</font>
        <br/><br/>
        <form action="/surveydiscuss.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/surveydiscuss.jsp">
            <input type="hidden" name="action" value="postcomment">
            <input type="hidden" name="surveyid" value="<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Subject:</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("discusssubject", publicSurveyDiscuss.getDiscussSubject(), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                         <font class="formfieldnamefont">Comment:</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("discusscomment", publicSurveyDiscuss.getDiscussComment(), 6, 45, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Post Comment">
                    </td>
                </tr>

            </table>
        </form>
    <% } %>







<%@ include file="/template/footer.jsp" %>


