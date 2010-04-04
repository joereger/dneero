<%@ page import="com.dneero.htmluibeans.PublicBlogPost" %>
<%@ page import="com.dneero.util.RandomString" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicBlogPost publicBlogPost = (PublicBlogPost) Pagez.getBeanMgr().get("PublicBlogPost");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addcomment")) {
        try {
            ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6Le0RgwAAAAAAG5hYkFqs3xMIAIHIwreTJsPkFGg", "6Le0RgwAAAAAACMo7qCUbKxrPGpB6pgrITwOshzd", false);
            ReCaptchaResponse capResp = captcha.checkAnswer(request.getRemoteAddr(), request.getParameter("recaptcha_challenge_field"), request.getParameter("recaptcha_response_field"));
            if (capResp.isValid()) {
                publicBlogPost.setName(Textbox.getValueFromRequest("name", "Name", false, DatatypeString.DATATYPEID));
                publicBlogPost.setUrl(Textbox.getValueFromRequest("url", "Url", false, DatatypeString.DATATYPEID));
                publicBlogPost.setComment(Textarea.getValueFromRequest("comment", "Comment", true));
                publicBlogPost.postComment();
            } else {
                Pagez.getUserSession().setMessage("Sorry, you need to type the squiggly letters properly.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
if (publicBlogPost==null || publicBlogPost.getBlogpost()==null || publicBlogPost.getBlogpost().getBlogpostid()==0){
    Pagez.sendRedirect("/blog.jsp");
    return;
}
%>
<%@ include file="/template/header.jsp" %>

    <font class="mediumfont" style="color: #0bae17;"><%=publicBlogPost.getBlogpost().getTitle()%></font>
    <br/>
    <font class="smallfont"><%=publicBlogPost.getBlogpost().getBody()%></font>
    <br/>
    <font class="tinyfont" style="color: #cccccc;">Posted by: <%=publicBlogPost.getBlogpost().getAuthor()%> at <%=Time.dateformatcompactwithtime(Time.getCalFromDate(publicBlogPost.getBlogpost().getDate()))%></font>
    <br/><br/>
     <%//@todo turn comments back on%>
     <%if (1==2 && !Pagez.getUserSession().getIsfacebookui()){%>
        <%if (publicBlogPost.getBlogpost()==null || publicBlogPost.getBlogpost().getBlogpostcomments()==null || publicBlogPost.getBlogpost().getBlogpostcomments().size()==0){%>

        <%} else {%>
            <%
                StringBuffer comment=new StringBuffer();
                comment.append("<font class=\"tinyfont\"><$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$></font>\n"+
    "        <font class=\"smallfont\">Comment by: </font>\n" +
    "        <a href=\"<$url$>\">\n" +
    "            <font class=\"smallfont\" style=\"color: #0000ff;\"><$name$></font>\n" +
    "        </a>\n" +
    "        <br/>\n" +
    "        <font class=\"tinyfont\"><$comment$></font>\n" +
    "        <br/><br/>");

                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("", comment.toString(), false, "", ""));
            %>
            <%=Grid.render(Util.setToArrayList(publicBlogPost.getBlogpost().getBlogpostcomments()), cols, 100, "/blogpost.jsp?blogpostid" + publicBlogPost.getBlogpost().getBlogpostid(), "page")%>
        <%}%>

        <form action="/blogpost.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/blogpost.jsp">
            <input type="hidden" name="action" value="addcomment">
            <input type="hidden" name="blogpostid" value="<%=publicBlogPost.getBlogpost().getBlogpostid()%>">
            <br/><br/>
            <font class="formfieldnamefont">Post a comment:</font>
            <br/>
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("name", publicBlogPost.getName(), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Url</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("url", publicBlogPost.getUrl(), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Comment</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("comment", publicBlogPost.getComment(), 5, 40, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Prove You're a Human</font>
                    </td>
                    <td valign="top">
                        <%
                        ReCaptcha captcha = ReCaptchaFactory.newReCaptcha("6Le0RgwAAAAAAG5hYkFqs3xMIAIHIwreTJsPkFGg", "6Le0RgwAAAAAACMo7qCUbKxrPGpB6pgrITwOshzd", false);
                        String captchaScript = captcha.createRecaptchaHtml(request.getParameter("error"), null);
                        out.print(captchaScript);
                        %>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Post Comment">
                    </td>
                </tr>



             </table>

         </form>
     <%}%>

<%@ include file="/template/footer.jsp" %>