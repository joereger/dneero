<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyDisclosure" %>
<%@ page import="com.dneero.dao.Survey" %>
<%
PublicSurveyDisclosure publicSurveyDisclosure = (PublicSurveyDisclosure)Pagez.getBeanMgr().get("PublicSurveyDisclosure");
%>
<%
//If we don't have a surveyid, shouldn't be on this page
if (publicSurveyDisclosure.getSurvey()==null || publicSurveyDisclosure.getSurvey().getTitle()==null || publicSurveyDisclosure.getSurvey().getSurveyid()<=0){
    Pagez.sendRedirect("/publicsurveylist.jsp");
    return;
}
//If the survey is draft or waiting
if (publicSurveyDisclosure.getSurvey().getStatus()<Survey.STATUS_OPEN){
    Pagez.sendRedirect("/surveynotopen.jsp");
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicSurveyDisclosure) Pagez.getBeanMgr().get("PublicSurveyDisclosure")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



    <font class="smallfont"><%=publicSurveyDisclosure.getSurvey().getDescription()%></font><br/><br/><br/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="450">
                <font class="largefont" style="color: #666666;">Paid Survey Disclosure</font>
                <br/><br/>
                <font class="normalfont">
                dNeero paid for this survey.  dNeero did not and does not encourage the blogger/social networker to post any particular opinion or answers.  dNeero encourages the blogger/social networker to simply tell the truth.
                <br/><br/>
                dNeero pays the blogger/social networker for completing the survey and for the number of times that the survey is displayed on his or her blog or social network profile.
                <br/><br/>
                A link to this disclosure statement is required (and is automatically posted) with each dNeero survey to protect the integrity of the blogosphere and social networks.
                </font>
            </td>
            <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                <td valign="top" align="left">
                    <div class="rounded" style="background: #00ff00;">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <center><img src="/images/exclamation-128.png" width="128" height="128"/></center>
                            <br/>
                            <% if (publicSurveyDisclosure.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                <div class="rounded" style="background: #cccccc; text-align: center;">
                                    <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                    <br/>
                                    <font class="mediumfont">This survey is closed.</font>
                                </div>
                                <br/>
                            <% } %>
                            <% if (publicSurveyDisclosure.getSurvey().getIscharityonly()){ %>
                                <br/><br/>
                                <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                    <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                    <br/>
                                    <font class="mediumfont">This is a Charity Only survey.</font>
                                    <br/>
                                    <font class="tinyfont">The creator of the survey requires that dNeero donate all of your earnings from the survey to a charity of your choice.  It's a chance to do some good!</font>
                                </div>
                            <% } %>
                        </div>
                        <br/>
                        <font class="smallfont">
                        <br/><br/><b>For more info:</b><br/>
                        Click the Questions tab on this page.
                        </font>
                    </div>
                </td>
            <% } %>
        </tr>
    </table>







<%@ include file="/template/footer.jsp" %>


