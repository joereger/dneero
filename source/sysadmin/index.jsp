<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.helpers.UserInputSafe" %>
<%@ page import="com.dneero.privatelabel.TemplateProcessor" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="com.dneero.startup.Log4jLevels" %>
<%@ page import="org.apache.log4j.Level" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SysAdmin Home";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminIndex sysadminIndex=(SysadminIndex) Pagez.getBeanMgr().get("SysadminIndex");
    SystemStats systemStats=(SystemStats) Pagez.getBeanMgr().get("SystemStats");
%>
<%@ include file="/template/header.jsp" %>



    <div class="rounded" style="padding: 0px; margin: 10px; background: #cccccc;">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                    <font class="largefont">$<%=Str.formatForMoney(systemStats.getDollarsavailabletobloggers())%></font>
                    <br/>
                    <font class="mediumfont">waiting to be earned</font>
                </div>
               </td>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                    <font class="largefont"><%=systemStats.getTotalusers()%></font>
                    <br/>
                    <font class="mediumfont">users in the system</font>
                </div>
               </td>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                    <font class="largefont"><%=systemStats.getTotalsurveystaken()%></font>
                    <br/>
                    <font class="mediumfont">conversations joined</font>
                </div>
               </td>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                    <font class="largefont"><%=systemStats.getTotalimpressions()%></font>
                    <br/>
                    <font class="mediumfont">results displayed</font>
                </div>
               </td>
           </tr>
        </table>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
        <%=sysadminIndex.getServermemory()%>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
        <font class="normalfont"><b>Pending Balance:</b> </font>
        <font class="normalfont"><%=Str.formatForMoney(systemStats.getSystembalance())%></font>
        <br/><font class="tinyfont">The amount of accrued balance that users are holding.  A positive number means we are holding this much money for people.  But keep in mind that this also includes money for researchers who are running conversations.</font>
        <br/><br/>
        <font class="normalfont"><b>System Balance Real World:</b> </font>
        <font class="normalfont"><%=Str.formatForMoney(systemStats.getSystembalancerealworld())%></font>
        <br/><font class="tinyfont">Total real world money taken in and sent out.   A negative number means that dNeero has not collected all the money it has sent out... which shouldn't happen.</font>
        <br/><br/>
        <font class="normalfont"><b>System Overall Balance:</b> </font>
        <font class="normalfont"><%=Str.formatForMoney(systemStats.getSystembalancetotal())%></font>
        <br/><font class="tinyfont">Estimate of gross income.</font>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
        <%=sysadminIndex.getFinancialStatsHtml()%>
    </div>




<%@ include file="/template/footer.jsp" %>