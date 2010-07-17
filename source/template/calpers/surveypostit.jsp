<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyPostit" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
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


    <% if (!publicSurveyPostit.getLoggedinuserhasalreadytakensurvey()){ %>
        <font class="mediumfont" style="color: #666666;">Join the Conversation!</font>
        <br/>
        <font class="normalfont" style="color: #666666;">Once you <a href="/survey.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>">join the conversation</a> you'll be able to share it with others.</font>
    <%}%>


    <% if (publicSurveyPostit.getLoggedinuserhasalreadytakensurvey()){ %>

        <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
            <table cellpadding="3" cellspacing="15" border="0" width="100%">
            <tr>
                <td valign="top" width="35%">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <img src="/images/ok-128.png" alt="" width="128" height="128"/><br/>
                            <font class="mediumfont" style="color: #666666;">You've Entered this <%=Pagez._Survey()%>!  Your voice has been heard by all of CalPERS as we create our future!</font>
                        </div>
                        <br/>
                </td>
                <td valign="top" class="posttoblog_tabs_tablewidth">
                    <div class="rounded" style="background: #e6e6e6;">



                    <table cellpadding="3" cellspacing="15" border="0" width="400">
                    <tr>
                        <td valign="top">
                            <font class="largefont" style="color: #666666;">What's Next?</font>
                            <br/>
                            <font class="normalfont">
                                <ul>
                                    <li>See the <a href="/surveyresults.jsp?surveyid=<%=publicSurveyPostit.getSurvey().getSurveyid()%>">results</a> unfold in realtime!<br/><br/></li>
                                    <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyPostit.getSurvey().getSurveyid()%>">Discuss</a> the conversation and results with your fellow CalPERS team members.<br/><br/></li>
                                    <li>The <a href="/">Values Super Jam Blog</a> will be updated with new conversations and results analysis... check back soon! Or subscribe via <a href="/rss.xml">rss</a>.<br/><br/></li>
                                    <li>Optionally, <a href="#" id="togglepagelink">share your answers</a> within the organization by posting them to a webpage, blog, intranet or forum (conversations will only be visible from the CalPERS network even if you post to the web at large.)<br/><br/></li>
                                </ul>

                            </font>
                        </td>
                    </tr>
                    </table>



                        <%--<a href="#" id="togglepagelink"><font class="tinyfont">Embed Your Answers</font></a>--%>




                    <div id="togglepage">
                    <!-- Start hidable stuff -->




                    <br/><br/>
                    <font class="mediumfont">Embed Your Answers</font>
                    <br/>
                    <font class="smallfont">
                        <ol>
                            <li>Copy the code below. (Right-click and choose Copy or Left-click to select and hit Control-C)<br/>
                            <%if (publicSurveyPostit.getSurvey().getEmbedflash()){%>
                                <textarea rows="3" cols="25" readonly="readonly" onClick="javascript:this.select();"><%=publicSurveyPostit.getHtmltoposttoblogflash()%></textarea>
                            <%} else {%>
                                Sorry, this <%=Pagez._survey()%> does not allow Flash embedding.
                            <%}%>
                            </li>
                            <li>Login into your publishing tool of choice.</li>
                            <li>Go to the page that allows you to create a blog entry, web page or other digital asser.</li>
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

                    </div>
                </td>
            </tr>
        </table>
        <% } %>
        <br/>
    <% } %>







<%@ include file="/template/footer.jsp" %>


