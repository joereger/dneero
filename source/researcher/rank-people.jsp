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
Rank rank = null;
if (Pagez.getRequest().getParameter("rankid")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankid"))){
    rank = Rank.get(Integer.parseInt(Pagez.getRequest().getParameter("rankid")));
}
if (rank==null){
    Pagez.sendRedirect("/researcher/rank-list.jsp");
    return;
}
%>
<%@ include file="/template/header.jsp" %>

<font class="mediumfont"><%=rank.getName()%></font>
<br/><br/>



    <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top">

                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <%
                    ArrayList<Object[]> stats = (ArrayList<Object[]>)HibernateUtil.getSession().createQuery("select userid, sum(points) as summ, avg(normalizedpoints) from Rankuser where rankid='"+rank.getRankid()+"' group by userid").list();
                    
                    for (Iterator iterator = stats.iterator(); iterator.hasNext();) {
                        Object[] res = (Object[])iterator.next();
                        
                        int userid = (Integer)res[0];
                        long points = (Long)res[1];
                        Double avgnormalizedpoints = (Double)res[2];
                        User user = User.get(userid);
                        Double avgDbl = Double.parseDouble(String.valueOf(avgnormalizedpoints*100));
                        %>
                        <tr>
                            <td valign="top">
                                <font class="tinyfont"><%=user.getFirstname()%> <%=user.getLastname()%></font>
                            </td>
                            <td valign="top">
                                <font class="tinyfont"><%=points%> points</font>
                            </td>
                            <td valign="top">
                                <font class="tinyfont"><%=avgDbl.intValue()%>/100 Rating Strength</font>
                            </td>
                        </tr>
                        <%
                    }
                %>
                </table>


            </td>
            <td valign="top" width="33%">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="normalfont">These are the people who are ranked.</font>
                </div>

            </td>
        </tr>
    </table>





<%@ include file="/template/footer.jsp" %>

