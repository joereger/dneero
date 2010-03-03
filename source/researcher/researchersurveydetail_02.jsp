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
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="<img src=\"/images/process-train-survey-02.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherSurveyDetail02) Pagez.getBeanMgr().get("ResearcherSurveyDetail02")).getTitle() + "</font>\n" +
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
                    Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp?surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid());
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
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                researcherSurveyDetail02.saveSurvey();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail02.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp?surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid());
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



    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Add questions to your conversation on this page.  Choose from the Question Type dropdown box and then click Add Question.  You'll quickly build a list of questions.  Click Preview the Conversation to see what these questions look like to somebody joining the conversation.  When you're done, click Next Step.
    <br/><br/>
    </font></div></center>

    <br/><br/>

    <script language="JavaScript" type="text/javascript">
      var panels = new Array('panel1', 'panel2', 'panel3');
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

        for(i = 0; i < panels.length; i++){
          document.getElementById(panels[i]).style.display = (name == panels[i]) ? 'block':'none';
        }
        return false;
      }
    </script>
    <div id="tabs">
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel1');" id="tab1" onclick="return false;">Questions</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" onclick="return false;">Preview Your Conversation</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel3');" onclick="return false;">Question Type Samples</a>
    </div>
    <div class="panel" id="panel1" style="display: block">
            <img src="/images/clear.gif" width="700" height="1"/><br/>

            <%if (researcherSurveyQuestionList.getQuestions()==null || researcherSurveyQuestionList.getQuestions().size()==0){%>
                <font class="normalfont">This conversation contains no questions... you need to add one to continue.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("", "<img src=\"/images/question.png\"/>", true, "", ""));
                    cols.add(new GridCol("Question", "<$question$>", false, "", "smallfont"));
                    cols.add(new GridCol("Required?", "<$isrequired$>", false, "", "smallfont"));
                    if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {
                        cols.add(new GridCol("", "<a href=\"/researcher/researchersurveydetail_02.jsp?action=editquestion&questionid=<$questionid$>&componenttype=<$componenttype$>&surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"\">Edit</a>", false, "", "smallfont"));
                    }
                    if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {
                        cols.add(new GridCol("", "<a href=\"/researcher/researchersurveydetail_02.jsp?action=deletequestion&questionid=<$questionid$>&surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"\">Del</a>", false, "", "smallfont"));
                    }
                    if (researcherSurveyDetail02.getSurvey().getEmbedversion()==Survey.EMBEDVERSION_02){
                        cols.add(new GridCol("Order", "<input type='text' name='questionorder-<$questionid$>' value='<$questionorder$>' size='2' maxlength='4'>", true, "", "smallfont"));
                    }
                %>
                <%=Grid.render(researcherSurveyQuestionList.getQuestions(), cols, 50, "/researcher/researchersurveydetail_02.jsp", "page")%>
                <%if (researcherSurveyDetail02.getSurvey().getEmbedversion()==Survey.EMBEDVERSION_02){%>
                    <div style="float:right;"><input type="submit" class="formsubmitbutton" value="Save Question Orders" onclick="document.getElementById('action').value='questionorder';"></div>
                    <br clear="all"/>
                <%}%>
            <%}%>


            <%if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                <br/><br/>
                <font class="mediumfont" style="color: #000000;">Add a Question of Type</font>
                <br/>
                <font class="smallfont" style="color: #000000;">See tab at upper right for Question Type Samples</font>
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
            <%}%>
    </div>
    <div class="panel" id="panel2" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <%=researcherSurveyDetail02.getSurveyForTakers()%>
    </div>
    <div class="panel" id="panel3" style="display: none">
            <img src="/images/clear.gif" width="700" height="1"/><br/>
            <font class="mediumfont">Question Type: Textbox (Short Text)</font><br/>
            <img src="/images/questiontypes/textbox.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Essay (Long Text)</font><br/>
            <img src="/images/questiontypes/essay.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Dropdown (Choose One) *Rankings-compatible</font><br/>
            <img src="/images/questiontypes/dropdown.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Checkboxes (Choose Multiple)</font><br/>
            <img src="/images/questiontypes/selectmultiple.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Range (i.e. 1-10)</font><br/><br/>
            <img src="/images/questiontypes/range.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Matrix (Choose One Per Row)</font><br/>
            <img src="/images/questiontypes/matrixselectone.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Matrix (Choose Many Per Row)</font><br/>
            <img src="/images/questiontypes/matrixselectmany.gif" border="0" alt=""></img><br/><br/>
    </div>



    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail02.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>


<%@ include file="/template/footer.jsp" %>