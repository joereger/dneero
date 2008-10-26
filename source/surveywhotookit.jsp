<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyWhotookit" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.dao.Survey" %>
<%
PublicSurveyWhotookit publicSurveyWhotookit = (PublicSurveyWhotookit)Pagez.getBeanMgr().get("PublicSurveyWhotookit");
%>
<%
    //If we don't have a surveyid, shouldn't be on this page
    if (publicSurveyWhotookit.getSurvey() == null || publicSurveyWhotookit.getSurvey().getTitle() == null || publicSurveyWhotookit.getSurvey().getSurveyid()<=0) {
        Pagez.sendRedirect("/publicsurveylist.jsp");
        return;
    }
    //If the survey is draft or waiting
    if (publicSurveyWhotookit.getSurvey().getStatus()<Survey.STATUS_OPEN) {
        Pagez.sendRedirect("/surveynotopen.jsp");
        return;
    }
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicSurveyWhotookit) Pagez.getBeanMgr().get("PublicSurveyWhotookit")).getSurvey().getTitle();
String navtab="home";
String acl="public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



    <font class="smallfont"><%=publicSurveyWhotookit.getSurvey().getDescription()%></font><br/><br/><br/>

    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Share It"><span>Share It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Answers"><span>Answers</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Who's In?"><span>Who's In?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>" title="Disclosure"><span>Disclosure</span></a></li>
      </ul>
    </div>
    <br/><br/><br/>


    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>
    <table width="100%" cellpadding="5">
        <tr>
            <td valign="top" width="150">
                <img src="/images/users-128.png" width="128" height="128"/>
            </td>
            <td valign="top">
                <center><div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;"><font class="smallfont">
                People who joined the conversation and where it's been posted.
                </font></div></center>
            </td>
        </tr>
    </table>

    <br/>
    <script language="JavaScript" type="text/javascript">
      var panels = new Array('panel1', 'panel2');
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
    <a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>&panel=panel1" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel1');" id="tab1" <%=onclick%>>Who Took It?</a>
    <%if (!Pagez.getUserSession().getIsfacebookui()){%>
        <a href="/surveywhotookit.jsp?surveyid=<%=publicSurveyWhotookit.getSurvey().getSurveyid()%>&panel=panel2" class="tab" onmousedown="return event.returnValue = showPanel(this, 'panel2');" <%=onclick%>>Where It's Been Posted</a>
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
            <img src="/images/clear.gif" width="550" height="1"/><br/>
            <%=publicSurveyWhotookit.getWhotookitHtml()%>
    </div>
    <%
    String panel2style = "display: none";
    if (request.getParameter("panel")!=null && request.getParameter("panel").equals("panel2")){
        panel2style = "display: block";
    }
    %>
    <div class="panel" id="panel2" style="<%=panel2style%>">
            <img src="/images/clear.gif" width="550" height="1"/><br/>
            <%=publicSurveyWhotookit.getImpressionsHtml()%>
    </div>


    




<%@ include file="/template/footer.jsp" %>


