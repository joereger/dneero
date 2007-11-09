<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "End User License Agreement";
String navtab = "home";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <font class="formfieldnamefont">The End User License Agreement has changed.<br/>You must read and agree to it before you can proceed:</font>
                </td>


                <td valign="top">
                    <h:commandButton action="<%=((LoginAgreeNewEula)Pagez.getBeanMgr().get("LoginAgreeNewEula")).getAgree()%>" value="I Agree to the EULA" styleClass="formsubmitbutton"></h:commandButton>
                </td>


                <td valign="top">
                    <h:message for="eula" styleClass="RED"></h:message>
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=((LoginAgreeNewEula)Pagez.getBeanMgr().get("LoginAgreeNewEula")).getEula()%>" id="eula" cols="80" rows="25" required="true">
                    </h:inputTextarea>
                </td>


            </table>


<%@ include file="/jsp/templates/footer.jsp" %>
