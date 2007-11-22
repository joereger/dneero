<%@ page import="com.dneero.htmlui.Pagez" %>
<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <%@ include file="footer-dneero.jsp" %>
<% } else { %>
    <%@ include file="footer-facebook.jsp" %>
<% }%>
