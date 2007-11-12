<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Panels";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



    <t:div rendered="#{researcherPanels.msg ne '' and researcherPanels.msg ne null}">
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        <%=((ResearcherPanels)Pagez.getBeanMgr().get("ResearcherPanels")).getMsg()%>
        <br/><br/></font></div></center>
        <br/><br/>
    </t:div>

    <h:form id="panels">

        <t:saveState id="save" value="#{researcherPanels}"/>
        
        <br/><br/>

        <t:dataTable id="datatable" value="<%=((ResearcherPanels)Pagez.getBeanMgr().get("ResearcherPanels")).getListitems()%>" var="listitem" rendered="#{!empty researcherPanels.listitems}" rows="10" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Panel Name"/>
            </f:facet>
            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getPanel().getName()%>" styleClass="normalfont" style="font-weight: bold;"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Create Date"/>
            </f:facet>
            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getPanel().getCreatedate()%>" styleClass="smallfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Members"/>
            </f:facet>
            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getNumberofmembers()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((ResearcherPanelsListBloggers)Pagez.getBeanMgr().get("ResearcherPanelsListBloggers")).getBeginView()%>">
                <h:outputText value="View Members" styleClass="smallfont" escape="false" />
                <f:param name="panelid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getPanel().getPanelid()%>" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((ResearcherPanelsEdit)Pagez.getBeanMgr().get("ResearcherPanelsEdit")).getBeginView()%>">
                <h:outputText value="Edit" styleClass="smallfont" escape="false" />
                <f:param name="panelid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getPanel().getPanelid()%>" />
            </h:commandLink>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((ResearcherPanels)Pagez.getBeanMgr().get("ResearcherPanels")).getDeletePanel()%>">
                <h:outputText value="Delete" styleClass="smallfont" escape="false" />
                <f:param name="panelid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getPanel().getPanelid()%>" />
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



        <br/><br/>
        <h:inputText value="<%=((ResearcherPanels)Pagez.getBeanMgr().get("ResearcherPanels")).getNewpanelname()%>" id="newpanelname"></h:inputText>
        <h:commandButton action="<%=((ResearcherPanels)Pagez.getBeanMgr().get("ResearcherPanels")).getCreateNewPanel()%>" value="Create a New Panel" styleClass="formsubmitbutton"></h:commandButton>






<%@ include file="/jsp/templates/footer.jsp" %>

