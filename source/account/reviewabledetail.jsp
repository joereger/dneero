<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.review.Reviewable" %>
<%@ page import="com.dneero.review.ReviewableFactory" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Flagged Content";
String navtab = "youraccount";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
    Review review = null;
    if (Num.isinteger(Pagez.getRequest().getParameter("reviewid"))){
        review = Review.get(Integer.parseInt(Pagez.getRequest().getParameter("reviewid")));
    }
    if (review==null){
        Pagez.sendRedirect("/account/reviewables.jsp");
        Pagez.getUserSession().setMessage("That flagged content was not found.");
        return;
    }
    Reviewable reviewable = null;
    if (review.getId()>0 && review.getType()>0){
        reviewable = ReviewableFactory.get(review.getId(), review.getType());
    }
%>
<%@ include file="/template/header.jsp" %>
    <br/><br/>

    <font class="mediumfont"><%=reviewable.getShortSummary()%></font><br/>

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">Conversation Igniter</font><br/>
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <%
                        String ciStatus = "Not Yet Reviewed";
                        if (review.getIsresearcherreviewed()){
                            ciStatus = "Reviewed";
                            if (review.getIsresearcherwarned()){
                                ciStatus = "Warning";
                            }
                            if (review.getIsresearcherrejected()){
                                ciStatus = "Flagged Bad Content";
                            }
                        }
                        %>
                        <center><font class="mediumfont" style="color: #cccccc;"><%=ciStatus%></font></center>
                    </div>
                    <%if (review.getResearchernotes().length()>0){%>
                        <font class="normalfont" style="font-weight: bold;">Conversation Igniter Comments:</font><br/>
                        <font class="tinyfont"><%=review.getResearchernotes()%></font><br/>
                    <%}%>
                </div>
            </td>
            <td valign="top">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <font class="mediumfont">dPolice (The dNeero Content Police)</font><br/>
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <%
                        String dpStatus = "Not Yet Reviewed";
                        if (review.getIssysadminreviewed()){
                            dpStatus = "Reviewed";
                            if (review.getIsresearcherwarned()){
                                dpStatus = "Warning";
                            }
                            if (review.getIssysadminrejected()){
                                dpStatus = "Flagged Bad Content";
                            }
                        }
                        %>
                        <center><font class="mediumfont" style="color: #cccccc;"><%=dpStatus%></font></center>
                    </div>
                    <%if (review.getResearchernotes().length()>0){%>
                        <font class="normalfont" style="font-weight: bold;">dPolice Comments:</font><br/>
                        <font class="tinyfont"><%=review.getSysadminnotes()%></font><br/>
                    <%}%>
                </div>
            </td>
        </tr>
    </table>

    <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
        <font class="mediumfont" style="font-weight: bold;">General description of this sort of issue:</font><br/>
        <%=reviewable.getTypeDescription()%>
    </div>

    <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
        <font class="mediumfont" style="font-weight: bold;">Detail of Content in Question:</font><br/>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
            <%=reviewable.getFullSummary()%>
        </div>
    </div>




<%@ include file="/template/footer.jsp" %>


