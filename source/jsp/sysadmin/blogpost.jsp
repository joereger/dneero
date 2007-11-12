<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Blog Posting";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



        <table cellpadding="0" cellspacing="0" border="0">


            <td valign="top">
                <h:outputText value="Date" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <t:inputDate value="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getDate()%>" type="both" popupCalendar="true" id="date" required="true"></t:inputDate>
            </td>
            <td valign="top">
                <h:message for="date" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Author" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getAuthor()%>" size="45" id="author" required="true"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="author" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Title" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getTitle()%>" size="75" id="title" required="true"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="title" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Body" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputTextarea value="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getBody()%>" id="body" required="true" cols="75" rows="10"></h:inputTextarea>
            </td>
            <td valign="top">
                <h:message for="body" styleClass="RED"></h:message>
            </td>


            <td valign="top">
                <h:outputText value="Categories" styleClass="formfieldnamefont"></h:outputText>
                <br/>
                <h:outputText value="(comma-separated)" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getCategories()%>" size="75" maxlength="250" id="categories" required="false"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="categories" styleClass="RED"></h:message>
            </td>


            <td valign="top">
            </td>
            <td valign="top">
                <h:commandButton action="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getDelete()%>" value="Delete" styleClass="formsubmitbutton" rendered="#{sysadminBlogpost.blogpostid gt 0}"></h:commandButton>
                <h:commandButton action="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getSave()%>" value="Save!" styleClass="formsubmitbutton"></h:commandButton>
            </td>
            <td valign="top">
            </td>

        </table>



    </h:form>

    <br/><br/>

    <h:form>

        <t:saveState id="save" value="#{sysadminBlogpost}"/>

        <t:dataTable id="datatable" value="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getBlogposts()%>" rows="50" var="blogpost" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Date"/>
            </f:facet>
            <h:outputText value="<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getDate()%>" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Title"/>
            </f:facet>
            <h:outputText value="<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getTitle()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Author"/>
            </f:facet>
            <h:outputText value="<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getAuthor()%>" styleClass="smallfont"/>
          </h:column>


          <h:column>
            <f:facet name="header">
              <h:outputText value="-" style="color: #ffffff;"/>
            </f:facet>
            <h:commandLink action="<%=((SysadminBlogpost)Pagez.getBeanMgr().get("SysadminBlogpost")).getBeginView()%>">
                <h:outputText value="Edit/Delete" escape="false" />
                <f:param name="blogpostid" value="<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getBlogpostid()%>" />
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


