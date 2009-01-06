<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail06" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
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
            if (researcherSurveyDetail06.getSurvey().getStatus()>Survey.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail06.getSurvey().getSurveyid());
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchersurveydetail_postlaunch.jsp?surveyid="+researcherSurveyDetail06.getSurvey().getSurveyid());
                    return;
                }
            }
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
                Pagez.sendRedirect("/researcher/researchersurveydetail_postlaunch.jsp?surveyid="+researcherSurveyDetail06.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                Pagez.sendRedirect("/researcher/researchersurveydetail_05.jsp?surveyid="+researcherSurveyDetail06.getSurvey().getSurveyid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("applycoupon"))) {
        try {
            if (researcherSurveyDetail06.getSurvey().getStatus()==Survey.STATUS_DRAFT){
                researcherSurveyDetail06.setCouponcode(Textbox.getValueFromRequest("couponcode", "Coupon Code", true, DatatypeString.DATATYPEID));
                logger.debug("Apply Coupon was clicked");
                researcherSurveyDetail06.applyCoupon();
            } else {
                Pagez.getUserSession().setMessage("Coupons can only be applied to conversations in the draft state... before they launch.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("applyresellercode"))) {
        try {
            if (researcherSurveyDetail06.getSurvey().getStatus()==Survey.STATUS_DRAFT){
                researcherSurveyDetail06.setResellercode(Textbox.getValueFromRequest("resellercode", "Reseller Code", false, DatatypeString.DATATYPEID));
                researcherSurveyDetail06.applyResellerCode();
            } else {
                Pagez.getUserSession().setMessage("Reseller Codes can only be applied to conversations in the draft state... before they launch.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchersurveydetail_06.jsp" method="post" id="rsdform">
    <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_06.jsp">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail06.getSurvey().getSurveyid()%>"/>



    <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;">
        <font class="mediumfont" style="color: #666666;">Financial Summary</font><br/>
        <table cellpadding="0" cellspacing="0" border="0">


            <%--<tr>--%>
                <%--<td valign="top">--%>
                    <%--<font class="formfieldnamefont">Bloggers In System Fulfilling Requirements</font>--%>
                <%--</td>--%>
                <%--<td valign="top">--%>
                    <%--<font class="normalfont"><%=researcherSurveyDetail06.getNumberofbloggersqualifiedforthissurvey()%></font>--%>
                <%--</td>--%>
            <%--</tr>--%>



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
                    <font class="formfieldnamefont">Max Possible Payment for Responses</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getMaxrespondentpayments()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible Payment for Displays to Peers</font>
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
                    <font class="formfieldnamefont">Hide Overall Aggregate Results Fee</font>
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
                    <font class="normalfont"><%=researcherSurveyDetail06.getDneerofee()%></font><font class="normalfont"></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Coupon Discount (Applied to Max)</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherSurveyDetail06.getCoupondiscountamt()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible Spend</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><b><%=researcherSurveyDetail06.getMaxpossiblespend()%></b></font>
                </td>
            </tr>



        </table>
    </div>

    <%if (1==2){%>
        <%if (researcherSurveyDetail06.getWarningnumberofbloggerslessthanrequested()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: You've requested a number of respondents that is larger than the current number of bloggers in the system that fulfill your targeting criteria.  This may or may not be a problem.  Conversations often attract new people to the system... you are in no way limited to the bloggers already signed-up.
            <br/><h:commandLink value="Possible Remedy: Relax the Targeting Criteria" styleClass="smallfont" action="researchersurveydetail_04" immediate="true"/>
            <br/><h:commandLink value="Idea: Increase Your Incentive to Attract New People" styleClass="smallfont" action="researchersurveydetail_05" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningnumberrequestedratiotoobig()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: The ratio of the number of respondents you've requested to the number of bloggers that qualify for your criteria is a little high meaning that it may be difficult to attract enough respondents. This may or may not be a problem.  Conversations often attract new bloggers to the system... you are in no way limited to the bloggers already signed-up.
            <br/><h:commandLink value="Possible Remedy: Relax the Targeting Criteria" styleClass="smallfont" action="researchersurveydetail_04" immediate="true"/>
            <br/><h:commandLink value="Idea: Increase Your Incentive to Attract New Bloggers" styleClass="smallfont" action="researchersurveydetail_05" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningtoomanyquestions()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: You seem to have a large number of questions in your conversation.  There is nothing wrong with this.  But large conversations and blogs may not be the best fit.  First, bloggers are a quick bunch, always having a lot to do. Second, you're asking them to post the conversation to their peers... large conversations may take over a blog at which point the blogger will take the conversation down.  This may not be a problem for you, but we did want you to know about the possible issue.
            <br/><h:commandLink value="Possible Remedy: Adjust the Questions of Your Conversation" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningnoquestions()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: This conversation has no questions.  You may be trying to use the system as an advertising placement tool (which is fine) but we thought that you may have accidentally skipped the question page.
            <br/><h:commandLink value="Possible Remedy: Add Questions to Your Conversation" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
            </font>
        <%}%>

        <%if (researcherSurveyDetail06.getWarningtimeperiodtooshort()){%>
            <img src="/images/lightbulb_on.png"/>
            <font class="smallfont">
            Warning: The conversation time period is rather short.  You may have very good reasons for doing so but we did want to note that conversations take some time to be publicized, signed up for, posted, etc.  30 days is a good safe period of time for a conversation.
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
    <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="normalfont">
                        <ul>
                            <li>I understand that by igniting this conversation I am committing to spending up to <%=researcherSurveyDetail06.getMaxpossiblespend()%> (Max Possible Spend.)</li>
                            <li>Actual charges will be based only on the activities of conversation completion and impressions (i.e. viewing of your conversation on the web.)</li>
                            <li>I understand that my account balance must be sufficient to support activities:
                                <ul>
                                    <li>20% of the <%=researcherSurveyDetail06.getMaxpossiblespend()%> will be charged now.</li>
                                    <li>Whenever my account balance falls below 10% of the sum of the Max Possible Spends for all of my live (open) conversations, additional charges will be made to attain the 20% balance.</li>
                                    <li>If my account balance falls below 5% of the sum of the Max Possible Spends for all of my live (open) conversations, then all my conversations will be put on hold until my account balance is increased.</li>
                                    <li>However, if my account balance is sufficient to complete the activities I have requested, my live (open) conversations will not be put on hold. </li>
                                </ul>
                            </li>
                            <li>Impressions on blogs will be paid for during a period extending 30 days from the end date of the conversation, not to exceed the limits on impressions that you've set with your conversation.  As such, dNeero will refund money after this period when the max number of conversations is not reached.</li>
                        </ul>
                    </font>
                </td>
                <td valign="top" width="35%">
                    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                        <div class="rounded" style="background: #ffffff; text-align: left; padding: 20px;">
                            <center>
                            <font class="formfieldnamefont">Reseller Code:</font>
                            <br/>
                            <%=Textbox.getHtml("resellercode", researcherSurveyDetail06.getResellercode(), 50, 15, "", "font-size: 10px;")%>
                            <br/>
                            <input type="submit" class="formsubmitbutton" value="Apply Reseller Code" onclick="document.getElementById('action').value='applyresellercode';" style="font-size: 10px;">
                            <br/>
                            <font class="tinyfont">Enter Reseller Code so that resellers paid for their efforts.  This will not affect your pricing.</font>
                            </center>
                        </div>
                    </div>
                    <br/><br/>
                    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">

                        <font class="smallfont">
                            <%if (researcherSurveyDetail06.getCoupons()==null || researcherSurveyDetail06.getCoupons().size()==0){%>
                                <!--<font class="tinyfont" style="color: #cccccc;">None.</font>-->
                            <%} else {%>
                                <!--<font class="mediumfont" style="color: #666666;">Coupon</font>
                                <br/> -->
                                <%
                                    ArrayList<GridCol> cols = new ArrayList<GridCol>();
                                    cols.add(new GridCol("", "<b><$name$></b><br/><$description$>", false, "", "tinyfont"));
                                    //cols.add(new GridCol("Code", "<$couponcode$>", false, "", "tinyfont"));
                                    cols.add(new GridCol("", "<$discountpercent$>%", false, "", "tinyfont"));
                                    //cols.add(new GridCol("Start/End", "<$startdate|" + Grid.GRIDCOLRENDERER_DATETIMECOMPACT + "$><br/><$enddate|" + Grid.GRIDCOLRENDERER_DATETIMECOMPACT + "$>", false, "", "tinyfont"));
                                %>
                                <%=Grid.render(researcherSurveyDetail06.getCoupons(), cols, 50, "/sysadmin/researchersurveydetail_06.jsp?surveyid="+researcherSurveyDetail06.getSurvey().getSurveyid(), "page")%>
                            <%}%>
                        </font>
                        <div class="rounded" style="background: #ffffff; text-align: left; padding: 20px;">
                            <center>
                            <font class="formfieldnamefont">Coupon Code:</font>
                            <br/>
                            <%=Textbox.getHtml("couponcode", "", 50, 15, "", "font-size: 10px;")%>
                            <br/>
                            <input type="submit" class="formsubmitbutton" value="Apply Coupon" onclick="document.getElementById('action').value='applycoupon';" style="font-size: 10px;">
                            </center>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
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
                    <input type="submit" class="formsubmitbutton" value="Ignite this Conversation!">
                <%} else {%>
                    <input type="submit" class="formsubmitbutton" value="Next Step">
                <%}%>
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->

</form>

<%@ include file="/template/footer.jsp" %>