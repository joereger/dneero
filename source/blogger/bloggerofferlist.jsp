<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="t"%>


<f:view>

        <h:form>
            <t:dataTable id="usertable"
                         var="offer"
                         value="#{BloggerOfferList.offers}"
                         styleClass="scrollerTable"
                         headerClass="standardTable_Header"
                         footerClass="standardTable_Header"
                         rowClasses="standardTable_Row1,standardTable_Row2"
                         columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                         preserveDataModel="true"
                         sortColumn="#{BloggerOfferList.sort}"
                         sortAscending="#{BloggerOfferList.ascending}"
                         rows="100"
                         rowId="#{offer.offerid}"
                         rendered="#{!empty BloggerOfferList.offers}"
                         preserveSort="true"
                         >
                <t:column width="40px;">
                    <f:facet name="header">
                         <h:outputText value="Offerid"/>
                    </f:facet>
                    <h:outputText value="#{offer.offerid}"/>
                    <h:inputHidden value="#{offer.offerid}"/>
                </t:column>
                <t:column style="width:200px;">
                    <f:facet name="header">
                        <t:commandSortHeader columnName="title">
                            <h:outputText value="Title"/>
                        </t:commandSortHeader>
                    </f:facet>
                    <t:outputText value="#{offer.title}"/>
                </t:column>
                <t:column>
                    <f:facet name="header">
                        <t:commandSortHeader columnName="description">
                            <h:outputText value="Description"/>
                        </t:commandSortHeader>
                    </f:facet>
                    <h:outputText value="#{offer.description}"/>
                </t:column>

                <t:column>
                    <f:facet name="header">
                        <t:commandSortHeader columnName="willingtopayperrespondent">
                            <h:outputText value="Willing to Pay"/>
                        </t:commandSortHeader>
                    </f:facet>
                    <h:outputText value="#{offer.willingtopayperrespondent}"/>
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


        </h:form>

</f:view>



