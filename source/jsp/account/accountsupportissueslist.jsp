<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.AccountSupportIssuesList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.AccountNewSupportIssue" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issues";
String navtab = "youraccount";
String acl = "account";
%>
<%
AccountNewSupportIssue accountNewSupportIssue = (AccountNewSupportIssue) Pagez.getBeanMgr().get("AccountNewSupportIssue");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            accountNewSupportIssue.setSubject(Textbox.getValueFromRequest("subject", "Subject", true, DatatypeString.DATATYPEID));
            accountNewSupportIssue.setNotes(Textarea.getValueFromRequest("notes", "Issue Description", true));
            accountNewSupportIssue.newIssue();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>
    <br/><br/>
    <%if (((AccountSupportIssuesList) Pagez.getBeanMgr().get("AccountSupportIssuesList")).getSupportissues() == null || ((AccountSupportIssuesList) Pagez.getBeanMgr().get("AccountSupportIssuesList")).getSupportissues().size() == 0) {%>

    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Id", "<$supportissueid$>", true, "", "tinyfont"));
            cols.add(new GridCol("Date", "<$datetime$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
            cols.add(new GridCol("Subject", "<a href=\"accountsupportissuedetail.jsp?supportissueid=<$supportissueid$>\"><$subject$></a>", false, "", "tinyfont"));
        %>
        <%=Grid.render(((AccountSupportIssuesList) Pagez.getBeanMgr().get("AccountSupportIssuesList")).getSupportissues(), cols, 10, "accountsupportissueslist.jsp", "page")%>
    <%}%>


    <form action="accountsupportissueslist.jsp" method="post">
        <input type="hidden" name="action" value="save">
            <br/>
            <font class="mediumfont">Ask a Question. Make an Observation. Recommend an Improvement.</font>
            <br/>
            <font class="smallfont">Use this form to ask us anything at all about your account.  Report bugs.  Tell us where you're confused.  Tell us what could be better.  All communications will be archived and tracked for you here in the support section.</font>
            <br/><br/>
            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Subject:</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("subject", accountNewSupportIssue.getSubject(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Issue Description:</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("notes", accountNewSupportIssue.getNotes(), 3, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton" value="Create a New Support Issue">
                    </td>
                </tr>

            </table>
        </form>


<%@ include file="/jsp/templates/footer.jsp" %>


