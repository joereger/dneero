<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Blogger FAQ<br/><br/></ui:define>
    <ui:param name="navtab" value="bloggers"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>



    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">What are the requirements for bloggers?</font>
    <br/>
    <font class="smallfont">
    <ul>
    <li>Your blog must be at least 90 days old</li>
    <li>Your blog must have at least 20 posts made in the last 90 days</li>
    <li>Your blog must be active with at least one post made per week</li>
    <li>We reserve the right to deny any post and cancel any account at any time for any reason</li>
    </ul>
    </font>
    <br/>
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
    <font class="mediumfont" color="#333333">What actually gets posted to my blog?</font>
    <br/>
    <font class="smallfont">
    A widget, no wider or taller than a YouTube widget, includes your survey answers and links for your readers to take the survey for themselves.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How long does my post need to stay up?</font>
    <br/>
    <font class="smallfont">
    To get paid for a survey you must accrue at least one impression (one person needs to see your blog with the survey in it) for 5 of the first 10 days following the date that you take the survey.   Of course, you want to keep your survey up at all times because the more impressions you get the more money you make.  We also reserve the right to not pay you if we believe that you're gaming the system, only putting surveys up once a day, automating impressions, etc.  By taking money for a survey you're committing to posting it to your blog.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Will my blog readers know I was paid?</font>
    <br/>
    <font class="smallfont">
    Yes, they will.  Disclosure is an important element of the blogosphere that dNeero is committed to.  When you post your survey answers to your blog we'll include an unobtrusive but clear note that this survey was paid for.  Of course, you can also have us give your earnings to charity which removes any concerns over money.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">How do blog readers feel about paid blog posts?</font>
    <br/>
    <font class="smallfont">
    Historically blog readers are very comfortable with paid blog posts as long as two conditions are met:
    <ol>
    <li>The fact that the payment is disclosed.</li>
    <li>Any bias created by the payment is discussed and understood.</li>
    </ol>
    Also note that you can have us give your earnings to charity which effectively removes this issue.
    </font>
    <br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Can I post anything else with my survey answers?</font>
    <br/>
    <font class="smallfont">
    Absolutely!  The survey is just a starting point.  Think of it as a way for advertisers and market researchers to start the conversation with you... and as a way for you to start a conversation with your readers.  Go beyond the survey.  Discuss other thoughts you have about the product, concept, etc.  The more you share with your readers the happier we are.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Ok, but am I required to post any words with my survey?</font>
    <br/>
    <font class="smallfont">
    No, if you choose to you can simply fill out the survey and post the html we provide to your blog.
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->

    <!-- Start FAQ Question -->
    <font class="mediumfont" color="#333333">Can I make money inviting friends?</font>
    <br/>
    <font class="smallfont">
    We want you to invite friends to the system so we're paying you up to #{bloggerEarningsRevshare.level1percent}% of what we pay your friends.  Here's how it works:
    <ul>
      <li><font class="smallfont">We calculate your friend's earnings</font></li>
      <li><font class="smallfont">We pay your friend</font></li>
      <li><font class="smallfont">We pay you up to #{bloggerEarningsRevshare.level1percent}% of your friend's earnings (out of our pocket)</font></li>
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
    <font class="mediumfont" color="#333333">Does dNeero pay me for my friends' survey responses and blog impressions?</font>
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
    You provide their email addresses and we'll do the rest.  Of course, your friends must click a link we send them.  If they don't use that link to sign up then there's no way to later associate them with your account... so follow-up with your friends and make sure they do things correctly.
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
    <font class="mediumfont" color="#333333">Sometimes I see surveys on the dNeero homepage but then when I log in they're not listed.  What's up with that?</font>
    <br/>
    <font class="smallfont">
    On the homepage we list all surveys... for anybody.  Once you log in we know about you and we can show you only the surveys for which you qualify.  Researchers can target surveys by geographic area, age, etc.
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
    <font class="mediumfont" color="#333333">How long do I get paid for survey displays in my blog?</font>
    <br/>
    <font class="smallfont">
    You get paid until you reach the limit set by the survey creator (shown when you decide whether to take the survey).  Or until 30 days have elapsed since the end of the survey (also listed on the survey).
    </font>
    <br/><br/><br/>
    <!-- End FAQ Question -->


    </ui:define>


</ui:composition>
</html>