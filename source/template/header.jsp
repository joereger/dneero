<%@ page import="com.dneero.cachedstuff.*" %>
<%@ page import="com.dneero.charity.CharityReport" %>
<%@ page import="com.dneero.constants.*" %>
<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateCacheStats" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.display.components.def.ComponentTypes" %>
<%@ page import="com.dneero.helpers.UserInputSafe" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmlui.Date" %>
<%@ page import="com.dneero.htmluibeans.*" %>
<%@ page import="com.dneero.money.CurrentBalanceCalculator" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%@ page import="com.dneero.pageperformance.PagePerformance" %>
<%@ page import="com.dneero.pageperformance.PagePerformanceUtil" %>
<%@ page import="com.dneero.privatelabel.TemplateProcessor" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="com.dneero.startup.Log4jLevels" %>
<%@ page import="com.dneero.systemprops.BaseUrl" %>
<%@ page import="com.dneero.systemprops.InstanceProperties" %>
<%@ page import="com.dneero.systemprops.SystemProperty" %>
<%@ page import="com.dneero.util.*" %>
<%@ page import="org.apache.log4j.Level" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.apache.velocity.VelocityContext" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.*" %>
<% if (!Pagez.getUserSession().getIsfacebookui()) { %>
    <%
    String templateHName = "";
    String templateH= "";
    if (Pagez.getUserSession().getPl()!=null && Pagez.getUserSession().getPl().getWebhtmlheader()!=null && Pagez.getUserSession().getPl().getWebhtmlheader().length()>0){
        templateH= Pagez.getUserSession().getPl().getWebhtmlheader();
        templateHName = "pageheader-plid-"+Pagez.getUserSession().getPl().getPlid();
    } else {
        templateH= Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/header-dneero.vm").toString();
        templateHName = "pageheader-plid-default";
    }
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("pagetitle", pagetitle);
    velocityContext.put("navtab", navtab);
    velocityContext.put("acl", acl);
    String header = TemplateProcessor.process(templateHName, templateH, velocityContext);
    %>
    <%=header%>
    <%Pagez.getUserSession().setMessage("");%>
    <div style="width: 755px; overflow: auto;">
<% } else { %>
    <%@ include file="header-facebook.jsp" %>
<% }%>