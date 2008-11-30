<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountInbox" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.AccountNewInboxMessage" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Inbox";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
AccountNewInboxMessage accountNewInboxMessage= (AccountNewInboxMessage) Pagez.getBeanMgr().get("AccountNewInboxMessage");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            accountNewInboxMessage.setSubject(Textbox.getValueFromRequest("subject", "Subject", true, DatatypeString.DATATYPEID));
            accountNewInboxMessage.setNotes(Textarea.getValueFromRequest("notes", "Description", true));
            accountNewInboxMessage.newIssue();
            Pagez.getUserSession().setMessage("Your message has been sent.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>
    <br/><br/>
    <%if (((AccountInbox) Pagez.getBeanMgr().get("AccountInbox")).getInboxmessages() == null || ((AccountInbox) Pagez.getBeanMgr().get("AccountInbox")).getInboxmessages().size() == 0) {%>

    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            //cols.add(new GridCol("Id", "<$mailid$>", true, "", "tinyfont"));
            cols.add(new GridCol("Date", "<$date$>", true, "", "tinyfont", "", "background: #e6e6e6; width:150px;"));
            cols.add(new GridCol("Subject", "<a href=\"/account/inboxdetail.jsp?mailid=<$mailid$>\"><$subject$></a>", false, "", "tinyfont"));
            cols.add(new GridCol("Read?", "<$isread$>", true, "", "tinyfont", "", "background: #e6e6e6; width: 100px;"));
        %>
        <%=Grid.render(((AccountInbox) Pagez.getBeanMgr().get("AccountInbox")).getInboxmessages(), cols, 10, "/account/inbox.jsp", "page")%>
    <%}%>

    <br/>
    <form action="/account/inbox.jsp" method="post">
        <input type="hidden" name="dpage" value="/account/inbox.jsp">
        <input type="hidden" name="action" value="save">
            <br/>
            <font class="mediumfont">Ask a Question. Make an Observation. Recommend an Improvement.</font>
            <br/>
            <font class="smallfont">Use this form to ask us anything at all about your account.  Report bugs.  Tell us where you're confused.  Tell us what could be better.  All communications will be archived and tracked for you here in the Inbox.</font>
            <br/><br/>
            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Subject:</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("subject", accountNewInboxMessage.getSubject(), 255, 30, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Message:</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("notes", accountNewInboxMessage.getNotes(), 3, 40, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton" value="Send Message">
                    </td>
                </tr>

            </table>
        </form>


<%@ include file="/template/footer.jsp" %>


