<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Not Available";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


    <center>
        <div style="width: 50%;">
            <center>
                <img src="/images/info-128.png" alt="" width="128" height="128"/>
                <br/>
                <font class="mediumfont">The survey you've selected isn't available.  It may not have launched yet.  Or it may be closed.</font>
                <br/><br/>
                <div class="rounded" style="background: #e6e6e6; text-align: center;">
                    <a href="publicsurveylist.jsp"><font class="mediumfont" style="padding-left: 15px; color: #0000ff;">Find Surveys to Take</font></a>
                </div>
            </center>
        </div>
    </center>


<%@ include file="/jsp/templates/footer.jsp" %>

