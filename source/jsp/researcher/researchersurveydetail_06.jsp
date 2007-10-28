<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"
      xmlns:c="http://java.sun.com/jstl/core"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">
        <img src="/images/process-train-survey-06.gif" align="right" width="350" height="73"></img>
        <h:outputText value="${researcherSurveyDetail06.title}" styleClass="pagetitlefont" rendered="${researcherSurveyDetail06.title ne ''}"/>
        <br clear="all"/>
    </ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form>
    <h:messages styleClass="RED"/>
    <t:saveState id="save" value="#{researcherSurveyDetail06}"/>



    <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;">
    <h:panelGrid columns="3" cellpadding="3" border="0">


        <h:panelGroup>
            <h:outputText value="Bloggers In System Fulfilling Requirements" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.numberofbloggersqualifiedforthissurvey}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Number of Questions in Survey" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.numberofquestions}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Survey Start Date/Time" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.startdate}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Survey End Date/Time" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.enddate}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Max Possible Payment for Survey Responses" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.maxrespondentpayments}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Max Possible Payment for Survey Displays on Blogs" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.maximpressionpayments}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Survey Creation Fee" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="$5.00"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Hide Survey Overall Aggregate Results Fee" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.hideresultsfee}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Max Possible dNeero Fee" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.dneerofee}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Max Possible Spend" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="#{researcherSurveyDetail06.maxpossiblespend}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>



    </h:panelGrid>
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

    <d:roundedCornerBox uniqueboxname="warningtoomanyquestions" bodycolor="ffffcc" widthinpixels="700" rendered="#{researcherSurveyDetail06.warningtoomanyquestions}">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: You seem to have a large number of questions in your survey.  There is nothing wrong with this.  But large surveys and blogs may not be the best fit.  First, bloggers are a quick bunch, always having a lot to do. Second, you're asking them to post the survey to their blogs... large surveys may take over a blog at which point the blogger will take the survey down.  This may not be a problem for you, but we did want you to know about the possible issue.
        <br/><h:commandLink value="Possible Remedy: Adjust the Questions of Your Survey" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
        </font>
    </d:roundedCornerBox>

    <d:roundedCornerBox uniqueboxname="warningnoquestions" bodycolor="ffffcc" widthinpixels="700" rendered="#{researcherSurveyDetail06.warningnoquestions}">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: This survey has no questions.  You may be trying to use the system as an advertising placement tool (which is fine) but we thought that you may have accidentally skipped the question page.
        <br/><h:commandLink value="Possible Remedy: Add Questions to Your Survey" styleClass="smallfont" action="researchersurveydetail_02" immediate="true"/>
        </font>
    </d:roundedCornerBox>


    <d:roundedCornerBox uniqueboxname="warningtimeperiodtooshort" bodycolor="ffffcc" widthinpixels="700" rendered="#{researcherSurveyDetail06.warningtimeperiodtooshort}">
        <h:graphicImage url="/images/lightbulb_on.png"></h:graphicImage>
        <font class="smallfont">
        Warning: The survey time period is rather short.  You may have very good reasons for doing so but we did want to note that surveys take some time to be publicized, signed up for, posted, etc.  30 days is a good safe period of time for a survey.
        <br/><h:commandLink value="Possible Remedy: Adjust the Start and End Dates" styleClass="smallfont" action="researchersurveydetail_01" immediate="true"/>
        </font>
    </d:roundedCornerBox>





     <t:div rendered="#{researcherSurveyDetail06.warningdonthaveccinfo}">
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
                    <h:inputText value="#{researcherSurveyDetail06.firstname}" id="firstname"  size="15"></h:inputText>
                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                    <h:inputText value="#{researcherSurveyDetail06.lastname}" id="lastname" size="15"></h:inputText>
                    <h:message for="firstname" styleClass="RED"></h:message>
                    <h:message for="lastname" styleClass="RED"></h:message>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <h:outputText value="Street Address" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="#{researcherSurveyDetail06.street}" id="street" size="30"></h:inputText>
                    <h:message for="street" styleClass="RED"></h:message>
               </td>
            </tr>


            <tr>
               <td valign="top" align="left">
                    <h:outputText value="City, State, Zip" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="#{researcherSurveyDetail06.cccity}" id="cccity" size="20"></h:inputText>
                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                    <h:inputText value="#{researcherSurveyDetail06.ccstate}" id="ccstate"  size="2"></h:inputText>
                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                    <h:inputText value="#{researcherSurveyDetail06.postalcode}" id="postalcode" size="6"></h:inputText>
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
                    <h:selectOneMenu value="#{researcherSurveyDetail06.cctype}" id="cctype" required="true">
                        <f:selectItems value="#{researcherSurveyDetail06.creditcardtypes}"/>
                    </h:selectOneMenu>
                    <h:message for="cctype" styleClass="RED"></h:message>
               </td>
            </tr>

            <tr>
               <td valign="top" align="left">
                    <h:outputText value="Credit Card Number" styleClass="formfieldnamefont"></h:outputText>
               </td>
               <td valign="top" align="left">
                    <h:inputText value="#{researcherSurveyDetail06.ccnum}" id="ccnum"  size="18">
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
                    <h:selectOneMenu value="#{researcherSurveyDetail06.ccexpmo}" id="ccexpmo" required="true">
                        <f:selectItems value="#{researcherSurveyDetail06.monthsForCreditcard}"/>
                    </h:selectOneMenu>
                    /
                    <h:selectOneMenu value="#{researcherSurveyDetail06.ccexpyear}" id="ccexpyear" required="true">
                        <f:selectItems value="#{researcherSurveyDetail06.yearsForCreditcard}"/>
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
                    <h:inputText value="#{researcherSurveyDetail06.cvv2}" id="cvv2" size="3"></h:inputText>
                    <h:message for="cvv2" styleClass="RED"></h:message>
               </td>
            </tr>

            </table>


            </div>
        </t:div>














    <f:verbatim><br/><br/></f:verbatim>
    <font class="mediumfont">
        <ul>
            <li>I understand that by launching this survey I am committing to spending up to #{researcherSurveyDetail06.maxpossiblespend} (Max Possible Spend.)</li>
            <li>Actual charges will be based only on the activities of survey completion and impressions (i.e. viewing of your survey on blogs.)</li>
            <li>I understand that my account balance must be sufficient to support activities:
                <ul>
                    <li>20% of the #{researcherSurveyDetail06.maxpossiblespend} will be charged now.</li>
                    <li>Whenever my account balance falls below 10% of the sum of the Max Possible Spends for all of my live (open) surveys, additional charges will be made to attain the 20% balance.</li>
                    <li>If my account balance falls below 5% of the sum of the Max Possible Spends for all of my live (open) surveys, then all my surveys will be put on hold until my account balance is increased.</li>
                    <li>However, if my account balance is sufficient to complete the activities I have requested, my live (open) surveys will not be put on hold. </li>
                </ul>
            </li>
            <li>Impressions on blogs will be paid for during a period extending 30 days from the end date of the survey, not to exceed the limits on impressions that you've set with your survey.  As such, dNeero will refund money after this period when the max number of surveys is not reached.</li>
        </ul>
    </font>

    <f:verbatim><br/><br/></f:verbatim>
    <div class="surveyeditbuttonbox"><div class="surveyeditpreviousbutton"><h:commandButton action="#{researcherSurveyDetail06.previousStep}" value="Previous Step" styleClass="formsubmitbutton"/></div><div class="surveyeditnextbutton"><h:commandButton action="#{researcherSurveyDetail06.saveSurveyAsDraft}" value="Save and Continue Later" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail06.status eq 1}"/><h:commandButton action="#{researcherSurveyDetail06.saveSurvey}" value="Launch this Survey!" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail06.status eq 1}"/></div></div>



</h:form>

</ui:define>


</ui:composition>
</html>