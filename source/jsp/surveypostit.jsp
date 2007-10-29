<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "#{publicSurveyPostit.survey.title}";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="smallfont">#{publicSurveyPostit.survey.description}</font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=#{publicSurveyPostit.survey.surveyid}">Disclosure</a>
    -->

    <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
    <c:if test="${!publicSurveyPostit.loggedinuserhasalreadytakensurvey}">
        <c:if test="${!userSession.isfacebookui}">
            <table width="100%" cellpadding="5">
                <tr>
                    <td valign="top" width="450">
                        <br/><br/>
                        <f:verbatim escape="false">#{publicSurveyPostit.surveyOnBlogPreview}</f:verbatim>
                    </td>
                    <td valign="top">
                        <div class="rounded" style="background: #00ff00;">
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                    <h:outputText value="What's this?" styleClass="mediumfont"/>
                                    <br/>
                                    <img src="/images/info-128.png" width="128" height="128"/>
                                    <br/>
                                    <h:outputText value="It's how this survey looks in your blog!" styleClass="mediumfont"/>
                                    <br/>
                                    <h:outputText value="(of course, we'll put your actual answers in there)" styleClass="smallfont"/>
                            </div>
                            <br/>
                            <font class="smallfont">
                            <br/><br/><b>How do I add it to my blog?</b><br/>
                            We'll make it incredibly easy.  After you take the survey we'll give you a line of code that you copy and paste into a blog post.  That's it!

                            <br/><br/><b>What will appear on my blog?</b><br/>
                            The survey with your answers.  This is an opportunity to show your readers what you think.

                            <br/><br/><b>How big is it?</b><br/>
                            Smaller than a YouTube embed.  It's 425 pixels wide by 250 pixels tall.  That's it.  If the survey is long it'll scroll automatically.

                            <br/><br/><b>Can people who read my blog answer the questions?</b><br/>
                            Absolutely!  At the bottom of the survey there's a link where they can provide their own answers and then post them to their own blog.

                            <br/><br/><b>Will I see how others answered?</b><br/>
                            Yep!  This is the cool part!  When you click the See How Others Answered link at the bottom of the survey you'll see how those people who clicked from your blog answered.  Then you can quickly compare your readers to all bloggers who took the survey!
                            </font>
                        </div>
                    </td>
                </tr>
            </table>
        </c:if>
        <c:if test="${userSession.isfacebookui}">
            <div class="rounded" style="background: #e6e6e6;">
                <font class="normalfont">
                <b>Once you take the survey we'll post a link to your Facebook feed and profile box.  When friends click that link they'll see your answers and be able to answer for themselves.</b><br/>
                </font>
            </div>
        </c:if>
    </c:if>
    <c:if test="${publicSurveyPostit.loggedinuserhasalreadytakensurvey}">
        <c:if test="${userSession.isfacebookui}">
            <div class="rounded" style="background: #e6e6e6;">
                <img src="/images/ok-64.png" width="64" height="64" align="left"/>
                <font class="mediumfont"><b>Your survey response has been accepted!</b></font>
                <br/>
                <font class="smallfont">A link to your survey has been posted to your Facebook feed (which qualifies as a blog, for payment purposes).  Keep the links on your mini-feed and profile because to get paid you must have somebody view your answers on 5 of the 10 days following when you took the survey.</font><br/><br/>
                <c:if test="#{!publicSurveyPostit.justcompletedsurvey}">
                    <div class="rounded" style="background: #e6e6e6;">
                        <font class="smallfont">If you've deleted the survey from your Facebook Mini Feed or Profile and would like to add them back, click below.  Note that if you've turned off the profile box you'll have to turn it back on yourself in the Facebook settings for the dNeero app.</font><br/>
                        <h:commandButton action="#{publicSurveyPostit.updateFacebookProfile}" value="Update Facebook Profile" styleClass="formsubmitbutton"/>
                    </div>
                </c:if>
                <div class="rounded" style="background: #ffffff;">
                    <font class="formfieldnamefont"><b>Now earn even more... tell friends about this survey:</b></font>
                    <br/>
                    <font class="smallfont">They'll be able to see your answers and then take the survey themselves. If your friend hasn't ever used dNeero then <b>we'll pay you for any earnings they generate</b>... and any earnings their friends make... and any earnings their friends make... up to five levels deep!</font>
                    <br/>
                    <a href="#{publicSurveyPostit.invitefriendsurl}" target="top"><font class="mediumfont">Invite Friends to this Survey</font></a>
                </div>
            </div>
        </c:if>
        <c:if test="${!userSession.isfacebookui}">
            <table cellpadding="3" cellspacing="15" border="0" width="100%">
            <tr>
                <td valign="top">
                    <div class="rounded" style="background: #00ff00;">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                                <h:outputText value="You've Completed this Survey!" styleClass="mediumfont"/>
                                <br/>
                                <img src="/images/ok-128.png" width="128" height="128"/>
                                <br/>
                                <h:outputText value="Now post your answers to your blog!" styleClass="mediumfont"/>
                                <br/>
                                <h:outputText value="(instructions to the right... the survey must appear on your blog for 5 of 10 days from when you took the survey to be paid)" styleClass="smallfont"/>
                        </div>
                        <br/>
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <h:outputText value="You've already earned" styleClass="mediumfont"/>
                            <br/>
                            <h:outputText value="(pending posting verification)" styleClass="tinyfont"/>
                            <br/>
                            <h:outputText value="#{publicSurveyPostit.surveyEnhancer.minearning}" styleClass="largefont" style="font-size: 60px; color: #666666;"/>
                            <c:if test="${publicSurveyPostit.surveytakergavetocharity or publicSurveyPostit.survey.ischarityonly}">
                                <br/>
                                <h:outputText value="for charity" styleClass="mediumfont"/>
                            </c:if>
                            <c:if test="${publicSurveyPostit.surveytakergavetocharity}">
                                <br/>
                                <h:outputText value="(${publicSurveyPostit.charityname})" styleClass="mediumfont"/>
                            </c:if>
                            <br/>
                            <h:outputText value="and can earn up to #{publicSurveyPostit.surveyEnhancer.maxearning} total by posting it to your blog!" styleClass="mediumfont"/>
                        </div>
                        <br/>
                        <c:if test="#{publicSurveyPostit.survey.ischarityonly}">
                            <br/><br/>
                            <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                <br/>
                                <h:outputText value="This is a Charity Only survey." styleClass="mediumfont"/>
                                <br/>
                                <h:outputText value="The creator of the survey requires that dNeero donate all of your earnings from the survey to a charity of your choice.  It's a chance to do some good!" styleClass="tinyfont"/>
                            </div>
                        </c:if>

                        <font class="smallfont">
                        <br/><br/><b>How do I add it to my blog?</b><br/>
                        We've made it incredibly easy.  Click the Show link next to your blogging or social networking tool (right of this screen) and follow the instructions.  It usually boils down to copying and pasting a single line of code into a blog post.  That's it!

                        <br/><br/><b>What will appear on my blog?</b><br/>
                        The survey with your answers.  This is an opportunity to show your readers what you think.

                        <br/><br/><b>How big is it?</b><br/>
                        Smaller than a YouTube embed.  It's 425 pixels wide by 250 pixels tall.  That's it.  If the survey is long it'll scroll automatically.

                        <br/><br/><b>Can people who read my blog answer the questions?</b><br/>
                        Absolutely!  At the bottom of the survey there's a link where they can provide their own answers and then post them to their own blog.

                        <br/><br/><b>Will I see how others answered?</b><br/>
                        Yep!  This is the cool part!  When you click the See How Others Answered link at the bottom of the survey you'll see how those people who clicked from your blog answered.  Then you can quickly compare your readers to all bloggers who took the survey!
                        </font>

                        <br/><br/>
                        <c:if test="${userSession.isloggedin and publicSurveyPostit.loggedinuserhasalreadytakensurvey}">
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                <h:commandLink value="Need Help?" action="#{accountSupportIssuesList.beginView}" styleClass="mediumfont"/>
                            </div>
                        </c:if>
                    </div>
                </td>
                <td valign="top" class="posttoblog_tabs_tablewidth">
                    <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                    Instructions for posting your survey answers to various blogs and social networks.  Need <h:commandLink value="help" action="#{accountSupportIssuesList.beginView}" styleClass="smallfont" style="padding-left: 5px; padding-right: 1px;"/> posting to your blog?
                    </font></div></center>
                    <br/>
                    <t:collapsiblePanel id="cp7" value="true" title="cp7" var="cp7var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="TypePad " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp7var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp7var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into TypePad and click on the "Weblogs" tab at the top of the page.</li>
                                <li>Click on the "Begin a new post" button.</li>
                                <li>Click the "Edit HTML" tab.</li>
                                <li>Paste the code into the "Post Body" text box.</li>
                                <li>Click the "Preview" or "Save" button at the bottom of the page and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp8" value="true" title="cp8" var="cp8var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="LiveJournal " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp8var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp8var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy, or Left-click and hit Ctrl+C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Log in to LiveJournal and click the "Post" link at the top of the page. NOTE: If you go to your journal first (e.g., [YourJournal].livejournal.com) and login from there, you should click the "Post to Journal" link at the top of the page.</li>
                                <li>On the "Rich text" tab, click the "Embed Media" link (it's a round disk in the center of the Rich Text editor's menu bar). We recommend that you not use the HTML tab. </li>
                                <li>Paste the code in the "Insert Embedded Content" box and click the "Insert" button.</li>
                                <li>If you want to add text do so at the blinking cursor ... you'll see it beneath the survey window.</li>
                                <li>Write a subject for your post.</li>
                                <li>Click the "Post to YourJournal" button at the bottom of the page and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp1" value="true" title="cp1" var="cp1var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="WordPress " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp1var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp1var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy, or Left-click and hit Ctrl+C)<br/>
                                    <c:choose>
                                        <c:when test="#{1 eq 2}"></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into your WordPress.com blog and click on the "New Post" link at the top of the page. </li>
                                <li>Enter a "Title", and paste the dNeero code into the "Post" box (right click in the box and choose Paste, or left click in the box and hit Ctrl+V)</li>
                                <li>Space down down and enter any additional text you wish.</li>
                                <li>Click the "Publish" button and you're done!</li>
                            </ol>
                            Note: If are using a hosted blog from Wordpress.org, you can use <a href="http://kimili.com/plugins/kimili-flash-embed-for-wordpress">Kimli Flash Embed for Wordpress</a> to embed a flash object. You'll need to install the plugin, get the dNeero flash embed url from the flash embed tab and then create a blog post according to Kimili's syntax. It's a little complex but may be worth it!
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp2" value="true" title="cp2" var="cp2var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="MySpace " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp2var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp2var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflashwithembedandobjecttag}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into MySpace and click on the "Edit Profile" link on the center of the page.</li>
                                <li>Paste the code into any of the boxes such as "About Me", "I'd Like to Meet", "Interests", "Music", "Movies", "Television", "Books" or "Heroes" sections.</li>
                                <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp3" value="true" title="cp3" var="cp3var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="MySpace Blog " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp3var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp3var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><font style="color: #ffffff;">[code]</font>#{publicSurveyPostit.htmltoposttoblogflashwithembedandobjecttag}<font style="color: #ffffff;">[/code]</font></textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into MySpace and click on the "Manage Blog" link.</li>
                                <li>Click the "Post New Blog" link.</li>
                                <li>Type a title for your post in the "Subject" box.</li>
                                <li>Check the "View Source" check box at the bottom of the text field and paste your code into the "Body" field.</li>
                                <li>Click the "Preview and Post" button and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp4" value="true" title="cp4" var="cp4var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Blogger " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp4var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp4var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:when test="#{publicSurveyPostit.survey.embedjavascript}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblog}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login to Blogger and click on the "New Post" link on the top of the page.</li>
                                <li>The next page should contain the create post text boxes and the "Edit HTML" tab should be open ... if this is true, skip to step 6., otherwise proceed to step.</li>
                                <li>Click on the "Create" link under the "Posting" tab</li>
                                <li>Click on the "Edit HTML" tab.</li>
                                <li>Add a title in the "Title" textbox.</li>
                                <li>Paste the code into the big HTML textbox (Right click and choose Paste or hit Control-V)</li>
                                <li>Either in the Edit HTML or the Compose textboxes, you can add your add text where it says "Enter text here" ... if you are not going to add text just delete the two instances of "Enter text here".</li>
                                <li>Click "Publish" and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp5" value="true" title="cp5" var="cp5var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Friendster Profile " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp5var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp5var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into Friendster and click on the "Edit Profile" link on the center of the page.</li>
                                <li>Click the "Customize" tab.</li>
                                <li>Scroll down to the "Add Media" section and paste the code into the text box.</li>
                                <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp5b" value="true" title="cp5b" var="cp5bvar">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Friendster Free Blog " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp5bvar}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp5bvar}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{1 eq 2}"></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into Friendster, go to Blogs, then "My Blog Home", then under Shortcuts click "Create a Post"</li>
                                <li>Type in some text like "To see my most recent dNeero survey [click here].</li>
                                <li>Highlight "click here" (or any other text or image that you have in the Post Body, and the click the "Insert Link" icon ... a popup will appear ... delete "http://" ... paste the dNeero code ... Click "OK"</li>
                                <li>Enter a title.</li>
                                <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp5a" value="true" title="cp5a" var="cp5avar">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Friendster Paid Blog " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp5avar}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp5avar}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:when test="#{publicSurveyPostit.survey.embedjavascript}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblog}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into Friendster, go to Blogs, then "My Blog Home", then under Shortcuts click "Create a Post"</li>
                                <li>Click the "Edit HTML" tab. </li>
                                <li>Paste the dNeero survey code onto the Post Body window. You can paste either by Ctrl+v or right click in the Post Body window and choose Paste.</li>
                                <li>Enter a title.</li>
                                <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp6" value="true" title="cp6" var="cp6var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Yahoo! 360 " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp6var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp6var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into Yahoo! 360 and click on the "My Blog" link at the top of the page.</li>
                                <li>Click on the "Compose Blog Entry" link.</li>
                                <li>Enter a title for your post under "Entry Title:" and write some text for your blog entry in the "Entry Content:" text box (or upload an image). </li>
                                <li>Click the "View HTML Source" check box.</li>
                                <li>Enter a title for your post under "Entry Title" and paste the code into the "Entry Content" text box.</li>
                                <li>Click the "Post This Entry" button at the bottom of the page and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp9" value="true" title="cp9" var="cp9var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Tagged " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp9var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp9var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into Tagged and click on the "MyProfile" tab at the top of the page.</li>
                                <li>Click the "Write In Journal" link under the "My Profile" section.</li>
                                <li>Enter a "Subject" for your entry, and any text you wish in the "Text:" field.</li>
                                <li>In the "Widget:" field, click the "Enter Code" tab.</li>
                                <li>Paste the code in the text box.</li>
                                <li>Click the "Post" button at the bottom of the page and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cpxanga" value="true" title="cpxanga" var="cpxangavar">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Xanga " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cpxangavar}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cpxangavar}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Log in to Xanga and click on the "New Entry" link. (Make sure you're in the "Dash" tab.)</li>
                                <li>If your blog is set to Rich Text, proceed to step 4, otherwise click on the "settings" link, which is near the top right of the Weblog Entry page. You may have to login again, and then under "Basic Editor Setting" choose "Rich Text - default" from the dropdown box. Scroll to bottom of page and and click the "Save Changes" button.</li>
                                <li>Enter a "Title" and in the "Main Entry" box write your blog entry.</li>
                                <li>Click the "Edit HTML" check box, and paste the dNeero code into the "Main Entry" text box. (Left-click at the bottom of the "Main Entry" text box and either hit Ctrl+V, or Right-click and choose Paste.)</li>
                                <li>Click the "Save Changes" button at the bottom of the page and you're done! </li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp5z" value="true" title="cpz" var="cpzvar">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs">
                                <h:outputText value="Zimbio " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cpzvar}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cpzvar}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{1 eq 2}"></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttobloglink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into Zimbio, go to "My Dashboard", then "My Articles", then "Write Article"</li>
                                <li>Type in some text like "To see my most recent dNeero survey [click here]." </li>
                                <li>Highlight "click here" (or any other text or image that you have in the main window), and the click the "Insert/edit Link" icon ... a popup will appear ...</li>
                                <li>Paste the dNeero code in the "Link URL" box and click the "Insert" button. (Left-click in the "Link URL" text box and either hit Ctrl+V, or Right-click and choose Paste.)</li>
                                <li>Enter a Tiltle and any other text/images.</li>
                                <li>Click the "Save" button and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp11" value="true" title="cp11" var="cp11var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs_generic">
                                <h:outputText value="Generic Flash Embedding (if your tool isn't listed above) " styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp11var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp11var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedflash}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogflash}</textarea></c:when>
                                        <c:otherwise>Sorry, this survey does not allow Flash embedding.</c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into your publishing tool of choice.</li>
                                <li>Go to the page that allows you to create a blog entry.</li>
                                <li>Paste the code from above into your post being careful that:</li>
                                    <ul>
                                        <li>When your publishing tool publishes the code it should do so directly.  Sometimes this means editing the html of your post directly.  Sometimes this means that you need to use a special embed functionality.  Check with your publishing tool provider.</li>
                                    </ul>
                                <li>Click to publish and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp10" value="true" title="cp10" var="cp10var">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs_generic">
                                <h:outputText value="Generic Javascript Embedding (if Flash doesn't work)" styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp10var}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp10var}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{publicSurveyPostit.survey.embedjavascript}"><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblog}</textarea></c:when>
                                        <c:otherwise>This survey does not support Javascript embedding.</c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into your publishing tool of choice.</li>
                                <li>Go to the page that allows you to create a blog entry.</li>
                                <li>Paste the code from above into your post being careful that:</li>
                                    <ul>
                                        <li>When your publishing tool publishes the code it should do so directly.  Sometimes this means editing the html of your post directly.  Sometimes this means that you need to use a special embed functionality.  Check with your publishing tool provider.</li>
                                    </ul>
                                <li>Click to publish and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp10a" value="true" title="cp10a" var="cp10avar">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs_generic">
                                <h:outputText value="Generic Image/Link Embedding (a last resort)" styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp10avar}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp10avar}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{1 eq 2}"></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttoblogimagelink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into your publishing tool of choice.</li>
                                <li>Go to the page that allows you to create a blog entry.</li>
                                <li>Paste the code from above into your post being careful that:</li>
                                    <ul>
                                        <li>When your publishing tool publishes the code it should do so directly.  Sometimes this means editing the html of your post directly.  Sometimes this means that you need to use a special embed functionality.  Check with your publishing tool provider.</li>
                                    </ul>
                                <li>Click to publish and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>

                    <t:collapsiblePanel id="cp10b" value="true" title="cp10b" var="cp10bvar">
                        <f:facet name="header">
                            <t:div styleClass="posttoblog_tabs_generic">
                                <h:outputText value="Link Embedding (a very last resort)" styleClass="formfieldnamefont"/>
                                <t:headerLink immediate="true">
                                    <h:outputText value="Show Instructions" rendered="#{cp10bvar}" styleClass="normalfont"/>
                                    <h:outputText value="Hide" rendered="#{!cp10bvar}" styleClass="normalfont"/>
                                </t:headerLink>
                            </t:div>
                        </f:facet>
                        <font class="smallfont">
                            <ol>
                                <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                    <c:choose>
                                        <c:when test="#{1 eq 2}"></c:when>
                                        <c:otherwise><textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();">#{publicSurveyPostit.htmltoposttobloglink}</textarea></c:otherwise>
                                    </c:choose>
                                </li>
                                <li>Login into your publishing tool of choice.</li>
                                <li>Go to the page that allows you to create a blog entry.</li>
                                <li>Paste the code from above into your post being careful that:</li>
                                    <ul>
                                        <li>When your publishing tool publishes the code it should do so directly.  Sometimes this means editing the html of your post directly.  Sometimes this means that you need to use a special embed functionality.  Check with your publishing tool provider.</li>
                                    </ul>
                                <li>Click to publish and you're done!</li>
                            </ol>
                        </font>
                    </t:collapsiblePanel>
                </td>
            </tr>
        </table>
        </c:if>
        <br/>
    </c:if>







<%@ include file="/jsp/templates/footer.jsp" %>


