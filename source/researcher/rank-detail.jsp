<%@ page import="com.dneero.dao.Question" %>
<%@ page import="com.dneero.dao.Rankquestion" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankDetail" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Rankings";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankDetail researcherRankDetail =(ResearcherRankDetail) Pagez.getBeanMgr().get("ResearcherRankDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("dosomething")) {
        try {
            researcherRankDetail.saveAction();
            Pagez.getUserSession().setMessage("Panel deleted.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


 <br/><br/>

    <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top">

                <table cellpadding="0" cellspacing="0" border="0" width="100%">


                <%
                    for (Iterator<Question> iterator = researcherRankDetail.getQuestions().iterator(); iterator.hasNext();){
                        Question question = iterator.next();
                        Survey survey = Survey.get(question.getSurveyid());
                        %>
                        <tr>
                            <td valign="top">
                                <font class="normalfont" style="font-weight: bold;"><a href="/researcher/rank-addquestion.jsp?rankid=<%=researcherRankDetail.getRank().getRankid()%>&questionid=<%=question.getQuestionid()%>&surveyid=<%=survey.getSurveyid()%>"><%=question.getQuestion()%></a></font>
                                <br/><img src="/images/clear.gif" alt="" width="25" height="1"><font class="tinyfont">From survey: <a href="/researcher/rank-addquestion.jsp?rankid=<%=researcherRankDetail.getRank().getRankid()%>&surveyid=<%=survey.getSurveyid()%>"><%=survey.getTitle()%></a></font>
                                <%
                                List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                                                       .add(Restrictions.eq("rankid", researcherRankDetail.getRank().getRankid()))
                                                       .add(Restrictions.eq("questionid", question.getQuestionid()))
                                                       .addOrder(Order.desc("rankquestionid"))
                                                       .setCacheable(true)
                                                       .list();
                                for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
                                    Rankquestion rankquestion = rankquestionIterator.next();
                                    int numberWhoAnsweredLikeThis = NumFromUniqueResult.getInt("SELECT count(*) from Questionresponse where questionid="+question.getQuestionid()+" and value='"+Str.cleanForSQL(rankquestion.getAnswer())+"'");
                                    %>
                                    <br/><img src="/images/clear.gif" alt="" width="35" height="1"><font class="tinyfont"><%=rankquestion.getAnswer()%> = <%=rankquestion.getPoints()%> points (<%=numberWhoAnsweredLikeThis%> people)</font>
                                    <%
                                }
                                %>
                                <br/><br/>
                            </td>
                        </tr>
                        <%
                    }
                %>
                </table>








            </td>
            <td valign="top">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/rank-addquestion.jsp" method="post">
                        <input type="hidden" name="dpage" value="/researcher/rank-addquestion.jsp">
                        <input type="hidden" name="action" value="addquestion">
                        <input type="hidden" name="rankid" value="<%=researcherRankDetail.getRank().getRankid()%>">
                        <input type="submit" class="formsubmitbutton" value="Add a Question">
                    </form>
                </div>
                <br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">Ranking Stats</font>
                    <br/>
                    <%
                    int uniqueUsers = NumFromUniqueResult.getInt("SELECT count(distinct userid) from Rankuser");
                    %>
                    <br/><font class="smallfont"><%=uniqueUsers%> People Ranked</font>
                    <%
                    int pointsTotal = NumFromUniqueResult.getInt("SELECT count(points) from Rankuser");
                    %>
                    <br/><font class="smallfont"><%=pointsTotal%> Points Assessed</font>
                    
                </div>
            </td>
        </tr>
    </table>





<%@ include file="/template/footer.jsp" %>

