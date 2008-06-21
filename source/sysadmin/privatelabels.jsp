<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminSystemProps" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Private Labels";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>


<%@ include file="/template/header.jsp" %>

    <%
    List pls = HibernateUtil.getSession().createQuery("from Pl").setCacheable(true).list();
    %>


    <%if (pls==null || pls.size()==0){%>
        <font class="normalfont">No Private Labels!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Id", "<$plid$>", false, "", "tinyfont"));
            cols.add(new GridCol("Name", "<a href=\"/sysadmin/privatelabeledit.jsp?plid=<$plid$>\"><$name$></a>", false, "", "mediumfont"));
        %>
        <%=Grid.render(pls, cols, 200, "/sysadmin/privatelabels.jsp", "page")%>
    <%}%>



     <br/><br/>
     <a href="/sysadmin/privatelabeledit.jsp"><font class="mediumfont">New Private Label</font></a>



<%@ include file="/template/footer.jsp" %>