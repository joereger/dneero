<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib">

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Panels</ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="researcher" redirectonfail="true"/>

    <t:div rendered="#{researcherPanels.msg ne '' and researcherPanels.msg ne null}">
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        #{researcherPanels.msg}
        <br/><br/></font></div></center>
        <br/><br/>
    </t:div>

    <h:form id="panels">

        <t:saveState id="save" value="#{researcherPanels}"/>
        
        <br/><br/>

        <t:dataTable id="datatable" value="#{researcherPanels.listitems}" var="listitem" rendered="#{!empty researcherPanels.listitems}" rows="10" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Panel Name"/>
            </f:facet>
            <h:outputText value="#{listitem.panel.name}" styleClass="normalfont" style="font-weight: bold;"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Create Date"/>
            </f:facet>
            <h:outputText value="#{listitem.panel.createdate}" styleClass="smallfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Members"/>
            </f:facet>
            <h:outputText value="#{listitem.numberofmembers}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{researcherPanelsListBloggers.beginView}">
                <h:outputText value="View Members" styleClass="smallfont" escape="false" />
                <f:param name="panelid" value="#{listitem.panel.panelid}" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{researcherPanelsEdit.beginView}">
                <h:outputText value="Edit" styleClass="smallfont" escape="false" />
                <f:param name="panelid" value="#{listitem.panel.panelid}" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{researcherPanels.deletePanel}">
                <h:outputText value="Delete" styleClass="smallfont" escape="false" />
                <f:param name="panelid" value="#{listitem.panel.panelid}" />
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
        <h:inputText value="#{researcherPanels.newpanelname}" id="newpanelname"></h:inputText>
        <h:commandButton action="#{researcherPanels.createNewPanel}" value="Create a New Panel" styleClass="formsubmitbutton"></h:commandButton>




    </h:form>

</ui:define>


</ui:composition>
</html>

