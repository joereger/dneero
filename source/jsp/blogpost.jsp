<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>

    <font class="mediumfont" style="color: #0bae17;"><%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getBlogpost().getTitle()%></font>
    <br/>
    <h:outputText styleClass="smallfont" value="<%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getBlogpost().getBody()%>" escape="false"></h:outputText>
    <br/>
    <font class="tinyfont" style="color: #cccccc;">Posted by: <%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getBlogpost().getAuthor()%> at <h:outputText value=" <%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getBlogpost().getDate()%>" styleClass="tinyfont" style="color: #cccccc;"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText></font>
    <br/><br/>


    <t:dataTable sortable="false" id="datatable" value="<%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getBlogpost().getBlogpostcomments()%>" rows="1000" var="comment" rendered="#{!empty publicBlogPost.blogpost.blogpostcomments}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcol,tcol,tcol">
      <t:column>
        <f:facet name="header">
          <h:outputText value=""/>
        </f:facet>
        <h:outputText value="<%=((Comment)Pagez.getBeanMgr().get("Comment")).getDate()%>" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
      </t:column>
      <t:column>
        <f:facet name="header">
            <h:outputText value=""/>
        </f:facet>
        <font class="smallfont">Comment by: </font>
        <h:outputLink value="<%=((Comment)Pagez.getBeanMgr().get("Comment")).getUrl()%>">
            <h:outputText value="<%=((Comment)Pagez.getBeanMgr().get("Comment")).getName()%>" styleClass="smallfont" style="color: #0000ff;"/>
        </h:outputLink>
        <br/>
        <font class="tinyfont"><%=((Comment)Pagez.getBeanMgr().get("Comment")).getComment()%></font>
        <br/><br/>
      </t:column>
    </t:dataTable>
    <!--
    <t:dataScroller id="scroll_1" for="datatable" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;" rendered="#{!empty publicCharity.topdonatingUsers}">
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
    <br/><br/>
    <font class="formfieldnamefont">Post a comment:</font>
    <br/>
    <table cellpadding="0" cellspacing="0" border="0">

            <td valign="top">
                <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getName()%>" id="name" required="false" maxlength="255"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="name" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="URL" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputText value="<%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getUrl()%>" id="url" required="false" maxlength="255"></h:inputText>
            </td>
            <td valign="top">
                <h:message for="url" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Comment" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <h:inputTextarea value="<%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getComment()%>" id="comment" cols="45" rows="5" required="false">
                </h:inputTextarea>
            </td>
            <td valign="top">
                <h:message for="comment" styleClass="RED"></h:message>
            </td>

            <td valign="top">
                <h:outputText value="Prove You're a Human" styleClass="formfieldnamefont"></h:outputText>
            </td>
            <td valign="top">
                <div style="border: 1px solid #ccc; padding: 3px;">
                <h:inputText value="<%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getJ_captcha_response()%>" id="j_captcha_response" required="false"/>
                <br/>
                <font class="tinyfont">(type the squiggly letters that appear below)</font>
                <br/>
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td><img src="/images/clear.gif" alt="" width="1" height="100"></img></td>
                        <td style="background: url(/images/loading-captcha.gif);">
                            <img src="/images/clear.gif" alt="" width="200" height="1"></img><br/>
                            <h:graphicImage url="/jcaptcha"></h:graphicImage>
                        </td>
                    </tr>
                </table>
                </div>
            </td>
            <td valign="top">
                <h:message for="j_captcha_response" styleClass="RED"></h:message>
            </td>

            <td valign="top">
            </td>
            <td valign="top">
                <h:commandButton action="<%=((PublicBlogPost)Pagez.getBeanMgr().get("PublicBlogPost")).getPostComment()%>" value="Post Comment" styleClass="formsubmitbutton"></h:commandButton>
            </td>
            <td valign="top">
            </td>



     </table>



<%@ include file="/jsp/templates/footer.jsp" %>