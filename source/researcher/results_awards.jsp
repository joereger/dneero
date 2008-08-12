<%@ page import="com.dneero.dao.Incentiveaward" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.Surveyincentive" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountAwardsListitem" %>
<%@ page import="com.dneero.htmluibeans.ResearcherResultsAnswers" %>
<%@ page import="com.dneero.incentive.Incentive" %>
<%@ page import="com.dneero.incentive.IncentiveFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Results";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherResults researcherResults = (ResearcherResults) Pagez.getBeanMgr().get("ResearcherResults");
%>
<%@ include file="/template/header.jsp" %>

<div class="rounded" style="background: #e6e6e6; text-align: center; padding: 15px;">
        <font class="largefont"><%=researcherResults.getSurvey().getTitle()%></font>
        <br/>
        <a href="/researcher/results.jsp" style="padding-left: 15px;"><font class="subnavfont">Results Main</font></a>
        <a href="/researcher/results_answers.jsp"style="padding-left: 15px;"><font class="subnavfont">Results</font></a>
    <a href="/researcher/results_answers_userquestions.jsp"style="padding-left: 15px;"><font class="subnavfont">User Questions</font></a>    
    <a href="/researcher/results_answers_advanced.jsp"style="padding-left: 15px;"><font class="subnavfont">Filter Results</font></a>
        <a href="/researcher/results_impressions.jsp"style="padding-left: 15px;"><font class="subnavfont">Impressions</font></a>
        <a href="/researcher/results_respondents.jsp"style="padding-left: 15px;"><font class="subnavfont">Respondents</font></a>
        <a href="/researcher/results_awards.jsp"style="padding-left: 15px;"><font class="subnavfont">Awards</font></a>
        <a href="/researcher/results_demographics.jsp"style="padding-left: 15px;"><font class="subnavfont">Demographics</font></a>
        <a href="/researcher/results_financial.jsp"style="padding-left: 15px;"><font class="subnavfont">Financial Status</font></a>
    </div>
    <br/><br/>



    <%
    ArrayList<AccountAwardsListitem> awas = new ArrayList<AccountAwardsListitem>();
    List<Incentiveaward> ias = HibernateUtil.getSession().createCriteria(Incentiveaward.class)
                                       .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                       .add(Restrictions.eq("isvalid", true))
                                       .setCacheable(true)
                                       .list();
        for (Iterator<Incentiveaward> iaIt=ias.iterator(); iaIt.hasNext();) {
            Incentiveaward incentiveaward=iaIt.next();
            Response resp = Response.get(incentiveaward.getResponseid());
            Survey survey = Survey.get(resp.getSurveyid());
            Surveyincentive si = Surveyincentive.get(incentiveaward.getSurveyincentiveid());
            Incentive incentive = IncentiveFactory.getById(si.getType(), si);
            User user = User.get(incentiveaward.getUserid());

            AccountAwardsListitem awa = new AccountAwardsListitem();
            awa.setIncentiveaward(incentiveaward);
            awa.setIncentive(incentive);
            awa.setResponse(resp);
            awa.setSurvey(survey);
            awa.setSurveyincentive(si);
            awa.setUser(user);
            awas.add(awa);
        }
    %>

    <%if (awas==null || awas.size()==0){%>
        <font class="normalfont">No awards have been granted yet.</font>
    <%} else {%>
        <%
        ArrayList<GridCol> cols=new ArrayList<GridCol>();
        cols.add(new GridCol("Detail", "<a href=\"/researcher/results_awards_detail.jsp?incentiveawardid=<$incentiveaward.incentiveawardid$>\">Details</a>", true, "", "tinyfont"));
        cols.add(new GridCol("Date", "<$incentiveaward.date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
        cols.add(new GridCol("User", "<a href=\"/profile.jsp?userid=<$incentiveaward.userid$>\"><$user.firstname$> <$user.lastname$></a>", false, "", "tinyfont"));
        cols.add(new GridCol("", "<$incentiveaward.misc1$>", false, "", "tinyfont"));
        cols.add(new GridCol("", "<$incentiveaward.misc2$>", false, "", "tinyfont"));
        cols.add(new GridCol("", "<$incentiveaward.misc3$>", false, "", "tinyfont"));
        cols.add(new GridCol("", "<$incentiveaward.misc4$>", false, "", "tinyfont"));
        cols.add(new GridCol("", "<$incentiveaward.misc5$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(awas, cols, 150, "/researcher/results_awards.jsp", "page")%>
    <%}%>
    


<%@ include file="/template/footer.jsp" %>