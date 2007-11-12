<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit EULA";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



        <font class="mediumfont">Be careful here... every edit... even the slightest one... causes every user to have to read and accept the new EULA the next time they log in.</font>
        <br/><br/>

         <h:form id="eulaform">


            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value="Last Edited: <%=((SysadminEditEula)Pagez.getBeanMgr().get("SysadminEditEula")).getDate()%>" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:outputText value="Eulaid: <%=((SysadminEditEula)Pagez.getBeanMgr().get("SysadminEditEula")).getEulaid()%>" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                </td>
            

                <td valign="top">
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=((SysadminEditEula)Pagez.getBeanMgr().get("SysadminEditEula")).getEula()%>" id="eula" cols="80" rows="25" required="true">
                    </h:inputTextarea>
                </td>
                <td valign="top">
                    <h:message for="eula" styleClass="RED"></h:message>
                </td>





                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminEditEula)Pagez.getBeanMgr().get("SysadminEditEula")).getEdit()%>" value="Edit the EULA and Force All Users to Re-Accept" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>



<%@ include file="/jsp/templates/footer.jsp" %>