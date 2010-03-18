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
                        cols.add(new GridCol("", "<img src=\"/images/activity_32.gif\" align=\"middle\" border=\"0\" width=\"32\" height=\"32\"><a href=\"/researcher/rank-detail.jsp?rankid=<$rankid$>\"><$name$></a>", false, "", "mediumfont", "background: #ffffff;", ""));
                    %>
                    <%=Grid.render(researcherRankList.getRanks(), cols, 50, "/researcher/rank-list.jsp", "page")%>
                <%}%>
                <br/><br/>
            </td>
            <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/rank-list.jsp" method="post" class="niceform">
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
                                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Create a New Ranking">
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">What are Rankings?</font>
                    <br/>
                    <font class="smallfont">
                        Rankings allow you to choose certain questions as indicators of a quality that you want to track.  For example, you could create an "Environmentally Friendly" Ranking and when people answer the question "Do you care about the environment?" with a "Yes" you assign them 50 points.
                        <br/><br/>You can use Rankings to measure across many conversations.  In this way you get a sense of how a person responds over time to many different things.
                        <br/><br/>As people score points you can skim those in the top 10% of Ranking score and put them into a Panel which allows you to create conversations that only they can see and take.  In this way you're not only targeting but engaging your audience.
                    </font>
                </div>
            </td>
        </tr>
    </table>







<%@ include file="/template/footer.jsp" %>

