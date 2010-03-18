<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsEdit" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit SuperPanel";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercarePanelsEdit customercarePanelsEdit= (CustomercarePanelsEdit)Pagez.getBeanMgr().get("CustomercarePanelsEdit");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("edit")) {
        try {
            customercarePanelsEdit.getPanel().setName(Textbox.getValueFromRequest("panelname", "Panel Name", true, DatatypeString.DATATYPEID));
            customercarePanelsEdit.getPanel().setDescription(Textarea.getValueFromRequest("paneldescription", "Panel Description", true));
            customercarePanelsEdit.edit();
            Pagez.getUserSession().setMessage("Panel edited.");
            Pagez.sendRedirect("/customercare/panels.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <form action="/customercare/panels-edit.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/customercare/panels-edit.jsp">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="panelid" value="<%=customercarePanelsEdit.getPanel().getPanelid()%>">

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel Name</font>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("panelname", customercarePanelsEdit.getPanel().getName(), 255, 35, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Description</font>
                </td>
                <td valign="top">
                    <%=Textarea.getHtml("paneldescription", customercarePanelsEdit.getPanel().getDescription(), 5, 45, "", "")%>
                </td>
            </tr>


             <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Edit SuperPanel">
                </td>
            </tr>

        </table>

    </form>

<%@ include file="/template/footer.jsp" %>

