<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/user.png\" align=\"right\" alt=\"\" border=\"0\"/><%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getUser().getFirstname()%> <%=((PublicProfileAnswers)Pagez.getBeanMgr().get("PublicProfileAnswers")).getUser().getLastname()%>'s Impressions<br/><br clear=\"all\"/>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



    <font class="mediumfont" style="color: #cccccc;"><%=((PublicProfileImpressions)Pagez.getBeanMgr().get("PublicProfileImpressions")).getSurvey().getTitle()%></font>
    <br/><br/>

    <t:saveState id="save" value="#{publicProfileImpressions}"/>

    <t:dataTable id="datatable" value="<%=((PublicProfileImpressions)Pagez.getBeanMgr().get("PublicProfileImpressions")).getList()%>" rows="50" var="imp" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">

      <h:column>
        <f:facet name="header">
          <h:outputText value="Specific Page"/>
        </f:facet>
        <h:outputLink target="referer" value="<%=((Imp)Pagez.getBeanMgr().get("Imp")).getReferer()%>">
            <h:outputText value="<%=((Imp)Pagez.getBeanMgr().get("Imp")).getReferertruncated()%>" styleClass="normalfont"/>
        </h:outputLink>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Impressions"/>
        </f:facet>
        <h:outputText value="<%=((Imp)Pagez.getBeanMgr().get("Imp")).getImpressionspaidandtobepaid()%>"></h:outputText>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Quality Rating"/>
        </f:facet>
        <h:outputText value="<%=((Imp)Pagez.getBeanMgr().get("Imp")).getImpressionquality()%>"></h:outputText>
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