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
    <ui:define name="title">Transactions<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
    <d:authorization acl="systemadmin" redirectonfail="true"/>
    <h:messages styleClass="RED"/>

        <font class="mediumfont">Real World Money Movement</font>
        <h:form>
            <t:saveState id="save" value="#{sysadminTransactions}"/>
            <h:outputText value="There are not yet any financial transactions on this account." rendered="#{empty sysadminTransactions.transactions}"/>
            <t:dataScroller for="datatable2" maxPages="5"/>
            <t:dataTable id="datatable2" value="#{sysadminTransactions.transactions}" rows="50" var="balance" rendered="#{!empty sysadminTransactions.transactions}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcolnowrap,tcolnowrap,tcolnowrap,tcol,tcol,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Id"/>
                </f:facet>
                <h:outputText value="#{balance.balancetransactionid}" styleClass="tinyfont"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Date"/>
                </f:facet>
                <h:outputText value="#{balance.date}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Userid"/>
                </f:facet>
                <h:commandLink action="#{sysadminUserDetail.beginView}">
                    <h:outputText value="#{balance.userid}" styleClass="tinyfont" style="color: #0000ff;"/>
                    <f:param name="userid" value="#{balance.userid}" />
                </h:commandLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value=""/>
                </f:facet>
                <h:outputText value="Success" rendered="#{balance.issuccessful}" styleClass="tinyfont"/>
                <h:outputText value="Fail" rendered="#{!balance.issuccessful}" styleClass="tinyfont"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Description"/>
                </f:facet>
                <h:outputText value="#{balance.description}"  styleClass="tinyfont"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Notes"/>
                </f:facet>
                <h:outputText value="#{balance.notes}" styleClass="tinyfont"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Amount"/>
                </f:facet>
                <h:outputText value="#{balance.amt}" styleClass="tinyfont"/>
              </h:column>
            </t:dataTable>
            <t:dataScroller id="scroll_2" for="datatable2" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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



