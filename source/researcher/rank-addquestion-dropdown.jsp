<%@ page import="com.dneero.dao.Rankquestion" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.Textbox" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankAddquestionDropdown" %>
<%@ page import="com.dneero.htmluibeans.ResearcherRankAddquestionDropdownListitem" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="com.dneero.util.Num" %>
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
    ResearcherRankAddquestionDropdown researcherRankAddquestionDropdown =(ResearcherRankAddquestionDropdown) Pagez.getBeanMgr().get("ResearcherRankAddquestionDropdown");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("saveaction")) {
        try {
            //First, delete any existing rankquestions
            HibernateUtil.getSession().createQuery("delete Rankquestion rq where rq.rankid="+researcherRankAddquestionDropdown.getRank().getRankid()+" and rq.questionid="+researcherRankAddquestionDropdown.getQuestion().getQuestionid()).executeUpdate();
            //Iterate all possible answers
            for (Iterator<ResearcherRankAddquestionDropdownListitem> iterator = researcherRankAddquestionDropdown.getPossibleanswers().iterator(); iterator.hasNext();){
                ResearcherRankAddquestionDropdownListitem rraqdli = iterator.next();
                //If the researcher has assigned points for this answer
                if (Num.isdouble(request.getParameter("points-"+rraqdli.getId()))) {
                    Double pointsDbl = Double.parseDouble(request.getParameter("points-"+rraqdli.getId()));
                    int points = pointsDbl.intValue();
                    //If the points aren't zero
                    if (points!=0){
                        Rankquestion rankquestion = null;
//                        if (rraqdli.getRankquestion()!=null && rraqdli.getRankquestion().getAnswer().equals(rraqdli.getPossibleanswer())){
//                            rankquestion = rraqdli.getRankquestion();
//                        } else {
                            rankquestion = new Rankquestion();
//                        }
                        rankquestion.setAnswer(rraqdli.getPossibleanswer());
                        rankquestion.setPoints(points);
                        rankquestion.setQuestionid(researcherRankAddquestionDropdown.getQuestion().getQuestionid());
                        rankquestion.setRankid(researcherRankAddquestionDropdown.getRank().getRankid());
                        rankquestion.setOpt1("");
                        rankquestion.setOpt2("");
                        try{rankquestion.save();}catch(Exception ex){logger.error("", ex);}
                    } else {
                        //Need to delete zero-value entries
                        List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                                                   .add(Restrictions.eq("rankid", researcherRankAddquestionDropdown.getRank().getRankid()))
                                                   .add(Restrictions.eq("questionid", researcherRankAddquestionDropdown.getQuestion().getQuestionid()))
                                                   .add(Restrictions.eq("answer", rraqdli.getPossibleanswer()))
                                                   .setCacheable(true)
                                                   .list();
                        for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
                            Rankquestion rankquestion = rankquestionIterator.next();
                            try{rankquestion.delete();}catch(Exception ex){logger.error("", ex);}
                        }
                    }
                }
            }

            researcherRankAddquestionDropdown.saveAction();
            researcherRankAddquestionDropdown.initBean();

            //Re-process the rankings for this question, but do so in a thread so that the ui is responsive
            try{
                RankForSurveyThread qThread = new RankForSurveyThread(researcherRankAddquestionDropdown.getSurvey().getSurveyid());
                qThread.startThread();
            } catch (Exception ex){logger.error("",ex);};

            Pagez.getUserSession().setMessage("Points saved.");
            Pagez.sendRedirect("/researcher/rank-detail.jsp?rankid="+researcherRankAddquestionDropdown.getRank().getRankid());
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<font class="mediumfont">Add a question to: <%=researcherRankAddquestionDropdown.getRank().getName()%></font>
<br/><br/>
<font class="mediumfont">From the Conversation: <%=researcherRankAddquestionDropdown.getSurvey().getTitle()%></font>
<br/><br/>
<font class="mediumfont">Question: <%=researcherRankAddquestionDropdown.getQuestion().getQuestion()%></font>
<br/><br/>
<div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
    <font class="formfieldnamefont">You now assign points to possible answers.  Points can either enhance the respondent's correlation to the ranking or decrease it (if you enter negative numbers).  All points must be integers and can be as large or as small as you like.</font>
</div>
<br/><br/>

    <form action="/researcher/rank-addquestion-dropdown.jsp" method="post">
        <input type="hidden" name="dpage" value="/researcher/rank-addquestion-dropdown.jsp">
        <input type="hidden" name="action" value="saveaction">
        <input type="hidden" name="rankid" value="<%=researcherRankAddquestionDropdown.getRank().getRankid()%>">
        <input type="hidden" name="surveyid" value="<%=researcherRankAddquestionDropdown.getSurvey().getSurveyid()%>">
        <input type="hidden" name="questionid" value="<%=researcherRankAddquestionDropdown.getQuestion().getQuestionid()%>">

        <table cellpadding="3" cellspacing="2" border="0">
            <tr>
                <td valign="top" bgcolor="#e6e6e6">
                    <font class="formfieldnamefont">Possible Answer</font>
                </td>
                <td valign="top" bgcolor="#e6e6e6">
                    <font class="formfieldnamefont">Points to Assign (Positive or Negative Integers)</font>
                </td>
            </tr>
            <%
                for (Iterator<ResearcherRankAddquestionDropdownListitem> iterator = researcherRankAddquestionDropdown.getPossibleanswers().iterator(); iterator.hasNext();){
                    ResearcherRankAddquestionDropdownListitem rraqdli = iterator.next();
                    int points = 0;
                    if (rraqdli.getRankquestion()!=null){
                        points = rraqdli.getRankquestion().getPoints();
                    }
                    %>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont"><%=rraqdli.getPossibleanswer()%></font>
                        </td>
                        <td valign="top">
                            <%=Textbox.getHtml("points-"+rraqdli.getId(), String.valueOf(points), 10, 5, "", "")%> <font class="smallfont">points</font>
                        </td>
                    </tr>
                    <%
                }
            %>

             <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Save Points">
                </td>
            </tr>

        </table>

    </form>



<%@ include file="/template/footer.jsp" %>

