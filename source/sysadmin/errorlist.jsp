<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminErrorList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Error and Debug Log";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminErrorList sysadminErrorList=(SysadminErrorList) Pagez.getBeanMgr().get("SysadminErrorList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("refresh")) {
        try {
            sysadminErrorList.setMinleveltoshow(Integer.parseInt(Dropdown.getValueFromRequest("minleveltoshow", "Min Level to Show", false)));
            sysadminErrorList.initBean();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("markallold")) {
        try {
            sysadminErrorList.markallold();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deleteall")) {
        try {
            sysadminErrorList.deleteall();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("onlyerrors")) {
        try {
            sysadminErrorList.onlyerrors();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <form action="errorlist.jsp" method="post">
            <input type="hidden" name="action" value="refresh">
            <%=Dropdown.getHtml("minleveltoshow", String.valueOf(sysadminErrorList.getMinleveltoshow()), sysadminErrorList.getLevels(), "", "")%>
            <input type="submit" class="formsubmitbutton" value="Refresh">
        </form>
        <a href="errorlist.jsp?action=markallold"><font class="smallfont">Mark All Old</font></a>
        <a href="errorlist.jsp?action=deleteall"><font class="smallfont">Delete All</font></a>
        <a href="errorlist.jsp?action=onlyerrors"><font class="smallfont">Only Errors</font></a>
        <br/><br/>


        <%if (sysadminErrorList.getErrors()==null || sysadminErrorList.getErrors().size()==0){%>
            <font class="normalfont">None found.</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Id", "<$errorid$>", false, "", "tinyfont"));
                cols.add(new GridCol("Id", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                cols.add(new GridCol("Id", "<$error$>", false, "", "smallfont"));
            %>
            <%=Grid.render(sysadminErrorList.getErrors(), cols, 250, "errorlist.jsp", "page")%>
        <%}%>










<%@ include file="/template/footer.jsp" %>



