<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

    <font class="mediumfont" style="color: #0bae17;">#{publicBlogPost.blogpost.title}</font>
    <br/>
    <h:outputText styleClass="smallfont" value="#{publicBlogPost.blogpost.body}" escape="false"></h:outputText>
    <br/>
    <font class="tinyfont" style="color: #cccccc;">Posted by: #{publicBlogPost.blogpost.author} at <h:outputText value=" #{publicBlogPost.blogpost.date}" styleClass="tinyfont" style="color: #cccccc;"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText></font>
    <br/><br/>


    <t:dataTable sortable="false" id="datatable" value="#{publicBlogPost.blogpost.blogpostcomments}" rows="1000" var="comment" rendered="#{!empty publicBlogPost.blogpost.blogpostcomments}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcol,tcol,tcol">
      <t:column>
        <f:facet name="header">
          <h:outputText value=""/>
        </f:facet>
        <h:outputText value="#{comment.date}" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
      </t:column>
      <t:column>
        <f:facet name="header">
            <h:outputText value=""/>
        </f:facet>
        <font class="smallfont">Comment by: </font>
        <h:outputLink value="#{comment.url}">
            <h:outputText value="#{comment.name}" styleClass="smallfont" style="color: #0000ff;"/>
        </h:outputLink>
        <br/>
        <font class="tinyfont">#{comment.comment}</font>
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
    <h:panelGrid columns="3" cellpadding="3" border="0">

            <h:panelGroup>
                <h:outputText value="Name" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{publicBlogPost.name}" id="name" required="false" maxlength="255"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="name" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="URL" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputText value="#{publicBlogPost.url}" id="url" required="false" maxlength="255"></h:inputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="url" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Comment" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <h:inputTextarea value="#{publicBlogPost.comment}" id="comment" cols="45" rows="5" required="false">
                </h:inputTextarea>
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="comment" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
                <h:outputText value="Prove You're a Human" styleClass="formfieldnamefont"></h:outputText>
            </h:panelGroup>
            <h:panelGroup>
                <div style="border: 1px solid #ccc; padding: 3px;">
                <h:inputText value="#{publicBlogPost.j_captcha_response}" id="j_captcha_response" required="false"/>
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
            </h:panelGroup>
            <h:panelGroup>
                <h:message for="j_captcha_response" styleClass="RED"></h:message>
            </h:panelGroup>

            <h:panelGroup>
            </h:panelGroup>
            <h:panelGroup>
                <h:commandButton action="#{publicBlogPost.postComment}" value="Post Comment" styleClass="formsubmitbutton"></h:commandButton>
            </h:panelGroup>
            <h:panelGroup>
            </h:panelGroup>



     </h:panelGrid>



<%@ include file="/jsp/templates/footer.jsp" %>