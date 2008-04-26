<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ChangePassword" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.AccountReseller" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
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

<table cellpadding="10" cellspacing="0" border="0" width="100%">

    <tr>
        <td valign="top">
            <%@ include file="/reseller-description.jsp" %>   
        </td>
        <td valign="top" width="37%">
            <div class="rounded" style="background: #0bae17; text-align: left; padding: 15px;">
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

            <br/>
            <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
                <font class="mediumfont" style="color: #666666;"><b>Conversations</b></font>
                <br/>
                <%if (accountReseller.getSurveys()==null || accountReseller.getSurveys().size()==0){%>
                    <font class="smallfont" style="color: #666666;"><b>No conversations have been created using your Reseller Code.</b></font>
                <%} else {%>
                    <font class="smallfont" style="color: #666666;"><b>These are the conversations that were created with your Reseller Code.</b></font>
                    <br/>
                    <%
                        ArrayList<GridCol> cols = new ArrayList<GridCol>();
                        cols.add(new GridCol("", "<b><a href=\"/survey.jsp?surveyid=<$surveyid$>\"><$title$></a>", false, "", "tinyfont", "", ""));
                    %>
                    <%=Grid.render(accountReseller.getSurveys(), cols, 20, "/account/reseller.jsp", "pagesurveys")%>
                <%}%>
            </div>

            <br/>
            <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
                <font class="mediumfont" style="color: #666666;"><b>Reseller Earnings</b></font>
                <br/>
                <%if (accountReseller.getBalances()==null || accountReseller.getBalances().size()==0){%>
                    <font class="smallfont" style="color: #666666;"><b>You haven't yet earned anything from the Reseller Program.</b></font>
                <%} else {%>
                    <%
                        ArrayList<GridCol> cols = new ArrayList<GridCol>();
                        cols.add(new GridCol("", "<b><$date|" + Grid.GRIDCOLRENDERER_DATETIMECOMPACT + "$></b><br/><$description$>", false, "", "tinyfont", "", ""));
                        cols.add(new GridCol("", "<$amt$>", true, "", "tinyfont"));
                    %>
                    <%=Grid.render(accountReseller.getBalances(), cols, 50, "/account/reseller.jsp", "pagebalances")%>
                <%}%>
            </div>

            <br/>
            <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
                <font class="mediumfont" style="color: #666666;"><b>Work Together</b></font>
                <br/>
                <font class="smallfont" style="color: #666666;"><b>We recently set up a <a href="http://www.facebook.com/group.php?gid=8641922193">dNeero Conversation Resellers Group</a> on Facebook where you can meet others using the program.  Ask questions, share sales strategies, learn more.</b></font>
            </div>

            <br/>
            <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
                <font class="mediumfont" style="color: #666666;"><b>Ask a Question</b></font>
                <br/>
                <font class="smallfont" style="color: #666666;"><b>You can always use the <a href="/account/accountsupportissueslist.jsp">Help System</a> to ask us a question.</b></font>
            </div>


        </td>
    </tr>

</table>


<%@ include file="/template/footer.jsp" %>

