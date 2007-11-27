<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail06" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-06.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail06) Pagez.getBeanMgr().get("ResearcherSurveyDetail06")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail06 researcherSurveyDetail06 = (ResearcherSurveyDetail06)Pagez.getBeanMgr().get("ResearcherSurveyDetail06");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            researcherSurveyDetail06.setCccity(Textbox.getValueFromRequest("cccity", "City", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail06.setCcexpmo(Textbox.getIntFromRequest("ccexpmo", "Expiration Month", false, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail06.setCcexpyear(Textbox.getIntFromRequest("ccexpyear", "Expiration Year", false, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail06.setCcnum(Textbox.getValueFromRequest("ccnum", "Credit Card Number", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail06.setCcstate(Textbox.getValueFromRequest("ccstate", "State", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail06.setCctype(Dropdown.getIntFromRequest("cctype", "Credit Card Type", false));
            researcherSurveyDetail06.setCvv2(Dropdown.getValueFromRequest("cvv2", "CVV2", false));
            researcherSurveyDetail06.setFirstname(Dropdown.getValueFromRequest("firstname", "First Name", false));
            researcherSurveyDetail06.setLastname(Dropdown.getValueFromRequest("lastname", "Last Name", false));
            researcherSurveyDetail06.setPostalcode(Dropdown.getValueFromRequest("postalcode", "Postal Code", false));
            researcherSurveyDetail06.setStreet(Dropdown.getValueFromRequest("street", "Street", false));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherSurveyDetail06.saveSurvey();
                Pagez.sendRedirect("researchersurveydetail_postlaunch.jsp?surveyid="+researcherSurveyDetail06.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your survey has been saved.");
                Pagez.sendRedirect("index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                Pagez.sendRedirect("researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail06.getSurvey().getSurveyid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="researchersurveydetail_06.jsp" method="post" id="rsdform">
    <input type="hidden" name="dpage" value="\researcher\researchersurveydetail_06.jsp">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail06.getSurvey().getSurveyid()%>"/>



    <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;">
        <table cellpadding="0" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Bloggers In System Fulfilling Requirements</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getNumberofbloggersqualifiedforthissurvey()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Number of Questions in Survey</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getNumberofquestions()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Survey Start Date/Time</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getStartdate()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Survey End Date/Time</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getEnddate()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible Payment for Survey Responses</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getMaxrespondentpayments()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible Payment for Survey Displays on Blogs</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getMaximpressionpayments()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Survey Creation Fee</font>
                </td>
                <td valign="top">
                    <font class="normalfont">$5.00</font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Hide Survey Overall Aggregate Results Fee</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getHideresultsfee()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible dNeero Fee</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getDneerofee()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible Spend</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getMaxpossiblespend()%></font>
                </td>
            </tr>



        </table>
    </div>

    <%if (1==2){%>
        <%if (researcherSurveyDetail06.getWarningnumberofbloggerslessthanrequested()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: You've requested a number of survey respondents that is larger than the current number of bloggers in the system that fulfill your targeting criteria.  This may or may not be a problem.  Surveys often attract new bloggers to the system... you are in no way limited to the bloggers already signed-up.
            <br/><h:commandLink value="Possible Remedy: Relax the Targeting Criteria" styleClass="smallfont" action="researchersurveydetail_04" immediate="true"/>
            <br/><h:commandLink value="Idea: Increase Your Incentive to Attract New Bloggers" styleClass="smallfont" action="researchersurveydetail_05" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningnumberrequestedratiotoobig()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: The ratio of the number of respondents you've requested to the number of bloggers that qualify for your criteria is a little high meaning that it may be difficult to attract enough respondents. This may or may not be a problem.  Surveys often attract new bloggers to the system... you are in no way limited to the bloggers already signed-up.
            <br/><h:commandLink value="Possible Remedy: Relax the Targeting Criteria" styleClass="smallfont" action="researchersurveydetail_04" immediate="true"/>
            <br/><h:commandLink value="Idea: Increase Your Incentive to Attract New Bloggers" styleClass="smallfont" action="researchersurveydetail_05" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningtoomanyquestions()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: You seem to have a large number of questions in your survey.  There is nothing wrong with this.  But large surveys and blogs may not be the best fit.  First, bloggers are a quick bunch, always having a lot to do. Second, you're asking them to post the survey to their blogs... large surveys may take over a blog at which point the blogger will take the survey down.  This may not be a problem for you, but we did want you to know about the possible issue.
            <br/><h:commandLink value="Possible Remedy: Adjust the Questions of Your Survey" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningnoquestions()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: This survey has no questions.  You may be trying to use the system as an advertising placement tool (which is fine) but we thought that you may have accidentally skipped the question page.
            <br/><h:commandLink value="Possible Remedy: Add Questions to Your Survey" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningtimeperiodtooshort()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: The survey time period is rather short.  You may have very good reasons for doing so but we did want to note that surveys take some time to be publicized, signed up for, posted, etc.  30 days is a good safe period of time for a survey.
            <br/><h:commandLink value="Possible Remedy: Adjust the Start and End Dates" styleClass="smallfont" action="researchersurveydetail_01" immediate="true"/>
            </font>
        <%}%>
    <%}%>



     <%if (researcherSurveyDetail06.getWarningdonthaveccinfo()){%>
        <div class="rounded" style="background: #ffffff; text-align: left; padding: 20px;">
            <font class="mediumfont" style="color: #cccccc;">Credit Card Info</font>
            <br/>

            <table cellpadding="3" cellspacing="0" border="0">

            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Name</font>
                    <br/>
                    <font class="tinyfont">(first then last)</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("firstname", researcherSurveyDetail06.getFirstname(), 255, 15, "", "")%>
                    <%=Textbox.getHtml("lastname", researcherSurveyDetail06.getLastname(), 255, 15, "", "")%>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Street Address</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("street", researcherSurveyDetail06.getStreet(), 255, 30, "", "")%>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">City, State, Zip</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("cccity", researcherSurveyDetail06.getCccity(), 255, 20, "", "")%>
                    <%=Textbox.getHtml("ccstate", researcherSurveyDetail06.getCcstate(), 255, 2, "", "")%>
                    <%=Textbox.getHtml("postalcode", researcherSurveyDetail06.getPostalcode(), 255, 6, "", "")%>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Credit Card Type</font>
               </td>
               <td valign="top" align="left">
                    <%=Dropdown.getHtml("cctype", String.valueOf(researcherSurveyDetail06.getCctype()), researcherSurveyDetail06.getCreditcardtypes(), "","")%>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Credit Card Number</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("ccnum", researcherSurveyDetail06.getCcnum(), 255, 18, "", "")%>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Expiration Date</font>
               </td>
               <td valign="top" align="left">
                    <%=Dropdown.getHtml("ccexpmo", String.valueOf(researcherSurveyDetail06.getCcexpmo()), researcherSurveyDetail06.getMonthsForCreditcard(), "","")%>
                    /
                    <%=Dropdown.getHtml("ccexpyear", String.valueOf(researcherSurveyDetail06.getCcexpyear()), researcherSurveyDetail06.getYearsForCreditcard(), "","")%>
               </td>
            </tr>



            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">CVV2</font>
                    <br/>
                    <font class="tinyfont">(three digit number on back of card)</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("cvv2", researcherSurveyDetail06.getCvv2(), 255, 3, "", "")%>
               </td>
            </tr>

            </table>


            </div>
        <%}%>


    <br/><br/>
    <font class="mediumfont">
        <ul>
            <li>I understand that by launching this survey I am committing to spending up to <%=researcherSurveyDetail06.getMaxpossiblespend()%> (Max Possible Spend.)</li>
            <li>Actual charges will be based only on the activities of survey completion and impressions (i.e. viewing of your survey on blogs.)</li>
            <li>I understand that my account balance must be sufficient to support activities:
                <ul>
                    <li>20% of the <%=researcherSurveyDetail06.getMaxpossiblespend()%> will be charged now.</li>
                    <li>Whenever my account balance falls below 10% of the sum of the Max Possible Spends for all of my live (open) surveys, additional charges will be made to attain the 20% balance.</li>
                    <li>If my account balance falls below 5% of the sum of the Max Possible Spends for all of my live (open) surveys, then all my surveys will be put on hold until my account balance is increased.</li>
                    <li>However, if my account balance is sufficient to complete the activities I have requested, my live (open) surveys will not be put on hold. </li>
                </ul>
            </li>
            <li>Impressions on blogs will be paid for during a period extending 30 days from the end date of the survey, not to exceed the limits on impressions that you've set with your survey.  As such, dNeero will refund money after this period when the max number of surveys is not reached.</li>
        </ul>
    </font>

    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail06.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->

</form>

<%@ include file="/template/footer.jsp" %>