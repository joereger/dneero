<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%
PublicSurvey publicSurvey = (PublicSurvey)Pagez.getBeanMgr().get("PublicSurvey");
%>
<%
//If we don't have a surveyid, shouldn't be on this page
if (publicSurvey==null || publicSurvey.getSurvey()==null || publicSurvey.getSurvey().getTitle()==null || publicSurvey.getSurvey().getSurveyid()<=0){
    Pagez.sendRedirect("/publicsurveylist.jsp");
    return;
}
//If the survey is draft or waiting
if (publicSurvey.getSurvey().getStatus()<Survey.STATUS_OPEN){
    Pagez.sendRedirect("/surveynotopen.jsp");
    return;
}
if (request.getParameter("show")!=null && request.getParameter("show").equals("results")){
    //redirect to results
    Pagez.sendRedirect("/surveyresults.jsp?surveyid="+request.getParameter("surveyid")+"&userid="+request.getParameter("userid"));
    return;
}
if (request.getParameter("show")!=null && request.getParameter("show").equals("disclosure")){
    //redirect to disclosure
    Pagez.sendRedirect("/surveydisclosure.jsp?surveyid="+request.getParameter("surveyid")+"&userid="+request.getParameter("userid"));
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = publicSurvey.getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("accesscode")!=null && !request.getParameter("accesscode").equals("")) {
        Pagez.getUserSession().setAccesscode(request.getParameter("accesscode"));
    }
%>
<%
    if (request.getParameter("actionfrompage") != null && request.getParameter("actionfrompage").equals("takesurvey")) {
        try {
            publicSurvey.takeSurvey();
            if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()<=0){
                Pagez.sendRedirect("/blogger/bloggerdetails.jsp");
                return;
            }
            if (!Pagez.getUserSession().getIsfacebookui() && !Pagez.getUserSession().getIsloggedin()){
                Pagez.sendRedirect("/registration.jsp");
                return;
            }
            Pagez.sendRedirect("/surveypostit.jsp?surveyid="+publicSurvey.getSurvey().getSurveyid()+"&justcompletedsurvey=1");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <font class="smallfont"><%=publicSurvey.getSurvey().getDescription()%></font><br/><br/><br/>


    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Share It"><span>Share It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Answers"><span>Answers</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Who's In?"><span>Who's In?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Disclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>

    <%if (!publicSurvey.getQualifiesforsurvey() && publicSurvey.getSurvey().getStatus()!=Survey.STATUS_CLOSED){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
            <font class="mediumfont">Sorry, you're not qualified to join this conversation.  Your qualification is determined by your Profile.  Researchers determine their intended audience when they ignite a conversation.</font>
        </div>
        <br/>
    <%}%>

    <%if (publicSurvey.getBloggerhastakentoomanysurveysalreadytoday() && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
            <font class="mediumfont">Sorry, you've already taken the maximum number of conversations today.  Wait until tomorrow (as defined by U.S. EST) and try again.</font>
        </div>
        <br/>
    <%}%>

    <%if (publicSurvey.getHaveerror()){%>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/alert.png" align="left"/>
        There were errors with your response:
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
                            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                                <center>
                                    <font class="mediumfont"><%=publicSurvey.getUserwhotooksurvey().getFirstname()%> <%=publicSurvey.getUserwhotooksurvey().getLastname()%>'s answers.</font><br/>
                                    <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                                        <a href="#joinconvo"><font class="tinyfont">How would you answer?</font></a>
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
                                <font class="mediumfont" style="font-weight: bold; color: #666666;">Join this conversation to earn up to</font>
                                <br/>
                                <font class="largefont" style="font-size: 30px; color: #666666;"><%=publicSurvey.getSurveyEnhancer().getMinearning()%></font>
                                <% if (publicSurvey.getSurveytakergavetocharity() || publicSurvey.getSurvey().getIscharityonly()){ %>
                                    <br/>
                                    <font class="mediumfont" style="font-weight: bold; color: #666666;">for charity</font>
                                <% } %>
                                <br/>
                                <font class="smallfont" style="font-weight: bold; color: #666666;">or up to <%=publicSurvey.getSurveyEnhancer().getMaxearning()%> depending on your profile traffic. Links to your answers must appear on your profile for at least five days and somebody needs to click them each day to get paid... usually your friends will take care of this on their own. (And yes, we're talking real world money here... we'll pay your PayPal account when you accrue a balance of $20.)</font>
                            </div>
                            <br/>
                        <%}%>
                        <a name="joinconvo"></a>
                        <form action="/survey.jsp" method="post" name="surveyform" style="margin: 0px; padding: 0px;">
                            <input type="hidden" name="dpage" value="/survey.jsp">
                            <input type="hidden" name="actionfrompage" value="takesurvey">
                            <input type="hidden" name="surveyid" value="<%=publicSurvey.getSurvey().getSurveyid()%>">
                            <input type="hidden" name="userid" value="<%=request.getParameter("userid")%>">
                            <input type="hidden" name="referredbyuserid" value="<%=request.getParameter("referredbyuserid")%>">

                        <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                            <center><font class="mediumfont" style="font-weight: bold;">Join the Conversation</font><br/><font class="smallfont" style="font-weight: bold;">This is a conversation so your answers will be visible to the public.</font></center><br/><br/>
                            <div class="rounded" style="background: #ffffff; padding: 10px;">
                                <%if (publicSurvey.getSurvey().getIsaccesscodeonly()){%>
                                    <div class="rounded" style="background: #cccccc;">
                                        <img src="/images/lock-48.png" alt="" width="48" height="48" align="right"/>
                                        <font class="mediumfont" style="color: #666666;">This conversation requires an Access Code</font>
                                        <br/>
                                        <font class="formfieldnamefont" style="color: #666666;">Access Code: </font><%=Textbox.getHtml("accesscode", Pagez.getUserSession().getAccesscode(), 255, 10, "", "")%>
                                        <br/>
                                        <font class="tinyfont" style="color: #666666;">Access Codes are used by conversation igniters to limit the pool of respondents in some way.  Sometimes Access Codes are handed out at an event.  Or at a point of sale.  Or sent out to a select group via email.  Generally speaking, if you don't have the Access Code, this conversation isn't for you.</font>
                                    </div><br/>
                                <%}%>
                                <% if (publicSurvey.getSurvey().getIscharityonly()){ %>
                                    <div class="rounded" style="background: #cccccc;">
                                        <img src="/images/charity-32.png" alt="For Charity" width="32" height="32" align="right"/>
                                        <font class="formfieldnamefont" style="color: #666666;">This is a Charity Only conversation.</font>
                                        <br/>
                                        <font class="tinyfont" style="color: #666666;">The conversation igniter has decided that all earnings from this conversation must be given to charity.</font>
                                    </div><br/>
                                <% } %>
                                <%=publicSurvey.getTakesurveyhtml()%>
                            </div>
                        </div>
                        <%if (publicSurvey.getUserquestionlistitems()!=null && publicSurvey.getUserquestionlistitems().size()>0){%>
                            <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                                <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                                    <font class="formfieldnamefont">Answer these questions from your friends:</font> <font class="formfieldnamefont" style="color: #ff0000;">(Required)</font><br/><br/>
                                    <%
                                        for (Iterator<PublicSurveyUserquestionListitem> iterator=publicSurvey.getUserquestionlistitems().iterator(); iterator.hasNext();){
                                            PublicSurveyUserquestionListitem psli=iterator.next();
                                            %><font class="smallfont" style="font-weight: bold;"><%=psli.getUser().getFirstname()%> <%=psli.getUser().getLastname()%> wants to know:</font><br/><%
                                            %><%=psli.getComponent().getHtmlForInput()%><%
                                            if (iterator.hasNext()){
                                                %><br/><br/><%
                                            }
                                        }
                                    %>
                                </div>
                            </div>
                        <%}%>
                        <%if (publicSurvey.getOptionaluserquestionlistitems()!=null && publicSurvey.getOptionaluserquestionlistitems().size()>0){%>
                            <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                                <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                                    <font class="formfieldnamefont">Answer other questions that interest you:</font> <font class="formfieldnamefont" style="color: #ff0000;">(Optional)</font><br/>
                                    <font class="tinyfont">(These questions are from other users.)</font><br/><br/>
                                    <div style="background : #ffffff; border: 0px solid #ffffff; padding : 5px; width : 400px; height: 150px; overflow : auto;">
                                    <%
                                        try{
                                            //Don't want to pull up all questions so use a random offset
                                            int numbertodisplay = 26;
                                            int minIndexToDisplay = 0;
                                            int maxIndexToDisplay = publicSurvey.getOptionaluserquestionlistitems().size();
                                            if (publicSurvey.getOptionaluserquestionlistitems().size()>numbertodisplay){
                                                int extras = publicSurvey.getOptionaluserquestionlistitems().size() - numbertodisplay;
                                                logger.debug("extras="+extras);
                                                if (extras<1){
                                                    extras = 0;
                                                }
                                                if (extras>publicSurvey.getOptionaluserquestionlistitems().size()){
                                                    extras = 0;
                                                }
                                                logger.debug("extras="+extras);
                                                minIndexToDisplay = Num.randomInt(extras);
                                                maxIndexToDisplay = minIndexToDisplay + numbertodisplay;
                                                if (maxIndexToDisplay>publicSurvey.getOptionaluserquestionlistitems().size()){
                                                    maxIndexToDisplay = publicSurvey.getOptionaluserquestionlistitems().size();
                                                }
                                            }
                                            logger.debug("minIndexToDisplay="+minIndexToDisplay+" maxIndexToDisplay="+maxIndexToDisplay);
                                            int indexCurrentlyShowing = 0;
                                            for (Iterator<PublicSurveyUserquestionListitem> iterator=publicSurvey.getOptionaluserquestionlistitems().iterator(); iterator.hasNext();){
                                                PublicSurveyUserquestionListitem psli=iterator.next();
                                                indexCurrentlyShowing = indexCurrentlyShowing + 1;
                                                if (indexCurrentlyShowing>minIndexToDisplay && indexCurrentlyShowing<maxIndexToDisplay){
                                                    %><font class="smallfont" style="font-weight: bold;"><%=psli.getUser().getFirstname()%> <%=psli.getUser().getLastname()%> wants to know:</font><br/><%
                                                    %><%=psli.getComponent().getHtmlForInput()%><%
                                                    if (iterator.hasNext()){
                                                        %><br/><br/><%
                                                    }
                                                }
                                            }
                                        } catch (Exception ex){
                                            logger.error("", ex);
                                        }
                                    %>
                                    </div>
                                </div>
                            </div>
                        <%}%>
                        <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                            <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                                <font class="formfieldnamefont">What do you want to know?</font><font class="formfieldnamefont" style="color: #ff0000;">(Required)</font><br/><font class="tinyfont">You can ask anything related to this conversation (unrelated/vulgar questions will be rejected). People who join the conversation after reading your answers will have to answer the question you ask.</font><br/>
                                <input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-question" size="50" maxlength="250"/>
                                <br/><br/><font class="formfieldnamefont">How do people answer your question?</font><br/>

                                <table cellspacing="0" cellpadding="0" border="0">
                                    <tr>
                                        <td rowspan="2" valign="top"><input type="radio" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-componenttype" value="MultipleChoice" checked="true"></td>
                                        <td rowspan="2" valign="top">
                                            <img src="/images/clear.gif" alt="" width="1" height="3"><br/><font class="smallfont" style="font-weight: bold;">Multiple Choice (recommended)</font><br/><font class="tinyfont">People can choose from one of the answers you define below.  Type up to eight possible answers.  Leave the rest blank.</font><br/>
                                            <table cellspacing="1" cellpadding="0" border="0">
                                            <tr>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                            </tr>
                                            <tr>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                            </tr>
                                            <tr>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                            </tr>
                                            <tr>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                            </tr>
                                            </table>
                                        </td>
                                        <td valign="top"><input type="radio" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-componenttype" value="ShortText"></td>
                                        <td valign="top"><img src="/images/clear.gif" alt="" width="1" height="3"><br/><font class="smallfont" style="font-weight: bold;">Short Text</font><br/><font class="tinyfont">People can answer whatever they want but it must be shorter than 250 characters.</font></td>

                                    </tr>
                                    <tr>
                                        <td valign="top"><input type="radio" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-componenttype" value="LongText"></td>
                                        <td valign="top"><img src="/images/clear.gif" alt="" width="1" height="3"><br/><font class="smallfont" style="font-weight: bold;">Long Text</font><br/><font class="tinyfont">People can answer whatever they want in a long essay format.</font></td>
                                    </tr>
                                    </table>
                                
                            </div>
                        </div>
                        <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                            <div class="rounded" style="background: #ffffff; padding: 5px;">
                                <table cellpadding="5" cellspacing="0" border="0">
                                    <tr>
                                        <td valign="top" align="left" width="20">
                                            <% if (!publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <input type="checkbox" name="<%=publicSurvey.getDNEERO_REQUEST_PARAM_IDENTIFIER()%>charity-isforcharity" value="1"/>
                                            <% } %>
                                            <% if (publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <input type="hidden" name="<%=publicSurvey.getDNEERO_REQUEST_PARAM_IDENTIFIER()%>charity-isforcharity" value="1"/>
                                            <% } %>
                                        </td>
                                        <td valign="top" width="155" align="left">
                                            <% if (!publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <font class="formfieldnamefont">Don't Pay Me. Give My Earnings to this Charity:</font>
                                            <% } %>
                                            <% if (publicSurvey.getSurvey().getIscharityonly()){ %>
                                                <font class="formfieldnamefont">Earnings From This Conversation Must be Given to Charity:</font>
                                            <% } %>
                                            <br/>
                                            <select name="<%=publicSurvey.getDNEERO_REQUEST_PARAM_IDENTIFIER()%>charity-charityname">
                                                <% if (!publicSurvey.getSurvey().getCharityonlyallowcustom()){ %>
                                                    <option value="Habitat for Humanity">Habitat for Humanity</option>
                                                    <option value="Make-A-Wish Foundation">Make-A-Wish Foundation</option>
                                                    <option value="American Cancer Society">American Cancer Society</option>
                                                    <option value="PetSmart Charities">PetSmart Charities</option>
                                                    <option value="Wikimedia Foundation">Wikimedia Foundation</option>
                                                    <option value="The Conservation Fund">The Conservation Fund</option>
                                                <%}%>
                                                <% if (!publicSurvey.getSurvey().getCharitycustom().equals("")){ %>
                                                    <option value="<%=Str.cleanForHtml(publicSurvey.getSurvey().getCharitycustom())%>"><%=publicSurvey.getSurvey().getCharitycustom()%></option>
                                                <%}%>
                                            </select>
                                            <br/>
                                            <font class="tinyfont">
                                                If you check the box we'll donate all of your earnings for this conversation to the charity of your choice.
                                            </font>
                                        </td>
                                        <td valign="top" align="left">
                                            <font class="tinyfont">
                                            Learn about each of the charities:
                                            <% if (!publicSurvey.getSurvey().getCharityonlyallowcustom()){ %>
                                                <br/><a href="http://www.habitat.org/" target="charity">Habitat for Humanity</a>
                                                <br/><a href="http://www.wish.org/" target="charity">Make-A-Wish Foundation</a>
                                                <br/><a href="http://www.cancer.org/" target="charity">American Cancer Society</a>
                                                <br/><a href="http://www.petsmartcharities.org/" target="charity">PetSmart Charities</a>
                                                <br/><a href="http://en.wikipedia.org/wiki/Wikimedia_Foundation" target="charity">Wikimedia Foundation</a>
                                                <br/><a href="http://www.conservationfund.org/" target="charity">The Conservation Fund</a>
                                            <%}%>
                                            <% if (!publicSurvey.getSurvey().getCharitycustom().equals("")){ %>
                                                <br/><a href="<%=Str.cleanForHtml(publicSurvey.getSurvey().getCharitycustomurl())%>" target="charity"><%=publicSurvey.getSurvey().getCharitycustom()%></a>
                                            <%}%>
                                            </font>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <br/>
                            <center>
                                <input type="submit" class="formsubmitbutton" value="Join the Conversation">
                            </center>
                        </div>
                        </form>
                    <% } %>
                    <% if (publicSurvey.getSurvey().getStatus()!=Survey.STATUS_OPEN && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                        <font class="mediumfont">This conversation is no longer open for respondents.  However, we still have many other conversations that allow you to make money!  Click the Results tab to see how people answered!</font>
                        <br/><br/>
                        <a href="/publicsurveylist.jsp"><font class="mediumfont">Find Another Conversation</font></a>
                    <% } %>
                    <br/><br/><br/><br/>
                    <font class="smallfont"><%=publicSurvey.getSocialbookmarklinks()%></font>
                </td>
                <% if (Pagez.getUserSession().getIsfacebookui()){ %>
                    <td valign="top" align="left">
                        <%if (publicSurvey.getUserwhotooksurvey()!=null && publicSurvey.getUserwhotooksurvey().getUserid()>0 && publicSurvey.getSurveytakergavetocharity()){%>
                            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                                <center>
                                    <font class="tinyfont" style="font-weight: bold;"><%=publicSurvey.getUserwhotooksurvey().getFirstname()%> <%=publicSurvey.getUserwhotooksurvey().getLastname()%> had dNeero donate all earnings from this conversation to <%=publicSurvey.getCharityname()%>.</font>
                                </center>
                            </div>
                        <%}%>
                        <% if (publicSurvey.getFacebookuserswhotooksurvey()!=null){ %>
                            <font class="mediumfont" style="color: #cccccc;">Friends Who Took It:</font><br/>
                            <%
                                for (Iterator<PublicSurveyFacebookFriendListitem> iterator=publicSurvey.getFacebookuserswhotooksurvey().iterator(); iterator.hasNext();){
                                    PublicSurveyFacebookFriendListitem publicSurveyFacebookFriendListitem= iterator.next();
                                    %>
                                    <a href="/survey.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>&userid=<%=publicSurveyFacebookFriendListitem.getUserid()%>&responseid=<%=publicSurveyFacebookFriendListitem.getResponseid()%>"><font class="tinyfont" style="color: #0000ff;"><%=publicSurveyFacebookFriendListitem.getFacebookUser().getFirst_name()%> <%=publicSurveyFacebookFriendListitem.getFacebookUser().getLast_name()%></font></a><br/>
                                    <%
                                }
                            %>
                            <br/><br/>
                        <%}%>
                        <% if (publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                            <font class="mediumfont" style="color: #cccccc;">You've Joined this Conversation:</font><br/>
                            <a href="/survey.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>&userid=<%=Pagez.getUserSession().getUser().getUserid()%>"><font class="tinyfont" style="color: #0000ff;">Your Answers</font></a><br/><br/>
                            <br/><br/>
                        <%}%>
                    </td>
                <% } %>
                <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                    <td valign="top" align="left">
                        <div class="rounded" style="background: #00ff00;">
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                <center><img src="/images/paste-128.png" width="128" height="128"/></center>
                                <br/>
                                <% if (publicSurvey.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                    <div class="rounded" style="background: #cccccc; text-align: center;">
                                        <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                        <br/>
                                        <font class="mediumfont">This conversation is closed.</font>
                                    </div>
                                    <br/>
                                <% } %>
                                <% if (publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                                    <font class="mediumfont">You've already earned</font>
                                    <font class="mediumfont">You've already earned</font>
                                    <br/>
                                    <font class="tinyfont">(pending posting verification)</font>
                                    <br/>
                                    <%
                                    if (publicSurvey.getSurveyEnhancer()==null){
                                        logger.debug("publicSurvey.getSurveyEnhancer() is null!!!");   
                                    }
                                    %>
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
                                    <font class="mediumfont">Join this conversation and earn</font>
                                    <br/>
                                    <font class="largefont" style="font-size: 60px; color: #666666;"><%=publicSurvey.getSurveyEnhancer().getMinearning()%></font>
                                    <% if (publicSurvey.getSurveytakergavetocharity() || publicSurvey.getSurvey().getIscharityonly()){ %>
                                        <br/>
                                        <font class="mediumfont">for charity</font>
                                    <% } %>
                                    <br/>
                                    <font class="mediumfont">or up to <%=publicSurvey.getSurveyEnhancer().getMaxearning()%> when you post it to your blog!</font>
                                    <br/><br/>
                                    <font class="smallfont">(just answer the questions to the left and click Join the Conversation... easy!)</font>
                                <% } %>
                                <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey() && publicSurvey.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                    <font class="mediumfont">People who joined this conversation earned</font>
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
                                        <font class="mediumfont">This is a Charity Only conversation</font>
                                        <br/>
                                        <font class="tinyfont">The conversation igniter requires that dNeero donate all of your earnings from the conversation to a charity of your choice.  It's a chance to do some good!</font>
                                    </div>
                                <% } %>
                            </div>
                            <br/>

                            <font class="smallfont">


                            <br/><br/><b>Get paid to share your opinion.</b><br/>
                            dNeero pays people to fill out conversations and post their answers to their peers.

                            <br/><br/><b>Your answers appear on your blog</b><br/>
                            However you answer, your friends will see it.  Express yourself!

                            <br/><br/><b>You need to register and qualify to get paid</b><br/>
                            Once you answer the questions you'll need to register and fill out a short profile that asks some basic demographic questions.  If you qualify for this conversation then you'll get paid.

                            <br/><br/><b>You need to be honest</b><br/>
                            We aren't paying you for any particular opinion.  We're paying you for your time and for the exposure on your blog.  If you don't like a product or service you need to be honest about that fact.

                            <br/><br/><b>Stimulate conversation</b><br/>
                            Engage companies who are actively seeking your feedback on products and services.  At the same time, engage your blog readers in discussion of the same product.

                            <br/><br/><b>No gimmicks.</b><br/>
                            This is the real deal.  A simple model that respects your privacy and allows you to control what you blog about and when.  We pay for activity you choose to engage in.

                            <br/><br/><b>If you don't like this conversation</b><br/>
                            We have plenty more <a href="/publicsurveylist.jsp">conversations</a> for you to choose from.  And all of them pay!

                            <br/><br/>
                            <div class="rounded" style="background: #ffffff; padding: 10px; margin: 5px; text-align: left;">
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
                                            <font class="formfieldnamefont">Possible Pay for Conversation Response</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurveyEnhancer().getWillingtopayforresponse()%></font>
                                            <br/>
                                            <font class="tinyfont">Assuming you post your conversation and it qualifies.</font>
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
                                            <font class="smallfont">Up to <%=publicSurvey.getSurvey().getNumberofrespondentsrequested()%> people may join this conversation for pay.</font>
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
                                            <font class="formfieldnamefont">ID</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=publicSurvey.getSurvey().getSurveyid()%></font>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Referred by</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=Pagez.getUserSession().getPendingSurveyReferredbyuserid()%></font>
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


