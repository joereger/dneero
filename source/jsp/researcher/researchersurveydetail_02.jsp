<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail02" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyQuestionList" %>
<%@ page import="com.dneero.htmlui.*" %>
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

        for(i = 0; i < panels.length; i++)
          document.getElementById(panels[i]).style.display = (name == panels[i]) ? 'block':'none';

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
            <t:dataTable id="datatable" value="<%=researcherSurveyQuestionList.getQuestions()%>" rows="10" var="question" rendered="#{!empty researcherSurveyQuestionList.questions}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcol,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value=""/>
                </f:facet>
                <h:graphicImage url="/images/question.png"></h:graphicImage>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Question"/>
                </f:facet>
                <h:outputText value="<%=question.getQuestion()%>"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Respondent Must Answer?"/>
                </f:facet>
                <h:outputText value="Yes" rendered="<%=question.getIsrequired()%>"/>
                <h:outputText value="No" rendered="<%=((!question)Pagez.getBeanMgr().get("!question")).getIsrequired()%>"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <%if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <h:commandLink action="<%=researcherSurveyDetail02.getBeginEdit()%>" rendered="#{researcherSurveyDetail02.status eq 1}">
                        <h:outputText value="Edit" escape="false" />
                        <f:param name="questionid" value="<%=question.getQuestionid()%>" />
                        <f:param name="componenttype" value="<%=question.getComponenttype()%>" />
                    </h:commandLink>
                <%}%>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <%if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                    <h:commandLink action="<%=researcherSurveyDetail02.getDeleteQuestion()%>" rendered="#{researcherSurveyDetail02.status eq 1}">
                        <h:outputText value="Del" escape="false" />
                        <f:param name="questionid" value="<%=question.getQuestionid()%>" />
                    </h:commandLink>
                <%}%>
              </h:column>
            </t:dataTable>
            <t:dataScroller id="scroll_1" for="datatable" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
                <f:facet name="first" >
                    <t:graphicImage url="/images/datascroller/play-first.png" border="0" />
                </f:facet>
                <f:facet name="last">
                    <t:graphicImage url="/images/datascroller/play-forward.png" border="0" />
                </f:facet>
                <f:facet name="previous">
                    <t:graphicImage url="/images/datascroller/play-back.png" border="0" />
                </f:facet>
                <f:facet name="next">
                    <t:graphicImage url="/images/datascroller/play.png" border="0" />
                </f:facet>
            </t:dataScroller>
            <%if (researcherSurveyDetail02.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                <br/>
                <font class="mediumfont" style="color: #cccccc;">Add a Question of Type</font>
                <br/>
                <font class="mediumfont" style="color: #000000;">See tab at upper right for Question Type Samples</font>
                <br/>
                <%//@todo links for each question type%>
                <h:selectOneRadio value="<%=researcherSurveyDetail02.getNewquestioncomponenttype()%>" layout="pageDirection" id="newquestioncomponenttype" styleClass="normalfont" required="true" rendered="#{researcherSurveyDetail02.status eq 1}">
                    <f:selectItems value="<%=((ComponentTypes)Pagez.getBeanMgr().get("ComponentTypes")).getTypesaslinkedhashmap()%>"/>
                </h:selectOneRadio>
                <h:commandButton action="<%=researcherSurveyDetail02.getAddQuestion()%>" value="Add Question" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail02.status eq 1}">
                    <f:param name="isnewquestion" value="1" />
                </h:commandButton>
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