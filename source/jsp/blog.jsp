<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "dNeero Social Surveys Blog";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>


     <div style="width: 99%; text-align: right;">
        <a href="/rss.xml"><img src="/images/feed-icon-16x16.png" alt="RSS Feed" width="16" height="16" border="0"/> <font class="smallfont">RSS Feed</font></a>
     </div>
     <br/>

     <!--
     <c:forEach var="blogpost" items="<%=((PublicBlog)Pagez.getBeanMgr().get("PublicBlog")).getBlogposts()%>">
        <a href="blogpost.jsf?blogpostid=<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getBlogpostid()%>"><font class="mediumfont" style="color: #0bae17; font-weight: bold;"><%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getTitle()%></font></a>
        <br/>
        <h:outputText styleClass="smallfont" value="<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getBody()%>" escape="false"></h:outputText>
        <br/>
        <font class="tinyfont" style="color: #999999;">Posted by: <%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getAuthor()%> at <h:outputText value=" <%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getDate()%>" styleClass="tinyfont" style="color: #cccccc;"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText></font>
        <br/><br/>
     </c:forEach>

     <br/><br/>
     -->



     <t:dataTable id="datatable1" value="<%=((PublicBlog)Pagez.getBeanMgr().get("PublicBlog")).getBlogposts()%>" rows="100" var="blogpost" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
      <h:column>
        <f:facet name="header">
          <h:outputText value=""/>
        </f:facet>
        <a href="/blogpost.jsf?blogpostid=<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getBlogpostid()%>"><font class="mediumfont" style="color: #0bae17; font-weight: bold;"><%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getTitle()%></font></a>
        <br/>
        <h:outputText styleClass="smallfont" value="<%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getBody()%>" escape="false"></h:outputText>
        <br/>
        <font class="tinyfont" style="color: #999999;">Posted by: <%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getAuthor()%> at <h:outputText value=" <%=((Blogpost)Pagez.getBeanMgr().get("Blogpost")).getDate()%>" styleClass="tinyfont" style="color: #cccccc;"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText></font>
        <br/><br/>
      </h:column>
     </t:dataTable>
    <t:dataScroller id="scroll_1" for="datatable1" fastStep="100" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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




<%@ include file="/jsp/templates/footer.jsp" %>