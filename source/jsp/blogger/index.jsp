<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"
      xmlns:c="http://java.sun.com/jstl/core"

      >

<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title"><img src="/images/user.png" alt="" border="0" width="128" height="128" align="right"/>For Bloggers<br/><br clear="all"/></ui:define>
    <ui:param name="navtab" value="bloggers"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>

    <h:form>




    <c:if test="${!userSession.isloggedin or bloggerIndex.showmarketingmaterial}">

        <table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="70%">
                    <div style="margin: 15px;">
                        <font class="mediumfont" style="color: #999999">Quick Summary</font>
                        <br/>
                        dNeero helps bloggers make money filling out surveys and posting their answers to their blogs.
                        <br/><br/>
                        After filling out a survey here at dNeero.com you've already made some money.  But you can make more by posting your answers to your blog (all you have to do is copy-and-paste a single line of code).  Your blog readers will see your answers along with any other thoughts you had about the survey.  Your readers can then complete the survey, or see how others from your blog answered. If they complete the survey, we pay you a recruitment fee based on what they earn.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How it Works for Bloggers</font>
                        <br/>
                        The process is quick and easy.  We guide you through each step with wizards that tell you what to do next.  Here are the steps:
                        <br/><br/>
                        <img src="/images/blogger-diagram.gif" width="475" height="403" border="0"></img>
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Blogs and Surveys: So Happy Together</font>
                        <br/>
                        To post a survey to your blog you just copy-and-paste a single line of code.  Here's the result:
                        <br/>
                        <img src="/images/survey-in-blog.gif" width="475" height="555" border="0"></img>
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Avoid Losing Any Credibility... and Support a Great Cause!</font>
                        <br/>
                        One of the big concerns from bloggers is that the money in this model presents possible bias, and that your readers will not want to feel 'monetized'. Thanks to open and sharing people like <a href="http://www.ck-blog.com/">CK</a> we decided to make donating to charity a way to shed this concern -- now you can give any of the earnings to a good cause, avoid bias... and increase the features within your posts!
                        <br/><br/>
                        With a single click you can direct earnings from any survey to a charity of your choosing.  Learn more about the program <a href="/charity.jsf">here</a>.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How to Get Started</font>
                        <br/>
                        Sign Up is free.  We collect some basics like email and password and then send you an email message to activate your account.  After your first log in we'll collect some basic demographic information (age, gender, location, etc.) so that we can find the best opportunities for you.  We'll present you a list of survey opportunities, you'll complete surveys of your choosing and within minutes you'll see an account balance.  We pay your PayPal account whenever you accrue $20 in your account... no waiting unitl the end of calendar quarters!
                    </div>
                    <table width="100%">
                        <tr>
                            <td width="50%" align="center">
                                <div style="width: 200px;"><d:greenRoundedButton pathtoapproot="../"><h:commandLink value="Read the Blogger FAQ" action="bloggerfaq" styleClass="subnavfont" style="color: #ffffff;"/></d:greenRoundedButton></div>
                            </td>
                            <td width="50%" align="center">
                                <div style="width: 200px;"><d:greenRoundedButton pathtoapproot="../" rendered="#{!userSession.isloggedin}"><h:commandLink value="Sign Up Now" action="#{registration.beginView}" styleClass="subnavfont" style="color: #ffffff;"/></d:greenRoundedButton></div>
                            </td>
                        </tr>
                    </table>
               </td>
               <td valign="top" width="30%">
                    <center>
                    <h:commandLink action="#{publicSurveyList.beginView}"><img src="/images/blogger-check-out-opps.gif" width="190" height="132" border="0"></img></h:commandLink>
                    </center>
                    <br/><br/>
                    <div class="rounded" style="background: #eeeeee;">
                        <font class="largefont">$<h:outputText value="#{systemStats.dollarsavailabletobloggers}" styleClass="largefont"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText></font>
                        <br/>
                        <font class="mediumfont">waiting to be earned by bloggers!</font>
                    </div>
               </td>
           </tr>
        </table>


        <br/>


    </c:if>

    

    <c:if test="${userSession.isloggedin and (userSession.user.bloggerid gt 0) and (!bloggerIndex.showmarketingmaterial)}">
        <t:div rendered="#{bloggerIndex.msg ne ''}">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont">#{bloggerIndex.msg}</font>
            </div>
       </t:div>

        <t:div rendered="#{bloggerIndex.responsependingmsg ne ''}">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <img src="/images/alert.png" border="0" align="right"/>
                <font class="mediumfont"><f:verbatim escape="false">#{bloggerIndex.responsependingmsg}</f:verbatim></font>
            </div>
            <br/><br/>
        </t:div>

        <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="250" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Find Surveys to Take" action="#{publicSurveyList.beginView}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Make money taking surveys and posting your answers to your blog.</font>
                            </td></tr></table>
                        </div>
                        <!--<c:if test="#{!empty bloggerCompletedsurveys.listrecent}">
                            <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                                <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                    <font class="mediumfont" style="color: #596697;">Payment Status for Recent Surveys:</font>
                                </td></tr>
                                <tr><td valign="top"></td><td valign="top">
                                    <c:forEach var="completedsurvey" items="${bloggerCompletedsurveys.listrecent}">
                                        <h:outputLink value="/survey.jsf?surveyid=#{completedsurvey.surveyid}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"><h:outputText>#{completedsurvey.surveytitle}</h:outputText></h:outputLink><br/>
                                        <font class="smallfont">
                                            <f:verbatim escape="false">#{completedsurvey.response.responsestatushtml}</f:verbatim>
                                        </font><br/><br/>
                                    </c:forEach>
                                </td></tr></table>
                            </div>
                        </c:if>-->
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Earn Money Inviting Friends" action="#{bloggerEarningsRevshare.beginView}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Invite friends and earn money when they take surveys!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Earnings from Completed Surveys" action="#{bloggerIndex.beginView}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">See how much you've earned.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Update Blogger Profile" action="#{bloggerDetails.beginView}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Your profile helps us find surveys that fit your interests.  Keep it up to date.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Blogger Frequently Asked Questions" action="bloggerfaq" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Get your answers here!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Blogger Basic Info" action="#{bloggerIndex.beginView}" styleClass="mediumfont" style="color: #596697;"><f:param name="showmarketingmaterial" value="1"/></h:commandLink>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Basic Blogger information, how the system works, etc.</font>
                            </td></tr></table>
                        </div>
                    </div>
                </td>
                <td valign="top">
                    <!--
                    <h:outputText value="Surveys You Qualify For" styleClass="largefont" style="color: #cccccc;" escape="false"/>
                    <t:div rendered="#{empty bloggerSurveyList.surveys}">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                            <font class="smallfont">There are currently no surveys targeted to your demographic profile but we're always adding new ones so check back soon!</font>
                        </div>
                        <br/><br/>
                    </t:div>
                    <t:saveState id="save" value="#{bloggerSurveyList}"/>
                    <t:dataTable id="datatable" value="#{bloggerSurveyList.surveys}" rows="25" var="srvy" rendered="#{!empty bloggerSurveyList.surveys}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcolnowrap,tcolnowrap">
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Title"/>
                        </f:facet>
                        <h:commandLink action="#{publicSurveyTakeRedirector.beginView}">
                            <h:outputText value="#{srvy.title}" escape="false" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"/>
                            <f:param name="surveyid" value="#{srvy.surveyid}" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Questions"/>
                        </f:facet>
                        <h:outputText value="#{srvy.numberofquestions}" styleClass="smallfont"/>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Timing"/>
                        </f:facet>
                        <h:outputText value="#{srvy.daysuntilend}" styleClass="smallfont"/>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Earn Up To"/>
                        </f:facet>
                        <h:outputText value="#{srvy.maxearning}" styleClass="smallfont"/>
                      </h:column>
                    </t:dataTable>
                    <t:dataScroller id="scroll_1" for="datatable" rendered="#{!empty bloggerSurveyList.surveys}" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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

                    <br/><br/>
                    -->

                    <h:outputText value="Surveys You've Completed" styleClass="largefont" style="color: #cccccc;" escape="false"/>
                    <t:div rendered="#{empty bloggerCompletedsurveys.list}">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                            <font class="smallfont">
                            <h:outputText value="You haven't yet completed any surveys." styleClass="smallfont" style="padding-right:8px;"/>
                            <h:commandLink action="#{publicSurveyList.beginView}">
                            <h:outputText value="Find Surveys to Take" escape="false" styleClass="smallfont" style="font-weight: bold; color: #0000ff;"/>
                            </h:commandLink>
                            </font>
                        </div>
                        <br/><br/>
                    </t:div>

                    <c:if test="#{!empty bloggerCompletedsurveys.list}">
                        <c:forEach var="completedsurvey" items="${bloggerCompletedsurveys.list}">
                            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                                <table cellpadding="2" cellspacing="0" border="0" width="100%">
                                    <tr>
                                        <td valign="top">
                                            <h:outputText value="#{completedsurvey.responsedate}" styleClass="tinyfont"/><br/>
                                            <h:outputLink value="/survey.jsf?surveyid=#{completedsurvey.surveyid}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"><h:outputText>#{completedsurvey.surveytitle}</h:outputText></h:outputLink><br/>
                                            <h:outputText value="Est earnings: #{completedsurvey.amttotal}" styleClass="tinyfont" style="font-weight: bold;"/>
                                            <c:if test="${!userSession.isfacebookui}">
                                                <br/>
                                                <h:outputLink value="/survey.jsf?surveyid=#{completedsurvey.surveyid}" rendered="#{completedsurvey.response.poststatus eq 0}" styleClass="tinyfont" style="font-weight:bold;"><h:outputText>Needs to be Posted</h:outputText></h:outputLink>
                                                <h:outputText value="Posted At Least Once" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 1}"/>
                                                <h:outputText value="Posted Successfully" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 2}"/>
                                                <h:outputText value="Too Late to Post" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 3}"/>
                                            </c:if>
                                            <br/>
                                            <h:commandLink action="#{bloggerImpressions.beginView}">
                                                <h:outputText value="#{completedsurvey.totalimpressions}" styleClass="tinyfont" style="font-weight:bold; text-decoration: none; " escape="false"/>
                                                <f:param name="surveyid" value="#{completedsurvey.surveyid}" />
                                           </h:commandLink><h:outputText value=" impressions" styleClass="tinyfont" style="font-weight:bold;"/>
                                        </td>
                                        <td valign="top" align="right">
                                            <f:verbatim escape="false">#{completedsurvey.response.responsestatushtml}</f:verbatim>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <br/>
                        </c:forEach>
                        <font class="tinyfont" style="color: #666666;">Survey statuses update nightly. Remember, you must leave the survey on your mini-feed and profile to generate clicks for 5 days in the 10 after you take it to get paid.  Days that qualify are marked green.</font>
                    </c:if>

                    <t:div rendered="#{!empty bloggerCompletedsurveys.list}">
                        <br/><br/>
                        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
                        Note: Earnings calculations are estimated and not final.   Final payment notification and calculation can be found on <h:commandLink  action="#{accountBalance.beginView}" styleClass="smallfont" style="padding-left: 8px;">Your Account Balance</h:commandLink> page. Posting and payment status both update nightly.
                        </font></div></center>
                    </t:div>


                </td>

            </tr>
        </table>
    </c:if>


    </h:form>



    </ui:define>
</ui:composition>
</html>