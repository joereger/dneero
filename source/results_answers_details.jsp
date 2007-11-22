<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicResultsAnswersDetails" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicResultsAnswersDetails)Pagez.getBeanMgr().get("PublicResultsAnswersDetails")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicResultsAnswersDetails publicResultsAnswersDetails = (PublicResultsAnswersDetails)Pagez.getBeanMgr().get("PublicResultsAnswersDetails");
%>
<%@ include file="/template/header.jsp" %>



    <font class="smallfont"><%=publicResultsAnswersDetails.getSurvey().getDescription()%></font>
    <br/><br/><br/>


    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="65%">
                <%=publicResultsAnswersDetails.getResults()%>
            </td>
            <td valign="top" align="left">
                <div class="rounded" style="background: #00ff00; text-align: center;">
                    <div class="rounded" style="background: #ffffff; text-align: center;">
                        <center><img src="/images/undo-128.png" width="128" height="128"/></center>
                        <br/>
                        <a href="surveyresults.jsp?surveyid=<%=publicResultsAnswersDetails.getSurvey().getSurveyid()%>&show=results"><font class="mediumfont">Return to the Survey Results</font></a>
                    </div>
                </div>
            </td>
        </tr>
    </table>






<%@ include file="/template/footer.jsp" %>


