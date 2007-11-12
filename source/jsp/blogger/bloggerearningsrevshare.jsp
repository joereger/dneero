<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.BloggerEarningsRevshare" %>
<%@ page import="com.dneero.htmluibeans.BloggerEarningsRevshareInvite" %>
<%@ page import="com.dneero.htmluibeans.BloggerEarningsRevshareTreeHandler" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.htmluibeans.BloggerEarningsRevshareTreeNode" %>
<%@ page import="com.dneero.util.Str" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Revenue Sharing";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
    BloggerEarningsRevshare bloggerEarningsRevshare=(BloggerEarningsRevshare) Pagez.getBeanMgr().get("BloggerEarningsRevshare");
    BloggerEarningsRevshareInvite bloggerEarningsRevshareInvite=(BloggerEarningsRevshareInvite) Pagez.getBeanMgr().get("BloggerEarningsRevshareInvite");
    BloggerEarningsRevshareTreeHandler bloggerEarningsRevshareTreeHandler=(BloggerEarningsRevshareTreeHandler) Pagez.getBeanMgr().get("BloggerEarningsRevshareTreeHandler");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("invite")) {
        try {
            bloggerEarningsRevshareInvite.setEmail(Textarea.getValueFromRequest("email", "Email", true));
            bloggerEarningsRevshareInvite.setMessage(Textarea.getValueFromRequest("message", "Message", false));
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>



        <%if (bloggerEarningsRevshare.getMsg()!=null && !bloggerEarningsRevshare.getMsg().equals("")){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont"><%=bloggerEarningsRevshare.getMsg()%></font>
            </div>
        <%}%>


        <table cellpadding="0" cellspacing="0" border="0">
            <tr><td valign="top">
            <div style="background: #ffffff; width: 450px; padding: 20px;">
               <font class="mediumfont">Earn With Your Friends</font>
               <br/>
               <font class="smallfont">We'll pay you a piece of any dNeero earnings generated by friends that you invite to the program... out of our own pocket.  When one of your friends accepts the invitation you'll see them listed to the right... and then you'll be making even more money!</font>
               <br/>
               <br/>
               <font class="smallfont">Here's how it works:</font>
               <ul>
                  <li><font class="smallfont">We calculate your friend's earnings</font></li>
                  <li><font class="smallfont">We pay your friend</font></li>
                  <li><font class="smallfont">We pay you up to <%=bloggerEarningsRevshare.getLevel1percent()%>% of your friend's earnings (out of our pocket)</font></li>
                  <li><font class="smallfont">As your friend invites friends we pay you a percentage of your friend's friends earnings (also out of our pocket, see chart to right)</font></li> 
               </ul>

               <br/>
               <br/>
                <form action="bloggerearningsrevshare.jsp" method="post">
                    <input type="hidden" name="action" value="invite">

                    <table cellpadding="0" cellspacing="0" border="0">

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Email Addresses</font>
                                <br/>
                                <font class="normalfont">One per line</font>
                            </td>
                            <td valign="top">
                                <%=Textarea.getHtml("email", bloggerEarningsRevshareInvite.getEmail(), 5, 30, "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Optional Message</font>
                                <br/>
                                <font class="smallfont">We'll automatically include a link for your friend to click to easily sign up.</font>
                            </td>
                            <td valign="top">
                                <%=Textarea.getHtml("message", bloggerEarningsRevshareInvite.getMessage(), 5, 30, "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                            </td>
                            <td valign="top">
                                <input type="submit" value="Invite Friends">
                            </td>
                        </tr>

                    </table>

                </form>
            </div>

           </td>
           <td valign="top">
                <div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px; width: 250px;">
                    <font class="mediumfont">Friends</font>
                    <br/>
                    <font class="smallfont">Listed below are the friends that you've invited (and those that they've invited) to make money using their blog:</font>
                    <br/>
                    <%
                        for (Iterator it=bloggerEarningsRevshareTreeHandler.getTree().iterator(); it.hasNext();) {
                            BloggerEarningsRevshareTreeNode bertn = (BloggerEarningsRevshareTreeNode) it.next();
                            %>
                            <br/><font class="tinyfont"><%=bertn.getDescription()%> ($<%=Str.formatForMoney(bertn.getAmtEarnedFromThisBloggerAllTime())%>)</font>
                            <%
                        }
                    %>
                </div>

                <br/>
                <div class="rounded" style="background: #ffffcc; text-align: left; padding: 20px; width: 250px;">
                <font class="mediumfont">Earn on Your Friend's Friends</font>
                    <br/>
                    <font class="smallfont">You share in the revenue even if your friends do the inviting!  Of course, each level away from you that percentage will be smaller... but those little bits can add up!</font>
                    <br/>
                    <table cellpadding="0" cellspacing="0" border="0">

                        <td valign="top"><h:outputText value=" "></h:outputText></td>
                        <td valign="top"><h:outputText value="You Earn" styleClass="mediumfont"></h:outputText></td>

                        <td valign="top"><h:outputText value="1st Level" styleClass="mediumfont"></h:outputText></td>
                        <td valign="top"><h:outputText value="<%=bloggerEarningsRevshare.getLevel1percent()%> %"></h:outputText></td>

                        <td valign="top"><h:outputText value="2nd Level" styleClass="mediumfont"></h:outputText></td>
                        <td valign="top"><h:outputText value="<%=bloggerEarningsRevshare.getLevel2percent()%> %"></h:outputText></td>

                        <td valign="top"><h:outputText value="3rd Level" styleClass="mediumfont"></h:outputText></td>
                        <td valign="top"><h:outputText value="<%=bloggerEarningsRevshare.getLevel3percent()%> %"></h:outputText></td>

                        <td valign="top"><h:outputText value="4th Level" styleClass="mediumfont"></h:outputText></td>
                        <td valign="top"><h:outputText value="<%=bloggerEarningsRevshare.getLevel4percent()%> %"></h:outputText></td>

                        <td valign="top"><h:outputText value="5th Level" styleClass="mediumfont"></h:outputText></td>
                        <td valign="top"><h:outputText value="<%=bloggerEarningsRevshare.getLevel5percent()%> %"></h:outputText></td>

                     </table>
                 </div>

            </td></tr>
        </table>



<%@ include file="/jsp/templates/footer.jsp" %>


