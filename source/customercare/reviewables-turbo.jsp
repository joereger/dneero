<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSurveyList" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSurveyList" %>
<%@ page import="com.dneero.review.ReviewableUtil" %>
<%@ page import="com.dneero.review.Reviewable" %>
<%@ page import="com.dneero.mail.MailtypeReviewableRejection" %>
<%@ page import="com.dneero.mail.MailNotify" %>
<%@ page import="com.dneero.mail.MailtypeReviewableWarning" %>
<%@ page import="com.dneero.review.ReviewableFactory" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reviewable Items";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
int numOnPage = 25;
if (Num.isinteger(request.getParameter("numOnPage"))){
    numOnPage = Integer.parseInt(request.getParameter("numOnPage"));
}

%>
<%
    ArrayList<Reviewable> reviewables = new ArrayList<Reviewable>();
    int id = 0;
    if (com.dneero.util.Num.isinteger(request.getParameter("id"))){
        id = Integer.parseInt(request.getParameter("id"));
    }
    int type = 0;
    if (com.dneero.util.Num.isinteger(request.getParameter("type"))){
        type = Integer.parseInt(request.getParameter("type"));
    }
    if (id>0 && type>0){
        Reviewable reviewable = ReviewableFactory.get(id, type);
        if (reviewable!=null){
            reviewables.add(reviewable);
        }
    } else {
        reviewables = ReviewableUtil.getPendingForSysadminSorted();
    }
%>
<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("dostuff")){
    //Run through *all* reviewables, checking to see if any incoming data matches
    //Must do *all* because if we only did first numOnPage there is a time lag and some items could be lost
    //Wait for it...
    //....
    //..now you feel me.
    for (Iterator<Reviewable> ri=reviewables.iterator(); ri.hasNext();) {
        Reviewable reviewable=ri.next();
        //Create prefix
        String prefix = "r-"+reviewable.getId()+"-"+reviewable.getType()+"-";
        //Get notes, if available
        String notes = "";
        if (request.getParameter(prefix+"notes")!=null){
            notes = request.getParameter(prefix+"notes");
        }
        //See if we have an incoming vote
        if (request.getParameter(prefix+"radios")!=null){
            //Set up a review object
            //Create a base review with default values
            Review review = new Review();
            review.setDateofcreation(new java.util.Date());
            review.setDatelastupdated(new java.util.Date());
            review.setId(reviewable.getId());
            review.setType(reviewable.getType());
            review.setIsresearcherrejected(false);
            review.setIsresearcherreviewed(false);
            review.setIsresearcherwarned(false);
            review.setIssysadminrejected(false);
            review.setIssysadminreviewed(false);
            review.setIssysadminwarned(false);
            review.setResearchernotes("");
            review.setSysadminnotes("");
            review.setUseridofcontentcreator(reviewable.getUseridofcontentcreator());
            review.setUseridofresearcher(reviewable.getUseridofresearcher());
            //Load any existing review on top
            if (reviewable!=null){
                List<Review> researcherreviews = HibernateUtil.getSession().createCriteria(Review.class)
                                    .add(Restrictions.eq("type", reviewable.getType()))
                                    .add(Restrictions.eq("id", reviewable.getId()))
                                   .setCacheable(true)
                                   .list();
                for (Iterator<Review> reviewIterator=researcherreviews.iterator(); reviewIterator.hasNext();) {
                    review=reviewIterator.next();
                }
            }
            //Get down to processing
            if (request.getParameter(prefix+"radios").equals("approve")){
                //Approve Start
                //Record notes... note that this does a specific check to only update a Review object if one already exists... otherwise I don't want to create a new Review for all approvals
                if (review!=null && review.getReviewid()>0){
                    review.setDatelastupdated(new java.util.Date());
                    review.setId(reviewable.getId());
                    review.setType(reviewable.getType());
                    //review.setIsresearcherrejected(false);
                    //review.setIsresearcherreviewed(true);
                    //review.setIsresearcherwarned(false);
                    review.setIssysadminreviewed(true);
                    review.setIssysadminrejected(false);
                    review.setIssysadminwarned(false);
                    review.setSysadminnotes(notes);
                    review.setUseridofcontentcreator(reviewable.getUseridofcontentcreator());
                    review.setUseridofresearcher(reviewable.getUseridofresearcher());
                    try{review.save();}catch(Exception ex){logger.error("", ex);}
                }
                //Change the underlying Reviewable object
                reviewable.approveBySysadmin();
                //Approve End
            } else if (request.getParameter(prefix+"radios").equals("reject")) {
                //Reject Start
                if (notes!=null && notes.length()>0){
                    if (review!=null){
                        review.setDatelastupdated(new java.util.Date());
                        review.setId(reviewable.getId());
                        review.setType(reviewable.getType());
                        //review.setIsresearcherrejected(true);
                        //review.setIsresearcherreviewed(true);
                        //review.setIsresearcherwarned(false);
                        review.setIssysadminreviewed(true);
                        review.setIssysadminrejected(true);
                        review.setIssysadminwarned(false);
                        review.setSysadminnotes(notes);
                        review.setUseridofcontentcreator(reviewable.getUseridofcontentcreator());
                        review.setUseridofresearcher(reviewable.getUseridofresearcher());
                        try{review.save();}catch(Exception ex){logger.error("", ex);}
                    }
                    //Change the underlying Reviewable object
                    reviewable.rejectBySysadmin();
                    //Create an inbox item
                    if (reviewable.isMailCreated()){
                        User userWhoCreatedContent = User.get(reviewable.getUseridofcontentcreator());
                        Mail mail = new Mail();
                        mail.setIsflaggedforcustomercare(false);
                        mail.setSubject("Content Rejection");
                        mail.setDate(new java.util.Date());
                        mail.setUserid(userWhoCreatedContent.getUserid());
                        mail.setIsread(true);
                        try{mail.save();} catch (Exception ex){logger.error("", ex);}
                        Mailchild mailchild = new Mailchild();
                        mailchild.setMailid(mail.getMailid());
                        mailchild.setDate(new java.util.Date());
                        mailchild.setIsfromcustomercare(true);
                        mailchild.setMailtypeid(MailtypeReviewableRejection.TYPEID);
                        mailchild.setVar1(reviewable.getShortSummary());
                        mailchild.setVar2(review.getResearchernotes());
                        mailchild.setVar3(review.getSysadminnotes());
                        mailchild.setVar4(String.valueOf(review.getReviewid()));
                        mailchild.setVar5("");
                        try{mailchild.save();} catch (Exception ex){logger.error("", ex);}
                        MailNotify.notify(mail);
                    }
                }  else {
                    Pagez.getUserSession().setMessage("When you reject you must provide notes so a rejection was ignored.");
                }
                //Reject End
            } else if (request.getParameter(prefix+"radios").equals("warn")) {
                //Warn Start
                if (notes!=null && notes.length()>0){
                    if (review!=null){
                        review.setDatelastupdated(new java.util.Date());
                        review.setId(reviewable.getId());
                        review.setType(reviewable.getType());
                        //review.setIsresearcherrejected(true);
                        //review.setIsresearcherreviewed(true);
                        //review.setIsresearcherwarned(false);
                        review.setIssysadminreviewed(true);
                        review.setIssysadminrejected(false);
                        review.setIssysadminwarned(true);
                        review.setSysadminnotes(notes);
                        review.setUseridofcontentcreator(reviewable.getUseridofcontentcreator());
                        review.setUseridofresearcher(reviewable.getUseridofresearcher());
                        try{review.save();}catch(Exception ex){logger.error("", ex);}
                    }
                    //Change the underlying Reviewable object
                    reviewable.approveBySysadmin();
                    //Create an inbox item
                    User userWhoCreatedContent = User.get(reviewable.getUseridofcontentcreator());
                    Mail mail = new Mail();
                    mail.setIsflaggedforcustomercare(false);
                    mail.setSubject("Content Warning");
                    mail.setDate(new java.util.Date());
                    mail.setUserid(userWhoCreatedContent.getUserid());
                    mail.setIsread(true);
                    try{mail.save();} catch (Exception ex){logger.error("", ex);}
                    Mailchild mailchild = new Mailchild();
                    mailchild.setMailid(mail.getMailid());
                    mailchild.setDate(new java.util.Date());
                    mailchild.setIsfromcustomercare(true);
                    mailchild.setMailtypeid(MailtypeReviewableWarning.TYPEID);
                    mailchild.setVar1(reviewable.getShortSummary());
                    mailchild.setVar2(review.getResearchernotes());
                    mailchild.setVar3(review.getSysadminnotes());
                    mailchild.setVar4(String.valueOf(review.getReviewid()));
                    mailchild.setVar5("");
                    try{mailchild.save();} catch (Exception ex){logger.error("", ex);}
                    MailNotify.notify(mail);
                }  else {
                    Pagez.getUserSession().setMessage("When you warn you must provide notes so a warning was ignored.");
                }
                //Warn End
            } else if (request.getParameter(prefix+"radios").equals("skip")) {
                //Skip Start
                //Do nothing!?!?
                //Skip End
            }
            //Handle Scoring
            if (request.getParameter(prefix+"score")!=null){
                if (Num.isinteger(request.getParameter(prefix+"score"))){
                    int score = Integer.parseInt(request.getParameter(prefix+"score"));
                    if (score>=0 && score<=5){
                        reviewable.scoreBySysadmin(score);
                    }
                }
            }
        }
    }
    Pagez.getUserSession().setMessage("Reviews saved.");
    //Refresh the list
    reviewables = ReviewableUtil.getPendingForSysadminSorted();
}
%>

<%@ include file="/template/header.jsp" %>


    <br/>
    <%if (reviewables==null || reviewables.size()==0){%>
        <font class="normalfont">No new reviewables are waiting.</font>
    <%} else {
        %>

        <form action="/customercare/reviewables-turbo.jsp" method="post">
            <input type="hidden" name="dpage" value="/customercare/reviewables-turbo.jsp">
            <input type="hidden" id="action" name="action" value="dostuff">
            <%if (id>0 && type>0){%>
                <input type="hidden" name="id" value="<%=id%>">
                <input type="hidden" name="type" value="<%=type%>">
            <%}%>

        <font class="mediumfont" style="color: #cccccc;"><%=reviewables.size()%> reviewables remain.</font><br/><br/>

        <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <%
        int count = 0;
        for (Iterator<Reviewable> reviewableIterator=reviewables.iterator(); reviewableIterator.hasNext();) {
            Reviewable reviewable=reviewableIterator.next();
            //Limit display to a certain number of items
            count = count + 1;
            if (count<numOnPage){
                //Load review obj
                Review review = null;
                if (reviewable!=null){
                    List<Review> researcherreviews = HibernateUtil.getSession().createCriteria(Review.class)
                                        .add(Restrictions.eq("type", reviewable.getType()))
                                        .add(Restrictions.eq("id", reviewable.getId()))
                                       .setCacheable(true)
                                       .list();
                    for (Iterator<Review> reviewIterator=researcherreviews.iterator(); reviewIterator.hasNext();) {
                        review=reviewIterator.next();
                    }
                }
                //Prefix
                String prefix = "r-"+reviewable.getId()+"-"+reviewable.getType()+"-";
                %>
                <tr>
                    <td valign="top">
                        <table cellpadding="2" cellspacing="0" border="0">
                            <tr>
                                <td valign="top" colspan="4"><img src="/images/clear.gif" alt="" width="1" height="10"/></td>
                            </tr>
                            <tr>
                                <td valign="top"><center><%if(reviewable.isApproveSupported()){%><input type="radio" name="<%=prefix%>radios" value="approve" checked><%}%></center></td>
                                <td valign="top"><center><%if(reviewable.isRejectSupported()){%><input type="radio" name="<%=prefix%>radios" value="reject"><%}%></center></td>
                                <td valign="top"><center><%if(reviewable.isWarnSupported()){%><input type="radio" name="<%=prefix%>radios" value="warn"><%}%></center></td>
                                <td valign="top"><center><input type="radio" name="<%=prefix%>radios" value="skip"></center></td>
                            </tr>
                            <tr>
                                <td valign="top"><center><font class="tinyfont">Approve</font></center></td>
                                <td valign="top"><center><font class="tinyfont">Reject</font></center></td>
                                <td valign="top"><center><font class="tinyfont">Warn</font></center></td>
                                <td valign="top"><center><font class="tinyfont">Skip</font></center></td>
                            </tr>
                            <tr>
                                <td valign="top" colspan="4">
                                    <%
                                    String researcherNotes = "";
                                    if (review!=null){
                                        researcherNotes = review.getResearchernotes();
                                    }
                                    %>
                                    <center><%=Textarea.getHtml(prefix+"researchernotes", researcherNotes, 2, 25, "", "font-size: 9px; width: 90%; background: #e6e6e6;")%></center>
                                    <center><%=Textarea.getHtml(prefix+"notes", "", 2, 25, "", "font-size: 9px; width: 90%;")%></center>
                                    <font class="tinyfont" style="font-weight: bold;">Notes only for Reject and Warn.</font>
                                    <font class="tinyfont" style="font-weight: normal;">Explain/justify your action in bottom box.</font>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td valign="top">
                        <div class="rounded" style="padding: 5px; margin: 10px; background: #e6e6e6;">

                           <%--<%=Textarea.getHtml(prefix+"notes", "", 1, 75, "", "font-size: 9px; width: 90%;")%>--%>
                            <%--<br/><font class="smallfont" style="font-weight: bold;">Notes (Only Required for Reject and Warn)</font><br/>--%>
                            <%--<font class="tinyfont" style="font-weight: bold;">Will be seen by customer care and users.  You need to explain and justify your Rejection or Warning action.</font><br/>--%>

                           <div class="rounded" style="padding: 5px; margin: 10px; background: #ffffff;">
                               <div style="width: 550px; overflow: auto;">
                                   <%if (reviewable.supportsScoringBySysadmin()){%>
                                        <table cellpadding="2" cellspacing="0" border="0">
                                            <tr>
                                                <td valign="top"><center><input type="radio" name="<%=prefix%>score" value="0" checked></center></td>
                                                <td valign="top"><center><input type="radio" name="<%=prefix%>score" value="1"></center></td>
                                                <td valign="top"><center><input type="radio" name="<%=prefix%>score" value="2"></center></td>
                                                <td valign="top"><center><input type="radio" name="<%=prefix%>score" value="3"></center></td>
                                                <td valign="top"><center><input type="radio" name="<%=prefix%>score" value="4"></center></td>
                                                <td valign="top"><center><input type="radio" name="<%=prefix%>score" value="5"></center></td>
                                            </tr>
                                            <tr>
                                                <td valign="top"><center><font class="tinyfont">Not Scored</font></center></td>
                                                <td valign="top"><center><font class="tinyfont">Horrible</font></center></td>
                                                <td valign="top"><center><font class="tinyfont">Pretty Bad</font></center></td>
                                                <td valign="top"><center><font class="tinyfont">Average</font></center></td>
                                                <td valign="top"><center><font class="tinyfont">Pretty Good</font></center></td>
                                                <td valign="top"><center><font class="tinyfont">Great</font></center></td>
                                            </tr>
                                        </table>
                                        <br/><br/>
                                   <%}%>
                                   <%=reviewable.getFullSummary()%>
                                   <br/>
                                   <a href="/customercare/reviewables-turbo.jsp?type=<%=reviewable.getType()%>&id=<%=reviewable.getId()%>"><font class="tinyfont">Review this Alone</font></a>
                               </div>
                           </div>
                        </div>
                    </td>
                </tr>
                <%
            }
        }
        %>
        </table>
        <br/><br/>
        <input type="submit" class="formsubmitbutton" value="Save and Continue">
        <br/><br/>
        <font class="normalfont" style="font-weight: bold;">Number to Display: </font>
        <select name="numOnPage">
            <%String sel = "";%>
            <%if (numOnPage==10){sel="selected";}else{sel="";}%>
            <option value="10" <%=sel%>>10</option>
            <%if (numOnPage==25){sel="selected";}else{sel="";}%>
            <option value="25" <%=sel%>>25</option>
            <%if (numOnPage==50){sel="selected";}else{sel="";}%>
            <option value="50" <%=sel%>>50</option>
        </select><br/>
        </form>
        <%


}%>



<%@ include file="/template/footer.jsp" %>



