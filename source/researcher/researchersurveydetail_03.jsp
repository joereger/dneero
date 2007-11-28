<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail03" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.dao.Question" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-03.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail03) Pagez.getBeanMgr().get("ResearcherSurveyDetail03")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
    String navtab="researchers";
    String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail03 researcherSurveyDetail03 = (ResearcherSurveyDetail03)Pagez.getBeanMgr().get("ResearcherSurveyDetail03");
%>
<%
    if (request.getParameter("action")!=null && request.getParameter("action").equals("resetformatting")) {
        try {
            logger.debug("Resetformatting was clicked");
            researcherSurveyDetail03.resetFormatting();
            Pagez.getUserSession().setMessage("Template reset.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            researcherSurveyDetail03.setTemplate((Textbox.getValueFromRequest("template", "Template", false, DatatypeString.DATATYPEID)));
            researcherSurveyDetail03.setEmbedflash(CheckboxBoolean.getValueFromRequest("embedflash"));
            researcherSurveyDetail03.setEmbedjavascript(CheckboxBoolean.getValueFromRequest("embedjavascript"));
            researcherSurveyDetail03.setEmbedlink(CheckboxBoolean.getValueFromRequest("embedlink"));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherSurveyDetail03.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_04.jsp?surveyid="+researcherSurveyDetail03.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your survey has been saved.");
                researcherSurveyDetail03.saveSurvey();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail03.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail03.getSurvey().getSurveyid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchersurveydetail_03.jsp" method="post" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_03.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail03.getSurvey().getSurveyid()%>"/>

    <br/><br/>

    <!-- Start Top -->

    <script language="JavaScript" type="text/javascript">
      var panels = new Array('panel1', 'panel2');
      var selectedTab = null;
      function showPanel(tab, name)
      {
        if (selectedTab)
        {
          selectedTab.style.backgroundColor = '';
          selectedTab.style.paddingTop = '';
          selectedTab.style.marginTop = '4px';
        }
        selectedTab = tab;
        selectedTab.style.backgroundColor = 'white';
        selectedTab.style.paddingTop = '6px';
        selectedTab.style.marginTop = '0px';

        for(i = 0; i < panels.length; i++)
          document.getElementById(panels[i]).style.display = (name == panels[i]) ? 'block':'none';

        return false;
      }
    </script>
    <div id="tabs">
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel1');" id="tab1" onclick="return false;">Preview of Survey in Blog</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" onclick="return false;">Advanced Formatting</a>
    </div>
    <div class="panel" id="panel1" style="display: block">
            <img src="/images/clear.gif" width="725" height="1"/><br/>
            <center>
                <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
                <img src="/images/lightbulb_on.png" alt="" align="right"/>
                This is how your survey looks when posted to a blog.  Bloggers, depending on their blogging tool, can choose from one of many embedding methods.  Each renders the survey slightly differently.  You can enable/disable each method on the Advanced Formatting tab.
                <br/><br/></font></div></center>
                <table cellpadding="30" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <font class="formfieldnamefont">Flash Embed:</font>
                        </td>
                        <td valign="top" align="left">
                            <%=researcherSurveyDetail03.getEmbedflashsyntax()%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" nowrap="true" style="border-top: solid 2px #cccccc;">
                            <font class="formfieldnamefont">Javascript Embed:</font>
                        </td>
                        <td valign="top" align="left" style="border-top: solid 2px #cccccc;">
                            <%=researcherSurveyDetail03.getEmbedjavascriptsyntax()%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" nowrap="true" style="border-top: solid 2px #cccccc;">
                            <font class="formfieldnamefont">Image Embed:</font>
                        </td>
                        <td valign="top" align="left" style="border-top: solid 2px #cccccc;">
                            <%=researcherSurveyDetail03.getEmbedimagesyntax()%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" nowrap="true" style="border-top: solid 2px #cccccc;">
                            <font class="formfieldnamefont">Link Embed:</font>
                        </td>
                        <td valign="top" align="left" style="border-top: solid 2px #cccccc;">
                            <%=researcherSurveyDetail03.getEmbedlinksyntax()%>
                        </td>
                    </tr>
                </table>
            </center>
    </div>
    <div class="panel" id="panel2" style="display: none">
            <img src="/images/clear.gif" width="725" height="1"/><br/>
            <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
            <img src="/images/lightbulb_on.png" alt="" align="right"/>
            In this step you can optionally change the formatting of the survey as it appears on a person's blog.  Click the Advanced Formatting tab to get started.  This requires some basic HTML skills and is very powerful:
            <ul>
            <li>Change the order of questions</li>
            <li>Describe your motivations behind the survey</li>
            </ul>
            Advanced Formatting is powerful.  Please keep in mind that you are dealing with space on a person's blog.  Being more outrageous may require you to pay a higher per-display amount to get bloggers to post it.  Be careful and respectful.
            <br/><br/>
            Add HTML formatting to the box below.  You must click Save Advanced Formatting to see your changes.  A list of question tags can be found to the right of the screen. Use these tags to add questions to the survey.  Each question tag will be replaced with the respondent's actual answer when it's displayed on their blog.  Before you move to the next section verify that your survey looks like you want it to look.
            </font></div></center>
            <br/><br/>
            <table cellpadding="0" cellspacing="5" border="0">
                <tr>
                    <td valign="top" width="75%">
                        <%if (researcherSurveyDetail03.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                            <input type="submit" class="formsubmitbutton" value="Reset" onclick="document.rsdform.action.value='resetformatting'">
                            <br/>
                            <%=Textarea.getHtml("template", researcherSurveyDetail03.getTemplate(), 15, 45, "", "")%>
                            <br/><br/>
                            <table cellpadding="0" cellspacing="5" border="0">
                                <tr>
                                    <td valign="top" nowrap="true">
                                        <%=CheckboxBoolean.getHtml("embedjavascript", researcherSurveyDetail03.getEmbedjavascript(), "", "")%>
                                        <font class="formfieldnamefont">Allow Javascript Embed?</font>
                                    </td>
                                    <td valign="top">
                                        <font class="smallfont">This is the most robust embedding option, least likely to cause issues with browsers/users.</font>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top" nowrap="true">
                                        <%=CheckboxBoolean.getHtml("embedflash", researcherSurveyDetail03.getEmbedflash(), "", "")%>
                                        <font class="formfieldnamefont">Allow Flash Embed?</font>
                                    </td>
                                    <td valign="top">
                                        <font class="smallfont">Flash embedding is powerful because it opens up MySpace and some other hard-to-embed environments but this reach comes at a cost:  you're unable to embed video/images in your survey or do advanced html formatting.</font>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top" nowrap="true">
                                        <%=CheckboxBoolean.getHtml("embedlink", researcherSurveyDetail03.getEmbedlink(), "", "")%>
                                        <font class="formfieldnamefont">Allow Link Embed?</font>
                                    </td>
                                    <td valign="top">
                                        <font class="smallfont">The simplest form of embedding that works anywhere on the web.  Only a single image link appears on the blogger's site.  That link goes to a page that displays the survey.</font>
                                    </td>
                                </tr>
                            </table>
                            <input type="submit" class="formsubmitbutton" value="Reset" onclick="document.getElementById('action').value='resetformatting';">
                            <input type="submit" class="formsubmitbutton" value="Save Advanced Formatting" onclick="document.getElementById('action').value='savetemplate';">
                        <%}%>
                    </td>
                    <td valign="top">
                        <!-- Begin Question Key -->
                        <div class="rounded" style="background: #e6e6e6;">
                            <font class="mediumfont">Question Tags:</font>
                            <br/>
                            <font class="smallfont">Use these tags to move a question around in the survey.  You can create tables using the html tags below but remember that you have a set 425 pixels to work with.</font>
                            <br/>
                            <%
                                for (Iterator iterator=researcherSurveyDetail03.getQuestions().iterator(); iterator.hasNext();){
                                    Question question=(Question) iterator.next();
                                    %>
                                    <b><font class="smallfont">&lt;$question_<%=question.getQuestionid()%>$></font></b>
                                    <br/>
                                    <font class="tinyfont"><%=question.getQuestion()%></font>
                                    <br/><br/>
                                    <%
                                }
                            %>
                        </div>
                        <!-- End Question Key -->
                        <div class="rounded" style="background: #e6e6e6;">
                            <font class="mediumfont">Supported HTML Tags:</font>
                            <br/>
                            <font class="smallfont">A powerful subset of html tags is supported.  These tags must be carefully applied in xHTML format.  If the survey does not display then there is an error with the html syntax.  You can style these tags with basic CSS.</font>
                            <br/>
                            <table cellpadding="0" cellspacing="5" border="0">
                                <tr>
                                    <td valign="top" width="50%">
                                        &lt;a&gt;<br/>
                                        &lt;b&gt;<br/>
                                        &lt;blockquote&gt;<br/>
                                        &lt;div&gt;<br/>
                                        &lt;h1&gt; - &lt;h6&gt;<br/>
                                        &lt;hr&gt;<br/>
                                        &lt;i&gt;<br/>
                                        &lt;img&gt;<br/>
                                        &lt;li&gt;<br/>
                                        &lt;ol&gt;<br/>
                                    </td>
                                    <td valign="top" width="50%">
                                        &lt;p&gt;<br/>
                                        &lt;pre&gt;<br/>
                                        &lt;strong&gt;<br/>
                                        &lt;style&gt;<br/>
                                        &lt;table&gt;<br/>
                                        &lt;td&gt;<br/>
                                        &lt;th&gt;<br/>
                                        &lt;tr&gt;<br/>
                                        &lt;ul&gt;<br/>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="rounded" style="background: #e6e6e6;">
                            <font class="mediumfont">Tips:</font>
                            <font class="smallfont">
                                <ul>
                                    <li>HTML in surveys can be finickey.  We must display inside of a Flash movie to provide access to more bloggers and it requires very strictly perfect HTML.</li>
                                    <li>Make small changes at first.</li>
                                    <li>A very common mistake is not closing tags.  All tags must be closed with another tag of the same name (or self-closed).</li>
                                    <li>If you get into trouble and can't get your survey to display, start over by clicking Reset.</li>
                                    <li>Images can be added to surveys and will display in Flash and Javascript embedding.  However, you must close the image tag and you must include width and height attributes that are properly quoted.  Here's an example:<br/>&lt;img src="http://domain.com/image.jpg" width="100" height="50"/> (Note the closing / at the end of the img tag)</li>
                                    <li>Only JPEG/JPG files are supported.  These files can not be progressive JPEGs.</li>
                                </ul>
                            </font>
                        </div>
                    </td>
                </tr>
            </table>
    </div>





    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail03.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>





<%@ include file="/template/footer.jsp" %>