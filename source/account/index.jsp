<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.AccountIndex" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "youraccount";
String acl = "account";
%>
<%
AccountIndex accountIndex = (AccountIndex) Pagez.getBeanMgr().get("AccountIndex");
%>
<%
if (accountIndex.getUserhasresponsependings()){
    Pagez.getUserSession().setWhereToRedirectToAfterSignup("");
    Pagez.sendRedirect("/blogger/index.jsp");
    return;
}
%>
<%
if (Pagez.getUserSession().getWhereToRedirectToAfterSignup()!=null && !Pagez.getUserSession().getWhereToRedirectToAfterSignup().equals("")){
    String whereToRedirectToAfterSignup = Pagez.getUserSession().getWhereToRedirectToAfterSignup();
    Pagez.getUserSession().setWhereToRedirectToAfterSignup("");
    Pagez.sendRedirect(whereToRedirectToAfterSignup);
    return;
}
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>





    <%
    if (true){
        String templateHomepageName = "account-index.vm-"+Pagez.getUserSession().getPl().getPlid();
        String templateHomepage = PlTemplate.getFileFromPlTemplateDir(Pagez.getUserSession().getPl(), "account-index.vm");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("pagetitle", pagetitle);
        velocityContext.put("navtab", navtab);
        velocityContext.put("acl", acl);
        velocityContext.put("accountIndex", accountIndex);
        velocityContext.put("userSession", Pagez.getUserSession());
        velocityContext.put("_survey", Pagez._survey());
        velocityContext.put("_surveys", Pagez._surveys());
        velocityContext.put("_Survey", Pagez._Survey());
        velocityContext.put("_Surveys", Pagez._Surveys());
        velocityContext.put("PROP_FACEBOOK_APP_NAME", SystemProperty.PROP_FACEBOOK_APP_NAME);
        velocityContext.put("requestParamMsg", request.getParameter("msg"));
        //
        long unreadInboxItems = NumFromUniqueResult.getInt("select count(*) from Mail where userid='"+Pagez.getUserSession().getUser().getUserid()+"' and isread=false");
        velocityContext.put("unreadInboxItems", unreadInboxItems);
        //
        long openReviewItems = NumFromUniqueResult.getInt("select count(*) from Review where useridofcontentcreator='"+Pagez.getUserSession().getUser().getUserid()+"' and (isresearcherrejected=true or isresearcherwarned=true or issysadminwarned=true or issysadminrejected=true)");
        velocityContext.put("openReviewItems", openReviewItems);
        //
        BloggerCompletedsurveys bloggerCompletedsurveys = new BloggerCompletedsurveys();
        bloggerCompletedsurveys.setMaxtodisplay(1);
        bloggerCompletedsurveys.initBean();
        velocityContext.put("bloggerCompletedsurveys", bloggerCompletedsurveys);
        //
        ResearcherSurveyList researcherSurveyList = null;
        if (Pagez.getUserSession().getPl().getIsanybodyallowedtocreatesurveys() || Pagez.getUserSession().getIsCreateSurveys()){
            researcherSurveyList = new ResearcherSurveyList();
            researcherSurveyList.setMaxtodisplay(3);
            researcherSurveyList.initBean();
        }
        velocityContext.put("researcherSurveyList", researcherSurveyList);
        //
        BloggerCompletedTwitasks bloggerCompletedTwitasks = new BloggerCompletedTwitasks();
        bloggerCompletedTwitasks.setMaxtodisplay(1);
        bloggerCompletedTwitasks.initBean();
        velocityContext.put("bloggerCompletedTwitasks", bloggerCompletedTwitasks);
        //Build the page
        String thePage = TemplateProcessor.process(templateHomepageName, templateHomepage, velocityContext);
        %>
        <%=thePage%>
     <%} %>



       



<%@ include file="/template/footer.jsp" %>


