<%@ page import="com.dneero.dao.Question" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankAddquestion" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankDetail" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Ranking";
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
<%
if (researcherRankAddquestion.getRank()!=null && researcherRankAddquestion.getSurvey()!=null && researcherRankAddquestion.getQuestion()!=null){
    Question question = researcherRankAddquestion.getQuestion();
    String postfix = "";
    if (question.getComponenttype()==com.dneero.display.components.Dropdown.ID){
        postfix = "dropdown";
    } else if (question.getComponenttype()==com.dneero.display.components.Checkboxes.ID){
        postfix = "checkboxes";
    }
    if (!postfix.equals("")){
        Pagez.sendRedirect("/researcher/rank-addquestion-"+postfix+".jsp?rankid="+researcherRankAddquestion.getRank().getRankid()+"&questionid="+researcherRankAddquestion.getQuestion().getQuestionid()+"&surveyid="+researcherRankAddquestion.getSurvey().getSurveyid());
        return;
    }
}
%>
<%@ include file="/template/header.jsp" %>

<font class="mediumfont">Add a question to: <%=researcherRankAddquestion.getRank().getName()%></font>
<br/><br/>

<%if (researcherRankAddquestion.getSurvey()==null){%>
    <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
        <font class="formfieldnamefont">Please choose a conversation to pull the question from.</font>
    </div>
    <br/><br/>
    <%
    List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
                                       .add(Restrictions.eq("researcherid", Pagez.getUserSession().getUser().getResearcherid()))
                                       .addOrder(Order.desc("surveyid"))
                                        .setCacheable(true)
                                       .list();
        for (Iterator<Survey> surveyIterator = surveys.iterator(); surveyIterator.hasNext();) {
            Survey survey = surveyIterator.next();
            %>
            Survey: <a href="/researcher/rank-addquestion.jsp?rankid=<%=researcherRankAddquestion.getRank().getRankid()%>&surveyid=<%=survey.getSurveyid()%>"><%=survey.getTitle()%></a>
            <br/>
            <%
        }
    %>
<%} else {%>
    <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
        <font class="formfieldnamefont">From the Conversation: <%=researcherRankAddquestion.getSurvey().getTitle()%></font>
        <br/>
        <font class="formfieldnamefont">Please choose the question to add. (<b>only certain question types are currently supported</b>)</font>
    </div>
    <br/><br/>
    <%
    List<Question> questions = HibernateUtil.getSession().createCriteria(Question.class)
                                       .add(Restrictions.eq("surveyid", researcherRankAddquestion.getSurvey().getSurveyid()))
                                       .addOrder(Order.desc("questionid"))
                                        .setCacheable(true)
                                       .list();
        for (Iterator<Question> qIterator = questions.iterator(); qIterator.hasNext();) {
            Question question = qIterator.next();
            Component cpt =  ComponentTypes.getComponentByType(question.getComponenttype(), question, null);
            if (cpt.supportsRank()){
                %>
                Question: <a href="/researcher/rank-addquestion.jsp?rankid=<%=researcherRankAddquestion.getRank().getRankid()%>&surveyid=<%=researcherRankAddquestion.getSurvey().getSurveyid()%>&questionid=<%=question.getQuestionid()%>"><%=question.getQuestion()%></a>
                <br/>
                <%
            }
        }
    %>


<%}%>






<%@ include file="/template/footer.jsp" %>

