<%@ page import="com.dneero.constants.Dneerousagemethods" %>
<%@ page import="com.dneero.constants.*" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.*" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherResultsAnswersAdvanced researcherResultsAnswersAdvanced = (ResearcherResultsAnswersAdvanced) Pagez.getBeanMgr().get("ResearcherResultsAnswersAdvanced");
%>
<%
    if (request.getParameter("action")!=null && (request.getParameter("action").equals("viewthroughfilter"))) {
        try {


            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                           .add(Restrictions.eq("plid", Pagez.getUserSession().getPl()))
                           .addOrder(Order.asc("ordernum"))
                           .setCacheable(true)
                           .list();
            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                Demographic demographic = demographicIterator.next();
                researcherResultsAnswersAdvanced.getDemographicsXML().setValues(demographic.getDemographicid(), DropdownMultiselect.getValueFromRequest("demographic_"+demographic.getDemographicid(), demographic.getName(), false));
            }


            researcherResultsAnswersAdvanced.setAgemin(Textbox.getIntFromRequest("agemin", "Age Min", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setAgemax(Textbox.getIntFromRequest("agemax", "Age Max", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setMinsocialinfluencepercentile(Dropdown.getIntFromRequest("minsocialinfluencepercentile", "Min Social Influence", false));
            researcherResultsAnswersAdvanced.setDayssincelastsurvey(Textbox.getIntFromRequest("dayssincelastsurvey", "Days Since Last Participation", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setTotalsurveystakenatleast(Textbox.getIntFromRequest("totalsurveystakenatleast", "Total "+Pagez._Surveys()+" Joined of At Least", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setTotalsurveystakenatmost(Textbox.getIntFromRequest("totalsurveystakenatmost", "Total "+Pagez._Surveys()+" Joined of At Most", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setDneerousagemethods(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("dneerousagemethods", "Usage Methods", false)));
            researcherResultsAnswersAdvanced.setFiltername(Textbox.getValueFromRequest("filtername", "Filter Name", false, DatatypeString.DATATYPEID));

            researcherResultsAnswersAdvanced.viewThroughFilter();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action")!=null && (request.getParameter("action").equals("showresults"))) {
        try {
            researcherResultsAnswersAdvanced.showResults();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action")!=null && (request.getParameter("action").equals("deletefilter"))) {
        try {
            researcherResultsAnswersAdvanced.deleteFilter();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsAnswersAdvanced.getSurvey().getTitle()%></font>
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
    <br/><br/>

    <%if (request.getParameter("action")!=null && (request.getParameter("action").equals("showresults") || request.getParameter("action").equals("viewthroughfilter"))) {%>

            <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                <font class="mediumfont" style="color: #666666;">
                Filter Applied:
                <%if (researcherResultsAnswersAdvanced.getRespondentfilter()!=null){%>
                    <%=researcherResultsAnswersAdvanced.getRespondentfilter().getName()%>
                <%} else {%>
                    Custom
                <%}%>
                </font><br/>
                <font class="tinyfont" style="color: #666666;">
                    <a href="/researcher/results_answers_advanced.jsp">Reset Filter or Choose Another</a>
                <%if (researcherResultsAnswersAdvanced.getRespondentfilter()!=null){%>
                    | <a href="/researcher/panels-addpeople.jsp?surveyid=<%=researcherResultsAnswersAdvanced.getSurvey().getSurveyid()%>&respondentfilterid=<%=researcherResultsAnswersAdvanced.getRespondentfilter().getRespondentfilterid()%>&showonly=addbysurvey">Add These People to a Panel</a>
                <%}%>
                </font>
            </div>


        <%if (researcherResultsAnswersAdvanced.getResults()!=null){%>
            <%=researcherResultsAnswersAdvanced.getResults()%>
        <%}%>
        <br/><br/>
        <%//@todo download advanced results as csv page%>
        <!--<a href="/researcher/results_csv.jsp">Download Results as CSV</a>-->
    <%}else{%>

       <table cellpadding="5" cellspacing="0" border="0">
            <tr>
                <td valign="top">

                    <form action="/researcher/results_answers_advanced.jsp" method="post"  class="niceform" id="rsdform">
                        <input type="hidden" name="dpage" value="/researcher/results_answers_advanced.jsp">
                        <input type="hidden" name="action" value="viewthroughfilter" id="action">
                        <%if (researcherResultsAnswersAdvanced.getRespondentfilter()!=null){%>
                            <input type="hidden" name="respondentfilterid" value="<%=researcherResultsAnswersAdvanced.getRespondentfilter().getRespondentfilterid()%>">
                        <%}%>
                        <input type="hidden" name="surveyid" value="<%=researcherResultsAnswersAdvanced.getSurvey().getSurveyid()%>"/>


                    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
                        <font class="mediumfont">Filter Results</font>
                        <br/>
                        Use this screen to look at a subset of the <a href="/researcher/results_answers.jsp?surveyid=<%=researcherResultsAnswersAdvanced.getSurvey().getSurveyid()%>">overall results</a>.  Choose only the criteria for respondents that you want to see and then click Show Results.  You can optionally name and save your filter for easy access later on.  Saved filters can be applied to other <%=Pagez._surveys()%> too.
                    </font></div></center>

                    <br/><br/>



                    <table cellpadding="2" cellspacing="0" border="0">


                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Social Influence Rating (TM)</font>
                                <br/>
                                <font class="smallfont"></font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("minsocialinfluencepercentile", String.valueOf(researcherResultsAnswersAdvanced.getMinsocialinfluencepercentile()), StaticVariables.getPercentiles(), "", "")%>
                            </td>

                        </tr>


                        <%//Commented out and spoofed with hidden vars for performance.  Using real values would trigger expensive work in SurveyCriteriaXML%>
                        <input type="hidden" name="totalsurveystakenatleast" value="0">
                        <input type="hidden" name="totalsurveystakenatmost" value="10000">
                        <input type="hidden" name="dayssincelastsurvey" value="0">
                        <%--<tr>--%>
                            <%--<td valign="top">--%>
                                <%--<font class="formfieldnamefont">Number of Surveys Taken</font>--%>
                                <%--<br/>--%>
                                <%--<font class="smallfont">Reflects the experience level of the respondent.  Enter a minimum and maximum number of surveys that qualified respondents are allowed to have taken.</font>--%>
                            <%--</td>--%>
                            <%--<td valign="top">--%>
                                <%--<%=Textbox.getHtml("totalsurveystakenatleast", String.valueOf(researcherResultsAnswersAdvanced.getTotalsurveystakenatleast()), 5, 4, "", "")%>--%>
                                <%-----%>
                                <%--<%=Textbox.getHtml("totalsurveystakenatmost", String.valueOf(researcherResultsAnswersAdvanced.getTotalsurveystakenatmost()), 5, 4, "", "")%>--%>
                            <%--</td>--%>

                            <%--<td valign="top">--%>
                                <%--<font class="formfieldnamefont">Days Since Joining of At Least</font>--%>
                                <%--<br/>--%>
                                <%--<font class="smallfont">A qualifying respondent must have not joined in at least this many days.</font>--%>
                            <%--</td>--%>
                            <%--<td valign="top">--%>
                                <%--<%=Textbox.getHtml("dayssincelastsurvey", String.valueOf(researcherResultsAnswersAdvanced.getDayssincelastsurvey()), 5, 3, "", "")%>--%>
                            <%--</td>--%>
                        <%--</tr>--%>



                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Age Range</font>
                            </td>
                            <td valign="top">
                                <%=Textbox.getHtml("agemin", String.valueOf(researcherResultsAnswersAdvanced.getAgemin()), 5, 3, "", "font-size: 8px;")%>
                                -
                                <%=Textbox.getHtml("agemax", String.valueOf(researcherResultsAnswersAdvanced.getAgemax()), 5, 3, "", "font-size: 8px;")%>
                            </td>
                        </tr>



                        <%
                            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", Pagez.getUserSession().getPl()))
                                           .addOrder(Order.asc("ordernum"))
                                           .setCacheable(true)
                                           .list();
                            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                                Demographic demographic = demographicIterator.next();
                                %>

                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont"><%=demographic.getName()%></font>
                                        <%if (demographic.getDescription().length()>0){ %>
                                            <br/><font class="tinyfont"><%=demographic.getDescription()%></font>
                                        <%}%>
                                    </td>
                                    <td valign="top">
                                        <%=DropdownMultiselect.getHtml("demographic_"+demographic.getDemographicid(),
                                                            researcherResultsAnswersAdvanced.getDemographicsXML().getValues(demographic.getDemographicid()),
                                                            Util.stringArrayToTreeMap(researcherResultsAnswersAdvanced.getDemographicsXML().getAllPossibleValues(demographic.getDemographicid())),
                                                            15,
                                                            "",
                                                            "")%>
                                    </td>
                                </tr>

                                <%
                            }
                        %>





                        <tr>

                            <td valign="top">
                                <font class="formfieldnamefont">Usage Method</font>
                                <br/>
                                <font class="tinyfont"></font>
                            </td>
                            <td valign="top">
                                <%=DropdownMultiselect.getHtml("dneerousagemethods", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getDneerousagemethods()), Util.treeSetToTreeMap(Dneerousagemethods.get()), 3, "", "font-size: 8px;")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Name Your Filter to Save It (Optional)</font>
                            </td>
                            <td valign="top">
                                <%=Textbox.getHtml("filtername", String.valueOf(researcherResultsAnswersAdvanced.getFiltername()), 50, 30, "", "")%>
                                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Show Results">
                            </td>

                        </tr>
                    </table>




                    </form>
                </td>
                <td valign="top" width="33%">
                    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                        <font class="mediumfont">Your Filters</font>
                        <br/>
                        <%

                        %>
                        <%if (researcherResultsAnswersAdvanced.getRespondentfilters()==null || researcherResultsAnswersAdvanced.getRespondentfilters().size()==0){%>
                            <font class="tinyfont">You haven't created any filters yet.</font>
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
                            <font class="tinyfont">Choose a filter to see the results from this <%=Pagez._survey()%> filtered through it.</font>
                            </form>
                        <%}%>
                    </div>
                </td>
            </tr>
        </table>



    <%}%>
    

    


<%@ include file="/template/footer.jsp" %>