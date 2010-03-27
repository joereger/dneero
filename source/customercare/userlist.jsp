<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
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
            customercareUserListList.setSearchname(Textbox.getValueFromRequest("searchname", "Name", false, DatatypeString.DATATYPEID));
            customercareUserListList.setSearchuserid(Textbox.getValueFromRequest("searchuserid", "Userid", false, DatatypeString.DATATYPEID));
            customercareUserListList.setSearchreferredbyuserid(Textbox.getValueFromRequest("searchreferredbyuserid", "Referred By Userid", false, DatatypeString.DATATYPEID));
            customercareUserListList.setSearchsignedupafter(Date.getValueFromRequest("searchsignedupafter", "Signed Up After", false).getTime());
            customercareUserListList.setSearchsignedupbefore(Date.getValueFromRequest("searchsignedupbefore", "Signed Up Before", false).getTime());
            logger.error("calling search() from jsp");
            customercareUserListList.search();
            if (customercareUserListList.getUsers()!=null && customercareUserListList.getUsers().size()==1){
                CustomercareUserListItem cculi = customercareUserListList.getUsers().get(0);
                Pagez.sendRedirect("/customercare/userdetail.jsp?userid="+cculi.getUser().getUserid());
            }
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <form action="/customercare/userlist.jsp" method="post" class="niceform">
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
                    <font class="tinyfont">Email</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Referred By</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Signed up After</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Signed up Before</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Facebook?</font>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <%=Textbox.getHtml("searchuserid", customercareUserListList.getSearchuserid(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchname", customercareUserListList.getSearchname(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchemail", customercareUserListList.getSearchemail(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchreferredbyuserid", customercareUserListList.getSearchreferredbyuserid(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Date.getHtml("searchsignedupafter", customercareUserListList.getSearchsignedupafter(), "", "font-size: 9px;")%>
                </td>
                <td valign="top">
                    <%=Date.getHtml("searchsignedupbefore", customercareUserListList.getSearchsignedupbefore(), "", "font-size: 9px;")%>
                </td>
                <td valign="top">
                    <%=CheckboxBoolean.getHtml("searchfacebookers", customercareUserListList.getSearchfacebookers(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top" colspan="5">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Search">
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
                cols.add(new GridCol("Userid", "<a href=\"/customercare/userdetail.jsp?userid=<$user.userid$>\"><$user.userid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Facebookuid", "<$user.facebookuserid$>", false, "", "tinyfont"));
                cols.add(new GridCol("Name", "<$user.name$>", false, "", "tinyfont"));
                cols.add(new GridCol("Nickname", "<$user.nickname$>", false, "", "tinyfont"));
                cols.add(new GridCol("Email", "<$user.email$>", false, "", "tinyfont"));
                cols.add(new GridCol("Signup Date", "<$user.createdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                cols.add(new GridCol("Referred By Userid", "<a href=\"/customercare/userdetail.jsp?userid=<$user.referredbyuserid$>\"><$user.referredbyuserid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("City", "<$city$>", false, "", "tinyfont"));
                cols.add(new GridCol("State", "<$state$>", false, "", "tinyfont"));
                cols.add(new GridCol("Country", "<$country$>", false, "", "tinyfont"));
            %>
            <%=Grid.render(customercareUserListList.getUsers(), cols, 200, "/customercare/userlist.jsp", "page")%>
        <%}%>






<%@ include file="/template/footer.jsp" %>



