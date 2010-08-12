<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmluibeans.PublicSurveyDiscuss" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%@ page import="java.util.ArrayList" %>
<%
PublicSurveySingleUserquestion publicSurveySingleUserquestion = (PublicSurveySingleUserquestion)Pagez.getBeanMgr().get("PublicSurveySingleUserquestion");
%>
<%
    //If we don't have a surveyid, shouldn't be on this page
    if (publicSurveySingleUserquestion.getSurvey() == null || publicSurveySingleUserquestion.getSurvey().getTitle() == null || publicSurveySingleUserquestion.getSurvey().getSurveyid()<=0) {
        Pagez.sendRedirect("/publicsurveylist.jsp");
        return;
    }
//If the survey is draft or waiting
    if (publicSurveySingleUserquestion.getSurvey().getStatus()<Survey.STATUS_OPEN) {
        Pagez.sendRedirect("/surveynotopen.jsp");
        return;
    }
    if (publicSurveySingleUserquestion.getQuestion()==null || publicSurveySingleUserquestion.getQuestion().getQuestionid()<=0) {
        Pagez.sendRedirect("/survey.jsp?s="+request.getParameter("surveyid"));
        return;
    }
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("answerquestion")) {
        try {
            publicSurveySingleUserquestion.answerQuestion();
            Pagez.getUserSession().setMessage("You have answered the question '"+publicSurveySingleUserquestion.getQuestion().getQuestion()+"' and it will be appended to your complete response!");
            Pagez.sendRedirect("/surveyresults.jsp?surveyid="+request.getParameter("surveyid")+"&tab=userquestions");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <% Survey surveyInTabs = publicSurveySingleUserquestion.getSurvey();%>
    <%@ include file="/template/calpers/jspoverride/surveytabs.jsp" %>

    <font class="mediumfont" style="color: #cccccc;">Answer a Single Question</font>
    <br/>

    <a href="/survey.jsp?surveyid=<%=publicSurveySingleUserquestion.getSurvey().getSurveyid()%>"><font class="largefont" style="color: #666666;"><%=publicSurveySingleUserquestion.getSurvey().getTitle()%></font></a>
    <br/><br/><br/>

    <img src="/images/clear.gif" width="700" height="1" class="survey_tabs_body_width"/><br/>


    <% if (Pagez.getUserSession().getIsloggedin() && publicSurveySingleUserquestion.getLoggedinuserhasalreadytakensurvey()){ %>
        <br/><br/>
        <form action="/surveysingleuserquestion.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/surveysingleuserquestion.jsp">
            <input type="hidden" name="action" value="answerquestion">
            <input type="hidden" name="surveyid" value="<%=publicSurveySingleUserquestion.getSurvey().getSurveyid()%>">
            <input type="hidden" name="questionid" value="<%=publicSurveySingleUserquestion.getQuestion().getQuestionid()%>">
            <input type="hidden" name="returnurl" value="<%=request.getParameter("returnurl")%>">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <%
                            PublicSurveyUserquestionListitem psli = new PublicSurveyUserquestionListitem();
                            psli.setQuestion(publicSurveySingleUserquestion.getQuestion());
                            psli.setUser(Pagez.getUserSession().getUser());
                            Blogger blogger = null;
                            if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                                blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
                            }
                            psli.setComponent(ComponentTypes.getComponentByType(publicSurveySingleUserquestion.getQuestion().getComponenttype(), publicSurveySingleUserquestion.getQuestion(), blogger));
                            %><font class="smallfont" style="font-weight: bold;"><%=psli.getUser().getNickname()%> wants to know:</font><br/><%
                            %><%=psli.getComponent().getHtmlForInput(publicSurveySingleUserquestion.getResponse())%><%
                        %>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyl" value="Answer this Question">
                    </td>
                </tr>

            </table>
        </form>
    <% } else { %>
        <font class="mediumfont">Sorry.  You must be logged in and you must have <a href="/survey.jsp?s=<%=publicSurveySingleUserquestion.getSurvey().getSurveyid()%>">completed this <%=Pagez._survey()%></a> before you can answer individual questions.</font>
    <% }  %>





<%@ include file="/template/footer.jsp" %>


