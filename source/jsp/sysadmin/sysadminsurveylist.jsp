<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Surveys";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <t:dataTable id="datatable" value="<%=((SysadminSurveyList)Pagez.getBeanMgr().get("SysadminSurveyList")).getSurveys()%>" rows="100" var="survey" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcol,tcol,tcol,tcol">
      <h:column>
        <f:facet name="header">
          <h:outputText value="Surveyid"/>
        </f:facet>
        <h:outputText value="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getSurveyid()%>"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Title"/>
        </f:facet>
        <a href='/sysadmin/sysadminsurveydetail.jsf?surveyid=<%=((Survey)Pagez.getBeanMgr().get("Survey")).getSurveyid()%>'><%=((Survey)Pagez.getBeanMgr().get("Survey")).getTitle()%></a>
        <!--
        <h:commandLink action="<%=((SysadminSurveyDetail)Pagez.getBeanMgr().get("SysadminSurveyDetail")).getBeginView()%>">
            <h:outputText value="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getTitle()%>" styleClass="mediumfont" style="color: #0000ff;"/>
            <f:param name="surveyid" value="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getSurveyid()%>" />
        </h:commandLink>
        -->
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Displays"/>
        </f:facet>
        <h:outputText value="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getPublicsurveydisplays()%>"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Spotlight?"/>
        </f:facet>
        <h:outputText value="Yes" styleClass="smallfont" rendered="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getIsspotlight()%>"/>
        <h:outputText value="No" styleClass="smallfont" rendered="<%=((!survey)Pagez.getBeanMgr().get("!survey")).getIsspotlight()%>"/>
      </h:column>
      <h:column>
        <f:facet name="header">
          <h:outputText value="Status"/>
        </f:facet>
        <h:outputText value="Draft" styleClass="smallfont" rendered="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getStatus==1()%>"/>
        <h:outputText value="Pending, Waiting for Start Date" styleClass="smallfont" rendered="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getStatus==2()%>"/>
        <h:outputText value="Pending, Waiting for Funds" styleClass="smallfont" rendered="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getStatus==3()%>"/>
        <h:outputText value="Open" styleClass="smallfont" rendered="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getStatus==4()%>"/>
        <h:outputText value="Closed" styleClass="smallfont" rendered="<%=((Survey)Pagez.getBeanMgr().get("Survey")).getStatus==5()%>"/>
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



<%@ include file="/jsp/templates/footer.jsp" %>



