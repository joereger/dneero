<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"></img>\n" +
"        <h:outputText value=\"<%=((ResearcherSurveyDetail02)Pagez.getBeanMgr().get("ResearcherSurveyDetail02")).getTitle()%>\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetail02.title ne ''}\"/>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <h:inputHidden value="<%=((UserSession)Pagez.getBeanMgr().get("UserSession")).getCurrentSurveyid()%>" />
    <h:inputHidden value="<%=((ResearcherSurveyDetail02checkboxes)Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes")).getQuestionid()%>" />

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Type a question in the Question box.  Next, give respondents options to choose from.  Because this is a Checkbox question type, they'll be able to choose multiple options as part of their answer.
    <br/><br/>
    Here's what a Checkboxes question looks like:
    <center><img src="../images/questiontypes/selectmultiple.gif" border="0"></img></center>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>



    <!-- Start Bottom -->

    <table cellpadding="0" cellspacing="0" border="0">

        <td valign="top">
            <h:outputText value="Question Type"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="Checkbox"></h:outputText>
        </td>
        <td valign="top">
        </td>



        <td valign="top">
            <h:outputText value="Question" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02checkboxes)Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes")).getQuestion()%>" id="question" required="true">
                <f:validateLength minimum="3" maximum="254"></f:validateLength>
            </h:inputText>
        </td>
        <td valign="top">
            <h:message for="question" styleClass="RED"></h:message>
        </td>


        <td valign="top">
            <h:outputText value="Is Required?" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:selectBooleanCheckbox value="<%=((ResearcherSurveyDetail02checkboxes)Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes")).getIsrequired()%>" id="isrequired" required="true"></h:selectBooleanCheckbox>
        </td>
        <td valign="top">
            <h:message for="isrequired" styleClass="RED"></h:message>
        </td>

        <td valign="top">
            <h:outputText value="Options" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <font class="smallfont">One option per line.</font><br/>
            <h:inputTextarea value="<%=((ResearcherSurveyDetail02checkboxes)Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes")).getOptions()%>" id="options" required="true" cols="25" rows="10">
            </h:inputTextarea>
        </td>
        <td valign="top">
            <h:message for="options" styleClass="RED"></h:message>
        </td>


        <td valign="top">
        </td>
        <td valign="top">
            <h:commandButton action="<%=((ResearcherSurveyDetail02checkboxes)Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes")).getSaveQuestion()%>" value="Save Question and Continue" styleClass="formsubmitbutton"></h:commandButton>
            <br/><br/><h:commandLink value="Nevermind, Take me Back" styleClass="tinyfont" action="<%=((ResearcherSurveyDetail02)Pagez.getBeanMgr().get("ResearcherSurveyDetail02")).getBeginView()%>" immediate="true"/>
        </td>
        <td valign="top">
        </td>

    </table>

    <!-- End Bottom -->



<%@ include file="/jsp/templates/footer.jsp" %>