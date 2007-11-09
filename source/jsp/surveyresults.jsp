<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getTitle()%>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="smallfont"><%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getDescription()%></font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getSurveyid()%>">Disclosure</a>
    -->

    <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="450">
                <t:panelTabbedPane id="resultspanel" bgcolor="#ffffff" selectedIndex="<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getResultstabselectedindex()%>">
                    <t:panelTab id="resultspanel_a" label="Everybody">
                        <h:graphicImage url="/images/clear.gif" width="415" height="1"/><br/>
                        <f:verbatim escape="false"><%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getResultsHtml()%></f:verbatim>
                    </t:panelTab>
                    <t:panelTab id="resultspanel_b" label="<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getResultsfriendstabtext()%>" rendered="${(!empty publicSurveyResults.userwhotooksurvey) and (publicSurveyResults.userwhotooksurvey.userid gt 0)}">
                        <h:graphicImage url="/images/clear.gif" width="415" height="1"/><br/>
                        <table width="100%" cellpadding="10" cellspacing="0" border="0">
                            <tr>
                                <td valign="top">
                                    <f:verbatim escape="false"><%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getResultsHtmlForUserWhoTookSurvey()%></f:verbatim>
                                </td>
                                <t:div rendered="#{1 eq 1}">
                                    <td valign="top">
                                         <!-- List of friends who've taken and those who've not -->
                                    </td>
                                </t:div>
                            </tr>
                        </table>

                    </t:panelTab>
                    <t:panelTab id="resultspanel_c" label="Your Friends" rendered="<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getResultsshowyourfriendstab()%>">
                        <h:graphicImage url="/images/clear.gif" width="415" height="1"/><br/>
                        <f:verbatim escape="false"><%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getResultsYourFriends()%></f:verbatim>
                    </t:panelTab>
                </t:panelTabbedPane>
            </td>
            <% if ("<%=((!userSession)Pagez.getBeanMgr().get("!userSession")).getIsfacebookui()%>){ %>
                <td valign="top" align="left">
                    <div class="rounded" style="background: #00ff00;">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <center><img src="/images/statistic-128.png" width="128" height="128"/></center>
                            <br/>
                            <% if ("${publicSurveyResults.survey.status ge 5}){ %>
                                <div class="rounded" style="background: #cccccc; text-align: center;">
                                    <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                    <br/>
                                    <h:outputText value="This survey is closed." styleClass="mediumfont"/>
                                </div>
                                <br/>
                            <% } %>
                            <% if ("<%=((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getIscharityonly()%>){ %>
                                <br/><br/>
                                <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                    <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                    <br/>
                                    <h:outputText value="This is a Charity Only survey." styleClass="mediumfont"/>
                                    <br/>
                                    <h:outputText value="The creator of the survey requires that dNeero donate all of your earnings from the survey to a charity of your choice.  It's a chance to do some good!" styleClass="tinyfont"/>
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


