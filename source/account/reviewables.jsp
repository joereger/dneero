<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountInbox" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.AccountNewInboxMessage" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Flagged Content";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>
    <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
        <font class="smallfont">Content is not flagged because you post a specific opinion that somebody disagrees with.  Content is flagged for being illegal, hateful, incomplete, etc.  You have a responsibility to answer honestly and in good faith.  Entering "sdfghj" into an answer box doesn't cut it.  Asking off-topic questions doesn't cut it.  Check each flagged issue... some may simply be warnings.  Nothing happens to your account until cPolice (the content police) looks at it... this protects you from an unruly conversation creator (not that we've had any of those.)  Content flagging is done to protect the quality of conversations.  Which protects you.  Nobody wants to be associated with low quality web junk.</font>
    </div>
    <br/>
    <%

    List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                       .add(Restrictions.eq("useridofcontentcreator", Pagez.getUserSession().getUser().getUserid()))
                                       .addOrder(Order.desc("datelastupdated"))
                                       .setCacheable(true)
                                       .list();
    if (reviews==null || reviews.size()==0) {
        %><font class="normalfont">You have no flagged content... this is a good thing.</font><%
    } else {
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("", "<a href=\"/account/reviewabledetail.jsp?reviewid=<$reviewid$>\">View</a>", false, "", "tinyfont"));
            cols.add(new GridCol("Created", "<$dateofcreation|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", ""));
            cols.add(new GridCol("Last Updated", "<$datelastupdated|"+Grid.GRIDCOLRENDERER_DATETIMEAGOTEXT+"$>", true, "", "tinyfont", "", ""));
        %>
        <%=Grid.render(reviews, cols, 25, "/account/reviewables.jsp", "page")%>
    <%}%>





<%@ include file="/template/footer.jsp" %>


