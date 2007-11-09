<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-01.gif\" align=\"right\" width=\"350\" height=\"73\"></img>\n" +
"        <h:outputText value=\"New Survey\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetail01.title eq ''}\"/>\n" +
"        <h:outputText value=\"<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getTitle()%>\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetail01.title ne ''}\"/>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:inputHidden name="surveyid" value="<%=((UserSession)Pagez.getBeanMgr().get("UserSession")).getCurrentSurveyid()%>"/>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    On this page you set the very general parameters for the survey.  Choose a title that'll get people interested.  Give enough information in the description for them to understand what you're trying to understand.  Your start date must be today or in the future.  You want to keep the survey open long enough to attract bloggers and have them fill out the survey... a month is a good starting point.
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <f:verbatim><br/></f:verbatim>

    <table cellpadding="0" cellspacing="0" border="0">

        <td valign="top">
            <h:outputText value="Survey Title" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:inputText value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getTitle()%>" size="50" id="title" required="true" rendered="#{researcherSurveyDetail01.status le 1}">
                <f:validateLength minimum="3" maximum="200"></f:validateLength>
            </h:inputText>
            <h:outputText value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getTitle()%>" rendered="#{researcherSurveyDetail01.status ge 2}"></h:outputText>
        </td>
        <td valign="top">
            <h:message for="title" styleClass="RED"></h:message>
        </td>



        <td valign="top">
            <h:outputText value="Description" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:inputTextarea value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getDescription()%>" id="description" cols="45" required="true" rendered="#{researcherSurveyDetail01.status le 1}">
                <f:validateLength minimum="3" maximum="50000"></f:validateLength>
            </h:inputTextarea>
            <h:outputText value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getDescription()%>" rendered="#{researcherSurveyDetail01.status ge 2}"></h:outputText>
        </td>
        <td valign="top">
            <h:message for="description" styleClass="RED"></h:message>
        </td>


        <td valign="top">
            <h:outputText value="Start Date" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <t:inputDate value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getStartdate()%>"  type="both" popupCalendar="true" id="startdate" required="true" rendered="#{researcherSurveyDetail01.status le 1}"></t:inputDate>
            <h:outputText value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getStartdate()%>" rendered="#{researcherSurveyDetail01.status ge 2}"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
            <br/>
            <font class="tinyfont">Click the ... button for a pop-up calendar.</font>
            <br/>
            <font class="tinyfont">(format is: dd month yyyy hour minute)</font>
        </td>
        <td valign="top">
            <h:message for="startdate" styleClass="RED"></h:message>
        </td>


        <td valign="top">
            <h:outputText value="End Date" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <t:inputDate value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getEnddate()%>" type="both" popupCalendar="true" id="enddate" required="true" rendered="#{researcherSurveyDetail01.status le 1}"></t:inputDate>
            <h:outputText value="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getEnddate()%>" rendered="#{researcherSurveyDetail01.status ge 2}"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
            <br/>
            <font class="tinyfont">Click the ... button for a pop-up calendar.</font>
            <br/>
            <font class="tinyfont">(format is: dd month yyyy hour minute)</font>
        </td>
        <td valign="top">
            <h:message for="enddate" styleClass="RED"></h:message>
        </td>

    </table>

    <div class="surveyeditbuttonbox"><div class="surveyeditpreviousbutton"></div><div class="surveyeditnextbutton"><h:commandButton action="<%=((ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getSaveSurvey()%>" value="Next Step" styleClass="formsubmitbutton"></h:commandButton></div></div>



<%@ include file="/jsp/templates/footer.jsp" %>