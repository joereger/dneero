<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyPostit" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.systemprops.SystemProperty" %>
<%@ page import="com.dneero.facebook.FacebookApiWrapper" %>
<%@ page import="com.dneero.facebook.FacebookUser" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.util.Str" %>
<%
    PublicSurveyPostit publicSurveyPostit=(PublicSurveyPostit) Pagez.getBeanMgr().get("PublicSurveyPostit");
%>
<%
    //If we don't have a surveyid, shouldn't be on this page
    if (publicSurveyPostit.getSurvey() == null || publicSurveyPostit.getSurvey().getTitle() == null || publicSurveyPostit.getSurvey().getSurveyid()<=0) {
        Pagez.sendRedirect("/publicsurveylist.jsp");
        return;
    }
    //If the survey is draft or waiting
    if (publicSurveyPostit.getSurvey().getStatus()<Survey.STATUS_OPEN) {
        Pagez.sendRedirect("/surveynotopen.jsp");
        return;
    }
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("updatefacebookprofile")) {
        try {
            publicSurveyPostit.updateFacebookProfile();
            Pagez.getUserSession().setMessage("Your Facebook profile should have been updated.");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <% Survey surveyInTabs = publicSurveyPostit.getSurvey();%>
    <%@ include file="/surveytabs.jsp" %>

    <a href="/survey.jsp?surveyid=<%=publicSurveyPostit.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyPostit.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <% if (!publicSurveyPostit.isLoggedinuserhasalreadytakensurvey()){ %>
        <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
            <table width="100%" cellpadding="5">
                <tr>
                    <td valign="top" width="65%">
                        <br/><br/>
                        <center>
                        <%=publicSurveyPostit.getSurveyOnBlogPreview()%>
                        </center>
                    </td>
                    <td valign="top">
                        <div class="rounded" style="background: #e6e6e6;">
                            <div class="rounded" style="background: #ffffff; text-align: left;">
                                <img src="/images/info-48.png" width="48" height="48" align="right"/>
                                <font class="mediumfont" style="color: #666666;">This is how your conversation answers appear when embedded into your blog or social network</font>
                                <br/>
                                <font class="smallfont" style="color: #666666;">(we'll put your actual answers in there)</font>
                            </div>
                            <br/>
                            <font class="smallfont">
                            <br/><br/><b>How do I add it to my blog or social network?</b><br/>
                            We'll make it incredibly easy.  After you answer the questions we'll give you a line of code that you copy and paste into a blog post.  That's it!

                            <br/><br/><b>What will appear on my blog?</b><br/>
                            The questions with your answers.  This is an opportunity to show your readers what you think.

                            <br/><br/><b>How big is it?</b><br/>
                            Smaller than a YouTube embed.  It's 425 pixels wide by 250 pixels tall.  That's it.  If the questions/answers are long it'll scroll automatically.

                            <br/><br/><b>Can people who read my blog answer the questions?</b><br/>
                            Absolutely!  At the bottom of the conversation there's a link where they can provide their own answers and then post them to their own blog.

                            <br/><br/><b>Will I see how others answered?</b><br/>
                            Yep!  This is the cool part!  When you click the See How Others Answered link at the bottom of the conversation you'll see how those people who clicked from your blog answered.  Then you can quickly compare your readers to all bloggers who joined the conversation!
                            </font>
                        </div>
                    </td>
                </tr>
            </table>
        <% } %>
        <% if (Pagez.getUserSession().getIsfacebookui()){ %>
            <div class="rounded" style="background: #e6e6e6;">
                <font class="normalfont">
                <b>Once you join the conversation we'll post a link to your Facebook feed and profile box.  When friends click that link they'll see your answers and be able to answer for themselves.</b><br/>
                </font>
            </div>
        <% } %>
    <% } %>
    <% if (publicSurveyPostit.isLoggedinuserhasalreadytakensurvey()){ %>
        <% if (Pagez.getUserSession().getIsfacebookui()){ %>
            <div class="rounded" style="background: #e6e6e6;">
                <img src="/images/ok-64.png" width="64" height="64" align="left"/>
                <font class="mediumfont"><b>Your response has been accepted!</b></font>
                <br/>
                <font class="smallfont">A link to your answers has been posted to your Facebook feed (which qualifies as a blog, for payment purposes).  Keep the links on your mini-feed and profile because to get paid you must have somebody view your answers on 5 of the 10 days following when you joined the conversation.</font><br/><br/>
                <% if (!publicSurveyPostit.getJustcompletedsurvey()){ %>
                    <div class="rounded" style="background: #e6e6e6;">
                        <font class="smallfont">If you've deleted the conversation from your Facebook Mini Feed or Profile and would like to add them back, click below.  Note that if you've turned off the profile box you'll have to turn it back on yourself in the Facebook settings for the app.</font><br/>
                        <form action="/surveypostit.jsp" method="post" class="niceform">
                            <input type="hidden" name="dpage" value="/surveypostit.jsp">
                            <input type="hidden" name="action" value="updatefacebookprofile">
                            <input type="hidden" name="surveyid" value="<%=publicSurveyPostit.getSurvey().getSurveyid()%>">
                            <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Update Facebook Profile">
                        </form>
                    </div>
                <% } %>
                <div class="rounded" style="background: #ffffff;">
                    <font class="formfieldnamefont"><b>Now earn even more... tell friends about this conversation:</b></font>
                    <br/>
                    <font class="smallfont">They'll be able to see your answers and then join the conversation themselves. If your friend hasn't ever used this site then <b>we'll pay you for any earnings they generate</b>... and any earnings their friends make... and any earnings their friends make... up to five levels deep!</font>
                    <br/>
                    <fb:request-form
                        action="http://apps.facebook.com/<%=SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)%>/?dpage=/surveypostit.jsp&surveyid=<%=publicSurveyPostit.getSurvey().getSurveyid()%>"
                        method="POST"
                        invite="true"
                        type="Conversation"
                        content="<%=Str.cleanForHtml(publicSurveyPostit.getSurvey().getTitle())%> - <%=Str.cleanForHtml(publicSurveyPostit.getSurvey().getDescription())%><fb:req-choice url='http://apps.facebook.com/<%=SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)%>/?action=showsurvey-<%=publicSurveyPostit.getSurvey().getSurveyid()%>-<%=Pagez.getUserSession().getUser().getUserid()%>' label='Check out this Conversation!' />
                    ">
                        <fb:multi-friend-selector
                            showborder="false"
                            actiontext="Invite friends to see your answers for this conversation."
                            rows="3"
                            max="20"
                            bypass="skip" />
                    </fb:request-form>

                </div>
            </div>
        <% } %>
        <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
            <table cellpadding="3" cellspacing="15" border="0" width="100%">
            <tr>
                <td valign="top" width="35%">
                    <div class="rounded" style="background: #e6e6e6;">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <font class="mediumfont">You've Entered this Conversation!</font>
                            <br/>
                            <img src="/images/ok-128.png" width="128" height="128"/>
                            <br/>
                            <font class="mediumfont">Now share your answers to get others involved!</font>
                        </div>
                        <br/>
                        <% if (publicSurveyPostit.getSurvey().getIscharityonly()){ %>
                            <br/><br/>
                            <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                <br/>
                                <font class="mediumfont">This is a Charity Only conversation.</font>
                                <br/>
                                <font class="tinyfont">The conversation creator requires that we donate all of your earnings from the conversation to a charity of your choice.  It's a chance to do some good!</font>
                            </div>
                        <% } %>

                        <font class="smallfont">
                        <br/><br/><b>What gets shared?</b><br/>
                        The conversation with your answers.  This is an opportunity to show your readers, friends and family what you think and encourage them to join the conversation.

                        <br/><br/><b>How big is it?</b><br/>
                        Smaller than a YouTube embed.  It's 425 pixels wide by 250 pixels tall.  That's it.  If the conversation is long it'll scroll automatically.

                        <br/><br/><b>Can my friends jump in?</b><br/>
                        Absolutely!  At the bottom of the conversation there's a link where they can provide their own answers.

                        <br/><br/><b>Will I see how others answered?</b><br/>
                        Yes!  This is the cool part!  When you click the See How Others Answered link at the bottom of the conversation you'll see how those people who clicked from you answered.  Then you can quickly compare your readers to all those who joined the conversation!
                        </font>

                        <br/><br/>
                        <% if (Pagez.getUserSession().getIsloggedin() && publicSurveyPostit.isLoggedinuserhasalreadytakensurvey()){ %>
                                <center><a href="/account/inbox.jsp"><font class="smallfont">Need Help?</font></a></center>
                        <% } %>
                    </div>
                </td>
                <td valign="top" class="posttoblog_tabs_tablewidth">

                    <%if (publicSurveyPostit.getPostresponseinstructions()!=null && !publicSurveyPostit.getPostresponseinstructions().equals("")){%>
                        <div class="rounded" style="background: #e6e6e6;">
                            <font class="mediumfont"><%=publicSurveyPostit.getPostresponseinstructions()%></font>
                        </div>
                    <%}%>



                    <center>
                    <font class="mediumfont">Choose Your Social Network</font>
                    <br/>
                    <font class="smallfont">Or copy Embed Code and paste into your social network.</font>
                    <br/><br/>
                    </center>

                    <center>
                    <div style="text-align:left; width: 400px;">
                        <!-- START GIGYA -->
                        <script src="http://cdn.gigya.com/wildfire/js/wfapiv2.js"></script>
                        <textarea rows="1" cols="1" id="TEXTAREA_ID" style="display: none">
                        <%=publicSurveyPostit.getHtmltoposttoblogflash()%>
                        </textarea>
                        <div id="divWildfirePost"></div>
                        <script>
                        var pconf={
                          useFacebookMystuff: 'false',
                          defaultContent: 'TEXTAREA_ID',
                          UIConfig: '<config baseTheme="v2"><display showEmail="false" showBookmark="false"></display></config>'
                        };
                        Wildfire.initPost('551572', 'divWildfirePost', 400, 300, pconf);
                        </script>
                        <!-- END GIGYA -->
                        <br/>
                        <a href="#" id="togglepagelink"><font class="tinyfont">Other Embed Codes</font></a>
                    </div>
                    </center>



                    <div id="togglepage">
                    <!-- Start hidable stuff -->


                    <br/><br/>
                    <font class="mediumfont">TypePad</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login into TypePad and click on the "Weblogs" tab at the top of the page.</li>
                            <li>Click on the "Begin a new post" button.</li>
                            <li>Click the "Edit HTML" tab.</li>
                            <li>Paste the code into the "Post Body" text box.</li>
                            <li>Click the "Preview" or "Save" button at the bottom of the page and you're done!</li>
                        </ol>
                    </font>


                    <br/><br/>
                    <font class="mediumfont">LiveJournal</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy, or Left-click and hit Ctrl+C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Log in to LiveJournal and click the "Post" link at the top of the page. NOTE: If you go to your journal first (e.g., [YourJournal].livejournal.com) and login from there, you should click the "Post to Journal" link at the top of the page.</li>
                            <li>On the "Rich text" tab, click the "Embed Media" link (it's a round disk in the center of the Rich Text editor's menu bar). We recommend that you not use the HTML tab. </li>
                            <li>Paste the code in the "Insert Embedded Content" box and click the "Insert" button.</li>
                            <li>If you want to add text do so at the blinking cursor ... you'll see it beneath the conversation window.</li>
                            <li>Write a subject for your post.</li>
                            <li>Click the "Post to YourJournal" button at the bottom of the page and you're done!</li>
                        </ol>
                    </font>

                    <br/><br/>
                    <font class="mediumfont">FaceBook</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Use the <a href="http://apps.facebook.com/dneerosocialsurveys/">The Facebook App</a>!</li>
                            <li>This site is deeply integrated into Facebook... there's no need for you to post anything... we'll automatically add conversations to your profile!</li>
                            <li>Adding the app will create you a new Facebook-focused account... you'll need to move your PayPal address from your current account (if you have already entered it).</li>
                        </ol>
                    </font>


                    <br/><br/>
                    <font class="mediumfont">WordPress</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy, or Left-click and hit Ctrl+C)<br/>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            </li>
                            <li>Login into your WordPress.com blog and click on the "New Post" link at the top of the page. </li>
                            <li>Enter a "Title", and paste the code into the "Post" box (right click in the box and choose Paste, or left click in the box and hit Ctrl+V)</li>
                            <li>Space down down and enter any additional text you wish.</li>
                            <li>Click the "Publish" button and you're done!</li>
                        </ol>
                        Note: If are using a hosted blog from Wordpress.org, you can use <a href="http://kimili.com/plugins/kimili-flash-embed-for-wordpress">Kimli Flash Embed for Wordpress</a> to embed a flash object. You'll need to install the plugin, get the flash embed url from the flash embed tab and then create a blog post according to Kimili's syntax. It's a little complex but may be worth it!
                    </font>

                    <br/><br/>
                    <font class="mediumfont">MySpace</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login into MySpace and click on the "Edit Profile" link on the center of the page.</li>
                            <li>Paste the code into any of the boxes such as "About Me", "I'd Like to Meet", "Interests", "Music", "Movies", "Television", "Books" or "Heroes" sections.</li>
                            <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                        </ol>
                    </font>


                    <br/><br/>
                    <font class="mediumfont">MySpace Blog</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login into MySpace and click on the "Manage Blog" link.</li>
                            <li>Click the "Post New Blog" link.</li>
                            <li>Type a title for your post in the "Subject" box.</li>
                            <li>Check the "View Source" check box at the bottom of the text field and paste your code into the "Body" field.</li>
                            <li>Click the "Preview and Post" button and you're done!</li>
                        </ol>
                    </font>

                    <br/><br/>
                    <font class="mediumfont">Blogger</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else if (publicSurveyPostit.getSurvey().getEmbedjavascript()) {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblog()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login to Blogger and click on the "New Post" link on the top of the page. </li>
                            <li>The next page should contain the create post text boxes and the "Edit HTML" tab should be open ... if this is true, skip to step 6., otherwise proceed to step 4.</li>
                            <li>Click on the "Create" link under the "Posting" tab </li>
                            <li>Click on the "Edit HTML" tab. </li>
                            <li>Add a title in the "Title" textbox. </li>
                            <li>Paste the code into the big HTML textbox (Right click and choose Paste or hit Control-V) </li>
                            <li>Either in the Edit HTML or the Compose textboxes, you can add your text. </li>
                            <li>Click "Publish Post" and you're done!</li>
                        </ol>
                    </font>

                    <br/><br/>
                    <font class="mediumfont">Friendster Profile</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login into Friendster and click on the "Edit Profile" link on the center of the page.</li>
                            <li>Click the "Customize" tab.</li>
                            <li>Scroll down to the "Add Media" section and paste the code into the text box.</li>
                            <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                        </ol>
                    </font>

                    <br/><br/>
                    <font class="mediumfont">Friendster Free Blog</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            </li>
                            <li>Login into Friendster, go to Blogs, then "My Blog Home", then under Shortcuts click "Create a Post"</li>
                            <li>Type in some text like "To see my most recent conversation [click here].</li>
                            <li>Highlight "click here" (or any other text or image that you have in the Post Body, and the click the "Insert Link" icon ... a popup will appear ... delete "http://" ... paste the code ... Click "OK"</li>
                            <li>Enter a title.</li>
                            <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                        </ol>
                    </font>


                    <br/><br/>
                    <font class="mediumfont">Friendster Paid Blog</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else if (publicSurveyPostit.getSurvey().getEmbedjavascript()) {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblog()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login into Friendster, go to Blogs, then "My Blog Home", then under Shortcuts click "Create a Post"</li>
                            <li>Click the "Edit HTML" tab. </li>
                            <li>Paste the conversation code onto the Post Body window. You can paste either by Ctrl+v or right click in the Post Body window and choose Paste.</li>
                            <li>Enter a title.</li>
                            <li>Scroll down to the bottom of the page and click the "Save" button and you're done!</li>
                        </ol>
                    </font>


                    <br/><br/>
                    <font class="mediumfont">Yahoo! 360</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login into Yahoo! 360 and click on the "My Blog" link at the top of the page.</li>
                            <li>Click on the "Compose Blog Entry" link.</li>
                            <li>Enter a title for your post under "Entry Title:" and write some text for your blog entry in the "Entry Content:" text box (or upload an image). </li>
                            <li>Click the "View HTML Source" check box.</li>
                            <li>Enter a title for your post under "Entry Title" and paste the code into the "Entry Content" text box.</li>
                            <li>Click the "Post This Entry" button at the bottom of the page and you're done!</li>
                        </ol>
                    </font>


                    <br/><br/>
                    <font class="mediumfont">Tagged</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Login into Tagged and click on the "MyProfile" tab at the top of the page.</li>
                            <li>Click the "Write In Journal" link under the "My Profile" section.</li>
                            <li>Enter a "Subject" for your entry, and any text you wish in the "Text:" field.</li>
                            <li>In the "Widget:" field, click the "Enter Code" tab.</li>
                            <li>Paste the code in the text box.</li>
                            <li>Click the "Post" button at the bottom of the page and you're done!</li>
                        </ol>
                    </font>

                    <br/><br/>
                    <font class="mediumfont">Xanga</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
                            <%}%>
                            </li>
                            <li>Log in to Xanga and click on the "New Entry" link. (Make sure you're in the "Dash" tab.)</li>
                            <li>If your blog is set to Rich Text, proceed to step 4, otherwise click on the "settings" link, which is near the top right of the Weblog Entry page. You may have to login again, and then under "Basic Editor Setting" choose "Rich Text - default" from the dropdown box. Scroll to bottom of page and and click the "Save Changes" button.</li>
                            <li>Enter a "Title" and in the "Main Entry" box write your blog entry.</li>
                            <li>Click the "Edit HTML" check box, and paste the code into the "Main Entry" text box. (Left-click at the bottom of the "Main Entry" text box and either hit Ctrl+V, or Right-click and choose Paste.)</li>
                            <li>Click the "Save Changes" button at the bottom of the page and you're done! </li>
                        </ol>
                    </font>

                    <br/><br/>
                    <font class="mediumfont">Zimbio</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttobloglink()%></textarea>
                            </li>
                            <li>Login into Zimbio, go to "My Dashboard", then "My Articles", then "Write Article"</li>
                            <li>Type in some text like "To see my most recent conversation [click here]." </li>
                            <li>Highlight "click here" (or any other text or image that you have in the main window), and the click the "Insert/edit Link" icon ... a popup will appear ...</li>
                            <li>Paste the code in the "Link URL" box and click the "Insert" button. (Left-click in the "Link URL" text box and either hit Ctrl+V, or Right-click and choose Paste.)</li>
                            <li>Enter a Tiltle and any other text/images.</li>
                            <li>Click the "Save" button and you're done!</li>
                        </ol>
                    </font>

                    <br/><br/>
                    <font class="mediumfont">Generic Flash Embedding (if your tool isn't listed above)</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                Sorry, this conversation does not allow Flash embedding.
                            <%}%>
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


                    <br/><br/>
                    <font class="mediumfont">Generic Javascript Embedding (if Flash doesn't work)</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedjavascript()) {%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblog()%></textarea>
                            <%} else {%>
                                Sorry, this conversation does not allow Javascript embedding.
                            <%}%>
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

                    <br/><br/>
                    <font class="mediumfont">Generic Image/Link Embedding (a last resort)</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogimagelink()%></textarea>
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

                    <br/><br/>
                    <font class="mediumfont">Link Embedding (a very last resort)</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttobloglink()%></textarea>
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

                    <!-- End hidable stuff -->
                    </div>

                    <script>
                        $("#togglepagelink").click(function() {
                            $("#togglepage").toggle();
                        });
                        $("#togglepage").hide();
                    </script>

                </td>
            </tr>
        </table>
        <% } %>
        <br/>
    <% } %>







<%@ include file="/template/footer.jsp" %>


