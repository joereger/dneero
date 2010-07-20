<%@ page import="com.dneero.helpers.IsBloggerInPanel" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%@ page import="java.util.Iterator" %>
<%String jspPageName="/superpanels.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SuperPanels";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("dosomething")) {
        try {

        } catch (Exception ex) {
            Pagez.getUserSession().setMessage("Sorry, there's been an error.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<table cellpadding="5" cellspacing="5" border="0" width="100%">
    <tr>
        <td valign="top">
            <%
            String emptyStr = "";
            List sps= HibernateUtil.getSession().createQuery("from Panel where issystempanel=true order by panelid asc"+emptyStr).list();
            for (Iterator iterator = sps.iterator(); iterator.hasNext();) {
                Panel panel = (Panel) iterator.next();
                %>
                <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 10px;">
                    <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                        <font class="mediumfont"><%=panel.getName()%></font>
                    </div>
                    <div class="rounded" style="background: #e6e6e6; padding: 10px; text-align: left;">
                        <font class="normalfont"><%=panel.getDescription()%></font>
                    </div>
                    <div class="rounded" style="background: #e6e6e6; padding: 8px; text-align: left;">
                        <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){%>
                            <%if (!IsBloggerInPanel.isBloggerInPanel(panel, Blogger.get(Pagez.getUserSession().getUser().getBloggerid()))){%>
                                <%
                                int applicationsPending = NumFromUniqueResult.getInt("select count(*) from Panelapplication where userid='"+Pagez.getUserSession().getUser().getUserid()+"' and panelid='"+panel.getPanelid()+"' and issysadminreviewed=false");
                                if (applicationsPending>0){
                                %>
                                    <font class="smallfont" style="font-weight: bold;">Application Submitted for Review</font>
                                <%} else {%>
                                    <a href="/blogger/superpanels-apply.jsp?panelid=<%=panel.getPanelid()%>"><font class="smallfont" style="font-weight: bold;">Apply for Membership</font></a>
                                <%}%>
                            <%} else {%>
                                <font class="smallfont" style="font-weight: bold;">You're Already a Member!</font>
                            <%}%>
                        <%} else {%>
                            <font class="smallfont" style="font-weight: bold;">Login/Signup to Apply!</font>
                        <%}%>
                    </div>
                </div>
                <br/>
                <%
            }
            %>
        </td>
        <td valign="top" width="35%">
            <div class="rounded" style="background: #33FF00; text-align: left; padding: 10px;">
                <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                    <font class="mediumfont" style="color: #cccccc;">What are SuperPanels?</font>
                    <br/>
                    <font class="normalfont" style="font-weight: bold;">SuperPanels are groups of social people who focus on specific things.  These bloggers are the best of the best... the shining stars of social media who generate thoughtful <%=Pagez._surveys()%>.  Marketers who work with SuperPanels opt for quality over quantity.  They want to make sure they're reaching high quality social media participants who invest time in their content and provide value every time.</font>
                </div>
                <br/>
                <font class="smallfont">

                <b>Who's in the SuperPanels?</b>
                <br/>Experts in their fields.  Enthusiasts.  Mavens.  Fans.  Social People who engage in valuable discussion about the area of focus.
                <br/><br/>

                <b>How do they get in?</b>
                <br/>Some are invited.  Some apply for membership.  We review every member of every SuperPanel to make sure they're high quality contributors to the social plane.
                <br/><br/>

                <b>How do I apply?</b>
                <br/>Sign up and become a social person.  Come back to this page and click to Apply for Membership.  We'll ask you to succinctly describe why you feel you belong on the SuperPanel and then we'll review your posting venues and past performance.
                <br/><br/>

                <b>What are the benefits?</b>
                <br/>Access to exclusive, targeted <%=Pagez._surveys()%>.  Researchers who want to focus their efforts.  More earnings.  Increased Social Influence Rating.
                <br/><br/>

                </font>
            </div>
        </td>
    </tr>
</table>



<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>

