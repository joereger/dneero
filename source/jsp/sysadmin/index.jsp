<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SysAdmin Home";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <div class="rounded" style="padding: 0px; margin: 10px; background: #33FF00;">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                    <font class="largefont">$<h:outputText value="#{systemStats.dollarsavailabletobloggers}" styleClass="largefont"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText></font>
                    <br/>
                    <font class="mediumfont">waiting to be earned</font>
                </div>
               </td>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                    <font class="largefont">#{systemStats.totalusers}</font>
                    <br/>
                    <font class="mediumfont">users in the system</font>
                </div>
               </td>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                    <font class="largefont">#{systemStats.totalsurveystaken}</font>
                    <br/>
                    <font class="mediumfont">surveys taken by users</font>
                </div>
               </td>
               <td valign="top" width="25%">
                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                    <font class="largefont">#{systemStats.totalimpressions}</font>
                    <br/>
                    <font class="mediumfont">survey results displayed</font>
                </div>
               </td>
           </tr>
        </table>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
        <f:verbatim escape="false">#{sysadminIndex.servermemory}</f:verbatim>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
        <font class="normalfont"><b>Pending Balance:</b> </font>
        <h:outputText value="#{systemStats.systembalance}" styleClass="normalfont"><f:convertNumber type="number" maxFractionDigits="2"/></h:outputText>
        <br/><font class="tinyfont">The amount of accrued balance that users are holding.  A positive number means we are holding this much money for people.  But keep in mind that this also includes money for researchers who are running surveys.</font>
        <br/><br/>
        <font class="normalfont"><b>System Balance Real World:</b> </font>
        <h:outputText value="#{systemStats.systembalancerealworld}" styleClass="normalfont"><f:convertNumber type="number" maxFractionDigits="2"/></h:outputText>
        <br/><font class="tinyfont">Total real world money taken in and sent out.   A negative number means that dNeero has not collected all the money it has sent out... which shouldn't happen.</font>
        <br/><br/>
        <font class="normalfont"><b>System Overall Balance:</b> </font>
        <h:outputText value="#{systemStats.systembalancetotal}" styleClass="normalfont"><f:convertNumber type="number" maxFractionDigits="2"/></h:outputText>
        <br/><font class="tinyfont">Estimate of gross income.</font>
    </div>

    <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
        <f:verbatim escape="false">#{sysadminIndex.financialStatsHtml}</f:verbatim>
    </div>




    </ui:define>


</ui:composition>
</html>