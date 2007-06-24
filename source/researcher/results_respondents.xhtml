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
    <ui:define name="title">Survey Results<br/><br/></ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

<h:form>

    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont">#{researcherResultsRespondents.survey.title}</font>
        <br/>
        <h:commandLink value="Results Main" action="#{researcherResults.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Response Report" action="#{researcherResultsAnswers.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Impressions" action="#{researcherResultsImpressions.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Respondents" action="#{researcherResultsRespondents.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Financial Status" action="#{researcherResultsFinancial.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
    </div>
    <br/><br/>

    <t:saveState id="save" value="#{researcherResultsRespondents}"/>

    <t:dataTable id="datatable" value="#{researcherResultsRespondents.list}" rows="50" var="listitem" rendered="#{!empty researcherResultsRespondents.list}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Date"/>
            </f:facet>
            <h:outputText value="#{listitem.responsedate}" styleClass="smallfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Blogger Name"/>
            </f:facet>
            <h:outputText value="#{listitem.firstname} #{listitem.lastname}" escape="false" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{publicProfile.beginView}">
                <h:outputText value="Blogger's Profile" escape="false" styleClass="smallfont"/>
                <f:param name="bloggerid" value="#{listitem.bloggerid}" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{publicProfileAnswers.beginView}">
                <h:outputText value="Blogger's Answers" escape="false" styleClass="smallfont"/>
                <f:param name="bloggerid" value="#{listitem.bloggerid}" />
                <f:param name="surveyid" value="#{researcherResultsRespondents.survey.surveyid}" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{publicProfileImpressions.beginView}">
                <h:outputText value="Blogger's Impressions" escape="false" styleClass="smallfont"/>
                <f:param name="responseid" value="#{listitem.responseid}" />
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


    
</h:form>

</ui:define>


</ui:composition>
</html>