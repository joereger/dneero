<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.BloggerEarningsRevshare" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "dNeero Facebook App FAQ";
String navtab = "bloggers";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>




    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Is this thing for real?</font>
    <br/>
    <font class="smallfont">
    It is.  We're transparent about what we're asking you to do to get paid.  Other pay-for-survey companies dangle an always-vanishing carrot in front of you and ask you to do a ton of crazy stuff.  We pay people daily.  Most people who review us say something like "one of the only things I tried that actually pays."
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Ok, I've taken a survey... when do I get paid?</font>
    <br/>
    <font class="smallfont">
    Our advertisers and market researchers pay for answers (from you) and clicks (from your friends). To qualify for payment, as described many places on the app, you must generate 5 days with clicks in the first 10 after you take the survey. A click is generated when a friend sees the surveys listed on your profile box or mini feed and clicks on it. On your completed surveys dNeero page you'll see a list of surveys you've taken and a day-by-day status report telling you how close you are to being paid. Once you get paid for a number of surveys and accrue more than $20 we'll pay your PayPal account. We pay people every day and are generally regarded as one of the few apps that does actually pay.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Sometimes I'm using the dNeero Facebook App and I find myself outside of Facebook and on dNeero.com... what's that all about?</font>
    <br/>
    <font class="smallfont">
    Sometimes it's just that you left the dNeero Facebook App on your screen for a while without clicking anything.  But sometimes it just happens randomly.  This random behavior drives us nuts.  It appears to be an issue with the way that Internet Explorer handles cookies in iFrames.  It's often exacerbated if you have an account at dNeero.com and have the Keep Me Logged In checked.  To remedy this situation (or at least make it somewhat better):
    <ul>
        <li>Go to http://www.dneero.com</li>
        <li>If you're logged-in, click Log Out (upper right)</li>
        <li>Go back to http://www.facebook.com and re-enter the dNeero Facebook App</li>
    </ul>
    We're sorry for this issue... like we said... it drives us nuts too.  We've tried many things to fix it automatically for you but somehow it just seems to creep back into the mix.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Sometimes I click something and it takes me back to the main page... what's that all about?</font>
    <br/>
    <font class="smallfont">
    Same as the question above.  It drives us nuts and we've done a lot to try to prevent it but some browsers just do this from time to time.  We'll keep working on it.  Apologies for the inconvenience.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Do I need to have friends take the survey to get paid?</font>
    <br/>
    <font class="smallfont">
    No, they don't need to take it.  But they do need to see your answers.  And to do that they need to install the dNeero Facebook App.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Do I need to post positive reviews?</font>
    <br/>
    <font class="smallfont">
    Absolutely not!  You should post whatever you actually believe. dNeero will not allow advertisers to bias you in any way.  You are being paid for your time and your opinion... not for any particular opinion.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">What actually gets posted to my profile?</font>
    <br/>
    <font class="smallfont">
    A link to your mini-feed that says something like "Joe Reger took the survey 'Some Cool Survey' and earned $3.45."  We also list your surveys in a profile box.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How long does my post need to stay up?</font>
    <br/>
    <font class="smallfont">
    To get paid for a survey you must accrue at least one impression (one person needs to click from your profile) for 5 of the first 10 days following the date that you take the survey.   Of course, you want to keep your survey up at all times because the more impressions you get the more money you make.  We also reserve the right to not pay you if we believe that you're gaming the system, only putting surveys up once a day, automating impressions, etc.  By taking money for a survey you're committing to generate some exposure for the survey.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Will my friends/readers know I was paid?</font>
    <br/>
    <font class="smallfont">
    Yes, they will.  Disclosure is an important element of social surveys that dNeero is committed to.  Whenever somebody sees your survey answers we'll include an unobtrusive but clear note that this survey was paid for.  Of course, you can also have us give your earnings to charity which removes any concerns over money.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How do people feel about paid blog posts?</font>
    <br/>
    <font class="smallfont">
    We can take some lessons from the blogosphere.  Historically blog readers are very comfortable with paid blog posts as long as two conditions are met:
    <ol>
    <li>The fact that the payment is disclosed.</li>
    <li>Any bias created by the payment is discussed and understood.</li>
    </ol>
    Also note that you can have us give your earnings to charity which effectively removes this issue.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->



    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Can I make money inviting friends?</font>
    <br/>
    <font class="smallfont">
    We want you to invite friends to the system so we're paying you up to <%=((BloggerEarningsRevshare)Pagez.getBeanMgr().get("BloggerEarningsRevshare")).getLevel1percent()%>% of what we pay your friends.  Here's how it works:
    <ul>
      <li><font class="smallfont">We calculate your friend's earnings</font></li>
      <li><font class="smallfont">We pay your friend</font></li>
      <li><font class="smallfont">We pay you up to <%=((BloggerEarningsRevshare) Pagez.getBeanMgr().get("BloggerEarningsRevshare")).getLevel1percent()%>% of your friend's earnings (out of our pocket)</font></li>
    </ul>
    </font>
    <br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Does dNeero pay based on friends that my friends invite?</font>
    <br/>
    <font class="smallfont">
    Yes. We pay you for the activity of friends of friends up to five levels deep.  The amount we pay is less each level away from you, but those little bits add up.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">And how is this not a pyramid scheme?</font>
    <br/>
    <font class="smallfont">
    According to <a href='http://en.wikipedia.org/wiki/Pyramid_scheme'>Wikipedia</a> a pyramid scheme "is a non-sustainable business model that involves the exchange of money primarily for enrolling other people into the scheme, usually without any product or service being delivered."  Wikipedia then continues: "There are other commercial models using cross-selling such as multi-level marketing (MLM) or party planning which are perfectly legal and sustainable." (Quotes as of Oct 31, 2006)
    <br/><br/>
    dNeero does not charge you to take part in the program.  Pyramid schemes often include a hefty up-front charge with the promise of exponential growth from downstream users.
    <br/><br/>
    dNeero does not pay you simply to recruit friends... we pay you a share of our revenue that is generated by your friends.
    <br/><br/>
    Nobody is ever left "holding the bag."  The model is sustainable.  We are simply choosing to share a defined percentage of our revenue with you to attract new users.
    <br/><br/>
    Nobody is ever paying any money for an item of fictional value, as in pyramid schemes where items with little actual value are paid for.  The dNeero system works on the other side of the equation... when we pay you.  There's no "entry price."   
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">And dNeero pays me for my friends' activity forever?</font>
    <br/>
    <font class="smallfont">
    At this point, yes.  This is not a "one-time" payment... whenever your friend makes money, you make money.  However we are likely to impose a one or two year limit on the revenue share.</font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Does dNeero pay me for my friends' survey responses and impressions?</font>
    <br/>
    <font class="smallfont">
    All of it.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How do I invite friends and start living the good life?</font>
    <br/>
    <font class="smallfont">
    When your friends click from your profile we track that they came from you.  If they haven't signed up for dNeero then you get credit for bringing them in.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">What if my friend is in the system already?</font>
    <br/>
    <font class="smallfont">
    Then it's too late.  This is a friendly land grab.  Invite your friends before somebody else does!
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Is my friend any worse off for having been invited? In other words, will they get paid less so that I can get paid more?</font>
    <br/>
    <font class="smallfont">
    Your friend will get paid the same amount whether they sign up alone or through your invitation.  The money paid out from this program comes from dNeero's pockets, not your friends'.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->


    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How do I get notified of new survey opportunities?</font>
    <br/>
    <font class="smallfont">
    We'll tell you about them.  In your Account Settings you can turn on notifications via email, Twitter and/or XMPP/Jabber.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">When do I get paid in the real world?</font>
    <br/>
    <font class="smallfont">
    We check account balances every day.  If your account balance is over $20 we will pay you. Simple.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How do I get paid?</font>
    <br/>
    <font class="smallfont">
    For security and ease-of-use we pay your PayPal account.  Setting up a <a href="http://www.paypal.com" target="paypal">PayPal account</a> takes minutes.  Once you have a PayPal account you simply enter your PayPal address (usually your email address) into the dNeero system so that we can send you money.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Can I get paid some other way?</font>
    <br/>
    <font class="smallfont">
    Not at this time.  But tell us what you're thinking and we'll certainly consider it.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How many surveys a day can I take?</font>
    <br/>
    <font class="smallfont">
    Five.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How long do I get paid for survey displays/impressions?</font>
    <br/>
    <font class="smallfont">
    You get paid until you reach the limit set by the survey creator (shown when you decide whether to take the survey).  Or until 30 days have elapsed since the end of the survey (also listed on the survey).
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->


<%@ include file="/jsp/templates/footer.jsp" %>