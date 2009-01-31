<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail01" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "" +
"        <font class=\"pagetitlefont\">"+((ResearcherTwitaskDetail01) Pagez.getBeanMgr().get("ResearcherTwitaskDetail01")).getQuestion()+"</font>\n" +
"        <br clear=\"all\"/>";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetail01 researcherTwitaskDetail01= (ResearcherTwitaskDetail01)Pagez.getBeanMgr().get("ResearcherTwitaskDetail01");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {
            if (researcherTwitaskDetail01.getTwitask().getStatus()>Twitask.STATUS_DRAFT){
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_04.jsp?twitaskid="+ researcherTwitaskDetail01.getTwitask().getTwitaskid());
                return;
            }
            researcherTwitaskDetail01.setQuestion(Textbox.getValueFromRequest("question", "Question", true, DatatypeString.DATATYPEID));
            researcherTwitaskDetail01.setStartdate(DateTime.getValueFromRequest("startdate", "Start Date", true).getTime());
            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                researcherTwitaskDetail01.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_04.jsp?twitaskid="+ researcherTwitaskDetail01.getTwitask().getTwitaskid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                researcherTwitaskDetail01.save();
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                researcherTwitaskDetail01.save();
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+ researcherTwitaskDetail01.getTwitask().getTwitaskid());
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>

<%@ include file="/template/header.jsp" %>


<form action="/researcher/researchertwitaskdetail_01.jsp" method="post" id="rsdform">
    <input type="hidden" name="dpage" value="/researcher/researchertwitaskdetail_01.jsp">
    <input type="hidden" name="action" value="next" id="action">
    <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail01.getTwitask().getTwitaskid()%>"/>


        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        On this page you set the very general parameters for the conversation.  Choose a title that'll get people interested.  Give enough information in the description for them to understand what you're trying to understand.  Your start date must be today or in the future.  You want to keep the conversation open long enough to attract bloggers and have them join the conversation... a month is a good starting point.
        </font></div></center>

        <br/><br/>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">

                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Conversation Title</font>
                            </td>
                            <td valign="top">
                                <%if (researcherTwitaskDetail01.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                                    <%=Textbox.getHtml("title", researcherTwitaskDetail01.getQuestion(), 255, 50, "", "")%>
                                <%} else {%>
                                    <font class="smallfont"><%=researcherTwitaskDetail01.getQuestion()%></font>
                                <%}%>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Start Date</font>
                            </td>
                            <td valign="top">
                                <%if (researcherTwitaskDetail01.getTwitask().getStatus()<=Twitask.STATUS_DRAFT) {%>
                                    <%=DateTime.getHtml("startdate", Time.getCalFromDate(researcherTwitaskDetail01.getStartdate()), "", "")%>
                                <%} else {%>
                                    <font class="normalfont"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(researcherTwitaskDetail01.getStartdate()))%></font>
                                <%}%>
                            </td>
                        </tr>
                    </table>

                </td>
                <td valign="top">
                    <!-- Right Side placeholder -->
                </td>
            </tr>
        </table>
        
        <br/><br/>
        <!-- Start Bottom Nav -->
        <!-- Start Bottom Nav -->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" align="left">
                <!--<input type="submit" class="formsubmitbutton" value="Previous Step" onclick="document.getElementById('action').value='previous';">-->
            </td>
            <td valign="top" align="right">
                <%if (researcherTwitaskDetail01.getTwitask().getStatus()==Twitask.STATUS_DRAFT) {%>
                    <input type="submit" class="formsubmitbutton" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">
                <%}%>
                <input type="submit" class="formsubmitbutton" value="Next Step">
            </td>
        </tr>
    </table>
    <!-- End Bottom Nav -->


</form>


<%@ include file="/template/footer.jsp" %>