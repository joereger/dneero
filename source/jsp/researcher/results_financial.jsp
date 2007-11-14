<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsFinancial.getSurvey().getTitle()%></font>
        <br/>
        <a href="results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Response Report</font></a>
        <a href="results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>



    <table cellpadding="0" cellspacing="0" border="0">
        <td valign="top">
            <h:outputText value="Survey Responses to Date" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getSms().getResponsesToDate()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Spent on Responses to Date" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            $<h:outputText value="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getSms().getSpentOnResponsesToDateIncludingdNeeroFee()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Blog Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            <h:outputText value="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getSms().getImpressionsToDate()%>"></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Spent on Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            $<h:outputText value="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getSms().getSpentOnImpressionsToDateIncludingdNeeroFee()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Total Spent to Date" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            $<h:outputText value="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getSms().getSpentToDateIncludingdNeeroFee()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Max Possible Spend" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            $<h:outputText value="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getSms().getMaxPossibleSpend()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </td>
        <td valign="top">
        </td>

        <td valign="top">
            <h:outputText value="Remaining Possible Spend" styleClass="formfieldnamefont"></h:outputText>
        </td>
        <td valign="top">
            $<h:outputText value="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getSms().getRemainingPossibleSpend()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
        </td>
        <td valign="top">
        </td>
    </table>

    


<%@ include file="/jsp/templates/footer.jsp" %>