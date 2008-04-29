<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountBalance" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.CustomercareBalance" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Balance";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareBalance customercareBalanceance= (CustomercareBalance) Pagez.getBeanMgr().get("CustomercareBalance");
%>
<%@ include file="/template/header.jsp" %>


    

    <%if (customercareBalanceance.getBalances()==null || customercareBalanceance.getBalances().size()==0){%>
        <font class="normalfont">There are not yet any balance records.</font>
    <%} else {%>
        <%
        ArrayList<GridCol> cols=new ArrayList<GridCol>();
        cols.add(new GridCol("Id", "<$balanceid$>", true, "", "tinyfont"));
        cols.add(new GridCol("User", "<a href=\"/customercare/userdetail.jsp?userid=<$userid$>\"><$username$></a>", true, "", "tinyfont"));
        cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
        cols.add(new GridCol("Description", "<$description$>", false, "", "tinyfont"));
        cols.add(new GridCol("Funds", "<$fundstype$>", true, "", "tinyfont"));
        cols.add(new GridCol("Amount", "<$amt$>", true, "", "tinyfont"));
        cols.add(new GridCol("Balance", "<$currentbalance$>", true, "", "tinyfont"));
        %>
        <%=Grid.render(customercareBalanceance.getBalances(), cols, 200, "/customercare/balance.jsp", "page")%>
    <%}%>

    <br/><br/>
    <a href="/customercare/transactions.jsp"><font class="smallfont">View Transfer Details</font></a>


<%@ include file="/template/footer.jsp" %>