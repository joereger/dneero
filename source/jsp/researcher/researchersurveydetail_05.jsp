<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-05.gif\" align=\"right\" width=\"350\" height=\"73\"></img>\n" +
"        <h:outputText value=\"${researcherSurveyDetail05.title}\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetail05.title ne ''}\"/>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>




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

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>

    <h:panelGrid columns="3" cellpadding="3" border="0">


        <h:panelGroup>
            <h:outputText value="Survey Taking Incentive" styleClass="mediumfont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Willing to Pay Per Respondent ($USD)" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <font class="smallfont">Amount to pay to a person who fulfills the targeting criteria and successfully fills out the survey.  Paying more will attract more people.  The minimum is $.10.  A good starting point is $2.50.</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail05.willingtopayperrespondent}" id="willingtopayperrespondent" required="true" rendered="#{researcherSurveyDetail05.status eq 1}">
                <f:validateDoubleRange minimum=".10" maximum="10000"></f:validateDoubleRange>
            </h:inputText>
            <h:outputText value="#{researcherSurveyDetail05.willingtopayperrespondent}" rendered="#{researcherSurveyDetail05.status ne 1}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="willingtopayperrespondent" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Number of Respondents Requested" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <font class="smallfont">The number of people that you would like to have fill out the survey and post to their blogs.  Once this number is reached no more people can take the survey.  The minimum is 100.</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail05.numberofrespondentsrequested}" id="numberofrespondentsrequested" required="true" rendered="#{researcherSurveyDetail05.status eq 1}">
                <f:validateDoubleRange minimum="25" maximum="10000000"></f:validateDoubleRange>
            </h:inputText>
            <h:outputText value="#{researcherSurveyDetail05.numberofrespondentsrequested}" rendered="#{researcherSurveyDetail05.status ne 1}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="numberofrespondentsrequested" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
            <br/><br/>
            <h:outputText value="Blog Posting Incentive" styleClass="mediumfont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Willing to Pay Per Thousand Survey Displays on a Blog (CPM) ($USD)" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <font class="smallfont">Once surveys are taken they are posted to a person's blog.  With this value you determine what you're willing to pay for 1000 displays (CPM) of your survey.  This value must be at least $0.25 to cover bandwidth costs and can go as high as $1000 ($1000 would be equivalent to $1 per display).  The more you pay the more you attract bloggers who will display your survey prominently on their blog.</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail05.willingtopaypercpm}" id="willingtopaypercpm" required="true" rendered="#{researcherSurveyDetail05.status eq 1}">
                <f:validateDoubleRange minimum=".25" maximum="1000"></f:validateDoubleRange>
            </h:inputText>
            <h:outputText value="#{researcherSurveyDetail05.willingtopaypercpm}" rendered="#{researcherSurveyDetail05.status ne 1}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="willingtopaypercpm" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Max Survey Displays Per Blog" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <font class="smallfont">You may want to cap the maximum number of displays that a blogger can get paid for.  Your survey will continue to be displayed if the blog is visited over this number of times but you won't be charged for it.  It is tempting to set this number low, but be cautious... bloggers will quickly realize that they don't stand to make money by displaying your survey... so they won't.  The minimum value is 1000.</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail05.maxdisplaysperblog}" id="maxdisplaysperblog" required="true" rendered="#{researcherSurveyDetail05.status eq 1}">
                <f:validateDoubleRange minimum="1000" maximum="10000000"></f:validateDoubleRange>
            </h:inputText>
            <h:outputText value="#{researcherSurveyDetail05.maxdisplaysperblog}" rendered="#{researcherSurveyDetail05.status ne 1}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="maxdisplaysperblog" styleClass="RED"></h:message>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Max Survey Displays Total" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <font class="smallfont">This is a safety check that allows you to cap the total number of displays that you're willing to pay for.  Note that this value is not simply the number of respondents requested multiplied by the max surveys per blog... many bloggers have multiple blogs and can get paid on each one separately.  The minimum value is 25% of Number of Respondents Requested multiplied by Max Survey Displays Per Blog.</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail05.maxdisplaystotal}" id="maxdisplaystotal" required="true" rendered="#{researcherSurveyDetail05.status eq 1}">
                <f:validateDoubleRange minimum="1" maximum="10000000"></f:validateDoubleRange>
            </h:inputText>
            <h:outputText value="#{researcherSurveyDetail05.maxdisplaystotal}" rendered="#{researcherSurveyDetail05.status ne 1}"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="maxdisplaystotal" styleClass="RED"></h:message>
        </h:panelGroup>

        <h:panelGroup>
            <br/><br/>
            <h:outputText value="Charity Only Option" styleClass="mediumfont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Only if Blogger Lets dNeero Give Earnings to Charity?" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <font class="smallfont">By checking this box only those bloggers willing let dNeero give all of their earnings from this survey to charity will be able to take the survey.  The blogger will be able to choose from a list of charities.</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectBooleanCheckbox title="ischarityonly" id="ischarityonly" value="#{researcherSurveyDetail05.ischarityonly}" rendered="#{researcherSurveyDetail05.status eq 1}"/>
            <h:outputText value="#{researcherSurveyDetail05.ischarityonly}" rendered="#{researcherSurveyDetail05.status ne 1}"></h:outputText>
            <font class="formfieldnamefont">Yes, Only Charitable Bloggers</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="ischarityonly" styleClass="RED"></h:message>
        </h:panelGroup>

        <h:panelGroup>
            <br/><br/>
            <h:outputText value="Overall Results Visibility" styleClass="mediumfont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Hide Overall Aggregate Results for This Survey?" styleClass="formfieldnamefont"></h:outputText>
            <br/>
            <font class="smallfont">By checking this box you're hiding overall aggregate results on the survey's main page results tab.  Still available will be the aggregate results for individual blogs.  This is a balance between the blogger's widget value and value for you, the researcher. There are times when you don't want your competition to be able to simply grab the benefit of your research investment.  We advocate openness (these are social surveys, afterall) and charge an additional 5% fee to hide results.  This is calculated as 5% of the maximum possible survey fee and is a one-time non-refundable fee.</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectBooleanCheckbox title="isresultshidden" id="isresultshidden" value="#{researcherSurveyDetail05.isresultshidden}" rendered="#{researcherSurveyDetail05.status eq 1}"/>
            <h:outputText value="#{researcherSurveyDetail05.isresultshidden}" rendered="#{researcherSurveyDetail05.status ne 1}"></h:outputText>
            <font class="formfieldnamefont">Yes, Hide Aggregate Results for an Additional 5% Fee</font>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="isresultshidden" styleClass="RED"></h:message>
        </h:panelGroup>


    </h:panelGrid>

    <f:verbatim><br/><br/></f:verbatim>
    <div class="surveyeditbuttonbox"><div class="surveyeditpreviousbutton"><h:commandButton action="#{researcherSurveyDetail05.previousStep}" value="Previous Step" styleClass="formsubmitbutton"/></div><div class="surveyeditnextbutton"><h:commandButton action="#{researcherSurveyDetail05.saveSurveyAsDraft}" value="Save and Continue Later" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail05.status eq 1}"/><h:commandButton action="#{researcherSurveyDetail05.saveSurvey}" value="Next Step" styleClass="formsubmitbutton"/></div></div>



<%@ include file="/jsp/templates/footer.jsp" %>