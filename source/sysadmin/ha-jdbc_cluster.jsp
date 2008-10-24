<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminEditEula" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="javax.management.MBeanServer" %>
<%@ page import="javax.management.ObjectName" %>
<%@ page import="java.lang.management.ManagementFactory" %>
<%@ page import="javax.management.JMX" %>
<%@ page import="net.sf.hajdbc.sql.DriverDatabaseClusterMBean" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "HA-JDBC Cluster";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    String clusterId = "cluster1";
    MBeanServer server = ManagementFactory.getPlatformMBeanServer();
    ObjectName name = ObjectName.getInstance("net.sf.hajdbc", "cluster", clusterId);
    // There are 2 ways to programatically invoke methods on an mbean:
    // 1. Generic invoke
    //    Object[] parameterValues = new Object[] { databaseId };
    //    String[] parameterTypes = new String[] { String.class.getName() };
    //    server.invoke(name, "activate", parameterValues, parameterTypes);
    // 2. Dynamic proxy
    DriverDatabaseClusterMBean cluster = JMX.newMBeanProxy(server, name, DriverDatabaseClusterMBean.class);
%>
<%
    if (request.getParameter("action")!=null && request.getParameter("action").equals("activate")) {
        if (request.getParameter("databaseid")!=null) {
            try{
                cluster.activate(request.getParameter("databaseid"));
                Pagez.getUserSession().setMessage("Database "+request.getParameter("databaseid")+" activated.");
            } catch (Exception ex){
                logger.error("", ex);
                Pagez.getUserSession().setMessage("Error: "+ex.getMessage());
            }
        }
    }
%>
<%
    if (request.getParameter("action")!=null && request.getParameter("action").equals("deactivate")) {
        if (request.getParameter("databaseid")!=null) {
            try{
                cluster.deactivate(request.getParameter("databaseid"));
                Pagez.getUserSession().setMessage("Database "+request.getParameter("databaseid")+" deactivated.");
            } catch (Exception ex){
                logger.error("", ex);
                Pagez.getUserSession().setMessage("Error: "+ex.getMessage());
            }
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<%
    %><br/><br/><font class="mediumfont">Active Databases</font><br/><%
    Set<String> activeDatabases = cluster.getActiveDatabases();
    for (Iterator<String> stringIterator=activeDatabases.iterator(); stringIterator.hasNext();) {
        String db=stringIterator.next();
        boolean isalive=cluster.isAlive(db);
        %>
        <font class="tinyfont"><%=db%> isalive=<%=isalive%> <a href="ha-jdbc_cluster.jsp?action=deactivate&databaseid=<%=db%>">deactivate</a></font><br/>
        <%
    }

    %><br/><br/><font class="mediumfont">Inactive Databases</font><br/><%
    Set<String> inactiveDatabases = cluster.getInactiveDatabases();
    for (Iterator<String> stringIterator=inactiveDatabases.iterator(); stringIterator.hasNext();) {
        String db=stringIterator.next();
        boolean isalive=cluster.isAlive(db);
        %>
        <font class="tinyfont"><%=db%> isalive=<%=isalive%> <a href="ha-jdbc_cluster.jsp?action=activate&databaseid=<%=db%>">activate</a></font><br/>
        <%
    }
%>



<%@ include file="/template/footer.jsp" %>