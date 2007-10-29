<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Transactionn";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:outputText value="There are not yet any financial transactions on your account." rendered="#{empty accountBalancetransaction.balances}"/>

        <t:dataTable id="datatable" value="#{accountBalancetransaction.balances}" rows="25" var="balance" rendered="#{!empty accountBalancetransaction.balances}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
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
              <h:outputText value="Success?"/>
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

<%@ include file="/jsp/templates/footer.jsp" %>

