<%@ page import="com.dneero.dao.Incentiveaward" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.Surveyincentive" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountAwardsListitem" %>
<%@ page import="com.dneero.incentive.Incentive" %>
<%@ page import="com.dneero.incentive.IncentiveFactory" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Awards";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


    <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="smallfont" style="color: #000000;">These are the things that you've been awarded.  Sometimes it's cash.  Sometimes it's a coupon.  And in the future it'll be something else.</font>
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

            AccountAwardsListitem awa = new AccountAwardsListitem();
            awa.setIncentiveaward(incentiveaward);
            awa.setIncentive(incentive);
            awa.setResponse(resp);
            awa.setSurvey(survey);
            awa.setSurveyincentive(si);
            awas.add(awa);
        }
    %>

    <%if (awas==null || awas.size()==0){%>
        <font class="normalfont">You haven't yet been awarded anything.  Go join some conversations!</font>
    <%} else {%>
        <%
        ArrayList<GridCol> cols=new ArrayList<GridCol>();
        cols.add(new GridCol("Amount", "<a href=\"/account/award-detail.jsp?incentiveawardid=<$incentiveaward.incentiveawardid$>\">Details</a>", true, "", "tinyfont"));
        cols.add(new GridCol("Date", "<$incentiveaward.date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
        cols.add(new GridCol("From Survey", "<$survey.title$>", false, "", "tinyfont"));
        %>
        <%=Grid.render(awas, cols, 50, "/account/awards.jsp", "page")%>
    <%}%>



<%@ include file="/template/footer.jsp" %>