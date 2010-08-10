<%@ page import="com.dneero.dao.Pl" %>
<%@ page import="com.dneero.dao.Researcher" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.User" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.privatelabel.PlPeers" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%String jspPageName="/surveyresponse.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
if (request.getParameter("show")!=null && request.getParameter("show").equals("results")){
    //redirect to results
    Pagez.sendRedirect("/surveyresults.jsp?surveyid="+request.getParameter("surveyid")+"&userid="+request.getParameter("userid"));
    return;
}
if (request.getParameter("show")!=null && request.getParameter("show").equals("disclosure")){
    //redirect to disclosure
    Pagez.sendRedirect("/surveydisclosure.jsp?surveyid="+request.getParameter("surveyid")+"&userid="+request.getParameter("userid"));
    return;
}
%>
<%
PublicSurveyResponse publicSurvey = (PublicSurveyResponse)Pagez.getBeanMgr().get("PublicSurveyResponse");
%>
<%
//If we don't have a surveyid, shouldn't be on this page
if (publicSurvey ==null || publicSurvey.getSurvey()==null || publicSurvey.getSurvey().getTitle()==null || publicSurvey.getSurvey().getSurveyid()<=0){
    Pagez.sendRedirect("/publicsurveylist.jsp");
    return;
}
//If the survey is draft or waiting
if (publicSurvey.getSurvey().getStatus()<Survey.STATUS_OPEN){
    Pagez.sendRedirect("/surveynotopen.jsp");
    return;
}
//If the survey isn't peered with this pl
Pl plOfSurvey = Pl.get(publicSurvey.getSurvey().getPlid());
if (!PlPeers.isThereATwoWayTrustRelationship(plOfSurvey, Pagez.getUserSession().getPl())){
    Pagez.getUserSession().setMessage("No Private Label Peering Agreement Exists between plOfSurvey("+plOfSurvey.getPlid()+") and userSession.getPl("+Pagez.getUserSession().getPl().getPlid()+").");
    Pagez.sendRedirect("/notauthorized.jsp");
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





    <% Survey surveyInTabs = publicSurvey.getSurvey();%>
    <%@ include file="/surveytabs.jsp" %>



        <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
        <table width="100%" cellpadding="2">
            <tr>
                <td valign="top" width="65%">
                <% if (publicSurvey.getUserwhotooksurvey()!=null){ %>
                     <% if (publicSurvey.getUserwhotooksurvey().getUserid()>0){ %>
                            <center>
                            <%--<div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">--%>
                                <center>
                                    <font class="largefont"><%=publicSurvey.getUserwhotooksurvey().getNickname()%>'s answers.</font><br/>
                                    <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                                        <a href="survey.jsp?s=<%=publicSurvey.getSurvey().getSurveyid()%>&u=<%=publicSurvey.getUserwhotooksurvey().getUserid()%>"><font class="mediumfont">How Will You Answer?</font></a>
                                    <% } else { %>
                                        <a href="survey.jsp?s=<%=publicSurvey.getSurvey().getSurveyid()%>&u=<%=publicSurvey.getUserwhotooksurvey().getUserid()%>"><font class="mediumfont">Edit Your Answers</font></a>
                                    <% } %>
                                </center>
                                <%--<br/>--%>
                                <%--<center><%=publicSurvey.getSurveyResponseHtml()%></center>--%>
                            <%--</div>--%>


                            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                                <%--<font class="largefont" style="color: #666666;"><%=publicSurvey.getSurvey().getTitle()%></font>--%>
                                <%--<br/>--%>
                                <%--<font class="smallfont"><%=publicSurvey.getSurvey().getDescription()%></font>--%>
                                <%--<br/><br/><br/>--%>
                                <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                                    <center>
                                    <%
                                    //MAIN CONVO HERE
                                    //MAIN CONVO HERE
                                    //MAIN CONVO HERE
                                    %>
                                    <%=publicSurvey.getHtmltoposttoblogflash()%>
                                    <%
                                    //MAIN CONVO HERE
                                    //MAIN CONVO HERE
                                    //MAIN CONVO HERE
                                    %>
                                    </center>


                                </div>
                            </div>

                            </center>
                            <br/><br/>
                       <% } %>
                   <% } %>
                </td>
                <td valign="top">
                </td>
            </tr>
        </table>




<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>

