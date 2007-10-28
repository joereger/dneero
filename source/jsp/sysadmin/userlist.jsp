<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:d="http://dneero.com/taglib"

      >



<ui:composition template="/template/template-facelets.xhtml">
    <ui:define name="title">Users<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
    <d:authorization acl="systemadmin" redirectonfail="true"/>

    <h:form>

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="tinyfont">Userid</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">First Name</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Last Name</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Email</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Facebook?</font>
                </td>
                <td valign="top">

                </td>
            </tr>
            <tr>
                <td valign="top">
                    <h:inputText value="#{sysadminUserList.searchuserid}" id="searchuserrid" size="5"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="#{sysadminUserList.searchfirstname}" id="searchfirstname" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="#{sysadminUserList.searchlastname}" id="searchlastname" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="#{sysadminUserList.searchemail}" id="searchemail" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="#{sysadminUserList.searchfacebookers}" id="searchfacebookers"></h:selectBooleanCheckbox>
                </td>
                <td valign="top">
                    <h:commandButton action="#{sysadminUserList.search}"  value="Search" styleClass="formsubmitbutton"></h:commandButton>
                </td>
            </tr>
        </table>

        <br/>

        <!--<t:saveState id="save" value="#{sysadminUserList}"/>-->

        <t:dataTable id="datatable" value="#{sysadminUserList.users}" rows="100" var="user" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Userid"/>
            </f:facet>
            <a href='/sysadmin/userdetail.jsf?userid=#{user.userid}'>#{user.userid}</a>
            <!--
            <h:commandLink action="#{sysadminUserDetail.beginView}">
                <h:outputText value="#{user.userid}" styleClass="smallfont" style="color: #0000ff;"/>
                <f:param name="userid" value="#{user.userid}" />
            </h:commandLink>
            -->
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Email"/>
            </f:facet>
            <a href='/sysadmin/userdetail.jsf?userid=#{user.userid}'>#{user.email}</a>
            <!--
            <h:commandLink action="#{sysadminUserDetail.beginView}">
                <h:outputText value="#{user.email}" styleClass="mediumfont" style="color: #0000ff;"/>
                <f:param name="userid" value="#{user.userid}" />
            </h:commandLink>
            -->
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="First Name"/>
            </f:facet>
            <h:outputText value="#{user.firstname}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Last Name"/>
            </f:facet>
            <h:outputText value="#{user.lastname}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Signup Date"/>
            </f:facet>
            <h:outputText value="#{user.createdate}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
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
    </h:form>



</ui:define>


</ui:composition>
</html>


