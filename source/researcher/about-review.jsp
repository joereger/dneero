<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%String jspPageName="/researcher/about-review.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Content Review";
String navtab = "researchers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

<table cellpadding="10" cellspacing="2" border="0" width="100%">
    <tr>
        <td valign="top">
            <font class="mediumfont" style="color: #999999">Removing Truly Ugly Stuff</font>
            <br/>
            One concern with social media is always that people will post truly ugly things.  You need some ability to fight that stuff.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">A Two-Tier System to Defend Social Media</font>
            <br/>
            You can't have carte blanche control over any content that you don't like.  That's just not how we roll.  In fact, we're all about free speech and empowering social people to say what's on their minds. We've built the review system so that you can recommend content to reject but you can't actually reject it.  We'll review your rejections and if we see someting that's illegal or truly ugly we'll remove it or issue a warning.  We feel it's incredibly important to not allow companies the freedom to censor the social people that they engage.  If you expect to reject bad product reviews and have us agree with you then you're our of your mind.  We'll only remove stuff that's so horrible it's clear that it needs to come down.
            <br/><br/><br/>
            <font class="mediumfont" style="color: #999999">Rejection Guidelines</font>
            <br/>
            There are only a few reasons that content will be rejected:
            <ul>
                <li>Illegal content</li>
                <li>Copyright violations</li>
                <li>Abusive posts based on race, religion, gender, sexual orientation or other personal attributes</li>
                <li>Spam or junk postings</li>
                <li>Threats of physical abuse</li>
                <li>Other obviously inappropriate material</li>
            </ul>
            Spam, vulgar hate stuff, porn... it'll all come down.  Negative product reviews, things your company doesn't want to hear, a slam of your recent press release... it'll all stay up.  It's the grey area where somebody likens your product to a Nazi hooker that creates issues... what's free speech and what's vulgar?  We need to flesh this out more but we do hope it's clear that we will almost always fall on the side of the social people, not on the side of companies.  Yes, we'll lose sales because of this.  And then we'll win them back when they realize we're about integrity.  Side note: we're open to the idea of a third party mediating the review process... somebody like Chilling Effects... if you're in that arena let us know.
        </td>
        <td valign="top" width="30%">
            <%@ include file="/researcher/about-nav-include.jsp" %>
            <br/>
        </td>
    </tr>
</table>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>