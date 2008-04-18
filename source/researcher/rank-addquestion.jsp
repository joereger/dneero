<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankAddquestion" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankDetail" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Custom Rankings";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankAddquestion researcherRankAddquestion =(ResearcherRankAddquestion) Pagez.getBeanMgr().get("ResearcherRankAddquestion");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("dosomething")) {
        try {
            researcherRankAddquestion.saveAction();
            Pagez.getUserSession().setMessage("Panel deleted.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<font class="mediumfont">Add a question to: <%=researcherRankAddquestion.getRank().getName()%></font>
<br/><br/>

<%if (researcherRankAddquestion.getSurvey()==null){%>
    Please choose a survey to pull the question from:
    <br/>
    <%
    List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                       .add(Restrictions.eq("researcherid", Pagez.getUserSession().getUser().getResearcherid()))
                                       .addOrder(Order.desc("surveyid"))
                                        .setCacheable(true)
                                       .list();
        for (Iterator<Survey> surveyIterator = surveys.iterator(); surveyIterator.hasNext();) {
            Survey survey = surveyIterator.next();
            %>
            <a href="/researcher/rank-addquestion.jsp?rankid=<%=researcherRankAddquestion.getRank().getRankid()%>&surveyid=<%=survey.getSurveyid()%>"><%=survey.getTitle()%></a>
            <br/><br/>
            <%
        }
    %>
<%} else {%>
   Please choose the question to add:
   <br/>
    <%
    List<Question> questions = HibernateUtil.getSession().createCriteria(Question.class)
                                       .add(Restrictions.eq("surveyid", researcherRankAddquestion.getSurvey().getSurveyid()))
                                       .addOrder(Order.desc("questionid"))
                                        .setCacheable(true)
                                       .list();
        for (Iterator<Question> qIterator = questions.iterator(); qIterator.hasNext();) {
            Question question = qIterator.next();
            %>
            <a href="/researcher/rank-addquestion.jsp?rankid=<%=researcherRankAddquestion.getRank().getRankid()%>&surveyid=<%=researcherRankAddquestion.getSurvey().getSurveyid()%>&questionid=<%=question.getQuestionid()%>"><%=question.getQuestion()%></a>
            <br/><br/>
            <%
        }
    %>




<%}%>






<%@ include file="/template/footer.jsp" %>

