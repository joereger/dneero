<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyResults" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%
PublicSurveyResults publicSurveyResults = (PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults");
%>
<%
    //If we don't have a surveyid, shouldn't be on this page
    if (publicSurveyResults.getSurvey() == null || publicSurveyResults.getSurvey().getTitle() == null || publicSurveyResults.getSurvey().getSurveyid()<=0) {
        Pagez.sendRedirect("/publicsurveylist.jsp");
        return;
    }
    //If the survey is draft or waiting
    if (publicSurveyResults.getSurvey().getStatus()<Survey.STATUS_OPEN) {
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
<%@ include file="/template/header.jsp" %>

    <% Survey surveyInTabs = publicSurveyResults.getSurvey();%>
    <%@ include file="/template/calpers/jspoverride/surveytabs.jsp" %>

    <font class="mediumfont" style="color: #cccccc;">What People are Saying</font>
    <br/>
    <a href="/survey.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyResults.getSurvey().getTitle()%></font></a>
    <br/><br/>

        <font class="normalfont">
            <ul>
               <li>The two tabs below contain the answers to the questions we've used in this conversation.</li>
               <li>Both of these tabs display the most recent 15 responses.</li>
               <li>If there are more than 15 responses, click on the "See All" link.</li>
            </ul>
        </font>

    <br/><br/>


    <div id="tabs">
        <ul>
            <li><a href="#tabs-1"><b>Facilitators' Questions</b></a></li>
            <li><a href="#tabs-2"><b>Jammers' Questions</b></a></li>
            <%if (!Pagez.getUserSession().getIsfacebookui()){%>
                <%if (publicSurveyResults.getResultsHtmlForUserWhoTookSurvey()!=null && !publicSurveyResults.getResultsHtmlForUserWhoTookSurvey().equals("")){%>
                    <li><a href="#tabs-3"><%=publicSurveyResults.getResultsfriendstabtext()%></a></li>
                <% } %>
            <% } %>
            <%if (publicSurveyResults.getResultsshowyourfriendstab()){%>
                <li><a href="#tabs-4">Your Friends</a></li>
            <%}%>
        </ul>
        <div id="tabs-1">
            <font class="mediumfont" style="color: #666666;">Facilitators' Questions</font><br/>
            <%=publicSurveyResults.getResultsHtml()%>
        </div>
        <div id="tabs-2">
            <font class="mediumfont" style="color: #666666;">Jammers' Questions</font><br/><br/>
            <div class="rounded" style="background: #e6e6e6; padding: 10px; text-align: left;">
                <font class="normalfont">
                    Displayed below are the answers to questions that fellow Jammers entered into this conversation.
                </font>

            </div>
            <br/>
            <%=publicSurveyResults.getResultsUserquestionsHtml()%>
        </div>
        <%if (!Pagez.getUserSession().getIsfacebookui()){%>
            <%if (publicSurveyResults.getResultsHtmlForUserWhoTookSurvey()!=null && !publicSurveyResults.getResultsHtmlForUserWhoTookSurvey().equals("")){%>
                <div id="tabs-3">
                    <font class="mediumfont" style="color: #666666;"><%=publicSurveyResults.getResultsfriendstabtext()%></font><br/>
                    <table width="100%" cellpadding="10" cellspacing="0" border="0">
                        <tr>
                            <td valign="top">
                                <%=publicSurveyResults.getResultsHtmlForUserWhoTookSurvey()%>
                            </td>
                        </tr>
                    </table>
                </div>
            <%}%>
        <%}%>
        <%if (publicSurveyResults.getResultsshowyourfriendstab()){%>
            <div id="tabs-4">
                <font class="mediumfont" style="color: #cccccc;">Your Friends</font><br/>
                <%=publicSurveyResults.getResultsYourFriends()%>
            </div>
        <%}%>



<script>
        <%if (request.getParameter("tab")!=null && request.getParameter("tab").equals("userquestions")){%>
            $('#tabs').tabs();
            $('#tabs').tabs("select", "tabs-2");        
        <% } else { %>
            $('#tabs').tabs();
        <% } %>
</script>



<%@ include file="/template/footer.jsp" %>


