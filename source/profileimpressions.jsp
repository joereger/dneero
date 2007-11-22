<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicProfileAnswers" %>
<%@ page import="com.dneero.htmluibeans.PublicProfileImpressions" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/user.png\" align=\"right\" alt=\"\" border=\"0\"/>"+((PublicProfileImpressions) Pagez.getBeanMgr().get("PublicProfileImpressions")).getUser().getFirstname()+" "+ ((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getUser().getLastname()+"'s Impressions<br/><br clear=\"all\"/>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
PublicProfileImpressions publicProfileImpressions = (PublicProfileImpressions) Pagez.getBeanMgr().get("PublicProfileImpressions");
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="mediumfont" style="color: #cccccc;"><%=publicProfileImpressions.getSurvey().getTitle()%></font>
    <br/><br/>
    <%if (publicProfileImpressions.getList()==null || publicProfileImpressions.getList().size()==0){%>
        <font class="normalfont">None... yet.</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Specific Page", "<$referertruncated$>", true, "", "normalfont"));
            cols.add(new GridCol("Impressions", "<$impressionspaidandtobepaid$>", true, "", "normalfont"));
            cols.add(new GridCol("Quality Rating", "<$impressionquality$>", true, "", "normalfont"));
        %>
        <%=Grid.render(publicProfileImpressions.getList(), cols, 10, "profile.jsp?responseid="+publicProfileImpressions.getResponse().getResponseid(), "page")%>
    <%}%>



<%@ include file="/jsp/templates/footer.jsp" %>