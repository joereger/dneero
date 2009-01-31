<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail05" %>
<%@ page import="com.dneero.incentive.IncentiveCash" %>
<%@ page import="com.dneero.incentive.IncentiveCoupon" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherTwitaskDetail05) Pagez.getBeanMgr().get("ResearcherTwitaskDetail05")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetail05 researcherTwitaskDetail05= (ResearcherTwitaskDetail05)Pagez.getBeanMgr().get("ResearcherTwitaskDetail05");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherTwitaskDetail05.getTwitask().getStatus()>Twitask.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_04.jsp?twitaskid="+ researcherTwitaskDetail05.getTwitask().getTwitaskid());
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchertwitaskdetail_06.jsp?twitaskid="+ researcherTwitaskDetail05.getTwitask().getTwitaskid());
                    return;
                }
            }
            researcherTwitaskDetail05.setIscharityonly(CheckboxBoolean.getValueFromRequest("ischarityonly"));
            researcherTwitaskDetail05.setCharityonlyallowcustom(CheckboxBoolean.getValueFromRequest("charityonlyallowcustom"));
            researcherTwitaskDetail05.setCharitycustom(Textbox.getValueFromRequest("charitycustom", "Charity Name", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail05.setCharitycustomurl(Textbox.getValueFromRequest("charitycustomurl", "Charity Url", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail05.setNumberofrespondentsrequested(Textbox.getIntFromRequest("numberofrespondentsrequested", "Number of RespondentsRequested", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail05.setIncentivetype(Radio.getIntFromRequest("incentivetype", "Incentive Type", true, DatatypeInteger.DATATYPEID));
            researcherTwitaskDetail05.setCoupontitle(Textbox.getValueFromRequest("coupontitle", "Coupon Title", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail05.setCoupondescription(Textbox.getValueFromRequest("coupondescription", "Coupon Description", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail05.setCouponinstructions(Textbox.getValueFromRequest("couponinstructions", "Coupon Instructions", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail05.setCouponcodeprefix(Textbox.getValueFromRequest("couponcodeprefix", "Coupon Code Prefix", false, DatatypeString.DATATYPEID));
            researcherTwitaskDetail05.setCouponcodeaddrandompostfix(CheckboxBoolean.getValueFromRequest("couponcodeaddrandompostfix"));
            researcherTwitaskDetail05.setCouponestimatedcashvalue(Textbox.getDblFromRequest("couponestimatedcashvalue", "Coupon Estimated Cash Value", false, DatatypeDouble.DATATYPEID));

            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherTwitaskDetail05.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_06.jsp?twitaskid="+ researcherTwitaskDetail05.getTwitask().getTwitaskid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                researcherTwitaskDetail05.save();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherTwitaskDetail05.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_04.jsp?twitaskid="+ researcherTwitaskDetail05.getTwitask().getTwitaskid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchertwitaskdetail_05.jsp" method="post" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchertwitaskdetail_05.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail05.getTwitask().getTwitaskid()%>"/>


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
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("numberofrespondentsrequested", String.valueOf(researcherTwitaskDetail05.getNumberofrespondentsrequested()), 20, 7, "", "")%> <font class="formfieldnamefont">people</font>
                <%} else {%>
                    <font class="normalfont"><%=researcherTwitaskDetail05.getNumberofrespondentsrequested()%> people</font>
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
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                    <table cellpadding="2" cellspacing="1" border="0">
                        <tr>
                            <td valign="top">
                                    <%=Radio.getHtml("incentivetype", String.valueOf(researcherTwitaskDetail05.getIncentivetype()), String.valueOf(IncentiveCash.ID), "", "")%>
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
                                <%=Textbox.getHtml("willingtopayperrespondent", String.valueOf(researcherTwitaskDetail05.getWillingtopaypertwit()), 255, 10, "", "")%>
                                <br/><br/>
                            </td>
                            <td valign="top">
     
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">
                                    <%=Radio.getHtml("incentivetype", String.valueOf(researcherTwitaskDetail05.getIncentivetype()), String.valueOf(IncentiveCoupon.ID), "", "")%>
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
                                    <%=Textbox.getHtml("coupontitle", String.valueOf(researcherTwitaskDetail05.getCoupontitle()), 60, 40, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">A full description of what the respondent gets with the coupon, where they can redeem it, etc.</font>
                            </td>
                            <td valign="top">
                                    <%=Textarea.getHtml("coupondescription", String.valueOf(researcherTwitaskDetail05.getCoupondescription()), 2, 25, "", "font-size: 9px;")%>
                                    <%//=Textbox.getHtml("coupondescription", String.valueOf(researcherTwitaskDetail05.getCoupondescription()), 255, 25, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">A set of redemption instructions that the user will see after they're awarded the coupon.  These instructions will be emailed to respondents who successfully post the conversation to their peers for five days.  They'll also be available online.</font>
                            </td>
                            <td valign="top">
                                    <%=Textarea.getHtml("couponinstructions", String.valueOf(researcherTwitaskDetail05.getCouponinstructions()), 2, 25, "", "font-size: 9px;")%>
                                    <%//=Textbox.getHtml("couponinstructions", String.valueOf(researcherTwitaskDetail05.getCouponinstructions()), 255, 25, "", "font-size: 9px;")%>
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
                                    <%=Textbox.getHtml("couponestimatedcashvalue", String.valueOf(researcherTwitaskDetail05.getCouponestimatedcashvalue()), 10, 10, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">Coupon prefix.  The first characters of a coupon code.  No spaces.  Example: "MYCOUPON"</font>
                            </td>
                            <td valign="top">
                                    <%=Textbox.getHtml("couponcodeprefix", String.valueOf(researcherTwitaskDetail05.getCouponcodeprefix()), 10, 10, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">

                            </td>
                            <td valign="top">
                                    <font class="tinyfont">Include a random alphanumeric postfix with the coupon code?  For example, if MYCOUPON is the prefix, coupons will look like: MYCOUPONFGHGD and MYCOUPONHJHY.  This makes each coupon unique and trackable.</font>
                            </td>
                            <td valign="top">

                                    <%=CheckboxBoolean.getHtml("couponcodeaddrandompostfix", researcherTwitaskDetail05.getCouponcodeaddrandompostfix(), "", "")%> <font class="tinyfont">Yes, include a random alphanumeric postfix.</font>
                                    <%//=Textbox.getHtml("couponcodeaddrandompostfix", String.valueOf(researcherTwitaskDetail05.getCouponcodeaddrandompostfix()), 10, 10, "", "font-size: 9px;")%>
                            </td>
                        </tr>
                    </table>
                <%} else {%>
                    <font class="normalfont"><%=researcherTwitaskDetail05.getWillingtopaypertwit()%></font>
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
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                    <font class="formfieldnamefont">$</font>    
                    <%=Textbox.getHtml("willingtopaypercpm", String.valueOf(researcherTwitaskDetail05.getWillingtopaypertwit()), 255, 9, "", "")%>
                <%} else {%>
                    <font class="formfieldnamefont">$</font>
                    <font class="normalfont"><%=researcherTwitaskDetail05.getWillingtopaypertwit()%></font>
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
                <font class="formfieldnamefont">Only if Blogger Lets dNeero Give Earnings to Charity?</font>
                <br/>
                <font class="smallfont">By checking this box only those bloggers willing let dNeero give all of their earnings from this conversation to charity will be able to take the conversation.  The blogger will be able to choose from a list of charities.</font>
            </td>
            <td valign="top">
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                    <%=CheckboxBoolean.getHtml("ischarityonly", researcherTwitaskDetail05.getIscharityonly(), "", "")%>
                 <%} else {%>
                    <font class="normalfont"><%=researcherTwitaskDetail05.getIscharityonly()%></font>
                <%}%>
                <font class="formfieldnamefont">Yes, Only Charitable Bloggers</font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Add Your Own Custom Charity</font>
                <br/>
                <font class="smallfont">Add your own charity for bloggers to choose from.  You must provide a charity name and a URL where respondents can learn about the charity.  At that URL dNeero administrators must be able to easily find information that allows them to make donations to the charity.  If such information is not easily available dNeero will donate the funds to a charity of its choosing.</font>
            </td>
            <td valign="top">
                <font class="smallfont">Charity Name:</font><br/>
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("charitycustom", String.valueOf(researcherTwitaskDetail05.getCharitycustom()), 255, 35, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherTwitaskDetail05.getCharitycustom()%></font>
                <%}%>
                <br/>
                <font class="smallfont">Charity URL:</font><br/>
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("charitycustomurl", String.valueOf(researcherTwitaskDetail05.getCharitycustomurl()), 255, 35, "", "")%>
                    <br/><font class="tinyfont">(example: http://www.mycharity.com)</font>
                <%} else {%>
                    <font class="normalfont"><%=researcherTwitaskDetail05.getCharitycustomurl()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Only list your custom charity?</font>
                <br/>
                <font class="smallfont">By checking this box dNeero will list your custom charity as the only option for bloggers to choose from.</font>
            </td>
            <td valign="top">
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                    <%=CheckboxBoolean.getHtml("charityonlyallowcustom", researcherTwitaskDetail05.getCharityonlyallowcustom(), "", "")%>
                 <%} else {%>
                    <font class="normalfont"><%=researcherTwitaskDetail05.getCharityonlyallowcustom()%></font>
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




    </table>

    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherTwitaskDetail05.getTwitask().getStatus()==Twitask.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>

<%@ include file="/template/footer.jsp" %>