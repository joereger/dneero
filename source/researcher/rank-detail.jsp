<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanels" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Custom Rankings";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankDetail researcherRankDetail =(ResearcherRankDetail) Pagez.getBeanMgr().get("ResearcherRankDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("dosomething")) {
        try {
            researcherRankDetail.saveAction();
            Pagez.getUserSession().setMessage("Panel deleted.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


 <br/><br/>

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top">
                <%if (researcherRankDetail.getRdlis()==null || researcherRankDetail.getRdlis().size()==0){%>
                    <font class="normalfont">No questions have been added to this ranking yet.</font>
                <%} else {%>
                    <%
                        ArrayList<GridCol> cols=new ArrayList<GridCol>();
                        cols.add(new GridCol("Question", "<$question.question$>", false, "", "normalfont"));
                        cols.add(new GridCol("From Survey", "<$survey.title$>", false, "", "normalfont"));
                    %>
                    <%=Grid.render(researcherRankDetail.getRdlis(), cols, 50, "/researcher/rank-detail.jsp?rankid=", "page")%>
                <%}%>
                <br/><br/>
                <form action="/researcher/rank-addquestion.jsp" method="post">
                    <input type="hidden" name="dpage" value="/researcher/rank-addquestion.jsp">
                    <input type="hidden" name="action" value="addquestion">
                    <input type="hidden" name="rankid" value="<%=researcherRankDetail.getRank().getRankid()%>">
                    <input type="submit" class="formsubmitbutton" value="Add a Question">
                </form>
            </td>
            <td valign="top">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">Ranking Stats</font>
                    <br/>
                    <font class="smallfont">385XXX People Ranked</font>
                </div>
            </td>
        </tr>
    </table>





<%@ include file="/template/footer.jsp" %>

