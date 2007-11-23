<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02range" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02range) Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail02range researcherSurveyDetail02range = (ResearcherSurveyDetail02range)Pagez.getBeanMgr().get("ResearcherSurveyDetail02range");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherSurveyDetail02range.setQuestion(Textbox.getValueFromRequest("question", "Question", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
            researcherSurveyDetail02range.setMintitle(Textbox.getValueFromRequest("mintitle", "Min Title", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.setMin(Textbox.getValueFromRequest("min", "Min", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.setStep(Textbox.getValueFromRequest("step", "Step", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.setMax(Textbox.getValueFromRequest("max", "Max", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.setMaxtitle(Textbox.getValueFromRequest("maxtitle", "Max Title", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.saveQuestion();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    In a Range question type respondents choose from low to high numbers at some interval.  This is also known as a Lichert Scale.  Choose Min and Max Titles to tell surveytakers what the scale represents.  Then choose the Min and Max values as the lowest and highest possible choices. Finally, choose the Step value which determines the numerical distance between option.  For example, Step of 1 means that the scale will simply step from Min to Max like this: 1, 2, 3, 4, etc.  Step of 2 would give you: 2, 4, 6, 8, etc.
    <br/><br/>
    Here's what a Range question looks like:
    <center><img src="../images/questiontypes/range.gif" border="0"></img></center>
    </font></div></center>

    <br/><br/>



    <!-- Start Bottom -->
    <form action="researchersurveydetail_02_checkboxes.jsp" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02range.getQuestionid()%>"/>

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question Type</font>
                </td>
                <td valign="top">
                    <font class="normalfont">Range</font>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <h:outputText value="Question" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=researcherSurveyDetail02range.getQuestion()%>" id="question" required="true">
                        <f:validateLength minimum="3" maximum="254"></f:validateLength>
                    </h:inputText>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <h:outputText value="Is Required?" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("isrequired", researcherSurveyDetail02range.getIsrequired(), "", "")%>
                </td>
            </tr>

        </table>


        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <h:outputText value="Min Title" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:outputText value="Min" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:outputText value="Step" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:outputText value="Max" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:outputText value="Max Title" styleClass="formfieldnamefont"></h:outputText>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <%=Textbox.getHtml("mintitle", researcherSurveyDetail02range.getMintitle(), 20, 7, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("min", String.valueOf(researcherSurveyDetail02range.getMin()), 7, 3, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("step", String.valueOf(researcherSurveyDetail02range.getStep()), 7, 3, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("max", String.valueOf(researcherSurveyDetail02range.getMax()), 7, 3, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("maxtitle", researcherSurveyDetail02range.getMaxtitle(), 20, 7, "", "")%>
                </td>
            </tr>


        </table>


        <table cellpadding="0" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Save Question and Continue">
                    <br/><br/><a href="researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02range.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
            </tr>

        </table>
    </form>
    <!-- End Bottom -->



<%@ include file="/template/footer.jsp" %>