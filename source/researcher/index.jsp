<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/statistic-128.png\" alt=\"\" border=\"0\" width=\"128\" height=\"128\" align=\"right\"/>Conversation Igniters<br/><br clear=\"all\"/>";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherIndex researcherIndex=(ResearcherIndex) Pagez.getBeanMgr().get("ResearcherIndex");
    ResearcherSurveyList researcherSurveyList=(ResearcherSurveyList) Pagez.getBeanMgr().get("ResearcherSurveyList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("copy")) {
        try {
            researcherIndex.copy();
            Pagez.getUserSession().setMessage("Conversation copied!");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
if (Pagez.getUserSession().getIsloggedin() && (Pagez.getUserSession().getUser().getResearcherid()==0) && (!researcherIndex.getShowmarketingmaterial())){
    Pagez.sendRedirect("/researcher/researcherdetails.jsp");
}
%>
<%@ include file="/template/header.jsp" %>


    <% if (!Pagez.getUserSession().getIsloggedin() || researcherIndex.getShowmarketingmaterial()){ %>
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="70%">
                    <div style="margin: 15px;">
                        <font class="mediumfont" style="color: #999999">Executive Summary</font>
                        <br/>
                        dNeero allows you to create multi-question conversations and pay bloggers to both respond to it and post their answers on their blogs.  You can include images, video and rich html in your conversation for product photos, diagrams and whatever else you'd like the blogger to see and post to their blog.  The power in this approach is that respondents know that their answers will be viewed by their peers, a much more powerful force in their lives.  Results from dNeero's methodology give you a better sense of how a concept is portrayed in modern social networks.  dNeero also provides a means to track the movement of responses through small-scale social networks.  It's a new tool in the struggle to understand human behavior.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Major Benefits</font>
                        <br/>
                        Engage bloggers on their own turf for more accurate market data (<b>ethnographic research</b> - bloggers post to their blogs so you'll hear what they tell their peers about your product... not what they'll tell a researcher)
                        <br/><br/>
                        Generate product awareness while you collect data (<b>market research meets marketing</b>) - Each blogger who responds also has friends and family who read their blog.
                        <br/><br/>
                        Low risk, pay as you go: define max budget and <b>only pay for respondents and impressions that actually happen</b>
                        <br/><br/>
                        Quick and easy: <b>set up a campaign online in minutes</b>
                        <br/><br/>
                        <b>Full disclosure</b> that the blogger has been paid avoids misrepresentation and other ethical snafus
                        <br/><br/>
                        Customize the look of the conversation to <b>include logos, product pictures, clickable links</b>, etc... concersations meet guerilla marketing
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How it Works for Researchers</font>
                        <ol>
                        <li><b>Create a survey</b> composed of various question types (Text, Essay, Likert Scale, Matrix, Multiple Choice, etc.)</li>
                        <li><b>Define your target blogger demographic</b> by birthdate, gender, ethnicity, marital status, income, education, state, city, profession and politics.</li>
                        <li><b>Define how much you'll pay</b> for a conversation response and how much you'll pay for 1000 posts of that conversation response to a blog (CPM).  There are many incentive strategies you can implement.</li>
                        <li><b>dNeero will publicize your survey</b> and find bloggers who want to respond to it. You'll pay for only those responses and conversation displays that happen.</li>
                        <li><b>dNeero tracks conversation responses and blog impressions</b>, providing you with simple billing and results data</li>
                        </ol>
                        <br/>
                        <font class="mediumfont" style="color: #999999">dNeero Targets the Thought Leaders... the Outliers</font>
                        <br/>
                        Bloggers, by the nature of their activity, are thought leaders within their social network.  dNeero's concept focuses on these thought leaders.  In Tipping Point terminology these are the Mavens or Salespeople... those who bring knowledge and advocacy to their group.  dNeero is not well-suited to the discovery of trends across entire populations... there are better phone-based and polling-based tools out there for such projects.  dNeero gives you access to an increasingly-potent and somewhat-untouchable segment of society that uses technology to shape the opinions of others.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Frame the Conversation to Get the Data You Need</font>
                        <br/>
                        Other tools like Umbria and BuzzMetrics are great at finding out what people are saying about your product.  But this approach makes it difficult to see how people feel about something in particular.  Often you need to drill down or focus on a specific point.  With dNeero you're able to frame the conversation with questions.  This gets a base set of feedback from all social people who respond to it.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Two Birds with One Stone</font>
                        <br/>
                        Create buzz while you collect valuable market intelligence.  By the nature of dNeero's model you are disclosing your research to the public.  This may not work for some projects.  But for many this is powerful way to see a concept's play in the real world.  It is a two-way interaction between you and your constituents. Books like The Cluetrain Manifesto argue persuasively that the organizations that will succeed in the future are the ones who are most able to embrace a transparent model of interaction with their constituents.  Consumers, voters and members are all screaming for meaningful input into the product design and policy processes of the organizations they patronize.  By creating smart dNeero campaigns that expose strategic elements at the right time while collecting valuable feedback your organization can gain a solid competitive advantage.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Research Data is More Accurate Because Peers are Involved</font>
                        <br/>
                        Research data gained through dNeero is more reflective of how people actually act in their peer group because the answers they provide will be shared on their blog or social network.  In many traditional forms of market research the subject is apt to provide an answer that seems appropriate to the researcher but is not the actual answer that they'll use with their peers.  As a researcher you're much more interested in how they present their beliefs and views to their peers because these views drive market adoption and purchasing decisions.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Include Images in Surveys</font>
                        <br/>
                        Images allow conversation participants to answer questions about a new product design, etc.  And by including an image in the conversation you are actually putting that image onto many blog sites at once.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Social Influence Rating (TM)</font>
                        <br/>
                        Bloggers post their answers to their blogs.  Their readers are given the opportunity to join the same conversation.  We track this influence and incorporate blog traffic with the amount of skewing that the first blogger's answers resulted in (against the norm for the survey) and create a Social Influence Rating.  This helps you zero in on those bloggers that are effective in your space.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Standing Panels of Bloggers for Longitudinal Studies</font>
                        <br/>
                        Create a panel of bloggers and approach them with conversations over time to do longitudinal research in the blogosphere.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">The Charity Only Option</font>
                        <br/>
                        With a single check box you can designate a conversation as Charity Only.  Doing so means that only bloggers who agree to have all of their earnings from the conversation donated to a charity will be able to take it. Learn more about the program <a href="/charity.jsp">here</a>.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Get Started Now, Low Commitment</font>
                        <br/>
                        It's easy to create an account and ignite a conversation.  You won't be charged until you ignite a conversation, at which point it will be obvious that you're about to be charged.  Log in. Explore.  Ask us questions.  It's a new concept and we're excited to see how you apply it!
                    </div>
                    <table width="100%">
                        <tr>
                            <td width="50%" align="center">
                                <div style="width: 200px;"><%=com.dneero.htmlui.GreenRoundedButton.get("<a href=\"/researcher/researcherfaq.jsp\"><font class=\"subnavfont\" style=\"color: #ffffff;\">Read the Researcher FAQ</font></a>")%></div>
                            </td>
                            <td width="50%" align="center">
                                <%if (!Pagez.getUserSession().getIsloggedin()){%>
                                    <div style="width: 200px;"><%=com.dneero.htmlui.GreenRoundedButton.get("<a href=\"/registration.jsp\"><font class=\"subnavfont\" style=\"color: #ffffff;\">Sign Up Now</font></a>")%></div>
                                <%}%>
                            </td>
                        </tr>
                    </table>
               </td>
               <td valign="top" width="30%">
                   <%@ include file="/researcher/about-nav-include.jsp" %>
                   <br/>
               </td>
           </tr>
        </table>
    <% } %>



    <% if (Pagez.getUserSession().getIsloggedin() && (Pagez.getUserSession().getUser().getResearcherid()>0) && (!researcherIndex.getShowmarketingmaterial())){ %>
        <%if (researcherIndex.getMsg()!=null && !researcherIndex.getMsg().equals("")){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont"><%=researcherIndex.getMsg()%></font>
            </div>
        <%}%>

        <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="250" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researchersurveydetail_01.jsp"><font class="mediumfont" style="color: #596697;">Start a New Conversation</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Ignite a new conversation. This step-by-step wizard will guide you through the process.  Your conversation can be up and running in a matter of minutes.</font>
                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/panels.jsp"><font class="mediumfont" style="color: #596697;">Panels</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create and manage standing panels of bloggers for longitudinal studies.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/rank-list.jsp"><font class="mediumfont" style="color: #596697;">Rankings</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create your own ranking index and use it to define/track people across multiple surveys.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researcherdetails.jsp"><font class="mediumfont" style="color: #596697;">Update My Researcher Profile</font></a>
                             </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Help us understand your needs so that we can serve you better.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researcherbilling.jsp"><font class="mediumfont" style="color: #596697;">Billing Info</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Update your billing information on this screen.</font>
                            </td></tr></table>


                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/researcherfaq.jsp"><font class="mediumfont" style="color: #596697;">Researcher Frequently Asked Questions</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Get your answers here!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <a href="/researcher/index.jsp?showmarketingmaterial=1"><font class="mediumfont" style="color: #596697;">Researcher Basic Info</font></a>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Basic Researcher information, how the system works, etc.</font>
                            </td></tr></table>
                        </div>
                    </div>
                </td>
                <td valign="top">
                    <font class="largefont" style="color: #cccccc;">Conversations You've Created</font>
                    <br/>
                    <%if (researcherSurveyList.getSurveys()==null || researcherSurveyList.getSurveys().size()==0){%>
                        <font class="normalfont">You haven't yet created any conversations. <a href="/researcher/researchersurveydetail_01.jsp">Start a New Survey</a>.</font>
                    <%} else {%>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("Title", "<a href=\"/survey.jsp?surveyid=<$survey.surveyid$>\"><font style=\"font-weight:bold;\"><$survey.title$></font></a>", false, "", "normalfont"));
                            cols.add(new GridCol("Status", "<$status$>", false, "", "smallfont", "", ""));
                            cols.add(new GridCol("", "<$editorreviewlink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$invitelink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$resultslink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$copylink$>", false, "", "smallfont"));
                            cols.add(new GridCol("", "<$deletelink$>", false, "", "smallfont"));
                        %>
                        <%=Grid.render(researcherSurveyList.getSurveys(), cols, 50, "/researcher/index.jsp", "page")%>
                    <%}%>

                </td>

             </tr>
         </table>

    <% } %>



<%@ include file="/template/footer.jsp" %>