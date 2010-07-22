<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyDisclosure" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%
PublicSurveyDisclosure publicSurveyDisclosure = (PublicSurveyDisclosure)Pagez.getBeanMgr().get("PublicSurveyDisclosure");
%>
<%
//If we don't have a surveyid, shouldn't be on this page
if (publicSurveyDisclosure.getSurvey()==null || publicSurveyDisclosure.getSurvey().getTitle()==null || publicSurveyDisclosure.getSurvey().getSurveyid()<=0){
    Pagez.sendRedirect("/publicsurveylist.jsp");
    return;
}
//If the survey is draft or waiting
if (publicSurveyDisclosure.getSurvey().getStatus()<Survey.STATUS_OPEN){
    Pagez.sendRedirect("/surveynotopen.jsp");
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


    <% Survey surveyInTabs = publicSurveyDisclosure.getSurvey();%>
    <%@ include file="/template/political/jspoverride/surveytabs.jsp" %>


    <a href="/survey.jsp?surveyid=<%=publicSurveyDisclosure.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveyDisclosure.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>



    <% if (!publicSurveyDisclosure.getSurvey().getIsfree()){ %>
        <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
        <table width="100%" cellpadding="5">
            <tr>
                <td valign="top" width="450">
                    <font class="mediumfont" style="color: #666666;">Sponsored <%=Pagez._Survey()%> Disclosure</font>
                    <br/><br/>
                    <font class="normalfont">
                    This is a sponsored <%=Pagez._survey()%>.  The sponsor did not and does not encourage the blogger/social networker to post any particular opinion or answers.  The sponsor encourages the blogger/social networker to simply tell the truth.
                    <br/><br/>
                    The sponsor pays the blogger/social networker for their time to respond to the <%=Pagez._survey()%> and for the number of times that the <%=Pagez._survey()%> is displayed on his or her blog or social network profile.
                    <br/><br/>
                    A link to this disclosure statement is required (and is automatically posted) with each <%=Pagez._survey()%> to protect the integrity of the blogosphere and social networks.
                    </font>
                </td>
                <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                    <td valign="top" align="left">
                        <div class="rounded" style="background: #e6e6e6;">
                            <div class="rounded" style="background: #ffffff; text-align: center;">
                                <center><img src="/images/exclamation-128.png" width="128" height="128"/></center>
                                <br/>
                                <% if (publicSurveyDisclosure.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                    <div class="rounded" style="background: #cccccc; text-align: center;">
                                        <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                        <br/>
                                        <font class="mediumfont">This <%=Pagez._survey()%> is closed.</font>
                                    </div>
                                    <br/>
                                <% } %>
                                <% if (publicSurveyDisclosure.getSurvey().getIscharityonly()){ %>
                                    <br/><br/>
                                    <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                        <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                        <br/>
                                        <font class="mediumfont">This is a Charity Only <%=Pagez._survey()%>.</font>
                                        <br/>
                                        <font class="tinyfont">The <%=Pagez._survey()%> sponsor requires that we donate all of your earnings from the <%=Pagez._survey()%> to a charity of your choice.  It's a chance to do some good!</font>
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    </td>
                <% } %>
            </tr>
        </table>
    <% } else { %>
        <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
        <font class="mediumfont" style="color: #666666;"><%=Pagez._Survey()%> Disclosure</font>
        <br/><br/>
        <font class="normalfont">
        This <%=Pagez._survey()%> is powered by a third party technology.
        </font> 
   <% } %>











<%@ include file="/template/footer.jsp" %>


