<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail04" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="com.dneero.constants.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-04.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail04) Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail04 researcherSurveyDetail04 = (ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherSurveyDetail04.getSurvey().getStatus()>Survey.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid());
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid());
                    return;
                }
            }
            researcherSurveyDetail04.setAgemin(Textbox.getIntFromRequest("agemin", "Age Min", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail04.setAgemax(Textbox.getIntFromRequest("agemax", "Age Max", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail04.setBlogfocus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("blogfocus", "Blog Focus", false)));
            researcherSurveyDetail04.setBlogquality(Dropdown.getIntFromRequest("blogquality", "Blog Quality", false));
            researcherSurveyDetail04.setCity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("cities", "Cities", false)));
            researcherSurveyDetail04.setEducationlevel(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("educationlevel", "Education Levels", false)));
            researcherSurveyDetail04.setEthnicity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("ethnicity", "Ethnicity", false)));
            researcherSurveyDetail04.setGender(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("gender", "Genders", false)));
            researcherSurveyDetail04.setIncome(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("income", "Incomes", false)));
            researcherSurveyDetail04.setMaritalstatus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("maritalstatus", "Marital Statuses", false)));
            researcherSurveyDetail04.setMinsocialinfluencepercentile(Dropdown.getIntFromRequest("minsocialinfluencepercentile", "Min Social Influence", false));
            researcherSurveyDetail04.setPanels(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("panels", "Panels", false)));
            researcherSurveyDetail04.setPolitics(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("politics", "Politics", false)));
            researcherSurveyDetail04.setProfession(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("professions", "Professions", false)));
            researcherSurveyDetail04.setState(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("states", "States", false)));
            researcherSurveyDetail04.setIsaccesscodeonly(CheckboxBoolean.getValueFromRequest("isaccesscodeonly"));
            researcherSurveyDetail04.setAccesscode(Textbox.getValueFromRequest("accesscode", "Access Code", false, DatatypeString.DATATYPEID));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherSurveyDetail04.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your survey has been saved.");
                researcherSurveyDetail04.saveSurvey();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail04.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<form action="/researcher/researchersurveydetail_04.jsp" method="post" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_04.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail04.getSurvey().getSurveyid()%>"/>


    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Target your survey to the correct demographic.   Be careful not to cast too narrow a net.  In the final step we'll tell you how many bloggers fulfill your criteria and you'll have the opportunity to widen the search.
    <br/><br/><br/>
    </font></div></center>

    <br/><br/>



    <%if (researcherSurveyDetail04.getSurvey().getStatus()>Survey.STATUS_DRAFT) {%>
        <%=researcherSurveyDetail04.getSurveyCriteriaAsHtml()%>
        <br/>
        <b>Panels:</b>
        <%=researcherSurveyDetail04.getPanelsStr()%>
    <%}%>

    <%if (researcherSurveyDetail04.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
        <table cellpadding="0" cellspacing="0" border="0">


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Social Influence Rating (TM)</font>
                    <br/>
                    <font class="smallfont">Social Influence Rating takes site traffic, survey referrals and a number of other metrics into account to give you some measure of this blogger's influence with his/her readership.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("minsocialinfluencepercentile", String.valueOf(researcherSurveyDetail04.getMinsocialinfluencepercentile()), StaticVariables.getPercentiles(), "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Blog Quality of At Least</font>
                    <br/>
                    <font class="smallfont">Blog Quality is determined manually by our administrators visiting each blog post and assigning a general quality rating.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("blogquality", String.valueOf(researcherSurveyDetail04.getBlogquality()), StaticVariables.getBlogqualities(), "", "")%>
                </td>
            </tr>




            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Age Range</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("agemin", String.valueOf(researcherSurveyDetail04.getAgemin()), 5, 3, "", "")%>
                    -
                    <%=Textbox.getHtml("agemax", String.valueOf(researcherSurveyDetail04.getAgemax()), 5, 3, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Gender</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("gender", Util.stringArrayToArrayList(researcherSurveyDetail04.getGender()), Util.treeSetToTreeMap(Genders.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Ethnicity</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("ethnicity", Util.stringArrayToArrayList(researcherSurveyDetail04.getEthnicity()), Util.treeSetToTreeMap(Ethnicities.get()), 6, "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Marital Status</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("maritalstatus", Util.stringArrayToArrayList(researcherSurveyDetail04.getMaritalstatus()), Util.treeSetToTreeMap(Maritalstatuses.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Income</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("income", Util.stringArrayToArrayList(researcherSurveyDetail04.getIncome()), Util.treeSetToTreeMap(Incomes.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Education</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("educationlevel", Util.stringArrayToArrayList(researcherSurveyDetail04.getEducationlevel()), Util.treeSetToTreeMap(Educationlevels.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">State</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("states", Util.stringArrayToArrayList(researcherSurveyDetail04.getState()), Util.treeSetToTreeMap(States.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">City</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("cities", Util.stringArrayToArrayList(researcherSurveyDetail04.getCity()), Util.treeSetToTreeMap(Cities.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Profession</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("professions", Util.stringArrayToArrayList(researcherSurveyDetail04.getProfession()), Util.treeSetToTreeMap(Professions.get()), 6, "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Blog Focus</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("blogfocus", Util.stringArrayToArrayList(researcherSurveyDetail04.getBlogfocus()), Util.treeSetToTreeMap(Blogfocuses.get()), 6, "", "")%>
                </td>
            </tr>


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Politics</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("politics", Util.stringArrayToArrayList(researcherSurveyDetail04.getPolitics()), Util.treeSetToTreeMap(Politics.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Panel Membership</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("panels", Util.stringArrayToArrayList(researcherSurveyDetail04.getPanels()), researcherSurveyDetail04.getPanelsavailable(), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Access Code Only?</font>
                    <br/>
                    <font class="tinyfont">Access Code Only surveys require that everybody who takes the survey first enter an access code that you somehow communicate to them.  In this way you can limit and control who takes your survey.  Great for point-of-sale and real-world ties to the online world.</font>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("isaccesscodeonly", researcherSurveyDetail04.getIsaccesscodeonly(), "", "")%><font class="formfieldnamefont">Yes</font>
                    <br/>
                    <font class="normalfont">Access Code</font>
                    <br/>
                    <%=Textbox.getHtml("accesscode", researcherSurveyDetail04.getAccesscode(), 255, 10, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont"></font>
                </td>
                <td valign="top">
                </td>
            </tr>

        </table>
    <%}%>

    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail04.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>

<%@ include file="/template/footer.jsp" %>