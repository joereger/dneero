<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Old Surveys";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    <h:outputText value="There are currently no surveys listed." styleClass="mediumfont" rendered="#{empty publicOldSurveyList.surveys}"/>

    <t:dataTable sortable="true" id="datatable" value="#{publicOldSurveyList.surveys}" rows="15" var="srvy" rendered="#{!empty publicOldSurveyList.surveys}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <t:column>
        <f:facet name="header">
            <h:outputText value="Survey Name"/>
        </f:facet>
        <h:outputLink value="/surveyresults.jsf?surveyid=#{srvy.surveyid}">
            <h:outputText value="#{srvy.title}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"/>
        </h:outputLink>
        <br/>
        <h:outputText value="#{srvy.description}" escape="false" styleClass="tinyfont"/>
      </t:column>

      <t:column>
        <f:facet name="header">
          <h:outputText value="Respondents Earned Up To"/>
        </f:facet>
        <h:outputText value="#{srvy.maxearning}" styleClass="smallfont" style="font-weight:normal;"/>
      </t:column>
      <t:column>
        <f:facet name="header">
          <h:outputText value="Number of Respondents"/>
        </f:facet>
        <h:outputText value="#{srvy.numberofrespondents}" styleClass="smallfont" style="font-weight:normal;"/>
      </t:column>
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



