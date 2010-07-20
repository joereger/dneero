<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyQuestionList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.display.components.def.ComponentTypes" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.dneero.display.components.*" %>
<%@ page import="com.dneero.htmlui.Checkboxes" %>
<%@ page import="com.dneero.htmlui.Dropdown" %>
<%@ page import="com.dneero.htmlui.Textbox" %>
<%String jspPageName="/researcher/researchersurveydetail_02.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02) Pagez.getBeanMgr().get("ResearcherSurveyDetail02")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherSurveyDetail02 researcherSurveyDetail02=(ResearcherSurveyDetail02) Pagez.getBeanMgr().get("ResearcherSurveyDetail02");
    ResearcherSurveyQuestionList researcherSurveyQuestionList = (ResearcherSurveyQuestionList)Pagez.getBeanMgr().get("ResearcherSurveyQuestionList");
    ComponentTypes componentTypes = new ComponentTypes();
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherSurveyDetail02.getSurvey().getStatus()>Survey.STATUS_DRAFT){
                if (request.getParameter("action").equals("previous")){
                    Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp?surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"&ispreviousclick=1");
                    return;
                } else {
                    Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp?surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid());
                    return;
                }
            }
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherSurveyDetail02.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_03.jsp?surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your "+Pagez._Survey()+" has been saved.");
                researcherSurveyDetail02.saveSurvey();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail02.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp?surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"&ispreviousclick=1");
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("editquestion")) {
        try {
            researcherSurveyDetail02.beginEdit();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deletequestion")) {
        try {
            researcherSurveyDetail02.deleteQuestion();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("questionorder")) {
        try {
            for (Iterator<Question> iterator=researcherSurveyDetail02.getSurvey().getQuestions().iterator(); iterator.hasNext();) {
                Question question=iterator.next();
                if(Num.isinteger(request.getParameter("questionorder-"+question.getQuestionid()))){
                    int questionorder = Integer.parseInt(request.getParameter("questionorder-"+question.getQuestionid()));
                    question.setQuestionorder(questionorder);
                    question.save();
                }
            }
            researcherSurveyDetail02.getSurvey().refresh();
            researcherSurveyQuestionList.initBean();     
            Pagez.getUserSession().setMessage("Question Orders Saved!");
        } catch (Exception ex) {
            logger.error("", ex);
            Pagez.getUserSession().setMessage("Sorry, there's been an error.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchersurveydetail_02.jsp" method="post"  class="niceform" id="rsdform">
    <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_02.jsp">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail02.getSurvey().getSurveyid()%>"/>



    <a href="#" id="helplink"><img src="/images/helpswitch.gif" alt="Help" border="0" align="right"/></a>
    <div id="togglehelp">
        <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        Add questions to your <%=Pagez._survey()%> on this page.  Choose from the Question Type dropdown box and then click Add Question.  You'll quickly build a list of questions.  Click Preview the <%=Pagez._survey()%> to see what these questions look like to somebody joining the <%=Pagez._survey()%>.  When you're done, click Next Step.
        <br/><br/>
        </font></div>
    </div>


    
    <div id="tabs">
        <ul>
            <li><a href="#tabs-1">Questions</a></li>
            <li><a href="#tabs-2">Preview <%=Pagez._Survey()%></a></li>
            <li><a href="#tabs-3">Preview Embed Widget</a></li>
        </ul>
        <div id="tabs-1">
                <table cellpadding="10" cellspacing="0" border="0">
                    <tr>
                        <td valign="top" width="25%">
                            <%if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                                <font class="mediumfont" style="color: #000000;">Add a Question</font>
                                <br/>
                                <%
                                    Iterator keyValuePairs=componentTypes.getTypes().entrySet().iterator();
                                    for (int i=0; i<componentTypes.getTypes().size(); i++) {
                                        Map.Entry mapentry=(Map.Entry) keyValuePairs.next();
                                        String key=(String) mapentry.getKey();
                                        String value=(String) mapentry.getValue();
                                        String url = "";
                                        if (key.equals(String.valueOf(com.dneero.display.components.Textbox.ID))) {
                                            url="researchersurveydetail_02_textbox.jsp";
                                        }
                                        if (key.equals(String.valueOf(com.dneero.display.components.Essay.ID))) {
                                            url="researchersurveydetail_02_essay.jsp";
                                        }
                                        if (key.equals(String.valueOf(com.dneero.display.components.Dropdown.ID))) {
                                            url="researchersurveydetail_02_dropdown.jsp";
                                        }
                                        if (key.equals(String.valueOf(com.dneero.display.components.Checkboxes.ID))) {
                                            url="researchersurveydetail_02_checkboxes.jsp";
                                        }
                                        if (key.equals(String.valueOf(com.dneero.display.components.Range.ID))) {
                                            url="researchersurveydetail_02_range.jsp";
                                        }
                                        if (key.equals(String.valueOf(com.dneero.display.components.Matrix.ID))) {
                                            url="researchersurveydetail_02_matrix.jsp";
                                        }
                                        if (key.equals(String.valueOf(com.dneero.display.components.TestQuestion.ID))) {
                                            url="researchersurveydetail_02_testquestion.jsp";
                                        }
                                        if (key.equals(String.valueOf(com.dneero.display.components.Infotext.ID))) {
                                            url="researchersurveydetail_02_infotext.jsp";
                                        }
                                        %>
                                        <font class="formfieldnamefont"><a href="/researcher/<%=url%>?isnewquestion=1&surveyid=<%=researcherSurveyDetail02.getSurvey().getSurveyid()%>"><%=value%></a></font><br/>
                                        <%
                                    }
                                %>
                            </div>
                            <%}%>
                        </td>
                        <td valign="top">
                            <%if (researcherSurveyQuestionList.getQuestions()==null || researcherSurveyQuestionList.getQuestions().size()==0){%>
                                <font class="normalfont">This <%=Pagez._survey()%> contains no questions... you need to add one to continue.</font>
                            <%} else {%>
                                <%
                                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                                    cols.add(new GridCol("Question", "<$question$>", false, "", "smallfont"));
                                    cols.add(new GridCol("Required?", "<$isrequired$>", false, "", "smallfont"));
                                    if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {
                                        cols.add(new GridCol("", "<a href=\"/researcher/researchersurveydetail_02.jsp?action=editquestion&questionid=<$questionid$>&componenttype=<$componenttype$>&surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"\">Edit</a>", false, "", "smallfont"));
                                    }
                                    if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {
                                        cols.add(new GridCol("", "<a href=\"/researcher/researchersurveydetail_02.jsp?action=deletequestion&questionid=<$questionid$>&surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"\">Del</a>", false, "", "smallfont"));
                                    }
                                    if (researcherSurveyDetail02.getSurvey().getEmbedversion()==Survey.EMBEDVERSION_02){
                                        cols.add(new GridCol("Order", "<input type='text' name='questionorder-<$questionid$>' value='<$questionorder$>' size='6' maxlength='4'>", true, "", "smallfont"));
                                    }
                                %>
                                <%=Grid.render(researcherSurveyQuestionList.getQuestions(), cols, 50, "/researcher/researchersurveydetail_02.jsp", "page")%>
                                <%if (researcherSurveyDetail02.getSurvey().getEmbedversion()==Survey.EMBEDVERSION_02){%>
                                    <div style="float:right;"><input type="submit" class="formsubmitbutton sexybutton sexysimple sexym" value="update" onclick="document.getElementById('action').value='questionorder';"></div>
                                    <br clear="all"/>
                                <%}%>
                            <%}%>
                        </td>
                    </tr>
                </table>
        </div>
        <div id="tabs-2">
                <%=researcherSurveyDetail02.getSurveyForTakers()%>
        </div>
        <div id="tabs-3">
                <%=researcherSurveyDetail02.getEmbedflashsyntax()%>
        </div>
    </div>
    <%--<div class="panel" id="panel3" style="display: none">--%>
            <%--<img src="/images/clear.gif" width="700" height="1"/><br/>--%>
            <%--<font class="mediumfont">Question Type: Textbox (Short Text)</font><br/>--%>
            <%--<img src="/images/questiontypes/textbox.gif" border="0" alt=""></img><br/><br/>--%>
            <%--<font class="mediumfont">Question Type: Essay (Long Text)</font><br/>--%>
            <%--<img src="/images/questiontypes/essay.gif" border="0" alt=""></img><br/><br/>--%>
            <%--<font class="mediumfont">Question Type: Dropdown (Choose One) *Rankings-compatible</font><br/>--%>
            <%--<img src="/images/questiontypes/dropdown.gif" border="0" alt=""></img><br/><br/>--%>
            <%--<font class="mediumfont">Question Type: Checkboxes (Choose Multiple)</font><br/>--%>
            <%--<img src="/images/questiontypes/selectmultiple.gif" border="0" alt=""></img><br/><br/>--%>
            <%--<font class="mediumfont">Question Type: Range (i.e. 1-10)</font><br/><br/>--%>
            <%--<img src="/images/questiontypes/range.gif" border="0" alt=""></img><br/><br/>--%>
            <%--<font class="mediumfont">Question Type: Matrix (Choose One Per Row)</font><br/>--%>
            <%--<img src="/images/questiontypes/matrixselectone.gif" border="0" alt=""></img><br/><br/>--%>
            <%--<font class="mediumfont">Question Type: Matrix (Choose Many Per Row)</font><br/>--%>
            <%--<img src="/images/questiontypes/matrixselectmany.gif" border="0" alt=""></img><br/><br/>--%>
    <%--</div>--%>



    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail02.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>


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