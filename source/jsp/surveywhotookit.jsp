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
    <ui:define name="title">#{publicSurveyWhotookit.survey.title}<br/></ui:define>
    <ui:param name="navtab" value="home"/>
    <ui:define name="body">
    <d:authorization acl="public" redirectonfail="false"/>


<h:form id="survey">
    <!--<t:saveState id="save" value="#{publicSurveyWhotookit}"/>-->

    <font class="smallfont">#{publicSurveyWhotookit.survey.description}</font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=#{publicSurveyWhotookit.survey.surveyid}">Disclosure</a>
    -->

    <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="150">
                <img src="/images/users-128.png" width="128" height="128"/>
            </td>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                People who took the survey and where it's been posted.
                </font></div></center>
            </td>
        </tr>
    </table>
    <br/>
    <t:panelTabbedPane id="whotookitpanel" bgcolor="#ffffff">
        <t:panelTab id="whotookitpanel_a" label="Who Took It?">
            <h:graphicImage url="/images/clear.gif" width="550" height="1"/><br/>
            <t:dataTable id="datatablerespondents" value="#{publicSurveyWhotookit.respondents}" rows="500" var="respondent" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Date"/>
                </f:facet>
                <h:outputText value="#{respondent.response.responsedate}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Person"/>
                </f:facet>
                <h:outputLink value="/profile.jsf?userid=#{respondent.user.userid}">
                    <h:outputText value="#{respondent.user.firstname} #{respondent.user.lastname}" styleClass="normalfont" style="font-weight: bold;"/>
                </h:outputLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Answers"/>
                </f:facet>
                <h:outputLink value="/survey.jsf?u=#{respondent.user.userid}&amp;s=#{respondent.response.surveyid}&amp;p=0&amp;r=#{respondent.response.responseid}">
                    <h:outputText value="Answers" styleClass="tinyfont" style="font-weight: bold;"/>
                </h:outputLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value=" "/>
                </f:facet>
                <h:outputText value="Earnings to Charity" styleClass="tinyfont" style="font-weight: bold;" rendered="#{respondent.response.isforcharity}"/>
              </h:column>
            </t:dataTable>
            <!--
            <t:dataScroller id="scroll_1" for="datatablerespondents" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
        </t:panelTab>
        <t:panelTab id="whotookitpanel_b" label="Where It's Been Posted">
            <h:graphicImage url="/images/clear.gif" width="550" height="1"/><br/>
            <t:dataTable id="datatable" value="#{publicSurveyWhotookit.impressions}" rows="100" var="imp" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Web Address"/>
                </f:facet>
                <h:outputLink target="referer" value="#{imp.referer}">
                    <h:outputText value="See It!" styleClass="smallfont" style="font-weight: bold;"/>
                </h:outputLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Impressions"/>
                </f:facet>
                <h:outputText value="#{imp.impressionstotal}" styleClass="smallfont" style="font-weight: bold;"/>
              </h:column>
            </t:dataTable>
            <!--
            <t:dataScroller id="scroll_1" for="datatable" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
        </t:panelTab>
    </t:panelTabbedPane>





</h:form>

</ui:define>


</ui:composition>
</html>


