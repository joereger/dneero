<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <table cellpadding="0" cellspacing="0" border="0" width="750">
       <tr>
           <td align="center">
               <h:commandLink action="<%=((BloggerIndex)Pagez.getBeanMgr().get("BloggerIndex")).getBeginView()%>">
                    <f:param name="showmarketingmaterial" value="1"/>
                    <img src="/images/homepage-v2-blogger.png" width="376" height="355" border="0" alt=""/>
               </h:commandLink>
           </td>
           <td align="center">
               <h:commandLink action="<%=((ResearcherIndex)Pagez.getBeanMgr().get("ResearcherIndex")).getBeginView()%>">
                    <f:param name="showmarketingmaterial" value="1"/>
                    <img src="/images/homepage-v2-researcher.png" width="375" height="355" border="0"  alt=""/>
               </h:commandLink>
           </td>
       </tr>

       <tr>
           <td colspan="2" valign="top" align="center">
                <br/><br/>
                <table cellpadding="0" cellspacing="0" border="0" width="95%">
                    <tr>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['0']().getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['0']().getTitle()%></a></font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['1']().getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['1']().getTitle()%></a></font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['2']().getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['2']().getTitle()%></a></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['3']().getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveys['3']().getTitle()%></a></font><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['0']().getDescriptiontruncated()%>...</font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['1']().getDescriptiontruncated()%>...</font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['2']().getDescriptiontruncated()%>...</font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['3']().getDescriptiontruncated()%>...</font><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['0']().getMaxearning()%></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['1']().getMaxearning()%></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['2']().getMaxearning()%></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=((PublicIndex)Pagez.getBeanMgr().get("PublicIndex")).getSpotlightsurveyenhancers['3']().getMaxearning()%></font>
                        </td>
                    </tr>
                </table>
                <br/>
           </td>
       </tr>

       <tr>
           <td colspan="2" valign="top" align="right">
                <br/>
                <h:outputLink value="/publicsurveylist.jsf">
                    <h:outputText value="See All Survey Opportunities" styleClass="mediumfont" style="color: #0BAE17; font-weight: bold; text-decoration: none;"/>
                </h:outputLink>
           </td>
       </tr>

       <!--
       <tr>
           <td colspan="2" valign="top" align="center">
                <h:commandLink action="<%=((PublicSurveyList)Pagez.getBeanMgr().get("PublicSurveyList")).getBeginView()%>">
                    <img src="/images/survey-hp-wide.png" width="750" height="114" border="0"  alt=""/>
               </h:commandLink>
           </td>
       </tr>
       -->
      <!--
       <tr>
           <td colspan="2" valign="top">

                <% if ("#{systemProps.isbeta ne '1'}){ %>
                    <div class="rounded" style="padding: 0px; margin: 10px; background: #33FF00;">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                           <tr>
                               <td valign="top" width="33%">
                                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                                    <font class="largefont">$<h:outputText value="<%=((SystemStats)Pagez.getBeanMgr().get("SystemStats")).getDollarsavailabletobloggers()%>" styleClass="largefont"><f:converter converterId="DisplayAsMoneyConverter"/></h:outputText></font>
                                    <br/>
                                    <font class="mediumfont">waiting to be earned by bloggers!</font>
                                </div>
                               </td>
                               <td valign="top" width="33%">
                                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                                    <font class="largefont"><%=((SystemStats)Pagez.getBeanMgr().get("SystemStats")).getTotalbloggers()%></font>
                                    <br/>
                                    <font class="mediumfont">bloggers registered and seeking surveys</font>
                                </div>
                               </td>
                               <td valign="top" width="33%">
                                <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
                                    <font class="largefont"><%=((SystemStats)Pagez.getBeanMgr().get("SystemStats")).getTotalimpressions()%></font>
                                    <br/>
                                    <font class="mediumfont">survey results displayed in blogs</font>
                                </div>
                               </td>
                           </tr>
                        </table>
                    </div>
                <% } %>
           </td>
       </tr>
       -->

    </table>



<%@ include file="/jsp/templates/footer.jsp" %>