<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issues";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <t:saveState id="save" value="#{accountSupportIssuesList}"/>


            <t:dataTable id="datatable" value="<%=((AccountSupportIssuesList)Pagez.getBeanMgr().get("AccountSupportIssuesList")).getSupportissues()%>" rows="15" var="supportissue" rendered="#{!empty accountSupportIssuesList.supportissues}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Id"/>
                </f:facet>
                <h:outputText value="<%=((Supportissue)Pagez.getBeanMgr().get("Supportissue")).getSupportissueid()%>"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Date"/>
                </f:facet>
                <h:outputText value="<%=((Supportissue)Pagez.getBeanMgr().get("Supportissue")).getDatetime()%>" styleClass="mediumfont"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <h:commandLink action="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getBeginView()%>">
                    <h:outputText value="<%=((Supportissue)Pagez.getBeanMgr().get("Supportissue")).getSubject()%>" escape="false" />
                    <f:param name="supportissueid" value="<%=((Supportissue)Pagez.getBeanMgr().get("Supportissue")).getSupportissueid()%>" />
                </h:commandLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Status"/>
                </f:facet>
                <h:outputText value="Open" rendered="<%=((Supportissue)Pagez.getBeanMgr().get("Supportissue")).getStatus==0()%>"/>
                <h:outputText value="Working" rendered="<%=((Supportissue)Pagez.getBeanMgr().get("Supportissue")).getStatus==1()%>"/>
                <h:outputText value="Closed" rendered="<%=((Supportissue)Pagez.getBeanMgr().get("Supportissue")).getStatus==2()%>"/>
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
        <h:form>

            <br/>
            <font class="mediumfont">Ask a Question. Make an Observation. Recommend an Improvement.</font>
            <br/>
            <font class="smallfont">Use this form to ask us anything at all about your account.  Report bugs.  Tell us where you're confused.  Tell us what could be better.  All communications will be archived and tracked for you here in the support section.</font>
            <br/><br/>
            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value="Subject:" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((AccountNewSupportIssue)Pagez.getBeanMgr().get("AccountNewSupportIssue")).getSubject()%>" id="subject" required="true">
                        <f:validateLength minimum="3" maximum="1024"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="subject" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Issue Description:" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=((AccountNewSupportIssue)Pagez.getBeanMgr().get("AccountNewSupportIssue")).getNotes()%>" id="notes" required="true" cols="45" rows="5">
                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                </h:inputTextarea>
                </td>
                <td valign="top">
                    <h:message for="notes" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((AccountNewSupportIssue)Pagez.getBeanMgr().get("AccountNewSupportIssue")).getNewIssue()%>" value="Create a New Support Issue" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>


<%@ include file="/jsp/templates/footer.jsp" %>


