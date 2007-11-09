<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Earnings from Completed Surveys";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/header.jsp" %>



            <% if ("#{!empty bloggerCompletedsurveys.list}){ %>
                        <c:forEach var="completedsurvey" items="<%=((BloggerCompletedsurveys)Pagez.getBeanMgr().get("BloggerCompletedsurveys")).getList()%>">
                            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                                <table cellpadding="2" cellspacing="0" border="0" width="100%">
                                    <tr>
                                        <td valign="top">
                                            <h:outputText value="<%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getResponsedate()%>" styleClass="tinyfont"/><br/>
                                            <h:outputLink value="/survey.jsf?surveyid=<%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getSurveyid()%>" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"><h:outputText><%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getSurveytitle()%></h:outputText></h:outputLink><br/>
                                            <h:outputText value="Est earnings: <%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getAmttotal()%>" styleClass="tinyfont" style="font-weight: bold;"/>
                                            <% if ("<%=((!userSession)Pagez.getBeanMgr().get("!userSession")).getIsfacebookui()%>){ %>
                                                <br/>
                                                <h:outputLink value="/survey.jsf?surveyid=<%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getSurveyid()%>" rendered="#{completedsurvey.response.poststatus eq 0}" styleClass="tinyfont" style="font-weight:bold;"><h:outputText>Needs to be Posted</h:outputText></h:outputLink>
                                                <h:outputText value="Posted At Least Once" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 1}"/>
                                                <h:outputText value="Posted Successfully" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 2}"/>
                                                <h:outputText value="Too Late to Post" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 3}"/>
                                          <% } %>
                                          <br/>
                                          <h:commandLink action="<%=((BloggerImpressions)Pagez.getBeanMgr().get("BloggerImpressions")).getBeginView()%>">
                                                <h:outputText value="<%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getTotalimpressions()%>" styleClass="tinyfont" style="font-weight:bold; text-decoration: none; " escape="false"/>
                                                <f:param name="surveyid" value="<%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getSurveyid()%>" />
                                           </h:commandLink><h:outputText value=" impressions" styleClass="tinyfont" style="font-weight:bold;"/>
                                        </td>
                                        <td valign="top" align="right">
                                            <f:verbatim escape="false"><%=((Completedsurvey)Pagez.getBeanMgr().get("Completedsurvey")).getResponse().getResponsestatushtml()%></f:verbatim>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <br/>
                        </c:forEach>
                        <font class="tinyfont" style="color: #666666;">Survey statuses update nightly. Remember, you must leave the survey on your mini-feed and profile to generate clicks for 5 days in the 10 after you take it to get paid.  Days that qualify are marked green.</font>
                    <% } %>

             <!--
              <t:saveState id="save" value="#{bloggerCompletedsurveys}"/>

              <t:dataTable id="datatable" value="<%=((BloggerCompletedsurveys)Pagez.getBeanMgr().get("BloggerCompletedsurveys")).getList()%>" rendered="#{!empty bloggerCompletedsurveys.list}" rows="50" var="listitem" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcol,tcol,tcol">
                      <h:column rendered="true">
                        <f:facet name="header">
                          <h:outputText value=""/>
                        </f:facet>
                        <div class="rounded" style="background: #e6e6e6; padding: 10px; text-align: center;">
                            <h:outputLink value="/survey.jsf?surveyid=<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurveyid()%>" styleClass="normalfont" style="font-weight: bold; color: #999999;"><h:outputText><%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurveytitle()%></h:outputText></h:outputLink><br/>
                            <f:verbatim escape="false"><center><%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponse().getResponsestatushtml()%></center></f:verbatim>
                        </div>
                      </h:column>


                      <h:column rendered="true">
                        <f:facet name="header">
                          <h:outputText value="Survey Title"/>
                        </f:facet>
                        <h:commandLink action="<%=((PublicSurveyTakeRedirector)Pagez.getBeanMgr().get("PublicSurveyTakeRedirector")).getBeginView()%>">
                            <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurveytitle()%>" escape="false" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"/>
                            <f:param name="surveyid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurveyid()%>" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Date"/>
                        </f:facet>
                        <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponsedate()%>" styleClass="tinyfont"/>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Impressions"/>
                        </f:facet>
                        <center>
                            <h:commandLink action="<%=((BloggerImpressions)Pagez.getBeanMgr().get("BloggerImpressions")).getBeginView()%>">
                                <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getTotalimpressions()%>" styleClass="tinyfont" escape="false"/>
                                <f:param name="surveyid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurveyid()%>" />
                            </h:commandLink>
                        </center>
                      </h:column>
                      <% if ("<%=((!userSession)Pagez.getBeanMgr().get("!userSession")).getIsfacebookui()%>){ %>
                          <h:column>
                            <f:facet name="header">
                                <h:outputText value="Posted?"/>
                            </f:facet>
                            <h:commandLink action="<%=((PublicSurveyTakeRedirector)Pagez.getBeanMgr().get("PublicSurveyTakeRedirector")).getBeginView()%>" rendered="#{listitem.response.poststatus eq 0}">
                                <h:outputText value="Post It!" escape="false" styleClass="tinyfont" style="font-weight:bold;"/>
                                <f:param name="surveyid" value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getSurveyid()%>" />
                            </h:commandLink>
                            <h:outputText value="Posted At Least Once" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{listitem.response.poststatus eq 1}"/>
                            <h:outputText value="Posted" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{listitem.response.poststatus eq 2}"/>
                            <h:outputText value="Too Late to Post" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{listitem.response.poststatus eq 3}"/>
                          </h:column>
                      <% } %>
                      <h:column>
                        <f:facet name="header">
                            <h:outputText value="Paid?"/>
                        </f:facet>
                        <h:outputText value="Paid" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getResponse().getIspaid()%>"/>
                        <h:outputText value="Not Paid Yet" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="<%=((!listitem)Pagez.getBeanMgr().get("!listitem")).getResponse().getIspaid()%>"/>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Estimated Earnings"/>
                        </f:facet>
                        <h:outputText value="<%=((Listitem)Pagez.getBeanMgr().get("Listitem")).getAmttotal()%>" styleClass="tinyfont" style="font-weight: bold;"/>
                      </h:column>
                  </t:dataTable>
                    <t:dataScroller id="scroll_1" for="datatable" rendered="#{!empty bloggerCompletedsurveys.list}" fastStep="10" pageCountVar="pageCount" pageIndexVar="pageIndex" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
        <% if ("<%=((!userSession)Pagez.getBeanMgr().get("!userSession")).getIsfacebookui()%>){ %>
            <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
            <img src="/images/lightbulb_on.png" alt="" align="right"/>
            Your blog posting script: Click on any survey title to access the script that you'll use to post the survey to your blog.  You must do this to make money on your blog traffic impressions.
            <br/><br/>
            Note: Earnings calculations are not final.   Final payment notification and calculation can be found on <h:commandLink  action="<%=((AccountBalance)Pagez.getBeanMgr().get("AccountBalance")).getBeginView()%>" styleClass="smallfont">Your Account Balance</h:commandLink> page.
            </font></div></center>
        <% } %>

<%@ include file="/jsp/templates/footer.jsp" %>

