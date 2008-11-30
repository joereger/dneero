<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSupportIssuesList" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issues";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercareSupportIssuesList customercareSupportIssuesListList= (CustomercareSupportIssuesList)Pagez.getBeanMgr().get("CustomercareSupportIssuesList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("showall")) {
        try {
            customercareSupportIssuesListList.setShowall(true);
            customercareSupportIssuesListList.showAll();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>




     <a href="/customercare/sysadminsupportissueslist.jsp?action=showall"><font class="tinyfont">Show All?</font></a>
     <br/>



    <%if (customercareSupportIssuesListList.getSupportissues()==null || customercareSupportIssuesListList.getSupportissues().size()==0){%>
        <font class="normalfont">No issues!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Id", "<$mailid$>", false, "", "tinyfont"));
            cols.add(new GridCol("", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont", "width:150px;", ""));
            cols.add(new GridCol("", "<a href=\"/customercare/sysadminsupportissuedetail.jsp?mailid=<$mailid$>\"><$subject$></a>", false, "", "tinyfont"));
        %>
        <%=Grid.render(customercareSupportIssuesListList.getSupportissues(), cols, 200, "/customercare/sysadminsupportissueslist.jsp", "page")%>
    <%}%>








<%@ include file="/template/footer.jsp" %>



