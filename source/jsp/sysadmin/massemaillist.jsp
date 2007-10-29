<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Mass Emails";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:messages styleClass="RED"/>

    <t:saveState id="save" value="#{sysadminMassemailList}"/>

    <t:dataTable id="datatable" value="#{sysadminMassemailList.massemails}" rows="15" var="massemail" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcolnowrap,tcolnowrap,tcol,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Massemailid"/>
        </f:facet>
        <h:outputText value="#{massemail.massemailid}" styleClass="tinyfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Date"/>
        </f:facet>
          <h:outputText value="#{massemail.date}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Processed Userid"/>
        </f:facet>
          <h:outputText value="#{massemail.lastuseridprocessed}" styleClass="tinyfont"></h:outputText>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Subject"/>
        </f:facet>
        <h:commandLink action="#{sysadminMassemailDetail.beginView}">
            <h:outputText value="#{massemail.subject}" styleClass="tinyfont" style="color: #0000ff;"/>
            <f:param name="massemailid" value="#{massemail.massemailid}" />
        </h:commandLink>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Status"/>
        </f:facet>
        <h:outputText value="New" styleClass="smallfont" rendered="#{massemail.status==0}"/>
        <h:outputText value="Processing" styleClass="smallfont" rendered="#{massemail.status==1}"/>
        <h:outputText value="Complete" styleClass="smallfont" rendered="#{massemail.status==2}"/>
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


    <h:commandButton action="#{sysadminMassemailDetail.beginView}" value="New Mass Email" styleClass="formsubmitbutton"></h:commandButton>




<%@ include file="/jsp/templates/footer.jsp" %>



