<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSupportIssueDetail" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.dao.Supportissuecomm" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issue Detail";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareSupportIssueDetail customercareSupportIssueDetailtail=(CustomercareSupportIssueDetail) Pagez.getBeanMgr().get("CustomercareSupportIssueDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            customercareSupportIssueDetailtail.setStatus(Dropdown.getValueFromRequest("status", "Status", true));
            customercareSupportIssueDetailtail.setNotes(Textarea.getValueFromRequest("notes", "Notes", false));
            customercareSupportIssueDetailtail.newNote();
            Pagez.sendRedirect("/customercare/sysadminsupportissueslist.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


            <div class="rounded" style="padding: 10px; margin: 10px; background: #33FF00;">
                <font class="mediumfont"><%=customercareSupportIssueDetailtail.getSupportissue().getSubject()%></font>
            </div>



            <%
            for (Iterator<Supportissuecomm> iterator=customercareSupportIssueDetailtail.getSupportissuecomms().iterator(); iterator.hasNext();){
                Supportissuecomm supportissuecomm = iterator.next();
                %>
                <div class="rounded" style="padding: 10px; margin: 10px; background: #e6e6e6;">
                    <font class="smallfont" style="font-weight: bold;"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(supportissuecomm.getDatetime()))%></font>
                    <br/>
                    <%if (!supportissuecomm.getIsfromdneeroadmin()){%>
                        <a href="/customercare/userdetail.jsp?userid=<%=customercareSupportIssueDetailtail.getFromuser().getUserid()%>"><font class="smallfont" style="font-weight: bold;">From: <%=customercareSupportIssueDetailtail.getFromuser().getFirstname()%> <%=customercareSupportIssueDetailtail.getFromuser().getLastname()%></font></a>
                    <%} else {%>
                        <font class="smallfont" style="font-weight: bold;">dNeero Admin</font>
                    <%}%>
                    <% if (customercareSupportIssueDetailtail.getFromuser().getFacebookuserid()>0){ %>
                        <font class="smallfont" style="font-weight: bold;">(Facebook User)</font>
                    <% } %>
                    <br/>
                    <font class="smallfont"><%=supportissuecomm.getNotes()%></font>
                </div>
                <%
            }
            %>



        <form action="/customercare/sysadminsupportissuedetail.jsp" method="post">
            <input type="hidden" name="dpage" value="/customercare/sysadminsupportissuedetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="supportissueid" value="<%=customercareSupportIssueDetailtail.getSupportissueid()%>">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <%=Textarea.getHtml("notes", customercareSupportIssueDetailtail.getNotes(), 8, 72, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <%
                            TreeMap<String, String> options=new TreeMap<String, String>();
                            options.put("0", "Open");
                            options.put("1", "Working");
                            options.put("2", "Closed");
                        %>
                        <br/><%=Dropdown.getHtml("status", String.valueOf(customercareSupportIssueDetailtail.getStatus()), options, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton" value="Add a Comment">
                    </td>
                </tr>

            </table>

        </form>





<%@ include file="/template/footer.jsp" %>



