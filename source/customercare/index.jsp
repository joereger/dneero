<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.dneero.cache.html.DbcacheexpirableCache" %>
<%@ page import="com.dneero.iptrack.IptrackAnalyzer" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Customer Care";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%

%>
<%@ include file="/template/header.jsp" %>



<%
    long openSupportIssues = NumFromUniqueResult.getInt("select count(*) from Mail where isflaggedforcustomercare=true");
    long openReviewItems = NumFromUniqueResult.getInt("select count(*) from Review where (isresearcherrejected=true or isresearcherwarned=true) and issysadminreviewed=false");
%>


<div class="rounded" style="padding: 0px; margin: 10px; background: #33FF00;">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                    <font class="largefont"><%=openSupportIssues%></font>
                    <br/>
                    <font class="mediumfont">open <a href="/customercare/sysadminsupportissueslist.jsp">support issues</a></font>
                </div>
               </td>
               <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                    <font class="largefont">~<%=openReviewItems%></font>
                    <br/>
                    <font class="mediumfont">items for <a href="/customercare/reviewables-turbo.jsp">review</a></font>
                </div>
               </td>
               <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                    <font class="largefont">??</font>
                    <br/>
                    <font class="mediumfont">questionable <a href="/customercare/iptrack.jsp">ips</a></font>
                </div>
               </td>
           </tr>
        </table>
    </div>







<%@ include file="/template/footer.jsp" %>