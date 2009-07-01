<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02matrix" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02matrix) Pagez.getBeanMgr().get("ResearcherSurveyDetail02matrix")).getSurvey().getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail02matrix researcherSurveyDetail02matrix = (ResearcherSurveyDetail02matrix)Pagez.getBeanMgr().get("ResearcherSurveyDetail02matrix");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherSurveyDetail02matrix.setQuestion(Textbox.getValueFromRequest("question", "Question", true, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02matrix.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
            researcherSurveyDetail02matrix.setRows(Textarea.getValueFromRequest("rows", "Rows", true));
            researcherSurveyDetail02matrix.setCols(Textarea.getValueFromRequest("cols", "Cols", true));
            researcherSurveyDetail02matrix.setImage(Textbox.getValueFromRequest("image", "Image URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02matrix.setAudio(Textbox.getValueFromRequest("audio", "Audio URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02matrix.setVideo(Textbox.getValueFromRequest("video", "Video URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02matrix.saveQuestion();
            Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail02matrix.getSurvey().getSurveyid());
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    In a Matrix question type respondents are given a grid of possible answers.   To create this grid enter options for the Rows and the Columns and decide whether respondents can choose many answers per row.
    <br/><br/>
    Here's what a Matrix question looks like:
    <center><img src="/images/questiontypes/matrixselectone.gif" border="0"></img></center>
    </font></div></center>

    <br/><br/>



    <!-- Start Bottom -->
     <form action="/researcher/researchersurveydetail_02_matrix.jsp" method="post">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_02_matrix.jsp">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02matrix.getQuestionid()%>"/>

        <table cellpadding="5" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question Type</font>
                </td>
                <td valign="top">
                    <font class="normalfont">Matrix</font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("question", researcherSurveyDetail02matrix.getQuestion(), 1000, 60, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Is Required?</font>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("isrequired", researcherSurveyDetail02matrix.getIsrequired(), "", "")%>
                </td>
            </tr>


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Image URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("image", researcherSurveyDetail02matrix.getImage(), 1000, 30, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Audio URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("audio", researcherSurveyDetail02matrix.getAudio(), 1000, 30, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Video URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("video", researcherSurveyDetail02matrix.getVideo(), 1000, 30, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Respondents choose many answers per row?</font>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("respondentcanselectmany", researcherSurveyDetail02matrix.getRespondentcanselectmany(), "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Rows</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">One option per line.</font><br/>
                    <%=Textarea.getHtml("rows", researcherSurveyDetail02matrix.getRows(), 10, 25, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Columns</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">One option per line.</font><br/>
                    <%=Textarea.getHtml("cols", researcherSurveyDetail02matrix.getCols(), 10, 25, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Save Question and Continue">
                    <br/><br/><a href="/researcher/researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02matrix.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
            </tr>

        </table>
    </form>
    <!-- End Bottom -->



<%@ include file="/template/footer.jsp" %>