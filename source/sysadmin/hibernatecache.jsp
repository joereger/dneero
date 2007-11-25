<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.SysadminHibernateCache" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Cache";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminHibernateCache sysadminHibernateCache = (SysadminHibernateCache)Pagez.getBeanMgr().get("SysadminHibernateCache");
%>
<%@ include file="/template/header.jsp" %>


    <script language="JavaScript" type="text/javascript">
      var panels = new Array('panel1', 'panel2', 'panel3');
      var selectedTab = null;
      function showPanel(tab, name)
      {
        if (selectedTab)
        {
          selectedTab.style.backgroundColor = '';
          selectedTab.style.paddingTop = '';
          selectedTab.style.marginTop = '4px';
        }
        selectedTab = tab;
        selectedTab.style.backgroundColor = 'white';
        selectedTab.style.paddingTop = '6px';
        selectedTab.style.marginTop = '0px';

        for(i = 0; i < panels.length; i++)
          document.getElementById(panels[i]).style.display = (name == panels[i]) ? 'block':'none';

        return false;
      }
    </script>
    <div id="tabs">
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel1');" id="tab1" onclick="return false;">IAOs</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" onclick="return false;">Hibernate</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel3');" onclick="return false;">Misc Cache</a>
    </div>
    <div class="panel" id="panel1" style="display: block">
        <img src="/images/clear.gif" width="700" height="1"/><br/>
        <%=sysadminHibernateCache.getIaosqueue()%>
    </div>
    <div class="panel" id="panel2" style="display: none">
        <img src="/images/clear.gif" width="700" height="1"/><br/>
        <%=sysadminHibernateCache.getCacheashtml()%>
    </div>
    <div class="panel" id="panel3" style="display: none">
        <img src="/images/clear.gif" width="700" height="1"/><br/>
        <%=sysadminHibernateCache.getMisccacheashtml()%>
    </div>

  



<%@ include file="/template/footer.jsp" %>