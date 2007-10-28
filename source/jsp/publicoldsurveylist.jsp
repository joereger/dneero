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
    <ui:define name="title">Old Surveys<br/><br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>



<h:form>


    <t:saveState id="save" value="#{publicOldSurveyList}"/>
    <h:outputText value="There are currently no surveys listed." styleClass="mediumfont" rendered="#{empty publicOldSurveyList.surveys}"/>

    <t:dataTable sortable="true" id="datatable" value="#{publicOldSurveyList.surveys}" rows="15" var="srvy" rendered="#{!empty publicOldSurveyList.surveys}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <t:column>
        <f:facet name="header">
            <h:outputText value="Survey Name"/>
        </f:facet>
        <h:outputLink value="/surveyresults.jsf?surveyid=#{srvy.surveyid}">
            <h:outputText value="#{srvy.title}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"/>
        </h:outputLink>
        <br/>
        <h:outputText value="#{srvy.description}" escape="false" styleClass="tinyfont"/>
      </t:column>

      <t:column>
        <f:facet name="header">
          <h:outputText value="Respondents Earned Up To"/>
        </f:facet>
        <h:outputText value="#{srvy.maxearning}" styleClass="smallfont" style="font-weight:normal;"/>
      </t:column>
      <t:column>
        <f:facet name="header">
          <h:outputText value="Number of Respondents"/>
        </f:facet>
        <h:outputText value="#{srvy.numberofrespondents}" styleClass="smallfont" style="font-weight:normal;"/>
      </t:column>
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



