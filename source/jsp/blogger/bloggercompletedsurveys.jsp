<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Earnings from Completed Surveys";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/header.jsp" %>



            <c:if test="#{!empty bloggerCompletedsurveys.list}">
                        <c:forEach var="completedsurvey" items="${bloggerCompletedsurveys.list}">
                            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                                <table cellpadding="2" cellspacing="0" border="0" width="100%">
                                    <tr>
                                        <td valign="top">
                                            <h:outputText value="#{completedsurvey.responsedate}" styleClass="tinyfont"/><br/>
                                            <h:outputLink value="/survey.jsf?surveyid=#{completedsurvey.surveyid}" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"><h:outputText>#{completedsurvey.surveytitle}</h:outputText></h:outputLink><br/>
                                            <h:outputText value="Est earnings: #{completedsurvey.amttotal}" styleClass="tinyfont" style="font-weight: bold;"/>
                                            <c:if test="${!userSession.isfacebookui}">
                                                <br/>
                                                <h:outputLink value="/survey.jsf?surveyid=#{completedsurvey.surveyid}" rendered="#{completedsurvey.response.poststatus eq 0}" styleClass="tinyfont" style="font-weight:bold;"><h:outputText>Needs to be Posted</h:outputText></h:outputLink>
                                                <h:outputText value="Posted At Least Once" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 1}"/>
                                                <h:outputText value="Posted Successfully" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 2}"/>
                                                <h:outputText value="Too Late to Post" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{completedsurvey.response.poststatus eq 3}"/>
                                          </c:if>
                                          <br/>
                                          <h:commandLink action="#{bloggerImpressions.beginView}">
                                                <h:outputText value="#{completedsurvey.totalimpressions}" styleClass="tinyfont" style="font-weight:bold; text-decoration: none; " escape="false"/>
                                                <f:param name="surveyid" value="#{completedsurvey.surveyid}" />
                                           </h:commandLink><h:outputText value=" impressions" styleClass="tinyfont" style="font-weight:bold;"/>
                                        </td>
                                        <td valign="top" align="right">
                                            <f:verbatim escape="false">#{completedsurvey.response.responsestatushtml}</f:verbatim>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <br/>
                        </c:forEach>
                        <font class="tinyfont" style="color: #666666;">Survey statuses update nightly. Remember, you must leave the survey on your mini-feed and profile to generate clicks for 5 days in the 10 after you take it to get paid.  Days that qualify are marked green.</font>
                    </c:if>

             <!--
              <t:saveState id="save" value="#{bloggerCompletedsurveys}"/>

              <t:dataTable id="datatable" value="#{bloggerCompletedsurveys.list}" rendered="#{!empty bloggerCompletedsurveys.list}" rows="50" var="listitem" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcolnowrap,tcol,tcol,tcol,tcol">
                      <h:column rendered="true">
                        <f:facet name="header">
                          <h:outputText value=""/>
                        </f:facet>
                        <div class="rounded" style="background: #e6e6e6; padding: 10px; text-align: center;">
                            <h:outputLink value="/survey.jsf?surveyid=#{listitem.surveyid}" styleClass="normalfont" style="font-weight: bold; color: #999999;"><h:outputText>#{listitem.surveytitle}</h:outputText></h:outputLink><br/>
                            <f:verbatim escape="false"><center>#{listitem.response.responsestatushtml}</center></f:verbatim>
                        </div>
                      </h:column>


                      <h:column rendered="true">
                        <f:facet name="header">
                          <h:outputText value="Survey Title"/>
                        </f:facet>
                        <h:commandLink action="#{publicSurveyTakeRedirector.beginView}">
                            <h:outputText value="#{listitem.surveytitle}" escape="false" styleClass="normalfont" style="font-weight: bold; color: #0000ff;"/>
                            <f:param name="surveyid" value="#{listitem.surveyid}" />
                        </h:commandLink>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Date"/>
                        </f:facet>
                        <h:outputText value="#{listitem.responsedate}" styleClass="tinyfont"/>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Impressions"/>
                        </f:facet>
                        <center>
                            <h:commandLink action="#{bloggerImpressions.beginView}">
                                <h:outputText value="#{listitem.totalimpressions}" styleClass="tinyfont" escape="false"/>
                                <f:param name="surveyid" value="#{listitem.surveyid}" />
                            </h:commandLink>
                        </center>
                      </h:column>
                      <c:if test="${!userSession.isfacebookui}">
                          <h:column>
                            <f:facet name="header">
                                <h:outputText value="Posted?"/>
                            </f:facet>
                            <h:commandLink action="#{publicSurveyTakeRedirector.beginView}" rendered="#{listitem.response.poststatus eq 0}">
                                <h:outputText value="Post It!" escape="false" styleClass="tinyfont" style="font-weight:bold;"/>
                                <f:param name="surveyid" value="#{listitem.surveyid}" />
                            </h:commandLink>
                            <h:outputText value="Posted At Least Once" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{listitem.response.poststatus eq 1}"/>
                            <h:outputText value="Posted" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{listitem.response.poststatus eq 2}"/>
                            <h:outputText value="Too Late to Post" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{listitem.response.poststatus eq 3}"/>
                          </h:column>
                      </c:if>
                      <h:column>
                        <f:facet name="header">
                            <h:outputText value="Paid?"/>
                        </f:facet>
                        <h:outputText value="Paid" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{listitem.response.ispaid}"/>
                        <h:outputText value="Not Paid Yet" escape="false" styleClass="tinyfont" style="font-weight:bold;" rendered="#{!listitem.response.ispaid}"/>
                      </h:column>
                      <h:column>
                        <f:facet name="header">
                          <h:outputText value="Estimated Earnings"/>
                        </f:facet>
                        <h:outputText value="#{listitem.amttotal}" styleClass="tinyfont" style="font-weight: bold;"/>
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
        <c:if test="${!userSession.isfacebookui}">
            <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
            <img src="/images/lightbulb_on.png" alt="" align="right"/>
            Your blog posting script: Click on any survey title to access the script that you'll use to post the survey to your blog.  You must do this to make money on your blog traffic impressions.
            <br/><br/>
            Note: Earnings calculations are not final.   Final payment notification and calculation can be found on <h:commandLink  action="#{accountBalance.beginView}" styleClass="smallfont">Your Account Balance</h:commandLink> page.
            </font></div></center>
        </c:if>

<%@ include file="/jsp/templates/footer.jsp" %>

