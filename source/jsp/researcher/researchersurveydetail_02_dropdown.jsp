<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"></img>\n" +
"        <h:outputText value=\"${researcherSurveyDetail02.title}\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetail02.title ne ''}\"/>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <h:inputHidden name="surveyid" value="#{userSession.currentSurveyid}" />
    <h:inputHidden name="questionid" value="#{researcherSurveyDetail02dropdown.questionid}" />

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Type a question in the Question box.  Next, give respondents options to choose from.  Because this is a Dropdown question type, they'll only be able to choose one option as part of their answer.
    <br/><br/>
    Here's what a Dropdown question looks like:
    <center><img src="../images/questiontypes/dropdown.gif" border="0"></img></center>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>



    <!-- Start Bottom -->

    <h:panelGrid columns="3" cellpadding="3" border="0">

        <h:panelGroup>
            <h:outputText value="Question Type"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:outputText value="Dropdown"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>




        <h:panelGroup>
            <h:outputText value="Question" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:inputText value="#{researcherSurveyDetail02dropdown.question}" id="question" required="true">
                <f:validateLength minimum="3" maximum="254"></f:validateLength>
            </h:inputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="question" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
            <h:outputText value="Is Required?" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <h:selectBooleanCheckbox value="#{researcherSurveyDetail02dropdown.isrequired}" id="isrequired" required="true"></h:selectBooleanCheckbox>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="isrequired" styleClass="RED"></h:message>
        </h:panelGroup>

        <h:panelGroup>
            <h:outputText value="Options" styleClass="formfieldnamefont"></h:outputText>
        </h:panelGroup>
        <h:panelGroup>
            <font class="tinyfont">One option per line.</font><br/>
            <h:inputTextarea value="#{researcherSurveyDetail02dropdown.options}" id="options" required="true" cols="25" rows="10">
            </h:inputTextarea>
        </h:panelGroup>
        <h:panelGroup>
            <h:message for="options" styleClass="RED"></h:message>
        </h:panelGroup>


        <h:panelGroup>
        </h:panelGroup>
        <h:panelGroup>
            <h:commandButton action="#{researcherSurveyDetail02dropdown.saveQuestion}" value="Save Question and Continue" styleClass="formsubmitbutton"></h:commandButton>
            <br/><br/><h:commandLink value="Nevermind, Take me Back" styleClass="tinyfont" action="#{researcherSurveyDetail02.beginView}" immediate="true"/>
        </h:panelGroup>
        <h:panelGroup>
        </h:panelGroup>

    </h:panelGrid>

    <!-- End Bottom -->



<%@ include file="/jsp/templates/footer.jsp" %>