<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "One-Time Configuration is Complete!";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/header.jsp" %>



            <font class="smallfont">With your profile and blog set up, you're ready to make some money!</font>
            <br/><br/>
            <h:commandLink value="Click Here to Continue" action="<%=((BloggerIndex)Pagez.getBeanMgr().get("BloggerIndex")).getBeginView()%>" style="color: #0000ff;" styleClass="mediumfont"/>


<%@ include file="/jsp/templates/footer.jsp" %>