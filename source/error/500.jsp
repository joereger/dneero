<%@ page isErrorPage="true" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.util.ErrorDissect" %>

<%
    try {
        Logger logger=Logger.getLogger(this.getClass().getName());
        logger.error("", exception);
    } catch (Throwable e) {
        e.printStackTrace();
    }
%>

<html>
<head>
<title>Oops!</title>
</head>
<body bgcolor=#ffffff>
<br><br>
<blockquote><blockquote>
<img src="/images/alert.png" alt=""/>
<br/>
<strong><font face=arial size=+1 color="#666666">We're sorry.  We're updating our system.</font></strong><br/>
<font face=arial size=-1 color="#666666">We're always trying to launch new features to make the site more powerful.  We apologize for the invconvenience.  Please try again soon.</font>
</blockquote></blockquote>
<br>
<%
if (1==2){
    String errorAsString = ErrorDissect.dissect(exception);
    %>
        <blockquote><blockquote><%=errorAsString%></blockquote></blockquote>
    <%
}
%>
</body>
</html>

