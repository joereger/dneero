<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "#{publicSurveyDisclosure.survey.title}";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="smallfont">#{publicSurveyDisclosure.survey.description}</font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=#{publicSurveyDisclosure.survey.surveyid}">Disclosure</a>
    -->


    <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
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
            <c:if test="${!userSession.isfacebookui}">
                <td valign="top" align="left">
                    <div class="rounded" style="background: #00ff00;">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <center><img src="/images/exclamation-128.png" width="128" height="128"/></center>
                            <br/>
                            <c:if test="${publicSurveyDisclosure.survey.status ge 5}">
                                <div class="rounded" style="background: #cccccc; text-align: center;">
                                    <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                    <br/>
                                    <h:outputText value="This survey is closed." styleClass="mediumfont"/>
                                </div>
                                <br/>
                            </c:if>
                            <c:if test="#{publicSurveyDisclosure.survey.ischarityonly}">
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
                        <br/><br/><b>For more info:</b><br/>
                        Click the Questions tab on this page.
                        </font>
                    </div>
                </td>
            </c:if>
        </tr>
    </table>







<%@ include file="/jsp/templates/footer.jsp" %>


