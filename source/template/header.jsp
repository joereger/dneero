<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.display.components.def.ComponentTypes" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmlui.Date" %>
<%@ page import="com.dneero.htmluibeans.*" %>
<%@ page import="com.dneero.pageperformance.PagePerformanceUtil" %>
<%@ page import="com.dneero.privatelabel.PlTemplate" %>
<%@ page import="com.dneero.privatelabel.TemplateProcessor" %>
<%@ page import="com.dneero.systemprops.BaseUrl" %>
<%@ page import="com.dneero.util.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.apache.velocity.VelocityContext" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.*" %>
<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <%
    String templateHName = "pageheader-plid-"+Pagez.getUserSession().getPl().getPlid();
    String templateH = PlTemplate.getWebhtmlheader(Pagez.getUserSession().getPl());
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("pagetitle", pagetitle);
    velocityContext.put("navtab", navtab);
    velocityContext.put("acl", acl);
    String header = TemplateProcessor.process(templateHName, templateH, velocityContext);
    %>
    <%=header%>
    <%Pagez.getUserSession().setMessage("");%>
    <%--<div style="width: 755px; overflow: auto; padding: 10px;">--%>
<% } else { %>
    <%@ include file="header-facebook.jsp" %>
<% }%>