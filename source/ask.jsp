<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%String jspPageName="/ask.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Ask Twitter Questions";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


<table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="70%">
                    <div style="margin: 15px;">

                        It only takes a few seconds to ask your twitter followers a question and collect their answers on a page.
                        <br/><br/>

                        <font class="mediumfont" style="color: #999999">How it Works</font>
                        <br/>
                        <ol>
                            <li>You <b>ask a question</b></li>
                            <li>We <b>publish the question</b> to your Twitter followers</li>
                            <li>Your followers <b>answer the question</b></li>
                            <li>Your followers' followers see the answer, wonder what it's about and <b>jump in</b></li>
                            <li>We put everybody's answers onto a <b>single page</b></li>
                        </ol>
                        
                        <br/><br/>
                        <a class="sexybutton" href="/registration.jsp?whereToRedirectToAfterSignup=/researcher/index-twitask.jsp"><span><span>Get Started Now!</span></span></a>
                    </div>
               </td>
               <%--<td valign="top" width="30%">--%>
                   <%----%>
               <%--</td>--%>
           </tr>
        </table>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>
