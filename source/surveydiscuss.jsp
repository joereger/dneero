<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyDiscuss" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicSurveyDiscuss) Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicSurveyDiscuss publicSurveyDiscuss = (PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss");
%>
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



    <font class="smallfont"><%=publicSurveyDiscuss.getSurvey().getDescription()%></font><br/><br/><br/>


    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurveyDiscuss.getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>

    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                Discuss this survey here.  Thoughts on the results?  Thoughts on the financial incentive?  Thoughts on the people who are posting it to their blogs?  Thoughts on the questions asked?  Thoughts on anything else related to this survey?
                </font></div></center>
                <% if (Pagez.getUserSession().getIsloggedin()){ %>
                    <br/><br/>
                    <font class="mediumfont">You must be logged-in to take part in the discussion.</font>
                <% } %>
            </td>
            <td valign="top" width="150">
                <img src="/images/wireless-green-128.png" width="128" height="128"/>
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
            cols.add(new GridCol("", "<a href=\"profile.jsp?userid=<$user.userid$>\"><$user.firstname$> <$user.lastname$></a>", false, "", "normalfont", "", "font-weight: bold;"));
            cols.add(new GridCol("", co.toString(), false, "", ""));
        %>
        <%=Grid.render(publicSurveyDiscuss.getSurveydiscusses(), cols, 50, "surveydiscuss.jsp", "page")%>
    <%}%>



    <br/><br/>
    <% if (Pagez.getUserSession().getIsloggedin()){ %>
        <font class="mediumfont">Post a Comment!</font>
        <br/><br/>
        <form action="surveydiscuss.jsp" method="post">
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
                        <input type="submit" class="formsubmitbutton" value="Post Comment">
                    </td>
                </tr>

            </table>
        </form>
    <% } %>







<%@ include file="/template/footer.jsp" %>


