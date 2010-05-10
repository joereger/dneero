    <div id="csstabs">
      <ul>
        <li><a href="/survey.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Questions"><span>Questions</span></a></li>
        <li><a href="/surveypostit.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Share It"><span>Share It</span></a></li>
        <li><a href="/surveyresults.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Answers"><span>Answers</span></a></li>
        <li><a href="/surveywhotookit.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Who's In?"><span>Who's In?</span></a></li>
        <li><a href="/surveydiscuss.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Discuss"><span>Discuss</span></a></li>
        <li><a href="/surveyrequirements.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Requirements"><span>Requirements</span></a></li>
        <%if (!publicSurvey.getSurvey().getIsfree()){%><li><a href="/surveydisclosure.jsp?surveyid=<%=publicSurvey.getSurvey().getSurveyid()%>" title="Disclosure"><span>Disclosure</span></a></li><%} %>
      </ul>
    </div>
    <br/><br/><br/>