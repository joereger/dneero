<%@ page import="com.dneero.dao.Pl" %>
<%@ page import="com.dneero.dao.Researcher" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.User" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.privatelabel.PlPeers" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%
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
//If the survey isn't peered with this pl
Pl plOfSurvey = Pl.get(publicSurvey.getSurvey().getPlid());
if (!PlPeers.isThereATwoWayTrustRelationship(plOfSurvey, Pagez.getUserSession().getPl())){
    Pagez.getUserSession().setMessage("No Private Label Peering Agreement Exists between plOfSurvey("+plOfSurvey.getPlid()+") and userSession.getPl("+Pagez.getUserSession().getPl().getPlid()+").");
    Pagez.sendRedirect("/notauthorized.jsp");
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
                Pagez.sendRedirect("/login.jsp");
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


<%
if(true){
    logger.error("pre-surveyintabs!!!!!");
}
%>


    <% Survey surveyInTabs = publicSurvey.getSurvey();%>
    <%@ include file="/template/calpers/jspoverride/surveytabs.jsp" %>
    <%--<font class="mediumfont" style="color: #cccccc;">Your Conversation</font>--%>
    <%--<br/>--%>
<%
if(true){
    logger.error("post-surveyintabs!!!!!");    
}
%>

    <%if (!publicSurvey.getQualifiesforsurvey() && publicSurvey.getSurvey().getStatus()!=Survey.STATUS_CLOSED){%>
        <%if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                <font class="mediumfont">Sorry, you're not qualified to join this <%=Pagez._survey()%>.  Your qualification is determined by your Profile.  People determine their intended audience when they create a <%=Pagez._survey()%>.</font>
            </div>
            <br/>
        <%}%>
    <%}%>

    <%if (publicSurvey.getBloggerhastakentoomanysurveysalreadytoday() && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
            <font class="mediumfont">Sorry, you've already taken the maximum number of <%=Pagez._surveys()%> today.  Wait until tomorrow (as defined by U.S. EST) and try again.</font>
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
                <td valign="top" width="65%">
                <% if (publicSurvey.getUserwhotooksurvey()!=null){ %>
                     <% if (publicSurvey.getUserwhotooksurvey().getUserid()>0){ %>
                        <%if (!publicSurvey.getIsuserwhotooksurveysameasloggedinuser()){%>
                            <center>
                            <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                                <center>
                                    <font class="mediumfont"><%=publicSurvey.getUserwhotooksurvey().getNickname()%>'s answers.</font><br/>
                                    <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                                        <!--<a href="survey.jsp#joinconvo"><font class="tinyfont">How would you answer?</font></a>-->
                                    <% } %>
                                </center>
                                <br/>
                                <center><%=publicSurvey.getSurveyResponseHtml()%></center>
                            </div>
                            </center>
                            <br/><br/>
                          <% } %>
                       <% } %>
                   <% } %>
                   <% if ((publicSurvey.getSurvey().getStatus()==Survey.STATUS_OPEN || (publicSurvey.getSurvey().getStatus()==Survey.STATUS_CLOSED && publicSurvey.getLoggedinuserhasalreadytakensurvey())) && ((Pagez.getUserSession().getFacebookUser()!=null && Pagez.getUserSession().getFacebookUser().getHas_added_app()) || !Pagez.getUserSession().getIsfacebookui())){ %>
                        <%if((publicSurvey.getLoggedinuserhasalreadytakensurvey() && publicSurvey.getIsuserwhotooksurveysameasloggedinuser()) || !publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
                            <%if (Pagez.getUserSession().getIsfacebookui() && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
                                <div class="rounded" style="background: #ffffff; text-align: center;">
                                    <font class="mediumfont" style="font-weight: bold; color: #666666;">Join this <%=Pagez._survey()%> to earn</font>
                                    <br/>
                                    <font class="largefont" style="font-size: 20px; color: #666666;"><%=publicSurvey.getSurvey().getIncentive().getFullSummaryHtml()%></font>
                                    <% if (publicSurvey.getSurveytakergavetocharity() || publicSurvey.getSurvey().getIscharityonly()){ %>
                                        <br/>
                                        <font class="mediumfont" style="font-weight: bold; color: #666666;">for charity</font>
                                    <% } %>
                                    <br/>
                                    <% if (publicSurvey.getSurveyEnhancer().getMaxEarningCPMDbl()>0){ %>
                                        <font class="smallfont" style="font-weight: bold; color: #666666;">You'll also earn up to an additional <%=publicSurvey.getSurveyEnhancer().getMaxEarningCPM()%> depending on your profile traffic.</font>
                                    <%}%>
                                    <br/><font class="smallfont" style="font-weight: bold; color: #666666;">Links to your answers must appear on your profile for at least five days and somebody needs to click them each day to get paid... usually your friends will take care of this on their own.</font>
                                </div>
                                <br/>
                            <%}%>

                            <a name="joinconvo"></a>
                            <form action="/survey.jsp" method="post" name="surveyform" style="margin: 0px; padding: 0px;" class="niceform">
                                <input type="hidden" name="dpage" value="/survey.jsp">
                                <input type="hidden" name="actionfrompage" value="takesurvey">
                                <input type="hidden" name="surveyid" value="<%=publicSurvey.getSurvey().getSurveyid()%>">
                                <input type="hidden" name="userid" value="<%=request.getParameter("userid")%>">
                                <%if (publicSurvey.getResponse()!=null){%>
                                    <input type="hidden" name="responseid" value="<%=publicSurvey.getResponse().getResponseid()%>">
                                <%}%>
                                <input type="hidden" name="referredbyuserid" value="<%=request.getParameter("referredbyuserid")%>">

                            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                                <br clear="all"/>
                                <%--<%if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>--%>
                                    <%--<font class="mediumfont" style="font-weight: bold; color: #666666;">Enter the <%=Pagez._Survey()%></font>--%>
                                <%--<%}else{%>--%>
                                    <%--<font class="mediumfont" style="font-weight: bold; color: #666666;">Edit your Answers</font>--%>
                                <%--<%}%>--%>
                                <%--<br/>--%>
                                <font class="largefont" style="color: #666666;"><%=publicSurvey.getSurvey().getTitle()%></font>
                                <br/>
                                <font class="smallfont"><%=publicSurvey.getSurvey().getDescription()%></font>
                                <br/><br/><br/>
                                <div class="rounded" style="background: #ffffff; padding: 10px;">
                                    <%
                                    //MAIN CONVO HERE
                                    //MAIN CONVO HERE
                                    //MAIN CONVO HERE
                                    %>
                                    <%=publicSurvey.getTakesurveyhtml()%>
                                </div>
                            </div>
                            <%if (1==2 && publicSurvey.getUserquestionlistitems()!=null && publicSurvey.getUserquestionlistitems().size()>0){%>
                                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                                    <div class="rounded" style="background: #ffffff; padding: 10px; text-align: left;">
                                        <font class="formfieldnamefont">Answer these questions from your friends:</font> <font class="formfieldnamefont" style="color: #ff0000;">(Required)</font><br/><br/>
                                        <%
                                            for (Iterator<PublicSurveyUserquestionListitem> iterator=publicSurvey.getUserquestionlistitems().iterator(); iterator.hasNext();){
                                                PublicSurveyUserquestionListitem psli=iterator.next();
                                                %><font class="smallfont" style="font-weight: bold;"><%=psli.getUser().getNickname()%> wants to know:</font><br/><%
                                                %><%=psli.getComponent().getHtmlForInput(publicSurvey.getResponse())%><%
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
                                        <font class="formfieldnamefont">Questions by other Values Super Jammers... answer those that interest you:</font> <font class="formfieldnamefont" style="color: #000000;">(Optional)</font><br/>
                                        <font class="tinyfont"></font><br/><br/>
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
                                                    if (indexCurrentlyShowing>=minIndexToDisplay && indexCurrentlyShowing<=maxIndexToDisplay){
                                                        %><font class="smallfont" style="font-weight: bold;"><%=psli.getUser().getNickname()%> wants to know:</font><br/><%
                                                        %><%=psli.getComponent().getHtmlForInput(publicSurvey.getResponse())%><%
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
                                    <font class="formfieldnamefont">Ask Other Values Super Jammers a Question</font> <font class="formfieldnamefont" style="color: #ff0000;"></font><br/><font class="tinyfont">You can ask other Values Super Jammers anything related to this <%=Pagez._survey()%>. Those who join after you will have the opportunity to answer your question.<br/><br/><i>Our Jam will only grow if you ask questions. Please share a question, even if it's been asked before. If you don't have a question right now, it's OK ... just enter "none at this time".</i></font><br/>
                                    <br/><font class="formfieldnamefont">Type a Question in the Box Below</font><br/>
                                    <input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-question" size="50" value="<%=Str.cleanForHtml(publicSurvey.getYourquestion())%>" maxlength="500"/>
                                    <br/><br/><font class="formfieldnamefont">How do people answer your question?</font><br/>

                                    <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
                                            <%
                                            String ismultchoice = "";
                                            if (publicSurvey.getYourquestionismultiplechoice()){
                                                ismultchoice = " checked";
                                            }
                                            %>
                                            <td rowspan="2" valign="top"><input type="radio" <%=ismultchoice%> name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-componenttype" value="MultipleChoice"></td>
                                            <td rowspan="2" valign="top">
                                                <img src="/images/clear.gif" alt="" width="1" height="3"><br/><font class="smallfont" style="font-weight: bold;">Multiple Choice</font><br/><font class="tinyfont">People can choose from one of the answers you define below.  Type up to eight possible answers.  Leave the rest blank.</font><br/>
                                                <table cellspacing="1" cellpadding="0" border="0">
                                                <tr>
                                                    <%String tmp = "";%>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>0){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(0);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>1){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(1);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                </tr>
                                                <tr>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>2){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(2);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>3){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(3);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                </tr>
                                                <tr>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>4){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(4);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>5){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(5);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                </tr>
                                                <tr>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>6){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(6);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                    <%if (publicSurvey.getYourquestionmultiplechoiceoptions()!=null && publicSurvey.getYourquestionmultiplechoiceoptions().size()>7){tmp = publicSurvey.getYourquestionmultiplechoiceoptions().get(7);} else {tmp="";}%>
                                                    <td><input type="text" name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-predefinedanswer" value="<%=Str.cleanForHtml(tmp)%>" size="20" maxlength="30" style="font-size: 9px;"/></td>
                                                </tr>
                                                </table>
                                            </td>
                                            <%
                                            String isshorttext = "";
                                            if (publicSurvey.getYourquestionisshorttext()){
                                                isshorttext = " checked";
                                            }
                                            %>
                                            <td valign="top"><input type="radio" <%=isshorttext%> name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-componenttype" value="ShortText"></td>
                                            <td valign="top"><img src="/images/clear.gif" alt="" width="1" height="3"><br/><font class="smallfont" style="font-weight: bold;">Short Text</font><br/><font class="tinyfont">People can answer whatever they want but it must be shorter than 500 characters.</font></td>

                                        </tr>
                                        <tr>
                                            <%
                                            String islongtext = "";
                                            if (publicSurvey.getYourquestionislongtext()){
                                                islongtext = " checked";
                                            }
                                            %>
                                            <td valign="top"><!--<input type="radio" <%=islongtext%> name="<%=SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER%>userquestion-componenttype" value="LongText">--></td>
                                            <td valign="top"><!--<img src="/images/clear.gif" alt="" width="1" height="3"><br/><font class="smallfont" style="font-weight: bold;">Long Text</font><br/><font class="tinyfont">People can answer whatever they want in a long essay format.</font>--></td>
                                        </tr>
                                        </table>

                                </div>
                            </div>
                            <% if (!publicSurvey.getSurvey().getIsfree()){%>
                                <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 10px;">
                                    <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
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
                                                        <font class="formfieldnamefont">Earnings From This <%=Pagez._Survey()%> Must be Given to Charity:</font>
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
                                                        If you check the box we'll donate all of your earnings for this <%=Pagez._survey()%> to the charity of your choice.
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
                                    <%}%>

                                </div>
                            <% } %>
                            <br/>
                            <center>
                                <% if (!publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
                                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Enter the <%=Pagez._Survey()%>">
                                <%}else{%>
                                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Edit Your Answers">
                                <%}%>
                            </center>
                            </form>
                        <% } %>
                    <% } %>
                    <% if (publicSurvey.getSurvey().getStatus()!=Survey.STATUS_OPEN && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){ %>
                        <font class="mediumfont">This <%=Pagez._survey()%> is no longer open for respondents.  However, we still have many others!  Click the Results tab to see how people answered!</font>
                        <br/><br/>
                        <a href="/publicsurveylist.jsp"><font class="mediumfont">Find Another <%=Pagez._Survey()%></font></a>
                    <% } %>
                    <%--<br/><br/><br/><br/>--%>
                    <%--<font class="smallfont"><%=publicSurvey.getSocialbookmarklinks()%></font>--%>
                </td>

                    <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                        <td valign="top" align="left">
                            <div class="rounded" style="background: #e6e6e6;">
                                <% if (publicSurvey.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                    <div class="rounded" style="background: #f6f6f6; text-align: center;">
                                        <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                        <br/>
                                        <font class="mediumfont">This <%=Pagez._survey()%> is closed.</font>
                                    </div>
                                    <br/>
                                <% } %>
                                <%if (publicSurvey.getSurvey().getIsaccesscodeonly() && !publicSurvey.getLoggedinuserhasalreadytakensurvey()){%>
                                    <div class="rounded" style="background: #f6f6f6; text-align: left;">
                                        <img src="/images/lock-48.png" alt="" width="48" height="48" align="right"/>
                                        <font class="mediumfont" style="color: #666666;">This <%=Pagez._survey()%> requires an Access Code</font>
                                        <br/>
                                        <font class="formfieldnamefont" style="color: #666666;">Access Code: </font><%=Textbox.getHtml("accesscode", Pagez.getUserSession().getAccesscode(), 255, 10, "", "")%>
                                        <br/>
                                        <font class="tinyfont" style="color: #666666;">Access Codes are used by marketers to limit the pool of respondents in some way.  Sometimes Access Codes are handed out at an event.  Or at a point of sale.  Or sent out to a select group via email.  Generally speaking, if you don't have the Access Code, this <%=Pagez._survey()%> isn't for you.</font>
                                    </div>
                                    <br/>
                                <%}%>
                                <% if (publicSurvey.getSurvey().getIscharityonly()){ %>
                                    <div class="rounded" style="background: #f6f6f6; text-align: left;">
                                        <img src="/images/charity-32.png" alt="For Charity" width="32" height="32" align="right"/>
                                        <font class="formfieldnamefont" style="color: #666666;">This is a Charity Only <%=Pagez._Survey()%>.</font>
                                        <br/>
                                        <font class="tinyfont" style="color: #666666;">The <%=Pagez._survey()%> creator has decided that all earnings from this <%=Pagez._survey()%> must be given to charity.</font>
                                    </div>
                                    <br/>
                                <% } %>
                            <% } %>




                           <%--<center>--%>
                           <%--<img src="/images/info-128.png" alt="" width="128" height="128"/>--%>
                           <%--</center>--%>
                           <%--<br/>--%>
                                <!-- Stats -->
                                <div class="rounded" style="background: #f6f6f6; text-align: left;">
                                    <table cellpadding="0" cellspacing="5" border="0">



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
                                                <font class="formfieldnamefont">Respondents</font>
                                            </td>
                                            <td valign="top">
                                                <%if (publicSurvey.getSurvey()!=null && publicSurvey.getSurvey().getResponses()!=null){%>
                                                    <font class="smallfont"><%=publicSurvey.getSurvey().getResponses().size()%></font>
                                                    <br/><br/>
                                                <%} else {%>
                                                    <font class="smallfont">0</font>
                                                    <br/><br/>
                                                <%}%>
                                            </td>
                                        </tr>


                                        <%--<tr>--%>
                                            <%--<td valign="top">--%>
                                                <%--<font class="formfieldnamefont">Displays to Date</font>--%>
                                            <%--</td>--%>
                                            <%--<td valign="top">--%>
                                                    <%--<font class="smallfont"><%=publicSurvey.getSurveyEnhancer().getImpressionsalreadygotten()%></font>--%>
                                                    <%--<br/><br/>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>


                                        <%--<tr>--%>
                                            <%--<td valign="top">--%>
                                                <%--<font class="formfieldnamefont">This Page Displayed</font>--%>
                                            <%--</td>--%>
                                            <%--<td valign="top">--%>
                                                <%--<font class="smallfont"><%=publicSurvey.getSurvey().getPublicsurveydisplays()%> times</font>--%>
                                            <%--</td>--%>
                                        <%--</tr>--%>


                                     </table>
                                </div>
                                <!-- End Stats -->


                           <br/>
                           <font class="normalfont" style="color: #000000;">
                               <%--<b>Welcome to the conversation!</b>  These survey-based conversations are done transparently and openly. They build community and discussion while putting our collective finger on the pulse of our organization.--%>

                               <br/><br/>
                               <b>Instructions</b>
                               <ol>
                                   <li>Give your thoughts - Answer the questions to the left</li>
                                   <li>Create new ideas - Ask questions you want others to consider</li>
                                   <li>Tap in to what people think - Go to the What People Are Saying tab to see how people reply</li>
                               </ol>


                               <br/><br/>
                               <b>Core Values</b>
                               <ul>
                                   <li><b>Quality</b> - Strive to meet internal and external customers' needs through innovation, competence and teamwork. Seek to "do it right" the first time.</li>
                                   <li><b>Respect</b> - Be sensitive to the needs of others, both within the organization and outside of the organization. Be courteous, considerate, responsive, and professional.</li>
                                   <li><b>Integrity</b> - In all endeavors, act in an ethical, honest, and professional manner.</li>
                                   <li><b>Openness</b> - Be willing to listen to and share information with others. Be trusting and receptive to new ideas. Trusting.</li>
                                   <li><b>Accountability</b> - Take ownership and responsibility for actions and their results. Accept both risks and rewards, trusting that good-faith risks will not be punished.</li>
                               </ul>




                               <%--<br/><br/>--%>
                               <%--<b>Your answers will be posted publicly within the CalPERS network.  We ask you to observe the following guidelines for participation</b>:--%>
                               <%--<ul>--%>
                               <%--<li>--%>
                               <%--when responding/participating please follow our CalPERS'--%>
                               <%--values: quality, respect, integrity, openness, accountability--%>
                               <%--</li>--%>
                               <%--<li>--%>
                               <%--respond with an open mind and listen to understand--%>
                               <%--</li>--%>
                               <%--<li>--%>
                               <%--assume that people have positive intent toward the future of--%>
                               <%--the organization and that they seek to do what's best for our--%>
                               <%--members -- as you do also--%>
                               <%--</li>--%>
                               <%--<li>--%>
                               <%--be nice, play fair--%>
                               <%--</li>--%>
                               <%--</ul>--%>
                           <%--<br/><br/>--%>
                           <%--<b>We Will Know We are Successful When</b>--%>
                               <%--<br/>--%>
                               <%--In the first ever Values Super Jam we--%>
                               <%--will measure success by the:--%>
                               <%--<ul>--%>
                               <%--<li>--%>
                               <%--level of participation - in particular, how many of us engage in--%>
                               <%--the conversation--%>
                               <%--</li>--%>
                               <%--<li>--%>
                               <%--learning - what we learn from the conversation itself, and from the questions we ask each other--%>
                               <%--</li>--%>
                               <%--<li>--%>
                               <%--how the experience is rated - near the end of the--%>
                               <%--2 days, we will ask you to provide feedback on the experience--%>
                               <%--and process... please give us five stars!--%>
                               <%--</li>--%>
                               <%--</ul>--%>
                           <%--<br/><br/>--%>
                           <%--We want you to engage.  We want you to shape the future.  We want you to inspire each other with hope, optimism and action.--%>
                           <%--<br/><br/>--%>
                           <%--Innovation.  Synergy.  Teamwork.  Collaboration.  ROI.--%>

                           </font>

                        </font>
                    </div>





                </td>
            </tr>
        </table>
    <%}%>



<%@ include file="/template/footer.jsp" %>


