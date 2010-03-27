<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsListBloggers" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "People in Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherPanelsListBloggers researcherPanelsListBloggers = (ResearcherPanelsListBloggers)Pagez.getBeanMgr().get("ResearcherPanelsListBloggers");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("remove")) {
        try {
            researcherPanelsListBloggers.removeFromPanel();
            Pagez.getUserSession().setMessage("User removed from panel.");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


        <%if (researcherPanelsListBloggers.getListitems()==null || researcherPanelsListBloggers.getListitems().size()==0){%>
            <font class="normalfont">Nobody in this panel.</font>
        <%} else {%>
            <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Name", "<a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.nickname$></a>", true, "", "smallfont"));
            cols.add(new GridCol("Name", "<a href=\"/researcher/panels-listbloggersinpanel.jsp?panelmembershipid=<$panelmembership.panelmembershipid$>&action=remove\">Remove</a>", true, "", "smallfont"));
            %>
            <%=Grid.render(researcherPanelsListBloggers.getListitems(), cols, 50, "/researcher/panels-listbloggersinpanel.jsp", "page")%>
        <%}%>



<%@ include file="/template/footer.jsp" %>


