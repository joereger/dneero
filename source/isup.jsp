<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dao.Blogpost" %>
<%@ page import="com.dneero.htmluibeans.PublicBlog" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dao.hibernate.*" %>
<%@ page import="com.dneero.email.EmailSend" %>
<%@ page import="com.dneero.email.EmailTemplateProcessor" %>
<%System.out.println("ISUP: -----");%>
<%System.out.println("ISUP: ----------");%>
<%System.out.println("ISUP: Start: "+Time.dateformatcompactwithtime(Time.nowInUserTimezone("EST")));%>
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
String reason = "";
%>

<br/>
<font class="smallfont">
<%
    if (isup){
        try{
            UserSession userSession = Pagez.getUserSession();
            if (userSession!=null){
                System.out.println("ISUP: get UserSession = success");
                %>get UserSession = success<%
            }
        } catch (Exception ex){
            isup = false;
            reason = "get UserSession = fail";
            System.out.println("ISUP: "+reason);
            %><%=reason%><%
        }
    }
%>
</font>

<br/>
<font class="smallfont">
<%
    if (isup){
        try{
            List<Eula> eulas = HibernateUtil.getSession().createCriteria(Eula.class)
                                           .add(Restrictions.gt("eulaid", Num.randomInt(10)))
                                           .setMaxResults(5)
                                           .setCacheable(false)
                                           .list();
            if (eulas!=null){
                System.out.println("ISUP: connect to main db = success");
                %>connect to main db = success<%
                for (Iterator<Eula> eulaIterator=eulas.iterator(); eulaIterator.hasNext();) {
                    Eula eula=eulaIterator.next();
                }
            }
        } catch (Exception ex){
            isup = false;
            reason = "connect to main db = fail";
            System.out.println("ISUP: "+reason);
            %><%=reason%><%
        }
    }
%>
</font>

<br/>
<font class="smallfont">
<%
    if (isup){
        try{
            List<Eula> eulas = HibernateUtil.getSession().createCriteria(Eula.class)
                                           .add(Restrictions.gt("eulaid", Num.randomInt(10)))
                                           .setMaxResults(5)
                                           .setCacheable(true)
                                           .list();
            if (eulas!=null){
                System.out.println("ISUP: connect to main db cached = success");
                %>connect to main db cached = success<%
                for (Iterator<Eula> eulaIterator=eulas.iterator(); eulaIterator.hasNext();) {
                    Eula eula=eulaIterator.next();
                }
            }
        } catch (Exception ex){
            isup = false;
            reason = "connect to main db cached = fail";
            System.out.println("ISUP: "+reason);
            %><%=reason%><%
        }
    }
%>
</font>

<br/>
<font class="smallfont">
<%
    if (isup){
        try{
            List<Dbcache> dbcaches = HibernateUtilDbcache.getSession().createCriteria(Dbcache.class)
                                           .add(Restrictions.lt("dbcacheid", Num.randomInt(10)))
                                           .setMaxResults(5)
                                           .setCacheable(false)
                                           .list();
            if (dbcaches!=null){
                System.out.println("ISUP: connect to dbcache db = success");
                %>connect to dbcache db = success<%
                for (Iterator<Dbcache> dbcacheIterator=dbcaches.iterator(); dbcacheIterator.hasNext();) {
                    Dbcache dbcache=dbcacheIterator.next();
                }
            }
        } catch (Exception ex){
            isup = false;
            reason = "connect to dbcache db = fail";
            System.out.println("ISUP: "+reason);
            %><%=reason%><%
        }
    }
%>
</font>

<br/>
<font class="smallfont">
<%
    if (isup){
        try{
            List<Dbcache> dbcaches = HibernateUtilDbcache.getSession().createCriteria(Dbcache.class)
                                           .add(Restrictions.lt("dbcacheid", Num.randomInt(10)))
                                           .setMaxResults(5)
                                           .setCacheable(true)
                                           .list();
            if (dbcaches!=null){
                System.out.println("ISUP: connect to dbcache db cached = success");
                %>connect to dbcache db cached = success<%
                for (Iterator<Dbcache> dbcacheIterator=dbcaches.iterator(); dbcacheIterator.hasNext();) {
                    Dbcache dbcache=dbcacheIterator.next();
                }
            }
        } catch (Exception ex){
            isup = false;
            reason = "connect to dbcache db cached = fail";
            System.out.println("ISUP: "+reason);
            %><%=reason%><%
        }
    }
%>
</font>

<br/>
<font class="smallfont">
<%
    if (isup){
        try{
            List<Impression> impressions = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                           .add(Restrictions.lt("impressionid", Num.randomInt(10)))
                                           .setMaxResults(5)
                                           .setCacheable(false)
                                           .list();
            if (impressions!=null){
                System.out.println("ISUP: connect to impressions db = success");
                %>connect to impressions db = success<%
                for (Iterator<Impression> impressionIterator=impressions.iterator(); impressionIterator.hasNext();) {
                    Impression impression=impressionIterator.next();
                }
            }
        } catch (Exception ex){
            isup = false;
            reason = "connect to impressions db = fail";
            System.out.println("ISUP: "+reason);
            %><%=reason%><%
        }
    }
%>
</font>

<br/>
<font class="smallfont">
<%
    if (isup){
        try{
            List<Impression> impressions = HibernateUtilImpressions.getSession().createCriteria(Impression.class)
                                           .add(Restrictions.lt("impressionid", Num.randomInt(10)))
                                           .setMaxResults(5)
                                           .setCacheable(true)
                                           .list();
            if (impressions!=null){
                System.out.println("ISUP: connect to impressions db cached = success");
                %>connect to impressions db cached = success<%
                for (Iterator<Impression> impressionIterator=impressions.iterator(); impressionIterator.hasNext();) {
                    Impression impression=impressionIterator.next();
                }
            }
        } catch (Exception ex){
            isup = false;
            reason = "connect to impressions db cached = fail";
            System.out.println("ISUP: "+reason);
            %><%=reason%><%
        }
    }
%>
</font>

<br/><br/>
<font class="largefont">
<%if (isup){%>
    isup=true
    <%System.out.println("ISUP: isup=true");%>
<%} else {%>
    isup=false
    <%System.out.println("ISUP: isup=false");%>
    <%
        EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero Isup Fail", reason);
    %>
<%}%>
</font>

<%@ include file="/template/footer.jsp" %>
<%System.out.println("ISUP: End: "+Time.dateformatcompactwithtime(Time.nowInUserTimezone("EST")));%>
<%System.out.println("ISUP: ----------");%>
<%System.out.println("ISUP: -----");%>