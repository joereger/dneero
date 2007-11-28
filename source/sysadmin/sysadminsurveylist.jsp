<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.SysadminSurveyList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Surveys";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminSurveyList sysadminSurveyList=(SysadminSurveyList) Pagez.getBeanMgr().get("SysadminSurveyList");
%>
<%@ include file="/template/header.jsp" %>



    <%if (sysadminSurveyList.getSurveys()==null || sysadminSurveyList.getSurveys().size()==0){%>
        <font class="normalfont">No surveys!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Surveyid", "<$surveyid$>", false, "", "tinyfont"));
            cols.add(new GridCol("Title", "<a href=\"/sysadmin/sysadminsurveydetail.jsp?surveyid=<$surveyid$>\"><$title$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Displays", "<$publicsurveydisplays$>", false, "", "tinyfont"));
            cols.add(new GridCol("Spotlight?", "<$isspotlight$>", false, "", "tinyfont"));
            cols.add(new GridCol("Status", "<$status$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(sysadminSurveyList.getSurveys(), cols, 200, "/sysadmin/sysadminsurveylist.jsp", "page")%>
    <%}%>






<%@ include file="/template/footer.jsp" %>



