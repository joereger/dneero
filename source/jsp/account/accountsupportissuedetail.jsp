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
    <ui:define name="title">Support: Issue Detail<br/><br/></ui:define>
    <ui:param name="navtab" value="youraccount"/>
    <ui:define name="body">
    <d:authorization acl="account" redirectonfail="true"/>


    <h:form>
            <h:messages styleClass="RED"/>
            <h:inputHidden name="supportissueid" value="#{accountSupportIssueDetail.supportissueid}" />



            <h:outputText value="#{accountSupportIssueDetail.supportissue.subject}" styleClass="mediumfont"></h:outputText>



            <c:forEach var="supportissuecomm" items="#{accountSupportIssueDetail.supportissuecomms}">
                <d:roundedCornerBox uniqueboxname="supportissuecomm-#{supportissuecomm.supportissuecommid}" bodycolor="e6e6e6" widthinpixels="500">
                    <h:outputText value="#{supportissuecomm.datetime}"></h:outputText>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="From: dNeero Admin" style="font-weight: bold;" rendered="#{supportissuecomm.isfromdneeroadmin}"></h:outputText>
                    <h:outputText value="From: You" rendered="#{!supportissuecomm.isfromdneeroadmin}"></h:outputText>
                    <f:verbatim><br/><br/></f:verbatim>
                    <h:outputText value="#{supportissuecomm.notes}"></h:outputText>
                </d:roundedCornerBox>
            </c:forEach>



            <h:panelGrid columns="3" cellpadding="3" border="0">


                <h:panelGroup>
                    <h:outputText value=" "></h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputTextarea value="#{accountSupportIssueDetail.notes}" id="notes" required="true" rows="5" cols="50">
                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                </h:inputTextarea>
                </h:panelGroup>
                <h:panelGroup>
                    <h:message for="notes" styleClass="RED"></h:message>
                </h:panelGroup>

                <h:panelGroup>
                </h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="#{accountSupportIssueDetail.newNote}" value="Add a Comment to this Issue" styleClass="formsubmitbutton"></h:commandButton>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>




        </h:form>

</ui:define>


</ui:composition>
</html>


