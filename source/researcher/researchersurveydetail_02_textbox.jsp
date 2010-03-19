<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02textbox" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02textbox) Pagez.getBeanMgr().get("ResearcherSurveyDetail02textbox")).getSurvey().getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail02textbox researcherSurveyDetail02textbox = (ResearcherSurveyDetail02textbox)Pagez.getBeanMgr().get("ResearcherSurveyDetail02textbox");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherSurveyDetail02textbox.setQuestion(Textbox.getValueFromRequest("question", "Question", true, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02textbox.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
            researcherSurveyDetail02textbox.setImage(Textbox.getValueFromRequest("image", "Image URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02textbox.setAudio(Textbox.getValueFromRequest("audio", "Audio URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02textbox.setVideo(Textbox.getValueFromRequest("video", "Video URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02textbox.saveQuestion();
            Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail02textbox.getSurvey().getSurveyid());
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <a href="#" id="helplink"><img src="/images/helpswitch.gif" alt="Help" border="0" align="right"/></a>
    <div id="togglehelp">
        <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        Type a question in the Question box.  Respondents will be able to type a short piece of text as an answer to this question.
        <br/><br/>
        Here's what a Textbox question looks like:
        <img src="/images/questiontypes/textbox.gif" border="0" alt=""></img>
        </font></div>
    </div>

    <br/><br/>



    <!-- Start Bottom -->
    <form action="/researcher/researchersurveydetail_02_textbox.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_02_textbox.jsp">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02textbox.getQuestionid()%>"/>

    <div id="tabs">
        <ul>
            <li><a href="#tabs-1">Add Question</a></li>
        </ul>
        <div id="tabs-1">

        <table cellpadding="5" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question Type</font>
                </td>
                <td valign="top">
                    <font class="normalfont">Textbox</font>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Question</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("question", researcherSurveyDetail02textbox.getQuestion(), 1000, 60, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Is Required?</font>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("isrequired", researcherSurveyDetail02textbox.getIsrequired(), "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Image URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("image", researcherSurveyDetail02textbox.getImage(), 1000, 30, "", "")%>
                    <br/><font class="tinyfont">Fully qualified url referencing a GIF, PNG or JPG.</font>
                </td>
            </tr>

            <%--<tr>--%>
                <%--<td valign="top">--%>
                    <%--<font class="formfieldnamefont">Audio URL (Optional)</font>--%>
                <%--</td>--%>
                <%--<td valign="top">--%>
                    <%--<%=Textbox.getHtml("audio", researcherSurveyDetail02textbox.getAudio(), 1000, 30, "", "")%>--%>
                <%--</td>--%>
            <%--</tr>--%>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Video URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("video", researcherSurveyDetail02textbox.getVideo(), 1000, 30, "", "")%>
                    <br/><font class="tinyfont">Fully qualified url referencing an FLV or MP4 file.</font>
                </td>
            </tr>


            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save Question and Continue">
                    <br/><br/><a href="researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02textbox.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
            </tr>

        </table>
        </div></div>

    </form>
    <!-- End Bottom -->

<script>
        $("#helplink").click(function() {
            $("#togglehelp").toggle();
        });
        $("#togglehelp").hide();
    </script>
<script>
        $('#tabs').tabs();
</script>

<%@ include file="/template/footer.jsp" %>