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
    <h:inputHidden value="<%=((ResearcherSurveyDetail02textbox)Pagez.getBeanMgr().get("ResearcherSurveyDetail02textbox")).getQuestionid()%>" />

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Type a question in the Question box.  Respondents will be able to type a short piece of text as an answer to this question.
    <br/><br/>
    Here's what a Textbox question looks like:
    <img src="../images/questiontypes/textbox.gif" border="0" alt=""></img>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>



    <!-- Start Bottom -->

    <table cellpadding="0" cellspacing="0" border="0">

        <td valign="top">
            <h:outputText value="Question Type"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="Textbox"></h:outputText>
        </td>
        <td valign="top">
        </td>

        


        <td valign="top">
            <h:outputText value="Question" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02textbox)Pagez.getBeanMgr().get("ResearcherSurveyDetail02textbox")).getQuestion()%>" id="question" required="true">
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
            <h:selectBooleanCheckbox value="<%=((ResearcherSurveyDetail02textbox)Pagez.getBeanMgr().get("ResearcherSurveyDetail02textbox")).getIsrequired()%>" id="isrequired" required="true"></h:selectBooleanCheckbox>
        </td>
        <td valign="top">
            <h:message for="isrequired" styleClass="RED"></h:message>
        </td>




        <td valign="top">
        </td>
        <td valign="top">
            <h:commandButton action="<%=((ResearcherSurveyDetail02textbox)Pagez.getBeanMgr().get("ResearcherSurveyDetail02textbox")).getSaveQuestion()%>" value="Save Question and Continue" styleClass="formsubmitbutton"></h:commandButton>
            <br/><br/><h:commandLink value="Nevermind, Take me Back" styleClass="tinyfont" action="<%=((ResearcherSurveyDetail02)Pagez.getBeanMgr().get("ResearcherSurveyDetail02")).getBeginView()%>" immediate="true"/>
        </td>
        <td valign="top">
        </td>

    </table>

    <!-- End Bottom -->



<%@ include file="/jsp/templates/footer.jsp" %>