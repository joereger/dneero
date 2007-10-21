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
<strong><font face=arial size=+1 color="#000000">We're sorry.  We must be updating the software at this time.  We're always trying to launch new features to make the site more powerful.  We apologize for the invconvenience.  Please try again soon.</font></strong>
</blockquote></blockquote>
<br>
</body>
</html>

