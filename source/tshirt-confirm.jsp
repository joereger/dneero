<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicTshirtConfirm" %>
<%String jspPageName="/tshirt-confirm.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Confirm dNeero T-Shirt Order";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    PublicTshirtConfirm publicTshirtConfirm = (PublicTshirtConfirm) Pagez.getBeanMgr().get("PublicTshirtConfirm");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("buy")) {
        try {
            publicTshirtConfirm.buy();
            if (publicTshirtConfirm.getIspaysuccess()){
                Pagez.sendRedirect("/tshirt-ordercomplete.jsp");
                return;
            }
        } catch(Exception ex){
                logger.error(ex);
                Pagez.getUserSession().setMessage("Sorry, there has been an error... please try again.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


Are you sure you want to buy this t-shirt?
<br/><br/>
<a href="/tshirt-confirm.jsp?action=buy&token=<%=request.getParameter("token")%>&PayerID=<%=request.getParameter("PayerID")%>">Confirm & Pay</a>





<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>