<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareUserList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Users";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercareUserList customercareUserListList= (CustomercareUserList)Pagez.getBeanMgr().get("CustomercareUserList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("search")) {
        try {
            customercareUserListList.setSearchemail(Textbox.getValueFromRequest("searchemail", "Email", false, DatatypeString.DATATYPEID));
            customercareUserListList.setSearchfacebookers(CheckboxBoolean.getValueFromRequest("searchfacebookers"));
            customercareUserListList.setSearchfirstname(Textbox.getValueFromRequest("searchfirstname", "Firstname", false, DatatypeString.DATATYPEID));
            customercareUserListList.setSearchlastname(Textbox.getValueFromRequest("searchlastname", "Lastname", false, DatatypeString.DATATYPEID));
            customercareUserListList.setSearchuserid(Textbox.getValueFromRequest("searchuserid", "Userid", false, DatatypeString.DATATYPEID));
            customercareUserListList.initBean();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <form action="/customercare/userlist.jsp" method="post">
        <input type="hidden" name="dpage" value="/customercare/userlist.jsp">
        <input type="hidden" name="action" value="search">
        
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="tinyfont">Userid</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">First Name</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Last Name</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Email</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Facebook?</font>
                </td>
                <td valign="top">

                </td>
            </tr>
            <tr>
                <td valign="top">
                    <%=Textbox.getHtml("searchuserrid", customercareUserListList.getSearchuserid(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchfirstname", customercareUserListList.getSearchfirstname(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchlastname", customercareUserListList.getSearchlastname(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchemail", customercareUserListList.getSearchemail(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("searchfacebookers", customercareUserListList.getSearchfacebookers(), "", "")%>
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Search">
                </td>
            </tr>
        </table>
    </form>

        <br/>

        <%if (customercareUserListList.getUsers()==null || customercareUserListList.getUsers().size()==0){%>
            <font class="normalfont">No users!</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Userid", "<a href=\"/customercare/userdetail.jsp?userid=<$userid$>\"><$userid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Bloggerid", "<$bloggerid$>", false, "", "tinyfont"));
                cols.add(new GridCol("Researcherid", "<$researcherid$>", false, "", "tinyfont"));
                cols.add(new GridCol("Facebookuid", "<$facebookuserid$>", false, "", "tinyfont"));
                cols.add(new GridCol("Name", "<$firstname$> <$lastname$>", false, "", "tinyfont"));
                cols.add(new GridCol("Email", "<$email$>", false, "", "tinyfont"));
                cols.add(new GridCol("Signup Date", "<$createdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
            %>
            <%=Grid.render(customercareUserListList.getUsers(), cols, 200, "/customercare/userlist.jsp", "page")%>
        <%}%>






<%@ include file="/template/footer.jsp" %>



