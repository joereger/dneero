<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "#{publicSurvey.survey.title}";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <font class="smallfont">#{publicSurvey.survey.description}</font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=#{publicSurvey.survey.surveyid}" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=#{publicSurvey.survey.surveyid}" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=#{publicSurvey.survey.surveyid}" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=#{publicSurvey.survey.surveyid}" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=#{publicSurvey.survey.surveyid}" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=#{publicSurvey.survey.surveyid}" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=#{publicSurvey.survey.surveyid}" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=#{publicSurvey.survey.surveyid}">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=#{publicSurvey.survey.surveyid}">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=#{publicSurvey.survey.surveyid}">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=#{publicSurvey.survey.surveyid}">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=#{publicSurvey.survey.surveyid}">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=#{publicSurvey.survey.surveyid}">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=#{publicSurvey.survey.surveyid}">Disclosure</a>
    -->

    <t:div rendered="#{(!publicSurvey.qualifiesforsurvey) and (publicSurvey.survey.status ne 5)}">
        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
            <h:outputText value="Sorry, you're not qualified to take this survey.  Your qualification is determined by your Blogger Profile.  Researchers determine their intended audience when they create a survey." styleClass="mediumfont"/>
        </div>
        <br/>
    </t:div>

    <t:div rendered="#{publicSurvey.bloggerhastakentoomanysurveysalreadytoday and !publicSurvey.loggedinuserhasalreadytakensurvey}">
        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
            <h:outputText value="Sorry, you've already taken the maximum number of surveys today.  Wait until tomorrow (as defined by U.S. EST) and try again." styleClass="mediumfont"/>
        </div>
        <br/>
    </t:div>


    <t:div rendered="#{publicSurvey.haveerror}">
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/alert.png" align="left"/>
        There were errors with your response:
        <br/><br/>
        <h:messages styleClass="RED"/>
        <br/><br/>
        Use your browser's back button, correct these errors and re-submit.  Thanks.
        </font></div></center>
    </t:div>
    <t:div rendered="#{!publicSurvey.haveerror}">

        <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
        <table width="100%" cellpadding="2">
            <tr>
                <td valign="top" width="450">
                <c:if test="#{!empty publicSurvey.userwhotooksurvey}">
                     <c:if test="${publicSurvey.userwhotooksurvey.userid>0}">
                            <center>
                            <div class="rounded" style="width: 400px; padding: 15px; margin: 5px; background: #e6e6e6;">
                                <center>
                                    <font class="mediumfont">${publicSurvey.userwhotooksurvey.firstname} ${publicSurvey.userwhotooksurvey.lastname}'s answers.</font><br/>
                                    <c:if test="#{!publicSurvey.loggedinuserhasalreadytakensurvey}">
                                        <font class="tinyfont">(you can answer below)</font>
                                    </c:if>
                                </center>
                                <br/>
                                <center><f:verbatim>#{publicSurvey.surveyResponseHtml}</f:verbatim></center>
                            </div>
                            </center>
                            <br/><br/>
                       </c:if>
                   </c:if>
                   <!--
                   <t:div rendered="#{userSession.isfacebookui and publicSurvey.loggedinuserhasalreadytakensurvey}">
                        <div class="rounded" style="background: #e6e6e6; text-align: center;">
                            <h:outputText value="Your answers." styleClass="mediumfont"/>
                        </div>
                        <center>
                            <f:verbatim>#{publicSurvey.surveyResponseFlashEmbed}</f:verbatim>
                        </center>
                    </t:div>
                    -->
                    <c:if test="${(publicSurvey.survey.status eq 4) and (!publicSurvey.loggedinuserhasalreadytakensurvey) and ( (!empty userSession.facebookUser and userSession.facebookUser.has_added_app) || !userSession.isfacebookui)}">
                        <t:div rendered="#{userSession.isfacebookui and !publicSurvey.loggedinuserhasalreadytakensurvey}">
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                <h:outputText value="Take this survey to earn at least" styleClass="mediumfont" style="font-weight: bold; color: #666666;"/>
                                <br/>
                                <h:outputText value="#{publicSurvey.surveyEnhancer.minearning}" styleClass="largefont" style="font-size: 30px; color: #666666;"/>
                                <c:if test="${publicSurvey.surveytakergavetocharity or publicSurvey.survey.ischarityonly}">
                                    <br/>
                                    <h:outputText value="for charity" styleClass="mediumfont" style="font-weight: bold; color: #666666;"/>
                                </c:if>
                                <br/>
                                <h:outputText value="and up to #{publicSurvey.surveyEnhancer.maxearning} depending on your profile traffic. Links to your answers must appear on your profile for at least five days and somebody needs to click them each day to get paid... usually your friends will take care of this on their own. (And yes, we're talking real world money here... we'll pay your PayPal account when you accrue a balance of $20.)" styleClass="smallfont" style="font-weight: bold; color: #666666;"/>
                            </div>
                            <br/>
                        </t:div>
                        <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                            <center><font class="smallfont" style="font-weight: bold;">Take the survey by answering the questions below.  Because this is a Social Survey your answers will be available to the public.</font></center><br/><br/>
                            <div class="rounded" style="background: #ffffff; padding: 5px;">
                                <f:verbatim escape="false">#{publicSurvey.takesurveyhtml}</f:verbatim>
                            </div>
                        </div>
                        <br/>
                        <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                            <div class="rounded" style="background: #ffffff; padding: 5px;">
                                <table cellpadding="5" cellspacing="0" border="0">
                                    <tr>
                                        <td valign="top" align="left" width="20">
                                            <c:if test="${!publicSurvey.survey.ischarityonly}">
                                                <input type="checkbox" name="${publicSurvey.DNEERO_REQUEST_PARAM_IDENTIFIER}charity-isforcharity" value="1"></input>
                                            </c:if>
                                            <c:if test="${publicSurvey.survey.ischarityonly}">
                                                <input type="hidden" name="${publicSurvey.DNEERO_REQUEST_PARAM_IDENTIFIER}charity-isforcharity" value="1"></input>
                                            </c:if>
                                        </td>
                                        <td valign="top" width="155" align="left">
                                            <c:if test="${!publicSurvey.survey.ischarityonly}">
                                                <font class="formfieldnamefont">Don't Pay Me. Give My Earnings to this Charity:</font>
                                            </c:if>
                                            <c:if test="${publicSurvey.survey.ischarityonly}">
                                                <font class="formfieldnamefont">Earnings From This Survey Must be Given to Charity:</font>
                                            </c:if>
                                            <br/>
                                            <select name="${publicSurvey.DNEERO_REQUEST_PARAM_IDENTIFIER}charity-charityname">
                                                <option value="Habitat for Humanity">Habitat for Humanity</option>
                                                <option value="Make-A-Wish Foundation">Make-A-Wish Foundation</option>
                                                <option value="American Cancer Society">American Cancer Society</option>
                                                <option value="PetSmart Charities">PetSmart Charities</option>
                                                <option value="Wikimedia Foundation">Wikimedia Foundation</option>
                                                <option value="The Conservation Fund">The Conservation Fund</option>
                                            </select>
                                            <br/>
                                            <font class="tinyfont">
                                                If you check the box we'll donate all of your earnings for this survey to the charity of your choice.
                                            </font>
                                        </td>
                                        <td valign="top" align="left">
                                            <font class="tinyfont">
                                            Learn about each of the charities:
                                            <br/><a href="http://www.habitat.org/" target="charity">Habitat for Humanity</a>
                                            <br/><a href="http://www.wish.org/" target="charity">Make-A-Wish Foundation</a>
                                            <br/><a href="http://www.cancer.org/" target="charity">American Cancer Society</a>
                                            <br/><a href="http://www.petsmartcharities.org/" target="charity">PetSmart Charities</a>
                                            <br/><a href="http://en.wikipedia.org/wiki/Wikimedia_Foundation" target="charity">Wikimedia Foundation</a>
                                            <br/><a href="http://www.conservationfund.org/" target="charity">The Conservation Fund</a>
                                            </font>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <br/>
                            <center>
                                <h:commandButton action="#{publicSurvey.takeSurvey}" value="Complete the Survey" styleClass="formsubmitbutton"></h:commandButton>
                            </center>
                        </div>
                    </c:if>
                    <c:if test="${(publicSurvey.survey.status ne 4) and (!publicSurvey.loggedinuserhasalreadytakensurvey)}">
                        <font class="mediumfont">This survey is no longer open for respondents.  However, we still have many other surveys that allow you to make money!  Click the Results tab to see how people answered!</font>
                        <br/><br/>
                        <h:commandButton action="#{publicSurveyList.beginView}" value="Find Another Survey" styleClass="formsubmitbutton"></h:commandButton>
                    </c:if>
                    <br/><br/><br/><br/>
                    <h:outputText value="#{publicSurvey.socialbookmarklinks}" escape="false" styleClass="smallfont"/>
                </td>
                <c:if test="${userSession.isfacebookui}">
                    <td valign="top" align="left">
                        <t:div rendered="#{!empty publicSurvey.userwhotooksurvey and publicSurvey.surveytakergavetocharity and (publicSurvey.userwhotooksurvey.userid>0)}">
                            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                                <center>
                                    <h:outputText value="${publicSurvey.userwhotooksurvey.firstname} ${publicSurvey.userwhotooksurvey.lastname} had dNeero donate all earnings from this survey to ${publicSurvey.charityname}." styleClass="tinyfont" style="font-weight: bold;"/>
                                </center>
                            </div>
                        </t:div>
                        <c:if test="${!empty publicSurvey.facebookuserswhotooksurvey}">
                            <font class="mediumfont" style="color: #cccccc;">Friends Who Took It:</font><br/>
                            <c:forEach var="fbuser" items="${publicSurvey.facebookuserswhotooksurvey}">
                                <!--<img src="/images/user-16.png" width="16" height="16" border="0" align="middle" alt=""/>-->
                                <h:outputLink value="/survey.jsf?surveyid=#{publicSurvey.survey.surveyid}&amp;userid=#{fbuser.userid}&amp;responseid=#{fbuser.responseid}" styleClass="tinyfont" style="color: #0000ff;"><h:outputText>#{fbuser.facebookUser.first_name} #{fbuser.facebookUser.last_name}</h:outputText></h:outputLink><br/>
                            </c:forEach>
                            <br/><br/>
                        </c:if>
                        <c:if test="#{publicSurvey.loggedinuserhasalreadytakensurvey}">
                            <font class="mediumfont" style="color: #cccccc;">You've Taken This Survey:</font><br/>
                            <h:outputLink value="/survey.jsf?surveyid=#{publicSurvey.survey.surveyid}&amp;userid=#{userSession.user.userid}" styleClass="tinyfont" style="color: #0000ff;"><h:outputText>Your Answers</h:outputText></h:outputLink><br/><br/>
                            <br/><br/>
                        </c:if>
                        <font class="mediumfont" style="color: #cccccc;">Tell Friends Who Haven't Taken It:</font><br/>
                        <a href="#{publicSurvey.invitefriendsurl}" target="top">Invite Friends</a>
                    </td>
                </c:if>
                <c:if test="${!userSession.isfacebookui}">
                    <td valign="top" align="left">
                        <div class="rounded" style="background: #00ff00;">
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                <center><img src="/images/paste-128.png" width="128" height="128"/></center>
                                <br/>
                                <c:if test="${publicSurvey.survey.status ge 5}">
                                    <div class="rounded" style="background: #cccccc; text-align: center;">
                                        <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                        <br/>
                                        <h:outputText value="This survey is closed." styleClass="mediumfont"/>
                                    </div>
                                    <br/>
                                </c:if>
                                <c:if test="${publicSurvey.loggedinuserhasalreadytakensurvey}">
                                    <h:outputText value="You've already earned" styleClass="mediumfont"/>
                                    <br/>
                                    <h:outputText value="(pending posting verification)" styleClass="tinyfont"/>
                                    <br/>
                                    <h:outputText value="#{publicSurvey.surveyEnhancer.minearning}" styleClass="largefont" style="font-size: 60px; color: #666666;"/>
                                    <c:if test="${publicSurvey.surveytakergavetocharity or publicSurvey.survey.ischarityonly}">
                                        <br/>
                                        <h:outputText value="for charity" styleClass="mediumfont"/>
                                    </c:if>
                                    <c:if test="${publicSurvey.surveytakergavetocharity}">
                                        <br/>
                                        <h:outputText value="(${publicSurvey.charityname})" styleClass="mediumfont"/>
                                    </c:if>
                                    <br/>
                                    <h:outputText value="and can earn up to #{publicSurvey.surveyEnhancer.maxearning} total by posting it to your blog!" styleClass="mediumfont"/>
                                </c:if>
                                <c:if test="${(!publicSurvey.loggedinuserhasalreadytakensurvey) and (publicSurvey.survey.status le 4)}">
                                    <h:outputText value="Take this survey and earn" styleClass="mediumfont"/>
                                    <br/>
                                    <h:outputText value="#{publicSurvey.surveyEnhancer.minearning}" styleClass="largefont" style="font-size: 60px; color: #666666;"/>
                                    <c:if test="${publicSurvey.surveytakergavetocharity or publicSurvey.survey.ischarityonly}">
                                        <br/>
                                        <h:outputText value="for charity" styleClass="mediumfont"/>
                                    </c:if>
                                    <br/>
                                    <h:outputText value="instantly or up to #{publicSurvey.surveyEnhancer.maxearning} when you post it to your blog!" styleClass="mediumfont"/>
                                    <br/><br/>
                                    <h:outputText value="(just answer the questions to the left and click Complete the Survey... easy!)" styleClass="smallfont"/>
                                </c:if>
                                <c:if test="${(!publicSurvey.loggedinuserhasalreadytakensurvey) and (publicSurvey.survey.status ge 5)}">
                                    <h:outputText value="People who took this survey earned" styleClass="mediumfont"/>
                                    <br/>
                                    <h:outputText value="#{publicSurvey.surveyEnhancer.minearning}" styleClass="largefont" style="font-size: 60px; color: #666666;"/>
                                    <br/>
                                    <h:outputText value="and then earned up to #{publicSurvey.surveyEnhancer.maxearning} total by posting it to their blogs!" styleClass="mediumfont"/>
                                </c:if>
                                <c:if test="#{publicSurvey.survey.ischarityonly}">
                                    <br/><br/>
                                    <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                        <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                        <br/>
                                        <h:outputText value="This is a Charity Only survey." styleClass="mediumfont"/>
                                        <br/>
                                        <h:outputText value="The creator of the survey requires that dNeero donate all of your earnings from the survey to a charity of your choice.  It's a chance to do some good!" styleClass="tinyfont"/>
                                    </div>
                                </c:if>
                            </div>
                            <br/>

                            <font class="smallfont">


                            <br/><br/><b>Get paid to blog.</b><br/>
                            dNeero pays bloggers to fill out surveys and post their answers to their blog.

                            <br/><br/><b>Your answers appear on your blog</b><br/>
                            However you answer, your blog readers will see it.  Express yourself!

                            <br/><br/><b>You need to register and qualify to get paid</b><br/>
                            Once you fill out the survey you'll need to register and fill out a short blogger profile that asks some basic demographic questions.  If you qualify for this survey then you'll get paid.

                            <br/><br/><b>You need to be honest</b><br/>
                            We aren't paying you for any particular opinion.  We're paying you for your time and for the exposure on your blog.  If you don't like a product or service you need to be honest about that fact.

                            <br/><br/><b>Stimulate conversation</b><br/>
                            Engage companies who are actively seeking your feedback on products and services.  At the same time, engage your blog readers in discussion of the same product.

                            <br/><br/><b>No gimmicks.</b><br/>
                            This is the real deal.  A simple model that respects your privacy and allows you to control what you blog about and when.  We pay for activity you choose to engage in.

                            <br/><br/><b>If you don't like this survey</b><br/>
                            We have plenty more <h:commandLink value="surveys" action="#{publicSurveyList.beginView}" style="padding-left: 5px;" styleClass="subnavfont"/> for you to choose from.  And all of them pay!

                            <br/><br/>
                            <div class="rounded" style="background: #ffffff; padding: 5px; margin: 5px; text-align: left;">
                                <h:panelGrid columns="2" cellpadding="3" border="0">

                                    <h:panelGroup>
                                        <h:outputText value=""/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:outputText value="" styleClass="mediumfont"/>
                                    </h:panelGroup>


                                    <h:panelGroup>
                                        <h:outputText value="Start Date" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:outputText value="#{publicSurvey.surveyEnhancer.startdate}"/>
                                    </h:panelGroup>

                                    <h:panelGroup>
                                        <h:outputText value="End Date" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:outputText value="#{publicSurvey.surveyEnhancer.enddate}"/>
                                    </h:panelGroup>





                                    <h:panelGroup>
                                        <h:outputText value="Pay for Responding to Survey" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:outputText value="#{publicSurvey.surveyEnhancer.willingtopayforresponse}"/>
                                    </h:panelGroup>

                                    <h:panelGroup>
                                        <h:outputText value="Pay for 1000 Hits on Your Blog" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:outputText value="#{publicSurvey.surveyEnhancer.willingtopayforcpm}"/>
                                    </h:panelGroup>


                                    <h:panelGroup>
                                        <h:outputText value="Number of Takers To Date" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <d:percentCompleteBar currentvalue="#{publicSurvey.surveyEnhancer.responsesalreadygotten}" maximumvalue="#{publicSurvey.survey.numberofrespondentsrequested}" mintitle="" maxtitle="" widthinpixels="75"></d:percentCompleteBar>
                                        <h:outputText value="Up to #{publicSurvey.survey.numberofrespondentsrequested} people may complete this survey for pay." styleClass="smallfont"/>
                                        <br/><br/>
                                    </h:panelGroup>


                                    <h:panelGroup>
                                        <h:outputText value="Number of Blog Displays To Date" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <d:percentCompleteBar currentvalue="#{publicSurvey.surveyEnhancer.impressionsalreadygotten}" maximumvalue="#{publicSurvey.survey.maxdisplaystotal}" mintitle="" maxtitle="" widthinpixels="75"></d:percentCompleteBar>
                                        <h:outputText value="We'll pay for the first #{publicSurvey.survey.maxdisplaystotal} displays in blogs." styleClass="smallfont"/>
                                        <br/><br/>
                                    </h:panelGroup>

                                    <h:panelGroup>
                                        <h:outputText value="This Page Displayed" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:outputText value="#{publicSurvey.survey.publicsurveydisplays} times"/>
                                    </h:panelGroup>

                                    <h:panelGroup>
                                        <h:outputText value="Surveyid" styleClass="formfieldnamefont"/>
                                    </h:panelGroup>
                                    <h:panelGroup>
                                        <h:outputText value="#{publicSurvey.survey.surveyid}"/>
                                    </h:panelGroup>

                                 </h:panelGrid>
                            </div>
                            </font>
                        </div>
                    </td>
                </c:if>
            </tr>
        </table>

    </t:div>







<%@ include file="/jsp/templates/footer.jsp" %>


