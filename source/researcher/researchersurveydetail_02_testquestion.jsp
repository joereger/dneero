<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02textbox" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02testquestion" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02testquestion) Pagez.getBeanMgr().get("ResearcherSurveyDetail02testquestion")).getSurvey().getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherSurveyDetail02testquestion researcherSurveyDetail02testquestion=(ResearcherSurveyDetail02testquestion) Pagez.getBeanMgr().get("ResearcherSurveyDetail02testquestion");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherSurveyDetail02testquestion.setQuestion(Textbox.getValueFromRequest("question", "Question", true, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02testquestion.setAnswermustcontain(Textbox.getValueFromRequest("answermustcontain", "Answer Must Contain", true, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02testquestion.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
            researcherSurveyDetail02testquestion.setImage(Textbox.getValueFromRequest("image", "Image URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02testquestion.setAudio(Textbox.getValueFromRequest("audio", "Audio URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02testquestion.setVideo(Textbox.getValueFromRequest("video", "Video URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02testquestion.saveQuestion();
            Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail02testquestion.getSurvey().getSurveyid());
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <a href="#" id="helplink"><img src="/images/helpswitch.gif" alt="Help" border="0" align="right"/></a>
    <div id="togglehelp">
        <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <b>A Qualifying Question is a unique type of question that the conversation participant must answer correctly to qualify to join in.</b>  This may come in handy in the following scenarios:
        <ul>
            <li><b>You want people to visit your site</b> before joining the conversation.  Include a link to your site in your conversation and then use a Test Question to ask them something like 'On our homepage what color is the button on the far right'.  If they answer 'blue' then they can join in.</li>
            <li><b>You want people to prove some membership in a group.</b>  Ask a question like 'Who is the president of the East Bay Elk's Club?'</li>
            <li><b>You want people to prove some skill or knowledge.</b>  Ask a question like 'What set of nerves reside close to the corpus collosum in the brain?'</li>
        </ul>
        Type a question in the Question box.  In the 'Respondent's Answer Must Contain' box type a word or phrase that must be included in what the respondent types as their answer.  They'll have to include an <b>exact match</b> of what you type so the answer that you choose should be simple and most anybody who knows the answer should type it.  For example, if you ask 'what color is the sky?' you should enter an answer like 'blue'.  You should not enter an answer like 'the sky is blue' because people will not likely type that exactly.
        <br/><br/>
        <b>If a conversation participant can't answer your question correctly they'll be told that they can't join the conversation.</b>  Note that this test question will not be included in the conversation results or in the widget that people embed into their blogs/social networks.
        </font></div>
    </div>

    <br/><br/>



    <!-- Start Bottom -->
    <form action="/researcher/researchersurveydetail_02_testquestion.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_02_testquestion.jsp">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02testquestion.getQuestionid()%>"/>


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
                        <font class="normalfont">Qualifying Question</font>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Question</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("question", researcherSurveyDetail02testquestion.getQuestion(), 1000, 60, "", "")%>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Image URL (Optional)</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("image", researcherSurveyDetail02testquestion.getImage(), 1000, 30, "", "")%>
                        <br/><font class="tinyfont">Fully qualified url referencing a GIF, PNG or JPG.</font>
                    </td>
                </tr>

                <%--<tr>--%>
                    <%--<td valign="top">--%>
                        <%--<font class="formfieldnamefont">Audio URL (Optional)</font>--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>
                        <%--<%=Textbox.getHtml("audio", researcherSurveyDetail02testquestion.getAudio(), 1000, 30, "", "")%>--%>
                    <%--</td>--%>
                <%--</tr>--%>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Video URL (Optional)</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("video", researcherSurveyDetail02testquestion.getVideo(), 1000, 30, "", "")%>
                        <br/><font class="tinyfont">Fully qualified url referencing an FLV or MP4 file.</font>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Respondent's Answer Must Contain</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("answermustcontain", researcherSurveyDetail02testquestion.getAnswermustcontain(), 250, 35, "", "")%>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save Question and Continue">
                        <br/><br/><a href="researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02testquestion.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
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