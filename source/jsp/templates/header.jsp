<%@ page import="com.dneero.htmlui.Pagez" %>
<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="header-dneero.jsp" %>
<% } else { %>
    <%@ include file="header-facebook.jsp" %>
<% }%>