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
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherResultsAnswersAdvanced researcherResultsAnswersAdvanced = (ResearcherResultsAnswersAdvanced) Pagez.getBeanMgr().get("ResearcherResultsAnswersAdvanced");
%>
<%
    if (request.getParameter("action")!=null && (request.getParameter("action").equals("find"))) {
        try {

            researcherResultsAnswersAdvanced.setAgemin(Textbox.getIntFromRequest("agemin", "Age Min", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setAgemax(Textbox.getIntFromRequest("agemax", "Age Max", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setBlogfocus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("blogfocus", "Blog Focus", false)));
            researcherResultsAnswersAdvanced.setBlogquality(Dropdown.getIntFromRequest("blogquality", "Blog Quality", false));
            researcherResultsAnswersAdvanced.setCity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("cities", "Cities", false)));
            researcherResultsAnswersAdvanced.setEducationlevel(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("educationlevel", "Education Levels", false)));
            researcherResultsAnswersAdvanced.setEthnicity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("ethnicity", "Ethnicity", false)));
            researcherResultsAnswersAdvanced.setGender(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("gender", "Genders", false)));
            researcherResultsAnswersAdvanced.setIncome(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("income", "Incomes", false)));
            researcherResultsAnswersAdvanced.setMaritalstatus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("maritalstatus", "Marital Statuses", false)));
            researcherResultsAnswersAdvanced.setMinsocialinfluencepercentile(Dropdown.getIntFromRequest("minsocialinfluencepercentile", "Min Social Influence", false));
            researcherResultsAnswersAdvanced.setDayssincelastsurvey(Textbox.getIntFromRequest("dayssincelastsurvey", "Days Since Last Survey", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setTotalsurveystakenatleast(Textbox.getIntFromRequest("totalsurveystakenatleast", "Total Surveys Taken of At Least", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setTotalsurveystakenatmost(Textbox.getIntFromRequest("totalsurveystakenatmost", "Total Surveys Taken of At Most", true, DatatypeInteger.DATATYPEID));
            researcherResultsAnswersAdvanced.setPolitics(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("politics", "Politics", false)));
            researcherResultsAnswersAdvanced.setDneerousagemethods(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("dneerousagemethods", "dNeero Usage Methods", false)));
            researcherResultsAnswersAdvanced.setProfession(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("professions", "Professions", false)));
            researcherResultsAnswersAdvanced.setState(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("states", "States", false)));

            researcherResultsAnswersAdvanced.find();
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
        <a href="/researcher/results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Response Report</font></a>
        <a href="/researcher/results_answers_advanced.jsp"style="padding-left: 15px;"><font class="subnavfont">Filter Results</font></a>
        <a href="/researcher/results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="/researcher/results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="/researcher/results_demographics.jsp"style="padding-left: 15px;"><font class="subnavfont">Demographics</font></a>
        <a href="/researcher/results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>

    <%if (request.getParameter("action")!=null && (request.getParameter("action").equals("find"))) {%>
        <%=researcherResultsAnswersAdvanced.getResults()%>
        <br/><br/>
        <%//@todo download advanced results as csv page%>
        <!--<a href="/researcher/results_csv.jsp">Download Results as CSV</a>-->
    <%}else{%>



        <form action="/researcher/results_answers_advanced.jsp" method="post" id="rsdform">
            <input type="hidden" name="dpage" value="/researcher/results_answers_advanced.jsp">
            <input type="hidden" name="action" value="find" id="action">
            <input type="hidden" name="surveyid" value="<%=researcherResultsAnswersAdvanced.getSurvey().getSurveyid()%>"/>


        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        Filter results.  Choose only the criteria for respondents that you want to see.
        <br/><br/><br/>
        </font></div></center>

        <br/><br/>



        <table cellpadding="5" cellspacing="0" border="0">


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Social Influence Rating (TM)</font>
                    <br/>
                    <font class="smallfont">Social Influence Rating takes site traffic, survey referrals and a number of other metrics into account to give you some measure of this blogger's influence with his/her readership.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("minsocialinfluencepercentile", String.valueOf(researcherResultsAnswersAdvanced.getMinsocialinfluencepercentile()), StaticVariables.getPercentiles(), "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Blog Quality of At Least</font>
                    <br/>
                    <font class="smallfont">Blog Quality is determined manually by our administrators visiting each blog post and assigning a general quality rating.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("blogquality", String.valueOf(researcherResultsAnswersAdvanced.getBlogquality()), StaticVariables.getBlogqualities(), "", "")%>
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
                    <%--<font class="formfieldnamefont">Days Since Taking Last Survey of At Least</font>--%>
                    <%--<br/>--%>
                    <%--<font class="smallfont">A qualifying respondent must have not taken another survey in at least this many days.</font>--%>
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
                    <%=Textbox.getHtml("agemin", String.valueOf(researcherResultsAnswersAdvanced.getAgemin()), 5, 3, "", "")%>
                    -
                    <%=Textbox.getHtml("agemax", String.valueOf(researcherResultsAnswersAdvanced.getAgemax()), 5, 3, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Gender</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("gender", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getGender()), Util.treeSetToTreeMap(Genders.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Ethnicity</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("ethnicity", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getEthnicity()), Util.treeSetToTreeMap(Ethnicities.get()), 6, "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Marital Status</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("maritalstatus", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getMaritalstatus()), Util.treeSetToTreeMap(Maritalstatuses.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Income</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("income", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getIncome()), Util.treeSetToTreeMap(Incomes.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Education</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("educationlevel", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getEducationlevel()), Util.treeSetToTreeMap(Educationlevels.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">State</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("states", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getState()), Util.treeSetToTreeMap(States.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">City</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("cities", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getCity()), Util.treeSetToTreeMap(Cities.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Profession</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("professions", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getProfession()), Util.treeSetToTreeMap(Professions.get()), 6, "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Blog Focus</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("blogfocus", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getBlogfocus()), Util.treeSetToTreeMap(Blogfocuses.get()), 6, "", "")%>
                </td>
            </tr>


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Politics</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("politics", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getPolitics()), Util.treeSetToTreeMap(Politics.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">dNeero Usage Method</font>
                    <br/>
                    <font class="tinyfont">There are multiple ways that users can access dNeero social surveys.  This control allows you to target each access method individually.</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("dneerousagemethods", Util.stringArrayToArrayList(researcherResultsAnswersAdvanced.getDneerousagemethods()), Util.treeSetToTreeMap(Dneerousagemethods.get()), 3, "", "")%>
                </td>
            </tr>

           

        </table>

        <input type="submit" class="formsubmitbutton" value="Generate Results (please be patient)">


        </form>




    <%}%>
    

    


<%@ include file="/template/footer.jsp" %>