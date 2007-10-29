<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/user.png\" align=\"right\" alt=\"\" border=\"0\"/>#{publicProfileAnswers.user.firstname} #{publicProfileAnswers.user.lastname}'s Answers<br/><br clear=\"all\"/>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="mediumfont" style="color: #cccccc;">#{publicProfileAnswers.survey.title}</font>
    <br/><br/>




<f:verbatim escape="false">#{publicProfileAnswers.resultsashtml}</f:verbatim>

    
</h:form>

</ui:define>


</ui:composition>
</html>