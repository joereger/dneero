<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountInboxDetail" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.mail.Mailtype" %>
<%@ page import="com.dneero.mail.MailtypeFactory" %>
<%String jspPageName="/account/inboxdetail.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Inbox";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
AccountInboxDetail accountInboxDetail = (AccountInboxDetail)Pagez.getBeanMgr().get("AccountInboxDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            accountInboxDetail.setNotes(Textarea.getValueFromRequest("notes", "Comments", true));
            accountInboxDetail.newNote();
            Pagez.getUserSession().setMessage("Thanks, your comments have been added.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <font class="mediumfont"><%=accountInboxDetail.getMail().getSubject()%></font>


    <%
        ArrayList<Mailchild> issues=accountInboxDetail.getInboxdetails();
        for (Iterator it=issues.iterator(); it.hasNext();) {
            Mailchild comm=(Mailchild) it.next();
            StringBuffer body = new StringBuffer();
            body.append("<font class=\"tinyfont\">"+Time.dateformatcompactwithtime(Time.getCalFromDate(comm.getDate()))+"</font>");
            body.append("<br/>");
            if (comm.getIsfromcustomercare()){
                body.append("<font class=\"normalfont\" style=\"font-weight: bold;\">From: Admin</font>");
            } else {
                body.append("<font class=\"normalfont\" style=\"font-weight: bold;\">From: You</font>");
            }
            body.append("<br/>");

            Mailtype mt = MailtypeFactory.get(comm.getMailtypeid());
            body.append("<font class=\"normalfont\">"+mt.renderToHtml(comm)+"</font>");

            %>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                <%=body.toString()%>
            </div>
            <%
        }
    %>

    <form action="/account/inboxdetail.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/account/inboxdetail.jsp">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="mailid" value="<%=accountInboxDetail.getMailid()%>">

            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("notes", accountInboxDetail.getNotes(), 3, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Add Comment">
                    </td>
                </tr>

            </table>

    </form>

<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>

