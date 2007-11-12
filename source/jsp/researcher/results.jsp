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
        <font class="largefont"><%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getSurvey().getTitle()%></font>
        <br/>
        <h:commandLink value="Results Main" action="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Response Report" action="<%=((ResearcherResultsAnswers)Pagez.getBeanMgr().get("ResearcherResultsAnswers")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Impressions" action="<%=((ResearcherResultsImpressions)Pagez.getBeanMgr().get("ResearcherResultsImpressions")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Respondents" action="<%=((ResearcherResultsRespondents)Pagez.getBeanMgr().get("ResearcherResultsRespondents")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Financial Status" action="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getBeginView()%>" styleClass="subnavfont" style="padding-left: 15px;"/>
    </div>




    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tr>
            <td colspan="2" valign="top">
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <h:outputText value="Survey Status: Draft" styleClass="mediumfont" rendered="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getStatus==1()%>"></h:outputText>
                    <h:outputText value="Survey Status: Waiting for Funds" styleClass="mediumfont" rendered="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getStatus==2()%>"></h:outputText>
                    <h:outputText value="Survey Status: Waiting for Start Date" styleClass="mediumfont" rendered="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getStatus==3()%>"></h:outputText>
                    <h:outputText value="Survey Status: Open" styleClass="mediumfont" rendered="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getStatus==4()%>"></h:outputText>
                    <h:outputText value="Survey Status: Closed" styleClass="mediumfont" rendered="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getStatus==5()%>"></h:outputText>
                </div>
                <br/>
            </td>
        </tr>
        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #88c99d; text-align: right; padding: 20px;">
                    <font class="smallfont">Total Survey Responses</font>
                    <br/>
                    <font class="largefont"><%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getTotalsurveyresponses()%></font>
                    <br/>
                    <h:commandButton action="<%=((ResearcherResultsAnswers)Pagez.getBeanMgr().get("ResearcherResultsAnswers")).getBeginView()%>" value="Response Report" styleClass="formsubmitbutton"></h:commandButton>
                </div>   
            </td>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #00ff00; text-align: left; padding: 20px;">
                    <font class="smallfont">Impressions on Blogs Qualifying for Payment</font>
                    <br/>
                    <font class="largefont"><%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getTotalsurveydisplays()%></font>
                    <br/>
                    <h:commandButton action="<%=((ResearcherResultsImpressions)Pagez.getBeanMgr().get("ResearcherResultsImpressions")).getBeginView()%>" value="View Impressions" styleClass="formsubmitbutton"></h:commandButton>
                </div>
            </td>
        </tr>
        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #00ff00; text-align: right; padding: 20px;">
                    <h:commandButton action="<%=((ResearcherResultsRespondents)Pagez.getBeanMgr().get("ResearcherResultsRespondents")).getBeginView()%>" value="See Respondents" styleClass="formsubmitbutton"></h:commandButton>
                    <br/>
                    <font class="largefont"><%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getTotalsurveyresponses()%></font>
                    <br/>
                    <font class="smallfont">People Have Taken the Survey</font>
                </div>
            </td>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #88c99d; text-align: left; padding: 20px;">
                    <h:commandButton action="<%=((ResearcherResultsFinancial)Pagez.getBeanMgr().get("ResearcherResultsFinancial")).getBeginView()%>" value="Financial Status" styleClass="formsubmitbutton"></h:commandButton>
                    <br/>
                    <font class="largefont">$<h:outputText value="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getSpenttodate()%>" styleClass="largefont"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText></font>
                    <br/>
                    <font class="smallfont">Has Been Spent to Date</font>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Survey Responses</font>
                    <d:percentCompleteBar currentvalue="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getTotalsurveyresponses()%>" maximumvalue="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getMaxsurveyresponses()%>" mintitle="" maxtitle="" widthinpixels="650"/>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Survey Impressions on Blogs</font>
                    <d:percentCompleteBar currentvalue="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getTotalsurveydisplays()%>" maximumvalue="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getMaxsurveydisplays()%>" mintitle="" maxtitle="" widthinpixels="650"/>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Possible Spend</font>
                    <d:percentCompleteBar currentvalue="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getSpenttodate()%>" maximumvalue="<%=((ResearcherResults)Pagez.getBeanMgr().get("ResearcherResults")).getMaxpossiblespend()%>" mintitle="" maxtitle="" widthinpixels="650"/>
                </div>
            </td>
        </tr>
    </table>





    <br/>















<%@ include file="/jsp/templates/footer.jsp" %>