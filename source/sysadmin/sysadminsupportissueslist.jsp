<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.htmluibeans.SysadminSupportIssuesList" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issues";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminSupportIssuesList sysadminSupportIssuesList = (SysadminSupportIssuesList)Pagez.getBeanMgr().get("SysadminSupportIssuesList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("showall")) {
        try {
            sysadminSupportIssuesList.setShowall(true);
            sysadminSupportIssuesList.showAll();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>




     <a href="sysadminsupportissueslist.jsp?action=showall"><font class="tinyfont">Show All?</font></a>
     <br/>



    <%if (sysadminSupportIssuesList.getSupportissues()==null || sysadminSupportIssuesList.getSupportissues().size()==0){%>
        <font class="normalfont">No surveys!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Id", "<$supportissueid$>", false, "", "tinyfont"));
            cols.add(new GridCol("", "<$datetime|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
            cols.add(new GridCol("", "<a href=\"sysadminsupportissuedetail.jsp?supportissueid=<$supportissueid$>\"><$subject$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Status", "<$status$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(sysadminSupportIssuesList.getSupportissues(), cols, 200, "sysadminsupportissueslist.jsp", "page")%>
    <%}%>








<%@ include file="/template/footer.jsp" %>



