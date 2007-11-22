<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminSupportIssueDetail" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.dao.Supportissuecomm" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Support Issue Detail";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
    SysadminSupportIssueDetail sysadminSupportIssueDetail=(SysadminSupportIssueDetail) Pagez.getBeanMgr().get("SysadminSupportIssueDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminSupportIssueDetail.setStatus(Dropdown.getValueFromRequest("status", "Status", true));
            sysadminSupportIssueDetail.setNotes(Textarea.getValueFromRequest("notes", "Notes", false));
            sysadminSupportIssueDetail.newNote();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>


            <div class="rounded" style="padding: 0px; margin: 10px; background: #33FF00;">
                <font class="mediumfont"><%=sysadminSupportIssueDetail.getSupportissue().getSubject()%></font>
            </div>



            <%
            for (Iterator<Supportissuecomm> iterator=sysadminSupportIssueDetail.getSupportissuecomms().iterator(); iterator.hasNext();){
                Supportissuecomm supportissuecomm = iterator.next();
                %>
                <div class="rounded" style="padding: 0px; margin: 10px; background: #e6e6e6;">
                    <font class="smallfont" style="font-weight: bold;"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(supportissuecomm.getDatetime()))%></font>
                    <br/>
                    <%if (!supportissuecomm.getIsfromdneeroadmin()){%>
                        <a href="userdetail.jsp?userid=<%=sysadminSupportIssueDetail.getFromuser().getUserid()%>"><font class="smallfont" style="font-weight: bold;">From: <%=sysadminSupportIssueDetail.getFromuser().getFirstname()%> <%=sysadminSupportIssueDetail.getFromuser().getLastname()%></font></a>
                    <%} else {%>
                        <font class="smallfont" style="font-weight: bold;">dNeero Admin</font>
                    <%}%>
                    <% if (sysadminSupportIssueDetail.getFromuser().getFacebookuserid()>0){ %>
                        <font class="smallfont" style="font-weight: bold;">(Facebook User)</font>
                    <% } %>
                    <br/>
                    <font class="smallfont"><%=supportissuecomm.getNotes()%></font>
                </div>
                <%
            }
            %>



        <form action="sysadminsupportissuedetail.jsp" method="post">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="supportissueid" value="<%=sysadminSupportIssueDetail.getSupportissueid()%>">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <%=Textarea.getHtml("notes", sysadminSupportIssueDetail.getNotes(), 8, 72, "", "")%>
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
                        <br/><%=Dropdown.getHtml("status", String.valueOf(sysadminSupportIssueDetail.getStatus()), options, "", "")%>
                        <h:selectOneMenu value="<%=sysadminSupportIssueDetail.getStatus()%>" id="status" required="true">
                            <f:selectItem  itemValue="0" itemLabel="Open"></f:selectItem>
                            <f:selectItem  itemValue="1" itemLabel="Working"></f:selectItem>
                            <f:selectItem  itemValue="2" itemLabel="Closed"></f:selectItem>
                        </h:selectOneMenu>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <input type="submit" value="Add a Comment">
                    </td>
                </tr>

            </table>

        </form>





<%@ include file="/jsp/templates/footer.jsp" %>



