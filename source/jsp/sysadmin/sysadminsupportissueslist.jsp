<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issues";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <table cellpadding="0" cellspacing="0" border="0">
            <tr>

                <td valign="top">
                    <font class="tinyfont">Show All?</font>
                </td>
                <td valign="top">

                </td>
            </tr>
            <tr>
                <td valign="top">
                    <h:selectBooleanCheckbox value="#{sysadminSupportIssuesList.showall}" id="showall"></h:selectBooleanCheckbox>
                </td>
                <td valign="top">
                    <h:commandButton action="#{sysadminSupportIssuesList.search}"  value="Search" styleClass="formsubmitbutton"></h:commandButton>
                </td>
            </tr>
        </table>

        <br/>


    <t:saveState id="save" value="#{sysadminSupportIssuesList}"/>

    <t:dataTable id="datatable" value="#{sysadminSupportIssuesList.supportissues}" rows="25" var="supportissue" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Id"/>
        </f:facet>
        <h:outputText value="#{supportissue.supportissueid}"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Date"/>
        </f:facet>
        <h:outputText value="#{supportissue.datetime}"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="-" style="color: #ffffff;"/>
        </f:facet>
        <h:commandLink action="#{sysadminSupportIssueDetail.beginView}">
            <h:outputText value="#{supportissue.subject}" escape="false" />
            <f:param name="supportissueid" value="#{supportissue.supportissueid}" />
        </h:commandLink>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Status"/>
        </f:facet>
        <h:outputText value="Open" rendered="#{supportissue.status==0}"/>
        <h:outputText value="Working" rendered="#{supportissue.status==1}"/>
        <h:outputText value="Closed" rendered="#{supportissue.status==2}"/>
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



