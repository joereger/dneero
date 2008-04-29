<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.CustomercareTransactions" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Transactions";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareTransactions customercareTransactionsions=(CustomercareTransactions) Pagez.getBeanMgr().get("CustomercareTransactions");
%>
<%@ include file="/template/header.jsp" %>


    <font class="mediumfont">Real World Money Movement</font>
    <br/><br/>
    <%if (customercareTransactionsions.getTransactions()==null || customercareTransactionsions.getTransactions().size()==0){%>
        <font class="normalfont">There are not yet any financial transactions!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Id", "<$balancetransactionid$>", true, "", "tinyfont"));
            cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
            cols.add(new GridCol("User", "<a href=\"/customercare/userdetail.jsp?userid=<$userid$>\"><$userid$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Successful?", "<$issuccessful$>", true, "", "tinyfont"));
            cols.add(new GridCol("Desc", "<$description$>", false, "", "tinyfont"));
            cols.add(new GridCol("Notes", "<$notes$>", false, "", "tinyfont"));
            cols.add(new GridCol("Amount", "<$amt$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(customercareTransactionsions.getTransactions(), cols, 200, "/customercare/transactions.jsp", "page")%>
    <%}%>


        


<%@ include file="/template/footer.jsp" %>



