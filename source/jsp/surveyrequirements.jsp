<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "#{publicSurveyRequirements.survey.title}";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="smallfont">#{publicSurveyRequirements.survey.description}</font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=#{publicSurveyRequirements.survey.surveyid}">Disclosure</a>
    -->

    <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
    <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
    To take this survey and get paid for it you must fulfill these demographic requirements.  If you take the survey before logging in and/or signing up and then do not fulfill these requirements we will discard your answers.
    </font></div></center>
    <f:verbatim escape="false">#{publicSurveyRequirements.surveyCriteriaAsHtml}</f:verbatim>





</h:form>

</ui:define>


</ui:composition>
</html>


