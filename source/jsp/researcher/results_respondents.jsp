<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResultsRespondents.getSurvey().getTitle()%></font>
        <br/>
        <a href="results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Response Report</font></a>
        <a href="results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>

    <t:saveState id="save" value="#{researcherResultsRespondents}"/>

    <t:dataTable id="datatable" value="<%=((ResearcherResultsRespondents)Pagez.getBeanMgr().get("ResearcherResultsRespondents")).getList()%>" rows="50" var="listitem" rendered="#{!empty researcherResultsRespondents.list}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Date"/>
            </f:facet>
            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponsedate()%>" styleClass="smallfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Blogger Name"/>
            </f:facet>
            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getFirstname()%> <%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getLastname()%>" escape="false" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getBeginView()%>">
                <h:outputText value="Blogger's Profile" escape="false" styleClass="smallfont"/>
                <f:param name="bloggerid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getBloggerid()%>" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getBeginView()%>">
                <h:outputText value="Blogger's Answers" escape="false" styleClass="smallfont"/>
                <f:param name="bloggerid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getBloggerid()%>" />
                <f:param name="surveyid" value="<%=((ResearcherResultsRespondents)Pagez.getBeanMgr().get("ResearcherResultsRespondents")).getSurvey().getSurveyid()%>" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((PublicProfileImpressions)Pagez.getBeanMgr().get("PublicProfileImpressions")).getBeginView()%>">
                <h:outputText value="Blogger's Impressions" escape="false" styleClass="smallfont"/>
                <f:param name="responseid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponseid()%>" />
            </h:commandLink>
          </h:column>

        </t:dataTable>
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


    


<%@ include file="/jsp/templates/footer.jsp" %>