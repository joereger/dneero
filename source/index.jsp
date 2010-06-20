<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%@ page import="com.dneero.htmluibeans.PublicIndex" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
PublicIndex publicIndex = (PublicIndex)Pagez.getBeanMgr().get("PublicIndex");
%>
<%
    if (request.getParameter("accesscode")!=null && !request.getParameter("accesscode").equals("")) {
        Pagez.getUserSession().setAccesscode(request.getParameter("accesscode"));
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("enteraccesscode")) {
        try {
            publicIndex.enterAccessCode();
            Pagez.getUserSession().setMessage("Sorry, no "+ Pagez._surveys()+" were found for that Access Code.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action")!=null && request.getParameter("action").equals("newtwitask")) {
        try {
            Pagez.getUserSession().setTwitaskQuestionFromHomepage(request.getParameter("twitterquestionfromhomepage"));
            if (Pagez.getUserSession().getIsloggedin()){
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp");
                return;
            }  else {
                Pagez.getUserSession().setWhereToRedirectToAfterSignup("/researcher/researchertwitaskdetail_01.jsp");
                Pagez.sendRedirect("/registration.jsp");
                return;
            }
        } catch (Exception ex) {
            logger.error(ex);
            Pagez.getUserSession().setMessage("There has been some sort of error... please try again.");
        }
    }
%>
<%
    if (request.getParameter("action")!=null && request.getParameter("action").equals("newsurvey")) {
        try {
            Pagez.getUserSession().setSurveyTitleFromHomepage(request.getParameter("surveytitlefromhomepage"));
            Pagez.getUserSession().setSurveyDescriptionFromHomepage(request.getParameter("surveydescriptionfromhomepage"));
            if (Pagez.getUserSession().getIsloggedin()){
                Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp");
                return;
            }  else {
                Pagez.getUserSession().setWhereToRedirectToAfterSignup("/researcher/researchersurveydetail_01.jsp");
                Pagez.sendRedirect("/registration.jsp");
                return;   
            }
        } catch (Exception ex) {
            logger.error(ex);
            Pagez.getUserSession().setMessage("There has been some sort of error... please try again.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <%
    if (1==1){
        String templateHomepageName = "index-plid-"+Pagez.getUserSession().getPl().getPlid();
        String templateHomepage = PlTemplate.getHomepagetemplate(Pagez.getUserSession().getPl());
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("pagetitle", pagetitle);
        velocityContext.put("navtab", navtab);
        velocityContext.put("acl", acl);
        velocityContext.put("totalConvoJoins", (TotalSurveysTaken) GetCachedStuff.get(new TotalSurveysTaken(), Pagez.getUserSession().getPl()));
        velocityContext.put("totalConvoEmbeds", (TotalImpressions) GetCachedStuff.get(new TotalImpressions(), Pagez.getUserSession().getPl()));
        velocityContext.put("usersWithMostConvos", (MostActiveUsersByTotalSurveysTaken) GetCachedStuff.get(new MostActiveUsersByTotalSurveysTaken(), Pagez.getUserSession().getPl()));
        velocityContext.put("donationsMiniReport", (DonationsToCharityMiniReport) GetCachedStuff.get(new DonationsToCharityMiniReport(), Pagez.getUserSession().getPl()));
        //velocityContext.put("recentDonations", (MostRecentDonations) GetCachedStuff.get(new MostRecentDonations(), Pagez.getUserSession().getPl()));
        velocityContext.put("recentConvos", (RecentSurveys) GetCachedStuff.get(new RecentSurveys(), Pagez.getUserSession().getPl()));
        velocityContext.put("recentConvoJoins", (RecentSurveyResponses) GetCachedStuff.get(new RecentSurveyResponses(), Pagez.getUserSession().getPl()));
        velocityContext.put("twitterAnswers", (TwitterQuestionAnswers) GetCachedStuff.get(new TwitterQuestionAnswers(), Pagez.getUserSession().getPl()));
        //velocityContext.put("recentlyPaid", (MostRecentPaidInBalance) GetCachedStuff.get(new MostRecentPaidInBalance(), Pagez.getUserSession().getPl()));
        //velocityContext.put("newestUsers", (NewestUsers) GetCachedStuff.get(new NewestUsers(), Pagez.getUserSession().getPl()));
        velocityContext.put("blogPosts", (BlogPosts) GetCachedStuff.get(new BlogPosts(), Pagez.getUserSession().getPl()));
        velocityContext.put("blogPostsFull", (BlogPostsFull) GetCachedStuff.get(new BlogPostsFull(), Pagez.getUserSession().getPl()));
        String homepage = TemplateProcessor.process(templateHomepageName, templateHomepage, velocityContext);
        %>
        <%=homepage%>
    <%}%>




<%@ include file="/template/footer.jsp" %>