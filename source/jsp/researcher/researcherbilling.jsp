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
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getFirstname()%>" id="firstname"  size="15"></h:inputText>
                                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getLastname()%>" id="lastname" size="15"></h:inputText>
                                    <h:message for="firstname" styleClass="RED"></h:message>
                                    <h:message for="lastname" styleClass="RED"></h:message>
                               </td>
                            </tr>

                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="Street Address" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getStreet()%>" id="street" size="30"></h:inputText>
                                    <h:message for="street" styleClass="RED"></h:message>
                               </td>
                            </tr>


                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="City, State, Zip" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCccity()%>" id="cccity" size="20"></h:inputText>
                                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCcstate()%>" id="ccstate"  size="2"></h:inputText>
                                    <h:outputText value=" " styleClass="formfieldnamefont"></h:outputText>
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getPostalcode()%>" id="postalcode" size="6"></h:inputText>
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
                                    <h:selectOneMenu value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCctype()%>" id="cctype" required="true">
                                        <f:selectItems value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCreditcardtypes()%>"/>
                                    </h:selectOneMenu>
                                    <h:message for="cctype" styleClass="RED"></h:message>
                               </td>
                            </tr>

                            <tr>
                               <td valign="top" align="left">
                                    <h:outputText value="Credit Card Number" styleClass="formfieldnamefont"></h:outputText>
                               </td>
                               <td valign="top" align="left">
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCcnum()%>" id="ccnum"  size="18">
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
                                    <h:selectOneMenu value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCcexpmo()%>" id="ccexpmo" required="true">
                                        <f:selectItems value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getMonthsForCreditcard()%>"/>
                                    </h:selectOneMenu>
                                    /
                                    <h:selectOneMenu value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCcexpyear()%>" id="ccexpyear" required="true">
                                        <f:selectItems value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getYearsForCreditcard()%>"/>
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
                                    <h:inputText value="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getCvv2()%>" id="cvv2" size="3"></h:inputText>
                                    <h:message for="cvv2" styleClass="RED"></h:message>
                               </td>
                            </tr>

                            </table>


                   </td>
               </tr>
           </table>












            <table cellpadding="0" cellspacing="0" border="0">
                <td valign="top">
                    <h:commandButton action="<%=((ResearcherBilling)Pagez.getBeanMgr().get("ResearcherBilling")).getSaveAction()%>" value="Save Billing Details" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>
                <td valign="top">
                </td>
            </table>




<%@ include file="/jsp/templates/footer.jsp" %>


