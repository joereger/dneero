<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Cache";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <t:panelTabbedPane id="panel" bgcolor="#ffffff">
        <t:panelTab id="panel_d" label="Iaos Cache">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <h:commandButton action="#{sysadminHibernateCache.runImpressionActivityObjectQueue}" value="ImpressionActivityObjectQueue" styleClass="formsubmitbutton"></h:commandButton>
            <br/>
            <f:verbatim escape="false">#{sysadminHibernateCache.iaosqueue}</f:verbatim>
        </t:panelTab>
        <t:panelTab id="panel_b" label="Hibernate Cache">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim escape="false">#{sysadminHibernateCache.cacheashtml}</f:verbatim>
        </t:panelTab>
        <t:panelTab id="panel_c" label="Misc Cache">
            <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
            <f:verbatim escape="false">#{sysadminHibernateCache.misccacheashtml}</f:verbatim>
        </t:panelTab>
    </t:panelTabbedPane>
    </h:form>

    </ui:define>


</ui:composition>
</html>