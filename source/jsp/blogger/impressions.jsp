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
    <ui:define name="title">Impressions for: ${bloggerImpressions.surveytitle}<br/><br/></ui:define>
    <ui:param name="navtab" value="bloggers"/>
    <ui:define name="body">
    <d:authorization acl="blogger" redirectonfail="true"/>
    
    
    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    This page lists blog displays/impressions of a survey.  Note that in the calculation of earnings some impressions may not be included because the researcher can set a maximum number of paid blog displays per blog.
    <br/><br/><br/>
    </font></div></center>

        <h:form>
              <t:saveState id="save" value="#{bloggerImpressions}"/>

              <t:dataTable id="datatable" value="#{bloggerImpressions.list}" rows="25" var="listitem" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Url"/>
                </f:facet>
                <h:outputText value="#{listitem.referer}" styleClass="smallfont"/>
              </h:column>
              <h:column rendered="true">
                <f:facet name="header">
                  <h:outputText value="Impressions Qualifying for Payment"/>
                </f:facet>
                <h:outputText value="#{listitem.impressionspaidandtobepaid}" escape="false" styleClass="smallfont" style="color: #0000ff;"/>
              </h:column>
              <h:column rendered="true">
                <f:facet name="header">
                  <h:outputText value="Impressions Total"/>
                </f:facet>
                <h:outputText value="#{listitem.impressionstotal}" escape="false" styleClass="smallfont" style="color: #0000ff;"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Quality Rating"/>
                </f:facet>
                <h:outputText value="#{listitem.quality}"/>
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


