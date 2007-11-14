<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicOldSurveyList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Old Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
PublicOldSurveyList publicOldSurveyList = (PublicOldSurveyList) Pagez.getBeanMgr().get("PublicOldSurveyList");
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <%if (publicOldSurveyList.getSurveys()==null || publicOldSurveyList.getSurveys().size()==0){%>
        <font class="normalfont">No old surveys listed right now... check back soon.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Survey Name", "<a href=\"survey.jsp?surveyid=<$surveyid$>&show=results\"><$title$></a><br/><font class=\"tinyfont\"><$description$></font>", false, "", "normalfont", "", "font-weight: bold; color: #0000ff;"));
            cols.add(new GridCol("Respondents Earned Up To", "<$maxearning$>", true, "", "smallfont"));
            cols.add(new GridCol("Number of Respondents", "<$numberofrespondents$>", true, "", "smallfont"));
        %>
        <%=Grid.render(publicOldSurveyList.getSurveys(), cols, 50, "publicoldsurveylist.jsp", "page")%>
    <%}%>

<%@ include file="/jsp/templates/footer.jsp" %>



