<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-04.gif\" align=\"right\" width=\"350\" height=\"73\"></img>\n" +
"        <h:outputText value=\"${researcherSurveyDetail04.title}\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetail04.title ne ''}\"/>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Target your survey to the correct demographic.   Be careful not to cast too narrow a net.  In the final step we'll tell you how many bloggers fulfill your criteria and you'll have the opportunity to widen the search.
    <br/><br/><br/>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>

    <h:messages/>

    <t:div rendered="#{researcherSurveyDetail04.status ne 1}">
        <f:verbatim escape="false">#{researcherSurveyDetail04.surveyCriteriaAsHtml}</f:verbatim>
        <br/>
        <b>Panels:</b>
        <h:outputText value="#{researcherSurveyDetail04.panelsStr}" rendered="#{researcherSurveyDetail04.status ne 1}" escape="false"></h:outputText>
    </t:div>

    <t:div rendered="#{researcherSurveyDetail04.status eq 1}">
        <h:panelGrid columns="4" cellpadding="3" border="0">

            <h:panelGroup>
                <h:outputText value="Social Influence Rating (TM)" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:outputText value="Social Influence Rating takes site traffic, survey referrals and a number of other metrics into account to give you some measure of this blogger's influence with his/her readership." styleClass="smallfont"></h:outputText>
                <br/>
                <h:message for="minsocialinfluencepercentile" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectOneMenu value="#{researcherSurveyDetail04.minsocialinfluencepercentile}" id="minsocialinfluencepercentile" required="true">
                   <f:selectItems value="#{staticVariables.percentiles}"/>
                </h:selectOneMenu>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Social Influence Rating 90 Days" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="minsocialinfluencepercentile90days" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectOneMenu value="#{researcherSurveyDetail04.minsocialinfluencepercentile90days}" id="minsocialinfluencepercentile90days" required="true">
                   <f:selectItems value="#{staticVariables.percentiles}"/>
                </h:selectOneMenu>
            </h:panelGroup>




            <h:panelGroup>
                <h:outputText value="Blog Quality of At Least" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:outputText value="Blog Quality is determined manually by our administrators visiting each blog post and assigning a general quality rating." styleClass="smallfont"></h:outputText>
                <br/>
                <h:message for="blogquality" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectOneListbox value="#{researcherSurveyDetail04.blogquality}" size="1" id="blogquality" layout="pageDirection" required="true">
                    <f:selectItems value="#{staticVariables.blogqualities}"/>
                </h:selectOneListbox>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Blog Quality Over Last 90 Days of At Least" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="blogquality90days" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectOneListbox value="#{researcherSurveyDetail04.blogquality90days}" size="1" id="blogquality90days" layout="pageDirection" required="true">
                    <f:selectItems value="#{staticVariables.blogqualities}"/>
                </h:selectOneListbox>
            </h:panelGroup>







            <h:panelGroup>
                <h:outputText value="Age Range" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="agemin" styleClass="RED"></h:message>
                <h:message for="agemax" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{researcherSurveyDetail04.agemin}" id="agemin" size="3" required="true">
                    <f:validateDoubleRange minimum="13" maximum="120"></f:validateDoubleRange>
                </h:inputText>
                <h:outputText value=" - "></h:outputText>
                <h:inputText value="#{researcherSurveyDetail04.agemax}" id="agemax" size="3" required="true">
                    <f:validateDoubleRange minimum="13" maximum="120"></f:validateDoubleRange>
                </h:inputText>
            </h:panelGroup>




            <h:panelGroup>
                <h:outputText value="Gender" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="gender" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyCheckbox value="#{researcherSurveyDetail04.gender}" id="gender" required="true">
                    <f:selectItems value="#{genders}"/>
                </h:selectManyCheckbox>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Ethnicity" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="ethnicity" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.ethnicity}" id="ethnicity" size="6" layout="pageDirection" required="true">
                    <f:selectItems value="#{ethnicities}"/>
                </h:selectManyListbox>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Marital Status" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="maritalstatus" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyCheckbox value="#{researcherSurveyDetail04.maritalstatus}" id="maritalstatus" required="true">
                    <f:selectItems value="#{maritalstatuses}"/>
                </h:selectManyCheckbox>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Income" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="income" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.income}" size="5" id="income" layout="pageDirection" required="true">
                    <f:selectItems value="#{incomes}"/>
                </h:selectManyListbox>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Education" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="educationlevel" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyCheckbox value="#{researcherSurveyDetail04.educationlevel}" id="educationlevel" layout="pageDirection" required="true">
                    <f:selectItems value="#{educationlevels}"/>
                </h:selectManyCheckbox>
            </h:panelGroup>



            <h:panelGroup>
                <h:outputText value="State" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="state" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.state}" size="5" id="state" layout="pageDirection" required="true">
                    <f:selectItems value="#{states}"/>
                </h:selectManyListbox>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="City" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="city" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.city}" size="5" id="city" layout="pageDirection" required="true">
                    <f:selectItems value="#{cities}"/>
                </h:selectManyListbox>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Profession" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="profession" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.profession}" size="5" id="profession" layout="pageDirection" required="true">
                    <f:selectItems value="#{professions}"/>
                </h:selectManyListbox>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Blog Focus" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="blogfocus" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.blogfocus}" size="5" id="blogfocus" layout="pageDirection" required="true">
                    <f:selectItems value="#{blogfocuses}"/>
                </h:selectManyListbox>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Politics" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="politics" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.politics}" size="5" id="politics" layout="pageDirection" required="true">
                    <f:selectItems value="#{politics}"/>
                </h:selectManyListbox>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Panel Membership" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:message for="panels" styleClass="RED"></h:message>
            </h:panelGroup>
            <h:panelGroup>
                <h:selectManyListbox value="#{researcherSurveyDetail04.panels}" id="panels" size="6" layout="pageDirection" required="false">
                    <f:selectItems value="#{researcherSurveyDetail04.panelsavailable}"/>
                </h:selectManyListbox>
            </h:panelGroup>


        </h:panelGrid>
    </t:div>

    <f:verbatim><br/><br/></f:verbatim>
    <div class="surveyeditbuttonbox"><div class="surveyeditpreviousbutton"><h:commandButton action="#{researcherSurveyDetail04.previousStep}" value="Previous Step" styleClass="formsubmitbutton"/></div><div class="surveyeditnextbutton"><h:commandButton action="#{researcherSurveyDetail04.saveSurveyAsDraft}" value="Save and Continue Later" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail04.status eq 1}"/><h:commandButton action="#{researcherSurveyDetail04.saveSurvey}" value="Next Step" styleClass="formsubmitbutton"/></div></div>

</h:form>

</ui:define>


</ui:composition>
</html>