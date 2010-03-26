<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherSurveyDetail04" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="com.dneero.constants.*" %>
<%
Logger logger=Logger.getLogger(this.getClass().getName());
String pagetitle="" +
        "        <font class=\"pagetitlefont\">" + ((ResearcherTwitaskDetail04) Pagez.getBeanMgr().get("ResearcherTwitaskDetail04")).getTitle() + "</font>\n" +
        "        <br clear=\"all\"/>";
String navtab="researchers";
String acl="researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherTwitaskDetail02 researcherTwitaskDetail02 = (ResearcherTwitaskDetail02)Pagez.getBeanMgr().get("ResearcherTwitaskDetail02");
%>
<%
    if (request.getParameter("action") != null && (request.getParameter("action").equals("next") || request.getParameter("action").equals("saveasdraft") || request.getParameter("action").equals("previous"))) {
        try {

            if (request.getParameter("action").equals("next")) {
                logger.debug("Next was clicked");
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_04.jsp?twitaskid="+ researcherTwitaskDetail02.getTwitask().getTwitaskid());
                return;
            } else if (request.getParameter("action").equals("saveasdraft")) {
                logger.debug("Saveasdraft was clicked");
                Pagez.getUserSession().setMessage("Your conversation has been saved.");
                Pagez.sendRedirect("/researcher/index.jsp");
                return;
            } else if (request.getParameter("action").equals("previous")) {
                logger.debug("Previous was clicked");
                Pagez.sendRedirect("/researcher/researchertwitaskdetail_01.jsp?twitaskid="+ researcherTwitaskDetail02.getTwitask().getTwitaskid()+"&ispreviousclick=1");
                return;
            }
        } catch (Exception ex) {
            Pagez.getUserSession().setMessage("Sorry, there's been an error, please try again.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>




        <%--<a href="/twitterredirector?twitaskid=<%=researcherTwitaskDetail02.getTwitask().getTwitaskid()%>">Please Click Here to Authorize Twitter</a><br/>--%>
        <center>
        <br/><br/>
        <div style="width: 350px;">
            <form action="/twitterredirector" method="get">
                <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail02.getTwitask().getTwitaskid()%>">
                <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Authorize Twitter">
            </form>
            <br/>
            Before you can continue you must tell Twitter that you authorize this application to post to your account.  Choose the account that you want us to post questions to and collect answers from.
        </div>
        </center>


<form action="/researcher/researchertwitaskdetail_02.jsp" method="post"  class="niceform" id="rsdform">
        <input type="hidden" name="dpage" value="/researcher/researchertwitaskdetail_02.jsp">
        <input type="hidden" name="action" value="next" id="action">
        <input type="hidden" name="twitaskid" value="<%=researcherTwitaskDetail02.getTwitask().getTwitaskid()%>"/>


            <br/><br/><br/><br/>
            <!-- Start Bottom Nav -->
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td valign="top" align="left">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Previous Step" onclick="document.getElementById('action').value='previous';">
                    </td>
                    <td valign="top" align="right">
                        <%if (researcherTwitaskDetail02.getTwitask().getStatus()==Twitask.STATUS_DRAFT) {%>
                            <%--<input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save and Continue Later" onclick="document.getElementById('action').value='saveasdraft';">--%>
                        <%}%>
                        <%--<input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Next Step">--%>
                    </td>
                </tr>
            </table>
            <!-- End Bottom Nav -->
    </form>



<%@ include file="/template/footer.jsp" %>