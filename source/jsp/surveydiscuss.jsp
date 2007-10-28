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
    <ui:define name="title">#{publicSurveyDiscuss.survey.title}<br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="false"/>


<h:form id="survey">
    <!--<t:saveState id="save" value="#{publicSurveyDiscuss}"/>-->

    <font class="smallfont">#{publicSurveyDiscuss.survey.description}</font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=#{publicSurveyDiscuss.survey.surveyid}">Disclosure</a>
    -->

    <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                Discuss this survey here.  Thoughts on the results?  Thoughts on the financial incentive?  Thoughts on the people who are posting it to their blogs?  Thoughts on the questions asked?  Thoughts on anything else related to this survey?
                </font></div></center>
                <c:if test="#{!userSession.isloggedin}">
                    <br/><br/>
                    <font class="mediumfont">You must be logged-in to take part in the discussion.</font>
                </c:if>
            </td>
            <td valign="top" width="150">
                <img src="/images/wireless-green-128.png" width="128" height="128"/>
            </td>
        </tr>
    </table>
    <t:div rendered="#{!empty publicSurveyDiscuss.surveydiscusses}">
        <t:dataTable id="datatablediscuss" value="#{publicSurveyDiscuss.surveydiscusses}" rows="500" var="discuss" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcol,tcol,tcol,tcol">
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <img src="/images/user-48.png" width="48" height="48"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:outputLink value="/profile.jsf?userid=#{discuss.user.userid}">
                <h:outputText value="#{discuss.user.firstname} #{discuss.user.lastname}" styleClass="normalfont" style="font-weight: bold;"/>
            </h:outputLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:outputText value="#{discuss.surveydiscuss.date}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
            <br/>
            <h:outputText value="#{discuss.surveydiscuss.subject}" styleClass="normalfont" style="font-weight: bold;"/>
            <br/>
            <h:outputText value="#{discuss.surveydiscuss.comment}" styleClass="smallfont"/><br/><br/>
          </h:column>
        </t:dataTable>
        <!--
        <t:dataScroller id="scroll_discuss" for="datatablediscuss" fastStep="10" pageCountVar="pageCountDiscuss" pageIndexVar="pageIndexDiscuss" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
    </t:div>
    <br/><br/>
    <c:if test="#{userSession.isloggedin}">
        <font class="mediumfont">Post a Comment!</font>
        <br/><br/>
        <h:messages styleClass="RED"/>
        <h:panelGrid columns="3" cellpadding="3" border="0">

            <h:panelGroup>
                <h:outputText value="Subject:" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{publicSurveyDiscuss.discussSubject}" id="discussSubject"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="discussSubject" styleClass="RED"></h:message>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Comment:" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputTextarea value="#{publicSurveyDiscuss.discussComment}" id="discussComment" cols="65" rows="6"></h:inputTextarea>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="discussComment" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
            </h:panelGroup>
            <h:panelGroup>
                <h:commandButton action="#{publicSurveyDiscuss.newComment}" value="Post Comment" styleClass="formsubmitbutton"></h:commandButton>
            </h:panelGroup>
            <h:panelGroup>
            </h:panelGroup>

        </h:panelGrid>
    </c:if>





</h:form>

</ui:define>


</ui:composition>
</html>


