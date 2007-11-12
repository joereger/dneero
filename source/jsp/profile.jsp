<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "XXX's Profile";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>


    <t:div rendered="#{publicProfile.msg ne '' and publicProfile.msg ne null}">
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getMsg()%>
        </font></div></center>
        <br/><br/>
    </t:div>

    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
    <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" width="50%">
                <img src="/images/user.png" alt="" border="0" width="128" height="128"/>
            </td>
            <td valign="top">
                <div class="rounded" style="background: #ffffff; text-align: left; padding: 15px;">
                    <table cellpadding="10" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td valign="top" width="50%">
                                <h:outputText value="Social Influence Rating (TM)" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top" width="50%">
                                <h:outputText value="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getBlogger().getSocialinfluencerating()%>" styleClass="smallfont"></h:outputText>
                                <br/>
                                <h:outputText value="(Top <%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getSocialinfluenceratingpercentile()%>%)" styleClass="smallfont"></h:outputText>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" width="50%">
                                <h:outputText value="Amt Earned for Charity" styleClass="formfieldnamefont"></h:outputText>
                            </td>
                            <td valign="top" width="50%">
                                <h:outputText value="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getCharityamtdonatedForscreen()%>" styleClass="smallfont"></h:outputText>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
          </tr>
       </table>
       </div>


       <br/><br/>
       <font class="mediumfont" style="color: #cccccc;">Surveys Taken</font>
       <br/>

        <t:saveState id="save" value="#{publicProfile}"/>
        <t:dataScroller for="datatable1" maxPages="5"/>
        <t:dataTable id="datatable1" value="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getListitems()%>" rows="10" var="listitem" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
          <h:column>
            <f:facet name="header">
              <h:outputText value="Date"/>
            </f:facet>
              <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponse().getResponsedate()%>" styleClass="tinyfont"><f:convertDateTime type="both" dateStyle="short" timeStyle="medium"/></h:outputText>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Survey Title"/>
            </f:facet>
            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurvey().getTitle()%>" styleClass="normalfont"/>
          </h:column>
          <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <h:outputLink value="/survey.jsf?u=<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getUser().getUserid()%>&amp;s=<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurvey().getSurveyid()%>&amp;p=0&amp;r=<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponse().getResponseid()%>">
                    <h:outputText value="Answers" styleClass="smallfont"/>
                </h:outputLink>
              </h:column>
              <h:column>
                <f:facet name="header">
                  <h:outputText value="-" style="color: #ffffff;"/>
                </f:facet>
                <h:commandLink action="<%=((PublicProfileImpressions)Pagez.getBeanMgr().get("PublicProfileImpressions")).getBeginView()%>">
                    <h:outputText value="Impressions" escape="false" styleClass="smallfont"/>
                    <f:param name="responseid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponse().getResponseid()%>" />
                </h:commandLink>
              </h:column>
         </t:dataTable>
        <t:dataScroller id="scroll_1" for="datatable1" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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



        <% if ("#{userSession.isloggedin and (userSession.user.researcherid gt 0)}){ %>
            <br/><br/>
            <font class="mediumfont" style="color: #cccccc;">Panel Membership</font>
            <br/>
            <t:saveState id="save" value="#{publicProfile}"/>
            <t:dataScroller for="datatable3" maxPages="5"/>
            <t:dataTable id="datatable3" value="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getPanels()%>" rows="10" var="panel" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcolnowrap,tcolnowrap">
              <h:column>
                <f:facet name="header">
                  <h:outputText value="Panel Name"/>
                </f:facet>
                <h:outputText value="<%=((Panel)Pagez.getBeanMgr().get("Panel")).getName()%>" styleClass="normalfont"/>
              </h:column>
            </t:dataTable>
            <t:dataScroller id="scroll_3" for="datatable3" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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

            <h:selectOneMenu value="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getPanelid()%>" id="panelid" required="true" rendered="#{!empty publicProfile.panelids}">
               <f:selectItems value="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getPanelids()%>"/>
            </h:selectOneMenu>
            <h:commandButton action="<%=((PublicProfile)Pagez.getBeanMgr().get("PublicProfile")).getAddToPanel()%>" value="Add Blogger Panel" styleClass="formsubmitbutton"></h:commandButton>
        <% } %>

    


<%@ include file="/jsp/templates/footer.jsp" %>