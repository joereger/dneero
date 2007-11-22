<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsListBloggers" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Bloggers in Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
ResearcherPanelsListBloggers researcherPanelsListBloggers = (ResearcherPanelsListBloggers)Pagez.getBeanMgr().get("ResearcherPanelsListBloggers");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("remove")) {
        try {
            researcherPanelsListBloggers.removeFromPanel();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>


        <%if (researcherPanelsListBloggers.getListitems()==null || researcherPanelsListBloggers.getListitems().size()==0){%>
            <font class="normalfont">Nobody in this panel.</font>
        <%} else {%>
            <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Name", "<a href=\"/jsp/profile.jsp?userid=<$user.userid$>\"><$user.firstname$> <$user.lastname$></a>", true, "", "smallfont"));
            cols.add(new GridCol("Name", "<a href=\"panels-listbloggersinpanel.jsp?panelmembershipid=<$panelmembership.panelmembershipid$>&action=remove\">Remove</a>", true, "", "smallfont"));
            %>
            <%=Grid.render(researcherPanelsListBloggers.getListitems(), cols, 50, "panels-listbloggersinpanel.jsp", "page")%>
        <%}%>



<%@ include file="/jsp/templates/footer.jsp" %>


