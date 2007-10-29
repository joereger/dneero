<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.Authorization" %>
<%
//Do page auth
Authorization.check(acl);
%>
<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="header-dneero.jsp" %>
<% } else { %>
    <%@ include file="header-facebook.jsp" %>
<% }%>