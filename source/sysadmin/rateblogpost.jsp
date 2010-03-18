<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminRateBlogPost" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%@ page import="com.dneero.htmlui.*" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    SysadminRateBlogPost sysadminRateBlogPost = (SysadminRateBlogPost)Pagez.getBeanMgr().get("SysadminRateBlogPost");
    StaticVariables staticVariables = (StaticVariables)Pagez.getBeanMgr().get("StaticVariables");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("rate")) {
        try {
            sysadminRateBlogPost.setQuality(Textbox.getIntFromRequest("quality", "Quality", true, DatatypeInteger.DATATYPEID));
            sysadminRateBlogPost.rateAction();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <%if (sysadminRateBlogPost.getHaveposttoreview()){%>
             <form action="/sysadmin/rateblogpost.jsp" method="post" class="niceform">
                <input type="hidden" name="dpage" value="/sysadmin/rateblogpost.jsp">
                <input type="hidden" name="action" value="rate">
                <input type="hidden" name="impressionid" value="<%=sysadminRateBlogPost.getImpression().getImpressionid()%>">

                <div class="rounded" style="padding: 15px; margin: 5px; background: #00ff00;">
                    <center>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td valign="top">
                                    <font class="largefont">Bad</font>
                                </td>
                                <td valign="top">
                                    <%=Dropdown.getHtml("quality", String.valueOf(sysadminRateBlogPost.getQuality()), staticVariables.getBlogqualities(), "", "")%>
                                </td>
                                <td valign="top">
                                    <font class="largefont">Good</font>
                                </td>
                                <td valign="top">
                                    <img src="/images/clear.gif" width="10" height="1"></img>
                                </td>
                                <td valign="top">
                                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Rate Post">
                                    <br/>
                                    <font class="tinyfont"><%=sysadminRateBlogPost.getRemainingtoreview()%> posts remaining</font>
                                </td>
                            </tr>
                        </table>
                    </center>
                </div>
                <center><font class="tinyfont" style="color: #666666;"><%=sysadminRateBlogPost.getImpression().getReferer()%></font></center>
            </form>
            <br/>
            <font class="normalfont"><%=sysadminRateBlogPost.getIframestr()%></font>

            <%} else {%>
                <font class="normalfont">No posts to review at this time.</font>
            <%}%>



<%@ include file="/template/footer.jsp" %>