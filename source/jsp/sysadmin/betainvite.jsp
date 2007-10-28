<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >



<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Beta Invites<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
    <d:authorization acl="systemadmin" redirectonfail="true"/>

    <h:form>


        <h:panelGrid columns="3" cellpadding="3" border="0">


            <h:panelGroup>
                <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{sysadminBetainvite.name}" id="name" required="true"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="name" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{sysadminBetainvite.email}" id="email" required="true"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="email" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{sysadminBetainvite.password}" id="password" required="true"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="password" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Message" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputTextarea value="#{sysadminBetainvite.message}" id="message" required="true" cols="75" rows="10">
                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                </h:inputTextarea>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="message" styleClass="RED"></h:message>
            </h:panelGroup>




            <h:panelGroup>
            </h:panelGroup>
            <h:panelGroup>
                <h:commandButton action="#{sysadminBetainvite.newInvite}" value="Invite this Person" styleClass="formsubmitbutton"></h:commandButton>
            </h:panelGroup>
            <h:panelGroup>
            </h:panelGroup>

        </h:panelGrid>



    </h:form>

    <br/><br/>

    <h:form>

        <t:saveState id="save" value="#{sysadminBetainvite}"/>

        <t:dataTable id="datatable" value="#{sysadminBetainvite.betainvites}" rows="50" var="betainvite" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap, tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Name"/>
            </f:facet>
            <h:outputText value="#{betainvite.name}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Email"/>
            </f:facet>
            <h:outputText value="#{betainvite.email}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Pass"/>
            </f:facet>
            <h:outputText value="#{betainvite.password}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Has Logged In?"/>
            </f:facet>
            <h:outputText value="Yes" styleClass="smallfont" rendered="#{betainvite.hasloggedin}"/>
            <h:outputText value="No" styleClass="smallfont" rendered="#{!betainvite.hasloggedin}"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Times Logged In"/>
            </f:facet>
            <h:outputText value="#{betainvite.numberoftimesloggedin}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Last Login"/>
            </f:facet>
            <h:outputText value="#{betainvite.datelastloggedin}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
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



</ui:define>


</ui:composition>
</html>



