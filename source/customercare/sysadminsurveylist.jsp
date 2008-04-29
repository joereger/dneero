<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSurveyList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Conversations";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareSurveyList customercareSurveyListList=(CustomercareSurveyList) Pagez.getBeanMgr().get("CustomercareSurveyList");
%>
<%@ include file="/template/header.jsp" %>



    <%if (customercareSurveyListList.getSurveys()==null || customercareSurveyListList.getSurveys().size()==0){%>
        <font class="normalfont">No conversations!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Surveyid", "<$surveyid$>", false, "", "tinyfont"));
            cols.add(new GridCol("Title", "<a href=\"/customercare/sysadminsurveydetail.jsp?surveyid=<$surveyid$>\"><$title$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Displays", "<$publicsurveydisplays$>", false, "", "tinyfont"));
            cols.add(new GridCol("Spotlight?", "<$isspotlight$>", false, "", "tinyfont"));
            cols.add(new GridCol("Status", "<$status$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(customercareSurveyListList.getSurveys(), cols, 200, "/customercare/sysadminsurveylist.jsp", "page")%>
    <%}%>






<%@ include file="/template/footer.jsp" %>



