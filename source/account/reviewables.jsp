<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountSupportIssuesList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.AccountNewSupportIssue" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Content Reviews";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>
    <br/><br/>
    <%

    List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                       .add(Restrictions.eq("useridofcontentcreator", Pagez.getUserSession().getUser().getUserid()))
                                       .addOrder(Order.desc("datelastupdated"))
                                       .setCacheable(true)
                                       .list();


    if (reviews==null || reviews.size()==0) {
        %><font class="normalfont">You have no review issues... this is a good thing.</font><%
    } else {
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Created", "<$dateofcreation|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
            cols.add(new GridCol("Last Updated", "<$datelastupdated|"+Grid.GRIDCOLRENDERER_DATETIMEAGOTEXT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
            cols.add(new GridCol("Summary", "<a href=\"/account/reviewabledetail.jsp?reviewid=<$reviewid$>\">View</a>", false, "", "tinyfont"));
        %>
        <%=Grid.render(reviews, cols, 25, "/account/reviewables.jsp", "page")%>
    <%}%>





<%@ include file="/template/footer.jsp" %>


