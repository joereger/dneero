<%@ page import="com.dneero.privatelabel.PlTemplate" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%String jsptoinclude="survey.jsp";%>
<jsp:include page="<%=PlTemplate.getIncludeDirectiveFileName(jsptoinclude, Pagez.getUserSession().getPl())%>" />