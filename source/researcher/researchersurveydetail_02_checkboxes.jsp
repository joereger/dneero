<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02checkboxes" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02checkboxes) Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes")).getSurvey().getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail02checkboxes researcherSurveyDetail02checkboxes = (ResearcherSurveyDetail02checkboxes)Pagez.getBeanMgr().get("ResearcherSurveyDetail02checkboxes");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherSurveyDetail02checkboxes.setQuestion(Textbox.getValueFromRequest("question", "Question", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail02checkboxes.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
            researcherSurveyDetail02checkboxes.setOptions(Textarea.getValueFromRequest("options", "Options", true));
            researcherSurveyDetail02checkboxes.setImage(Textbox.getValueFromRequest("image", "Image URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02checkboxes.setAudio(Textbox.getValueFromRequest("audio", "Audio URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02checkboxes.setVideo(Textbox.getValueFromRequest("video", "Video URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02checkboxes.saveQuestion();
            Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail02checkboxes.getSurvey().getSurveyid());
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>

<%@ include file="/template/header.jsp" %>

    <a href="#" id="helplink"><img src="/images/helpswitch.gif" alt="Help" border="0" align="right"/></a>
    <div id="togglehelp">
        <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        Type a question in the Question box.  Next, give respondents options to choose from.  Because this is a Checkbox question type, they'll be able to choose multiple options as part of their answer.
        <br/><br/>
        Here's what a Checkboxes question looks like:
        <center><img src="/images/questiontypes/selectmultiple.gif" border="0"></img></center>
        </font></div>
    </div>

    <br/><br/>



    <!-- Start Bottom -->
    <form action="/researcher/researchersurveydetail_02_checkboxes.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_02_checkboxes.jsp">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02checkboxes.getQuestionid()%>"/>

        <table cellpadding="5" cellspacing="0" border="0">
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
                    <font class="formfieldnamefont">Question</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("question", researcherSurveyDetail02checkboxes.getQuestion(), 1000, 60, "", "")%>
                </td>
            </tr>

             <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Is Required?</font>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("isrequired", researcherSurveyDetail02checkboxes.getIsrequired(), "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Image URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("image", researcherSurveyDetail02checkboxes.getImage(), 1000, 30, "", "")%>
                    <br/><font class="tinyfont">Fully qualified url referencing a GIF, PNG or JPG.</font>
                </td>
            </tr>

            <%--<tr>--%>
                <%--<td valign="top">--%>
                    <%--<font class="formfieldnamefont">Audio URL (Optional)</font>--%>
                <%--</td>--%>
                <%--<td valign="top">--%>
                    <%--<%=Textbox.getHtml("audio", researcherSurveyDetail02checkboxes.getAudio(), 1000, 30, "", "")%>--%>
                <%--</td>--%>
            <%--</tr>--%>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Video URL (Optional)</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("video", researcherSurveyDetail02checkboxes.getVideo(), 1000, 30, "", "")%>
                    <br/><font class="tinyfont">Fully qualified url referencing an FLV or MP4 file.</font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Options</font>
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
                    <input type="submit" class="formsubmitbutton" value="Save Question and Continue">
                    <br/><br/><a href="/researcher/researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02checkboxes.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
            </tr>

        </table>
    </form>
    <!-- End Bottom -->

<script>
        $("#helplink").click(function() {
            $("#togglehelp").toggle();
        });
        $("#togglehelp").hide();
    </script>

<%@ include file="/template/footer.jsp" %>