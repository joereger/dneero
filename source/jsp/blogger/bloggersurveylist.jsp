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
    <ui:define name="title">Survey Opportunities<br/><br/></ui:define>
    <ui:param name="navtab" value="bloggers"/>
    <ui:define name="body">
    <d:authorization acl="blogger" redirectonfail="true"/>



<h:form>
   <t:saveState id="save" value="#{bloggerSurveyList}"/>

    <h:outputText value="There are currently no surveys that fit your profile.  Please check back soon as we're always adding new surveys!" styleClass="mediumfont" rendered="#{empty bloggerSurveyList.surveys}"/>

    <t:dataTable id="datatable" value="#{bloggerSurveyList.surveys}" rows="25" var="srvy" rendered="#{!empty bloggerSurveyList.surveys}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Title"/>
        </f:facet>
        <h:commandLink action="#{publicSurveyTakeRedirector.beginView}">
            <h:outputText value="#{srvy.title}" escape="false" styleClass="normalfont" style="color: #0000ff;"/>
            <f:param name="surveyid" value="#{srvy.surveyid}" />
        </h:commandLink>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Questions"/>
        </f:facet>
        <h:outputText value="#{srvy.numberofquestions}" styleClass="smallfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Timing"/>
        </f:facet>
        <h:outputText value="#{srvy.daysuntilend}" styleClass="smallfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Earn Up To"/>
        </f:facet>
        <h:outputText value="#{srvy.maxearning}" styleClass="smallfont"/>
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


</h:form>

</ui:define>


</ui:composition>
</html>



