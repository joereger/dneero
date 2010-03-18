<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail04" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="com.dneero.constants.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail04) Pagez.getBeanMgr().get("ResearcherSurveyDetail04")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail04 researcherSurveyDetail04 = (ResearcherSurveyDetail04)Pagez.getBeanMgr().get("ResearcherSurveyDetail04");
%>
<%if (researcherSurveyDetail04.getSurvey().getIsopentoanybody()){
    if (request.getParameter("ispreviousclick")!=null && request.getParameter("ispreviousclick").equals("1")){
        Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid()+"&ispreviousclick=1");
        return;
    }
    Pagez.sendRedirect("/researcher/researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid());
    return;
}%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherSurveyDetail04.getSurvey().getStatus()>Survey.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid()+"&ispreviousclick=1");
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid());
                    return;
                }
            }
            researcherSurveyDetail04.setAgemin(Textbox.getIntFromRequest("agemin", "Age Min", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail04.setAgemax(Textbox.getIntFromRequest("agemax", "Age Max", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail04.setBlogfocus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("blogfocus", "Blog Focus", false)));
            researcherSurveyDetail04.setCity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("cities", "Cities", false)));
            researcherSurveyDetail04.setCountry(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("countries", "Countries", false)));
            researcherSurveyDetail04.setEducationlevel(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("educationlevel", "Education Levels", false)));
            researcherSurveyDetail04.setEthnicity(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("ethnicity", "Ethnicity", false)));
            researcherSurveyDetail04.setGender(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("gender", "Genders", false)));
            researcherSurveyDetail04.setIncome(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("income", "Incomes", false)));
            researcherSurveyDetail04.setMaritalstatus(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("maritalstatus", "Marital Statuses", false)));
            researcherSurveyDetail04.setMinsocialinfluencepercentile(Dropdown.getIntFromRequest("minsocialinfluencepercentile", "Min Social Influence", false));
            researcherSurveyDetail04.setDayssincelastsurvey(Textbox.getIntFromRequest("dayssincelastsurvey", "Days Since Last Survey", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail04.setTotalsurveystakenatleast(Textbox.getIntFromRequest("totalsurveystakenatleast", "Total Conversations Joined of At Least", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail04.setTotalsurveystakenatmost(Textbox.getIntFromRequest("totalsurveystakenatmost", "Total Conversations Joined of At Most", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail04.setPanels(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("panels", "Panels", false)));
            researcherSurveyDetail04.setSuperpanels(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("superpanels", "SuperPanels", false)));
            researcherSurveyDetail04.setPolitics(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("politics", "Politics", false)));
            researcherSurveyDetail04.setDneerousagemethods(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("dneerousagemethods", "Usage Methods", false)));
            researcherSurveyDetail04.setProfession(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("professions", "Professions", false)));
            researcherSurveyDetail04.setState(Util.arrayListToStringArray(DropdownMultiselect.getValueFromRequest("states", "States", false)));
            researcherSurveyDetail04.setIsaccesscodeonly(CheckboxBoolean.getValueFromRequest("isaccesscodeonly"));
            researcherSurveyDetail04.setAccesscode(Textbox.getValueFromRequest("accesscode", "Access Code", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail04.setIsopentoanybody(CheckboxBoolean.getValueFromRequest("isopentoanybody"));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherSurveyDetail04.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                researcherSurveyDetail04.saveSurvey();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail04.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp?surveyid="+researcherSurveyDetail04.getSurvey().getSurveyid()+"&ispreviousclick=1");
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<form action="/researcher/researchersurveydetail_04.jsp" method="post"  class="niceform" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_04.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail04.getSurvey().getSurveyid()%>"/>


        <%
            String isopenChecked = "";
            if (researcherSurveyDetail04.getIsopentoanybody()){
                isopenChecked = "checked=\"checked\"";
            }
        %>
        <input type="checkbox" id="isopentoanybody" name="isopentoanybody" value="1" <%=isopenChecked%> /> Anybody can participate

        <div id="togglepage">

            <a href="#" id="helplink"><img src="/images/helpswitch.gif" alt="Help" border="0" align="right"/></a>
            <div id="togglehelp">
                <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
                Target your conversation to the correct demographic.   Be careful not to cast too narrow a net.  In the final step we'll tell you how many bloggers fulfill your criteria and you'll have the opportunity to widen the search.
                <br/><br/><br/>
                </font></div>
            </div>

            <br/><br/>



            <%if (researcherSurveyDetail04.getSurvey().getStatus()>Survey.STATUS_DRAFT) {%>
                <%=researcherSurveyDetail04.getSurveyCriteriaAsHtml()%>
                <br/>
                <b>Panels:</b>
                <%=researcherSurveyDetail04.getPanelsStr()%>
                <br/>
                <b>SuperPanels:</b>
                <%=researcherSurveyDetail04.getSuperpanelsStr()%>
            <%}%>

            <%if (researcherSurveyDetail04.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                <table cellpadding="5" cellspacing="0" border="0">


                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Social Influence Rating (TM)</font>
                            <br/>
                            <font class="smallfont">Social Influence Rating takes site traffic, conversation referrals and a number of other metrics into account to give you some measure of this social person's influence with his/her readership.</font>
                        </td>
                        <td valign="top">
                            <%=Dropdown.getHtml("minsocialinfluencepercentile", String.valueOf(researcherSurveyDetail04.getMinsocialinfluencepercentile()), StaticVariables.getPercentiles(), "", "")%>
                        </td>


                        <td valign="top">
                            <font class="formfieldnamefont">Usage Method</font>
                            <br/>
                            <font class="tinyfont">There are multiple ways that users can access conversations.  This control allows you to target each access method individually.</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("dneerousagemethods", Util.stringArrayToArrayList(researcherSurveyDetail04.getDneerousagemethods()), Util.treeSetToTreeMap(Dneerousagemethods.get()), 3, "", "")%>
                        </td>
                    </tr>



                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Number of Conversations Joined</font>
                            <br/>
                            <font class="smallfont">Reflects the experience level of the respondent.  Enter a minimum and maximum number of conversations that qualified respondents are allowed to have joined.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("totalsurveystakenatleast", String.valueOf(researcherSurveyDetail04.getTotalsurveystakenatleast()), 5, 4, "", "")%>
                            -
                            <%=Textbox.getHtml("totalsurveystakenatmost", String.valueOf(researcherSurveyDetail04.getTotalsurveystakenatmost()), 5, 4, "", "")%>
                        </td>

                        <td valign="top">
                            <font class="formfieldnamefont">Days Since Taking Last Conversation of At Least</font>
                            <br/>
                            <font class="smallfont">A qualifying respondent must have not taken another conversation in at least this many days.</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("dayssincelastsurvey", String.valueOf(researcherSurveyDetail04.getDayssincelastsurvey()), 5, 3, "", "")%>
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
                            <font class="formfieldnamefont">Country</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("countries", Util.stringArrayToArrayList(researcherSurveyDetail04.getCountry()), Util.treeSetToTreeMap(Countries.get()), 6, "", "")%>
                        </td>

                        <td valign="top">
                            <font class="formfieldnamefont">Politics</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("politics", Util.stringArrayToArrayList(researcherSurveyDetail04.getPolitics()), Util.treeSetToTreeMap(Politics.get()), 6, "", "")%>
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
                            <font class="formfieldnamefont">Panel Membership</font>
                            <br/>
                            <font class="tinyfont">If you select a panel then this convo will only apply to people in that panel that also fulfill other demographic requirements.</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("panels", Util.stringArrayToArrayList(researcherSurveyDetail04.getPanels()), researcherSurveyDetail04.getPanelsavailable(), 6, "", "")%>
                        </td>

                        <td valign="top">
                            <font class="formfieldnamefont">SuperPanel Membership</font>
                            <br/>
                            <font class="tinyfont">If you select a panel then this convo will only apply to people in that panel that also fulfill other demographic requirements.</font>
                        </td>
                        <td valign="top">
                            <%=DropdownMultiselect.getHtml("superpanels", Util.stringArrayToArrayList(researcherSurveyDetail04.getSuperpanels()), researcherSurveyDetail04.getSuperpanelsavailable(), 6, "", "")%>
                        </td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Access Code Only?</font>
                            <br/>
                            <font class="tinyfont">Access Code Only conversations require that everybody who takes the conversation first enter an access code that you somehow communicate to them.  In this way you can limit and control who joins your conversation.  Great for point-of-sale and real-world ties to the online world.</font>
                        </td>
                        <td valign="top">
                            <%=CheckboxBoolean.getHtml("isaccesscodeonly", researcherSurveyDetail04.getIsaccesscodeonly(), "", "")%>
                        </td>

                        <td valign="top">
                            <font class="normalfont">Access Code</font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("accesscode", researcherSurveyDetail04.getAccesscode(), 255, 10, "", "")%>
                        </td>
                    </tr>

                </table>
            <%}%>
    </div>
    <script>
        $("#isopentoanybody").change(function() {
            $("#togglepage").toggle();
        });
        <% if (researcherSurveyDetail04.getIsopentoanybody()){%>
        $("#togglepage").hide();
        <% }  %>
    </script>
    <script>
        $("#helplink").click(function() {
            $("#togglehelp").toggle();
        });
        $("#togglehelp").hide();
    </script>

    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail04.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>

<%@ include file="/template/footer.jsp" %>