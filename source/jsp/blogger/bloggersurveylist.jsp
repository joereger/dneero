<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Opportunities";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <h:outputText value="There are currently no surveys that fit your profile.  Please check back soon as we're always adding new surveys!" styleClass="mediumfont" rendered="#{empty bloggerSurveyList.surveys}"/>

    <t:dataTable id="datatable" value="<%=((BloggerSurveyList)Pagez.getBeanMgr().get("BloggerSurveyList")).getSurveys()%>" rows="25" var="srvy" rendered="#{!empty bloggerSurveyList.surveys}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Title"/>
        </f:facet>
        <h:commandLink action="<%=((PublicSurveyTakeRedirector)Pagez.getBeanMgr().get("PublicSurveyTakeRedirector")).getBeginView()%>">
            <h:outputText value="<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getTitle()%>" escape="false" styleClass="normalfont" style="color: #0000ff;"/>
            <f:param name="surveyid" value="<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getSurveyid()%>" />
        </h:commandLink>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Questions"/>
        </f:facet>
        <h:outputText value="<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getNumberofquestions()%>" styleClass="smallfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Timing"/>
        </f:facet>
        <h:outputText value="<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getDaysuntilend()%>" styleClass="smallfont"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Earn Up To"/>
        </f:facet>
        <h:outputText value="<%=((Srvy)Pagez.getBeanMgr().get("Srvy")).getMaxearning()%>" styleClass="smallfont"/>
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



