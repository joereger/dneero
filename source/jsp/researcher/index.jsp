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
    <ui:define name="title"><img src="/images/statistic-128.png" alt="" border="0" width="128" height="128" align="right"/>For Researchers and Marketers<br/><br clear="all"/></ui:define>
    <ui:param name="navtab" value="researchers"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="true"/>

    <h:form>
    <h:messages styleClass="RED"/>
    <c:if test="${!userSession.isloggedin or researcherIndex.showmarketingmaterial}">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
           <tr>
               <td valign="top" width="70%">
                    <div style="margin: 15px;">
                        <font class="mediumfont" style="color: #999999">Executive Summary</font>
                        <br/>
                        dNeero allows you to create multi-question surveys and pay bloggers to both respond to it and post their answers on their blogs.  You can include images, video and rich html in your survey for product photos, diagrams and whatever else you'd like the blogger to see and post to their blog.  The power in this approach is that respondents know that their answers will be viewed by their peers, a much more powerful force in their lives.  Results from dNeero's methodology give you a better sense of how a concept is portrayed in modern social networks.  dNeero also provides a means to track the movement of responses through small-scale social networks.  It's a new tool in the struggle to understand human behavior.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Major Benefits</font>
                        <br/>
                        Engage bloggers on their own turf for more accurate market data (<b>ethnographic research</b> - bloggers post to their blogs so you'll hear what they tell their peers about your product... not what they'll tell a researcher)
                        <br/><br/>
                        Generate product awareness while you collect data (<b>market research meets marketing</b>) - Each blogger who responds also has friends and family who read their blog.
                        <br/><br/>
                        Low risk, pay as you go: define max budget and <b>only pay for respondents and impressions that actually happen</b>
                        <br/><br/>
                        Quick and easy: <b>set up a campaign online in minutes</b>
                        <br/><br/>
                        <b>Full disclosure</b> that the blogger has been paid avoids misrepresentation and other ethical snafus
                        <br/><br/>
                        Customize the look of the survey to <b>include logos, product pictures, clickable links</b>, etc... survey taking meets guerilla marketing
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">How it Works for Researchers</font>
                        <ol>
                        <li><b>Create a survey</b> composed of various question types (Text, Essay, Likert Scale, Matrix, Multiple Choice, etc.)</li>
                        <li><b>Define your target blogger demographic</b> by birthdate, gender, ethnicity, marital status, income, education, state, city, profession and politics.</li>
                        <li><b>Define how much you'll pay</b> for a survey response and how much you'll pay for 1000 posts of that survey response to a blog (CPM).  There are many incentive strategies you can implement.</li>
                        <li><b>dNeero will publicize your survey</b> and find bloggers who want to respond to it. You'll pay for only those responses and survey displays that happen.</li>
                        <li><b>dNeero tracks survey responses and blog impressions</b>, providing you with simple billing and results data</li>
                        </ol>
                        <br/>
                        <font class="mediumfont" style="color: #999999">dNeero Targets the Thought Leaders... the Outliers</font>
                        <br/>
                        Bloggers, by the nature of their activity, are thought leaders within their social network.  dNeero's concept focuses on these thought leaders.  In Tipping Point terminology these are the Mavens or Salespeople... those who bring knowledge and advocacy to their group.  dNeero is not well-suited to the discovery of trends across entire populations... there are better phone-based and polling-based tools out there for such projects.  dNeero gives you access to an increasingly-potent and somewhat-untouchable segment of society that uses technology to shape the opinions of others.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Frame the Conversation to Get the Data You Need</font>
                        <br/>
                        Other tools like Umbria and BuzzMetrics are great at finding out what people are saying about your product.  But this approach makes it difficult to see how people feel about something in particular.  Often you need to drill down or focus on a specific point.  With dNeero you're able to frame the conversation in the form of a survey.  This gets a base set of feedback from all bloggers who respond to it.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Two Birds with One Stone</font>
                        <br/>
                        Create buzz while you collect valuable market intelligence.  By the nature of dNeero's model you are disclosing your research to the public.  This may not work for some projects.  But for many this is powerful way to see a concept's play in the real world.  It is a two-way interaction between you and your constituents. Books like The Cluetrain Manifesto argue persuasively that the organizations that will succeed in the future are the ones who are most able to embrace a transparent model of interaction with their constituents.  Consumers, voters and members are all screaming for meaningful input into the product design and policy processes of the organizations they patronize.  By creating smart dNeero campaigns that expose strategic elements at the right time while collecting valuable feedback your organization can gain a solid competitive advantage.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Research Data is More Accurate Because Peers are Involved</font>
                        <br/>
                        Research data gained through dNeero is more reflective of how people actually act in their peer group because the answers they provide will be shared on their blog or social network.  In many traditional forms of market research the subject is apt to provide an answer that seems appropriate to the researcher but is not the actual answer that they'll use with their peers.  As a researcher you're much more interested in how they present their beliefs and views to their peers because these views drive market adoption and purchasing decisions.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Include Images in Surveys</font>
                        <br/>
                        Images allow survey takers to answer questions about a new product design, etc.  And by including an image in the survey you are actually putting that image onto many blog sites at once.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Social Influence Rating (TM)</font>
                        <br/>
                        Bloggers post their survey answers to their blogs.  Their readers are given the opportunity to take the same survey.  We track this influence and incorporate blog traffic with the amount of skewing that the first blogger's answers resulted in (against the norm for the survey) and create a Social Influence Rating.  This helps you zero in on those bloggers that are effective in your space.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Standing Panels of Bloggers for Longitudinal Studies</font>
                        <br/>
                        Create a panel of bloggers and approach them with surveys over time to do longitudinal research in the blogosphere.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">The Charity Only Option</font>
                        <br/>
                        With a single check box you can designate a survey as Charity Only.  Doing so means that only bloggers who agree to have all of their earnings from the survey donated to a charity will be able to take it. Learn more about the program <a href="/charity.jsf">here</a>.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Get Started Now, Low Commitment</font>
                        <br/>
                        It's easy to create an account and build a survey.  You won't be charged until you launch a survey, at which point it will be obvious that you're about to be charged.  Log in. Explore.  Ask us questions.  It's a new concept and we're excited to see how you apply it!
                    </div>
                    <table width="100%">
                        <tr>
                            <td width="50%" align="center">
                                <div style="width: 200px;"><d:greenRoundedButton pathtoapproot="../"><h:commandLink value="Read the Researcher FAQ" action="researcherfaq" styleClass="subnavfont" style="color: #ffffff;"/></d:greenRoundedButton></div>
                            </td>
                            <td width="50%" align="center">
                                <div style="width: 200px;"><d:greenRoundedButton pathtoapproot="../" rendered="#{!userSession.isloggedin}"><h:commandLink value="Sign Up Now" action="#{registration.beginView}" styleClass="subnavfont" style="color: #ffffff;"/></d:greenRoundedButton></div>
                            </td>
                        </tr>
                    </table>
               </td>
               <td valign="top" width="30%">
                    <div class="rounded" style="background: #eeeeee;">
                        <div class="rounded" style="background: #ffffff;">
                        <font class="smallfont">
                            <img src="/images/redbutton-disclosure.gif" align="left"/><br clear="all"/>All blog posts must disclose the fact that the blogger was paid to take the survey.  This ensures that the balance of trust in the blogosphere remains controlled by bloggers and their readers.  dNeero is dedicated to a free, excited and motivated blogosphere.  We want to bring researchers and marketers to the party and we want them to thrive, but we need to make sure they don't burn the couch before they go.
                            <br/><br clear="all"/>
                            <img src="/images/redbutton-neutrality.gif" align="left"/><br clear="all"/>Bloggers are never required to post positive reviews.  dNeero defends the the blogger's freedom to thrash any product by forcing researchers to remain neutral.  This, of course, increases the quality of market research garnered by the process and avoids the sugging quagmire.  Paying people to be fake is not what dNeero is about.  In fact, the prospect of such companies terrifies us... they degrade the blogosphere and reduce the credibility of research done in it.
                            <br/><br clear="all"/>
                            <img src="/images/redbutton-twoway.gif" align="left"/><br clear="all"/>Unlike other pay-for-blog schemes dNeero is a two-way process.  We'll stop short of claiming full Cluetrain credentials.  But the fact is that a researcher generates exposure and collects information from a market at the same time.  This is a first step to lowering the walls between the marketing department and the market research department.  dNeero believes that in the digital age outbound messaging and inbound communication should be united.  We didn't make this stuff up... go read Cluetrain.
                        </font>
                        </div>
                    </div>
               </td>
           </tr>
        </table>




    </c:if>

    <c:if test="${userSession.isloggedin and (userSession.user.researcherid eq 0)}">
        <center>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF; width: 65%;">
            <h:commandLink action="researcherdetails"><font class="mediumfont">Quick One-time Researcher Configuration Required</font></h:commandLink>
            <br/>
            <font class="smallfont">Before you can do blogosphere research we need to collect a few pieces of information about you as a researcher.  This will only take a minute or two.</font>
            <br/><br/><br/>
            <h:commandLink action="researcherdetails"><font class="normalfont">Continue</font></h:commandLink>
        </div>
        </center>
    </c:if>

    <c:if test="${userSession.isloggedin and (userSession.user.researcherid gt 0) and (!researcherIndex.showmarketingmaterial)}">
        <t:div rendered="#{researcherIndex.msg ne ''}">
            <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                <font class="mediumfont">#{researcherIndex.msg}</font>
            </div>
        </t:div>

        <table cellpadding="10" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="250" valign="top">
                    <div class="rounded" style="padding: 5px; margin: 5px; background: #e6e6e6;">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Start a New Survey" action="#{researcherSurveyDetail01.beginViewNewSurvey}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create a new survey for bloggers. This step-by-step wizard will guide you through the process.  Your survey can be up and running in a matter of minutes.</font>
                            </td></tr></table>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Panels" action="#{researcherPanels.beginView}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Create and manage standing panels of bloggers for longitudinal studies.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Update My Researcher Profile" action="#{researcherDetails.beginView}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Help us understand your needs so that we can serve you better.</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Billing Info" action="#{researcherBilling.beginView}" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Update your billing information on this screen.</font>
                            </td></tr></table>


                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Researcher Frequently Asked Questions" action="researcherfaq" styleClass="mediumfont" style="color: #596697;"/>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Get your answers here!</font>
                            </td></tr></table>

                            <br/><br/>
                            <table cellpadding="0" cellspacing="0" border="0"><tr><td valign="top"><img src="/images/wireless-green.png" alt="" border="0"/></td><td valign="top"><img src="/images/clear.gif" width="1" height="5"/><br/>
                                <h:commandLink value="Researcher Basic Info" action="#{researcherIndex.beginView}" styleClass="mediumfont" style="color: #596697;"><f:param name="showmarketingmaterial" value="1"/></h:commandLink>
                            </td></tr>
                            <tr><td valign="top"></td><td valign="top">
                                <font class="smallfont">Basic Researcher information, how the system works, etc.</font>
                            </td></tr></table>
                        </div>
                    </div>
                </td>
                <td valign="top">
                    <h:outputText value="Surveys You've Created" styleClass="largefont" style="color: #cccccc;" escape="false"/>
                    <t:div rendered="#{empty researcherSurveyList.surveys}">
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
                            <font class="smallfont">You have not yet created any surveys.<br/><h:commandLink value="Start a New Survey" action="#{researcherSurveyDetail01.beginViewNewSurvey}" styleClass="smallfont" style="color: #596697;"/></font>
                        </div>
                        <br/><br/>
                    </t:div>
                    <t:saveState id="save" value="#{researcherSurveyList}"/>
                    <t:dataTable id="datatable" value="#{researcherSurveyList.surveys}" rows="50" var="survey" rendered="#{!empty researcherSurveyList.surveys}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcol,tcol,tcolnowrap,tcolnowrap">
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Title"/>
                        </f:facet>
                        <h:outputText value="#{survey.title}" escape="false" styleClass="normalfont" style="font-weight:bold;" rendered="#{survey.status eq 1}"/>
                        <h:commandLink action="#{publicSurveyTakeRedirector.beginView}" rendered="#{survey.status ne 1}">
                            <h:outputText value="#{survey.title}" escape="false" styleClass="normalfont" style="font-weight:bold;"/>
                            <f:param name="surveyid" value="#{survey.surveyid}" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Status"/>
                        </f:facet>
                        <h:outputText value="Draft" styleClass="smallfont" rendered="#{survey.status==1}"/>
                        <h:outputText value="Pending, Waiting for Start Date" styleClass="smallfont" rendered="#{survey.status==2}"/>
                        <h:outputText value="Pending, Waiting for Funds" styleClass="smallfont" rendered="#{survey.status==3}"/>
                        <h:outputText value="Live" styleClass="smallfont" rendered="#{survey.status==4}"/>
                        <h:outputText value="Closed" styleClass="smallfont" rendered="#{survey.status==5}"/>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="-" style="color: #ffffff;"/>
                        </f:facet>
                        <h:commandLink action="#{researcherSurveyDetail01.beginView}" rendered="#{survey.status eq 1}">
                            <h:outputText value="Edit" styleClass="smallfont" escape="false" />
                            <f:param name="surveyid" value="#{survey.surveyid}" />
                        </h:commandLink>
                        <h:commandLink action="#{researcherSurveyDetail01.beginView}" rendered="#{survey.status ne 1}">
                            <h:outputText value="Review" styleClass="smallfont" escape="false" />
                            <f:param name="surveyid" value="#{survey.surveyid}" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="-" style="color: #ffffff;"/>
                        </f:facet>
                        <h:commandLink action="#{researcherEmailinvite.beginView}" rendered="#{survey.status eq 4}">
                            <h:outputText value="Invite" styleClass="smallfont" escape="false" />
                            <f:param name="surveyid" value="#{survey.surveyid}" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="-" style="color: #ffffff;"/>
                        </f:facet>
                        <h:commandLink action="#{researcherResults.beginView}" rendered="#{survey.status ne 1}">
                            <h:outputText value="Results" styleClass="smallfont" escape="false" />
                            <f:param name="surveyid" value="#{survey.surveyid}" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="-" style="color: #ffffff;"/>
                        </f:facet>
                        <h:commandLink action="#{researcherIndex.copy}">
                            <h:outputText value=" Copy" styleClass="smallfont" escape="false"/>
                            <f:param name="surveyid" value="#{survey.surveyid}" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="-" style="color: #ffffff;"/>
                        </f:facet>
                        <h:commandLink action="#{researcherSurveyDelete.beginView}" rendered="#{survey.status eq 1}">
                            <h:outputText value=" Delete" styleClass="smallfont" escape="false"/>
                            <f:param name="surveyid" value="#{survey.surveyid}" />
                        </h:commandLink>
                      </h:column>
                    </t:dataTable>
                    <t:dataScroller id="scroll_1" for="datatable" rendered="#{!empty researcherSurveyList.surveys}" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
                </td>

             </tr>
         </table>

    </c:if>

    </h:form>

    </ui:define>


</ui:composition>
</html>