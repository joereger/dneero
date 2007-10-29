<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Social Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <c:if test="#{userSession.isfacebookui and publicSurveyList.facebookjustaddedapp}">
        <!--/*
          *
          *  If this tag is being served on a secure (SSL) page, you must replace
          *  'http://www.trianads.com/adserver/www/delivery/.. .'
          * with
          *  'https://www.trianads.com/adserver/www/delivery/...'
          *
          *  To help prevent caching of this tracker beacon, if possible,
          *  Replace %%RANDOM_NUMBER%% with a randomly generated number (or timestamp)
          *
          *  Place this code at the top of your post-add URL page, just after the <body> tag.
          *
          */-->
        <div id='m3_tracker_4' style='position: absolute; left: 0px; top: 0px; visibility: hidden;'><img src=' http://www.trianads.com/adserver/www/delivery/ti.php?trackerid=4&amp;cb=#{publicSurveyList.rndstr}' width='0' height='0' alt='' /></div> 
    </c:if>


    <table cellpadding="0" border="0" width="100%">
        <tr>
            <td valign="top">


                <!--<t:saveState id="save" value="#{publicSurveyList}"/>-->
                <h:outputText value="There are currently no surveys listed.  Please check back soon as we're always adding new ones!" styleClass="mediumfont" rendered="#{empty publicSurveyList.surveys}"/>

                <t:dataTable sortable="true" id="datatable" value="#{publicSurveyList.surveys}" rows="100" var="srvy" rendered="#{!empty publicSurveyList.surveys}" styleClass="dataTable" headerClass="" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
                  <t:column>
                    <f:facet name="header">
                        <h:outputText value=""/>
                    </f:facet>
                    <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                        <table cellpadding="0" border="0" width="100%">
                            <tr>
                                <td>
                                    <h:outputLink value="/survey.jsf?surveyid=#{srvy.surveyid}" style="text-decoration: none;">
                                        <h:outputText value="#{srvy.title}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"/>
                                    </h:outputLink><br/>
                                    <h:outputText value="#{srvy.description}" escape="false" styleClass="tinyfont"/><br/><br/>
                                    <font class="tinyfont"><b>#{srvy.daysuntilend}</b></font>
                                </td>
                                <td width="25%">
                                    <div class="rounded" style="background: #ffffff; padding: 10px;">
                                        <center>
                                            <font class="tinyfont"><b>Earn Up To</b></font><br/>
                                            <font class="mediumfont">#{srvy.maxearning}</font>
                                            <c:if test="#{srvy.ischarityonly}">
                                                <br/><font class="tinyfont"><b>for charity</b></font>
                                            </c:if>
                                        </center>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                  </t:column>
                </t:dataTable>
                <!--
                <t:dataScroller id="scroll_1" for="datatable"  fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
                    <f:facet name="first" >
                        <t:graphicImage url="/images/datascroller/play-first.png" border="0" />
                    </f:facet>
                    <f:facet name="last">
                        <t:graphicImage url="/images/datascroller/play-forward.png" border="0" />
                    </f:facet>
                    <f:facet name="previous">
                        <t:graphicImage url="/images/datascroller/play-back.png" border="0" />
                    </f:facet>
                    <f:facet name="next">
                        <t:graphicImage url="/images/datascroller/play.png" border="0" />
                    </f:facet>
                </t:dataScroller>
                -->

            </td>

            <c:if test="#{userSession.isfacebookui}">
                <td valign="top" width="50%" style="padding-top: 6px;">
                    <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                        <c:if test="#{1 eq 1 or userSession.isloggedin}">
                            <div class="rounded" style="background: #ffffff; padding: 10px;">
                                <font class="formfieldnamefont" style="color: #666666;">Current Balance:</font>
                                <br/><font class="largefont" style="color: #cccccc;">#{publicSurveyList.accountBalance.currentbalance}</font>
                                <c:if test="#{!empty publicSurveyList.accountBalance}">
                                    <c:if test="#{publicSurveyList.accountBalance.pendingearningsDbl gt 0}">
                                        <br/><h:commandLink value="Pending: #{publicSurveyList.accountBalance.pendingearnings}" action="#{bloggerCompletedsurveys.beginView}" styleClass="formfieldnamefont" style="color: #0000ff;" rendered="#{userSession.isloggedin and (userSession.user.bloggerid gt 0)}"/>
                                    </c:if>
                                </c:if>
                                <br/><font class="tinyfont" style="color: #666666; font-weight: bold;">(yes, we're talking real world money here)</font>
                            </div>
                            <br/><br/>
                        </c:if>




                        <font class="mediumfont">Surveys Friends Have Taken:</font><br/>
                        <c:if test="${empty publicSurveyList.facebookSurveyThatsBeenTakens}">
                            <font class="tinyfont">Your friends haven't taken any surveys yet.</font>
                        </c:if>
                        <c:forEach var="surveyfriendstook" items="${publicSurveyList.facebookSurveyThatsBeenTakens}">
                            <h:outputLink value="/survey.jsf?surveyid=#{surveyfriendstook.survey.surveyid}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"><h:outputText>#{surveyfriendstook.survey.title}</h:outputText></h:outputLink><br/>
                            <font class="smallfont">
                                How they answered:
                                <c:forEach var="friendwhotook" items="${surveyfriendstook.facebookSurveyTakers}">
                                    <h:outputLink value="/survey.jsf?surveyid=#{surveyfriendstook.survey.surveyid}&amp;userid=#{friendwhotook.userid}&amp;responseid=#{friendwhotook.responseid}" styleClass="smallfont" style="color: #0000ff;"><h:outputText>#{friendwhotook.facebookUser.first_name} #{friendwhotook.facebookUser.last_name}</h:outputText></h:outputLink>
                                </c:forEach>
                            </font><br/><br/>
                        </c:forEach>

                        <br/><br/>
                        <font class="mediumfont">Friends on dNeero:</font><br/>
                        <c:if test="${empty publicSurveyList.facebookuserswhoaddedapp}">
                            <font class="tinyfont">None, yet.</font><br/>
                        </c:if>
                        <h:panelGrid columns="4" cellpadding="2" border="0">
                            <c:forEach var="fbuser" items="${publicSurveyList.facebookuserswhoaddedapp}">
                                <h:panelGroup>
                                    <img src="#{fbuser.facebookUser.pic_square}" width="50" height="50" border="0" align="middle" alt=""/><br/>
                                    <font class="tinyfont" style="font-weight: bold;">#{fbuser.facebookUser.first_name} #{fbuser.facebookUser.last_name}</font><br/><br/>
                                </h:panelGroup>
                            </c:forEach>
                        </h:panelGrid>
                        <br/>
                        <center><a href="#{publicSurveyList.invitefriendsurl}" target="top"><font class="normalfont" style="font-weight: bold; color: #0000ff;">Invite Friends Who Aren't On dNeero</font></a></center>
                        <br/><br/>
                        <!--
                        <font class="mediumfont">Tell Friends Who Aren't Yet Using dNeero:</font><br/>
                        <h:commandButton action="#{publicSurveyList.tellFriends}" value="Tell Friends" styleClass="formsubmitbutton"/>
                        -->
                    </div>
                </td>
            </c:if>

        </tr>
    </table>

</h:form>

</ui:define>


</ui:composition>
</html>



