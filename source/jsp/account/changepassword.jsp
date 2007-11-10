<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ChangePassword" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Change Password";
String navtab = "youraccount";
String acl = "account";
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
    try {
        ((ChangePassword) Pagez.getBeanMgr().get("ChangePassword")).setPassword(TextboxSecret.getValueFromRequest("password", "New Password", true, DatatypeString.DATATYPEID));
        ((ChangePassword) Pagez.getBeanMgr().get("ChangePassword")).setPasswordverify(TextboxSecret.getValueFromRequest("passwordverify", "Verify New Password", true, DatatypeString.DATATYPEID));
        ((ChangePassword) Pagez.getBeanMgr().get("ChangePassword")).saveAction();
    } catch (ValidationException vex) {
        Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
    }
}
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <br/><br/>
    <form action="changepassword.jsp" method="post">
        <input type="hidden" name="action" value="save">
            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">New Password</font>
                    </td>
                    <td valign="top">
                        <%=TextboxSecret.getHtml("password", ((ChangePassword) Pagez.getBeanMgr().get("ChangePassword")).getPassword(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Verify New Password</font>
                    </td>
                    <td valign="top">
                        <%=TextboxSecret.getHtml("passwordverify", ((ChangePassword) Pagez.getBeanMgr().get("ChangePassword")).getPasswordverify(), 255, 20, "", "")%>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton" value="Save New Password">
                    </td>
                </tr>

            </table>
       </form>

<%@ include file="/jsp/templates/footer.jsp" %>

