<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "About Us";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

    <br/><br/>
    <font class="mediumfont" style="color: #0bae17;"><b>Quick Summary</b></font>
    <br/>
    We're in Atlanta.  Not funded.  Seeking press.  Seeking conversation igniters.  Seeking social people.
    <br/><br/>
    dNeero was created by <a href="http://www.joereger.com">Joe Reger, Jr.</a> who works on it full-time.  Time is also kindly donated by Joe's father, Joe's sister, Barbara Stafford and Lance Weatherby.
    <br/><br/>
    An outgrowth of <a href="http://www.reger.com">Reger.com</a> datablogging, we've been working on dNeero for a little over a year.  We've been involved with the blogosphere as a provider of datablogs since 1999.
    <br/><br/>
    dNeero went into closed beta on April 24th, 2007 and into public beta on June 25th, 2007.  Thanks to all the bloggers, market researchers and business advisors who took the time to beat on the system prior to launch.
    <br/><br/>
    Order a dNeero t-shirt <a href="/tshirt.jsp">here</a>.
    <br/><br/>

    <font class="mediumfont" style="color: #0bae17;"><b>Contact Us</b></font>
    <br/>Always more than happy to give you a demo or answer any questions you may have:
    <br/>
    <br/>Phone: 404.394.6102
    <br/>Email: info@dneero.com



<%@ include file="/template/footer.jsp" %>
