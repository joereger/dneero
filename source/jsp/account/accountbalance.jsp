<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"
      xmlns:c="http://java.sun.com/jstl/core"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Account Balance: #{accountBalance.currentbalance}<br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>


    <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
        <h:outputText value="(We owe you money.  Once a day, if you have a balance of over $20 and don't have any live surveys that you've launched yourself then we'll send money to the PayPal address in your account settings.)" styleClass="smallfont" style="color: #000000;" rendered="#{accountBalance.currentbalanceDbl gt 0}"/>
        <h:outputText value="(You owe us money.)" styleClass="smallfont" style="color: #000000;" rendered="#{accountBalance.currentbalanceDbl lt 0}"/>
    </div>
    <br/><br/>
    
    <h:form>
        <t:saveState id="save" value="#{accountBalance}"/>

        <h:outputText value="There are not yet any financial transactions on your account.  Go fill out some surveys!  Or create some!" rendered="#{empty accountBalance.balances}"/>

        <t:dataTable id="datatable" value="#{accountBalance.balances}" rows="25" var="balance" rendered="#{!empty accountBalance.balances}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Id"/>
            </f:facet>
            <h:outputText value="#{balance.balanceid}" styleClass="tinyfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Date"/>
            </f:facet>
            <h:outputText value="#{balance.date}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Description"/>
            </f:facet>
            <h:outputText value="#{balance.description}" styleClass="smallfont"/>

          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Amount"/>
            </f:facet>
            <h:outputText value="#{balance.amt}" styleClass="tinyfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Balance"/>
            </f:facet>
            <h:outputText value="#{balance.currentbalance}" styleClass="tinyfont" style="font-weight: bold;"/>
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
        <h:commandLink value="View Transfer Details" action="#{accountTransactions.beginView}" style="padding-left: 25px;" styleClass="smallfont" rendered="#{!empty accountBalancetransaction.balances}"/>


    </h:form>

</ui:define>


</ui:composition>
</html>


