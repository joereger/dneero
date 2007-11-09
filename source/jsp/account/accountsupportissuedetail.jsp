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
            <h:inputHidden name="supportissueid" value="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getSupportissueid()%>" />



            <h:outputText value="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getSupportissue().getSubject()%>" styleClass="mediumfont"></h:outputText>



            <c:forEach var="supportissuecomm" items="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getSupportissuecomms()%>">
                <d:roundedCornerBox uniqueboxname="supportissuecomm-<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getSupportissuecommid()%>" bodycolor="e6e6e6" widthinpixels="500">
                    <h:outputText value="<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getDatetime()%>"></h:outputText>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="From: dNeero Admin" style="font-weight: bold;" rendered="<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getIsfromdneeroadmin()%>"></h:outputText>
                    <h:outputText value="From: You" rendered="<%=((!supportissuecomm)Pagez.getBeanMgr().get("!supportissuecomm")).getIsfromdneeroadmin()%>"></h:outputText>
                    <f:verbatim><br/><br/></f:verbatim>
                    <h:outputText value="<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getNotes()%>"></h:outputText>
                </d:roundedCornerBox>
            </c:forEach>



            <table cellpadding="0" cellspacing="0" border="0">


                <td valign="top">
                    <h:outputText value=" "></h:outputText>
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getNotes()%>" id="notes" required="true" rows="5" cols="50">
                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                </h:inputTextarea>
                </td>
                <td valign="top">
                    <h:message for="notes" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((AccountSupportIssueDetail)Pagez.getBeanMgr().get("AccountSupportIssueDetail")).getNewNote()%>" value="Add a Comment to this Issue" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>



<%@ include file="/jsp/templates/footer.jsp" %>

