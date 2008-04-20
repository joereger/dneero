<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanels" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Rankings";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankList researcherRankList =(ResearcherRankList) Pagez.getBeanMgr().get("ResearcherRankList");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("newrank")) {
        try {
            researcherRankList.setNewrankname(Textbox.getValueFromRequest("newrankname", "Rank Name", true, DatatypeString.DATATYPEID));
            researcherRankList.newRank();
            Pagez.getUserSession().setMessage("Ranking created.");
            Pagez.sendRedirect("/researcher/rank-detail.jsp?rankid="+researcherRankList.getNewrankid());
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <br/><br/>

    <table cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td valign="top">
                <%if (researcherRankList.getRanks()==null || researcherRankList.getRanks().size()==0){%>
                    <!--<font class="normalfont">No rankings yet.</font>-->
                <%} else {%>
                    <%
                        ArrayList<GridCol> cols=new ArrayList<GridCol>();
                        cols.add(new GridCol("", "<a href=\"/researcher/rank-detail.jsp?rankid=<$rankid$>\"><$name$></a>", false, "", "mediumfont", "background: #ffffff;", ""));
                    %>
                    <%=Grid.render(researcherRankList.getRanks(), cols, 50, "/researcher/rank-list.jsp", "page")%>
                <%}%>
                <br/><br/>
            </td>
            <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/rank-list.jsp" method="post">
                        <input type="hidden" name="dpage" value="/researcher/rank-list.jsp">
                        <input type="hidden" name="action" value="newrank">

                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td valign="top">
                                    <font class="tinyfont">Enter a new ranking name:</font><br/>
                                    <%=Textbox.getHtml("newrankname", researcherRankList.getNewrankname(), 255, 25, "", "")%>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <input type="submit" class="formsubmitbutton" value="Create a New Ranking">
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">What are Rankings?</font>
                    <br/>
                    <font class="smallfont">Rankings allow you to choose certain questions as indicators of a quality that you want to track.  You can use Rankings across surveys.  When a person answers a question in a certain way they score ranking points.</font>
                </div>
            </td>
        </tr>
    </table>







<%@ include file="/template/footer.jsp" %>

