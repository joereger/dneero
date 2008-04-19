<%@ page import="com.dneero.cachedstuff.*" %>
<%@ page import="com.dneero.charity.CharityReport" %>
<%@ page import="com.dneero.constants.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateCacheStats" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.display.components.def.ComponentTypes" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmlui.Date" %>
<%@ page import="com.dneero.htmluibeans.*" %>
<%@ page import="com.dneero.money.CurrentBalanceCalculator" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%@ page import="com.dneero.pageperformance.PagePerformance" %>
<%@ page import="com.dneero.pageperformance.PagePerformanceUtil" %>
<%@ page import="com.dneero.systemprops.BaseUrl" %>
<%@ page import="com.dneero.systemprops.InstanceProperties" %>
<%@ page import="com.dneero.systemprops.SystemProperty" %>
<%@ page import="com.dneero.systemprops.WebAppRootDir" %>
<%@ page import="com.dneero.util.*" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.*" %>
<%
    boolean isauthorised= Authorization.check(acl);
    if (!isauthorised){
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getIsloggedin()) {
            Pagez.sendRedirect("/notauthorized.jsp");
            return;
        } else {
            Pagez.sendRedirect("/login.jsp");
            return;
        }
    }
%>