<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Social Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <% if ("#{userSession.isfacebookui and publicSurveyList.facebookjustaddedapp}){ %>
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
        <div id='m3_tracker_4' style='position: absolute; left: 0px; top: 0px; visibility: hidden;'><img src=' http://www.trianads.com/adserver/www/delivery/ti.php?trackerid=4&amp;cb=<%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getRndstr()%>' width='0' height='0' alt='' /></div> 
    <% } %>


    <table cellpadding="0" border="0" width="100%">
        <tr>
            <td valign="top">


                <!--<t:saveState id="save" value="#{publicSurveyList}"/>-->
                <h:outputText value="There are currently no surveys listed.  Please check back soon as we're always adding new ones!" styleClass="mediumfont" rendered="#{empty publicSurveyList.surveys}"/>

                <t:dataTable sortable="true" id="datatable" value="<%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getSurveys()%>" rows="100" var="srvy" rendered="#{!empty publicSurveyList.surveys}" styleClass="dataTable" headerClass="" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
                  <t:column>
                    <f:facet name="header">
                        <h:outputText value=""/>
                    </f:facet>
                    <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                        <table cellpadding="0" border="0" width="100%">
                            <tr>
                                <td>
                                    <h:outputLink value="/survey.jsf?surveyid=<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getSurveyid()%>" style="text-decoration: none;">
                                        <h:outputText value="<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getTitle()%>" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"/>
                                    </h:outputLink><br/>
                                    <h:outputText value="<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getDescription()%>" escape="false" styleClass="tinyfont"/><br/><br/>
                                    <font class="tinyfont"><b><%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getDaysuntilend()%></b></font>
                                </td>
                                <td width="25%">
                                    <div class="rounded" style="background: #ffffff; padding: 10px;">
                                        <center>
                                            <font class="tinyfont"><b>Earn Up To</b></font><br/>
                                            <font class="mediumfont"><%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getMaxearning()%></font>
                                            <% if ("<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getIscharityonly()%>){ %>
                                                <br/><font class="tinyfont"><b>for charity</b></font>
                                            <% } %>
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

            <% if ("<%=((UserSession)Pagez.getBeanMgr().get("UserSession")).getIsfacebookui()%>){ %>
                <td valign="top" width="50%" style="padding-top: 6px;">
                    <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                        <% if ("#{1 eq 1 or userSession.isloggedin}){ %>
                            <div class="rounded" style="background: #ffffff; padding: 10px;">
                                <font class="formfieldnamefont" style="color: #666666;">Current Balance:</font>
                                <br/><font class="largefont" style="color: #cccccc;"><%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getAccountBalance().getCurrentbalance()%></font>
                                <% if ("#{!empty publicSurveyList.accountBalance}){ %>
                                    <% if ("#{publicSurveyList.accountBalance.pendingearningsDbl gt 0}){ %>
                                        <br/><h:commandLink value="Pending: <%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getAccountBalance().getPendingearnings()%>" action="<%=((BloggerCompletedsurveys)Pagez.getBeanMgr().get("BloggerCompletedsurveys")).getBeginView()%>" styleClass="formfieldnamefont" style="color: #0000ff;" rendered="#{userSession.isloggedin and (userSession.user.bloggerid gt 0)}"/>
                                    <% } %>
                                <% } %>
                                <br/><font class="tinyfont" style="color: #666666; font-weight: bold;">(yes, we're talking real world money here)</font>
                            </div>
                            <br/><br/>
                        <% } %>




                        <font class="mediumfont">Surveys Friends Have Taken:</font><br/>
                        <% if ("${empty publicSurveyList.facebookSurveyThatsBeenTakens}){ %>
                            <font class="tinyfont">Your friends haven't taken any surveys yet.</font>
                        <% } %>
                        <c:forEach var="surveyfriendstook" items="<%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getFacebookSurveyThatsBeenTakens()%>">
                            <h:outputLink value="/survey.jsf?surveyid=<%=((Surveyfriendstook)Pagez.getBeanMgr().get("Surveyfriendstook")).getSurvey().getSurveyid()%>" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"><h:outputText><%=((Surveyfriendstook)Pagez.getBeanMgr().get("Surveyfriendstook")).getSurvey().getTitle()%></h:outputText></h:outputLink><br/>
                            <font class="smallfont">
                                How they answered:
                                <c:forEach var="friendwhotook" items="<%=((Surveyfriendstook)Pagez.getBeanMgr().get("Surveyfriendstook")).getFacebookSurveyTakers()%>">
                                    <h:outputLink value="/survey.jsf?surveyid=<%=((Surveyfriendstook)Pagez.getBeanMgr().get("Surveyfriendstook")).getSurvey().getSurveyid()%>&amp;userid=<%=((Friendwhotook)Pagez.getBeanMgr().get("Friendwhotook")).getUserid()%>&amp;responseid=<%=((Friendwhotook)Pagez.getBeanMgr().get("Friendwhotook")).getResponseid()%>" styleClass="smallfont" style="color: #0000ff;"><h:outputText><%=((Friendwhotook)Pagez.getBeanMgr().get("Friendwhotook")).getFacebookUser().getFirst_name()%> <%=((Friendwhotook)Pagez.getBeanMgr().get("Friendwhotook")).getFacebookUser().getLast_name()%></h:outputText></h:outputLink>
                                </c:forEach>
                            </font><br/><br/>
                        </c:forEach>

                        <br/><br/>
                        <font class="mediumfont">Friends on dNeero:</font><br/>
                        <% if ("${empty publicSurveyList.facebookuserswhoaddedapp}){ %>
                            <font class="tinyfont">None, yet.</font><br/>
                        <% } %>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <c:forEach var="fbuser" items="<%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getFacebookuserswhoaddedapp()%>">
                                <td valign="top">
                                    <img src="<%=((Fbuser)Pagez.getBeanMgr().get("Fbuser")).getFacebookUser().getPic_square()%>" width="50" height="50" border="0" align="middle" alt=""/><br/>
                                    <font class="tinyfont" style="font-weight: bold;"><%=((Fbuser)Pagez.getBeanMgr().get("Fbuser")).getFacebookUser().getFirst_name()%> <%=((Fbuser)Pagez.getBeanMgr().get("Fbuser")).getFacebookUser().getLast_name()%></font><br/><br/>
                                </td>
                            </c:forEach>
                        </table>
                        <br/>
                        <center><a href="<%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getInvitefriendsurl()%>" target="top"><font class="normalfont" style="font-weight: bold; color: #0000ff;">Invite Friends Who Aren't On dNeero</font></a></center>
                        <br/><br/>
                        <!--
                        <font class="mediumfont">Tell Friends Who Aren't Yet Using dNeero:</font><br/>
                        <h:commandButton action="<%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getTellFriends()%>" value="Tell Friends" styleClass="formsubmitbutton"/>
                        -->
                    </div>
                </td>
            <% } %>

        </tr>
    </table>



<%@ include file="/jsp/templates/footer.jsp" %>



