<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherIndex" %>
<%@ page import="com.dneero.htmluibeans.BloggerIndex" %>
<%@ page import="com.dneero.htmluibeans.PublicIndex" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicIndex publicIndex = (PublicIndex)Pagez.getBeanMgr().get("PublicIndex");
%>
<%
    if (request.getParameter("accesscode")!=null && !request.getParameter("accesscode").equals("")) {
        Pagez.getUserSession().setAccesscode(request.getParameter("accesscode"));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("enteraccesscode")) {
        try {
            publicIndex.enterAccessCode();
            Pagez.getUserSession().setMessage("Sorry, no surveys were found for that Access Code.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



    <table cellpadding="0" cellspacing="0" border="0" width="750">
       <tr>
           <td align="center">
               <a href="/blogger/index.jsp?showmarketingmaterial=1"><img src="/images/homepage-v2-blogger.png" width="376" height="355" border="0" alt=""/></a>
           </td>
           <td align="center">
                <a href="/researcher/index.jsp?showmarketingmaterial=1"><img src="/images/homepage-v2-researcher.png" width="375" height="355" border="0" alt=""/></a>
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
                            <font class="mediumfont"><a href="/survey.jsp?surveyid=<%=publicIndex.getSpotlightsurveys().get(0).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(0).getTitle()%></a></font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsp?surveyid=<%=publicIndex.getSpotlightsurveys().get(1).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(1).getTitle()%></a></font><br/>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsp?surveyid=<%=publicIndex.getSpotlightsurveys().get(2).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(2).getTitle()%></a></font>
                        </td>
                        <td valign="top" width="25%" style="padding: 0px 8px 0px 8px;">
                            <font class="smallfont" style="color: #666666; font-weight: bold;">Survey Opportunity:</font><br/>
                            <font class="mediumfont"><a href="/survey.jsp?surveyid=<%=publicIndex.getSpotlightsurveys().get(3).getSurveyid()%>" style="color: #0BAE17; font-weight: bold;"><%=publicIndex.getSpotlightsurveys().get(3).getTitle()%></a></font><br/>
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
           <td valign="middle" align="center">
               <a href="/publicsurveylist.jsp"><font class="mediumfont" style="font-weight: bold;">See All Surveys</font></a>
           </td>
           <td valign="top" align="right">
                <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                    <form action="/index.jsp" method="post">
                        <input type="hidden" name="action" value="enteraccesscode">
                        <font class="normalfont"><b>Got an Access Code?</b></font>
                        <%=Textbox.getHtml("accesscode", Pagez.getUserSession().getAccesscode(), 255, 10, "", "")%>
                        <input type="submit" class="formsubmitbutton" value="Go">
                    </form>
                </div>
           </td>
       </tr>





    </table>



<%@ include file="/template/footer.jsp" %>