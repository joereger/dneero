<%@ page import="org.apache.log4j.Logger" %>
<%String jspPageName="/tshirt-ordercomplete.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Your T-Shirt Order is Complete";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>

<%@ include file="/template/header.jsp" %>


Your order is complete!  Just send Joe an email at joe.reger@dneero.com with your name, shipping address and the size of t-shirt you'd like!  Thanks!



<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>