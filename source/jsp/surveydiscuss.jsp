<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getTitle()%>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="smallfont"><%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getDescription()%></font><br/><br/><br/>

    <h:messages styleClass="RED"/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>
    <!--
    <a href="/survey.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>">Questions</a> |
    <a href="/surveypostit.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>">Post It</a> |
    <a href="/surveyresults.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>">Results</a> |
    <a href="/surveywhotookit.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>">Who Took It?</a> |
    <a href="/surveydiscuss.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>">Discuss</a> |
    <a href="/surveyrequirements.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>">Requirements</a> |
    <a href="/surveydisclosure.jsf?surveyid=<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurvey().getSurveyid()%>">Disclosure</a>
    -->

    <h:graphicImage url="/images/clear.gif" width="700" height="1" styleClass="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                Discuss this survey here.  Thoughts on the results?  Thoughts on the financial incentive?  Thoughts on the people who are posting it to their blogs?  Thoughts on the questions asked?  Thoughts on anything else related to this survey?
                </font></div></center>
                <% if ("<%=((!userSession)Pagez.getBeanMgr().get("!userSession")).getIsloggedin()%>){ %>
                    <br/><br/>
                    <font class="mediumfont">You must be logged-in to take part in the discussion.</font>
                <% } %>
            </td>
            <td valign="top" width="150">
                <img src="/images/wireless-green-128.png" width="128" height="128"/>
            </td>
        </tr>
    </table>
    <t:div rendered="#{!empty publicSurveyDiscuss.surveydiscusses}">
        <t:dataTable id="datatablediscuss" value="<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getSurveydiscusses()%>" rows="500" var="discuss" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcol,tcol,tcol,tcol">
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
            <h:outputLink value="/profile.jsf?userid=<%=((Discuss)Pagez.getBeanMgr().get("Discuss")).getUser().getUserid()%>">
                <h:outputText value="<%=((Discuss)Pagez.getBeanMgr().get("Discuss")).getUser().getFirstname()%> <%=((Discuss)Pagez.getBeanMgr().get("Discuss")).getUser().getLastname()%>" styleClass="normalfont" style="font-weight: bold;"/>
            </h:outputLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:outputText value="<%=((Discuss)Pagez.getBeanMgr().get("Discuss")).getSurveydiscuss().getDate()%>" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
            <br/>
            <h:outputText value="<%=((Discuss)Pagez.getBeanMgr().get("Discuss")).getSurveydiscuss().getSubject()%>" styleClass="normalfont" style="font-weight: bold;"/>
            <br/>
            <h:outputText value="<%=((Discuss)Pagez.getBeanMgr().get("Discuss")).getSurveydiscuss().getComment()%>" styleClass="smallfont"/><br/><br/>
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
    <% if ("<%=((UserSession)Pagez.getBeanMgr().get("UserSession")).getIsloggedin()%>){ %>
        <font class="mediumfont">Post a Comment!</font>
        <br/><br/>
        <h:messages styleClass="RED"/>
        <table cellpadding="0" cellspacing="0" border="0">

            <td valign="top">
                <h:outputText value="Subject:" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getDiscussSubject()%>" id="discussSubject"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="discussSubject" styleClass="RED"></h:message>
            </td>


            <td valign="top">
                <h:outputText value="Comment:" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputTextarea value="<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getDiscussComment()%>" id="discussComment" cols="65" rows="6"></h:inputTextarea>
            </td>
            <td valign="top">
                <h:message for="discussComment" styleClass="RED"></h:message>
            </td>

            <td valign="top">
            </td>
            <td valign="top">
                <h:commandButton action="<%=((PublicSurveyDiscuss)Pagez.getBeanMgr().get("PublicSurveyDiscuss")).getNewComment()%>" value="Post Comment" styleClass="formsubmitbutton"></h:commandButton>
            </td>
            <td valign="top">
            </td>

        </table>
    <% } %>







<%@ include file="/jsp/templates/footer.jsp" %>


