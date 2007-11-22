<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminBlogpost" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Blog Posting";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
SysadminBlogpost sysadminBlogpost = (SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminBlogpost.setAuthor(Textbox.getValueFromRequest("author", "Author", true, DatatypeString.DATATYPEID));
            sysadminBlogpost.setBody(Textarea.getValueFromRequest("body", "Body", true));
            sysadminBlogpost.setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            sysadminBlogpost.setCategories(Textbox.getValueFromRequest("categories", "Categories", false, DatatypeString.DATATYPEID));
            sysadminBlogpost.save();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            sysadminBlogpost.delete();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <form action="blogpost.jsp" method="post">
        <input type="hidden" name="action" value="save">

        <table cellpadding="0" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Date</font>
                </td>
                <td valign="top">
                    <t:inputDate value="<%=sysadminBlogpost.getDate()%>" type="both" popupCalendar="true" id="date" required="true"></t:inputDate>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Author</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=sysadminBlogpost.getAuthor()%>" size="45" id="author" required="true"></h:inputText>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Title</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=sysadminBlogpost.getTitle()%>" size="75" id="title" required="true"></h:inputText>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Body</font>
                </td>
                <td valign="top">
                    <h:inputTextarea value="<%=sysadminBlogpost.getBody()%>" id="body" required="true" cols="75" rows="10"></h:inputTextarea>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Categories</font>
                    <br/>
                    <font class="smallfont">(comma-separated)</font>
                </td>
                <td valign="top">
                    <h:inputText value="<%=sysadminBlogpost.getCategories()%>" size="75" maxlength="250" id="categories" required="false"></h:inputText>
                </td>
            </tr>

            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" value="Save!">
                    <%if (sysadminBlogpost.getBlogpostid()>0){%>
                        <br/><br/>
                        <a href="blogpost.jsp?blogpostid=<%=sysadminBlogpost.getBlogpostid()%>&action=delete"><font class="tinyfont">Delete</font></a>
                    <%}%>
                </td>
            </tr>

        </table>
    </form>



    <br/><br/>

    <%if (sysadminBlogpost.getBlogposts()==null || sysadminBlogpost.getBlogposts().size()==0){%>
        <font class="normalfont">No blog posts</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols=new ArrayList<GridCol>();
            cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "smallfont"));
            cols.add(new GridCol("Title", "<a href=\"blogpost.jsp?blogpostid<$blogpostid$>\"><$title$></a>", true, "", "smallfont"));
            cols.add(new GridCol("Author", "<$author$>", true, "", "smallfont"));
        %>
        <%=Grid.render(sysadminBlogpost.getBlogposts(), cols, 50, "blogpost.jsp", "page")%>
    <%}%>






<%@ include file="/jsp/templates/footer.jsp" %>


