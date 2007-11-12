<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Bloggers in Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



        <t:dataTable id="datatable" value="<%=((ResearcherPanelsListBloggers)Pagez.getBeanMgr().get("ResearcherPanelsListBloggers")).getListitems()%>" rows="50" var="listitem" rendered="#{!empty researcherPanelsListBloggers.listitems}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Blogger Name"/>
            </f:facet>
            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getUser().getLastname()%>, <%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getUser().getFirstname()%>" styleClass="normalfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getBeginView()%>">
                <h:outputText value="Blogger's Profile" escape="false" styleClass="smallfont"/>
                <f:param name="bloggerid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getBlogger().getBloggerid()%>" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((ResearcherPanelsListBloggers)Pagez.getBeanMgr().get("ResearcherPanelsListBloggers")).getRemoveFromPanel()%>">
                <h:outputText value="Remove From Panel" escape="false" styleClass="smallfont"/>
                <f:param name="panelmembershipid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getPanelmembership().getPanelmembershipid()%>" />
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


