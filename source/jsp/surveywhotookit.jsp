<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getTitle()%>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="smallfont"><%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getDescription()%></font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getSurveyid()%>">Disclosure</a>
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
            <t:dataTable id="datatablerespondents" value="<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getRespondents()%>" rows="500" var="respondent" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Date"/>
                </f:facet>
                <h:outputText value="<%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getResponse().getResponsedate()%>" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Person"/>
                </f:facet>
                <h:outputLink value="/profile.jsf?userid=<%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getUser().getUserid()%>">
                    <h:outputText value="<%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getUser().getFirstname()%> <%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getUser().getLastname()%>" styleClass="normalfont" style="font-weight: bold;"/>
                </h:outputLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Answers"/>
                </f:facet>
                <h:outputLink value="/survey.jsf?u=<%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getUser().getUserid()%>&amp;s=<%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getResponse().getSurveyid()%>&amp;p=0&amp;r=<%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getResponse().getResponseid()%>">
                    <h:outputText value="Answers" styleClass="tinyfont" style="font-weight: bold;"/>
                </h:outputLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value=" "/>
                </f:facet>
                <h:outputText value="Earnings to Charity" styleClass="tinyfont" style="font-weight: bold;" rendered="<%=((Respondent)Pagez.getBeanMgr().get("Respondent")).getResponse().getIsforcharity()%>"/>
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
            <t:dataTable id="datatable" value="<%=((PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getImpressions()%>" rows="100" var="imp" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Web Address"/>
                </f:facet>
                <h:outputLink target="referer" value="<%=((Imp)Pagez.getBeanMgr().get("Imp")).getReferer()%>">
                    <h:outputText value="See It!" styleClass="smallfont" style="font-weight: bold;"/>
                </h:outputLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Impressions"/>
                </f:facet>
                <h:outputText value="<%=((Imp)Pagez.getBeanMgr().get("Imp")).getImpressionstotal()%>" styleClass="smallfont" style="font-weight: bold;"/>
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







<%@ include file="/jsp/templates/footer.jsp" %>


