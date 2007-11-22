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
<%@ include file="/jsp/templates/auth.jsp" %>
<%
    ResearcherSurveyDetail02 researcherSurveyDetail02=(ResearcherSurveyDetail02) Pagez.getBeanMgr().get("ResearcherSurveyDetail02");
    ResearcherSurveyQuestionList researcherSurveyQuestionList = (ResearcherSurveyQuestionList)Pagez.getBeanMgr().get("ResearcherSurveyQuestionList");
    ComponentTypes componentTypes = (ComponentTypes)Pagez.getBeanMgr().get("ComponentTypes");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("save") || request.getParameter("action").equals("previous"))) {
        try {
            if (request.getParameter("action").equals("next")) {
                researcherSurveyDetail02.saveSurvey();
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                researcherSurveyDetail02.saveSurveyAsDraft();
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail02.previousStep();
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>


<form action="researchersurveydetail_02.jsp" method="post" id="rsdform">
    <input type="hidden" name="action" value="next">
    <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail02.getSurvey().getSurveyid()%>"/>



    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Add questions to your survey on this page.  Choose from the Question Type dropdown box and then click Add Question.  You'll quickly build a list of questions.  Click Preview the Survey to see what these questions look like to somebody taking the survey.  When you're done, click Next Step.
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
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" onclick="return false;">Preview Your Survey</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel3');" onclick="return false;">Question Type Samples</a>
    </div>
    <div class="panel" id="panel1" style="display: block">
            <img src="/images/clear.gif" width="700" height="1"/><br/>

            <%if (researcherSurveyQuestionList.getQuestions()==null || researcherSurveyQuestionList.getQuestions().size()==0){%>
                <font class="normalfont">This survey contains no questions.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("", "<img src=\"/images/question.png\"/>", true, "", ""));
                    cols.add(new GridCol("Question", "<$question$>", false, "", "smallfont"));
                    cols.add(new GridCol("Required?", "<$isrequired$>", false, "", "smallfont"));
                    if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {
                        cols.add(new GridCol("", "<a href=\"researchersurveydetail_02.jsp?action=editquestion&questionid=<$questionid$>&componenttype=<$componenttype$>&surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"\">Edit</a>", false, "", "smallfont"));
                    }
                    if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {
                        cols.add(new GridCol("", "<a href=\"researchersurveydetail_02.jsp?action=deletequestion&questionid=<$questionid$>&surveyid="+researcherSurveyDetail02.getSurvey().getSurveyid()+"\">Del</a>", false, "", "smallfont"));
                    }
                %>
                <%=Grid.render(researcherSurveyQuestionList.getQuestions(), cols, 50, "researchersurveydetail_02.jsp", "page")%>
            <%}%>


            <%if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                <br/>
                <font class="mediumfont" style="color: #cccccc;">Add a Question of Type</font>
                <br/>
                <font class="mediumfont" style="color: #000000;">See tab at upper right for Question Type Samples</font>
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
                        %>
                        <a href="<%=url%>?isnewquestion=1&surveyid=<%=researcherSurveyDetail02.getSurvey().getSurveyid()%>"><font class="smallfont"><%=value%></font></a><br/>
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
            <img src="../images/questiontypes/textbox.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Essay (Long Text)</font><br/>
            <img src="../images/questiontypes/essay.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Dropdown (Choose One)</font><br/>
            <img src="../images/questiontypes/dropdown.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Checkboxes (Choose Multiple)</font><br/>
            <img src="../images/questiontypes/selectmultiple.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Range (i.e. 1-10)</font><br/><br/>
            <img src="../images/questiontypes/range.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Matrix (Choose One Per Row)</font><br/>
            <img src="../images/questiontypes/matrixselectone.gif" border="0" alt=""></img><br/><br/>
            <font class="mediumfont">Question Type: Matrix (Choose Many Per Row)</font><br/>
            <img src="../images/questiontypes/matrixselectmany.gif" border="0" alt=""></img><br/><br/>
    </div>



    <br/><br/>
    <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" value="Previous Step" onclick="document.rsdform.action.value='previous'">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail02.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" value="Save and Continue Later" onclick="document.rsdform.action.value='saveasdraft'">
                <%}%>
                <input type="submit" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->
</form>


<%@ include file="/jsp/templates/footer.jsp" %>