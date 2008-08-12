<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResultsFinancial" %>
<%@ page import="com.dneero.util.Str" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherResultsFinancial researcherResultsFinancial= (ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial");
%>
<%@ include file="/template/header.jsp" %>



    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsFinancial.getSurvey().getTitle()%></font>
        <br/>
        <a href="/researcher/results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="/researcher/results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Results</font></a>
        <a href="/researcher/results_answers_userquestions.jsp"style="padding-left: 15px;"><font class="subnavfont">User Questions</font></a>
        <a href="/researcher/results_answers_advanced.jsp"style="padding-left: 15px;"><font class="subnavfont">Filter Results</font></a>
        <a href="/researcher/results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="/researcher/results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="/researcher/results_awards.jsp"style="padding-left: 15px;"><font class="subnavfont">Awards</font></a>
        <a href="/researcher/results_demographics.jsp"style="padding-left: 15px;"><font class="subnavfont">Demographics</font></a>
        <a href="/researcher/results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>



    <table cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Responses to Date</font>
            </td>
            <td valign="top">
                <font class="normalfont"><%=researcherResultsFinancial.getSms().getResponsesToDate()%></font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Spent on Responses to Date</font>
            </td>
            <td valign="top">
                <font class="normalfont">$<%=Str.formatForMoney(researcherResultsFinancial.getSms().getSpentOnResponsesToDateIncludingdNeeroFee())%></font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Blog Impressions to Date</font>
            </td>
            <td valign="top">
                <font class="normalfont"><%=researcherResultsFinancial.getSms().getImpressionsToDate()%></font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Spent on Impressions to Date</font>
            </td>
            <td valign="top">
                <font class="normalfont">$<%=Str.formatForMoney(researcherResultsFinancial.getSms().getSpentOnImpressionsToDateIncludingdNeeroFee())%></font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Total Spent to Date</font>
            </td>
            <td valign="top">
                <font class="normalfont">$<%=Str.formatForMoney(researcherResultsFinancial.getSms().getSpentToDateIncludingdNeeroFee())%></font>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Max Possible Spend</font>
            </td>
            <td valign="top">
                <font class="normalfont">$<%=Str.formatForMoney(researcherResultsFinancial.getSms().getMaxPossibleSpend())%></font>
            </td>
        </tr>


        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Remaining Possible Spend</font>
            </td>
            <td valign="top">
                <font class="normalfont">$<%=Str.formatForMoney(researcherResultsFinancial.getSms().getRemainingPossibleSpend())%></font>
            </td>
        </tr>


    </table>

    


<%@ include file="/template/footer.jsp" %>