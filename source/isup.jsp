<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dao.Blogpost" %>
<%@ page import="com.dneero.htmluibeans.PublicBlog" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dao.hibernate.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Is the site up?";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<font class="mediumfont">
This page is here for automated checking of system status.  There is nothing here for users, customers, researchers, etc.  Just a geek's tool.
</font>
<br/><br/>

<%
boolean isup = true;
%>

<br/>
<font class="smallfont">
<%
try{
    List<Eula> eulas = HibernateUtil.getSession().createCriteria(Eula.class)
                                   .add(Restrictions.gt("eulaid", Num.randomInt(10)))
                                   .setMaxResults(5)
                                   .setCacheable(false)
                                   .list();
    if (eulas!=null){
        %>connect to main db = success (eulas.size()=<%=eulas.size()%>)<%
        for (Iterator<Eula> eulaIterator=eulas.iterator(); eulaIterator.hasNext();) {
            Eula eula=eulaIterator.next();
        }
    }
} catch (Exception ex){
    isup = false;
    %>connect to main db = fail<%
}
%>
</font>

<br/>
<font class="smallfont">
<%
try{
    List<Dbcache> dbcaches = HibernateUtilDbcache.getSession().createCriteria(Dbcache.class)
                                   .add(Restrictions.lt("dbcacheid", Num.randomInt(10)))
                                   .setMaxResults(5)
                                   .setCacheable(false)
                                   .list();
    if (dbcaches!=null){
        %>connect to dbcache db = success (dbcaches.size()=<%=dbcaches.size()%>)<%
        for (Iterator<Dbcache> dbcacheIterator=dbcaches.iterator(); dbcacheIterator.hasNext();) {
            Dbcache dbcache=dbcacheIterator.next();
        }
    }
} catch (Exception ex){
    isup = false;
    %>connect to dbcache db = fail<%
}
%>
</font>

<br/>
<font class="smallfont">
<%
try{
    List<Impression> impressions = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                   .add(Restrictions.lt("impressionid", Num.randomInt(10)))
                                   .setMaxResults(5)
                                   .setCacheable(false)
                                   .list();
    if (impressions!=null){
        %>connect to impressions db = success (impressions.size()=<%=impressions.size()%>)<%
        for (Iterator<Impression> impressionIterator=impressions.iterator(); impressionIterator.hasNext();) {
            Impression impression=impressionIterator.next();
        }
    }
} catch (Exception ex){
    isup = false;
    %>connect to impressions db = fail<%
}
%>
</font>

<br/><br/>
<font class="largefont">
<%if (isup){%>
    isup=true
<%} else {%>
    isup=false
<%}%>
</font>

<%@ include file="/template/footer.jsp" %>