<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


<table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="70%">
                    <div style="margin: 15px;">
                        <font class="mediumfont" style="color: #999999">What're Social People?</font>
                        <br/>
                        Anybody who engages in social activity online.  Bloggers, Facebookers, Myspacers and other social network participants.  Social People is a catch-all term.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Quick Summary</font>
                        <br/>
                        We help social people make money joining conversations and posting their opinions where their peers can join in.
                        <br/><br/>
                        After filling out a questions here at this site you've already reserved some money for your effort... but haven't earned it yet.  To capture that money all you have to do is post your answers to your blog and generate some traffic for a few days (all you have to do is copy-and-paste a single line of code).  Your blog readers will see your answers along with any other thoughts you had about the conversation.  Your readers can then join the conversation, or see how others from your blog answered. If they join the conversation and get paid, we pay you a recruitment fee based on what they earn.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How it Works for Social People</font>
                        <br/>
                        You find conversations that interest you.  You answer a set of questions honestly.  You post your answers in a place where your peers can see them (we have a widget that's a simple copy and paste).  Your friends engage in the conversation.  You get paid.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Blogs and Conversations: So Happy Together</font>
                        <br/>
                        To post a conversation to your blog you just copy-and-paste a single line of code.  Here's the result:
                        <br/>
                        <img src="/images/survey-in-blog.gif" width="475" height="555" border="0"></img>
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Avoid Losing Any Credibility... and Support a Great Cause!</font>
                        <br/>
                        One of the big concerns from bloggers is that the money in this model presents possible bias, and that your readers will not want to feel 'monetized'. Thanks to open and sharing people like <a href="http://www.ck-blog.com/">CK</a> we decided to make donating to charity a way to shed this concern -- now you can give any of the earnings to a good cause, avoid bias... and increase the features within your posts!
                        <br/><br/>
                        With a single click you can direct earnings from any conversation to a charity of your choosing.  Learn more about the program <a href="/charity.jsp">here</a>.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How to Get Started</font>
                        <br/>
                        Sign Up is free.  We collect some basics like email and password and then send you an email message to activate your account.  After your first log in we'll collect some basic demographic information (age, gender, location, etc.) so that we can find the best opportunities for you.  We'll present you a list of conversation opportunities, you'll join conversations of your choosing and within minutes you'll see an account balance.  We pay your PayPal account whenever you accrue $20 in your account... no waiting unitl the end of calendar quarters!
                    </div>
                    <table width="100%">
                        <tr>
                            <td width="50%" align="center">
                                <div style="width: 200px;"><%=GreenRoundedButton.get("<a href=\"/blogger/bloggerfaq.jsp\"><font class=\"subnavfont\" style=\"color: #ffffff;\">Read the Blogger FAQ</font></a>")%></div>
                            </td>
                            <td width="50%" align="center">
                                <%if(!Pagez.getUserSession().getIsloggedin()){%>
                                    <div style="width: 200px;"><%=GreenRoundedButton.get("<a href=\"/registration.jsp\"><font class=\"subnavfont\" style=\"color: #ffffff;\">Sign Up Now</font></a>")%></div>
                                <%}%>
                            </td>
                        </tr>
                    </table>
               </td>
               <td valign="top" width="30%">

               </td>
           </tr>
        </table>


        <br/>


<%@ include file="/template/footer.jsp" %>
