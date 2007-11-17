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
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel1');" id="tab1" onclick="return false;">Questions</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" onclick="return false;">Preview Your Survey</a>
    <a href="" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel3');" onclick="return false;">Question Type Samples</a>
    </div>
    <div class="panel" id="panel1" style="display: block">
        <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
        <h:commandButton action="<%=((SysadminHibernateCache)Pagez.getBeanMgr().get("SysadminHibernateCache")).getRunImpressionActivityObjectQueue()%>" value="ImpressionActivityObjectQueue" styleClass="formsubmitbutton"></h:commandButton>
        <br/>
        <%=((SysadminHibernateCache)Pagez.getBeanMgr().get("SysadminHibernateCache")).getIaosqueue()%>
    </div>
    <div class="panel" id="panel2" style="display: none">
        <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
        <%=((SysadminHibernateCache)Pagez.getBeanMgr().get("SysadminHibernateCache")).getCacheashtml()%>
    </div>
    <div class="panel" id="panel3" style="display: none">
        <h:graphicImage url="/images/clear.gif" width="700" height="1"/><br/>
        <%=((com.dneero.htmluibeans.SysadminHibernateCache) Pagez.getBeanMgr().get("SysadminHibernateCache")).getMisccacheashtml()%>
    </div>

  



<%@ include file="/jsp/templates/footer.jsp" %>