<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.htmluibeans.BloggerIndex" %>
<%@ page import="com.dneero.htmluibeans.PublicIndex" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
PublicIndex publicIndex = (PublicIndex)Pagez.getBeanMgr().get("PublicIndex");
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <table cellpadding="0" cellspacing="0" border="0" width="750">
       <tr>
           <td align="center">
               <a href="/jsp/blogger/index.jsp?showmarketingmaterial=1"><img src="/images/homepage-v2-blogger.png" width="376" height="355" border="0" alt=""/></a>
           </td>
           <td align="center">
                <a href="/jsp/researcher/index.jsp?showmarketingmaterial=1"><img src="/images/homepage-v2-researcher.png" width="375" height="355" border="0" alt=""/></a>
           </td>
       </tr>

       <%if (publicIndex.getSpotlightsurveys()!=null && publicIndex.getSpotlightsurveys().get(0)!=null && publicIndex.getSpotlightsurveys().get(1)!=null && publicIndex.getSpotlightsurveys().get(2)!=null && publicIndex.getSpotlightsurveys().get(3)!=null){%>
       <tr>
           <td colspan="2" valign="top" align="center">
                <br/><br/>
                <table cellpadding="0" cellspacing="0" border="0" width="95%">
                    <tr>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=publicIndex.getSpotlightsurveys().get(0).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(0).getTitle()%></a></font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=publicIndex.getSpotlightsurveys().get(1).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(1).getTitle()%></a></font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=publicIndex.getSpotlightsurveys().get(2).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(2).getTitle()%></a></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsf?surveyid=<%=publicIndex.getSpotlightsurveys().get(3).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(3).getTitle()%></a></font><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=publicIndex.getSpotlightsurveyenhancers().get(0).getDescriptiontruncated()%>...</font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=publicIndex.getSpotlightsurveyenhancers().get(1).getDescriptiontruncated()%>...</font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=publicIndex.getSpotlightsurveyenhancers().get(2).getDescriptiontruncated()%>...</font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="tinyfont"><%=publicIndex.getSpotlightsurveyenhancers().get(3).getDescriptiontruncated()%>...</font><br/>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=publicIndex.getSpotlightsurveyenhancers().get(0).getMaxearning()%></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=publicIndex.getSpotlightsurveyenhancers().get(1).getMaxearning()%></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=publicIndex.getSpotlightsurveyenhancers().get(2).getMaxearning()%></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="mediumfont" style="color: #cccccc;">earn up to:</font><br/>
                            <font class="largefont" style="color: #cccccc;"><%=publicIndex.getSpotlightsurveyenhancers().get(3).getMaxearning()%></font>
                        </td>
                    </tr>
                </table>
                <br/>
           </td>
       </tr>
       <%}%>

       <tr>
           <td colspan="2" valign="top" align="right">
                <br/>
                <a href="/jsp/publicsurveylist.jsp"><font class="mediumfont" style="color: #0BAE17; font-weight: bold; text-decoration: none;">See All Surveys</font></a>
           </td>
       </tr>



    </table>



<%@ include file="/jsp/templates/footer.jsp" %>