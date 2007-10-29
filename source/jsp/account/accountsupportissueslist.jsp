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


            <t:dataTable id="datatable" value="#{accountSupportIssuesList.supportissues}" rows="15" var="supportissue" rendered="#{!empty accountSupportIssuesList.supportissues}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
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
                <h:outputText value="#{supportissue.datetime}" styleClass="mediumfont"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <h:commandLink action="#{accountSupportIssueDetail.beginView}">
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
        </h:form>
        <h:form>

            <br/>
            <font class="mediumfont">Ask a Question. Make an Observation. Recommend an Improvement.</font>
            <br/>
            <font class="smallfont">Use this form to ask us anything at all about your account.  Report bugs.  Tell us where you're confused.  Tell us what could be better.  All communications will be archived and tracked for you here in the support section.</font>
            <br/><br/>
            <h:messages styleClass="RED"/>
            <h:panelGrid columns="3" cellpadding="3" border="0">

                <h:panelGroup>
                    <h:outputText value="Subject:" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText value="#{accountNewSupportIssue.subject}" id="subject" required="true">
                        <f:validateLength minimum="3" maximum="1024"></f:validateLength>
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="subject" styleClass="RED"></h:message>
                </h:panelGroup>


                <h:panelGroup>
                    <h:outputText value="Issue Description:" styleClass="formfieldnamefont"></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputTextarea value="#{accountNewSupportIssue.notes}" id="notes" required="true" cols="45" rows="5">
                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                </h:inputTextarea>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="notes" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{accountNewSupportIssue.newIssue}" value="Create a New Support Issue" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>


<%@ include file="/jsp/templates/footer.jsp" %>


