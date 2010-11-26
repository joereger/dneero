<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.helpers.ResearcherCreateIfNeeded" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail01" %>
<%@ page import="com.dneero.util.Time" %>
<%String jspPageName="/researcher/researchersurveydetail_01.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "<font class=\"pagetitlefont\">"+((ResearcherSurveyDetail01) Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getTitle()+"</font>\n" +
"        <br clear=\"all\"/>";
if (((ResearcherSurveyDetail01) Pagez.getBeanMgr().get("ResearcherSurveyDetail01")).getTitle().equals("")){
    pagetitle = "<font class=\"pagetitlefont\">Create a "+Pagez._Survey()+"</font>\n" +
"        <br clear=\"all\"/>";
}
String navtab = "researchers";
String acl = "researcher";
%>
<%
    ResearcherCreateIfNeeded.createIfNecessary();
%>
<%
if (!Pagez.getUserSession().getPl().getIsanybodyallowedtocreatesurveys()){
    if (!Pagez.getUserSession().getIsCreateSurveys()){
        Pagez.getUserSession().setMessage("Sorry, you don't appear to have Create Surveys permission.  Please contact a system administrator if you believe this is an error.");
        Pagez.sendRedirect("/researcher/index.jsp");
        return;
    }
}
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherSurveyDetail01 researcherSurveyDetail01 = (ResearcherSurveyDetail01)Pagez.getBeanMgr().get("ResearcherSurveyDetail01");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous") || request.getParameter("action").equals("editlaunched"))) {
        try {
            if (request.getParameter("action").equals("editlaunched")) {
                logger.debug("Editlaunched was clicked");
                Pagez.getUserSession().setMessage("Your "+Pagez._Survey()+" has been unlaunched and is available for editing.");
                researcherSurveyDetail01.editLaunched();
                Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp?surveyid="+researcherSurveyDetail01.getSurvey().getSurveyid());
                return;
            } 
            if (researcherSurveyDetail01.getSurvey().getStatus()>Survey.STATUS_DRAFT){
                Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail01.getSurvey().getSurveyid());
                return;
            }
            researcherSurveyDetail01.setTitle(Textbox.getValueFromRequest("title", "Title", true, DatatypeString.DATATYPEID));
            researcherSurveyDetail01.setDescription(Textarea.getValueFromRequest("description", "Description", true));
            researcherSurveyDetail01.setStartdate(DateTime.getValueFromRequest("startdate", "Start Date", true).getTime());
            researcherSurveyDetail01.setEnddate(DateTime.getValueFromRequest("enddate", "End Date", true).getTime());
            researcherSurveyDetail01.setEmbedversion(Dropdown.getIntFromRequest("embedversion", "Embed Version", true));
            researcherSurveyDetail01.setIsfree(!CheckboxBoolean.getValueFromRequest("isfree"));
            researcherSurveyDetail01.setIsopentoanybody(!CheckboxBoolean.getValueFromRequest("isopentoanybody"));
            researcherSurveyDetail01.setIsuserrequiredtoaddquestion(!CheckboxBoolean.getValueFromRequest("isuserrequiredtoaddquestion"));
            researcherSurveyDetail01.setIsanonymousresponseallowed(CheckboxBoolean.getValueFromRequest("isanonymousresponseallowed"));
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherSurveyDetail01.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_02.jsp?surveyid="+researcherSurveyDetail01.getSurvey().getSurveyid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your "+Pagez._Survey()+" has been saved.");
                researcherSurveyDetail01.saveSurvey();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherSurveyDetail01.saveSurvey();
                Pagez.sendRedirect("/researcher/researchersurveydetail_01.jsp?surveyid="+researcherSurveyDetail01.getSurvey().getSurveyid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>

<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchersurveydetail_01.jsp" method="post"  class="niceform" id="rsdform">
    <input type="hidden" name="dpage" value="/researcher/researchersurveydetail_01.jsp">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="surveyid" value="<%=researcherSurveyDetail01.getSurvey().getSurveyid()%>"/>

    <a href="#" id="helplink"><img src="/images/helpswitch.gif" alt="Help" border="0" align="right"/></a>
    <div id="togglehelp">
        <div class="rounded" data-corner="20px"><font class="smallfont">
            On this page you set the very general parameters for the <%=Pagez._survey()%>.  Choose a title that'll get people interested.  Give enough information in the description for them to understand what you're trying to understand.  Your start date must be today or in the future.  You want to keep the <%=Pagez._survey()%> open long enough to attract bloggers and have them join the <%=Pagez._survey()%>... a month is a good starting point.
        </font></div>
    </div>
    <br/><br/>

    <div id="tabs">
        <ul>
            <li><a href="#tabs-1"><%=Pagez._Survey()%> Basics</a></li>
        </ul>
        <div id="tabs-1">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">

                        <table cellpadding="5" cellspacing="0" border="0">
                            <tr>
                                <td valign="top">
                                    <font class="formfieldnamefont"><%=Pagez._Survey()%> Title</font>
                                </td>
                                <td valign="top">
                                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                                        <%=Textbox.getHtml("title", researcherSurveyDetail01.getTitle(), 255, 50, "", "width:350px;")%>
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
                                        <%=Textarea.getHtml("description", researcherSurveyDetail01.getDescription(), 3, 45, "", "width:350px;")%>
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
                            <tr>
                                <td valign="top">
                                    <font class="formfieldnamefont"></font>
                                </td>
                                <td valign="top">
                                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                                        <%=CheckboxBoolean.getHtml("isuserrequiredtoaddquestion", !researcherSurveyDetail01.getIsuserrequiredtoaddquestion(), "", "")%> Users don't have to add a question to the <%=Pagez._survey()%> (recommendation: keep unchecked)
                                    <%} else {%>
                                        <font class="normalfont">User Must Add Question: <%=researcherSurveyDetail01.getIsuserrequiredtoaddquestion()%></font>
                                    <%}%>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <font class="formfieldnamefont"></font>
                                </td>
                                <td valign="top">
                                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                                        <%=CheckboxBoolean.getHtml("isfree", !researcherSurveyDetail01.getIsfree(), "", "")%> I want to pay participants via cash, coupon or charitable donation (recommendation: keep unchecked)
                                    <%} else {%>
                                        <font class="normalfont">There is No Incentive: <%=researcherSurveyDetail01.getIsfree()%></font>
                                    <%}%>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <font class="formfieldnamefont"></font>
                                </td>
                                <td valign="top">
                                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                                        <%=CheckboxBoolean.getHtml("isopentoanybody", !researcherSurveyDetail01.getIsopentoanybody(), "", "")%> I want to limit who can participate (recommendation: keep unchecked)
                                    <%} else {%>
                                        <font class="normalfont">Open to Anybody: <%=researcherSurveyDetail01.getIsopentoanybody()%></font>
                                    <%}%>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <font class="formfieldnamefont"></font>
                                </td>
                                <td valign="top">
                                    <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                                        <%=CheckboxBoolean.getHtml("isanonymousresponseallowed", researcherSurveyDetail01.getIsanonymousresponseallowed(), "", "")%> People can respond anonymously (recommendation: keep unchecked)
                                    <%} else {%>
                                        <font class="normalfont">People can respond anonymously: <%=researcherSurveyDetail01.getIsanonymousresponseallowed()%></font>
                                    <%}%>
                                </td>
                            </tr>
                            <%if (1==2 || Pagez.getUserSession().getIsSysadmin()){%>
                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont">Embed Version</font>
                                    </td>
                                    <td valign="top">
                                        <%if (researcherSurveyDetail01.getSurvey().getStatus()<=Survey.STATUS_DRAFT) {%>
                                            <%=Dropdown.getHtml("embedversion", String.valueOf(researcherSurveyDetail01.getEmbedversion()),researcherSurveyDetail01.getEmbedVersions(), "", "")%>
                                        <%} else {%>
                                            <font class="normalfont">Embed Version: <%=researcherSurveyDetail01.getEmbedversion()%></font>
                                        <%}%>
                                    </td>
                                </tr>
                            <%} else {%>
                                <input type="hidden" name="embedversion" value="<%=Survey.EMBEDVERSION_02%>">
                            <%}%>
                        </table>

                    </td>
                    <td valign="top">
                        <!-- Right Side placeholder -->
                    </td>
                </tr>
            </table>

        </div></div>

        <br/><br/>
        <!-- Start Bottom Nav -->
        <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <!--<input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Previous Step" onclick="document.getElementById('action').value='previous';">-->
            </td>
            <td valign="top" align="right">
                <%if (researcherSurveyDetail01.getSurvey().getStatus()==Survey.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>

                <%if (researcherSurveyDetail01.getSurvey().getStatus()==Survey.STATUS_OPEN) {%>
                    <%if (Pagez.getUserSession().getIsEditLaunchedSurveys() && Pagez.getUserSession().getIsCreateSurveys()){ %>
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Unlaunch and Edit" onclick="document.getElementById('action').value='editlaunched';">
                    <%}%>
                <%}%>




                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->


    <script>
        $("#helplink").click(function() {
            $("#togglehelp").toggle();
        });
        $("#togglehelp").hide();
        $(".toggledate").hide();
    </script>
<script>
        $('#tabs').tabs();
</script>

</form>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>