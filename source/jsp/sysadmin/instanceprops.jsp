<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "InstanceProps... Be Careful!!!";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



            <h:messages styleClass="RED"/>
            <table cellpadding="0" cellspacing="0" border="0">

                <td valign="top">
                    <h:outputText value="Instance Name" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getInstancename()%>" id="instancename" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="instancename" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="dbConnectionUrl" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbConnectionUrl()%>" id="dbConnectionUrl" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbConnectionUrl" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="dbUsername" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbUsername()%>" id="dbUsername" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbUsername" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="dbPassword" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbPassword()%>" id="dbPassword" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbPassword" styleClass="RED"></h:message>
                </td>


                <td valign="top">
                    <h:outputText value="dbMaxActive" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbMaxActive()%>" id="dbMaxActive" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbMaxActive" styleClass="RED"></h:message>
                </td>
                
                <td valign="top">
                    <h:outputText value="dbMaxIdle" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbMaxIdle()%>" id="dbMaxIdle" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbMaxIdle" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="dbMinIdle" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbMinIdle()%>" id="dbMinIdle" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbMinIdle" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="dbMaxWait" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbMaxWait()%>" id="dbMaxWait" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbMaxWait" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="dbDriverName" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont"></font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getDbDriverName()%>" id="dbDriverName" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="dbDriverName" styleClass="RED"></h:message>
                </td>

                <td valign="top">
                    <h:outputText value="runScheduledTasksOnThisInstance" styleClass="formfieldnamefont"></h:outputText>
                    <br/>
                    <font class="tinyfont">0 or 1</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getRunScheduledTasksOnThisInstance()%>" id="runScheduledTasksOnThisInstance" required="true">
                        <f:validateLength maximum="255"></f:validateLength>
                    </h:inputText>
                </td>
                <td valign="top">
                    <h:message for="runScheduledTasksOnThisInstance" styleClass="RED"></h:message>
                </td>



                <td valign="top">
                </td>
                <td valign="top">
                    <br/><br/>
                    <h:commandButton action="<%=((SysadminInstanceProps)Pagez.getBeanMgr().get("SysadminInstanceProps")).getSaveProps()%>" value="Save Instance Props" styleClass="formsubmitbutton"></h:commandButton>
                </td>
                <td valign="top">
                </td>

            </table>



<%@ include file="/jsp/templates/footer.jsp" %>