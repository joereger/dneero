<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Researcher Billing Details";
String navtab = "researchers";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>



           <table cellpadding="0" cellspacing="0" border="0">
               <tr>

                   <td valign="top" align="left">
                        <font class="mediumfont" style="color: #cccccc;">Credit Card Info</font>
                            <br/>

                            <table cellpadding="3" cellspacing="0" border="0">

                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
                                    <br/>
                                    <h:outputText value="(first then last)" styleClass="tinyfont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="#{researcherBilling.firstname}" id="firstname"  size="15"></h:inputText>
                                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                                    <h:inputText value="#{researcherBilling.lastname}" id="lastname" size="15"></h:inputText>
                                    <h:message for="firstname" styleClass="RED"></h:message>
                                    <h:message for="lastname" styleClass="RED"></h:message>
                               </td>
                            </tr>

                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="Street Address" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="#{researcherBilling.street}" id="street" size="30"></h:inputText>
                                    <h:message for="street" styleClass="RED"></h:message>
                               </td>
                            </tr>


                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="City, State, Zip" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="#{researcherBilling.cccity}" id="cccity" size="20"></h:inputText>
                                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                                    <h:inputText value="#{researcherBilling.ccstate}" id="ccstate"  size="2"></h:inputText>
                                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                                    <h:inputText value="#{researcherBilling.postalcode}" id="postalcode" size="6"></h:inputText>
                                    <h:message for="cccity" styleClass="RED"></h:message>
                                    <h:message for="ccstate" styleClass="RED"></h:message>
                                    <h:message for="postalcode" styleClass="RED"></h:message>
                               </td>
                            </tr>


                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="Credit Card Type" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:selectOneMenu value="#{researcherBilling.cctype}" id="cctype" required="true">
                                        <f:selectItems value="#{researcherBilling.creditcardtypes}"/>
                                    </h:selectOneMenu>
                                    <h:message for="cctype" styleClass="RED"></h:message>
                               </td>
                            </tr>

                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="Credit Card Number" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="#{researcherBilling.ccnum}" id="ccnum"  size="18">
                                        <t:validateCreditCard />
                                    </h:inputText>
                                    <h:message for="ccnum" styleClass="RED"></h:message>
                               </td>
                            </tr>


                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="Expiration Date" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:selectOneMenu value="#{researcherBilling.ccexpmo}" id="ccexpmo" required="true">
                                        <f:selectItems value="#{researcherBilling.monthsForCreditcard}"/>
                                    </h:selectOneMenu>
                                    /
                                    <h:selectOneMenu value="#{researcherBilling.ccexpyear}" id="ccexpyear" required="true">
                                        <f:selectItems value="#{researcherBilling.yearsForCreditcard}"/>
                                    </h:selectOneMenu>
                                    <h:message for="ccexpmo" styleClass="RED"></h:message>
                                    <h:message for="ccexpyear" styleClass="RED"></h:message>
                               </td>
                            </tr>



                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="CVV2" styleClass="formfieldnamefont"></h:outputText>
                                    <br/>
                                    <h:outputText value="(three digit number on back of card)" styleClass="tinyfont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="#{researcherBilling.cvv2}" id="cvv2" size="3"></h:inputText>
                                    <h:message for="cvv2" styleClass="RED"></h:message>
                               </td>
                            </tr>

                            </table>


                   </td>
               </tr>
           </table>












            <h:panelGrid columns="3" cellpadding="3" border="0">
                <h:panelGroup>
                    <h:commandButton action="#{researcherBilling.saveAction}" value="Save Billing Details" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>
            </h:panelGrid>




<%@ include file="/jsp/templates/footer.jsp" %>


