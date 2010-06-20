<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicOldSurveyList" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Old "+ Pagez._Surveys();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicOldSurveyList publicOldSurveyList = (PublicOldSurveyList) Pagez.getBeanMgr().get("PublicOldSurveyList");
%>
<%@ include file="/template/header.jsp" %>

    <%if (publicOldSurveyList.getSurveys()==null || publicOldSurveyList.getSurveys().size()==0){%>
        <font class="normalfont">No old <%=Pagez._Surveys()%> listed right now... check back soon.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol(Pagez._Survey()+" Name", "<a href=\"/surveyresults.jsp?surveyid=<$surveyid$>\"><$title$></a><br/><font class=\"tinyfont\"><$description$></font>", false, "", "normalfont", "", "font-weight: bold;"));
            //cols.add(new GridCol("Respondents Earned Up To", "<$maxearning$>", true, "", "smallfont"));
            //cols.add(new GridCol("Number of Respondents", "<$numberofrespondents$>", true, "", "smallfont"));
        %>
        <%=Grid.render(publicOldSurveyList.getSurveys(), cols, 50, "/publicoldsurveylist.jsp", "page")%>
    <%}%>

<%@ include file="/template/footer.jsp" %>



