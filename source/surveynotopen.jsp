<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%String jspPageName="/surveynotopen.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = Pagez._Survey()+" Not Available";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


    <center>
        <div style="width: 50%;">
            <center>
                <img src="/images/info-128.png" alt="" width="128" height="128"/>
                <br/>
                <font class="mediumfont">The <%=Pagez._survey()%> you've selected isn't available.  It may not have launched yet.  Or it may be closed.</font>
                <br/><br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center;">
                    <a href="/publicsurveylist.jsp"><font class="mediumfont" style="padding-left: 15px; color: #0000ff;">Find <%=Pagez._Survey()%> to Join</font></a>
                </div>
            </center>
        </div>
    </center>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>


