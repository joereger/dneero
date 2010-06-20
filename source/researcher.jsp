<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Create "+ Pagez._surveys();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


<table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="70%">
                    <div style="margin: 15px;">

                        It only takes a few minutes to spin up a <%=Pagez._survey()%> about anything.  Your weekend plans.  A tv show.  An event.
                        <br/><br/>

                        <font class="mediumfont" style="color: #999999">How it Works</font>
                        <br/>
                        <ol>
                            <li>You <b>create a set of questions</b></li>
                            <li>We <b>publish your questions</b> as a survey on the site</li>
                            <li>Bloggers and social networkers <b>answer the questions</b></li>
                            <li>Bloggers and social networkers <b>post their own answers to their blogs and social networks</b></li>
                            <li>Their friends see your questions, their friend's answers and can <b>jump in</b></li>
                            <li>You get <b>buzz and market intelligence</b> (plus, it's fun!)</li>
                        </ol>


                        <br/><br/>
                        <a class="sexybutton" href="/registration.jsp?whereToRedirectToAfterSignup=/researcher/index.jsp"><span><span>Get Started Now!</span></span></a>
                    </div>
               </td>
               <td valign="top" width="30%">
                   <%//@ include file="/researcher/about-nav-include.jsp" %>
                   <br/>
               </td>
           </tr>
        </table>


<%@ include file="/template/footer.jsp" %>
