<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanels" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Panels";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherPanels researcherPanels=(ResearcherPanels) Pagez.getBeanMgr().get("ResearcherPanels");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            researcherPanels.deletePanel();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("newpanel")) {
        try {
            researcherPanels.setNewpanelname(Textbox.getValueFromRequest("newpanelname", "New Panel Name", true, DatatypeString.DATATYPEID));
            researcherPanels.createNewPanel();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <%if (researcherPanels.getMsg()!=null && !researcherPanels.getMsg().equals("")){%>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        <%=researcherPanels.getMsg()%>
        <br/><br/></font></div></center>
        <br/><br/>
    <%}%>


        <br/><br/>

        <%if (researcherPanels.getListitems()==null || researcherPanels.getListitems().size()==0){%>
            <font class="normalfont">No panels yet.</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Panel Name", "<$panel.name$>", false, "", "normalfont"));
                cols.add(new GridCol("Create Date", "<$panel.createdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "smallfont"));
                cols.add(new GridCol("Members", "<$numberofmembers$>", true, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"panels-listbloggersinpanel.jsp?panelid=<$panel.panelid$>\">View Members</a>", true, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"panels-edit.jsp?panelid=<$panel.panelid$>\">Edit</a>", true, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"panels.jsp?panelid=<$panel.panelid$>&action=delete\">Delete</a>", true, "", "smallfont"));
            %>
            <%=Grid.render(researcherPanels.getListitems(), cols, 50, "panels.jsp", "page")%>
        <%}%>






        <br/><br/>
        <form action="panels.jsp" method="post">
            <input type="hidden" name="action" value="newpanel">
            <%=com.dneero.htmlui.Textbox.getHtml("newpanelname", researcherPanels.getNewpanelname(), 255, 35, "", "")%>
            <input type="submit" class="formsubmitbutton" value="Create a New Panel">
        </form>





<%@ include file="/template/footer.jsp" %>

