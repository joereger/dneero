<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Error and Debug Log";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <h:selectOneMenu value="#{sysadminErrorList.minleveltoshow}" id="minleveltoshow" required="false">
            <f:selectItems value="#{sysadminErrorList.levels}"/>
        </h:selectOneMenu>

        <h:commandButton action="#{sysadminErrorList.load}"  value="Refresh" styleClass="formsubmitbutton"></h:commandButton>
        <h:commandButton action="#{sysadminErrorList.markallold}"  value="Mark All Old" styleClass="formsubmitbutton"></h:commandButton>
        <h:commandButton action="#{sysadminErrorList.deleteall}"  value="Delete All" styleClass="formsubmitbutton"></h:commandButton>
        <h:commandButton action="#{sysadminErrorList.onlyerrors}"  value="Only Errors" styleClass="formsubmitbutton"></h:commandButton>

        <br/><br/>
        <t:saveState id="save" value="#{sysadminErrorList}"/>
        <t:dataTable id="datatable" value="#{sysadminErrorList.errors}" rows="15" var="error" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column sortProperty="errorid" sortable="true">
            <f:facet name="header">
              <h:outputText value="Id"/>
            </f:facet>
            <h:outputText value="#{error.errorid}" styleClass="tinyfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Date"/>
            </f:facet>
            <h:outputText value="#{error.date}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column sortProperty="status" sortable="true" >
            <f:facet name="header">
              <h:outputText value="Status"/>
            </f:facet>
            <h:outputText value="New" styleClass="tinyfont" rendered="#{error.status eq 1}"/>
            <h:outputText value="Old" styleClass="tinyfont" rendered="#{error.status eq 0}"/>
          </h:column>
          <h:column sortProperty="level" sortable="true">
            <f:facet name="header">
              <h:outputText value="Level"/>
            </f:facet>
            <h:outputText value="Debug" styleClass="tinyfont" rendered="#{error.level eq 10000}"/>
            <h:outputText value="Info" styleClass="tinyfont" rendered="#{error.level eq 20000}"/>
            <h:outputText value="Warn" styleClass="tinyfont" rendered="#{error.level eq 30000}"/>
            <h:outputText value="Error" styleClass="tinyfont" rendered="#{error.level eq 40000}"/>
            <h:outputText value="Fatal" styleClass="tinyfont" rendered="#{error.level eq 50000}"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Error"/>
            </f:facet>
            <h:outputText value="#{error.error}" styleClass="smallfont" escape="false"/>
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



