<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountBalance" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Balance: "+((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getCurrentbalance();
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
        <%if (((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getCurrentbalanceDbl()>0){%>
            <h:outputText value="" styleClass="smallfont" style="color: #000000;" rendered="#{accountBalance.currentbalanceDbl gt 0}"/>
            <font class="smallfont" style="color: #000000;">(We owe you money.  Once a day, if you have a balance of over $20 and don't have any live surveys that you've launched yourself then we'll send money to the PayPal address in your account settings.)</font>
        <%} else if (((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getCurrentbalanceDbl()<0){%>
            <h:outputText value="(You owe us money.)" styleClass="smallfont" style="color: #000000;" rendered="#{accountBalance.currentbalanceDbl lt 0}"/>
        <%}%>
    </div>
    <br/><br/>
    

    <%if (((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getBalances()==null || ((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getBalances().size()==0){%>
        <font class="normalfont">There are not yet any financial transactions on your account.  Go fill out some surveys!  Or create some!</font>
    <%} else {%>
        <%
        ArrayList<GridCol> cols=new ArrayList<GridCol>();
        cols.add(new GridCol("Id", "<$balanceid$>", true, "", "tinyfont"));
        cols.add(new GridCol("Date", "<$date$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
        cols.add(new GridCol("Description", "<$description$>", false, "", "tinyfont"));
        cols.add(new GridCol("Amount", "<$amt$>", true, "", "tinyfont"));
        cols.add(new GridCol("Balance", "<$currentbalance$>", true, "", "tinyfont"));
        %>
        <%=Grid.render(((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getBalances(), cols, 50, "accountbalance.jsp", "page")%>
    <%}%>

    <br/><br/>
    <a href="/jsp/account/accounttransactions.jsp"><font class="smallfont">View Transfer Details</font></a>


<%@ include file="/jsp/templates/footer.jsp" %>