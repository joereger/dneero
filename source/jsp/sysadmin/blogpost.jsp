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
    <ui:define name="title">dNeero.com Blog Posting<br/><br/></ui:define>
    <ui:param name="navtab" value="sysadmin"/>
    <ui:define name="body">
    <d:authorization acl="systemadmin" redirectonfail="true"/>

    <h:form>
        <t:saveState id="save" value="#{sysadminBlogpost}"/>

        <h:panelGrid columns="3" cellpadding="3" border="0">


            <h:panelGroup>
                <h:outputText value="Date" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <t:inputDate value="#{sysadminBlogpost.date}" type="both" popupCalendar="true" id="date" required="true"></t:inputDate>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="date" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Author" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{sysadminBlogpost.author}" size="45" id="author" required="true"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="author" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Title" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{sysadminBlogpost.title}" size="75" id="title" required="true"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="title" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Body" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputTextarea value="#{sysadminBlogpost.body}" id="body" required="true" cols="75" rows="10"></h:inputTextarea>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="body" styleClass="RED"></h:message>
            </h:panelGroup>


            <h:panelGroup>
                <h:outputText value="Categories" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:outputText value="(comma-separated)" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{sysadminBlogpost.categories}" size="75" maxlength="250" id="categories" required="false"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="categories" styleClass="RED"></h:message>
            </h:panelGroup>


            <h:panelGroup>
            </h:panelGroup>
            <h:panelGroup>
                <h:commandButton action="#{sysadminBlogpost.delete}" value="Delete" styleClass="formsubmitbutton" rendered="#{sysadminBlogpost.blogpostid gt 0}"></h:commandButton>
                <h:commandButton action="#{sysadminBlogpost.save}" value="Save!" styleClass="formsubmitbutton"></h:commandButton>
            </h:panelGroup>
            <h:panelGroup>
            </h:panelGroup>

        </h:panelGrid>



    </h:form>

    <br/><br/>

    <h:form>

        <t:saveState id="save" value="#{sysadminBlogpost}"/>

        <t:dataTable id="datatable" value="#{sysadminBlogpost.blogposts}" rows="50" var="blogpost" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Date"/>
            </f:facet>
            <h:outputText value="#{blogpost.date}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Title"/>
            </f:facet>
            <h:outputText value="#{blogpost.title}" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Author"/>
            </f:facet>
            <h:outputText value="#{blogpost.author}" styleClass="smallfont"/>
          </h:column>


          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="#{sysadminBlogpost.beginView}">
                <h:outputText value="Edit/Delete" escape="false" />
                <f:param name="blogpostid" value="#{blogpost.blogpostid}" />
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




    </h:form>



</ui:define>


</ui:composition>
</html>



