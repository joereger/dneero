<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%String jspPageName="/researcher/about-execsum.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Overview of "+Pagez._Survey()+" Creation";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">Executive Summary</font>
            <br/>
            <%=Pagez.getUserSession().getPl().getNameforui()%> allows you to create multi-question <%=Pagez._surveys()%> and pay bloggers to both respond to it and post their answers on their blogs.  You can include images, video and rich html in your <%=Pagez._survey()%> for product photos, diagrams and whatever else you'd like the blogger to see and post to their blog.  The power in this approach is that respondents know that their answers will be viewed by their peers, a much more powerful force in their lives.  Results from our methodology give you a better sense of how a concept is portrayed in modern social networks.  <%=Pagez.getUserSession().getPl().getNameforui()%> also provides a means to track the movement of responses through small-scale social networks.  It's a new tool in the struggle to understand human behavior.
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
            Customize the look of the <%=Pagez._survey()%> to <b>include logos, product pictures, clickable links</b>, etc... concersations meet guerilla marketing
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">How it Works for Researchers</font>
            <ol>
            <li><b>Create a <%=Pagez._survey()%></b> composed of various question types (Text, Essay, Likert Scale, Matrix, Multiple Choice, etc.)</li>
            <li><b>Define your target blogger demographic</b> by birthdate, gender, ethnicity, marital status, income, education, state, city, profession and politics.</li>
            <li><b>Define how much you'll pay</b> for a <%=Pagez._survey()%> response and how much you'll pay for 1000 posts of that <%=Pagez._survey()%> response to a blog (CPM).  There are many incentive strategies you can implement.</li>
            <li><b><%=Pagez.getUserSession().getPl().getNameforui()%> will publicize your <%=Pagez._survey()%></b> and find bloggers who want to respond to it. You'll pay for only those responses and <%=Pagez._survey()%> displays that happen.</li>
            <li><b><%=Pagez.getUserSession().getPl().getNameforui()%> tracks <%=Pagez._survey()%> responses and blog impressions</b>, providing you with simple billing and results data</li>
            </ol>
            <br/>
            <font class="mediumfont" style="color: #999999"><%=Pagez.getUserSession().getPl().getNameforui()%> Targets the Thought Leaders... the Outliers</font>
            <br/>
            Bloggers, by the nature of their activity, are thought leaders within their social network.  Our concept focuses on these thought leaders.  In Tipping Point terminology these are the Mavens or Salespeople... those who bring knowledge and advocacy to their group.  <%=Pagez.getUserSession().getPl().getNameforui()%> is not well-suited to the discovery of trends across entire populations... there are better phone-based and polling-based tools out there for such projects.  <%=Pagez.getUserSession().getPl().getNameforui()%> gives you access to an increasingly-potent and somewhat-untouchable segment of society that uses technology to shape the opinions of others.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Frame the <%=Pagez._Survey()%> to Get the Data You Need</font>
            <br/>
            Other tools like Umbria and BuzzMetrics are great at finding out what people are saying about your product.  But this approach makes it difficult to see how people feel about something in particular.  Often you need to drill down or focus on a specific point.  With <%=Pagez.getUserSession().getPl().getNameforui()%> you're able to frame the <%=Pagez._survey()%> with questions.  This gets a base set of feedback from all social people who respond to it.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Two Birds with One Stone</font>
            <br/>
            Create buzz while you collect valuable market intelligence.  By the nature of our model you are disclosing your research to the public.  This may not work for some projects.  But for many this is powerful way to see a concept's play in the real world.  It is a two-way interaction between you and your constituents. Books like The Cluetrain Manifesto argue persuasively that the organizations that will succeed in the future are the ones who are most able to embrace a transparent model of interaction with their constituents.  Consumers, voters and members are all screaming for meaningful input into the product design and policy processes of the organizations they patronize.  By creating smart <%=Pagez.getUserSession().getPl().getNameforui()%> campaigns that expose strategic elements at the right time while collecting valuable feedback your organization can gain a solid competitive advantage.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Research Data is More Accurate Because Peers are Involved</font>
            <br/>
            Research data gained through <%=Pagez.getUserSession().getPl().getNameforui()%> is more reflective of how people actually act in their peer group because the answers they provide will be shared on their blog or social network.  In many traditional forms of market research the subject is apt to provide an answer that seems appropriate to the researcher but is not the actual answer that they'll use with their peers.  As a researcher you're much more interested in how they present their beliefs and views to their peers because these views drive market adoption and purchasing decisions.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Include Images in <%=Pagez._Survey()%></font>
            <br/>
            Images allow <%=Pagez._survey()%> participants to answer questions about a new product design, etc.  And by including an image in the <%=Pagez._survey()%> you are actually putting that image onto many blog sites at once.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Social Influence Rating (TM)</font>
            <br/>
            Bloggers post their answers to their blogs.  Their readers are given the opportunity to join the same <%=Pagez._survey()%>.  We track this influence and incorporate blog traffic with the amount of skewing that the first blogger's answers resulted in (against the norm for the <%=Pagez._survey()%>) and create a Social Influence Rating.  This helps you zero in on those bloggers that are effective in your space.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Standing Panels of Bloggers for Longitudinal Studies</font>
            <br/>
            Create a panel of bloggers and approach them with <%=Pagez._surveys()%> over time to do longitudinal research in the blogosphere.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">The Charity Only Option</font>
            <br/>
            With a single check box you can designate a <%=Pagez._survey()%> as Charity Only.  Doing so means that only bloggers who agree to have all of their earnings from the <%=Pagez._survey()%> donated to a charity will be able to take it. Learn more about the program <a href="/charity.jsp">here</a>.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Get Started Now, Low Commitment</font>
            <br/>
            It's easy to setup an account and create a <%=Pagez._survey()%>.
        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>