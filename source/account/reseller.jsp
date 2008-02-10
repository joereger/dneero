<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ChangePassword" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.AccountReseller" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "dNeero Reseller Program";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
    AccountReseller accountReseller = (AccountReseller) Pagez.getBeanMgr().get("AccountReseller");
%>
<%
//if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
//    try {
//        //accountReseller.saveAction();
//        Pagez.getUserSession().setMessage("Something happened.");
//        Pagez.sendRedirect("/account/index.jsp");
//        return;
//    } catch (ValidationException vex) {
//        Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
//    }
//}
%>
<%@ include file="/template/header.jsp" %>

<%
    String resellerpercentStr = String.valueOf(SurveyMoneyStatus.RESELLERPERCENTDEFAULT);
    if (Pagez.getUserSession().getUser().getResellerpercent()>0){
        resellerpercentStr = String.valueOf(Pagez.getUserSession().getUser().getResellerpercent());
    }
%>


<br/><br/>

<table cellpadding="0" cellspacing="0" border="0" width="100%">

    <tr>
        <td valign="top">
            <%@ include file="/reseller-description.jsp" %>   
        </td>
        <td valign="top" width="35%">
            <div class="rounded" style="background: #cccccc; text-align: left; padding: 15px;">
                <center>
                <div class="rounded" style="background: #ffffff; text-align: left; padding: 15px;">
                    <center>
                        <font class="smallfont" style="color: #666666;"><b><%=Pagez.getUserSession().getUser().getFirstname()%> <%=Pagez.getUserSession().getUser().getLastname()%>'s</b></font>
                        <br/>
                        <font class="mediumfont" style="color: #000000;"><b>Reseller Code</b></font>
                        <br/>
                        <font class="largefont" style="color: #666666;"><%=Pagez.getUserSession().getUser().getResellercode()%></font>
                        <br/>
                        <font class="smallfont" style="color: #999999;"><b>You earn <%=resellerpercentStr%>%</b></font>
                    </center>
                </div>
                </center>
            </div>
        </td>
    </tr>





</table>


<%@ include file="/template/footer.jsp" %>

