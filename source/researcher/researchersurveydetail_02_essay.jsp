<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02essay" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02essay) Pagez.getBeanMgr().get("ResearcherSurveyDetail02essay")).getSurvey().getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail02essay researcherSurveyDetail02essay = (ResearcherSurveyDetail02essay)Pagez.getBeanMgr().get("ResearcherSurveyDetail02essay");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherSurveyDetail02essay.setQuestion(Textbox.getValueFromRequest("question", "Question", true, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02essay.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
            researcherSurveyDetail02essay.setImage(Textbox.getValueFromRequest("image", "Image URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02essay.setAudio(Textbox.getValueFromRequest("audio", "Audio URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02essay.setVideo(Textbox.getValueFromRequest("video", "Video URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02essay.saveQuestion();
            Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail02essay.getSurvey().getSurveyid());
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Type a question in the Question box.  Because this is an Essay question type, respondents will be able to type a free-text response to this question.
    <br/><br/>
    Here's what an Essay question looks like:
    <center><img src="/images/questiontypes/essay.gif" border="0"></img></center>
    </font></div></center>

    <br/><br/>



    <!-- Start Bottom -->
    <form action="/researcher/researchersurveydetail_02_essay.jsp" method="post">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_02_essay.jsp">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02essay.getQuestionid()%>"/>

        <table cellpadding="5" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question Type</font>
                </td>
                <td valign="top">
                    <font class="normalfont">Essay</font>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("question", researcherSurveyDetail02essay.getQuestion(), 1000, 60, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Is Required?</font>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("isrequired", researcherSurveyDetail02essay.getIsrequired(), "", "")%>
                </td>
            </tr>


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Image URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("image", researcherSurveyDetail02essay.getImage(), 1000, 30, "", "")%>
                </td>
            </tr>

            <%--<tr>--%>
                <%--<td valign="top">--%>
                    <%--<font class="formfieldnamefont">Audio URL (Optional)</font>--%>
                <%--</td>--%>
                <%--<td valign="top">--%>
                    <%--<%=Textbox.getHtml("audio", researcherSurveyDetail02essay.getAudio(), 1000, 30, "", "")%>--%>
                <%--</td>--%>
            <%--</tr>--%>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Video URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("video", researcherSurveyDetail02essay.getVideo(), 1000, 30, "", "")%>
                </td>
            </tr>


            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Save Question and Continue">
                    <br/><br/><a href="/researcher/researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02essay.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
            </tr>

        </table>
    </form>
    <!-- End Bottom -->



<%@ include file="/template/footer.jsp" %>