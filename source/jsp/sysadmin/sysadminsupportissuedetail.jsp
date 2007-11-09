<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issue Detail";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>





<h:form>
            <h:messages styleClass="RED"/>
            <h:inputHidden name="supportissueid" value="<%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getSupportissueid()%>" />


            <d:roundedCornerBox uniqueboxname="supportissuesubject" bodycolor="00ff00" widthinpixels="500">
                 <h:outputText value="<%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getSupportissue().getSubject()%>" styleClass="mediumfont"></h:outputText>
            </d:roundedCornerBox>


            <c:forEach var="supportissuecomm" items="<%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getSupportissuecomms()%>">
                <d:roundedCornerBox uniqueboxname="supportissuecomm-<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getSupportissuecommid()%>" bodycolor="e6e6e6" widthinpixels="500">
                    <h:outputText value="<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getDatetime()%>" style="font-weight: bold;"></h:outputText>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="From: dNeero Admin" rendered="<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getIsfromdneeroadmin()%>"  style="font-weight: bold;"></h:outputText>
                    <h:commandLink action="<%=((SysadminUserDetail)Pagez.getBeanMgr().get("SysadminUserDetail")).getBeginView()%>" immediate="true" rendered="<%=((!supportissuecomm)Pagez.getBeanMgr().get("!supportissuecomm")).getIsfromdneeroadmin()%>">
                        <h:outputText value="From: <%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getFromuser().getFirstname()%> <%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getFromuser().getLastname()%>"  style="font-weight: bold;"/>
                        <f:param name="userid" value="<%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getFromuser().getUserid()%>" />
                    </h:commandLink>
                    <% if ("#{sysadminSupportIssueDetail.fromuser.facebookuserid gt 0}){ %>
                        <h:outputText value="(Facebook User)"  style="font-weight: bold;"></h:outputText>        
                    <% } %>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="<%=((Supportissuecomm)Pagez.getBeanMgr().get("Supportissuecomm")).getNotes()%>"></h:outputText>
                </d:roundedCornerBox>
            </c:forEach>


            <d:roundedCornerBox uniqueboxname="newsupportissue" bodycolor="00ff00" widthinpixels="500">
            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value=" "></h:outputText>
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getNotes()%>" id="notes" cols="72" rows="8" required="true">
                        <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                    </h:inputTextarea>
                </td>
                <td valign="top">
                    <h:message for="notes" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value=" "></h:outputText>
                </td>
                <td valign="top">
                    <h:selectOneMenu value="<%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getStatus()%>" id="status" required="true">
                        <f:selectItem  itemValue="0" itemLabel="Open"></f:selectItem>
                        <f:selectItem  itemValue="1" itemLabel="Working"></f:selectItem>
                        <f:selectItem  itemValue="2" itemLabel="Closed"></f:selectItem>
                    </h:selectOneMenu>
                </td>
                <td valign="top">
                    <h:message for="status" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminSupportIssueDetail)Pagez.getBeanMgr().get("SysadminSupportIssueDetail")).getNewNote()%>" value="Add a Comment" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>
            </d:roundedCornerBox>




<%@ include file="/jsp/templates/footer.jsp" %>



