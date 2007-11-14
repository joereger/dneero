<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.SysadminHibernateCache" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Cache";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


    <t:panelTabbedPane id="panel" bgcolor="#ffffff">
        <t:panelTab id="panel_d" label="Iaos Cache">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <h:commandButton action="<%=((SysadminHibernateCache)Pagez.getBeanMgr().get("SysadminHibernateCache")).getRunImpressionActivityObjectQueue()%>" value="ImpressionActivityObjectQueue" styleClass="formsubmitbutton"></h:commandButton>
            <br/>
            <%=((SysadminHibernateCache)Pagez.getBeanMgr().get("SysadminHibernateCache")).getIaosqueue()%>
        </t:panelTab>
        <t:panelTab id="panel_b" label="Hibernate Cache">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <%=((SysadminHibernateCache)Pagez.getBeanMgr().get("SysadminHibernateCache")).getCacheashtml()%>
        </t:panelTab>
        <t:panelTab id="panel_c" label="Misc Cache">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <%=((com.dneero.htmluibeans.SysadminHibernateCache) Pagez.getBeanMgr().get("SysadminHibernateCache")).getMisccacheashtml()%>
        </t:panelTab>
    </t:panelTabbedPane>



<%@ include file="/jsp/templates/footer.jsp" %>