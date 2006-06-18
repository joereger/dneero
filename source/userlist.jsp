<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://xmlns.oracle.com/adf/faces" prefix="af"%>

<%--
<h:form>
    <af:table value="#{UserList.users}" bandingInterval="2" banding="row" var="user">
      <af:column>
        <f:facet name="header">
          <af:outputText value="Userid"/>
        </f:facet>
        <af:outputText value="#{user.userid}"/>
      </af:column>
      <af:column>
        <f:facet name="header">
          <af:outputText value="Email"/>
        </f:facet>
        <af:outputText value="#{user.email}"/>
      </af:column>
      <af:column>
        <f:facet name="header">
          <af:outputText value="First Name"/>
        </f:facet>
        <af:outputText value="#{user.firstname}"/>
      </af:column>
      <af:column>
        <f:facet name="header">
          <af:outputText value="Last Name"/>
        </f:facet>
        <af:outputText value="#{user.lastname}"/>
      </af:column>
    </af:table>
</h:form>
--%>

<h:form>
    <t:dataTable id="usertable"
                 var="user"
                 value="#{UserList.users}"
                 styleClass="scrollerTable"
                 headerClass="standardTable_Header"
                 footerClass="standardTable_Header"
                 rowClasses="standardTable_Row1,standardTable_Row2"
                 columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                 preserveDataModel="true"
                 sortColumn="#{UserList.sort}"
                 sortAscending="#{UserList.ascending}"
                 rows="10"
                 rowId="#{user.userid}"
                 rendered="#{!empty UserList.users}"
                 preserveSort="true"
                 >
        <t:column style="width:40pc;">
            <f:facet name="header">
                <t:commandSortHeader columnName="userid">
                    <h:outputText value="Userid"/>
                </t:commandSortHeader>
            </f:facet>
            <t:outputText value="#{user.userid}"/>
        </t:column>
        <t:column style="width:200px;">
            <f:facet name="header">
                <t:commandSortHeader columnName="email">
                    <h:outputText value="Email"/>
                </t:commandSortHeader>
            </f:facet>
            <t:outputText value="#{user.email}"/>
        </t:column>
        <t:column>
            <f:facet name="header">
                <t:commandSortHeader columnName="firstname">
                    <h:outputText value="First Name"/>
                </t:commandSortHeader>
            </f:facet>
            <h:outputText value="#{user.firstname}"/>
        </t:column>
    </t:dataTable>

    <h:panelGrid columns="1" styleClass="scrollerTable2" columnClasses="standardTable_ColumnCentered" >
        <t:dataScroller id="scroll_1"
                        for="usertable"
                        fastStep="10"
                        pageCountVar="pageCount"
                        pageIndexVar="pageIndex"
                        styleClass="scroller"
                        paginator="true"
                        paginatorMaxPages="9"
                        paginatorTableClass="paginator"
                        paginatorActiveColumnStyle="font-weight:bold;">
            <f:facet name="first" >
                <t:graphicImage url="images/myfaces-examples/arrow-first.gif" border="1" />
            </f:facet>
            <f:facet name="last">
                <t:graphicImage url="images/myfaces-examples/arrow-last.gif" border="1" />
            </f:facet>
            <f:facet name="previous">
                <t:graphicImage url="images/myfaces-examples/arrow-previous.gif" border="1" />
            </f:facet>
            <f:facet name="next">
                <t:graphicImage url="images/myfaces-examples/arrow-next.gif" border="1" />
            </f:facet>
            <f:facet name="fastforward">
                <t:graphicImage url="images/myfaces-examples/arrow-ff.gif" border="1" />
            </f:facet>
            <f:facet name="fastrewind">
                <t:graphicImage url="images/myfaces-examples/arrow-fr.gif" border="1" />
            </f:facet>
        </t:dataScroller>

    </h:panelGrid>

</h:form>




