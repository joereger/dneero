<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsListBloggers" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "People in SuperPanel";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercarePanelsListBloggers customercarePanelsListBloggers= (CustomercarePanelsListBloggers)Pagez.getBeanMgr().get("CustomercarePanelsListBloggers");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("remove")) {
        try {
            customercarePanelsListBloggers.removeFromPanel();
            Pagez.getUserSession().setMessage("User removed from panel.");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


        <%if (customercarePanelsListBloggers.getListitems()==null || customercarePanelsListBloggers.getListitems().size()==0){%>
            <font class="normalfont">Nobody in this panel.</font>
        <%} else {%>
            <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Name", "<a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.nickname$></a>", true, "", "smallfont"));
            cols.add(new GridCol("IsRejected?", "<$panelmembership.issysadminrejected$>", true, "", "smallfont"));
            cols.add(new GridCol("Name", "<a href=\"/customercare/panels-listbloggersinpanel.jsp?panelmembershipid=<$panelmembership.panelmembershipid$>&panelid=<$panelmembership.panelid$>&action=remove\">Remove</a>", true, "", "smallfont"));
            %>
            <%=Grid.render(customercarePanelsListBloggers.getListitems(), cols, 50, "/customercare/panels-listbloggersinpanel.jsp", "page")%>
        <%}%>



<%@ include file="/template/footer.jsp" %>


