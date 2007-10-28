<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"
      xmlns:c="http://java.sun.com/jstl/core"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Support Issue Detail<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
    <d:authorization acl="systemadmin" redirectonfail="true"/>



<h:form>
            <h:messages styleClass="RED"/>
            <h:inputHidden name="supportissueid" value="#{sysadminSupportIssueDetail.supportissueid}" />


            <d:roundedCornerBox uniqueboxname="supportissuesubject" bodycolor="00ff00" widthinpixels="500">
                 <h:outputText value="#{sysadminSupportIssueDetail.supportissue.subject}" styleClass="mediumfont"></h:outputText>
            </d:roundedCornerBox>


            <c:forEach var="supportissuecomm" items="#{sysadminSupportIssueDetail.supportissuecomms}">
                <d:roundedCornerBox uniqueboxname="supportissuecomm-#{supportissuecomm.supportissuecommid}" bodycolor="e6e6e6" widthinpixels="500">
                    <h:outputText value="#{supportissuecomm.datetime}" style="font-weight: bold;"></h:outputText>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="From: dNeero Admin" rendered="#{supportissuecomm.isfromdneeroadmin}"  style="font-weight: bold;"></h:outputText>
                    <h:commandLink action="#{sysadminUserDetail.beginView}" immediate="true" rendered="#{!supportissuecomm.isfromdneeroadmin}">
                        <h:outputText value="From: #{sysadminSupportIssueDetail.fromuser.firstname} #{sysadminSupportIssueDetail.fromuser.lastname}"  style="font-weight: bold;"/>
                        <f:param name="userid" value="#{sysadminSupportIssueDetail.fromuser.userid}" />
                    </h:commandLink>
                    <c:if test="#{sysadminSupportIssueDetail.fromuser.facebookuserid gt 0}">
                        <h:outputText value="(Facebook User)"  style="font-weight: bold;"></h:outputText>        
                    </c:if>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="#{supportissuecomm.notes}"></h:outputText>
                </d:roundedCornerBox>
            </c:forEach>


            <d:roundedCornerBox uniqueboxname="newsupportissue" bodycolor="00ff00" widthinpixels="500">
            <h:messages styleClass="RED"/>
            <h:panelGrid columns="3" cellpadding="3" border="0">

                <h:panelGroup>
                    <h:outputText value=" "></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputTextarea value="#{sysadminSupportIssueDetail.notes}" id="notes" cols="72" rows="8" required="true">
                        <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                    </h:inputTextarea>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="notes" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                    <h:outputText value=" "></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectOneMenu value="#{sysadminSupportIssueDetail.status}" id="status" required="true">
                        <f:selectItem  itemValue="0" itemLabel="Open"></f:selectItem>
                        <f:selectItem  itemValue="1" itemLabel="Working"></f:selectItem>
                        <f:selectItem  itemValue="2" itemLabel="Closed"></f:selectItem>
                    </h:selectOneMenu>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="status" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{sysadminSupportIssueDetail.newNote}" value="Add a Comment" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>
            </d:roundedCornerBox>



        </h:form>

</ui:define>


</ui:composition>
</html>



