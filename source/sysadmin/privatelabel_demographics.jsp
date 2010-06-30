<%@ page import="com.dneero.cache.providers.CacheFactory" %>
<%@ page import="com.dneero.dao.Pl" %>
<%@ page import="com.dneero.privatelabel.PlFinder" %>
<%@ page import="com.dneero.privatelabel.PlVerification" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Private Label Demographic Fields";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>

<%
    Pl pl = new Pl();
    if (request.getParameter("plid")!=null && Num.isinteger(request.getParameter("plid"))){
        pl = Pl.get(Integer.parseInt(request.getParameter("plid")));
    } else {
        Pagez.getUserSession().setMessage("plid wasn't set");
        Pagez.sendRedirect("/sysadmin/privatelabels.jsp");
        return;
    }
    if (pl==null || pl.getPlid()==0){
        Pagez.getUserSession().setMessage("pl==null or plid==0");
        Pagez.sendRedirect("/sysadmin/privatelabels.jsp");
        return;
    }
%>

<%@ include file="/template/header.jsp" %>

        <font class="largefont"><%=pl.getName()%></font>
        <br/><br/>

        <%
        List demographics = HibernateUtil.getSession().createQuery("from Demographic where plid='"+pl.getPlid()+"' order by ordernum asc").setCacheable(true).list();
        %>


        <%if (demographics==null || demographics.size()==0){%>
            <font class="normalfont">No Demographics for this PL!</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Name", "<a href=\"/sysadmin/privatelabel_demographics_edit.jsp?plid=<$plid$>&demographicid=<$demographicid$>\"><$name$></a>", false, "", "mediumfont"));
                cols.add(new GridCol("PossibleValues", "<$possiblevalues$>", false, "", "tinyfont"));
            %>
            <%=Grid.render(demographics, cols, 200, "/sysadmin/privatelabel_demographics.jsp", "page")%>
        <%}%>



         <br/><br/>
         <a href="/sysadmin/privatelabel_demographics_edit.jsp?plid=<%=pl.getPlid()%>"><font class="mediumfont">New Demographic Field</font></a>



<%@ include file="/template/footer.jsp" %>



