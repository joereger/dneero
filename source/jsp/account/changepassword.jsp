<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Change Password";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">




                <td valign="top">
                    <h:outputText value="New Password" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputSecret value="<%=((ChangePassword)Pagez.getBeanMgr().get("ChangePassword")).getPassword()%>" id="password" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputSecret>
                </td>
                <td valign="top">
                    <h:message for="password" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Verify New Password" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputSecret value="<%=((ChangePassword)Pagez.getBeanMgr().get("ChangePassword")).getPasswordverify()%>" id="passwordverify" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputSecret>
                </td>
                <td valign="top">
                    <h:message for="passwordverify" styleClass="RED"></h:message>
                </td>




                <td valign="top">
                </td>
                <td valign="top">
                    <br/><br/>
                    <h:commandButton action="<%=((ChangePassword)Pagez.getBeanMgr().get("ChangePassword")).getSaveAction()%>" value="Save New Password" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>


<%@ include file="/jsp/templates/footer.jsp" %>

