<%@ page import="com.dneero.dao.Pl" %>
<%@ page import="com.dneero.privatelabel.PlVerification" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Private Label";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    String defaultWebHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/header-dneero.vm").toString();
    String defaultWebFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/footer-dneero.vm").toString();
%>
<%
    Pl pl = new Pl();
    pl.setWebhtmlheader(defaultWebHeader);
    pl.setWebhtmlfooter(defaultWebFooter);
    if (request.getParameter("plid")!=null && Num.isinteger(request.getParameter("plid"))){
        pl = Pl.get(Integer.parseInt(request.getParameter("plid")));
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("save")){
                pl.setName(Textbox.getValueFromRequest("name", "Name", true, DatatypeString.DATATYPEID));
                pl.setNameforui(Textbox.getValueFromRequest("nameforui", "Name For UI", true, DatatypeString.DATATYPEID));
                pl.setCustomdomain1(Textbox.getValueFromRequest("customdomain1", "Customdomain1", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setCustomdomain2(Textbox.getValueFromRequest("customdomain2", "Customdomain2", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setCustomdomain3(Textbox.getValueFromRequest("customdomain3", "Customdomain3", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setSubdomain(Textbox.getValueFromRequest("subdomain", "subdomain", false, DatatypeString.DATATYPEID).toLowerCase());
                pl.setWebhtmlheader(Textarea.getValueFromRequest("webhtmlheader", "Web Html Header", false));
                pl.setWebhtmlfooter(Textarea.getValueFromRequest("webhtmlfooter", "Web Html Footer", false));
                pl.setEmailhtmlheader(Textarea.getValueFromRequest("emailhtmlheader", "Email Html Header", false));
                pl.setEmailhtmlfooter(Textarea.getValueFromRequest("emailhtmlfooter", "Email Html Footer", false));
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    Pagez.getUserSession().setMessage("Saved!");
                    Pagez.sendRedirect("/sysadmin/privatelabels.jsp");
                    return;
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setwebhtmlheadertodefault")){
                pl.setWebhtmlheader(defaultWebHeader);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("setwebhtmlfootertodefault")){
                pl.setWebhtmlfooter(defaultWebFooter);
                //Validate data
                if (PlVerification.isValid(pl)){
                    pl.save();
                    Pagez.getUserSession().setMessage("Done!");
                } else {
                    Pagez.getUserSession().setMessage("Pl Fails Validation!");
                }
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



        <form action="/sysadmin/privatelabeledit.jsp" method="post">
            <input type="hidden" name="dpage" value="/sysadmin/privatelabeledit.jsp">
            <input type="hidden" name="action" value="save" id="action">
            <input type="hidden" name="plid" value="<%=pl.getPlid()%>">

            <table cellpadding="3" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("name", pl.getName(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name for UI</font>
                        <br/><font class="tinyfont">Ex("dNeero")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("nameforui", pl.getNameforui(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Subdomain</font>
                        <br/><font class="tinyfont">Ex("www", "someconame")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("subdomain", pl.getSubdomain(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain1</font>
                        <br/><font class="tinyfont">Ex("www.mypldomain.com")</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain1", pl.getCustomdomain1(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain2</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain2", pl.getCustomdomain2(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Customdomain3</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("customdomain3", pl.getCustomdomain3(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Web Html Header</font>
                        <br/><font class="tinyfont">Uses <a href="http://velocity.apache.org/engine/releases/velocity-1.5/vtl-reference-guide.html">VTL</a></font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setwebhtmlheadertodefault">Set to Default</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("webhtmlheader", pl.getWebhtmlheader(), 8, 80, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Web Html Footer</font>
                        <br/><font class="tinyfont"><a href="/sysadmin/privatelabeledit.jsp?plid=<%=pl.getPlid()%>&action=setwebhtmlfootertodefault">Set to Default</a></font>
                        <br/><font class="tinyfont">(Set to blank to always use system default)</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("webhtmlfooter", pl.getWebhtmlfooter(), 8, 80, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email Html Header</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("emailhtmlheader", pl.getEmailhtmlheader(), 8, 80, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email Html Footer</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("emailhtmlfooter", pl.getEmailhtmlfooter(), 8, 80, "", "")%>
                    </td>
                </tr>

             </table>
             <input type="submit" class="formsubmitbutton" value="Save"><br/>




        </form>



<%@ include file="/template/footer.jsp" %>



