<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey: <%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getTitle()%>";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



<h:form id="surveyedit">
    <h:messages styleClass="RED"></h:messages>
    <t:saveState id="save" value="#{sysadminSurveyDetail}"/>

    <t:panelTabbedPane id="panel" bgcolor="#ffffff">
        <t:panelTab id="panel_b" label="Summary">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>


            <table width="100%" cellpadding="5">
                <tr>
                    <td valign="top">
                        <font class="largefont"><%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getTitle()%></font>
                        <br/>
                        <font class="smallfont"><%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getDescription()%></font>
                        <br/><br/>
                        <font class="mediumfont">Survey ID: <%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getSurveyid()%></font>
                        <br/><br/>
                        <h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getUser().getFirstname()%> <%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getUser().getLastname()%>" styleClass="mediumfont"/>: <h:commandLink action="<%=((SysadminUserDetail)Pagez.getBeanMgr().get("SysadminUserDetail")).getBeginView()%>">
                            <h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getUser().getEmail()%>" styleClass="mediumfont" style="color: #0000ff;"/>
                            <f:param name="userid" value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getUser().getUserid()%>" />
                        </h:commandLink>
                        <br/><br/>
                        <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                            <h:outputText value="Survey Status: Draft" styleClass="mediumfont" rendered="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getStatus==1()%>"></h:outputText>
                            <h:outputText value="Survey Status: Waiting for Funds" styleClass="mediumfont" rendered="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getStatus==2()%>"></h:outputText>
                            <h:outputText value="Survey Status: Waiting for Start Date" styleClass="mediumfont" rendered="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getStatus==3()%>"></h:outputText>
                            <h:outputText value="Survey Status: Open" styleClass="mediumfont" rendered="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getStatus==4()%>"></h:outputText>
                            <h:outputText value="Survey Status: Closed" styleClass="mediumfont" rendered="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getStatus==5()%>"></h:outputText>
                        </div>
                        <br/><br/>
                        <table cellpadding="0" cellspacing="0" border="0">



                            <td valign="top">
                                <h:outputText value="Status" styleClass="formfieldnamefont"/>
                            </td>
                            <td valign="top">
                                <h:selectOneMenu value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getStatus()%>" id="status" required="true">
                                   <f:selectItems value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getStatuses()%>"/>
                                </h:selectOneMenu>
                            </td>
                            <td valign="top">
                                <h:message for="status" styleClass="RED"></h:message>
                            </td>



                            <td valign="top">
                                <h:outputText value="Survey Title" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                <h:inputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getTitle()%>" size="50" id="title" required="true">
                                    <f:validateLength minimum="3" maximum="200"></f:validateLength>
                                </h:inputText>
                            </td>
                            <td valign="top">
                                <h:message for="title" styleClass="RED"></h:message>
                            </td>



                            <td valign="top">
                                <h:outputText value="Description" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                <h:inputTextarea value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getDescription()%>" id="description" cols="45" required="true">
                                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                                </h:inputTextarea>
                            </td>
                            <td valign="top">
                                <h:message for="description" styleClass="RED"></h:message>
                            </td>

                            <td valign="top">
                                <h:outputText value="Template" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                <h:inputTextarea value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getTemplate()%>" id="template" cols="45" required="false">
                                </h:inputTextarea>
                            </td>
                            <td valign="top">
                                <h:message for="template" styleClass="RED"></h:message>
                            </td>




                            <td valign="top">
                                <h:outputText value="Spotlight Survey?" styleClass="formfieldnamefont"/>
                                <br/>
                                <h:outputText value="Spotlight Surveys appear on the homepage and are, well, spotlit." styleClass="tonyfont"/>
                            </td>
                            <td valign="top">
                                <h:selectBooleanCheckbox value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getIsspotlight()%>" id="isspotlight"></h:selectBooleanCheckbox>
                            </td>
                            <td valign="top">
                                <h:message for="isspotlight" styleClass="RED"></h:message>
                            </td>


                            <td valign="top">
                                <h:outputText value="Start Date" styleClass="formfieldnamefont"/>
                            </td>
                            <td valign="top">
                                <td valign="top">
                                    <t:inputDate value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getStartdate()%>" type="both" popupCalendar="true" id="startdate" required="true"></t:inputDate>
                                </td>
                            </td>
                            <td valign="top">
                                <h:message for="startdate" styleClass="RED"></h:message>
                            </td>

                            <td valign="top">
                                <h:outputText value="End Date" styleClass="formfieldnamefont"/>
                            </td>
                            <td valign="top">
                                <td valign="top">
                                    <t:inputDate value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getEnddate()%>" type="both" popupCalendar="true" id="enddate" required="true"></t:inputDate>
                                </td>
                                <br/>
                                <font class="tinyfont">Days since close: <%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getDayssinceclose()%></font>
                            </td>
                            <td valign="top">
                                <h:message for="enddate" styleClass="RED"></h:message>
                            </td>







                            <td valign="top">
                                <h:outputText value="Willing to Pay Per Respondent ($USD)" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">Amount to pay to a person who fulfills the targeting criteria and successfully fills out the survey.  Paying more will attract more people.  The minimum is $.25.  A good starting point is $2.50.</font>
                            </td>
                            <td valign="top">
                                <h:inputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getWillingtopayperrespondent()%>" id="willingtopayperrespondent" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000"></f:validateDoubleRange>
                                </h:inputText>
                            </td>
                            <td valign="top">
                                <h:message for="willingtopayperrespondent" styleClass="RED"></h:message>
                            </td>


                            <td valign="top">
                                <h:outputText value="Number of Respondents Requested" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">The number of people that you would like to have fill out the survey and post to their blogs.  Once this number is reached no more people can take the survey.  The minimum is 100.</font>
                            </td>
                            <td valign="top">
                                <h:inputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getNumberofrespondentsrequested()%>" id="numberofrespondentsrequested" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000000"></f:validateDoubleRange>
                                </h:inputText>
                            </td>
                            <td valign="top">
                                <h:message for="numberofrespondentsrequested" styleClass="RED"></h:message>
                            </td>



                            <td valign="top">
                                <h:outputText value="Willing to Pay Per Thousand Survey Displays on a Blog (CPM) ($USD)" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">Once surveys are taken they are posted to a person's blog.  With this value you determine what you're willing to pay for 1000 displays (CPM) of your survey.  This value must be at least $1 to cover bandwidth costs and can go as high as $1000 ($1000 would be equivalent to $1 per display).  The more you pay the more you attract bloggers who will display your survey prominently on their blog.</font>
                            </td>
                            <td valign="top">
                                <h:inputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getWillingtopaypercpm()%>" id="willingtopaypercpm" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="1000"></f:validateDoubleRange>
                                </h:inputText>
                            </td>
                            <td valign="top">
                                <h:message for="willingtopaypercpm" styleClass="RED"></h:message>
                            </td>


                            <td valign="top">
                                <h:outputText value="Max Survey Displays Per Blog" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">You may want to cap the maximum number of displays that a blogger can get paid for.  Your survey will continue to be displayed if the blog is visited over this number of times but you won't be charged for it.  It is tempting to set this number low, but be cautious... bloggers will quickly realize that they don't stand to make money by displaying your survey... so they won't.  The minimum value is 1000.</font>
                            </td>
                            <td valign="top">
                                <h:inputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getMaxdisplaysperblog()%>" id="maxdisplaysperblog" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000000"></f:validateDoubleRange>
                                </h:inputText>
                            </td>
                            <td valign="top">
                                <h:message for="maxdisplaysperblog" styleClass="RED"></h:message>
                            </td>

                            <td valign="top">
                                <h:outputText value="Max Survey Displays Total" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">This is a safety check that allows you to cap the total number of displays that you're willing to pay for.  Note that this value is not simply the number of respondents requested multiplied by the max surveys per blog... many bloggers have multiple blogs and can get paid on each one separately.  The minimum value is 1000.</font>
                            </td>
                            <td valign="top">
                                <h:inputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getMaxdisplaystotal()%>" id="maxdisplaystotal" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000000"></f:validateDoubleRange>
                                </h:inputText>
                            </td>
                            <td valign="top">
                                <h:message for="maxdisplaystotal" styleClass="RED"></h:message>
                            </td>








                            <td valign="top">
                            </td>
                            <td valign="top">
                                <h:commandButton action="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSaveSurvey()%>" value="Save" styleClass="formsubmitbutton"></h:commandButton>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Displays" styleClass="formfieldnamefont"/>
                            </td>
                            <td valign="top">
                                <h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getPublicsurveydisplays()%>"/>
                                <br/>
                                <h:outputText value="Times somebody's looked at the survey, considering whether or not to take it." styleClass="tinyfont"/>
                            </td>
                            <td valign="top">
                            </td>


                            <td valign="top">
                                <h:outputText value="Number of Respondents To Date" styleClass="formfieldnamefont"/>
                            </td>
                            <td valign="top">
                                <d:percentCompleteBar currentvalue="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurveyEnhancer().getResponsesalreadygotten()%>" maximumvalue="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getNumberofrespondentsrequested()%>" mintitle="" maxtitle="" widthinpixels="250"></d:percentCompleteBar>
                                <h:outputText value="Up to <%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getNumberofrespondentsrequested()%> people may complete this survey for pay." styleClass="smallfont"/>
                                <br/><br/>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Number of Blog Displays To Date" styleClass="formfieldnamefont"/>
                            </td>
                            <td valign="top">
                                <d:percentCompleteBar currentvalue="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurveyEnhancer().getImpressionsalreadygotten()%>" maximumvalue="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getMaxdisplaystotal()%>" mintitle="" maxtitle="" widthinpixels="250"></d:percentCompleteBar>
                                <h:outputText value="We'll pay for the first <%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurvey().getMaxdisplaystotal()%> displays in blogs." styleClass="smallfont"/>
                                <br/><br/>
                            </td>
                            <td valign="top">
                            </td>





                         </table>



                         <table cellpadding="0" cellspacing="0" border="0">
                            <td valign="top">
                                <h:outputText value="Survey Responses to Date" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                <h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSms().getResponsesToDate()%>"></h:outputText>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Spent on Responses to Date" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                $<h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSms().getSpentOnResponsesToDateIncludingdNeeroFee()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Blog Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                <h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSms().getImpressionsToDate()%>"></h:outputText>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Spent on Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                $<h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSms().getSpentOnImpressionsToDateIncludingdNeeroFee()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Total Spent to Date" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                $<h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSms().getSpentToDateIncludingdNeeroFee()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Max Possible Spend" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                $<h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSms().getMaxPossibleSpend()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </td>
                            <td valign="top">
                            </td>

                            <td valign="top">
                                <h:outputText value="Remaining Possible Spend" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top">
                                $<h:outputText value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSms().getRemainingPossibleSpend()%>"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </td>
                            <td valign="top">
                            </td>
                        </table>
                         

                    </td>
                </tr>
            </table>
        </t:panelTab>
        <t:panelTab id="panel_c" label="Question Preview">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim><%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurveyForTakers()%></f:verbatim>
        </t:panelTab>
        <t:panelTab id="panel_d" label="On-Blog">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <h:outputText escape="false" value="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurveyOnBlogPreview()%>"></h:outputText>
        </t:panelTab>
        <t:panelTab id="panel_f" label="Requirements">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim escape="false"><%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getSurveyCriteriaAsHtml()%></f:verbatim>
        </t:panelTab>
    </t:panelTabbedPane>
    




<%@ include file="/jsp/templates/footer.jsp" %>



