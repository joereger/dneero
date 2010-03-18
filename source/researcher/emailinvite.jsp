<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherEmailinvite" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Invite People to Join Your Conversation";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherEmailinvite researcherEmailinvite=(ResearcherEmailinvite) Pagez.getBeanMgr().get("ResearcherEmailinvite");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("invite")) {
        try {
            researcherEmailinvite.setManuallyenteredemailaddresses(Textbox.getValueFromRequest("manuallyenteredemailaddresses", "Email Addresses", true, DatatypeString.DATATYPEID));
            researcherEmailinvite.setMessage(Textarea.getValueFromRequest("message", "Message", false));
            researcherEmailinvite.setSubject(Textbox.getValueFromRequest("subject", "Subject", false, DatatypeString.DATATYPEID));
            researcherEmailinvite.setSurveyiduserisinvitedto(Dropdown.getIntFromRequest("surveyiduserisinvitedto", "Conversation to Invite To", true));
            researcherEmailinvite.invite();
            Pagez.sendRedirect("/researcher/emailinvite-complete.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



    <form action="/researcher/emailinvite.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/researcher/emailinvite.jsp">
        <input type="hidden" name="action" value="invite">

        <%if (!researcherEmailinvite.getResearcherhasatleastonelivesurvey()){%>
            <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
            <img src="/images/lightbulb_on.png" alt="" align="right"/>
            You must have at least one conversation with a status of Live to invite people to.  Check <a href="/researcher/index.jsp">Your List of Conversations</a>.  Create a conversation first and then come back to invite people to it.  If you just created a conversation, sometimes it takes a few minutes for the financial system to process and mark the conversation as Live... check back soon.  You'll be able to upload a list of email addresses or manually enter them.  You'll be able to set a custom message and email subject.
            <br/><br/><br/></font></div></center>
        <%}%>
        <%if (researcherEmailinvite.getResearcherhasatleastonelivesurvey()){%>
            <form action="/researcher/emailinvite.jsp" method="post" class="niceform">
                <input type="hidden" name="dpage" value="/researcher/emailinvite.jsp">
                <input type="hidden" name="action" value="invite">
                <table cellpadding="10" cellspacing="0" border="0">
                    <tr>
                        <td valign="top" width="50%">
                            <div class="rounded" style="background: #e6e6e6;">
                               <font class="mediumfont">Invite Via Email</font>
                               <br/><br/>



                                   <font class="formfieldnamefont">Conversations that are Live now:</font>
                                   <br/>
                                   <%=Dropdown.getHtml("surveyiduserisinvitedto", String.valueOf(researcherEmailinvite.getSurveyiduserisinvitedto()), researcherEmailinvite.getSurveyids(), "","")%>
                                   <br/><br/>
                                   <font class="formfieldnamefont">Subject:</font>
                                   <br/>
                                   <%=Textbox.getHtml("subject", researcherEmailinvite.getSubject(), 255, 35, "", "")%>

                                   <br/><br/>
                                   <font class="formfieldnamefont">Message (we'll add links to the conversation):</font>
                                   <br/>
                                   <%=Textarea.getHtml("message", researcherEmailinvite.getMessage(), 3, 35, "", "")%>


                                   <br/><br/>
                                   <font class="formfieldnamefont">Email Addresses (one per line):</font>
                                   <br/>
                                   <%=Textarea.getHtml("manuallyenteredemailaddresses", researcherEmailinvite.getManuallyenteredemailaddresses(), 3, 35, "", "")%>

                                   <%//@todo file upload list of email addresses in /researcher/emailinvite.jsp%>
                                   <!--
                                   <br/><br/>
                                   <font class="formfieldnamefont">Optionally upload a text or csv file of email addresses.<br/>One email address per line/row.<br/>In csv files email address must be in first column.</font>
                                   <br/>
                                   <t:inputFileUpload id="fileupload" accept="text/*" value="" storage="file" styleClass="none" required="false" maxlength="20000000"/>
                                    -->

                                   <br/><br/>
                                   <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Preview Invite">
                            </div>
                        </td>
                        <td valign="top">
                            <%if (researcherEmailinvite.getSurvey()!=null){%>
                                <div class="rounded" style="background: #e6e6e6;">
                                    <font class="mediumfont">Send them a Link</font>
                                    <br/><br/>
                                    <font class="formfieldnamefont">Copy and paste the link below into an email, blog or social networking profile:</font>
                                    <br/>
                                    <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><a href="<%=researcherEmailinvite.getUrl()%>"><%=researcherEmailinvite.getSurvey().getTitle()%></a></textarea>
                                    <br/><br/>
                                    <a href="<%=researcherEmailinvite.getUrl()%>"><font class="mediumfont">See Your Conversation Here</font></a>
                                    <br/><br/>
                                </div>
                            <%}%>
                        </td>
                    </tr>
                </table>
            </form>
        <%}%>



<%@ include file="/template/footer.jsp" %>

