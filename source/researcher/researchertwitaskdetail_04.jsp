<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail04" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="com.dneero.constants.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherTwitaskDetail04) Pagez.getBeanMgr().get("ResearcherTwitaskDetail04")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetail04 researcherTwitaskDetail04= (ResearcherTwitaskDetail04)Pagez.getBeanMgr().get("ResearcherTwitaskDetail04");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherTwitaskDetail04.getTwitask().getStatus()>Twitask.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid());
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_05.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid());
                    return;
                }
            }
            researcherTwitaskDetail04.setAgemin(Textbox.getIntFromRequest("agemin", "Age Min", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setAgemax(Textbox.getIntFromRequest("agemax", "Age Max", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setBlogfocus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("blogfocus", "Blog Focus", false)));
            researcherTwitaskDetail04.setCity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("cities", "Cities", false)));
            researcherTwitaskDetail04.setCountry(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("countries", "Countries", false)));
            researcherTwitaskDetail04.setEducationlevel(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("educationlevel", "Education Levels", false)));
            researcherTwitaskDetail04.setEthnicity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("ethnicity", "Ethnicity", false)));
            researcherTwitaskDetail04.setGender(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("gender", "Genders", false)));
            researcherTwitaskDetail04.setIncome(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("income", "Incomes", false)));
            researcherTwitaskDetail04.setMaritalstatus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("maritalstatus", "Marital Statuses", false)));
            researcherTwitaskDetail04.setMinsocialinfluencepercentile(Dropdown.getIntFromRequest("minsocialinfluencepercentile", "Min Social Influence", false));
            researcherTwitaskDetail04.setDayssincelastsurvey(Textbox.getIntFromRequest("dayssincelastsurvey", "Days Since Last Survey", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setTotalsurveystakenatleast(Textbox.getIntFromRequest("totalsurveystakenatleast", "Total Conversations Joined of At Least", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setTotalsurveystakenatmost(Textbox.getIntFromRequest("totalsurveystakenatmost", "Total Conversations Joined of At Most", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail04.setPanels(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("panels", "Panels", false)));
            researcherTwitaskDetail04.setSuperpanels(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("superpanels", "SuperPanels", false)));
            researcherTwitaskDetail04.setPolitics(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("politics", "Politics", false)));
            researcherTwitaskDetail04.setDneerousagemethods(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("dneerousagemethods", "dNeero Usage Methods", false)));
            researcherTwitaskDetail04.setProfession(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("professions", "Professions", false)));
            researcherTwitaskDetail04.setState(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("states", "States", false)));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherTwitaskDetail04.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_05.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                researcherTwitaskDetail04.save();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherTwitaskDetail04.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+ researcherTwitaskDetail04.getTwitask().getTwitaskid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<form action="/researcher/researchertwitaskdetail_04.jsp" method="post" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchertwitaskdetail_04.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail04.getTwitask().getTwitaskid()%>"/>


    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Target your Twitter Question to the correct demographic.   Be careful not to cast too narrow a net.  Often it's best to start with campaigns open to all... or all U.S., for example.
    <br/><br/><br/>
    </font></div></center>

    <br/><br/>



    <%if (researcherTwitaskDetail04.getTwitask().getStatus()>Twitask.STATUS_DRAFT) {%>
        <%=researcherTwitaskDetail04.getSurveyCriteriaAsHtml()%>
        <br/>
        <b>Panels:</b>
        <%=researcherTwitaskDetail04.getPanelsStr()%>
        <br/>
        <b>SuperPanels:</b>
        <%=researcherTwitaskDetail04.getSuperpanelsStr()%>
    <%}%>

    <%if (researcherTwitaskDetail04.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
        <table cellpadding="5" cellspacing="0" border="0">


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Social Influence Rating (TM)</font>
                    <br/>
                    <font class="smallfont">Social Influence Rating takes site traffic, conversation referrals and a number of other metrics into account to give you some measure of this social person's influence with his/her readership.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("minsocialinfluencepercentile", String.valueOf(researcherTwitaskDetail04.getMinsocialinfluencepercentile()), StaticVariables.getPercentiles(), "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">dNeero Usage Method</font>
                    <br/>
                    <font class="tinyfont">There are multiple ways that users can access dNeero social surveys.  This control allows you to target each access method individually.</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("dneerousagemethods", Util.stringArrayToArrayList(researcherTwitaskDetail04.getDneerousagemethods()), Util.treeSetToTreeMap(Dneerousagemethods.get()), 3, "", "")%>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Number of Conversations Joined</font>
                    <br/>
                    <font class="smallfont">Reflects the experience level of the respondent.  Enter a minimum and maximum number of conversations that qualified respondents are allowed to have joined.</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("totalsurveystakenatleast", String.valueOf(researcherTwitaskDetail04.getTotalsurveystakenatleast()), 5, 4, "", "")%>
                    -
                    <%=Textbox.getHtml("totalsurveystakenatmost", String.valueOf(researcherTwitaskDetail04.getTotalsurveystakenatmost()), 5, 4, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Days Since Taking Last Conversation of At Least</font>
                    <br/>
                    <font class="smallfont">A qualifying respondent must have not taken another conversation in at least this many days.</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("dayssincelastsurvey", String.valueOf(researcherTwitaskDetail04.getDayssincelastsurvey()), 5, 3, "", "")%>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Age Range</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("agemin", String.valueOf(researcherTwitaskDetail04.getAgemin()), 5, 3, "", "")%>
                    -
                    <%=Textbox.getHtml("agemax", String.valueOf(researcherTwitaskDetail04.getAgemax()), 5, 3, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Gender</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("gender", Util.stringArrayToArrayList(researcherTwitaskDetail04.getGender()), Util.treeSetToTreeMap(Genders.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Ethnicity</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("ethnicity", Util.stringArrayToArrayList(researcherTwitaskDetail04.getEthnicity()), Util.treeSetToTreeMap(Ethnicities.get()), 6, "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Marital Status</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("maritalstatus", Util.stringArrayToArrayList(researcherTwitaskDetail04.getMaritalstatus()), Util.treeSetToTreeMap(Maritalstatuses.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Income</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("income", Util.stringArrayToArrayList(researcherTwitaskDetail04.getIncome()), Util.treeSetToTreeMap(Incomes.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Education</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("educationlevel", Util.stringArrayToArrayList(researcherTwitaskDetail04.getEducationlevel()), Util.treeSetToTreeMap(Educationlevels.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">State</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("states", Util.stringArrayToArrayList(researcherTwitaskDetail04.getState()), Util.treeSetToTreeMap(States.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">City</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("cities", Util.stringArrayToArrayList(researcherTwitaskDetail04.getCity()), Util.treeSetToTreeMap(Cities.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Country</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("countries", Util.stringArrayToArrayList(researcherTwitaskDetail04.getCountry()), Util.treeSetToTreeMap(Countries.get()), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">Politics</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("politics", Util.stringArrayToArrayList(researcherTwitaskDetail04.getPolitics()), Util.treeSetToTreeMap(Politics.get()), 6, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Profession</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("professions", Util.stringArrayToArrayList(researcherTwitaskDetail04.getProfession()), Util.treeSetToTreeMap(Professions.get()), 6, "", "")%>
                </td>


                <td valign="top">
                    <font class="formfieldnamefont">Blog Focus</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("blogfocus", Util.stringArrayToArrayList(researcherTwitaskDetail04.getBlogfocus()), Util.treeSetToTreeMap(Blogfocuses.get()), 6, "", "")%>
                </td>
            </tr>




            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel Membership</font>
                    <br/>
                    <font class="tinyfont">If you select a panel then this convo will only apply to people in that panel that also fulfill other demographic requirements.</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("panels", Util.stringArrayToArrayList(researcherTwitaskDetail04.getPanels()), researcherTwitaskDetail04.getPanelsavailable(), 6, "", "")%>
                </td>

                <td valign="top">
                    <font class="formfieldnamefont">SuperPanel Membership</font>
                    <br/>
                    <font class="tinyfont">If you select a panel then this convo will only apply to people in that panel that also fulfill other demographic requirements.</font>
                </td>
                <td valign="top">
                    <%=DropdownMultiselect.getHtml("superpanels", Util.stringArrayToArrayList(researcherTwitaskDetail04.getSuperpanels()), researcherTwitaskDetail04.getSuperpanelsavailable(), 6, "", "")%>
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
                <%if (researcherTwitaskDetail04.getTwitask().getStatus()==Twitask.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>

<%@ include file="/template/footer.jsp" %>