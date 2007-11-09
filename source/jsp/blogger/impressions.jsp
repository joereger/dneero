<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<%=((BloggerImpressions)Pagez.getBeanMgr().get("BloggerImpressions")).getSurveytitle()%>";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/header.jsp" %>


    
    
    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
    <img src="/images/lightbulb_on.png" alt="" align="right"/>
    This page lists blog displays/impressions of a survey.  Note that in the calculation of earnings some impressions may not be included because the researcher can set a maximum number of paid blog displays per blog.
    <br/><br/><br/>
    </font></div></center>

        <h:form>
              <t:saveState id="save" value="#{bloggerImpressions}"/>

              <t:dataTable id="datatable" value="<%=((BloggerImpressions)Pagez.getBeanMgr().get("BloggerImpressions")).getList()%>" rows="25" var="listitem" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Url"/>
                </f:facet>
                <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getReferer()%>" styleClass="smallfont"/>
              </h:column>
              <h:column rendered="true">
                <f:facet name="header">
                  <h:outputText value="Impressions Qualifying for Payment"/>
                </f:facet>
                <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getImpressionspaidandtobepaid()%>" escape="false" styleClass="smallfont" style="color: #0000ff;"/>
              </h:column>
              <h:column rendered="true">
                <f:facet name="header">
                  <h:outputText value="Impressions Total"/>
                </f:facet>
                <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getImpressionstotal()%>" escape="false" styleClass="smallfont" style="color: #0000ff;"/>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Quality Rating"/>
                </f:facet>
                <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getQuality()%>"/>
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

