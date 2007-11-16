<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02checkboxes" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02checkboxes) Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
ResearcherSurveyDetail02checkboxes researcherSurveyDetail02checkboxes = (ResearcherSurveyDetail02checkboxes)Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherSurveyDetail02checkboxes.setQuestion(Textbox.getValueFromRequest("question", "Question", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02checkboxes.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
            researcherSurveyDetail02checkboxes.setOptions(Textarea.getValueFromRequest("options", "Options", true));
            researcherSurveyDetail02checkboxes.saveQuestion();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>

<%@ include file="/jsp/templates/header.jsp" %>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Type a question in the Question box.  Next, give respondents options to choose from.  Because this is a Checkbox question type, they'll be able to choose multiple options as part of their answer.
    <br/><br/>
    Here's what a Checkboxes question looks like:
    <center><img src="../images/questiontypes/selectmultiple.gif" border="0"></img></center>
    </font></div></center>

    <br/><br/>



    <!-- Start Bottom -->
    <form action="researchersurveydetail_02_checkboxes.jsp" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02checkboxes.getQuestionid()%>"/>

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question Type</font>
                </td>
                <td valign="top">
                    <font class="normalfont">Checkbox</font>
                </td>
             </tr>


             <tr>
                <td valign="top">
                    <h:outputText value="Question" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=researcherSurveyDetail02checkboxes.getQuestion()%>" id="question" required="true">
                        <f:validateLength minimum="3" maximum="254"></f:validateLength>
                    </h:inputText>
                </td>
            </tr>

             <tr>
                <td valign="top">
                    <h:outputText value="Is Required?" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("isrequired", researcherSurveyDetail02checkboxes.getIsrequired(), "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <h:outputText value="Options" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <font class="smallfont">One option per line.</font><br/>
                    <%=Textarea.getHtml("options", researcherSurveyDetail02checkboxes.getOptions(), 10, 25, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" value="Save Question and Continue">
                    <br/><br/><a href="researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02checkboxes.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
            </tr>

        </table>
    </form>
    <!-- End Bottom -->



<%@ include file="/jsp/templates/footer.jsp" %>