<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicSurvey" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyFacebookFriendListitem" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicSurvey) Pagez.getBeanMgr().get("PublicSurvey")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicSurvey publicSurvey = (PublicSurvey)Pagez.getBeanMgr().get("PublicSurvey");
%>
<%@ include file="/template/header.jsp" %>


    <font class="smallfont"><%=publicSurvey.getSurvey().getDescription()%></font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>

    <%if (!publicSurvey.getQualifiesforsurvey() && publicSurvey.getSurvey().getStatus()!=Survey.STATUS_CLOSED){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
            <font class="mediumfont">Sorry, you're not qualified to take this survey.  Your qualification is determined by your Blogger Profile.  Researchers determine their intended audience when they create a survey.</font>
        </div>
        <br/>
    <%}%>

    <%if (!publicSurvey.getBloggerhastakentoomanysurveysalreadytoday() && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
            <font class="mediumfont">Sorry, you've already taken the maximum number of surveys today.  Wait until tomorrow (as defined by U.S. EST) and try again.</font>
        </div>
        <br/>
    <%}%>

    <%if (publicSurvey.getHaveerror()){%>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/alert.png" align="left"/>
        There were errors with your response:
        <br/><br/>
        <h:messages styleClass="RED"/>
        <br/><br/>
        Use your browser's back button, correct these errors and re-submit.  Thanks.
        </font></div></center>
    <%}%>
    <%if (!publicSurvey.getHaveerror()){%>
        <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
        <table width="100%" cellpadding="2">
            <tr>
                <td valign="top" width="450">
                <% if (publicSurvey.getUserwhotooksurvey()!=null){ %>
                     <% if (publicSurvey.getUserwhotooksurvey().getUserid()>0){ %>
                            <center>
                            <div class="rounded" style="width: 400px; padding: 15px; margin: 5px; background: #e6e6e6;">
                                <center>
                                    <font class="mediumfont"><%=publicSurvey.getUserwhotooksurvey().getFirstname()%> <%=publicSurvey.getUserwhotooksurvey().getLastname()%>'s answers.</font><br/>
                                    <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                                        <font class="tinyfont">(you can answer below)</font>
                                    <% } %>
                                </center>
                                <br/>
                                <center><%=publicSurvey.getSurveyResponseHtml()%></center>
                            </div>
                            </center>
                            <br/><br/>
                       <% } %>
                   <% } %>
                   <% if (publicSurvey.getSurvey().getStatus()==Survey.STATUS_OPEN && !publicSurvey.getLoggedinuserhasalreadytakensurvey() && ((Pagez.getUserSession().getFacebookUser()!=null && Pagez.getUserSession().getFacebookUser().getHas_added_app()) || !Pagez.getUserSession().getIsfacebookui())){ %>
                        <%if (Pagez.getUserSession().getIsfacebookui() && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                <font class="mediumfont" style="font-weight: bold; color: #666666;">Take this survey to earn up to</font>
                                <br/>
                                <h:outputText value="<%=publicSurvey.getSurveyEnhancer().getMinearning()%>" styleClass="largefont" style="font-size: 30px; color: #666666;"/>
                                <% if (publicSurvey.getSurveytakergavetocharity() || publicSurvey.getSurvey().getIscharityonly()){ %>
                                    <br/>
                                    <font class="mediumfont" style="font-weight: bold; color: #666666;">for charity</font>
                                <% } %>
                                <br/>
                                <font class="smallfont" style="font-weight: bold; color: #666666;">or up to <%=publicSurvey.getSurveyEnhancer().getMaxearning()%> depending on your profile traffic. Links to your answers must appear on your profile for at least five days and somebody needs to click them each day to get paid... usually your friends will take care of this on their own. (And yes, we're talking real world money here... we'll pay your PayPal account when you accrue a balance of $20.)</font>
                            </div>
                            <br/>
                        <%}%>
                        <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                            <center><font class="smallfont" style="font-weight: bold;">Take the survey by answering the questions below.  Because this is a Social Survey your answers will be available to the public.</font></center><br/><br/>
                            <div class="rounded" style="background: #ffffff; padding: 5px;">
                                <%=publicSurvey.getTakesurveyhtml()%>
                            </div>
                        </div>
                        <br/>
                        <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                            <div class="rounded" style="background: #ffffff; padding: 5px;">
                                <table cellpadding="5" cellspacing="0" border="0">
                                    <tr>
                                        <td valign="top" align="left" width="20">
                                            <% if (!publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <input type="checkbox" name="<%=publicSurvey.getDNEERO_REQUEST_PARAM_IDENTIFIER()%>charity-isforcharity" value="1"></input>
                                            <% } %>
                                            <% if (publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <input type="hidden" name="<%=publicSurvey.getDNEERO_REQUEST_PARAM_IDENTIFIER()%>charity-isforcharity" value="1"></input>
                                            <% } %>
                                        </td>
                                        <td valign="top" width="155" align="left">
                                            <% if (!publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <font class="formfieldnamefont">Don't Pay Me. Give My Earnings to this Charity:</font>
                                            <% } %>
                                            <% if (publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <font class="formfieldnamefont">Earnings From This Survey Must be Given to Charity:</font>
                                            <% } %>
                                            <br/>
                                            <select name="<%=publicSurvey.getDNEERO_REQUEST_PARAM_IDENTIFIER()%>charity-charityname">
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
                                <input type="submit" class="formsubmitbutton" value="Complete the Survey">
                            </center>
                        </div>
                    <% } %>
                    <% if (publicSurvey.getSurvey().getStatus()!=Survey.STATUS_OPEN && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                        <font class="mediumfont">This survey is no longer open for respondents.  However, we still have many other surveys that allow you to make money!  Click the Results tab to see how people answered!</font>
                        <br/><br/>
                        <a href="publicsurveylist.jsp"><font class="mediumfont">Find Another Survey</font></a>
                    <% } %>
                    <br/><br/><br/><br/>
                    <font class="smallfont"><%=publicSurvey.getSocialbookmarklinks()%></font>
                </td>
                <% if (Pagez.getUserSession().getIsfacebookui()){ %>
                    <td valign="top" align="left">
                        <%if (publicSurvey.getUserwhotooksurvey()!=null && publicSurvey.getUserwhotooksurvey().getUserid()>0 && publicSurvey.getSurveytakergavetocharity()){%>
                            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                                <center>
                                    <font class="tinyfont" style="font-weight: bold;"><%=publicSurvey.getUserwhotooksurvey().getFirstname()%> <%=publicSurvey.getUserwhotooksurvey().getLastname()%> had dNeero donate all earnings from this survey to <%=publicSurvey.getCharityname()%>.</font>
                                </center>
                            </div>
                        <%}%>
                        <% if (publicSurvey.getFacebookuserswhotooksurvey()!=null){ %>
                            <font class="mediumfont" style="color: #cccccc;">Friends Who Took It:</font><br/>
                            <%
                                for (Iterator<PublicSurveyFacebookFriendListitem> iterator=publicSurvey.getFacebookuserswhotooksurvey().iterator(); iterator.hasNext();){
                                    PublicSurveyFacebookFriendListitem publicSurveyFacebookFriendListitem= iterator.next();
                                    %>
                                    <a href="survey.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>&userid=<%=publicSurveyFacebookFriendListitem.getUserid()%>&responseid=<%=publicSurveyFacebookFriendListitem.getResponseid()%>"><font class="tinyfont" style="color: #0000ff;"><%=publicSurveyFacebookFriendListitem.getFacebookUser().getFirst_name()%> <%=publicSurveyFacebookFriendListitem.getFacebookUser().getLast_name()%></font></a><br/>
                                    <%
                                }
                            %>
                            <br/><br/>
                        <%}%>
                        <% if (publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                            <font class="mediumfont" style="color: #cccccc;">You've Taken This Survey:</font><br/>
                            <a href="survey.jsp??surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>&userid=<%=Pagez.getUserSession().getUser().getUserid()%>"><font class="tinyfont" style="color: #0000ff;">Your Answers</font></a><br/><br/>
                            <br/><br/>
                        <%}%>
                        <font class="mediumfont" style="color: #cccccc;">Tell Friends Who Haven't Taken It:</font><br/>
                        <a href="<%=publicSurvey.getInvitefriendsurl()%>" target="top">Invite Friends</a>
                    </td>
                <% } %>
                <% if (Pagez.getUserSession().getIsfacebookui()){ %>
                    <td valign="top" align="left">
                        <div class="rounded" style="background: #00ff00;">
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                <center><img src="/images/paste-128.png" width="128" height="128"/></center>
                                <br/>
                                <% if (publicSurvey.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                    <div class="rounded" style="background: #cccccc; text-align: center;">
                                        <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                        <br/>
                                        <font class="mediumfont">This survey is closed.</font>
                                    </div>
                                    <br/>
                                <% } %>
                                <% if (publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                                    <font class="mediumfont">You've already earned</font>
                                    <h:outputText value="You've already earned" styleClass="mediumfont"/>
                                    <br/>
                                    <font class="tinyfont">(pending posting verification)</font>
                                    <br/>
                                    <font class="largefont" style="font-size: 60px; color: #666666;"><%=publicSurvey.getSurveyEnhancer().getMinearning()%></font>
                                    <% if (publicSurvey.getSurveytakergavetocharity() || publicSurvey.getSurvey().getIscharityonly()){ %>
                                        <br/>
                                        <font class="mediumfont">for charity</font>
                                    <% } %>
                                    <% if (publicSurvey.getSurveytakergavetocharity()){ %>
                                        <br/>
                                        <font class="mediumfont">(<%=publicSurvey.getCharityname()%>)</font>
                                    <% } %>
                                    <br/>
                                    <font class="mediumfont">and can earn up to <%=publicSurvey.getSurveyEnhancer().getMaxearning()%> total by posting it to your blog!</font>
                                <% } %>
                                <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey() && publicSurvey.getSurvey().getStatus()<=Survey.STATUS_OPEN){ %>
                                    <font class="mediumfont">Take this survey and earn</font>
                                    <br/>
                                    <font class="largefont" style="font-size: 60px; color: #666666;"><%=publicSurvey.getSurveyEnhancer().getMinearning()%></font>
                                    <% if (publicSurvey.getSurveytakergavetocharity() || publicSurvey.getSurvey().getIscharityonly()){ %>
                                        <br/>
                                        <font class="mediumfont">for charity</font>
                                    <% } %>
                                    <br/>
                                    <font class="mediumfont">or up to <%=publicSurvey.getSurveyEnhancer().getMaxearning()%> when you post it to your blog!</font>
                                    <br/><br/>
                                    <font class="smallfont">(just answer the questions to the left and click Complete the Survey... easy!)</font>
                                <% } %>
                                <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey() && publicSurvey.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                    <font class="mediumfont">People who took this survey earned</font>
                                    <br/>
                                    <font class="largefont" style="font-size: 60px; color: #666666;"><%=publicSurvey.getSurveyEnhancer().getMinearning()%></font>
                                    <br/>
                                    <font class="mediumfont">and then earned up to <%=publicSurvey.getSurveyEnhancer().getMaxearning()%> total by posting it to their blogs!</font>
                                <% } %>
                                <% if (publicSurvey.getSurvey().getIscharityonly()){ %>
                                    <br/><br/>
                                    <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                        <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                        <br/>
                                        <font class="mediumfont">This is a Charity Only survey</font>
                                        <br/>
                                        <font class="tinyfont">The creator of the survey requires that dNeero donate all of your earnings from the survey to a charity of your choice.  It's a chance to do some good!</font>
                                    </div>
                                <% } %>
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
                            We have plenty more <a href="publicsurveylist.jsp">surveys</a> for you to choose from.  And all of them pay!

                            <br/><br/>
                            <div class="rounded" style="background: #ffffff; padding: 5px; margin: 5px; text-align: left;">
                                <table cellpadding="0" cellspacing="0" border="0">


                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Start Date</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurveyEnhancer().getStartdate()%></font>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">End Date</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurveyEnhancer().getEnddate()%></font>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Possible Pay for Survey Response</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurveyEnhancer().getWillingtopayforresponse()%></font>
                                            <br/>
                                            <font class="tinyfont">Assuming you post your survey and it qualifies.</font>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Pay for 1000 Hits on Your Blog</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurveyEnhancer().getWillingtopayforcpm()%></font>
                                        </td>
                                    </tr>


                                     <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Respondents to Date</font>
                                        </td>
                                        <td valign="top">
                                            <%=PercentCompleteBar.get(String.valueOf(publicSurvey.getSurveyEnhancer().getResponsesalreadygotten()), String.valueOf(publicSurvey.getSurvey().getNumberofrespondentsrequested()), "", "", "75")%>
                                            <font class="smallfont">Up to <%=publicSurvey.getSurvey().getNumberofrespondentsrequested()%> people may complete this survey for pay.</font>
                                            <br/><br/>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Blog Displays to Date</font>
                                        </td>
                                        <td valign="top">
                                            <%=PercentCompleteBar.get(String.valueOf(publicSurvey.getSurveyEnhancer().getImpressionsalreadygotten()), String.valueOf(publicSurvey.getSurvey().getMaxdisplaystotal()), "", "", "75")%>
                                            <font class="smallfont">We'll pay for the first <%=publicSurvey.getSurvey().getMaxdisplaystotal()%> displays in blogs.</font>
                                            <br/><br/>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">This Page Displayed</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurvey().getPublicsurveydisplays()%> times</font>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">SurveyID</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurvey().getSurveyid()%></font>
                                        </td>
                                    </tr>

                                 </table>
                            </div>
                            </font>
                        </div>
                    </td>
                <% } %>
            </tr>
        </table>
    <%}%>







<%@ include file="/template/footer.jsp" %>


