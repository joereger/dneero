<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Users";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>



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
                    <h:inputText value="<%=((SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList")).getSearchuserid()%>" id="searchuserrid" size="5"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList")).getSearchfirstname()%>" id="searchfirstname" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList")).getSearchlastname()%>" id="searchlastname" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:inputText value="<%=((SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList")).getSearchemail()%>" id="searchemail" size="15"></h:inputText>
                </td>
                <td valign="top">
                    <h:selectBooleanCheckbox value="<%=((SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList")).getSearchfacebookers()%>" id="searchfacebookers"></h:selectBooleanCheckbox>
                </td>
                <td valign="top">
                    <h:commandButton action="<%=((SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList")).getSearch()%>"  value="Search" styleClass="formsubmitbutton"></h:commandButton>
                </td>
            </tr>
        </table>

        <br/>

        <!--<t:saveState id="save" value="#{sysadminUserList}"/>-->

        <t:dataTable id="datatable" value="<%=((SysadminUserList)Pagez.getBeanMgr().get("SysadminUserList")).getUsers()%>" rows="100" var="user" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Userid"/>
            </f:facet>
            <a href='/sysadmin/userdetail.jsf?userid=<%=((User)Pagez.getBeanMgr().get("User")).getUserid()%>'><%=((User)Pagez.getBeanMgr().get("User")).getUserid()%></a>
            <!--
            <h:commandLink action="<%=((SysadminUserDetail)Pagez.getBeanMgr().get("SysadminUserDetail")).getBeginView()%>">
                <h:outputText value="<%=((User)Pagez.getBeanMgr().get("User")).getUserid()%>" styleClass="smallfont" style="color: #0000ff;"/>
                <f:param name="userid" value="<%=((User)Pagez.getBeanMgr().get("User")).getUserid()%>" />
            </h:commandLink>
            -->
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Email"/>
            </f:facet>
            <a href='/sysadmin/userdetail.jsf?userid=<%=((User)Pagez.getBeanMgr().get("User")).getUserid()%>'><%=((User)Pagez.getBeanMgr().get("User")).getEmail()%></a>
            <!--
            <h:commandLink action="<%=((SysadminUserDetail)Pagez.getBeanMgr().get("SysadminUserDetail")).getBeginView()%>">
                <h:outputText value="<%=((User)Pagez.getBeanMgr().get("User")).getEmail()%>" styleClass="mediumfont" style="color: #0000ff;"/>
                <f:param name="userid" value="<%=((User)Pagez.getBeanMgr().get("User")).getUserid()%>" />
            </h:commandLink>
            -->
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="First Name"/>
            </f:facet>
            <h:outputText value="<%=((User)Pagez.getBeanMgr().get("User")).getFirstname()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Last Name"/>
            </f:facet>
            <h:outputText value="<%=((User)Pagez.getBeanMgr().get("User")).getLastname()%>" styleClass="smallfont"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Signup Date"/>
            </f:facet>
            <h:outputText value="<%=((User)Pagez.getBeanMgr().get("User")).getCreatedate()%>" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
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



