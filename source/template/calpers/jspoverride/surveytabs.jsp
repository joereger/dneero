<div id="csstabs">
  <ul>
    <li><a href="/survey.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>" title="Questions"><span>Your Conversation</span></a></li>
    <li><a href="/surveypostit.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>" title="What's Next"><span>What's Next</span></a></li>
    <li><a href="/surveyresults.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>" title="Answers"><span>All Users' Answers</span></a></li>
    <li><a href="/surveywhotookit.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>" title="Who's In?"><span>Who's In?</span></a></li>
    <li><a href="/surveydiscuss.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
    <!--<li><a href="/surveyrequirements.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>-->
    <%if (!surveyInTabs.getIsfree()){%><!--<li><a href="/surveydisclosure.jsp?surveyid=<%=surveyInTabs.getSurveyid()%>" title="Disclosure"><span>Disclosure</span></a></li>--><%} %>
  </ul>
</div>
<br/><br/><br/>