<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.SysadminMassemailList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Mass Emails";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminMassemailList sysadminMassemailList = (SysadminMassemailList)Pagez.getBeanMgr().get("SysadminMassemailList");
%>
<%@ include file="/template/header.jsp" %>



    <%if (sysadminMassemailList.getMassemails()==null || sysadminMassemailList.getMassemails().size()==0){%>
        <font class="normalfont">No surveys!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Id", "<$massemailid$>", false, "", "tinyfont"));
            cols.add(new GridCol("Date", "<$date|" + Grid.GRIDCOLRENDERER_DATETIMECOMPACT + "$>", false, "", "tinyfont"));
            cols.add(new GridCol("Processed Userid", "<$lastuseridprocessed$>", false, "", "tinyfont"));
            cols.add(new GridCol("Subject", "<a href=\"massemaildetail.jsp?massemailid=<$massemailid$>\"><$subject$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Status", "<$status$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(sysadminMassemailList.getMassemails(), cols, 200, "massemaillist.jsp", "page")%>
    <%}%>



     <br/><br/>
     <a href="sysadminmassemaildetail.jsp"><font class="mediumfont">New Mass Email</font></a>



<%@ include file="/template/footer.jsp" %>



