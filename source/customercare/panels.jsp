<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanels" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SuperPanels";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercarePanels customercarePanels=(CustomercarePanels) Pagez.getBeanMgr().get("CustomercarePanels");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            customercarePanels.deletePanel();
            Pagez.getUserSession().setMessage("Panel deleted.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("newpanel")) {
        try {
            customercarePanels.setNewpanelname(Textbox.getValueFromRequest("newpanelname", "New Panel Name", true, DatatypeString.DATATYPEID));
            customercarePanels.setNewpaneldescription(Textarea.getValueFromRequest("newpaneldescription", "New Panel Description", true));
            customercarePanels.createNewPanel();
            Pagez.getUserSession().setMessage("Panel created.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <%if (request.getParameter("action") != null && request.getParameter("action").equals("deletestart")) {%>
            <font class="mediumfont">Are you sure you want to delete it?  <a href="/customercare/panels.jsp?panelid=<%=request.getParameter("panelid")%>&action=delete">Yes</a></font>
        <%}%>
        <br/><br/>

        <%if (customercarePanels.getListitems()==null || customercarePanels.getListitems().size()==0){%>
            <font class="normalfont">No panels yet.</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Panel Name", "<$panel.name$>", false, "", "normalfont"));
                cols.add(new GridCol("Create Date", "<$panel.createdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "smallfont"));
                cols.add(new GridCol("Members", "<$numberofmembers$>", true, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"/customercare/panels-listbloggersinpanel.jsp?panelid=<$panel.panelid$>\">View Members</a>", true, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"/customercare/panels-addpeople.jsp?panelid=<$panel.panelid$>\">Add People</a>", true, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"/customercare/panels-edit.jsp?panelid=<$panel.panelid$>\">Edit</a>", true, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"/customercare/panels.jsp?panelid=<$panel.panelid$>&action=deletestart\">Delete</a>", true, "", "smallfont"));
            %>
            <%=Grid.render(customercarePanels.getListitems(), cols, 50, "/customercare/panels.jsp", "page")%>
        <%}%>






        <br/><br/>
        <form action="/customercare/panels.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/customercare/panels.jsp">
            <input type="hidden" name="action" value="newpanel">
            <%=com.dneero.htmlui.Textbox.getHtml("newpanelname", customercarePanels.getNewpanelname(), 255, 35, "", "")%>
            <br/><%=com.dneero.htmlui.Textarea.getHtml("newpaneldescription", customercarePanels.getNewpaneldescription(), 5, 45, "", "")%>
            <br/><input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Create a New SuperPanel">
        </form>





<%@ include file="/template/footer.jsp" %>

