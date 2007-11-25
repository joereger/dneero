<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail01" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="com.dneero.dao.Survey" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<img src=\"/images/process-train-survey-01.gif\" align=\"right\" width=\"350\" height=\"73\" alt=\"\"/>\n" +
"        <font class=\"pagetitlefont\">"+((ResearcherSurveyDetail01) Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getTitle()+"</font>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail01 researcherSurveyDetail01 = (ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("save") || request.getParameter("action").equals("previous"))) {
        try {
            researcherSurveyDetail01.setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail01.setDescription(Textarea.getValueFromRequest("description", "Description", true));
            researcherSurveyDetail01.setStartdate(DateTime.getValueFromRequest("startdate", "Start Date", true).getTime());
            researcherSurveyDetail01.setEnddate(DateTime.getValueFromRequest("enddate", "End Date", true).getTime());
            if (request.getParameter("action").equals("next")){
                researcherSurveyDetail01.saveSurvey();
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                //researcherSurveyDetail01.getSaveSurveyAsDraft();
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                //researcherSurveyDetail01.getPreviousStep();
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="researchersurveydetail_01.jsp" method="post" id="rsdform">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail01.getSurvey().getSurveyid()%>"/>


        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        On this page you set the very general parameters for the survey.  Choose a title that'll get people interested.  Give enough information in the description for them to understand what you're trying to understand.  Your start date must be today or in the future.  You want to keep the survey open long enough to attract bloggers and have them fill out the survey... a month is a good starting point.
        </font></div></center>

        <br/><br/>

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Survey Title</font>
                </td>
                <td valign="top">
                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                        <%=Textbox.getHtml("title", researcherSurveyDetail01.getTitle(), 255, 50, "", "")%>
                    <%} else {%>
                        <font class="smallfont"><%=researcherSurveyDetail01.getTitle()%></font>
                    <%}%>
                </td>
            </tr>



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Description</font>
                </td>
                <td valign="top">
                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                        <%=Textarea.getHtml("description", researcherSurveyDetail01.getDescription(), 3, 45, "", "")%>
                    <%} else {%>
                        <font class="normalfont"><%=researcherSurveyDetail01.getDescription()%></font>
                    <%}%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Start Date</font>
                </td>
                <td valign="top">
                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                        <%=DateTime.getHtml("startdate", Time.getCalFromDate(researcherSurveyDetail01.getStartdate()), "", "")%>
                    <%} else {%>
                        <font class="normalfont"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(researcherSurveyDetail01.getStartdate()))%></font>
                    <%}%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">End Date</font>
                </td>
                <td valign="top">
                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                        <%=DateTime.getHtml("enddate", Time.getCalFromDate(researcherSurveyDetail01.getEnddate()), "", "")%>
                    <%} else {%>
                        <font class="normalfont"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(researcherSurveyDetail01.getEnddate()))%></font>
                    <%}%>
                </td>
            </tr>

        </table>
        
        <br/><br/>
        <!-- Start Bottom Nav -->
        <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.getElementById('action').value='previous';">
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail01.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->


</form>


<%@ include file="/template/footer.jsp" %>