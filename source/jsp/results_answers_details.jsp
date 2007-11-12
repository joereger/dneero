<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<%=((PublicResultsAnswersDetails)Pagez.getBeanMgr().get("PublicResultsAnswersDetails")).getSurvey().getTitle()%>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



    <h:messages styleClass="RED"/>

    <font class="smallfont"><%=((PublicResultsAnswersDetails)Pagez.getBeanMgr().get("PublicResultsAnswersDetails")).getSurvey().getDescription()%></font>
    <br/><br/><br/>


    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="65%">
                <f:verbatim escape="false"><%=((PublicResultsAnswersDetails)Pagez.getBeanMgr().get("PublicResultsAnswersDetails")).getResults()%></f:verbatim>
            </td>
            <td valign="top" align="left">
                <div class="rounded" style="background: #00ff00; text-align: center;">
                    <div class="rounded" style="background: #ffffff; text-align: center;">
                        <center><img src="/images/undo-128.png" width="128" height="128"/></center>
                        <br/>
                        <a href="/surveyresults.jsf?surveyid=<%=((PublicResultsAnswersDetails)Pagez.getBeanMgr().get("PublicResultsAnswersDetails")).getSurvey().getSurveyid()%>&amp;show=results"><font class="mediumfont">Return to the Survey Results</font></a>    
                    </div>
                </div>
            </td>
        </tr>
    </table>






<%@ include file="/jsp/templates/footer.jsp" %>


