<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyResults" %>
<%@ page import="com.dneero.dao.Survey" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults")).getSurvey().getTitle();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicSurveyResults publicSurveyResults = (PublicSurveyResults)Pagez.getBeanMgr().get("PublicSurveyResults");
%>
<%@ include file="/template/header.jsp" %>



    <font class="smallfont"><%=publicSurveyResults.getSurvey().getDescription()%></font><br/><br/><br/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Post It"><span>Post It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Results"><span>Results</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Who Took It?"><span>Who Took It?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>" title="LDisclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="450">

                <script language="JavaScript" type="text/javascript">
                  <%if (publicSurveyResults.getResultsshowyourfriendstab()){%>
                    var panels = new Array('panel1', 'panel2', 'panel3');
                  <%} else {%>
                    var panels = new Array('panel1', 'panel2');
                  <%}%>
                  var selectedTab = null;
                  function showPanel(tab, name)
                  {
                    if (selectedTab)
                    {
                      selectedTab.style.backgroundColor = '';
                      selectedTab.style.paddingTop = '';
                      selectedTab.style.marginTop = '4px';
                    }
                    selectedTab = tab;
                    selectedTab.style.backgroundColor = 'white';
                    selectedTab.style.paddingTop = '6px';
                    selectedTab.style.marginTop = '0px';

                    for(i = 0; i < panels.length; i++){
                      document.getElementById(panels[i]).style.display = (name == panels[i]) ? 'block':'none';
                    }
                    return false;
                  }
                </script>
                <div id="tabs">
                <%
                    String onclick = "onclick=\"return false;\"";
                    if (Pagez.getUserSession().getIsfacebookui()){
                        onclick = "";
                    }
                %>
                <a href="/surveyresults.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>&panel=panel1" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel1');" id="tab1" <%=onclick%>>Everybody</a>
                <%if (!Pagez.getUserSession().getIsfacebookui()){%>
                    <a href="/surveyresults.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>&panel=panel2" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" <%=onclick%>><%=publicSurveyResults.getResultsfriendstabtext()%></a>
                <%}%>
                <%if (publicSurveyResults.getResultsshowyourfriendstab()){%>
                    <a href="/surveyresults.jsp?surveyid=<%=publicSurveyResults.getSurvey().getSurveyid()%>&panel=panel3" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel3');" <%=onclick%>>Your Friends</a>
                <%}%>
                </div>
                <%
                String panel1style = "display: none";
                if (request.getParameter("panel")!=null && request.getParameter("panel").equals("panel1")){
                    panel1style = "display: block";
                }
                if (request.getParameter("panel")==null){
                    panel1style = "display: block";    
                }
                %>
                <div class="panel" id="panel1" style="<%=panel1style%>">
                    <img src="/images/clear.gif" width="415" height="1"/><br/>
                    <font class="mediumfont" style="color: #cccccc;">Everybody</font><br/>
                    <%=publicSurveyResults.getResultsHtml()%>
                </div>
                <%
                String panel2style = "display: none";
                if (request.getParameter("panel")!=null && request.getParameter("panel").equals("panel2")){
                    panel2style = "display: block";
                }
                %>
                <%if (!Pagez.getUserSession().getIsfacebookui()){%>
                    <div class="panel" id="panel2" style="<%=panel2style%>">
                        <img src="/images/clear.gif" width="415" height="1"/><br/>
                        <font class="mediumfont" style="color: #cccccc;"><%=publicSurveyResults.getResultsfriendstabtext()%></font><br/>
                        <table width="100%" cellpadding="10" cellspacing="0" border="0">
                            <tr>
                                <td valign="top">
                                    <%=publicSurveyResults.getResultsHtmlForUserWhoTookSurvey()%>
                                </td>
                            </tr>
                        </table>
                    </div>
                <%}%>
                <%if (publicSurveyResults.getResultsshowyourfriendstab()){%>
                    <%
                    String panel3style = "display: none";
                    if (request.getParameter("panel")!=null && request.getParameter("panel").equals("panel3")){
                        panel3style = "display: block";
                    }
                    %>
                    <div class="panel" id="panel3" style="<%=panel3style%>">
                        <img src="/images/clear.gif" width="415" height="1"/><br/>
                        <font class="mediumfont" style="color: #cccccc;">Your Friends</font><br/>
                        <%=publicSurveyResults.getResultsYourFriends()%>
                    </div>
                <%}%>
            </td>
            <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                <td valign="top" align="left">
                    <div class="rounded" style="background: #00ff00;">
                        <div class="rounded" style="background: #ffffff; text-align: center;">
                            <center><img src="/images/statistic-128.png" width="128" height="128"/></center>
                            <br/>
                            <% if (publicSurveyResults.getSurvey().getStatus()>=Survey.STATUS_CLOSED){ %>
                                <div class="rounded" style="background: #cccccc; text-align: center;">
                                    <center><img src="/images/stop-alt-48.png" width="48" height="48"/></center>
                                    <br/>
                                    <font class="mediumfont">This survey is closed.</font>
                                </div>
                                <br/>
                            <% } %>
                            <% if (publicSurveyResults.getSurvey().getIscharityonly()){ %>
                                <br/><br/>
                                <div class="rounded" style="background: #e6e6e6; text-align: center;">
                                    <img src="/images/charity-128.png" alt="For Charity" width="128" height="128"/>
                                    <br/>
                                    <font class="mediumfont">This is a Charity Only survey.</font>
                                    <br/>
                                    <font class="tinyfont">The creator of the survey requires that dNeero donate all of your earnings from the survey to a charity of your choice.  It's a chance to do some good!</font>
                                </div>
                            <% } %>
                        </div>
                        <br/>
                        <font class="smallfont">
                            <br/><br/><b>For more info:</b><br/>
                            Click the Questions tab on this page.
                        </font>
                    </div>
                </td>
            <% } %>
        </tr>
    </table>






<%@ include file="/template/footer.jsp" %>


