<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminMassemailSend" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Password Required to Send";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminMassemailSend sysadminMassemailSend = (SysadminMassemailSend)Pagez.getBeanMgr().get("SysadminMassemailSend");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminMassemailSend.setPassword(TextboxSecret.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            sysadminMassemailSend.send();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <form action="massemailsend.jsp" method="post">
        <input type="hidden" name="action" value="send">
        <input type="hidden" name="massemailid" value="<%=sysadminMassemailSend.getMassemail().getMassemailid()%>">


        <font class="largefont">
        Are you sure you want to do this crazy thing?  Have you spell-checked everything?  Are you 100% certain that you didn't screw something up?  We're talking about a lot of outbound email here.
        </font>
        <br/><br/>
        <font class="formfieldnamefont">Please enter the send password:</font>
        <br/>
        <%=TextboxSecret.getHtml("password", sysadminMassemailSend.getPassword(), 255, 20, "", "")%>
        <br/>
        <input type="submit" value="Send this Mass Email">

    </form>




<%@ include file="/template/footer.jsp" %>



