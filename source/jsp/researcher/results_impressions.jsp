<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Survey Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont">#{researcherResultsImpressions.survey.title}</font>
        <br/>
        <h:commandLink value="Results Main" action="#{researcherResults.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Response Report" action="#{researcherResultsAnswers.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Impressions" action="#{researcherResultsImpressions.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Respondents" action="#{researcherResultsRespondents.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
        <h:commandLink value="Financial Status" action="#{researcherResultsFinancial.beginView}" styleClass="subnavfont" style="padding-left: 15px;"/>
    </div>
    <br/><br/>

    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    On this page you see a list of blogs that your survey has been posted to along with the number of times it has been viewed by that blog's readers.  You can visit each blog by clicking on the Specific Page link.
    <br/><br/><br/>
    </font></div></center>

    <br/><br/>

    <t:saveState id="save" value="#{researcherResultsImpressions}"/>

    <t:dataTable id="datatable" value="#{researcherResultsImpressions.researcherResultsImpressionsListitems}" rows="50" var="imp" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Specific Page"/>
        </f:facet>
        <h:outputLink target="referer" value="#{imp.referer}">
            <h:outputText value="#{imp.referertruncated}" styleClass="tinyfont"/>
        </h:outputLink>
        <h:outputText value="None" rendered="#{empty imp.referer}"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Impressions Qualifying"/>
        </f:facet>
        <h:outputText value="#{imp.impressionspaidandtobepaid}" styleClass="smallfont" style="color: #0000ff;"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Quality Rating"/>
        </f:facet>
        <h:outputText value="#{imp.impressionquality}" styleClass="smallfont" style="color: #0000ff;"/>
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
    
</h:form>

</ui:define>


</ui:composition>
</html>