<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSurveyList" %>
<%String jspPageName="/researcher/reviewables.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reviewable Items";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherReviewList researcherReviewList=(ResearcherReviewList) Pagez.getBeanMgr().get("ResearcherReviewList");
%>
<%@ include file="/template/header.jsp" %>


    <br/><br/>
    <font class="mediumfont">Pending Review Items</font> <font class="smallfont">(Or, use <a href="/researcher/reviewables-turbo.jsp">Turbo Reviewer</a>)</font><br/>
    <%if (researcherReviewList.getReviewables()==null || researcherReviewList.getReviewables().size()==0){%>
        <font class="normalfont">No new reviewables are waiting.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Type", "<$typeName$>", false, "", "tinyfont"));
            cols.add(new GridCol("", "<a href=\"/researcher/reviewables-turbo.jsp?type=<$type$>&id=<$id$>\"><$shortSummary$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMEAGOTEXT+"$>", true, "", "tinyfont", "", ""));
        %>
        <%=Grid.render(researcherReviewList.getReviewables(), cols, 15, "/researcher/reviewables.jsp", "page")%>
    <%}%>

    <br/><br/>
    <font class="mediumfont">Previous Review Items</font><br/>
    <%
    List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                           .add(Restrictions.eq("useridofresearcher", Pagez.getUserSession().getUser().getResearcherid()))
                                           .addOrder(Order.asc("datelastupdated"))
                                           .setCacheable(true)
                                           .list();
    %>
    <%if (reviews==null || reviews.size()==0){%>
        <font class="normalfont">No previous reviewables exist.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Summary", "<a href=\"/researcher/reviewables-turbo.jsp?type=<$type$>&id=<$id$>\">View</a>", false, "", "tinyfont"));
            cols.add(new GridCol("Created", "<$dateofcreation|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", ""));
            cols.add(new GridCol("Last Updated", "<$datelastupdated|"+Grid.GRIDCOLRENDERER_DATETIMEAGOTEXT+"$>", true, "", "tinyfont", "", ""));
        %>
        <%=Grid.render(reviews, cols, 25, "/researcher/reviewables.jsp", "pageold")%>
    <%}%>






<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>



