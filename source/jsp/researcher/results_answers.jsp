<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=((ResearcherResultsAnswers)Pagez.getBeanMgr().get("ResearcherResultsAnswers")).getSurvey().getTitle()%></font>
        <br/>
        <h:commandLink value="Results Main" action="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Response Report" action="<%=((ResearcherResultsAnswers)Pagez.getBeanMgr().get("ResearcherResultsAnswers")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Impressions" action="<%=((ResearcherResultsImpressions)Pagez.getBeanMgr().get("ResearcherResultsImpressions")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Respondents" action="<%=((ResearcherResultsRespondents)Pagez.getBeanMgr().get("ResearcherResultsRespondents")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Financial Status" action="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
    </div>
    <br/><br/>

    <f:verbatim><%=((ResearcherResultsAnswers)Pagez.getBeanMgr().get("ResearcherResultsAnswers")).getResults()%></f:verbatim>

    <h:commandButton action="<%=((ResearcherResultsAnswersCsv)Pagez.getBeanMgr().get("ResearcherResultsAnswersCsv")).getGetCsv()%>" value="Download as CSV" styleClass="formsubmitbutton"></h:commandButton>

    


<%@ include file="/jsp/templates/footer.jsp" %>