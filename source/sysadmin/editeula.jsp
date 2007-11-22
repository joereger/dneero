<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminEditEula" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit EULA";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
SysadminEditEula sysadminEditEula = (SysadminEditEula)Pagez.getBeanMgr().get("SysadminEditEula");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminEditEula.setEula(Textbox.getValueFromRequest("eula", "Eula", false, DatatypeString.DATATYPEID));
            sysadminEditEula.edit();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <font class="mediumfont">Be careful here... every edit... even the slightest one... causes every user to have to read and accept the new EULA the next time they log in.</font>
        <br/><br/>

         <form action="editeula.jsp" method="post">
            <input type="hidden" name="action" value="save">

            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Last Edited: <%=sysadminEditEula.getDate()%></font>
                    </td>
                    <td valign="top">
                        <font class="formfieldnamefont">Eulaid: <%=sysadminEditEula.getEulaid()%></font>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("eula", sysadminEditEula.getEula(), 25, 80, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" value="Edit the EULA and Force All Users to Re-Accept">
                    </td>
                </tr>

            </table>
    </form>


<%@ include file="/jsp/templates/footer.jsp" %>