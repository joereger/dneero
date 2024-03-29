<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountBalance" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%String jspPageName="/account/accountbalance.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Balance: "+((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getCurrentbalance();
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
AccountBalance accountBalance = (AccountBalance)Pagez.getBeanMgr().get("AccountBalance");
%>
<%@ include file="/template/header.jsp" %>

    <!-- Pre-SVNRepositories Backup on July 29, 2010-->
    <!-- Post-SVNRepositories Backup on July 30, 2010-->

    <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
        <%if (accountBalance.getCurrentbalanceDbl()>0){%>
            <font class="smallfont" style="color: #000000;">(We owe you money.  Once a day, usually early morning on the east coast of the US, if you have a balance of over $20 and don't have any live <%=Pagez._surveys()%> that you've launched yourself then we'll send money to the PayPal address in your account settings.)</font>
        <%} else if (accountBalance.getCurrentbalanceDbl()<0){%>
            <font class="smallfont" style="color: #000000;">(You owe us money.)</font>
        <%}%>
        <br/><font class="tinyfont" style="color: #666666;"><a href="/account/balancefaq.jsp">Balance Questions?</a></font>
    </div>
    <br/><br/>
    

    <%if (accountBalance.getBalances()==null || accountBalance.getBalances().size()==0){%>
        <font class="normalfont">There are not yet any financial transactions on your account.  Go join some <%=Pagez._surveys()%>!  Or create some!</font>
    <%} else {%>
        <%
        ArrayList<GridCol> cols=new ArrayList<GridCol>();
        cols.add(new GridCol("Id", "<$balanceid$>", true, "", "tinyfont"));
        cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
        cols.add(new GridCol("Description", "<$description$>", false, "", "tinyfont"));
        cols.add(new GridCol("Amount", "<$amt$>", true, "", "tinyfont"));
        cols.add(new GridCol("Balance", "<$currentbalance$>", true, "", "tinyfont"));
        %>
        <%=Grid.render(accountBalance.getBalances(), cols, 50, "/account/accountbalance.jsp", "page")%>
    <%}%>

    <br/><br/>
    <a href="/account/accounttransactions.jsp"><font class="smallfont">View Transfer Details</font></a>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>