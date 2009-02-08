<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Real World Crossover";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">Drive Real World Results</font>
            <br/>
            Products and services are purchased in the real world.  <%=Pagez.getUserSession().getPl().getNameforui()%> can help you drive real world, trackable results.
            <br clear="all"/><br/><br/>

            <font class="mediumfont" style="color: #999999">Product-in-hand Reviews</font>
            <br/>
            We can handle conversations that only people who you've shipped a product can take.  Along with the product you send them an access code and that code grants them access.
            <br clear="all"/><br/><br/>

            <font class="mediumfont" style="color: #999999">Point of Sale</font>
            <br/>
            Want to know what people think of your new hamburger/product/whatever? And generate buzz for it at the same time? Give them a small handout telling them that they can earn a few dollars reviewing your product with dNeero.  Using an access code, only those who've actually purchased the product can join the conversation.  Word of mouth is a powerful marketer... to take advantage of customers' early excitement with your product.
            <br clear="all"/><br/><br/>


        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>    
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>