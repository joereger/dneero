<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">
        <img src="/images/process-train-survey-02.gif" align="right" width="350" height="73" alt=""></img>
        <h:outputText value="${researcherSurveyDetail02.title}" styleClass="pagetitlefont" rendered="${researcherSurveyDetail02.title ne ''}"/>
        <br clear="all"/>
    </ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form>

    <t:saveState id="save" value="#{researcherSurveyDetail02}"/>
    <t:saveState id="save" value="#{researcherSurveyQuestionList}"/>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    Add questions to your survey on this page.  Choose from the Question Type dropdown box and then click Add Question.  You'll quickly build a list of questions.  Click Preview the Survey to see what these questions look like to somebody taking the survey.  When you're done, click Next Step.
    <br/><br/>
    </font></div></center>

    <f:verbatim><br/></f:verbatim>
    <h:messages styleClass="RED"></h:messages>
    <f:verbatim><br/></f:verbatim>


    <t:panelTabbedPane id="panel" bgcolor="#ffffff">
        <t:panelTab id="panel_a" label="Questions">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>

            <t:dataTable id="datatable" value="#{researcherSurveyQuestionList.questions}" rows="10" var="question" rendered="#{!empty researcherSurveyQuestionList.questions}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcol,tcol,tcolnowrap,tcolnowrap">
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
                <h:outputText value="#{question.question}"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Respondent Must Answer?"/>
                </f:facet>
                <h:outputText value="Yes" rendered="#{question.isrequired}"/>
                <h:outputText value="No" rendered="#{!question.isrequired}"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <h:commandLink action="#{researcherSurveyDetail02.beginEdit}" rendered="#{researcherSurveyDetail02.status eq 1}">
                    <h:outputText value="Edit" escape="false" />
                    <f:param name="questionid" value="#{question.questionid}" />
                    <f:param name="componenttype" value="#{question.componenttype}" />
                </h:commandLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <h:commandLink action="#{researcherSurveyDetail02.deleteQuestion}" rendered="#{researcherSurveyDetail02.status eq 1}">
                    <h:outputText value="Del" escape="false" />
                    <f:param name="questionid" value="#{question.questionid}" />
                </h:commandLink>
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
            <br/>
            <h:outputText value="Add a Question of Type:" styleClass="mediumfont" style="color: #cccccc;" rendered="#{researcherSurveyDetail02.status eq 1}"/>
            <br/>
            <h:outputText value="See tab at upper right for Question Type Samples" styleClass="smallfont" style="color: #000000;" rendered="#{researcherSurveyDetail02.status eq 1}"/>
            <br/>
            <h:selectOneRadio value="#{researcherSurveyDetail02.newquestioncomponenttype}" layout="pageDirection" id="newquestioncomponenttype" styleClass="normalfont" required="true" rendered="#{researcherSurveyDetail02.status eq 1}">
                <f:selectItems value="#{componentTypes.typesaslinkedhashmap}"/>
            </h:selectOneRadio>
            <h:commandButton action="#{researcherSurveyDetail02.addQuestion}" value="Add Question" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail02.status eq 1}">
                <f:param name="isnewquestion" value="1" />
            </h:commandButton>
        </t:panelTab>
        <t:panelTab id="panel_b" label="Preview your Survey">                                                                     
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim>#{researcherSurveyDetail02.surveyForTakers}</f:verbatim>
        </t:panelTab>
        <t:panelTab id="panel_c" label="Question Type Samples">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
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
        </t:panelTab>
    </t:panelTabbedPane>


    <f:verbatim><br/><br/></f:verbatim>
    <div class="surveyeditbuttonbox"><div class="surveyeditpreviousbutton"><h:commandButton action="#{researcherSurveyDetail02.previousStep}" value="Previous Step" styleClass="formsubmitbutton"/></div><div class="surveyeditnextbutton"><h:commandButton action="#{researcherSurveyDetail02.saveSurveyAsDraft}" value="Save and Continue Later" styleClass="formsubmitbutton" rendered="#{researcherSurveyDetail02.status eq 1}"/><h:commandButton action="#{researcherSurveyDetail02.saveSurvey}" value="Next Step" styleClass="formsubmitbutton"/></div></div>


</h:form>

</ui:define>


</ui:composition>
</html>