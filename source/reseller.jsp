<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = Pagez.getUserSession().getPl().getNameforui()+" Reseller Program";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


<table cellpadding="10" cellspacing="0" border="0" width="100%">

    <tr>
        <td valign="top">
            <%@ include file="/reseller-description.jsp" %>
        </td>
        <td valign="top" width="35%">
            <br/><br/>
            <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
                <font class="mediumfont" style="color: #666666;"><b>Work Together</b></font>
                <br/>
                <font class="smallfont" style="color: #666666;"><b>We recently set up a <a href="http://www.facebook.com/group.php?gid=8641922193">Conversation Resellers Group</a> on Facebook where you can meet others using the program.  Ask questions, share sales strategies, learn more.</b></font>
            </div>

            <br/><br/>
            <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
                <font class="mediumfont" style="color: #666666;"><b>Ask a Question</b></font>
                <br/>
                <font class="smallfont" style="color: #666666;"><b>Once you sign up for a <%=Pagez.getUserSession().getPl().getNameforui()%> account you can always use the <a href="/account/inbox.jsp">Help System</a> to ask us a question.</b></font>
            </div>
        </td>
    </tr>

</table>


<%@ include file="/template/footer.jsp" %>
