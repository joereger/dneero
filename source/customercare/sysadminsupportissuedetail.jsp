<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSupportIssueDetail" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.dao.Supportissuecomm" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.mail.Mailtype" %>
<%@ page import="com.dneero.mail.MailtypeFactory" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issue Detail";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareSupportIssueDetail customercareSupportIssueDetail=(CustomercareSupportIssueDetail) Pagez.getBeanMgr().get("CustomercareSupportIssueDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            customercareSupportIssueDetail.setNotes(Textarea.getValueFromRequest("notes", "Notes", false));
            customercareSupportIssueDetail.setKeepflaggedforcustomercare(CheckboxBoolean.getValueFromRequest("keepflaggedforcustomercare"));
            customercareSupportIssueDetail.newNote();
            Pagez.sendRedirect("/customercare/sysadminsupportissueslist.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


            <div class="rounded" style="padding: 10px; margin: 10px; background: #33FF00;">
                <font class="mediumfont"><%=customercareSupportIssueDetail.getMail().getSubject()%></font>
            </div>



            <%
            List<Mailchild> mailchildren = HibernateUtil.getSession().createQuery("from Mailchild where mailid='"+customercareSupportIssueDetail.getMail().getMailid()+"' order by mailchildid asc").list();
                for (Iterator<Mailchild> mailchildIterator=mailchildren.iterator(); mailchildIterator.hasNext();) {
                    Mailchild mailchild=mailchildIterator.next();
                    Mailtype mt = MailtypeFactory.get(mailchild.getMailtypeid());
                    %>
                    <div class="rounded" style="padding: 10px; margin: 10px; background: #e6e6e6;">
                        <font class="smallfont" style="font-weight: bold;"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(mailchild.getDate()))%></font>
                        <br/>
                        <%if (!mailchild.getIsfromcustomercare()){%>
                            <a href="/customercare/userdetail.jsp?userid=<%=customercareSupportIssueDetail.getFromuser().getUserid()%>"><font class="smallfont" style="font-weight: bold;">From: <%=customercareSupportIssueDetail.getFromuser().getFirstname()%> <%=customercareSupportIssueDetail.getFromuser().getLastname()%></font></a>
                        <%} else {%>
                            <font class="smallfont" style="font-weight: bold;">System Admin</font>
                        <%}%>
                        <% if (customercareSupportIssueDetail.getFromuser().getFacebookuserid()>0){ %>
                            <font class="smallfont" style="font-weight: bold;">(Facebook User)</font>
                        <% } %>
                        <br/>
                        <font class="smallfont"><%=mt.renderToHtml(mailchild)%></font>
                    </div>
                    <%
                }

            %>



        <form action="/customercare/sysadminsupportissuedetail.jsp" method="post">
            <input type="hidden" name="dpage" value="/customercare/sysadminsupportissuedetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="mailid" value="<%=customercareSupportIssueDetail.getMailid()%>">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <%=Textarea.getHtml("notes", customercareSupportIssueDetail.getNotes(), 8, 72, "", "")%>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("keepflaggedforcustomercare", false, "", "")%>
                        <font class="formfieldnamefont">Keep Flagged for Customer Care</font>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton" value="Add Comment">
                    </td>
                </tr>

            </table>

        </form>





<%@ include file="/template/footer.jsp" %>



