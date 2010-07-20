<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveys" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveysListitem" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.helpers.IsBloggerInPanel" %>
<%String jspPageName="/blogger/superpanels-apply.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Apply for SuperPanel";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/template/auth.jsp" %>
<%
Panel panel = null;
if (request.getParameter("panelid")!=null && Num.isinteger(request.getParameter("panelid"))) {
    panel = Panel.get(Integer.parseInt(request.getParameter("panelid")));
}
if (panel==null || panel.getPanelid()<=0 || !panel.getIssystempanel()){
    Pagez.sendRedirect("/superpanels.jsp");
    return;
}
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("apply")) {
        try {
            if (CheckboxBoolean.getValueFromRequest("venuessuitable")){
                Panelapplication panelapplication = new Panelapplication();
                panelapplication.setApplicationdate(new java.util.Date());
                panelapplication.setIssysadminrejected(false);
                panelapplication.setIssysadminreviewed(false);
                panelapplication.setPanelid(panel.getPanelid());
                panelapplication.setApplication(Textarea.getValueFromRequest("application", "Why You're Qualified", true));
                panelapplication.setSysadmincomments("");
                panelapplication.setUserid(Pagez.getUserSession().getUser().getUserid());
                panelapplication.save();
                Pagez.getUserSession().setMessage("Your application has been received and will be reviewed shortly.");
                Pagez.sendRedirect("/superpanels.jsp");
                return;
            } else {
                Pagez.getUserSession().setMessage("Sorry, you must agree that your venues are suitable.");
            }
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        } catch (Exception ex) {
            Pagez.getUserSession().setMessage("Sorry, there's been an error.  Please try again.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<blockquote>
<form action="/blogger/superpanels-apply.jsp" method="post" class="niceform">
    <input type="hidden" name="dpage" value="/blogger/superpanels-apply.jsp">
    <input type="hidden" name="action" value="apply">
    <input type="hidden" name="panelid" value="<%=panel.getPanelid()%>">


    <font class="mediumfont"><%=panel.getName()%></font>
    <br/>
    <font class="tinyfont"><%=panel.getDescription()%></font>

    <br/><br/>
    <font class="normalfont" style="font-weight: bold;">Why are you qualified for this SuperPanel?<br/>What about your blog readership is compelling?<br/>What do you blog about?<br/>If you have multiple Posting Venues, which one is most relevant?<br/>Please make your case for membership in a few sentences.</font>
    <br/>
    <%
    String applicationStr = "";
    if (request.getParameter("application")!=null){
        applicationStr = request.getParameter("application");    
    }
    %>
    <%=Textarea.getHtml("application", applicationStr, 6, 50, "", "width: 90%;")%>
    <br/><br/>

    <%--<font class="normalfont" style="font-weight: bold;">Your Posting Venues</font>--%>
    <%--<br/>--%>
    <%
        Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
        for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
            Venue venue=iterator.next();
            if (venue.getIsactive()){
                %>
                <font class="tinyfont" style="">http://<%=Str.truncateString(venue.getUrl(), 40)%> (<%=venue.getFocus()%>)</font><br/>
                <%
            }
        }
    %>
    <%=CheckboxBoolean.getHtml("venuessuitable", false, "", "")%><font class="normalfont" style="font-weight: bold;">One or more of my Posting Venues is suitable for this SuperPanel</font>
    <br/>
    <br/>
    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Apply to SuperPanel">

</form></blockquote>





<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>

