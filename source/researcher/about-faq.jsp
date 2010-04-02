<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Charity Capabilities";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">Remove Any Concern Over Bias</font>
            <br/>
            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Why is this better than traditional market research?</font>
            <br/>
            <font class="smallfont">
            One critical flaw with traditional market research, especially when targeted to the youth-oriented market of the blogosphere, is that respondents know that they're speaking to a researcher and speak accordingly.  You're not hearing what they say behind closed doors with their friends.  With dNeero, however, social people know that their answers will be seen by their peers who are a much stronger influencer than you are as a researcher.  The result is that you see a much more accurate portrayal of your product in the real world.  This is ethnographic research applied to technological social networks.  Peek into the digital world that bloggers inhabit.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Can I use it for free?</font>
            <br/>
            <font class="smallfont">
            Absolutely!  There's no requirement that you create an incentive for participants.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Can I limit my budget?</font>
            <br/>
            <font class="smallfont">
            You set a maximum spending limit when you create a conversation.  You're only accountable for that amount or less.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">How much does it cost to get started?</font>
            <br/>
            <font class="smallfont">
            You can create conversations for less than $100 by choosing low incentives and low numbers of required respondents.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">What if nobody joins my conversation?</font>
            <br/>
            <font class="smallfont">
            You don't pay.  This is a low-risk technology.  If you want to, you can try again with a higher payment incentive for bloggers.  You only pay for the activity that your conversation generates.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Can I embed product images into my conversation?</font>
            <br/>
            <font class="smallfont">
            Yes.  This is a very powerful concept in our technology.  You can add a product photo (or many) that will appear when social people join your conversation and when they post it to their blog.  Imagine having 5000 blogs spread a picture of your product along with their opinion on it.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Can I require positive reviews/opinions about my product?</font>
            <br/>
            <font class="smallfont">
            No way, Jose!  The blogosphere and market research are both built on honesty and integrity.  We will do everything in our power to maintain neutrality for our bloggers.  Doing so enhances the credibility of your marketing and market research.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">What do I get as conversation results?</font>
            <br/>
            <font class="smallfont">
            Both graphical and computer-formatted results are available.  Charts and graphs.  Percentages.  All the usuall stuff you'd expect.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Can everybody see the results?</font>
            <br/>
            <font class="smallfont">
            Yes!  The network effect is embraced.  People affect one another all the time in the real world.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Can I hide the results?</font>
            <br/>
            <font class="smallfont">
            You can, but you have to pay a 5% premium to do so.  When you create your conversation there's a simple check box to do so.  We'll hide the overall aggregate results so that competitors don't get the benefit of your investment.  And per-blog aggregate results will still be available.  It's a balance between your needs as a researcher and the value for bloggers.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">Can I visit the blogs where my conversation is posted?</font>
            <br/>
            <font class="smallfont">
            Absolutely!  When you see your results you'll get a list of those blogs where the conversation was posted along with the number of times that the conversation has been seen.  The powerful thing here is that you can get a much more palpable sense of what your customers/constituents are saying to their friends.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">How much does it cost?</font>
            <br/>
            <font class="smallfont">
            You can create a conversation as large or as small as you like and you'll be charged only for the activity that your conversation generates.  <%=Pagez.getUserSession().getPl().getNameforui()%> charges a 25% conversation management fee to use our technology.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">How/when am I charged?</font>
            <br/>
            <font class="smallfont">
            You're always in control of what's happening with your account.  When you create a conversation you cap the maximum amount that you can be charged.  Before you commit to any spend you are shown a clear breakdown of the maximum spending scenario.  Within the bounds that you design, here's what we do:
            <ul>
            <li>When you create your conversation you're charged 20% of the total possible value of the conversation.  This means 20% of the Max Possible Spend if you get as many respondents as you like and as many blog displays as you like.</li>
            <li>Additional charges will be made in 20% increments whenever your account balance falls below 10% of the sum of Max Possible Spend for all open conversations.  </li>
            <li>If your balance falls below 5% of the sum of Max Possible Spend values for all open conversations, then all conversations will be put on hold until your balance is increased.</li>
            </ul>
            </font>
            <br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">What if I still have a positive balance when my conversation ends?</font>
            <br/>
            <font class="smallfont">
            In general we attempt to fulfill your conversation order completely, meaning you'll have a zero balance.  But occasionally short-run conversations are closed while there's a positive balance in your account.  Impressions on blogs will be paid for during a period extending 30 days from the end date of the conversation, not to exceed the limits on impressions that you've set with your conversation.  As such, <%=Pagez.getUserSession().getPl().getNameforui()%> will refund money after this period has ended so long as you don't have other conversations that aren't Closed.  This means that you need to have a PayPal account set up and configured in the Account Settings section of your account.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">What payment methods do you accept?</font>
            <br/>
            <font class="smallfont">
            Using your account online you can specify a credit card (Visa, MasterCard, Amex, Discover).  However, for large conversations please contact us directly to discuss other arrangements.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->

            <!-- Start FAQ Question -->
            <font class="mediumfont" color="#333333">How many conversations a day is a blogger allowed to take?</font>
            <br/>
            <font class="smallfont">
            Five.
            </font>
            <br/><br/><br/>
            <!-- End FAQ Question -->
        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %>