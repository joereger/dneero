<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02range" %>
<%@ page import="com.dneero.htmlui.*" %>
<%String jspPageName="/researcher/researchersurveydetail_02_range.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02range) Pagez.getBeanMgr().get("ResearcherSurveyDetail02range")).getSurvey().getTitle() + "</font>\n" +
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
            researcherSurveyDetail02range.setImage(Textbox.getValueFromRequest("image", "Image URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.setAudio(Textbox.getValueFromRequest("audio", "Audio URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.setVideo(Textbox.getValueFromRequest("video", "Video URL", false, com.dneero.htmlui.DatatypeString.DATATYPEID));
            researcherSurveyDetail02range.saveQuestion();
            Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail02range.getSurvey().getSurveyid());
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <a href="#" id="helplink"><img src="/images/helpswitch.gif" alt="Help" border="0" align="right"/></a>
    <div id="togglehelp">
        <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        In a Range question type respondents choose from low to high numbers at some interval.  This is also known as a Lichert Scale.  Choose Min and Max Titles to tell <%=Pagez._survey()%> participants what the scale represents.  Then choose the Min and Max values as the lowest and highest possible choices. Finally, choose the Step value which determines the numerical distance between option.  For example, Step of 1 means that the scale will simply step from Min to Max like this: 1, 2, 3, 4, etc.  Step of 2 would give you: 2, 4, 6, 8, etc.
        <br/><br/>
        Here's what a Range question looks like:
        <center><img src="/images/questiontypes/range.gif" border="0"></img></center>
        </font></div>
     </div>


    <br/><br/>



    <!-- Start Bottom -->
    <form action="/researcher/researchersurveydetail_02_range.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_02_range.jsp">
        <input type="hidden" name="action" value="save" id="action">
        <input type="hidden" name="surveyid" value="<%=Pagez.getUserSession().getCurrentSurveyid()%>"/>
        <input type="hidden" name="questionid" value="<%=researcherSurveyDetail02range.getQuestionid()%>"/>

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
                        <font class="normalfont">Range</font>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Question</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("question", researcherSurveyDetail02range.getQuestion(), 1000, 60, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Is Required?</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isrequired", researcherSurveyDetail02range.getIsrequired(), "", "")%>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Image URL (Optional)</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("image", researcherSurveyDetail02range.getImage(), 1000, 30, "", "")%>
                        <br/><font class="tinyfont">Fully qualified url referencing a GIF, PNG or JPG.</font>
                    </td>
                </tr>

                <%--<tr>--%>
                    <%--<td valign="top">--%>
                        <%--<font class="formfieldnamefont">Audio URL (Optional)</font>--%>
                    <%--</td>--%>
                    <%--<td valign="top">--%>
                        <%--<%=Textbox.getHtml("audio", researcherSurveyDetail02range.getAudio(), 1000, 30, "", "")%>--%>
                    <%--</td>--%>
                <%--</tr>--%>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Video URL (Optional)</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("video", researcherSurveyDetail02range.getVideo(), 1000, 30, "", "")%>
                        <br/><font class="tinyfont">Fully qualified url referencing an FLV or MP4 file.</font>
                    </td>
                </tr>

            </table>


            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Min Title</font>
                    </td>
                    <td valign="top">
                        <font class="formfieldnamefont">Min</font>
                    </td>
                    <td valign="top">
                        <font class="formfieldnamefont">Step</font>
                    </td>
                    <td valign="top">
                        <font class="formfieldnamefont">Max</font>
                    </td>
                    <td valign="top">
                        <font class="formfieldnamefont">Max Title</font>
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
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save Question and Continue">
                        <br/><br/><a href="/researcher/researchersurveydetail_02.jsp?surveyid=<%=researcherSurveyDetail02range.getSurvey().getSurveyid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
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

<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>