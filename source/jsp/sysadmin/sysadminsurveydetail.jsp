<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey: #{sysadminSurveyDetail.survey.title}";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
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
                        <font class="largefont">#{sysadminSurveyDetail.survey.title}</font>
                        <br/>
                        <font class="smallfont">#{sysadminSurveyDetail.survey.description}</font>
                        <br/><br/>
                        <font class="mediumfont">Survey ID: #{sysadminSurveyDetail.survey.surveyid}</font>
                        <br/><br/>
                        <h:outputText value="#{sysadminSurveyDetail.user.firstname} #{sysadminSurveyDetail.user.lastname}" styleClass="mediumfont"/>: <h:commandLink action="#{sysadminUserDetail.beginView}">
                            <h:outputText value="#{sysadminSurveyDetail.user.email}" styleClass="mediumfont" style="color: #0000ff;"/>
                            <f:param name="userid" value="#{sysadminSurveyDetail.user.userid}" />
                        </h:commandLink>
                        <br/><br/>
                        <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 20px;">
                            <h:outputText value="Survey Status: Draft" styleClass="mediumfont" rendered="#{sysadminSurveyDetail.survey.status==1}"></h:outputText>
                            <h:outputText value="Survey Status: Waiting for Funds" styleClass="mediumfont" rendered="#{sysadminSurveyDetail.survey.status==2}"></h:outputText>
                            <h:outputText value="Survey Status: Waiting for Start Date" styleClass="mediumfont" rendered="#{sysadminSurveyDetail.survey.status==3}"></h:outputText>
                            <h:outputText value="Survey Status: Open" styleClass="mediumfont" rendered="#{sysadminSurveyDetail.survey.status==4}"></h:outputText>
                            <h:outputText value="Survey Status: Closed" styleClass="mediumfont" rendered="#{sysadminSurveyDetail.survey.status==5}"></h:outputText>
                        </div>
                        <br/><br/>
                        <h:panelGrid columns="3" cellpadding="3" border="0">



                            <h:panelGroup>
                                <h:outputText value="Status" styleClass="formfieldnamefont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:selectOneMenu value="#{sysadminSurveyDetail.survey.status}" id="status" required="true">
                                   <f:selectItems value="#{sysadminSurveyDetail.statuses}"/>
                                </h:selectOneMenu>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="status" styleClass="RED"></h:message>
                            </h:panelGroup>



                            <h:panelGroup>
                                <h:outputText value="Survey Title" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputText value="#{sysadminSurveyDetail.survey.title}" size="50" id="title" required="true">
                                    <f:validateLength minimum="3" maximum="200"></f:validateLength>
                                </h:inputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="title" styleClass="RED"></h:message>
                            </h:panelGroup>



                            <h:panelGroup>
                                <h:outputText value="Description" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputTextarea value="#{sysadminSurveyDetail.survey.description}" id="description" cols="45" required="true">
                                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                                </h:inputTextarea>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="description" styleClass="RED"></h:message>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Template" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputTextarea value="#{sysadminSurveyDetail.survey.template}" id="template" cols="45" required="false">
                                </h:inputTextarea>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="template" styleClass="RED"></h:message>
                            </h:panelGroup>




                            <h:panelGroup>
                                <h:outputText value="Spotlight Survey?" styleClass="formfieldnamefont"/>
                                <br/>
                                <h:outputText value="Spotlight Surveys appear on the homepage and are, well, spotlit." styleClass="tonyfont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:selectBooleanCheckbox value="#{sysadminSurveyDetail.survey.isspotlight}" id="isspotlight"></h:selectBooleanCheckbox>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="isspotlight" styleClass="RED"></h:message>
                            </h:panelGroup>


                            <h:panelGroup>
                                <h:outputText value="Start Date" styleClass="formfieldnamefont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:panelGroup>
                                    <t:inputDate value="#{sysadminSurveyDetail.survey.startdate}" type="both" popupCalendar="true" id="startdate" required="true"></t:inputDate>
                                </h:panelGroup>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="startdate" styleClass="RED"></h:message>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="End Date" styleClass="formfieldnamefont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:panelGroup>
                                    <t:inputDate value="#{sysadminSurveyDetail.survey.enddate}" type="both" popupCalendar="true" id="enddate" required="true"></t:inputDate>
                                </h:panelGroup>
                                <br/>
                                <font class="tinyfont">Days since close: #{sysadminSurveyDetail.dayssinceclose}</font>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="enddate" styleClass="RED"></h:message>
                            </h:panelGroup>







                            <h:panelGroup>
                                <h:outputText value="Willing to Pay Per Respondent ($USD)" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">Amount to pay to a person who fulfills the targeting criteria and successfully fills out the survey.  Paying more will attract more people.  The minimum is $.25.  A good starting point is $2.50.</font>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputText value="#{sysadminSurveyDetail.survey.willingtopayperrespondent}" id="willingtopayperrespondent" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000"></f:validateDoubleRange>
                                </h:inputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="willingtopayperrespondent" styleClass="RED"></h:message>
                            </h:panelGroup>


                            <h:panelGroup>
                                <h:outputText value="Number of Respondents Requested" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">The number of people that you would like to have fill out the survey and post to their blogs.  Once this number is reached no more people can take the survey.  The minimum is 100.</font>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputText value="#{sysadminSurveyDetail.survey.numberofrespondentsrequested}" id="numberofrespondentsrequested" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000000"></f:validateDoubleRange>
                                </h:inputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="numberofrespondentsrequested" styleClass="RED"></h:message>
                            </h:panelGroup>



                            <h:panelGroup>
                                <h:outputText value="Willing to Pay Per Thousand Survey Displays on a Blog (CPM) ($USD)" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">Once surveys are taken they are posted to a person's blog.  With this value you determine what you're willing to pay for 1000 displays (CPM) of your survey.  This value must be at least $1 to cover bandwidth costs and can go as high as $1000 ($1000 would be equivalent to $1 per display).  The more you pay the more you attract bloggers who will display your survey prominently on their blog.</font>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputText value="#{sysadminSurveyDetail.survey.willingtopaypercpm}" id="willingtopaypercpm" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="1000"></f:validateDoubleRange>
                                </h:inputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="willingtopaypercpm" styleClass="RED"></h:message>
                            </h:panelGroup>


                            <h:panelGroup>
                                <h:outputText value="Max Survey Displays Per Blog" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">You may want to cap the maximum number of displays that a blogger can get paid for.  Your survey will continue to be displayed if the blog is visited over this number of times but you won't be charged for it.  It is tempting to set this number low, but be cautious... bloggers will quickly realize that they don't stand to make money by displaying your survey... so they won't.  The minimum value is 1000.</font>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputText value="#{sysadminSurveyDetail.survey.maxdisplaysperblog}" id="maxdisplaysperblog" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000000"></f:validateDoubleRange>
                                </h:inputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="maxdisplaysperblog" styleClass="RED"></h:message>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Max Survey Displays Total" styleClass="formfieldnamefont"></h:outputText>
                                <br/>
                                <font class="smallfont">This is a safety check that allows you to cap the total number of displays that you're willing to pay for.  Note that this value is not simply the number of respondents requested multiplied by the max surveys per blog... many bloggers have multiple blogs and can get paid on each one separately.  The minimum value is 1000.</font>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:inputText value="#{sysadminSurveyDetail.survey.maxdisplaystotal}" id="maxdisplaystotal" required="true">
                                    <f:validateDoubleRange minimum="0" maximum="10000000"></f:validateDoubleRange>
                                </h:inputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:message for="maxdisplaystotal" styleClass="RED"></h:message>
                            </h:panelGroup>








                            <h:panelGroup>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:commandButton action="#{sysadminSurveyDetail.saveSurvey}" value="Save" styleClass="formsubmitbutton"></h:commandButton>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Displays" styleClass="formfieldnamefont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:outputText value="#{sysadminSurveyDetail.survey.publicsurveydisplays}"/>
                                <br/>
                                <h:outputText value="Times somebody's looked at the survey, considering whether or not to take it." styleClass="tinyfont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>


                            <h:panelGroup>
                                <h:outputText value="Number of Respondents To Date" styleClass="formfieldnamefont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                                <d:percentCompleteBar currentvalue="#{sysadminSurveyDetail.surveyEnhancer.responsesalreadygotten}" maximumvalue="#{sysadminSurveyDetail.survey.numberofrespondentsrequested}" mintitle="" maxtitle="" widthinpixels="250"></d:percentCompleteBar>
                                <h:outputText value="Up to #{sysadminSurveyDetail.survey.numberofrespondentsrequested} people may complete this survey for pay." styleClass="smallfont"/>
                                <br/><br/>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Number of Blog Displays To Date" styleClass="formfieldnamefont"/>
                            </h:panelGroup>
                            <h:panelGroup>
                                <d:percentCompleteBar currentvalue="#{sysadminSurveyDetail.surveyEnhancer.impressionsalreadygotten}" maximumvalue="#{sysadminSurveyDetail.survey.maxdisplaystotal}" mintitle="" maxtitle="" widthinpixels="250"></d:percentCompleteBar>
                                <h:outputText value="We'll pay for the first #{sysadminSurveyDetail.survey.maxdisplaystotal} displays in blogs." styleClass="smallfont"/>
                                <br/><br/>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>





                         </h:panelGrid>



                         <h:panelGrid columns="3" cellpadding="3" border="0">
                            <h:panelGroup>
                                <h:outputText value="Survey Responses to Date" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:outputText value="#{sysadminSurveyDetail.sms.responsesToDate}"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Spent on Responses to Date" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                $<h:outputText value="#{sysadminSurveyDetail.sms.spentOnResponsesToDateIncludingdNeeroFee}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Blog Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                <h:outputText value="#{sysadminSurveyDetail.sms.impressionsToDate}"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Spent on Impressions to Date" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                $<h:outputText value="#{sysadminSurveyDetail.sms.spentOnImpressionsToDateIncludingdNeeroFee}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Total Spent to Date" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                $<h:outputText value="#{sysadminSurveyDetail.sms.spentToDateIncludingdNeeroFee}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Max Possible Spend" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                $<h:outputText value="#{sysadminSurveyDetail.sms.maxPossibleSpend}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>

                            <h:panelGroup>
                                <h:outputText value="Remaining Possible Spend" styleClass="formfieldnamefont"></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                                $<h:outputText value="#{sysadminSurveyDetail.sms.remainingPossibleSpend}"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText>
                            </h:panelGroup>
                            <h:panelGroup>
                            </h:panelGroup>
                        </h:panelGrid>
                         

                    </td>
                </tr>
            </table>
        </t:panelTab>
        <t:panelTab id="panel_c" label="Question Preview">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim>#{sysadminSurveyDetail.surveyForTakers}</f:verbatim>
        </t:panelTab>
        <t:panelTab id="panel_d" label="On-Blog">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <h:outputText escape="false" value="#{sysadminSurveyDetail.surveyOnBlogPreview}"></h:outputText>
        </t:panelTab>
        <t:panelTab id="panel_f" label="Requirements">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim escape="false">#{sysadminSurveyDetail.surveyCriteriaAsHtml}</f:verbatim>
        </t:panelTab>
    </t:panelTabbedPane>
    




<%@ include file="/jsp/templates/footer.jsp" %>



