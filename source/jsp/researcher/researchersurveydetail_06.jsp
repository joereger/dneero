<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail06" %>
<%@ page import="com.dneero.dao.Survey" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-06.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail06) Pagez.getBeanMgr().get("ResearcherSurveyDetail06")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
ResearcherSurveyDetail06 researcherSurveyDetail06 = (ResearcherSurveyDetail06)Pagez.getBeanMgr().get("ResearcherSurveyDetail06");
%>
<%@ include file="/jsp/templates/header.jsp" %>


<form action="researchersurveydetail_06.jsp" method="post" id="rsdform">
        <input type="hidden" name="action" value="next">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail06.getSurvey().getSurveyid()%>"/>



    <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;">
    <table cellpadding="0" cellspacing="0" border="0">


        <td valign="top">
            <h:outputText value="Bloggers In System Fulfilling Requirements" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getNumberofbloggersqualifiedforthissurvey()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Number of Questions in Survey" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getNumberofquestions()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Survey Start Date/Time" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getStartdate()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Survey End Date/Time" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getEnddate()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>


        <td valign="top">
            <h:outputText value="Max Possible Payment for Survey Responses" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getMaxrespondentpayments()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Max Possible Payment for Survey Displays on Blogs" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getMaximpressionpayments()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Survey Creation Fee" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="$5.00"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Hide Survey Overall Aggregate Results Fee" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getHideresultsfee()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Max Possible dNeero Fee" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getDneerofee()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Max Possible Spend" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=researcherSurveyDetail06.getMaxpossiblespend()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>



    </table>
    </div>

    <d:roundedCornerBox uniqueboxname="warningnumberofbloggerslessthanrequested" bodycolor="ffffcc" widthinpixels="700" rendered="#{1 eq 2 and researcherSurveyDetail06.warningnumberofbloggerslessthanrequested}">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: You've requested a number of survey respondents that is larger than the current number of bloggers in the system that fulfill your targeting criteria.  This may or may not be a problem.  Surveys often attract new bloggers to the system... you are in no way limited to the bloggers already signed-up.
        <br/><h:commandLink value="Possible Remedy: Relax the Targeting Criteria" styleClass="smallfont" action="researchersurveydetail_04" immediate="true"/>
        <br/><h:commandLink value="Idea: Increase Your Incentive to Attract New Bloggers" styleClass="smallfont" action="researchersurveydetail_05" immediate="true"/>
        </font>
    </d:roundedCornerBox>

    <d:roundedCornerBox uniqueboxname="warningnumberrequestedratiotoobig" bodycolor="ffffcc" widthinpixels="700" rendered="#{1 eq 2 and researcherSurveyDetail06.warningnumberrequestedratiotoobig and !researcherSurveyDetail06.warningnumberofbloggerslessthanrequested}">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: The ratio of the number of respondents you've requested to the number of bloggers that qualify for your criteria is a little high meaning that it may be difficult to attract enough respondents. This may or may not be a problem.  Surveys often attract new bloggers to the system... you are in no way limited to the bloggers already signed-up.
        <br/><h:commandLink value="Possible Remedy: Relax the Targeting Criteria" styleClass="smallfont" action="researchersurveydetail_04" immediate="true"/>
        <br/><h:commandLink value="Idea: Increase Your Incentive to Attract New Bloggers" styleClass="smallfont" action="researchersurveydetail_05" immediate="true"/>
        </font>
    </d:roundedCornerBox>

    <d:roundedCornerBox uniqueboxname="warningtoomanyquestions" bodycolor="ffffcc" widthinpixels="700" rendered="<%=researcherSurveyDetail06.getWarningtoomanyquestions()%>">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: You seem to have a large number of questions in your survey.  There is nothing wrong with this.  But large surveys and blogs may not be the best fit.  First, bloggers are a quick bunch, always having a lot to do. Second, you're asking them to post the survey to their blogs... large surveys may take over a blog at which point the blogger will take the survey down.  This may not be a problem for you, but we did want you to know about the possible issue.
        <br/><h:commandLink value="Possible Remedy: Adjust the Questions of Your Survey" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
        </font>
    </d:roundedCornerBox>

    <d:roundedCornerBox uniqueboxname="warningnoquestions" bodycolor="ffffcc" widthinpixels="700" rendered="<%=researcherSurveyDetail06.getWarningnoquestions()%>">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: This survey has no questions.  You may be trying to use the system as an advertising placement tool (which is fine) but we thought that you may have accidentally skipped the question page.
        <br/><h:commandLink value="Possible Remedy: Add Questions to Your Survey" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
        </font>
    </d:roundedCornerBox>


    <d:roundedCornerBox uniqueboxname="warningtimeperiodtooshort" bodycolor="ffffcc" widthinpixels="700" rendered="<%=researcherSurveyDetail06.getWarningtimeperiodtooshort()%>">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: The survey time period is rather short.  You may have very good reasons for doing so but we did want to note that surveys take some time to be publicized, signed up for, posted, etc.  30 days is a good safe period of time for a survey.
        <br/><h:commandLink value="Possible Remedy: Adjust the Start and End Dates" styleClass="smallfont" action="researchersurveydetail_01" immediate="true"/>
        </font>
    </d:roundedCornerBox>





     <t:div rendered="<%=researcherSurveyDetail06.getWarningdonthaveccinfo()%>">
        <div class="rounded" style="background: #ffffff; text-align: left; padding: 20px;">
            <font class="mediumfont" style="color: #cccccc;">Credit Card Info</font>
            <br/>

            <table cellpadding="3" cellspacing="0" border="0">

            <tr>
               <td valign="top" align="left">
                    <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="(first then last)" styleClass="tinyfont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="<%=researcherSurveyDetail06.getFirstname()%>" id="firstname"  size="15"></h:inputText>
                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                    <h:inputText value="<%=researcherSurveyDetail06.getLastname()%>" id="lastname" size="15"></h:inputText>
                    <h:message for="firstname" styleClass="RED"></h:message>
                    <h:message for="lastname" styleClass="RED"></h:message>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <h:outputText value="Street Address" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="<%=researcherSurveyDetail06.getStreet()%>" id="street" size="30"></h:inputText>
                    <h:message for="street" styleClass="RED"></h:message>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <h:outputText value="City, State, Zip" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="<%=researcherSurveyDetail06.getCccity()%>" id="cccity" size="20"></h:inputText>
                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                    <h:inputText value="<%=researcherSurveyDetail06.getCcstate()%>" id="ccstate"  size="2"></h:inputText>
                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                    <h:inputText value="<%=researcherSurveyDetail06.getPostalcode()%>" id="postalcode" size="6"></h:inputText>
                    <h:message for="cccity" styleClass="RED"></h:message>
                    <h:message for="ccstate" styleClass="RED"></h:message>
                    <h:message for="postalcode" styleClass="RED"></h:message>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <h:outputText value="Credit Card Type" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:selectOneMenu value="<%=researcherSurveyDetail06.getCctype()%>" id="cctype" required="true">
                        <f:selectItems value="<%=researcherSurveyDetail06.getCreditcardtypes()%>"/>
                    </h:selectOneMenu>
                    <h:message for="cctype" styleClass="RED"></h:message>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <h:outputText value="Credit Card Number" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="<%=researcherSurveyDetail06.getCcnum()%>" id="ccnum"  size="18">
                        <t:validateCreditCard />
                    </h:inputText>
                    <h:message for="ccnum" styleClass="RED"></h:message>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <h:outputText value="Expiration Date" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:selectOneMenu value="<%=researcherSurveyDetail06.getCcexpmo()%>" id="ccexpmo" required="true">
                        <f:selectItems value="<%=researcherSurveyDetail06.getMonthsForCreditcard()%>"/>
                    </h:selectOneMenu>
                    /
                    <h:selectOneMenu value="<%=researcherSurveyDetail06.getCcexpyear()%>" id="ccexpyear" required="true">
                        <f:selectItems value="<%=researcherSurveyDetail06.getYearsForCreditcard()%>"/>
                    </h:selectOneMenu>
                    <h:message for="ccexpmo" styleClass="RED"></h:message>
                    <h:message for="ccexpyear" styleClass="RED"></h:message>
               </td>
            </tr>



            <tr>
               <td valign="top" align="left">
                    <h:outputText value="CVV2" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <h:outputText value="(three digit number on back of card)" styleClass="tinyfont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="<%=researcherSurveyDetail06.getCvv2()%>" id="cvv2" size="3"></h:inputText>
                    <h:message for="cvv2" styleClass="RED"></h:message>
               </td>
            </tr>

            </table>


            </div>
        </t:div>


    <f:verbatim><br/><br/></f:verbatim>
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
                <input type="submit" value="Previous Step" onclick="document.rsdform.action.value='previous'">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail06.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" value="Save and Continue Later" onclick="document.rsdform.action.value='saveasdraft'">
                    <input type="submit" value="Launch this Survey!">
                <%}%>
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->

</form>

<%@ include file="/jsp/templates/footer.jsp" %>