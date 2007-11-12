<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/charity-128.png\" alt=\"\" border=\"0\" width=\"128\" height=\"128\" align=\"right\"/>Make (Real) Change<br/><font class=\"mediumfont\">Make great content and great contributions -- at the same time!</font>";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%@ include file="/jsp/templates/header.jsp" %>

            <br/><br/><br/><br/>
            <table cellpadding="10" cellspacing="3" border="0">
                <tr>                                                                    
                    <td valign="top"  width="50%">
                        <font class="mediumfont" style="color: #999999">We'll Give Your Earnings to Charity</font>
                        <br/>
                        dNeero will, if you so choose, donate your earnings to charity.  Each time you take a survey you'll be able to choose whether to donate and which charity to donate to.  We currently have a number of charitable options and will look to add more in the future.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Results</font>
                        <br/>
                        Every quarter we'll write checks to the charities that you've chosen for the amounts that you've deferred.  We'll list your donations on this page.  When somebody clicks to dNeero from your blog we'll tell them that you donated your earnings to charity.
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Bloggers: Avoid Losing Any Credibility... and Support a Great Cause!</font>
                        <br/>
                        One of the big concerns from bloggers is that the money in this model presents possible bias, and that your readers will not want to feel 'monetized'. Thanks to open and sharing people like <a href="http://www.ck-blog.com/cks_blog/2007/07/new-models-new-.html">CK</a> we decided to make donating to charity a way to shed this concern -- now you can give any of the earnings to a good cause, avoid bias... and increase the features within your posts!
                        <br/><br/><br/>
                        <font class="mediumfont" style="color: #999999">Researchers and Marketers: Do Some Good</font>
                        <br/>
                        When you create a survey you can mark it as Charity Only which means that only bloggers who are willing to have their earnings donated to charity will be able to take it.
                    </td>
                    <td valign="top">
                        <center>
                        <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                            <font class="mediumfont" style="color: #999999">Top Donators</font>
                            <br/>
                            <h:outputText value="Nobody's donated... yet." styleClass="smallfont" rendered="#{empty publicCharity.topdonatingUsers}"/>
                            <t:dataTable sortable="false" id="datatable" value="<%=((PublicCharity)Pagez.getBeanMgr().get("PublicCharity")).getTopdonatingUsers()%>" rows="10" var="donator" rendered="#{!empty publicCharity.topdonatingUsers}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcol,tcol,tcol,tcol">
                              <t:column>
                                <f:facet name="header">
                                    <h:outputText value=""/>
                                </f:facet>
                                <h:outputLink value="/profile.jsf?userid=<%=((Donator)Pagez.getBeanMgr().get("Donator")).getUser().getUserid()%>">
                                    <h:outputText value="<%=((Donator)Pagez.getBeanMgr().get("Donator")).getUser().getFirstname()%> <%=((Donator)Pagez.getBeanMgr().get("Donator")).getUser().getLastname()%>" styleClass="smallfont" style="color: #0000ff;"/>
                                </h:outputLink>
                              </t:column>
                              <t:column>
                                <f:facet name="header">
                                  <h:outputText value=""/>
                                </f:facet>
                                <h:outputText value="<%=((Donator)Pagez.getBeanMgr().get("Donator")).getAmtforscreen()%>"  styleClass="smallfont"/>
                              </t:column>
                            </t:dataTable>
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
                            <br/>
                            <font class="tinyfont" style="color: #999999">(Updated Nightly)</font>
                        </div>
                        <br/><br/>
                        <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
                            <font class="mediumfont" style="color: #999999">Available Charities</font>
                            <br/>These are the charities that are currently available for you to choose from:
                            <br/><a href="http://www.habitat.org/" target="charity">Habitat for Humanity</a>
                            <br/><a href="http://www.wish.org/" target="charity">Make-A-Wish Foundation</a>
                            <br/><a href="http://www.cancer.org/" target="charity">American Cancer Society</a>
                            <br/><a href="http://www.petsmartcharities.org/" target="charity">PetSmart Charities</a>
                            <br/><a href="http://en.wikipedia.org/wiki/Wikimedia_Foundation" target="charity">Wikimedia Foundation</a>
                            <br/><a href="http://www.conservationfund.org/" target="charity">The Conservation Fund</a>
                        </div>
                        </center>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="mediumfont" style="color: #999999">Most Recent Donations</font>
                        <br/>
                        <h:outputText value="There haven't yet been any charitable donations... yet.  We're just getting started.  Go take some surveys and donate!" styleClass="mediumfont" rendered="#{empty publicCharity.publicCharityListItemsMostRecent}"/>
                        <t:dataTable sortable="false" id="datatable2" value="<%=((PublicCharity)Pagez.getBeanMgr().get("PublicCharity")).getPublicCharityListItemsMostRecent()%>" rows="50" var="charityitem" rendered="#{!empty publicCharity.publicCharityListItemsMostRecent}" styleClass="dataTable" headerClass="theader" footerClass="theader" rowClasses="trow1,trow2" columnClasses="tcolnowrap,tcol,tcol,tcol">
                          <t:column>
                            <f:facet name="header">
                                <h:outputText value="Donator"/>
                            </f:facet>
                            <h:outputLink value="/profile.jsf?userid=<%=((Charityitem)Pagez.getBeanMgr().get("Charityitem")).getUser().getUserid()%>">
                                <h:outputText value="<%=((Charityitem)Pagez.getBeanMgr().get("Charityitem")).getUser().getFirstname()%> <%=((Charityitem)Pagez.getBeanMgr().get("Charityitem")).getUser().getLastname()%>" styleClass="smallfont" style="color: #0000ff;"/>
                            </h:outputLink>
                          </t:column>
                          <t:column>
                            <f:facet name="header">
                              <h:outputText value="What was donated."/>
                            </f:facet>
                            <h:outputText value="<%=((Charityitem)Pagez.getBeanMgr().get("Charityitem")).getCharitydonation().getDescription()%>"  styleClass="smallfont"/>
                          </t:column>
                          <t:column>
                            <f:facet name="header">
                              <h:outputText value="Amount of Donation"/>
                            </f:facet>
                            <h:outputText value="<%=((Charityitem)Pagez.getBeanMgr().get("Charityitem")).getAmtForScreen()%>"  styleClass="smallfont"/>
                          </t:column>
                          <t:column>
                            <f:facet name="header">
                              <h:outputText value="Donated To"/>
                            </f:facet>
                            <h:outputText value="<%=((Charityitem)Pagez.getBeanMgr().get("Charityitem")).getCharitydonation().getCharityname()%>"  styleClass="smallfont"/>
                          </t:column>
                        </t:dataTable>
                        <t:dataScroller id="scroll_2" for="datatable2" fastStep="10" pageCountVar="pageCount2" pageIndexVar="pageIndex2" styleClass="scroller" paginator="true" paginatorMaxPages="9" paginatorTableClass="paginator" paginatorActiveColumnStyle="font-weight:bold;">
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
                    </td>
                </tr>

            </table>







<%@ include file="/jsp/templates/footer.jsp" %>
