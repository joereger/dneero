<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Balance FAQ";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How do I get paid?</font>
    <br/>
    <font class="smallfont">
    For security and ease-of-use we pay your PayPal account.  Setting up a <a href="http://www.paypal.com" target="paypal">PayPal account</a> takes minutes.  Once you have a PayPal account you simply enter your PayPal address (usually your email address) into the system on your account settings page so that we can send you money.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">When do I get paid?</font>
    <br/>
    <font class="smallfont">
    When your balance reaches $20 or more.  We pay people early in the morning (Eastern Standard Time, US) every few days.  Once your balance reaches $20 expect to be paid in 2-5 days. Look for it in your PayPal account.  If you don't see it, send us a note and we'll track it down for you!
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->



    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">What's a Pending Balance?</font>
    <br/>
    <font class="smallfont">
    When you join a <%=Pagez._survey()%> you still have a little more work to do.  You have to post it to your blog or social network where it needs to generate impressions for five of the next ten days.  While impressions are collecting the money is kept as a Pending Balance.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How long do I get paid for <%=Pagez._survey()%> impressions in my blog?</font>
    <br/>
    <font class="smallfont">
    Some <%=Pagez._surveys()%> include a payment incentive for participation. You get paid until you reach the limit set by the <%=Pagez._survey()%> creator (shown when you decide whether to join).  Or until 30 days have elapsed since the end (also listed on the <%=Pagez._survey()%>).
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">My balance is lower than I expected.  Why?</font>
    <br/>
    <font class="smallfont">
    Pay for joining certain <%=Pagez._surveys()%> comes in two ways.  First, you get paid to answer questions, post to your blog or social network and get impressions for five days.  Second, you get paid for impressions.  Each survey has a different ratio of pay between these two, as set up by the <%=Pagez._survey()%> creator.  You can see the breakdown before you join the <%=Pagez._survey()%>... it's listed on the right hand side of the screen.  So, to get paid more people generally need to get more impressions.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How long does my post need to stay up?</font>
    <br/>
    <font class="smallfont">
    To get paid for a <%=Pagez._survey()%> you must accrue some impressions (people to see your blog with the <%=Pagez._survey()%> in it) for 5 of the first 10 days following the date that you join the <%=Pagez._survey()%>.   Of course, you want to keep your <%=Pagez._survey()%> up at all times because the more impressions you get the more money you make.  We also reserve the right to not pay you if we believe that you're gaming the system, only putting <%=Pagez._surveys()%> up once a day, automating impressions, etc.  By taking money for a <%=Pagez._survey()%> you're committing to posting it where your peers can see it.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Can I get paid some way other than PayPal?</font>
    <br/>
    <font class="smallfont">
    Not at this time.  But tell us what you're thinking and we'll certainly consider it.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->


<%@ include file="/template/footer.jsp" %>