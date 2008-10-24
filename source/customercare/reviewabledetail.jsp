<%@ page import="com.dneero.dao.Review" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.Textarea" %>
<%@ page import="com.dneero.htmluibeans.CustomercareReviewDetail" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dneero.email.EmailTemplateProcessor" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "";
String navtab = "customercare";
String acl = "customercare";
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
            if (review!=null){
                String notes = Textarea.getValueFromRequest("notes", "Customer Care Notes", true);
                review.setDatelastupdated(new java.util.Date());
                review.setId(customercareReviewDetail.getReviewable().getId());
                review.setType(customercareReviewDetail.getReviewable().getType());
                review.setIssysadminrejected(true);
                review.setIssysadminreviewed(true);
                review.setIssysadminwarned(false);
                review.setSysadminnotes(notes);
                review.setUseridofcontentcreator(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
                review.setUseridofresearcher(customercareReviewDetail.getReviewable().getUseridofresearcher());
                try{review.save();}catch(Exception ex){logger.error("", ex);}
            }
            //Change the underlying Reviewable object
            customercareReviewDetail.getReviewable().rejectBySysadmin();
            //Create the args array to hold the dynamic stuff
            String[] args = new String[10];
            args[0] = customercareReviewDetail.getReviewable().getShortSummary();
            args[1] = review.getResearchernotes();
            args[2] = review.getSysadminnotes();
            //Send the email
            User userWhoCreatedContent = User.get(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
            EmailTemplateProcessor.sendMail("Content Flagging: Rejected Content", "reviewable-rejection", userWhoCreatedContent, args);
            //Redir
            Pagez.sendRedirect("/customercare/reviewables.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("approve")) {
        try {
            //Record notes
            if (review!=null){
                String notes = Textarea.getValueFromRequest("notes", "Customer Care Notes", true);
                review.setDatelastupdated(new java.util.Date());
                review.setId(customercareReviewDetail.getReviewable().getId());
                review.setType(customercareReviewDetail.getReviewable().getType());
                review.setIssysadminrejected(false);
                review.setIssysadminreviewed(true);
                review.setIssysadminwarned(false);
                review.setSysadminnotes(notes);
                review.setUseridofcontentcreator(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
                review.setUseridofresearcher(customercareReviewDetail.getReviewable().getUseridofresearcher());
                try{review.save();}catch(Exception ex){logger.error("", ex);}
            }
            //Change the underlying Reviewable object
            customercareReviewDetail.getReviewable().approveBySysadmin();
            Pagez.sendRedirect("/customercare/reviewables.jsp");
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
                String notes = Textarea.getValueFromRequest("notes", "Customer Care Notes", true);
                review.setDatelastupdated(new java.util.Date());
                review.setId(customercareReviewDetail.getReviewable().getId());
                review.setType(customercareReviewDetail.getReviewable().getType());
                review.setIssysadminrejected(false);
                review.setIssysadminreviewed(true);
                review.setIssysadminwarned(true);
                review.setSysadminnotes(notes);
                review.setUseridofcontentcreator(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
                review.setUseridofresearcher(customercareReviewDetail.getReviewable().getUseridofresearcher());
                try{review.save();}catch(Exception ex){logger.error("", ex);}
            }
            //Change the underlying Reviewable object
            customercareReviewDetail.getReviewable().approveBySysadmin();
            //Create the args array to hold the dynamic stuff
            String[] args = new String[10];
            args[0] = customercareReviewDetail.getReviewable().getShortSummary();
            args[1] = review.getResearchernotes();
            args[2] = review.getSysadminnotes();
            //Send the email
            User userWhoCreatedContent = User.get(customercareReviewDetail.getReviewable().getUseridofcontentcreator());
            EmailTemplateProcessor.sendMail("Content Flagging: Warning", "reviewable-warning", userWhoCreatedContent, args);
            //Redir
            Pagez.sendRedirect("/customercare/reviewables.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


<form action="/customercare/reviewabledetail.jsp" method="post">
    <input type="hidden" name="dpage" value="/customercare/reviewabledetail.jsp">
    <input type="hidden" id="action" name="action" value="approve">
    <input type="hidden" name="id" value="<%=customercareReviewDetail.getReviewable().getId()%>">
    <input type="hidden" name="type" value="<%=customercareReviewDetail.getReviewable().getType()%>">

    <div class="rounded" style="padding: 10px; margin: 10px; background: #e6e6e6;">
       <table cellpadding="0" cellspacing="0" border="0">
           <tr>
               <td valign="top" colspan="2">
                    <input type="submit" class="formsubmitbutton" value="Content is Approved" onclick="document.getElementById('action').value='approve';">
                    <input type="submit" class="formsubmitbutton" value="Content is Rejected" onclick="document.getElementById('action').value='reject';">
                    <input type="submit" class="formsubmitbutton" value="Issue Warning" onclick="document.getElementById('action').value='warn';">
               </td>
           </tr>
           <tr>
               <td valign="top">
                   <font class="smallfont" style="font-weight: bold;">Customer Care Notes</font><br/>
                   <%=Textarea.getHtml("notes", review.getSysadminnotes(), 3, 65, "", "font-size: 9px;")%>
                   <br/><font class="tinyfont" style="font-weight: bold;">Notes will be seen by conversation igniters and users.</font>
               </td>
               <td valign="top">
                   <font class="smallfont" style="font-weight: bold;">Researcher Notes (Read Only)</font><br/>
                   <%=Textarea.getHtml("researchernotes", review.getResearchernotes(), 3, 65, "", "font-size: 9px;")%>
               </td>
           </tr>
       </table>
    </div>

   <br/><br/>
   <div style="width: 750px; overflow: auto;">
       <%=customercareReviewDetail.getReviewable().getFullSummary()%>   
   </div>



</form>




<%@ include file="/template/footer.jsp" %>


