<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminUserList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Users";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
SysadminUserList sysadminUserList = (SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("search")) {
        try {
            sysadminUserList.setSearchemail(Textbox.getValueFromRequest("searchemail", "Email", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchfacebookers(CheckboxBoolean.getValueFromRequest("searchfacebookers"));
            sysadminUserList.setSearchfirstname(Textbox.getValueFromRequest("searchfirstname", "Firstname", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchlastname(Textbox.getValueFromRequest("searchlastname", "Lastname", false, DatatypeString.DATATYPEID));
            sysadminUserList.setSearchuserid(Textbox.getValueFromRequest("searchuserid", "Userid", false, DatatypeString.DATATYPEID));
            sysadminUserList.initBean();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <form action="userlist.jsp" method="post">
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
                    <h:inputText value="<%=sysadminUserList.getSearchuserid()%>" id="searchuserrid" size="5"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=sysadminUserList.getSearchfirstname()%>" id="searchfirstname" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=sysadminUserList.getSearchlastname()%>" id="searchlastname" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=sysadminUserList.getSearchemail()%>" id="searchemail" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="<%=sysadminUserList.getSearchfacebookers()%>" id="searchfacebookers"></h:selectBooleanCheckbox>
                </td>
                <td valign="top">
                    <input type="submit" value="Search">
                </td>
            </tr>
        </table>
    </form>

        <br/>

        <%if (sysadminUserList.getUsers()==null || sysadminUserList.getUsers().size()==0){%>
            <font class="normalfont">No users!</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Userid", "<a href=\"userdetail.jsf?userid=<$userid$>\"><$userid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Email", "<$email$>", false, "", "tinyfont"));
                cols.add(new GridCol("Name", "<$firstname$> <$lastname$>", false, "", "tinyfont"));
                cols.add(new GridCol("Signup Date", "<$createdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
            %>
            <%=Grid.render(sysadminUserList.getUsers(), cols, 200, "userlist.jsp", "page")%>
        <%}%>






<%@ include file="/jsp/templates/footer.jsp" %>



