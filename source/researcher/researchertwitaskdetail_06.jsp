<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail06" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherTwitaskDetail06) Pagez.getBeanMgr().get("ResearcherTwitaskDetail06")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetail06 researcherTwitaskDetail06= (ResearcherTwitaskDetail06)Pagez.getBeanMgr().get("ResearcherTwitaskDetail06");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherTwitaskDetail06.getTwitask().getStatus()>Twitask.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_05.jsp?twitaskid="+ researcherTwitaskDetail06.getTwitask().getTwitaskid());
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_postlaunch.jsp?twitaskid="+ researcherTwitaskDetail06.getTwitask().getTwitaskid());
                    return;
                }
            }
            researcherTwitaskDetail06.setCccity(Textbox.getValueFromRequest("cccity", "City", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail06.setCcexpmo(Textbox.getIntFromRequest("ccexpmo", "Expiration Month", false, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail06.setCcexpyear(Textbox.getIntFromRequest("ccexpyear", "Expiration Year", false, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail06.setCcnum(Textbox.getValueFromRequest("ccnum", "Credit Card Number", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail06.setCcstate(Textbox.getValueFromRequest("ccstate", "State", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail06.setCctype(Dropdown.getIntFromRequest("cctype", "Credit Card Type", false));
            researcherTwitaskDetail06.setCvv2(Dropdown.getValueFromRequest("cvv2", "CVV2", false));
            researcherTwitaskDetail06.setFirstname(Dropdown.getValueFromRequest("firstname", "First Name", false));
            researcherTwitaskDetail06.setLastname(Dropdown.getValueFromRequest("lastname", "Last Name", false));
            researcherTwitaskDetail06.setPostalcode(Dropdown.getValueFromRequest("postalcode", "Postal Code", false));
            researcherTwitaskDetail06.setStreet(Dropdown.getValueFromRequest("street", "Street", false));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherTwitaskDetail06.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_postlaunch.jsp?twitaskid="+ researcherTwitaskDetail06.getTwitask().getTwitaskid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_05.jsp?twitaskid="+ researcherTwitaskDetail06.getTwitask().getTwitaskid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>

<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("applyresellercode"))) {
        try {
            if (researcherTwitaskDetail06.getTwitask().getStatus()==Twitask.STATUS_DRAFT){
                researcherTwitaskDetail06.setResellercode(Textbox.getValueFromRequest("resellercode", "Reseller Code", false, DatatypeString.DATATYPEID));
                researcherTwitaskDetail06.applyResellerCode();
            } else {
                Pagez.getUserSession().setMessage("Reseller Codes can only be applied to conversations in the draft state... before they launch.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchertwitaskdetail_06.jsp" method="post" id="rsdform">
    <input type="hidden" name="dpage" value="/researcher/researchertwitaskdetail_06.jsp">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail06.getTwitask().getTwitaskid()%>"/>



    <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;">
        <font class="mediumfont" style="color: #666666;">Financial Summary</font><br/>
        <table cellpadding="0" cellspacing="0" border="0">


            <%--<tr>--%>
                <%--<td valign="top">--%>
                    <%--<font class="formfieldnamefont">Bloggers In System Fulfilling Requirements</font>--%>
                <%--</td>--%>
                <%--<td valign="top">--%>
                    <%--<font class="normalfont"><%=researcherTwitaskDetail06.getNumberofbloggersqualifiedforthissurvey()%></font>--%>
                <%--</td>--%>
            <%--</tr>--%>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Survey Start Date/Time</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherTwitaskDetail06.getStartdate()%></font>
                </td>
            </tr>


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible Payment for Responses</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherTwitaskDetail06.getMaxrespondentpayments()%></font>
                </td>
            </tr>


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">TwitterAsk Creation Fee</font>
                </td>
                <td valign="top">
                    <font class="normalfont">$5.00</font>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible dNeero Fee</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><%=researcherTwitaskDetail06.getDneerofee()%></font><font class="normalfont"></font>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Max Possible Spend</font>
                </td>
                <td valign="top">
                    <font class="normalfont"><b><%=researcherTwitaskDetail06.getMaxpossiblespend()%></b></font>
                </td>
            </tr>



        </table>
    </div>





     <%if (researcherTwitaskDetail06.getWarningdonthaveccinfo()){%>
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
                    <%=Textbox.getHtml("firstname", researcherTwitaskDetail06.getFirstname(), 255, 15, "", "")%>
                    <%=Textbox.getHtml("lastname", researcherTwitaskDetail06.getLastname(), 255, 15, "", "")%>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Street Address</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("street", researcherTwitaskDetail06.getStreet(), 255, 30, "", "")%>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">City, State, Zip</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("cccity", researcherTwitaskDetail06.getCccity(), 255, 20, "", "")%>
                    <%=Textbox.getHtml("ccstate", researcherTwitaskDetail06.getCcstate(), 255, 2, "", "")%>
                    <%=Textbox.getHtml("postalcode", researcherTwitaskDetail06.getPostalcode(), 255, 6, "", "")%>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Credit Card Type</font>
               </td>
               <td valign="top" align="left">
                    <%=Dropdown.getHtml("cctype", String.valueOf(researcherTwitaskDetail06.getCctype()), researcherTwitaskDetail06.getCreditcardtypes(), "","")%>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Credit Card Number</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("ccnum", researcherTwitaskDetail06.getCcnum(), 255, 18, "", "")%>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">Expiration Date</font>
               </td>
               <td valign="top" align="left">
                    <%=Dropdown.getHtml("ccexpmo", String.valueOf(researcherTwitaskDetail06.getCcexpmo()), researcherTwitaskDetail06.getMonthsForCreditcard(), "","")%>
                    /
                    <%=Dropdown.getHtml("ccexpyear", String.valueOf(researcherTwitaskDetail06.getCcexpyear()), researcherTwitaskDetail06.getYearsForCreditcard(), "","")%>
               </td>
            </tr>



            <tr>
               <td valign="top" align="left">
                    <font class="formfieldnamefont">CVV2</font>
                    <br/>
                    <font class="tinyfont">(three digit number on back of card)</font>
               </td>
               <td valign="top" align="left">
                    <%=Textbox.getHtml("cvv2", researcherTwitaskDetail06.getCvv2(), 255, 3, "", "")%>
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
                            <li>I understand that by igniting this conversation I am committing to spending up to <%=researcherTwitaskDetail06.getMaxpossiblespend()%> (Max Possible Spend.)</li>
                            <li>Actual charges will be based only on the activities of conversation completion and impressions (i.e. viewing of your conversation on the web.)</li>
                            <li>I understand that my account balance must be sufficient to support activities:
                                <ul>
                                    <li>20% of the <%=researcherTwitaskDetail06.getMaxpossiblespend()%> will be charged now.</li>
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
                            <%=Textbox.getHtml("resellercode", researcherTwitaskDetail06.getResellercode(), 50, 15, "", "font-size: 10px;")%>
                            <br/>
                            <input type="submit" class="formsubmitbutton" value="Apply Reseller Code" onclick="document.getElementById('action').value='applyresellercode';" style="font-size: 10px;">
                            <br/>
                            <font class="tinyfont">Enter Reseller Code so that resellers paid for their efforts.  This will not affect your pricing.</font>
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
                <%if (researcherTwitaskDetail06.getTwitask().getStatus()==Twitask.STATUS_DRAFT) {%>
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