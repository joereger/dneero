<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "My Surveys and Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>


        <t:dataTable id="datatable" value="#{researcherSurveyList.surveys}" rows="50" var="survey" rendered="#{!empty researcherSurveyList.surveys}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Title"/>
            </f:facet>
            <h:outputText value="#{survey.title}" styleClass="normalfont" style="font-weight: bold;"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Status"/>
            </f:facet>
            <h:outputText value="Draft" styleClass="smallfont" rendered="#{survey.status==1}"/>
            <h:outputText value="Pending, Waiting for Start Date" styleClass="smallfont" rendered="#{survey.status==2}"/>
            <h:outputText value="Pending, Waiting for Funds" styleClass="smallfont" rendered="#{survey.status==3}"/>
            <h:outputText value="Live" styleClass="smallfont" rendered="#{survey.status==4}"/>
            <h:outputText value="Closed" styleClass="smallfont" rendered="#{survey.status==5}"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{researcherSurveyDetail01.beginView}" rendered="#{survey.status eq 1}">
                <h:outputText value="Edit" styleClass="smallfont" escape="false" />
                <f:param name="surveyid" value="#{survey.surveyid}" />
            </h:commandLink>
            <h:commandLink action="#{researcherSurveyDetail01.beginView}" rendered="#{survey.status ne 1}">
                <h:outputText value="Review" styleClass="smallfont" escape="false" />
                <f:param name="surveyid" value="#{survey.surveyid}" />
            </h:commandLink>
          </h:column>
          <h:column rendered="#{survey.status eq 4}">
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{researcherEmailinvite.beginView}" rendered="#{survey.status eq 4}">
                <h:outputText value="Invite" styleClass="smallfont" escape="false" />
                <f:param name="surveyid" value="#{survey.surveyid}" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{researcherResults.beginView}" rendered="#{survey.status ne 1}">
                <h:outputText value="Results" styleClass="smallfont" escape="false" />
                <f:param name="surveyid" value="#{survey.surveyid}" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{researcherSurveyDelete.beginView}" rendered="#{survey.status eq 1}">
                <h:outputText value=" Delete" styleClass="smallfont" escape="false"/>
                <f:param name="surveyid" value="#{survey.surveyid}" />
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
        <br/><br/>
        <h:commandButton action="#{researcherSurveyDetail01.beginViewNewSurvey}" value="Create a New Survey" styleClass="formsubmitbutton"></h:commandButton>

    </h:form>

</ui:define>


</ui:composition>
</html>



