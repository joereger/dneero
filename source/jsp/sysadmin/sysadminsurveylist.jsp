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
    <ui:define name="title">Surveys<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
    <d:authorization acl="systemadmin" redirectonfail="true"/>



<h:form>


    <!--<t:saveState id="save" value="#{sysadminSurveyList}"/>-->

    <t:dataTable id="datatable" value="#{sysadminSurveyList.surveys}" rows="100" var="survey" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcol,tcol,tcol,tcol">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Surveyid"/>
        </f:facet>
        <h:outputText value="#{survey.surveyid}"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Title"/>
        </f:facet>
        <a href='/sysadmin/sysadminsurveydetail.jsf?surveyid=#{survey.surveyid}'>#{survey.title}</a>
        <!--
        <h:commandLink action="#{sysadminSurveyDetail.beginView}">
            <h:outputText value="#{survey.title}" styleClass="mediumfont" style="color: #0000ff;"/>
            <f:param name="surveyid" value="#{survey.surveyid}" />
        </h:commandLink>
        -->
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Displays"/>
        </f:facet>
        <h:outputText value="#{survey.publicsurveydisplays}"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Spotlight?"/>
        </f:facet>
        <h:outputText value="Yes" styleClass="smallfont" rendered="#{survey.isspotlight}"/>
        <h:outputText value="No" styleClass="smallfont" rendered="#{!survey.isspotlight}"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Status"/>
        </f:facet>
        <h:outputText value="Draft" styleClass="smallfont" rendered="#{survey.status==1}"/>
        <h:outputText value="Pending, Waiting for Start Date" styleClass="smallfont" rendered="#{survey.status==2}"/>
        <h:outputText value="Pending, Waiting for Funds" styleClass="smallfont" rendered="#{survey.status==3}"/>
        <h:outputText value="Open" styleClass="smallfont" rendered="#{survey.status==4}"/>
        <h:outputText value="Closed" styleClass="smallfont" rendered="#{survey.status==5}"/>
      </h:column>
    </t:dataTable>
    <!--
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
    -->

</h:form>

</ui:define>


</ui:composition>
</html>



