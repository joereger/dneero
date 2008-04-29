<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSurveyList" %>
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



    <%if (researcherReviewList.getReviewables()==null || researcherReviewList.getReviewables().size()==0){%>
        <font class="normalfont">No reviewables are waiting.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Summary", "<a href=\"/researcher/reviewabledetail.jsp?type=<$type$>&id=<$id$>\"><$shortSummary$></a>", false, "", "tinyfont"));
        %>
        <%=Grid.render(researcherReviewList.getReviewables(), cols, 100, "/researcher/reviewables.jsp", "page")%>
    <%}%>






<%@ include file="/template/footer.jsp" %>



