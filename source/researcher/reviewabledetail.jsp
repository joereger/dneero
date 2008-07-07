<%@ page import="com.dneero.dao.Review" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.Textarea" %>
<%@ page import="com.dneero.htmluibeans.CustomercareReviewDetail" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareReviewDetail customercareReviewDetail=(CustomercareReviewDetail) Pagez.getBeanMgr().get("CustomercareReviewDetail");
%>
<%
    //Create a base review with default values
    Review review = new Review();
    review.setDateofcreation(new java.util.Date());
    review.setDatelastupdated(new java.util.Date());
    review.setId(customercareReviewDetail.getReviewable().getId());
    review.setType(customercareReviewDetail.getReviewable().getType());
    review.setIsresearcherrejected(false);
    review.setIsresearcherreviewed(false);
    review.setIsresearcherwarned(false);
    review.setIssysadminrejected(false);
    review.setIssysadminreviewed(false);
    review.setIssysadminwarned(false);
    review.setResearchernotes("");
    review.setSysadminnotes("");
    review.setUseridofcontentcreator(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
    review.setUseridofresearcher(customercareReviewDetail.getReviewable().getUseridofresearcher());
    //Load any existing review on top
    if (customercareReviewDetail.getReviewable()!=null){
        List<Review> researcherreviews = HibernateUtil.getSession().createCriteria(Review.class)
                            .add(Restrictions.eq("type", customercareReviewDetail.getReviewable().getType()))
                            .add(Restrictions.eq("id", customercareReviewDetail.getReviewable().getId()))
                           .setCacheable(true)
                           .list();
        for (Iterator<Review> reviewIterator=researcherreviews.iterator(); reviewIterator.hasNext();) {
            review=reviewIterator.next();
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("reject")) {
        try {
            //Record notes
            //Record notes
            if (review!=null){
                String notes = Textarea.getValueFromRequest("researchernotes", "Notes", true);
                review.setDatelastupdated(new java.util.Date());
                review.setId(customercareReviewDetail.getReviewable().getId());
                review.setType(customercareReviewDetail.getReviewable().getType());
                review.setIsresearcherrejected(true);
                review.setIsresearcherreviewed(true);
                review.setIsresearcherwarned(false);
                review.setIssysadminreviewed(false);
                review.setResearchernotes(notes);
                review.setUseridofcontentcreator(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
                review.setUseridofresearcher(customercareReviewDetail.getReviewable().getUseridofresearcher());
                try{review.save();}catch(Exception ex){logger.error("", ex);}
            }
            //Change the underlying Reviewable object
            customercareReviewDetail.getReviewable().rejectByResearcher();
            Pagez.sendRedirect("/researcher/reviewables.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("approve")) {
        try {
            //Record notes... note that this does a specific check to only update a Review object if one already exists... otherwise I don't want to create a new Review for all approvals
            if (review!=null && review.getReviewid()>0){
                String notes = Textarea.getValueFromRequest("researchernotes", "Notes", false);
                review.setDatelastupdated(new java.util.Date());
                review.setId(customercareReviewDetail.getReviewable().getId());
                review.setType(customercareReviewDetail.getReviewable().getType());
                review.setIsresearcherrejected(false);
                review.setIsresearcherreviewed(true);
                review.setIsresearcherwarned(false);
                review.setIssysadminreviewed(false);
                review.setResearchernotes(notes);
                review.setUseridofcontentcreator(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
                review.setUseridofresearcher(customercareReviewDetail.getReviewable().getUseridofresearcher());
                try{review.save();}catch(Exception ex){logger.error("", ex);}
            }
            //Change the underlying Reviewable object
            customercareReviewDetail.getReviewable().approveByResearcher();
            Pagez.sendRedirect("/researcher/reviewables.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("warn")) {
        try {
            //Record notes
            if (review!=null){
                String notes = Textarea.getValueFromRequest("researchernotes", "Notes", true);
                review.setDatelastupdated(new java.util.Date());
                review.setId(customercareReviewDetail.getReviewable().getId());
                review.setType(customercareReviewDetail.getReviewable().getType());
                review.setIsresearcherrejected(false);
                review.setIsresearcherreviewed(true);
                review.setIsresearcherwarned(true);
                review.setIssysadminreviewed(false);
                review.setResearchernotes(notes);
                review.setUseridofcontentcreator(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
                review.setUseridofresearcher(customercareReviewDetail.getReviewable().getUseridofresearcher());
                try{review.save();}catch(Exception ex){logger.error("", ex);}
            }
            //Change the underlying Reviewable object
            customercareReviewDetail.getReviewable().approveByResearcher();
            Pagez.sendRedirect("/researcher/reviewables.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/researcher/reviewabledetail.jsp" method="post">
    <input type="hidden" name="dpage" value="/researcher/reviewabledetail.jsp">
    <input type="hidden" id="action" name="action" value="approve">
    <input type="hidden" name="id" value="<%=customercareReviewDetail.getReviewable().getId()%>">
    <input type="hidden" name="type" value="<%=customercareReviewDetail.getReviewable().getType()%>">

    <div class="rounded" style="padding: 10px; margin: 10px; background: #e6e6e6;">
       <table cellpadding="0" cellspacing="0" border="0">
           <tr>
               <td valign="top" colspan="2">
                    <input type="submit" class="formsubmitbutton" value="Approve" onclick="document.getElementById('action').value='approve';">
                    <input type="submit" class="formsubmitbutton" value="Reject" onclick="document.getElementById('action').value='reject';">
                    <input type="submit" class="formsubmitbutton" value="Warn" onclick="document.getElementById('action').value='warn';">
               </td>
               <td valign="top" rowspan="2">
                    <div class="rounded" style="padding: 10px; margin: 10px; background: #ffffff;">
                        <font class="mediumfont">There are only a few reasons to reject or warn:</font>
                        <font class="smallfont" style="font-weight: bold;">
                            <ul>
                                <li>Illegal content</li>
                                <li>Copyright violations</li>
                                <li>Abusive posts based on race, religion, gender, sexual orientation or other personal attributes</li>
                                <li>Spam or junk postings</li>
                                <li>Threats of physical abuse</li>
                                <li>Other obviously inappropriate material</li>
                            </ul>
                        </font>
                        <font class="smallfont">You can't use this feature to censor content you don't like.  dNeero will only reject content when it it obviously inappropriate.  dNeero will generally side with content creators when grey areas arise.</font>
                    </div>
               </td>
           </tr>
           <tr>
               <td valign="top">
                   <font class="smallfont" style="font-weight: bold;">Notes (Only Required for Reject and Warn)</font><br/>
                   <%=Textarea.getHtml("researchernotes", review.getResearchernotes(), 3, 65, "", "font-size: 9px;")%>
                   <br/><font class="tinyfont" style="font-weight: bold;">Notes will be seen by customer care and users.  You need to explain and justify your Rejection or Warning action.</font>
               </td>

           </tr>
       </table>
    </div>

    <%if (review.getSysadminnotes()!=null && !review.getSysadminnotes().equals("")){%>
        <div class="rounded" style="padding: 10px; margin: 10px; background: #e6e6e6;">
           <table cellpadding="0" cellspacing="0" border="0">
               <tr>
                   <td valign="top">
                       <font class="smallfont" style="font-weight: bold;">Customer Care Notes (Read Only)</font><br/>
                       <%=Textarea.getHtml("notes", review.getSysadminnotes(), 3, 65, "", "font-size: 9px;")%>
                   </td>
               </tr>
           </table>
        </div>
    <%}%>

   
   <div class="rounded" style="padding: 5px; margin: 10px; background: #e6e6e6;">
   <div class="rounded" style="padding: 5px; margin: 10px; background: #ffffff;">
   <div style="width: 670px; overflow: auto;">
       <%=customercareReviewDetail.getReviewable().getFullSummary()%>   
   </div>
   </div>
   </div>

</form>




<%@ include file="/template/footer.jsp" %>



