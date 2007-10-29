<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support: Issue Detail";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


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


