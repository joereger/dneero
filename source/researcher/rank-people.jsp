<%@ page import="com.dneero.dao.Question" %>
<%@ page import="com.dneero.dao.Rank" %>
<%@ page import="com.dneero.dao.Survey" %>
<%@ page import="com.dneero.dao.User" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.Textbox" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Ranking";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    ResearcherRankPeople researcherRankPeople =(ResearcherRankPeople) Pagez.getBeanMgr().get("ResearcherRankPeople");
%>
<%
if (researcherRankPeople.getRank()==null){
    Pagez.sendRedirect("/researcher/rank-list.jsp");
    return;
}
%>
<%@ include file="/template/header.jsp" %>

<font class="mediumfont"><%=researcherRankPeople.getRank().getName()%></font>
<br/><br/>



    <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top">

                

                <%if (researcherRankPeople.getRrplis()==null || researcherRankPeople.getRrplis().size()==0){%>
                    <font class="normalfont">Nobody is ranked yet.  They become ranked when you create a question and they answer it in a way that you define as awarding them points.</font>
                <%} else {%>
                    <%
                        ArrayList<GridCol> cols=new ArrayList<GridCol>();
                        cols.add(new GridCol("Name", "<a href=\"/profile.jsp?userid=<$userid$>\"><$name$></a>", false, "", "tinyfont", "", ""));
                        cols.add(new GridCol("Points", "<$points$> points", false, "", "tinyfont", "", ""));
                        cols.add(new GridCol("", "<$avgnormalizedpointsStr$>", false, "", "tinyfont", "", ""));
                    %>
                    <%=Grid.render(researcherRankPeople.getRrplis(), cols, 50, "/researcher/rank-people.jsp?rankid="+researcherRankPeople.getRank().getRankid(), "page")%>
                <%}%>


            </td>
            <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">Ranked People</font>
                    <br/>
                    <font class="smallfont">These are the people who are ranked. You can see their ranking along with a Ranking Strength.  Ranking Strength is the average of normalized points awarded for each question.  This is done because there's a difference between building many points over time with lowly Ranking-correlated answerd and quickly building many points with a few highly-Ranking-correlated answers.  In short, people with a higher Ranking Strength are more highly correlated to the qualities that you're measuring with your Ranking.</font>
                </div>
                <br/>
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <form action="/researcher/panels-addpeople.jsp" method="get">
                        <input type="hidden" name="dpage" value="/researcher/panels-addpeople.jsp">
                        <input type="hidden" name="showonly" value="addbyrankingpercent">
                        <input type="hidden" name="rankid" value="<%=researcherRankPeople.getRank().getRankid()%>">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Add People to Panel">
                    </form>
                    <font class="tinyfont">Add all or some or these people to Panels of respondents.  On the next screen you can apply filters to only add the top 90%, for example.</font>
                </div>
            </td>
        </tr>
    </table>





<%@ include file="/template/footer.jsp" %>

