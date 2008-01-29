<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResults" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Str" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherResults researcherResults=(ResearcherResults) Pagez.getBeanMgr().get("ResearcherResults");
%>
<%@ include file="/template/header.jsp" %>



    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResults.getSurvey().getTitle()%></font>
        <br/>
        <a href="/researcher/results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="/researcher/results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Response Report</font></a>
        <a href="/researcher/results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="/researcher/results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="/researcher/results_demographics.jsp"style="padding-left: 15px;"><font class="subnavfont">Demographics</font></a>
        <a href="/researcher/results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>




    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tr>
            <td colspan="2" valign="top">
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <%if (researcherResults.getStatus() == Survey.STATUS_DRAFT) {%>
                        <font class="mediumfont">Survey Status: Draft</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_WAITINGFORFUNDS){%>
                        <font class="mediumfont">Survey Status: Waiting for Funds</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_WAITINGFORSTARTDATE){%>
                        <font class="mediumfont">Survey Status: Waiting for Start Date</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_OPEN){%>
                        <font class="mediumfont">Survey Status: Open</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_CLOSED){%>
                        <font class="mediumfont">Survey Status: Closed</font>
                    <%}%>

                </div>
                <br/>
            </td>
        </tr>
        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #88c99d; text-align: right; padding: 20px;">
                    <font class="smallfont">Total Survey Responses</font>
                    <br/>
                    <font class="largefont"><%=researcherResults.getTotalsurveyresponses()%></font>
                    <br/>
                    <a href="/researcher/results_answers.jsp"><font class="normalfont">Response Report</font></a>
                </div>
            </td>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #00ff00; text-align: left; padding: 20px;">
                    <font class="smallfont">Impressions on Blogs Qualifying for Payment</font>
                    <br/>
                    <font class="largefont"><%=researcherResults.getTotalsurveydisplays()%></font>
                    <br/>
                    <a href="/researcher/results_impressions.jsp"><font class="normalfont">View Impressions</font></a>
                </div>
            </td>
        </tr>
        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #00ff00; text-align: right; padding: 20px;">
                    <a href="/researcher/results_respondents.jsp"><font class="normalfont">See Respondents</font></a>
                    <br/>
                    <font class="largefont"><%=researcherResults.getTotalsurveyresponses()%></font>
                    <br/>
                    <font class="smallfont">People Have Taken the Survey</font>
                </div>
            </td>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #88c99d; text-align: left; padding: 20px;">
                    <a href="/researcher/results_financial.jsp"><font class="normalfont">Financial Status</font></a>
                    <br/>
                    <font class="largefont">$<font class="largefont"><%=Str.formatForMoney(researcherResults.getSpenttodate())%></font></font>
                    <br/>
                    <font class="smallfont">Has Been Spent to Date</font>
                </div>
            </td>
        </tr>
        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #88c99d; text-align: right; padding: 20px;">
                    <font class="smallfont">Respondent Demographics</font>
                    <br/>
                    <a href="/researcher/results_demographics.jsp"><font class="normalfont">Demographics Report</font></a>
                </div>
            </td>
            <td width="50%" valign="top">
                <!--<div class="rounded" style="background: #00ff00; text-align: left; padding: 20px;">
                    <font class="smallfont">Impressions on Blogs Qualifying for Payment</font>
                    <br/>
                    <font class="largefont"><%=researcherResults.getTotalsurveydisplays()%></font>
                    <br/>
                    <a href="/researcher/results_impressions.jsp"><font class="normalfont">View Impressions</font></a>
                </div>-->
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Survey Responses</font>
                    <%=PercentCompleteBar.get(String.valueOf(researcherResults.getTotalsurveyresponses()), String.valueOf(researcherResults.getMaxsurveyresponses()), "", "", String.valueOf(650))%>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Survey Impressions on Blogs</font>
                    <%=PercentCompleteBar.get(String.valueOf(researcherResults.getTotalsurveydisplays()), String.valueOf(researcherResults.getMaxsurveydisplays()), "", "", String.valueOf(650))%>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Possible Spend</font>
                    <%=PercentCompleteBar.get(String.valueOf(researcherResults.getSpenttodate()), String.valueOf(researcherResults.getMaxpossiblespend()), "", "", String.valueOf(650))%>
                </div>
            </td>
        </tr>
    </table>





    <br/>















<%@ include file="/template/footer.jsp" %>