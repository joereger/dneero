<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSurveyList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reviewable Items";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%

    CustomercareReviewList customercareReviewList=(CustomercareReviewList) Pagez.getBeanMgr().get("CustomercareReviewList");
%>
<%@ include file="/template/header.jsp" %>


        <font class="mediumfont">Pending Review Items</font> <font class="smallfont">(Or, use <a href="/customercare/reviewables-turbo.jsp">Turbo Reviewer</a>)</font><br/>
    <%if (customercareReviewList.getReviewables()==null || customercareReviewList.getReviewables().size()==0){%>
        <font class="normalfont">No reviewables.  Go grab a beer and wait 32 seconds for the next reviewable item to come along.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Summary", "<a href=\"/customercare/reviewables-turbo.jsp?type=<$type$>&id=<$id$>\"><$shortSummary$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Researcher Reviewed?", "<$isresearcherreviewed$>", false, "", "tinyfont"));
            cols.add(new GridCol("Sysadmin Reviewed?", "<$issysadminreviewed$>", false, "", "tinyfont"));
            cols.add(new GridCol("Researcher Rejected?", "<$isresearcherrejected$>", false, "", "tinyfont"));
            cols.add(new GridCol("Sysadmin Rejected?", "<$issysadminrejected$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(customercareReviewList.getReviewables(), cols, 200, "/customercare/reviewables.jsp", "page")%>
    <%}%>






<%@ include file="/template/footer.jsp" %>



