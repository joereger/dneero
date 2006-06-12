<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="t"%>


<f:view>

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
                         rows="100"
                         rowId="#{user.userid}"
                         rendered="#{!empty UserList.users}"
                         preserveSort="true"
                         >
                <t:column width="40px;">
                    <f:facet name="header">
                         <h:outputText value="Userid"/>
                    </f:facet>
                    <h:outputText value="#{user.userid}"/>
                    <h:inputHidden value="#{user.userid}"/>
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

                <t:column>
                    <%-- Below is shown the use of the t:updateActionListener tag, which
                    sets the employee.id of our backing bean (employeeAction) and we can then use that
                    employee.id in that Action. I find this much cleaner than using f:param and having
                    to pull request params from the FacesContext. --%>
                    <%--
                    <t:commandLink action="#{UserList.prepareForEdit}">
                        <h:outputText value="Edit" />
                        <t:updateActionListener property="#{UserList.user.id}" value="#{user.id}" />
                    </t:commandLink>
                    --%>
                </t:column>
            </t:dataTable>

            <%--
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

            </h:panelGrid> --%>





        </h:form>

</f:view>



