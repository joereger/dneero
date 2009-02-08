<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail05" %>
<%@ page import="com.dneero.incentive.IncentiveCash" %>
<%@ page import="com.dneero.incentive.IncentiveCoupon" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-05.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail05) Pagez.getBeanMgr().get("ResearcherSurveyDetail05")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail05 researcherSurveyDetail05 = (ResearcherSurveyDetail05)Pagez.getBeanMgr().get("ResearcherSurveyDetail05");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherSurveyDetail05.getSurvey().getStatus()>Survey.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchersurveydetail_04.jsp?surveyid="+researcherSurveyDetail05.getSurvey().getSurveyid());
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchersurveydetail_06.jsp?surveyid="+researcherSurveyDetail05.getSurvey().getSurveyid());
                    return;
                }
            }
            researcherSurveyDetail05.setIscharityonly(CheckboxBoolean.getValueFromRequest("ischarityonly"));
            researcherSurveyDetail05.setCharityonlyallowcustom(CheckboxBoolean.getValueFromRequest("charityonlyallowcustom"));
            researcherSurveyDetail05.setIsresultshidden(CheckboxBoolean.getValueFromRequest("isresultshidden"));
            researcherSurveyDetail05.setCharitycustom(Textbox.getValueFromRequest("charitycustom", "Charity Name", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail05.setCharitycustomurl(Textbox.getValueFromRequest("charitycustomurl", "Charity Url", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail05.setMaxdisplaysperblog(Textbox.getIntFromRequest("maxdisplaysperblog", "Max Displays Per Blog", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail05.setMaxdisplaystotal(Textbox.getIntFromRequest("maxdisplaystotal", "Max Displays Total", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail05.setNumberofrespondentsrequested(Textbox.getIntFromRequest("numberofrespondentsrequested", "Number of RespondentsRequested", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail05.setWillingtopaypercpm(Textbox.getDblFromRequest("willingtopaypercpm", "Willing to Pay Per 1000 Impressions", true, DatatypeDouble.DATATYPEID));
            researcherSurveyDetail05.setWillingtopayperrespondent(Textbox.getDblFromRequest("willingtopayperrespondent", "Willing to Pay Per Respondent", false, DatatypeDouble.DATATYPEID));
            researcherSurveyDetail05.setIncentivetype(Radio.getIntFromRequest("incentivetype", "Incentive Type", true, DatatypeInteger.DATATYPEID));
            researcherSurveyDetail05.setCoupontitle(Textbox.getValueFromRequest("coupontitle", "Coupon Title", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail05.setCoupondescription(Textbox.getValueFromRequest("coupondescription", "Coupon Description", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail05.setCouponinstructions(Textbox.getValueFromRequest("couponinstructions", "Coupon Instructions", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail05.setCouponcodeprefix(Textbox.getValueFromRequest("couponcodeprefix", "Coupon Code Prefix", false, DatatypeString.DATATYPEID));
            researcherSurveyDetail05.setCouponcodeaddrandompostfix(CheckboxBoolean.getValueFromRequest("couponcodeaddrandompostfix"));
            researcherSurveyDetail05.setCouponestimatedcashvalue(Textbox.getDblFromRequest("couponestimatedcashvalue", "Coupon Estimated Cash Value", false, DatatypeDouble.DATATYPEID));

            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherSurveyDetail05.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_06.jsp?surveyid="+researcherSurveyDetail05.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                researcherSurveyDetail05.saveSurvey();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail05.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_04.jsp?surveyid="+researcherSurveyDetail05.getSurvey().getSurveyid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchersurveydetail_05.jsp" method="post" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_05.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail05.getSurvey().getSurveyid()%>"/>


    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    In this step you'll choose how much you're willing to pay bloggers. You'll want to craft an incentive that generates the response you're looking for.
    <br/><br/>
    Example 1: You have a new product to announce to the blogosphere.  You need to find new bloggers to talk about your product and you then need them to post the conversation to their blogs to tell their friends and families.  Your pricing should be a good balance of Conversation Participation Incentive and Peer Display Incentive.
    <br/><br/>
    Example 2: You want to see what bloggers think about a new concept.  Being more concerned about the research side of the equation, you may pay a lot for respondents and almost nothing for them to post the conversation to their blogs.
    <br/><br/>
    The possibilities are endless... give it some thought and create a great incentive.  The better the incentive the more activity you'll generate.
    </font></div></center>

    <br/><br/>

    <table cellpadding="5" cellspacing="0" border="0">

        <tr>
            <td valign="top" width="45%">
            </td>
            <td valign="top">
            </td>
        </tr>

        <tr>
            <td valign="top" colspan="2">
                <font class="mediumfont">Number of Respondents Requested</font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="smallfont">The number of people that you would like to have fill out the conversation and post to their blogs.  Once this number is reached no more people can join the conversation as paid participants.  The minimum is 100.</font><br/>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("numberofrespondentsrequested", String.valueOf(researcherSurveyDetail05.getNumberofrespondentsrequested()), 20, 7, "", "")%> <font class="formfieldnamefont">people</font> 
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getNumberofrespondentsrequested()%> people</font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top" colspan="2">
                <br/><br/>
                <font class="mediumfont">Conversation Participation Incentive (Choose One)</font><br/>
                <font class="smallfont">Amount to award a person who fulfills the targeting criteria and successfully joins the conversation.  Respondents will only earn this amount after they've joined your conversation *and* posted it to their peers for a period of time.  Awarding more will attract more people.</font><br/>
                <br/>
            </td>
            <!--<td valign="top">-->
                <!---->
            <!--</td>-->
        </tr>


        <tr>
            <!--<td valign="top">-->
                <!--<font class="formfieldnamefont">Incentive Per Respondent (Choose One)</font>-->
                <!--<br/>-->
                <!--<font class="smallfont">Amount to award a person who fulfills the targeting criteria and successfully joins the conversation.  Awarding more will attract more people.</font>-->
            <!--</td>-->
            <td valign="top" colspan="2">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <table cellpadding="2" cellspacing="1" border="0">
                        <tr>
                            <td valign="top">
                                    <%=Radio.getHtml("incentivetype", String.valueOf(researcherSurveyDetail05.getIncentivetype()), String.valueOf(IncentiveCash.ID), "", "")%>
                            </td>
                            <td valign="top" colspan="2">
                                    <font class="formfieldnamefont">Cash Incentive  (You define cost/person)</font><br/>
                                    <font class="tinyfont">Amount you'll pay per respondent... i.e. 2.50.  Minimum is 0.10.</font><br/>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" width="5%">

                            </td>
                            <td valign="top" width="40%">
                                <font class="formfieldnamefont">$</font>
                                <%=Textbox.getHtml("willingtopayperrespondent", String.valueOf(researcherSurveyDetail05.getWillingtopayperrespondent()), 255, 10, "", "")%>
                                <br/><br/>
                            </td>
                            <td valign="top">
     
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">
                                    <%=Radio.getHtml("incentivetype", String.valueOf(researcherSurveyDetail05.getIncentivetype()), String.valueOf(IncentiveCoupon.ID), "", "")%>
                            </td>
                            <td valign="top" colspan="2">
                                    <font class="formfieldnamefont">Coupon Incentive (Costs you $0.25/person)</font><br/>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">A title for your coupon.  Short and sweet. Ex: Save $5 on a $20 Meal at McDonald's</font>
                            </td>
                            <td valign="top">
                                    <%=Textbox.getHtml("coupontitle", String.valueOf(researcherSurveyDetail05.getCoupontitle()), 60, 40, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">A full description of what the respondent gets with the coupon, where they can redeem it, etc.</font>
                            </td>
                            <td valign="top">
                                    <%=Textarea.getHtml("coupondescription", String.valueOf(researcherSurveyDetail05.getCoupondescription()), 2, 25, "", "font-size: 9px;")%>
                                    <%//=Textbox.getHtml("coupondescription", String.valueOf(researcherSurveyDetail05.getCoupondescription()), 255, 25, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">A set of redemption instructions that the user will see after they're awarded the coupon.  These instructions will be emailed to respondents who successfully post the conversation to their peers for five days.  They'll also be available online.</font>
                            </td>
                            <td valign="top">
                                    <%=Textarea.getHtml("couponinstructions", String.valueOf(researcherSurveyDetail05.getCouponinstructions()), 2, 25, "", "font-size: 9px;")%>
                                    <%//=Textbox.getHtml("couponinstructions", String.valueOf(researcherSurveyDetail05.getCouponinstructions()), 255, 25, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">The estimated cash value of your coupon.  Doesn't have to be exact but should accurately reflect what they'll get.</font>
                            </td>
                            <td valign="top">
                                    <font class="formfieldnamefont">$</font>
                                    <%=Textbox.getHtml("couponestimatedcashvalue", String.valueOf(researcherSurveyDetail05.getCouponestimatedcashvalue()), 10, 10, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">Coupon prefix.  The first characters of a coupon code.  No spaces.  Example: "MYCOUPON"</font>
                            </td>
                            <td valign="top">
                                    <%=Textbox.getHtml("couponcodeprefix", String.valueOf(researcherSurveyDetail05.getCouponcodeprefix()), 10, 10, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">Include a random alphanumeric postfix with the coupon code?  For example, if MYCOUPON is the prefix, coupons will look like: MYCOUPONFGHGD and MYCOUPONHJHY.  This makes each coupon unique and trackable.</font>
                            </td>
                            <td valign="top">

                                    <%=CheckboxBoolean.getHtml("couponcodeaddrandompostfix", researcherSurveyDetail05.getCouponcodeaddrandompostfix(), "", "")%> <font class="tinyfont">Yes, include a random alphanumeric postfix.</font>
                                    <%//=Textbox.getHtml("couponcodeaddrandompostfix", String.valueOf(researcherSurveyDetail05.getCouponcodeaddrandompostfix()), 10, 10, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                    </table>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getWillingtopayperrespondent()%></font>
                <%}%>
            </td>
        </tr>





        <tr>
            <td valign="top">
                <br/><br/>
                <font class="mediumfont">Peer Posting Incentive</font>
            </td>
            <td valign="top">
            </td>
        </tr>


        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Willing to Pay Per Thousand Conversation Displays to a Peer (CPM) ($USD)</font>
                <br/>
                <font class="smallfont">Once conversations are joined they are posted to a person's blog or social network profile.  With this value you determine what you're willing to pay for 1000 displays (CPM) of your conversation.  This value must be at least $0.25 (unless you're using a Coupon Incentive above) to cover bandwidth costs and can go as high as $1000 ($1000 would be equivalent to $1 per display).  The more you pay the more you attract bloggers who will display your conversation prominently on their blog.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <font class="formfieldnamefont">$</font>    
                    <%=Textbox.getHtml("willingtopaypercpm", String.valueOf(researcherSurveyDetail05.getWillingtopaypercpm()), 255, 9, "", "")%>
                <%} else {%>
                    <font class="formfieldnamefont">$</font>
                    <font class="normalfont"><%=researcherSurveyDetail05.getWillingtopaypercpm()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Max Displays Per Account</font>
                <br/>
                <font class="smallfont">You may want to cap the maximum number of displays that a social person can get paid for.  Your conversation will continue to be displayed if the blog is visited over this number of times but you won't be charged for it.  It is tempting to set this number low, but be cautious... bloggers will quickly realize that they don't stand to make money by displaying your conversation... so they won't.  The minimum value is 1000.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("maxdisplaysperblog", String.valueOf(researcherSurveyDetail05.getMaxdisplaysperblog()), 255, 10, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getMaxdisplaysperblog()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Max Displays Total</font>
                <br/>
                <font class="smallfont">This is a safety check that allows you to cap the total number of displays that you're willing to pay for.  Note that this value is not simply the number of respondents requested multiplied by the max conversations per account... many social people have multiple blogs and can get paid on each one separately.  The minimum value is 25% of Number of Respondents Requested multiplied by Max Displays Per Account.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("maxdisplaystotal", String.valueOf(researcherSurveyDetail05.getMaxdisplaystotal()), 255, 10, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getMaxdisplaystotal()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <br/><br/>
                <font class="mediumfont">Charity Options</font>
            </td>
            <td valign="top">
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Only if Blogger Lets <%=Pagez.getUserSession().getPl().getNameforui()%> Give Earnings to Charity?</font>
                <br/>
                <font class="smallfont">By checking this box only those bloggers willing let <%=Pagez.getUserSession().getPl().getNameforui()%> give all of their earnings from this conversation to charity will be able to take the conversation.  The blogger will be able to choose from a list of charities.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=CheckboxBoolean.getHtml("ischarityonly", researcherSurveyDetail05.getIscharityonly(), "", "")%>
                 <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getIscharityonly()%></font>
                <%}%>
                <font class="formfieldnamefont">Yes, Only Charitable Bloggers</font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Add Your Own Custom Charity</font>
                <br/>
                <font class="smallfont">Add your own charity for bloggers to choose from.  You must provide a charity name and a URL where respondents can learn about the charity.  At that URL <%=Pagez.getUserSession().getPl().getNameforui()%> administrators must be able to easily find information that allows them to make donations to the charity.  If such information is not easily available <%=Pagez.getUserSession().getPl().getNameforui()%> will donate the funds to a charity of its choosing.</font>
            </td>
            <td valign="top">
                <font class="smallfont">Charity Name:</font><br/>
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("charitycustom", String.valueOf(researcherSurveyDetail05.getCharitycustom()), 255, 35, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getCharitycustom()%></font>
                <%}%>
                <br/>
                <font class="smallfont">Charity URL:</font><br/>
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("charitycustomurl", String.valueOf(researcherSurveyDetail05.getCharitycustomurl()), 255, 35, "", "")%>
                    <br/><font class="tinyfont">(example: http://www.mycharity.com)</font>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getCharitycustomurl()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Only list your custom charity?</font>
                <br/>
                <font class="smallfont">By checking this box <%=Pagez.getUserSession().getPl().getNameforui()%> will list your custom charity as the only option for bloggers to choose from.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=CheckboxBoolean.getHtml("charityonlyallowcustom", researcherSurveyDetail05.getCharityonlyallowcustom(), "", "")%>
                 <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getCharityonlyallowcustom()%></font>
                <%}%>
                <font class="smallfont">Only Show My Custom Charity</font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <br/><br/>
                <font class="mediumfont">Overall Results Visibility</font>
            </td>
            <td valign="top">
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Hide Overall Aggregate Results for This Survey?</font>
                <br/>
                <font class="smallfont">By checking this box you're hiding overall aggregate results on the conversation's main page results tab.  Still available will be the aggregate results for individual blogs.  This is a balance between the blogger's widget value and value for you, the researcher. There are times when you don't want your competition to be able to simply grab the benefit of your research investment.  We advocate openness and charge an additional 5% fee to hide results.  This is calculated as 5% of the maximum possible conversation fee and is a one-time non-refundable fee.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=CheckboxBoolean.getHtml("isresultshidden", researcherSurveyDetail05.getIsresultshidden(), "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getIsresultshidden()%></font>
                <%}%>
                <font class="formfieldnamefont">Yes, Hide Aggregate Results for an Additional 5% Fee</font>
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
                <%if (researcherSurveyDetail05.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>

<%@ include file="/template/footer.jsp" %>