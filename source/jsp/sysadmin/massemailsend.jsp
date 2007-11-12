<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Password Required to Send";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:messages styleClass="RED"/>
    <t:saveState id="save" value="#{sysadminMassemailSend}"/>
    <font class="largefont">
    Are you sure you want to do this crazy thing?  Have you spell-checked everything?  Are you 100% certain that you didn't screw something up?  We're talking about a lot of outbound email here.    
    </font>
    <br/><br/>
    <font class="formfieldnamefont">Please enter the send password:</font>   
    <br/>
    <h:inputSecret value="<%=((SysadminMassemailSend)Pagez.getBeanMgr().get("SysadminMassemailSend")).getPassword()%>" id="password" required="true"></h:inputSecret>
    <br/>
    <h:commandButton action="<%=((SysadminMassemailSend)Pagez.getBeanMgr().get("SysadminMassemailSend")).getSend()%>" value="Send this Mass Email" styleClass="formsubmitbutton"></h:commandButton>
    




<%@ include file="/jsp/templates/footer.jsp" %>



