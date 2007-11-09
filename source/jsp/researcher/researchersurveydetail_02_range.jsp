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

    <h:inputHidden name="surveyid" value="<%=((UserSession)Pagez.getBeanMgr().get("UserSession")).getCurrentSurveyid()%>" />
    <h:inputHidden name="questionid" value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getQuestionid()%>" />

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    In a Range question type respondents choose from low to high numbers at some interval.  This is also known as a Lichert Scale.  Choose Min and Max Titles to tell surveytakers what the scale represents.  Then choose the Min and Max values as the lowest and highest possible choices. Finally, choose the Step value which determines the numerical distance between option.  For example, Step of 1 means that the scale will simply step from Min to Max like this: 1, 2, 3, 4, etc.  Step of 2 would give you: 2, 4, 6, 8, etc.
    <br/><br/>
    Here's what a Range question looks like:
    <center><img src="../images/questiontypes/range.gif" border="0"></img></center>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>



    <!-- Start Bottom -->

    <table cellpadding="0" cellspacing="0" border="0">

        <td valign="top">
            <h:outputText value="Question Type"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="Range"></h:outputText>
        </td>
        <td valign="top">
        </td>

        


        <td valign="top">
            <h:outputText value="Question" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getQuestion()%>" id="question" required="true">
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
            <h:selectBooleanCheckbox value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getIsrequired()%>" id="isrequired" required="true"></h:selectBooleanCheckbox>
        </td>
        <td valign="top">
            <h:message for="isrequired" styleClass="RED"></h:message>
        </td>

    </table>


    <table cellpadding="0" cellspacing="0" border="0">

        <td valign="top">
            <h:outputText value="Min Title" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="Min" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="Step" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="Max" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="Max Title" styleClass="formfieldnamefont"></h:outputText>
        </td>

        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getMintitle()%>" id="mintitle" required="false" size="7" maxlength="20">
                <f:validateLength maximum="20"></f:validateLength>
            </h:inputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getMin()%>" id="min" required="true" size="3" maxlength="7">
                <f:validateDoubleRange minimum=".0001" maximum="1200000"></f:validateDoubleRange>
            </h:inputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getStep()%>" id="step" required="true" size="3" maxlength="7">
                <f:validateDoubleRange minimum=".0001" maximum="1200000"></f:validateDoubleRange>
            </h:inputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getMax()%>" id="max" required="true" size="3" maxlength="7">
                <f:validateDoubleRange minimum=".0001" maximum="1200000"></f:validateDoubleRange>
            </h:inputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getMaxtitle()%>" id="maxtitle" required="true" size="7" maxlength="20">
                <f:validateLength maximum="20"></f:validateLength>
            </h:inputText>
        </td>

        <td valign="top">
            <h:message for="mintitle" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:message for="min" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:message for="step" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:message for="max" styleClass="RED"></h:message>
        </td>
        <td valign="top">
            <h:message for="maxtitle" styleClass="RED"></h:message>
        </td>

    </table>


    <table cellpadding="0" cellspacing="0" border="0">

        <td valign="top">
        </td>
        <td valign="top">
            <h:commandButton action="<%=((ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getSaveQuestion()%>" value="Save Question and Continue" styleClass="formsubmitbutton"></h:commandButton>
            <br/><br/><h:commandLink value="Nevermind, Take me Back" styleClass="tinyfont" action="<%=((ResearcherSurveyDetail02)Pagez.getBeanMgr().get("ResearcherSurveyDetail02")).getBeginView()%>" immediate="true"/>
        </td>
        <td valign="top">
        </td>

    </table>

    <!-- End Bottom -->



<%@ include file="/jsp/templates/footer.jsp" %>