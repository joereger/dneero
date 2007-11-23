<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail05" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
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
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("save") || request.getParameter("action").equals("previous"))) {
        try {
            researcherSurveyDetail05.setIscharityonly(CheckboxBoolean.getValueFromRequest("ischarityonly"));
            researcherSurveyDetail05.setIsresultshidden(CheckboxBoolean.getValueFromRequest("isresultshidden"));
            researcherSurveyDetail05.setMaxdisplaysperblog(Integer.parseInt(Textbox.getValueFromRequest("maxdisplaysperblog", "Max Displays Per Blog", true, DatatypeInteger.DATATYPEID)));
            researcherSurveyDetail05.setMaxdisplaystotal(Integer.parseInt(Textbox.getValueFromRequest("maxdisplaystotal", "Max Displays Total", true, DatatypeInteger.DATATYPEID)));
            researcherSurveyDetail05.setNumberofrespondentsrequested(Integer.parseInt(Textbox.getValueFromRequest("numberofrespondentsrequested", "Number of RespondentsRequested", true, DatatypeInteger.DATATYPEID)));
            researcherSurveyDetail05.setWillingtopaypercpm(Integer.parseInt(Textbox.getValueFromRequest("willingtopaypercpm", "Willing to Pay Per 1000 Impressions", true, DatatypeDouble.DATATYPEID)));
            researcherSurveyDetail05.setWillingtopayperrespondent(Integer.parseInt(Textbox.getValueFromRequest("willingtopayperrespondent", "Willing to Pay Per Respondent", true, DatatypeDouble.DATATYPEID)));
            if (request.getParameter("action").equals("next")) {
                researcherSurveyDetail05.saveSurvey();
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                researcherSurveyDetail05.saveSurveyAsDraft();
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail05.previousStep();
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="researchersurveydetail_05.jsp" method="post" id="rsdform">
        <input type="hidden" name="action" value="next">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail05.getSurvey().getSurveyid()%>"/>


    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    In this step you'll choose how much you're willing to pay bloggers. You'll want to craft an incentive that generates the response you're looking for.
    <br/><br/>
    Example 1: You have a new product to announce to the blogosphere.  You need to find new bloggers to talk about your product and you then need them to post the survey to their blogs to tell their friends and families.  Your pricing should be a good balance of Survey Taking Incentive and Blog Display Incentive.
    <br/><br/>
    Example 2: You want to see what bloggers think about a new concept.  Being more concerned about the research side of the equation, you may pay a lot for survey respondents and almost nothing for them to post the survey to their blogs.
    <br/><br/>
    The possibilities are endless... give it some thought and create a great incentive.  The better the incentive the more activity you'll generate.
    </font></div></center>

    <br/><br/>

    <table cellpadding="0" cellspacing="0" border="0">

        <tr>
            <td valign="top">
                <font class="mediumfont">Survey Taking Incentive</font>
            </td>
            <td valign="top">
            </td>
        </tr>


        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Willing to Pay Per Respondent ($USD)</font>
                <br/>
                <font class="smallfont">Amount to pay to a person who fulfills the targeting criteria and successfully fills out the survey.  Paying more will attract more people.  The minimum is $.10.  A good starting point is $2.50.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("willingtopayperrespondent", String.valueOf(researcherSurveyDetail05.getWillingtopayperrespondent()), 255, 35, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getWillingtopayperrespondent()%></font>
                <%}%>
            </td>
        </tr>


        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Number of Respondents Requested</font>
                <br/>
                <font class="smallfont">The number of people that you would like to have fill out the survey and post to their blogs.  Once this number is reached no more people can take the survey.  The minimum is 100.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("numberofrespondentsrequested", String.valueOf(researcherSurveyDetail05.getNumberofrespondentsrequested()), 255, 35, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getNumberofrespondentsrequested()%></font>
                <%}%>
            </td>
        </tr>


        <tr>
            <td valign="top">
                <br/><br/>
                <font class="mediumfont">Blog Posting Incentive</font>
            </td>
            <td valign="top">
            </td>
        </tr>


        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Willing to Pay Per Thousand Survey Displays on a Blog (CPM) ($USD)</font>
                <br/>
                <font class="smallfont">Once surveys are taken they are posted to a person's blog.  With this value you determine what you're willing to pay for 1000 displays (CPM) of your survey.  This value must be at least $0.25 to cover bandwidth costs and can go as high as $1000 ($1000 would be equivalent to $1 per display).  The more you pay the more you attract bloggers who will display your survey prominently on their blog.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("willingtopaypercpm", String.valueOf(researcherSurveyDetail05.getWillingtopaypercpm()), 255, 35, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getWillingtopaypercpm()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Max Survey Displays Per Blog</font>
                <br/>
                <font class="smallfont">You may want to cap the maximum number of displays that a blogger can get paid for.  Your survey will continue to be displayed if the blog is visited over this number of times but you won't be charged for it.  It is tempting to set this number low, but be cautious... bloggers will quickly realize that they don't stand to make money by displaying your survey... so they won't.  The minimum value is 1000.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("maxdisplaysperblog", String.valueOf(researcherSurveyDetail05.getMaxdisplaysperblog()), 255, 35, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getMaxdisplaysperblog()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Max Survey Displays Total</font>
                <br/>
                <font class="smallfont">This is a safety check that allows you to cap the total number of displays that you're willing to pay for.  Note that this value is not simply the number of respondents requested multiplied by the max surveys per blog... many bloggers have multiple blogs and can get paid on each one separately.  The minimum value is 25% of Number of Respondents Requested multiplied by Max Survey Displays Per Blog.</font>
            </td>
            <td valign="top">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <%=Textbox.getHtml("maxdisplaystotal", String.valueOf(researcherSurveyDetail05.getMaxdisplaystotal()), 255, 35, "", "")%>
                <%} else {%>
                    <font class="normalfont"><%=researcherSurveyDetail05.getMaxdisplaystotal()%></font>
                <%}%>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <br/><br/>
                <font class="mediumfont">Charity Only Option</font>
            </td>
            <td valign="top">
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Only if Blogger Lets dNeero Give Earnings to Charity?</font>
                <br/>
                <font class="smallfont">By checking this box only those bloggers willing let dNeero give all of their earnings from this survey to charity will be able to take the survey.  The blogger will be able to choose from a list of charities.</font>
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
                <font class="smallfont">By checking this box you're hiding overall aggregate results on the survey's main page results tab.  Still available will be the aggregate results for individual blogs.  This is a balance between the blogger's widget value and value for you, the researcher. There are times when you don't want your competition to be able to simply grab the benefit of your research investment.  We advocate openness (these are social surveys, afterall) and charge an additional 5% fee to hide results.  This is calculated as 5% of the maximum possible survey fee and is a one-time non-refundable fee.</font>
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
                <input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.rsdform.action.value='previous'">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail05.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.rsdform.action.value='saveasdraft'">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>

<%@ include file="/template/footer.jsp" %>