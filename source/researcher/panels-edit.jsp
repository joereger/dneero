<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsEdit" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherPanelsEdit researcherPanelsEdit = (ResearcherPanelsEdit)Pagez.getBeanMgr().get("ResearcherPanelsEdit");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("edit")) {
        try {
            researcherPanelsEdit.getPanel().setName(Textbox.getValueFromRequest("panelname", "Panel Name", true, DatatypeString.DATATYPEID));
            researcherPanelsEdit.edit();
            Pagez.getUserSession().setMessage("Panel edited.");
            Pagez.sendRedirect("panels.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <form action="panels-edit.jsp" method="post">
        <input type="hidden" name="action" value="edit">

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel Name</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("panelname", researcherPanelsEdit.getPanel().getName(), 255, 35, "", "")%>
                </td>
            </tr>


             <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Edit Panel">
                </td>
            </tr>

        </table>

    </form>

<%@ include file="/template/footer.jsp" %>

