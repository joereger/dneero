<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.BloggerImpressions" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((BloggerImpressions) Pagez.getBeanMgr().get("BloggerImpressions")).getSurveytitle();
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/template/auth.jsp" %>
<%
    BloggerImpressions bloggerImpressions=(BloggerImpressions) Pagez.getBeanMgr().get("BloggerImpressions");
%>
<%@ include file="/template/header.jsp" %>

    
    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    This page lists blog displays/impressions of a conversation.  Note that in the calculation of earnings some impressions may not be included because the researcher can set a maximum number of paid blog displays per blog.
    <br/><br/><br/>
    </font></div></center>

    <%if (bloggerImpressions.getList()==null || bloggerImpressions.getList().size()==0){%>
        <font class="normalfont">No impressions found.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Referer", "<$referer$>", false, "", "smallfont"));
            cols.add(new GridCol("Total Impressions", "<$impressionstotal$>", false, "", "smallfont"));
        %>
        <%=Grid.render(bloggerImpressions.getList(), cols, 50, "/blogger/impressions.jsp", "page")%>
    <%}%>

<%@ include file="/template/footer.jsp" %>

