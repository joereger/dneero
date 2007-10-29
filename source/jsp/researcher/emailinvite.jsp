<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Invite People to Take Your Survey";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <h:form id="emailinvite" enctype="multipart/form-data">
        <t:div rendered="#{!researcherEmailinvite.researcherhasatleastonelivesurvey}">
            <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
            <img src="/images/lightbulb_on.png" alt="" align="right"/>
            You must have at least one survey with a status of Live to invite people to.  Check <h:commandLink value="Your List of Surveys" action="#{researcherSurveyList.beginView}" styleClass="smallfont" style="color: #0000ff;"/>.  Create a survey first and then come back to invite people to it.  If you just created a survey, sometimes it takes a few minutes for the financial system to process and mark the survey as Live... check back soon.  You'll be able to upload a list of email addresses or manually enter them.  You'll be able to set a custom message and email subject.  
            <br/><br/><br/></font></div></center>
        </t:div>
        <t:div rendered="#{researcherEmailinvite.researcherhasatleastonelivesurvey}">
            <table cellpadding="10" cellspacing="0" border="0">
                <tr>
                    <td valign="top" width="50%">
                        <div class="rounded" style="background: #e6e6e6;">
                           <font class="mediumfont">Invite Via Email</font>
                           <br/><br/>

                           <font class="formfieldnamefont">Surveys that are Live now:</font>
                           <br/>
                           <h:selectOneMenu value="#{researcherEmailinvite.surveyiduserisinvitedto}" id="surveyiduserisinvitedto" required="true">
                               <f:selectItems value="#{researcherEmailinvite.surveyids}"/>
                           </h:selectOneMenu>

                           <br/><br/>
                           <font class="formfieldnamefont">Subject:</font>
                           <br/>
                           <h:panelGroup>
                                <h:inputText value="#{researcherEmailinvite.subject}" id="subject" required="true" size="40"></h:inputText>
                           </h:panelGroup>

                           <br/><br/>
                           <font class="formfieldnamefont">Message (we'll add links to the survey):</font>
                           <br/>
                           <h:inputTextarea value="#{researcherEmailinvite.message}" id="message" cols="40" rows="3">
                                <f:validateLength minimum="0" maximum="10000"></f:validateLength>
                           </h:inputTextarea>

                           <br/><br/>
                           <font class="formfieldnamefont">Email Addresses (one per line):</font>
                           <br/>
                           <h:inputTextarea value="#{researcherEmailinvite.manuallyenteredemailaddresses}" id="manuallyenteredemailaddresses" cols="30" rows="5">
                                <f:validateLength minimum="0" maximum="100000"></f:validateLength>
                           </h:inputTextarea>

                           <br/><br/>
                           <font class="formfieldnamefont">Optionally upload a text or csv file of email addresses.<br/>One email address per line/row.<br/>In csv files email address must be in first column.</font>
                           <br/>
                           <t:inputFileUpload id="fileupload" accept="text/*" value="#{researcherEmailinvite.fileupload}" storage="file" styleClass="none" required="false" maxlength="20000000"/>

                           <br/><br/>
                           <h:commandButton action="#{researcherEmailinvite.invite}" value="Preview Invite" styleClass="formsubmitbutton"></h:commandButton>
                       </div>
                    </td>
                    <td valign="top">
                        <div class="rounded" style="background: #e6e6e6;">
                            <font class="mediumfont">Send them a Link</font>
                            <br/><br/>
                            <font class="formfieldnamefont">Copy and paste the link below into an email, blog or social networking profile:</font>
                            <br/>
                            <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><a href="#{researcherEmailinvite.url}">#{researcherEmailinvite.survey.title}</a></textarea>
                            <br/><br/>
                            <a href="#{researcherEmailinvite.url}"><font class="mediumfont">See Your Survey Here</font></a>
                            <br/><br/>
                        </div>
                    </td>
                </tr>
            </table>
        </t:div>



<%@ include file="/jsp/templates/footer.jsp" %>

