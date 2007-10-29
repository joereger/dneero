<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-06.gif\" align=\"right\" width=\"350\" height=\"73\"></img>\n" +
"        <h:outputText value=\"${researcherSurveyDetailPostlaunch.title}\" styleClass=\"pagetitlefont\" rendered=\"${researcherSurveyDetailPostlaunch.title ne ''}\"/>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <h:messages styleClass="RED"/>
    <h:inputHidden name="surveyid" value="#{userSession.currentSurveyid}" />

    <font class="pagetitlefont" style="color: #333333;">Congratulations!  Your survey has been launched!</font>
    <br/><br/>
    <font class="mediumfont">Here's what to expect next:</font>
    <br/><br/>
    <ul>
        <li>You can no longer edit your survey, but you can review it.</li>
        <li>We'll verify that your account balance has at least #{researcherSurveyDetailPostlaunch.initialcharge} which represents 20% of the maximum possible spend, #{researcherSurveyDetailPostlaunch.maxpossiblespend}, for your survey.</li>
        <li>Bloggers will see your survey and take it.</li>
            <ul>
                <li>Each time a blogger takes the survey your account balance will be debited by #{researcherSurveyDetailPostlaunch.willingtopayperrespondent}.</li>
                <li>We will close your survey either when the end date is reached or when the maximum number of respondents has been reached.</li>
                <li>Bloggers will post the survey to their blogs.</li>
            </ul>
        <li>Every few hours (generally 24, but sometimes 48) we will count all of the blog impressions and debit your account for them.</li>
        <li>If your account balance dips below 5% of the maximum value of all of your currently-active surveys at any given time then we will debit your account balance to get it back to the 20% mark.  For the case of surveys nearing their end we will only incorporate the final amount remaining in the calculation.  If your balance remains below the 5% we will shut down all surveys, placing them into a Waiting for Funds status wherein bloggers can not find them.</li>
        <li>You can watch blogger answers and impressions in real-time by going to the Results page for this survey.  You get to that page by going to My Surveys and then clicking Results.</li>

    </ul>

    <font class="smallfont">We appreciate your business!  If you have any questions, you can use the <h:commandLink value="Help/Support Ticket" action="accountsupportissueslist" styleClass="smallfont" style="margin-left: 5px;"/> system.</font>

    <br/><br/>
    <font class="smallfont">Invite people to take your survey:</font>
    <br/>
    <h:commandLink action="#{researcherEmailinvite.beginView}">
        <h:outputText value="Invite People to Take this Survey" styleClass="normalfont" escape="false" />
        <f:param name="surveyid" value="#{userSession.currentSurveyid}" />
    </h:commandLink>

    <br/><br/>
    <font class="smallfont">Promote your survey:</font>
    <br/>
    <h:outputText value="#{researcherSurveyDetailPostlaunch.socialbookmarklinks}" escape="false" styleClass="smallfont"/>


    <br/><br/>
    <font class="smallfont">Or return to your list of surveys:</font>
    <br/>
    <h:commandLink value="Return to List of Surveys" action="#{researcherIndex.beginView}" styleClass="mediumfont" style="color: #0000ff;"/>
    <br/><br/>


</h:form>

</ui:define>


</ui:composition>
</html>