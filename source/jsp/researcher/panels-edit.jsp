<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <table cellpadding="0" cellspacing="0" border="0">
                <td valign="top">
                    <h:outputText value="Panel Name" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((ResearcherPanelsEdit)Pagez.getBeanMgr().get("ResearcherPanelsEdit")).getPanel().getName()%>" id="panelname"></h:inputText>
                </td>
                <td valign="top">
                    <h:message for="panelname" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((ResearcherPanelsEdit)Pagez.getBeanMgr().get("ResearcherPanelsEdit")).getEdit()%>" value="Edit Panel" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>



<%@ include file="/jsp/templates/footer.jsp" %>

