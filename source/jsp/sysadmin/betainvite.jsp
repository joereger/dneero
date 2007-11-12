<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Beta Invites";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>




        <table cellpadding="0" cellspacing="0" border="0">


            <td valign="top">
                <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((SysadminBetainvite)Pagez.getBeanMgr().get("SysadminBetainvite")).getName()%>" id="name" required="true"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="name" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Email" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((SysadminBetainvite)Pagez.getBeanMgr().get("SysadminBetainvite")).getEmail()%>" id="email" required="true"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="email" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Password" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((SysadminBetainvite)Pagez.getBeanMgr().get("SysadminBetainvite")).getPassword()%>" id="password" required="true"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="password" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Message" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputTextarea value="<%=((SysadminBetainvite)Pagez.getBeanMgr().get("SysadminBetainvite")).getMessage()%>" id="message" required="true" cols="75" rows="10">
                    <f:validateLength minimum="3" maximum="50000"></f:validateLength>
                </h:inputTextarea>
            </td>
            <td valign="top">
                <h:message for="message" styleClass="RED"></h:message>
            </td>




            <td valign="top">
            </td>
            <td valign="top">
                <h:commandButton action="<%=((SysadminBetainvite)Pagez.getBeanMgr().get("SysadminBetainvite")).getNewInvite()%>" value="Invite this Person" styleClass="formsubmitbutton"></h:commandButton>
            </td>
            <td valign="top">
            </td>

        </table>



    </h:form>

    <br/><br/>

    <h:form>

        <t:saveState id="save" value="#{sysadminBetainvite}"/>

        <t:dataTable id="datatable" value="<%=((SysadminBetainvite)Pagez.getBeanMgr().get("SysadminBetainvite")).getBetainvites()%>" rows="50" var="betainvite" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap, tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Name"/>
            </f:facet>
            <h:outputText value="<%=((Betainvite)Pagez.getBeanMgr().get("Betainvite")).getName()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Email"/>
            </f:facet>
            <h:outputText value="<%=((Betainvite)Pagez.getBeanMgr().get("Betainvite")).getEmail()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Pass"/>
            </f:facet>
            <h:outputText value="<%=((Betainvite)Pagez.getBeanMgr().get("Betainvite")).getPassword()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Has Logged In?"/>
            </f:facet>
            <h:outputText value="Yes" styleClass="smallfont" rendered="<%=((Betainvite)Pagez.getBeanMgr().get("Betainvite")).getHasloggedin()%>"/>
            <h:outputText value="No" styleClass="smallfont" rendered="<%=((!betainvite)Pagez.getBeanMgr().get("!betainvite")).getHasloggedin()%>"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Times Logged In"/>
            </f:facet>
            <h:outputText value="<%=((Betainvite)Pagez.getBeanMgr().get("Betainvite")).getNumberoftimesloggedin()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Last Login"/>
            </f:facet>
            <h:outputText value="<%=((Betainvite)Pagez.getBeanMgr().get("Betainvite")).getDatelastloggedin()%>" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
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


