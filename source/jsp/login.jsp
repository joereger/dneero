<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Log In";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((Login)Pagez.getBeanMgr().get("Login")).getEmail()%>" id="email" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="email" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
                </td>
                <td valign="top">
                    <h:inputSecret value="<%=((Login)Pagez.getBeanMgr().get("Login")).getPassword()%>" id="password" required="true">
                        <f:validateLength minimum="3" maximum="255"></f:validateLength>
                    </h:inputSecret>
                    <f:verbatim><br/></f:verbatim>
                    <h:commandLink value="Lost your password?" action="<%=((LostPassword)Pagez.getBeanMgr().get("LostPassword")).getBeginView()%>" immediate="true" styleClass="tinyfont" style="color: #000000;"/>
                </td>
                <td valign="top">
                    <h:message for="password" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="<%=((Login)Pagez.getBeanMgr().get("Login")).getKeepmeloggedin()%>" id="keepmeloggedin"></h:selectBooleanCheckbox>
                    <font class="formfieldnamefont">Stay Logged In?</font>
                </td>
                <td valign="top">
                    <h:message for="keepmeloggedin" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((Login)Pagez.getBeanMgr().get("Login")).getLogin()%>" value="Log In" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>


            </table>


<%@ include file="/jsp/templates/footer.jsp" %>
