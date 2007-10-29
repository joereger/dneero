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
        <font class="largefont">#{researcherResults.survey.title}</font>
        <br/>
        <h:commandLink value="Results Main" action="#{researcherResults.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Response Report" action="#{researcherResultsAnswers.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Impressions" action="#{researcherResultsImpressions.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Respondents" action="#{researcherResultsRespondents.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Financial Status" action="#{researcherResultsFinancial.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
    </div>




    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tr>
            <td colspan="2" valign="top">
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <h:outputText value="Survey Status: Draft" styleClass="mediumfont" rendered="#{researcherResults.status==1}"></h:outputText>
                    <h:outputText value="Survey Status: Waiting for Funds" styleClass="mediumfont" rendered="#{researcherResults.status==2}"></h:outputText>
                    <h:outputText value="Survey Status: Waiting for Start Date" styleClass="mediumfont" rendered="#{researcherResults.status==3}"></h:outputText>
                    <h:outputText value="Survey Status: Open" styleClass="mediumfont" rendered="#{researcherResults.status==4}"></h:outputText>
                    <h:outputText value="Survey Status: Closed" styleClass="mediumfont" rendered="#{researcherResults.status==5}"></h:outputText>
                </div>
                <br/>
            </td>
        </tr>
        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #88c99d; text-align: right; padding: 20px;">
                    <font class="smallfont">Total Survey Responses</font>
                    <br/>
                    <font class="largefont">#{researcherResults.totalsurveyresponses}</font>
                    <br/>
                    <h:commandButton action="#{researcherResultsAnswers.beginView}" value="Response Report" styleClass="formsubmitbutton"></h:commandButton>
                </div>   
            </td>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #00ff00; text-align: left; padding: 20px;">
                    <font class="smallfont">Impressions on Blogs Qualifying for Payment</font>
                    <br/>
                    <font class="largefont">#{researcherResults.totalsurveydisplays}</font>
                    <br/>
                    <h:commandButton action="#{researcherResultsImpressions.beginView}" value="View Impressions" styleClass="formsubmitbutton"></h:commandButton>
                </div>
            </td>
        </tr>
        <tr>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #00ff00; text-align: right; padding: 20px;">
                    <h:commandButton action="#{researcherResultsRespondents.beginView}" value="See Respondents" styleClass="formsubmitbutton"></h:commandButton>
                    <br/>
                    <font class="largefont">#{researcherResults.totalsurveyresponses}</font>
                    <br/>
                    <font class="smallfont">People Have Taken the Survey</font>
                </div>
            </td>
            <td width="50%" valign="top">
                <div class="rounded" style="background: #88c99d; text-align: left; padding: 20px;">
                    <h:commandButton action="#{researcherResultsFinancial.beginView}" value="Financial Status" styleClass="formsubmitbutton"></h:commandButton>
                    <br/>
                    <font class="largefont">$<h:outputText value="#{researcherResults.spenttodate}" styleClass="largefont"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText></font>
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
                    <d:percentCompleteBar currentvalue="#{researcherResults.totalsurveyresponses}" maximumvalue="#{researcherResults.maxsurveyresponses}" mintitle="" maxtitle="" widthinpixels="650"/>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Survey Impressions on Blogs</font>
                    <d:percentCompleteBar currentvalue="#{researcherResults.totalsurveydisplays}" maximumvalue="#{researcherResults.maxsurveydisplays}" mintitle="" maxtitle="" widthinpixels="650"/>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2" valign="top">
                <br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                    <font class="mediumfont" style="color: #333333;">Possible Spend</font>
                    <d:percentCompleteBar currentvalue="#{researcherResults.spenttodate}" maximumvalue="#{researcherResults.maxpossiblespend}" mintitle="" maxtitle="" widthinpixels="650"/>
                </div>
            </td>
        </tr>
    </table>





    <br/>















</h:form>

</ui:define>


</ui:composition>
</html>