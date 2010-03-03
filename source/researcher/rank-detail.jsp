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
String pagetitle = "Ranking";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankDetail researcherRankDetail =(ResearcherRankDetail) Pagez.getBeanMgr().get("ResearcherRankDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("editranking")) {

        try {
            if (request.getParameter("name")!=null && !request.getParameter("name").equals("")) {
                researcherRankDetail.getRank().setName(request.getParameter("name"));
                try{researcherRankDetail.getRank().save();}catch(Exception ex){logger.error("", ex);}
                researcherRankDetail.saveAction();
                Pagez.getUserSession().setMessage("Ranking edited.");
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<font class="mediumfont"><%=researcherRankDetail.getRank().getName()%></font>
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
                                <br/><img src="/images/clear.gif" alt="" width="25" height="1"><font class="tinyfont">From conversation: <a href="/researcher/rank-addquestion.jsp?rankid=<%=researcherRankDetail.getRank().getRankid()%>&surveyid=<%=survey.getSurveyid()%>"><%=survey.getTitle()%></a></font>
                                <%
                                List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                                                       .add(Restrictions.eq("rankid", researcherRankDetail.getRank().getRankid()))
                                                       .add(Restrictions.eq("questionid", question.getQuestionid()))
                                                       .addOrder(Order.desc("rankquestionid"))
                                                       .setCacheable(true)
                                                       .list();
                                for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
                                    Rankquestion rankquestion = rankquestionIterator.next();
                                    int numberWhoAnsweredLikeThis = NumFromUniqueResult.getInt("SELECT count(*) from Rankuser where rankquestionid="+rankquestion.getRankquestionid()+" and rankid='"+rankquestion.getRankid()+"'");
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
            <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/rank-addquestion.jsp" method="post" class="niceform">
                        <input type="hidden" name="dpage" value="/researcher/rank-addquestion.jsp">
                        <input type="hidden" name="action" value="addquestion">
                        <input type="hidden" name="rankid" value="<%=researcherRankDetail.getRank().getRankid()%>">
                        <input type="submit" class="formsubmitbutton" value="Add a Question">
                    </form>
                    <font class="tinyfont">You build your ranking by defining points for possible answers to questions.  When people answer the questions that constitute the index they score (or lose) points.</font>
                </div>
                <br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">Ranking Stats</font>
                    <br/>
                    <%
                    int uniqueUsers = NumFromUniqueResult.getInt("SELECT count(distinct userid) from Rankuser where rankid='"+researcherRankDetail.getRank().getRankid()+"'");
                    %>
                    <br/><font class="smallfont"><a href="/researcher/rank-people.jsp?rankid=<%=researcherRankDetail.getRank().getRankid()%>"><%=uniqueUsers%> People</a> Ranked</font>
                    <%
                    int pointsTotal = NumFromUniqueResult.getInt("SELECT sum(points) from Rankuser where rankid='"+researcherRankDetail.getRank().getRankid()+"'");
                    %>
                    <br/><font class="smallfont"><%=pointsTotal%> Points Assessed</font>

                </div>
                <br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/panels-addpeople.jsp" method="get">
                        <input type="hidden" name="dpage" value="/researcher/panels-addpeople.jsp">
                        <input type="hidden" name="showonly" value="addbyrankingpercent">
                        <input type="hidden" name="rankid" value="<%=researcherRankDetail.getRank().getRankid()%>">
                        <input type="submit" class="formsubmitbutton" value="Add People to Panel">
                    </form>
                    <font class="tinyfont">Add all or some or these people to Panels of respondents.  On the next screen you can apply filters to only add the top 90%, for example.</font>
                </div>
                <br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/rank-demographics.jsp" method="get">
                        <input type="hidden" name="dpage" value="/researcher/rank-demographics.jsp">
                        <input type="hidden" name="rankid" value="<%=researcherRankDetail.getRank().getRankid()%>">
                        <input type="submit" class="formsubmitbutton" value="Ranking Demographics">
                    </form>
                    <font class="tinyfont">Age, gender, etc for members of this Ranking.</font>
                </div>
                <br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/rank-detail.jsp" method="post" class="niceform">
                        <input type="hidden" name="dpage" value="/researcher/rank-detail.jsp">
                        <input type="hidden" name="action" value="editranking">
                        <input type="hidden" name="rankid" value="<%=researcherRankDetail.getRank().getRankid()%>">
                        <%=Textbox.getHtml("name", researcherRankDetail.getRank().getName(), 255, 25, "", "")%>
                        <br/>
                        <input type="submit" class="formsubmitbutton" value="Edit Ranking">
                    </form>
                </div>

            </td>
        </tr>
    </table>





<%@ include file="/template/footer.jsp" %>

