<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResults" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Str" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = Pagez._Survey()+" Results";
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
        <a href="/researcher/results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Results</font></a>
        <a href="/researcher/results_answers_userquestions.jsp"style="padding-left: 15px;"><font class="subnavfont">User Questions</font></a>
        <a href="/researcher/results_answers_advanced.jsp"style="padding-left: 15px;"><font class="subnavfont">Filter Results</font></a>
        <a href="/researcher/results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="/researcher/results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="/researcher/results_awards.jsp"style="padding-left: 15px;"><font class="subnavfont">Awards</font></a>
        <a href="/researcher/results_demographics.jsp"style="padding-left: 15px;"><font class="subnavfont">Demographics</font></a>
        <a href="/researcher/results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>




    <table cellpadding="3" cellspacing="0" border="0" width="100%">

        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <%if (researcherResults.getStatus() == Survey.STATUS_DRAFT) {%>
                        <font class="mediumfont">Status: Draft</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_WAITINGFORFUNDS){%>
                        <font class="mediumfont">Status: Waiting for Funds</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_WAITINGFORSTARTDATE){%>
                        <font class="mediumfont">Status: Waiting for Start Date</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_OPEN){%>
                        <font class="mediumfont">Status: Open</font>
                    <%} else if (researcherResults.getStatus() == Survey.STATUS_CLOSED){%>
                        <font class="mediumfont">Status: Closed</font>
                    <%}%>

                </div>
                <div class="rounded" style="background: #88c99d; text-align: left; padding: 20px;">
                    <font class="smallfont">Total Responses</font>
                    <br/>
                    <font class="largefont"><%=researcherResults.getTotalsurveyresponses()%></font>
                    <br/>
                    <a href="/researcher/results_answers.jsp"><font class="normalfont">Response Report</font></a>
                </div>
                <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                    <font class="smallfont">Impressions on Blogs Qualifying for Payment</font>
                    <br/>
                    <font class="largefont"><%=researcherResults.getTotalsurveydisplays()%></font>
                    <br/>
                    <a href="/researcher/results_impressions.jsp"><font class="normalfont">View Impressions</font></a>
                </div>
                <div class="rounded" style="background: #88c99d; text-align: left; padding: 20px;">
                    <a href="/researcher/results_respondents.jsp"><font class="normalfont">See Respondents</font></a>
                    <br/>
                    <font class="largefont"><%=researcherResults.getTotalsurveyresponses()%></font>
                    <br/>
                    <font class="smallfont">People Have Joined the <%=Pagez._Survey()%></font>
                </div>
                <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                    <a href="/researcher/results_financial.jsp"><font class="normalfont">Financial Status</font></a>
                    <br/>
                    <font class="largefont">$<font class="largefont"><%=Str.formatForMoney(researcherResults.getSpenttodate())%></font></font>
                    <br/>
                    <font class="smallfont">Has Been Spent to Date</font>
                </div>
                <div class="rounded" style="background: #88c99d; text-align: left; padding: 20px;">
                    <font class="smallfont">Respondent Demographics</font>
                    <br/>
                    <a href="/researcher/results_demographics.jsp"><font class="normalfont">Demographics Report</font></a>
                </div>
            </td>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #ffffff; text-align: left; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Responses</font>
                    <%=PercentCompleteBar.get(String.valueOf(researcherResults.getTotalsurveyresponses()), String.valueOf(researcherResults.getMaxsurveyresponses()), "", "", String.valueOf(300))%>
                    <br/><br/>
                    <font class="mediumfont" style="color: #333333;">Impressions</font>
                    <%=PercentCompleteBar.get(String.valueOf(researcherResults.getTotalsurveydisplays()), String.valueOf(researcherResults.getMaxsurveydisplays()), "", "", String.valueOf(300))%>
                    <br/><br/>
                    <font class="mediumfont" style="color: #333333;">Possible Spend</font>
                    <%=PercentCompleteBar.get(String.valueOf(researcherResults.getSpenttodate()), String.valueOf(researcherResults.getMaxpossiblespend()), "", "", String.valueOf(300))%>
                </div>
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                    <font class="mediumfont">Filter Results</font>
                    <br/>
                    <%
                    ResearcherResultsAnswersAdvanced researcherResultsAnswersAdvanced = (ResearcherResultsAnswersAdvanced) Pagez.getBeanMgr().get("ResearcherResultsAnswersAdvanced");
                    %>
                    <%if (researcherResultsAnswersAdvanced.getRespondentfilters()==null || researcherResultsAnswersAdvanced.getRespondentfilters().size()==0){%>
                        <font class="tinyfont">You haven't <a href="/researcher/results_answers_advanced.jsp?surveyid=<%=researcherResultsAnswersAdvanced.getSurvey().getSurveyid()%>">created</a> any filters yet.</font>
                    <%} else {%>
                        <form action="/researcher/results_answers_advanced.jsp" method="post"  class="niceform" id="rsdform">
                        <input type="hidden" name="dpage" value="/researcher/results_answers_advanced.jsp">
                        <input type="hidden" name="action" value="showresults" id="action">
                        <input type="hidden" name="surveyid" value="<%=researcherResultsAnswersAdvanced.getSurvey().getSurveyid()%>"/>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            //cols.add(new GridCol("", "<input type=\"radio\" name=\"respondentfilterid\" value=\"<$respondentfilterid$>\">", false, "", "smallfont", "width: 20px;", ""));
                            cols.add(new GridCol("", "<a href=\"/researcher/results_answers_advanced.jsp?action=showresults&respondentfilterid=<$respondentfilterid$>&surveyid="+researcherResultsAnswersAdvanced.getSurvey().getSurveyid()+"\"><$name$></a>", false, "", "normalfont"));
                            cols.add(new GridCol("", "<a href=\"/researcher/results_answers_advanced.jsp?action=deletefilter&respondentfilterid=<$respondentfilterid$>&surveyid="+researcherResultsAnswersAdvanced.getSurvey().getSurveyid()+"\"><img src=\"/images/delete-16.png\" width=\"16\" height=\"16\" border=\"0\" alt=\"Delete this filter\"></a>", false, "", "tinyfont"));
                        %>
                        <%=Grid.render(researcherResultsAnswersAdvanced.getRespondentfilters(), cols, 500, "/researcher/results_answers_advanced.jsp", "page")%>
                        <!--<input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Show Filtered Results">-->
                        <br/>
                        <font class="tinyfont"><a href="/researcher/results_answers_advanced.jsp?surveyid=<%=researcherResultsAnswersAdvanced.getSurvey().getSurveyid()%>">Create a Filter</a></font>
                        </form>
                    <%}%>
                </div>
            </td>
        </tr>


    </table>





    <br/>















<%@ include file="/template/footer.jsp" %>