<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyResults" %>
<%@ page import="com.dneero.dao.Survey" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
PublicSurveyResults publicSurveyResults = (PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults");
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="smallfont"><%=publicSurveyResults.getSurvey().getDescription()%></font><br/><br/><br/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="450">
                <t:panelTabbedPane id="resultspanel" bgcolor="#ffffff" selectedIndex="<%=publicSurveyResults.getResultstabselectedindex()%>">
                    <t:panelTab id="resultspanel_a" label="Everybody">
                        <img src="/images/clear.gif" width="415" height="1"/><br/>
                        <%=publicSurveyResults.getResultsHtml()%>
                    </t:panelTab>
                    <t:panelTab id="resultspanel_b" label="<%=publicSurveyResults.getResultsfriendstabtext()%>" rendered="${(!empty publicSurveyResults.userwhotooksurvey) and (publicSurveyResults.userwhotooksurvey.userid gt 0)}">
                        <img src="/images/clear.gif" width="415" height="1"/><br/>
                        <table width="100%" cellpadding="10" cellspacing="0" border="0">
                            <tr>
                                <td valign="top">
                                    <%=publicSurveyResults.getResultsHtmlForUserWhoTookSurvey()%>
                                </td>
                            </tr>
                        </table>
                    </t:panelTab>
                    <t:panelTab id="resultspanel_c" label="Your Friends" rendered="<%=publicSurveyResults.getResultsshowyourfriendstab()%>">
                        <img src="/images/clear.gif" width="415" height="1"/><br/>
                        <%=publicSurveyResults.getResultsYourFriends()%>
                    </t:panelTab>
                </t:panelTabbedPane>
            </td>
            <% if (Pagez.getUserSession().getIsfacebookui()){ %>
                <td valign="top" align="left">
                    <div class="rounded" style="background: #00ff00;">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <center><img src="/images/statistic-128.png" width="128" height="128"/></center>
                            <br/>
                            <% if (publicSurveyResults.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                <div class="rounded" style="background: #cccccc; text-align: center;">
                                    <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                    <br/>
                                    <font class="mediumfont">This survey is closed.</font>
                                </div>
                                <br/>
                            <% } %>
                            <% if (publicSurveyResults.getSurvey().getIscharityonly()){ %>
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






<%@ include file="/jsp/templates/footer.jsp" %>


