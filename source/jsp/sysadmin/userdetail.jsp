<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "User: #{sysadminUserDetail.email}";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top" width="50%">
                    <h:form>

                        <h:inputHidden value="#{sysadminUserDetail.userid}" />

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont">First Name</font>
                                    </td>
                                    <td valign="top">
                                        <h:inputText value="#{sysadminUserDetail.firstname}" id="firstname" size="30" required="true"></h:inputText>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont">Last Name</font>
                                    </td>
                                    <td valign="top">
                                        <h:inputText value="#{sysadminUserDetail.lastname}" id="lastname" size="30" required="true"></h:inputText>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont">Email</font>
                                    </td>
                                    <td valign="top">
                                        <h:inputText value="#{sysadminUserDetail.email}" id="email" size="30" required="true"></h:inputText>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont">PayPal Address</font>
                                    </td>
                                    <td valign="top">
                                        <h:inputText value="#{sysadminUserDetail.paypaladdress}" id="paypaladdress" size="30" required="false"></h:inputText>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont">Referredbyuserid</font>
                                    </td>
                                    <td valign="top">
                                        <h:inputText value="#{sysadminUserDetail.referredbyuserid}" id="referredbyuserid" size="30" required="false"></h:inputText>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont">Facebook uid</font>
                                    </td>
                                    <td valign="top">
                                        <h:outputText value="#{sysadminUserDetail.user.facebookuserid}" styleClass="tinyfont"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">

                                    </td>
                                    <td valign="top">
                                        <h:commandButton action="#{sysadminUserDetail.save}"  value="Save User Details" styleClass="formsubmitbutton"></h:commandButton>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <h:commandButton action="#{sysadminUserDetail.sendresetpasswordemail}"  value="Send Password Reset Email" styleClass="formsubmitbutton"></h:commandButton>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <h:commandButton action="#{sysadminUserDetail.reactivatebyemail}"  value="Force Re-Activation By Email" styleClass="formsubmitbutton"></h:commandButton>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <h:commandButton action="#{sysadminUserDetail.runResearcherRemainingBalanceOperations}" value="ResearcherRemainingBalanceOperations" styleClass="formsubmitbutton"></h:commandButton>
                            <br/>
                            <font class="tinyfont">This will process account balances, remaining impressions, credit card transfers, etc for only this account.  Only does something if this user has a researcher record.</font>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <h:outputText value="User is a Sysadmin" rendered="#{sysadminUserDetail.issysadmin}" styleClass="mediumfont"></h:outputText>
                            <h:outputText value="User is not a Sysadmin" rendered="#{!sysadminUserDetail.issysadmin}" styleClass="mediumfont"></h:outputText>
                            <br/>
                            <h:commandButton action="#{sysadminUserDetail.togglesysadminprivs}"  value="Toggle Sysadmin Privileges" styleClass="formsubmitbutton"></h:commandButton>
                            <h:inputText value="#{sysadminUserDetail.activitypin}" id="activitypin" size="10"></h:inputText>
                            <br/>
                            <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                        </div>
                    </h:form>
                    </td>
                 
                    <td valign="top" width="50%">
                    <h:form id="activation">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <h:outputText value="This Account is Currently Enabled." rendered="#{sysadminUserDetail.isenabled}" styleClass="mediumfont"></h:outputText>
                            <h:outputText value="This Account is Currently Disabled." rendered="#{!sysadminUserDetail.isenabled}" styleClass="mediumfont"></h:outputText>
                            <br/>
                            <h:commandButton action="#{sysadminUserDetail.toggleisenabled}" value="Disable Account" styleClass="formsubmitbutton" rendered="#{sysadminUserDetail.isenabled}"></h:commandButton>
                            <h:commandButton action="#{sysadminUserDetail.toggleisenabled}" value="Enable Account" styleClass="formsubmitbutton" rendered="#{!sysadminUserDetail.isenabled}"></h:commandButton>
                        </div>
                    </h:form>
                    <h:form id="giveusermoney">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                                <h:inputHidden value="#{sysadminUserDetail.userid}" />
                                <font class="mediumfont">Give User Money</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to give:</font>
                                <br/>
                                <h:inputText value="#{sysadminUserDetail.amt}" id="amt" size="30" required="true"><f:validateDoubleRange minimum=".01" maximum="100000"></f:validateDoubleRange></h:inputText>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <br/>
                                <font class="tinyfont">(user will see reason)</font>
                                <br/>
                                <h:inputText value="#{sysadminUserDetail.reason}" id="reason" size="30" required="true"></h:inputText>
                                <br/>
                                <h:commandButton action="#{sysadminUserDetail.giveusermoney}"  value="Give User Money" styleClass="formsubmitbutton"></h:commandButton>
                        </div>
                    </h:form>
                    <h:form id="takeawayusermoney">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                                <h:inputHidden value="#{sysadminUserDetail.userid}" />
                                <font class="mediumfont">Take Money from User</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to take:</font>
                                <br/>
                                <h:inputText value="#{sysadminUserDetail.amt}" id="amt" size="30" required="true"><f:validateDoubleRange minimum=".01" maximum="100000"></f:validateDoubleRange></h:inputText>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <br/>
                                <font class="tinyfont">(user will see reason)</font>
                                <br/>
                                <h:inputText value="#{sysadminUserDetail.reason}" id="reason" size="30" required="true"></h:inputText>
                                <br/>
                                <h:commandButton action="#{sysadminUserDetail.takeusermoney}"  value="Take User Money" styleClass="formsubmitbutton"></h:commandButton>
                        </div>
                    </h:form>
                    </td>
                </tr>
            </table>

        <c:if test="#{!empty sysadminUserDetail.researcher}">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                <font class="mediumfont">Researcher Most Recent Financial Stats (Not Accurate)</font>
                <br/>
                <font class="tinyfont">(Calculated in ResearcherRemainingBalanceOperations.java)</font>
                <br/><br/>
                <font class="smallfont">Max Possible Spend: $#{sysadminUserDetail.researcher.notaccuratemaxpossspend}</font>
                <br/>
                <font class="smallfont">Max Remaining Spend: $#{sysadminUserDetail.researcher.notaccurateremainingpossspend}</font>
                <br/>
                <font class="smallfont">Current Balance: $#{sysadminUserDetail.researcher.notaccuratecurrbalance}</font>
                <br/>
                <font class="smallfont">Percent of Max: #{sysadminUserDetail.researcher.notaccuratepercentofmax} percent</font>
                <br/>
                <font class="smallfont">Amt To Charge: $#{sysadminUserDetail.researcher.notaccurateamttocharge}</font>
                <br/>
            </div>
        </c:if>

        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
        <font class="mediumfont">Account Balance (Internal Account Money Movement)</font>
        <h:form>
            <t:saveState id="save" value="#{sysadminUserDetail}"/>
            <h:outputText value="There are not yet any financial transactions on this account." rendered="#{empty sysadminUserDetail.balances}"/>
            <t:dataScroller for="datatable1" maxPages="5"/>
            <t:dataTable id="datatable1" value="#{sysadminUserDetail.balances}" rows="10" var="balance" rendered="#{!empty sysadminUserDetail.balances}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
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
            <t:dataScroller id="scroll_1" for="datatable1" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
        </div>


        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
        <font class="mediumfont">Account Transactions (Real World Money Movement)</font>
        <h:form>
            <t:saveState id="save" value="#{sysadminUserDetail}"/>
            <h:outputText value="There are not yet any financial transactions on this account." rendered="#{empty sysadminUserDetail.transactions}"/>
            <t:dataScroller for="datatable2" maxPages="5"/>
            <t:dataTable id="datatable2" value="#{sysadminUserDetail.transactions}" rows="10" var="balance" rendered="#{!empty sysadminUserDetail.transactions}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
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
                  <h:outputText value=""/>
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
            <t:dataScroller id="scroll_2" for="datatable2" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
        </div>

        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Completed Surveys</font>
            <h:form>
                <c:forEach var="completedsurvey" items="${sysadminUserDetail.responses}">
                    <h:outputLink value="/survey.jsf?surveyid=#{completedsurvey.surveyid}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"><h:outputText>#{completedsurvey.surveytitle}</h:outputText></h:outputLink><br/>
                    <font class="smallfont">
                        <f:verbatim escape="false">#{completedsurvey.response.responsestatushtml}</f:verbatim>
                    </font><br/><br/>
                </c:forEach>
            </h:form>
        </div>




<%@ include file="/jsp/templates/footer.jsp" %>


