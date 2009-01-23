<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareUserDetail" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveysListitem" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.money.CurrentBalanceCalculator" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%@ page import="com.dneero.review.ReviewableVenue" %>
<%@ page import="com.dneero.sir.SocialInfluenceRatingPercentile" %>
<%@ page import="com.dneero.scheduledjobs.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "User: "+((CustomercareUserDetail) Pagez.getBeanMgr().get("CustomercareUserDetail")).getEmail();
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercareUserDetail customercareUserDetail= (CustomercareUserDetail)Pagez.getBeanMgr().get("CustomercareUserDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("sendmessagetouser")) {
        try {
            customercareUserDetail.setMessagetouser(Textbox.getValueFromRequest("messagetouser", "Message", true, DatatypeString.DATATYPEID));
            customercareUserDetail.setMessagetousersubject(Textbox.getValueFromRequest("messagetousersubject", "Subject", true, DatatypeString.DATATYPEID));
            customercareUserDetail.sendusermessage();
            Pagez.getUserSession().setMessage("Message Sent!");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            customercareUserDetail.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
            customercareUserDetail.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
            customercareUserDetail.setEmail(Textbox.getValueFromRequest("email", "Email", false, DatatypeString.DATATYPEID));
            customercareUserDetail.setPaypaladdress(Textbox.getValueFromRequest("paypaladdress", "PayPal Address", false, DatatypeString.DATATYPEID));
            customercareUserDetail.setReferredbyuserid(Textbox.getIntFromRequest("referredbyuserid", "Referredbyuserid", false, DatatypeString.DATATYPEID));
            customercareUserDetail.setFacebookuid(Textbox.getValueFromRequest("facebookuserid", "Facebookuserid", false, DatatypeString.DATATYPEID));
            customercareUserDetail.save();
            Pagez.getUserSession().setMessage("User details saved");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("togglesysadmin")) {
        try {
            customercareUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            customercareUserDetail.setPwd(Textbox.getValueFromRequest("pwd", "Password", true, DatatypeString.DATATYPEID));
            customercareUserDetail.togglesysadminprivs();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("togglecustomercare")) {
        try {
            customercareUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            customercareUserDetail.setPwd(Textbox.getValueFromRequest("pwd", "Password", true, DatatypeString.DATATYPEID));
            customercareUserDetail.togglecustomercareprivs();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("updateresellerpercent")) {
        try {
            customercareUserDetail.setResellerpercent(Textbox.getDblFromRequest("resellerpercent", "Reseller Percent", true, DatatypeDouble.DATATYPEID));
            customercareUserDetail.updateresellerpercent();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deleteuser")) {
        try {
            customercareUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            customercareUserDetail.setPwd(Textbox.getValueFromRequest("pwd", "Password", true, DatatypeString.DATATYPEID));
            customercareUserDetail.deleteuser();
            Pagez.getUserSession().setMessage("User deleted");
            Pagez.sendRedirect("/customercare/userlist.jsp");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("toggleisenabled")) {
        try {
            customercareUserDetail.toggleisenabled();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("giveusermoney")) {
        try {
            customercareUserDetail.setAmt(Textbox.getDblFromRequest("amt", "Amount", true, DatatypeDouble.DATATYPEID));
            customercareUserDetail.setReason(Textbox.getValueFromRequest("reason", "Reason", true, DatatypeString.DATATYPEID));
            customercareUserDetail.setFundstype(Dropdown.getIntFromRequest("fundstype", "Funds Type", true));
            customercareUserDetail.setPwd(Textbox.getValueFromRequest("pwd", "Password", true, DatatypeString.DATATYPEID));
            customercareUserDetail.giveusermoney();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("takeusermoney")) {
        try {
            customercareUserDetail.setAmt(Textbox.getDblFromRequest("amt", "Amount", true, DatatypeDouble.DATATYPEID));
            customercareUserDetail.setReason(Textbox.getValueFromRequest("reason", "Reason", true, DatatypeString.DATATYPEID));
            customercareUserDetail.setFundstype(Dropdown.getIntFromRequest("fundstype", "Funds Type", true));
            customercareUserDetail.takeusermoney();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("passwordresetemail")) {
        try {
            customercareUserDetail.sendresetpasswordemail();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("reactivationemail")) {
        try {
            customercareUserDetail.reactivatebyemail();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("researcherremainingbalanceoperations")) {
        try {
            customercareUserDetail.runResearcherRemainingBalanceOperations();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("runcurrentbalanceupdater")) {
        try {
            customercareUserDetail.runCurrentBalanceUpdater();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <%
            CurrentBalanceCalculator cbc=new CurrentBalanceCalculator(customercareUserDetail.getUser());
        %>

        <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top" width="50%">

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">First Name</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("firstname", customercareUserDetail.getFirstname(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Last Name</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("lastname", customercareUserDetail.getLastname(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Email</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("email", customercareUserDetail.getEmail(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">PayPal Address</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("paypaladdress", customercareUserDetail.getPaypaladdress(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Referredbyuserid</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("referredbyuserid", String.valueOf(customercareUserDetail.getReferredbyuserid()), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Facebook uid</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("facebookuserid", String.valueOf(customercareUserDetail.getFacebookuid()), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Reseller Code</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=customercareUserDetail.getUser().getResellercode()%></font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Reseller Percent</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=customercareUserDetail.getUser().getResellerpercent()%>%</font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">

                                        </td>
                                        <td valign="top">
                                            <input type="submit" class="formsubmitbutton" value="Save User Details">
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <a href="/customercare/userdetailextended.jsp?userid=<%=customercareUserDetail.getUser().getUserid()%>"><b>User's extended detail screen.</b></a>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <a href="/customercare/iptrack.jsp?action=search&searchuserid=<%=customercareUserDetail.getUserid()%>"><b>Ip Track data for this user.</b></a>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <a href="/customercare/userlist.jsp?action=search&searchreferredbyuserid=<%=customercareUserDetail.getUserid()%>"><b>Users referred by this userid.</b></a>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="passwordresetemail">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="Send Password Reset Email">
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="reactivationemail">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="Force Re-Activation By Email">
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="researcherremainingbalanceoperations">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="ResearcherRemainingBalanceOperations">
                            </form>
                            <br/>
                            <font class="tinyfont">This will process account balances, remaining impressions, credit card transfers, etc for only this account.  Only does something if this user has a researcher record.</font>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <font class="tinyfont">
                                <br/><b>Currentbalance</b> ($<%=cbc.getCurrentbalance()%> / $<%=customercareUserDetail.getUser().getCurrentbalance()%>)
                                <br/><b>Currentbalanceblogger</b> ($<%=cbc.getCurrentbalanceblogger()%> / $<%=customercareUserDetail.getUser().getCurrentbalanceblogger()%>)
                                <br/><b>Currentbalanceresearcher</b> ($<%=cbc.getCurrentbalanceresearcher()%> / $<%=customercareUserDetail.getUser().getCurrentbalanceresearcher()%>)
                                <br/>(Realtime/User Table)
                            </font>
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="runcurrentbalanceupdater">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="Run CurrentBalanceUpdater">
                            </form>
                            <font class="tinyfont">This will refresh/update the current balance numbers stored with the User database table.</font>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="togglesysadmin">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <%if (customercareUserDetail.getIssysadmin()){%>
                                    <font class="mediumfont">User is a Sysadmin.</font>
                                <%} else {%>
                                    <font class="mediumfont">User is not a Sysadmin.</font>
                                <%}%>
                                <br/>
                                <font class="formfieldnamefont">Password:</font>
                                <br/>
                                <%=Textbox.getHtml("pwd", String.valueOf(customercareUserDetail.getPwd()), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Toggle Sysadmin Privileges">
                                <%=Textbox.getHtml("activitypin", String.valueOf(customercareUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="togglecustomercare">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <%if (customercareUserDetail.getIscustomercare()){%>
                                    <font class="mediumfont">User is a Customer Care Rep.</font>
                                <%} else {%>
                                    <font class="mediumfont">User is not a Customer Care Rep.</font>
                                <%}%>
                                <br/>
                                <font class="formfieldnamefont">Password:</font>
                                <br/>
                                <%=Textbox.getHtml("pwd", String.valueOf(customercareUserDetail.getPwd()), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Toggle Customer Care Privs">
                                <%=Textbox.getHtml("activitypin", String.valueOf(customercareUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="deleteuser">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <br/>
                                <font class="formfieldnamefont">Password:</font>
                                <br/>
                                <%=Textbox.getHtml("pwd", String.valueOf(customercareUserDetail.getPwd()), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Delete User">
                                <%=Textbox.getHtml("activitypin", String.valueOf(customercareUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                        </div>
                    </td>
                 
                    <td valign="top" width="50%">

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="toggleisenabled">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <%if (customercareUserDetail.getIsenabled()){%>
                                    <font class="mediumfont">This Account is Currently Enabled</font>
                                    <br/>
                                    <input type="submit" class="formsubmitbutton" value="Disable Account">
                                <%} else {%>
                                    <font class="mediumfont">This Account is Currently Disabled</font>
                                    <br/>
                                    <input type="submit" class="formsubmitbutton" value="Enable Account">
                                <%}%>
                            </form>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <font class="mediumfont">Posting Venues</font>
                            <br/>
                            <%
                            if (customercareUserDetail.getUser().getBloggerid()>0){
                                Blogger blogger = Blogger.get(customercareUserDetail.getUser().getBloggerid());
                                if (blogger!=null && blogger.getBloggerid()>0){
                                    %>
                                    <table cellpadding="3" cellspacing="0" border="0">
                                        <%
                                            for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
                                                Venue venue=iterator.next();
                                                if (venue.getIsactive()){
                                                    %>
                                                    <tr>
                                                        <td valign="top">
                                                            <font class="tinyfont" style="font-weight: bold;"><a href="/customercare/reviewables-turbo.jsp?type=<%=ReviewableVenue.TYPE%>&id=<%=venue.getVenueid()%>">review</a> http://<%=Str.truncateString(venue.getUrl(), 40)%></font>
                                                        </td>
                                                        <td valign="top">
                                                            <font class="tinyfont"><%=venue.getFocus()%></font>
                                                        </td>
                                                    </tr>
                                                    <%
                                                }
                                            }
                                        %>
                                    </table>
                                    <%
                                }
                            }
                            %>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <font class="mediumfont">Flaggings</font>
                            <br/>
                            <%
                            List<Review> reviews = HibernateUtil.getSession().createCriteria(Review.class)
                                                               .add(Restrictions.eq("useridofcontentcreator", customercareUserDetail.getUser().getUserid()))
                                                               .addOrder(Order.asc("reviewid"))
                                                               .setCacheable(true)
                                                               .list();
                            %>
                            <table cellpadding="3" cellspacing="0" border="0">
                                <%
                                    for (Iterator<Review> iterator=reviews.iterator(); iterator.hasNext();) {
                                        Review review=iterator.next();
                                        %>
                                        <tr>
                                            <td valign="top">
                                                <font class="tinyfont" style="font-weight: bold;"><a href="/customercare/reviewables-turbo.jsp?type=<%=review.getType()%>&id=<%=review.getId()%>">review</a> <%=Str.truncateString(review.getSysadminnotes(), 80)%></font>
                                            </td>
                                        </tr>
                                        <%
                                    }
                                %>
                            </table>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="sendmessagetouser">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <font class="mediumfont">Send User a Message</font>
                                <br/>
                                <font class="formfieldnamefont">Subject:</font>
                                <br/>
                                <%=Textbox.getHtml("messagetousersubject", String.valueOf(customercareUserDetail.getMessagetousersubject()), 255, 25, "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Message:</font>
                                <br/>
                                <%=Textarea.getHtml("messagetouser", customercareUserDetail.getMessagetouser(), 5, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Send">
                            </form>
                        </div>



                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="giveusermoney">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <font class="mediumfont">Give User Money</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to give:</font>
                                <br/>
                                <%=Textbox.getHtml("amt", String.valueOf(customercareUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <%=Dropdown.getHtml("fundstype", String.valueOf(customercareUserDetail.getFundstype()), StaticVariables.getFundsTypes(), "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <font class="tinyfont">(user will see)</font>
                                <br/>
                                <%=Textbox.getHtml("reason", customercareUserDetail.getReason(), 255, 25, "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Password:</font>
                                <br/>
                                <%=Textbox.getHtml("pwd", String.valueOf(customercareUserDetail.getPwd()), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Give User Money">
                            </form>
                        </div>


                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="takeusermoney">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <font class="mediumfont">Take Money from User</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to take:</font>
                                <br/>
                                <%=Textbox.getHtml("amt", String.valueOf(customercareUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <%=Dropdown.getHtml("fundstype", String.valueOf(customercareUserDetail.getFundstype()), StaticVariables.getFundsTypes(), "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <font class="tinyfont">(user will see)</font>
                                <br/>
                                <%=Textbox.getHtml("reason", customercareUserDetail.getReason(), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Take User Money">
                            </form>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/customercare/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/customercare/userdetail.jsp">
                                <input type="hidden" name="action" value="updateresellerpercent">
                                <input type="hidden" name="userid" value="<%=customercareUserDetail.getUserid()%>">
                                <font class="mediumfont">Reseller Percent</font>
                                <br/>
                                <font class="formfieldnamefont">Percent this person earns from reselling:</font>
                                <br/>
                                <%=Textbox.getHtml("resellerpercent", String.valueOf(customercareUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">Leave at 0 to use default value of <%=SurveyMoneyStatus.RESELLERPERCENTDEFAULT%></font>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Update">
                            </form>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <font class="mediumfont">Social Influence Rating</font>
                            <br/>
                            <br/>
                            <%
                            String socialinfluenceratingforscreen = "";
                            double socialinfluenceratingpercentile = SocialInfluenceRatingPercentile.getPercentileOfRanking(com.dneero.scheduledjobs.SystemStats.getTotalusers(), customercareUserDetail.getUser().getSirrank());
                            if (socialinfluenceratingpercentile>=50){
                                socialinfluenceratingforscreen = "Top "+(100-socialinfluenceratingpercentile)+"%";
                            } else {
                                socialinfluenceratingforscreen = "Bottom "+(socialinfluenceratingpercentile)+"%";
                            }
                            %>
                            <font class="formfieldnamefont">SiR: <%=socialinfluenceratingforscreen%></font>
                            <br/>
                            <font class="formfieldnamefont">SiR Points: <%=customercareUserDetail.getUser().getSirpoints()%></font>
                            <br/>
                            <font class="formfieldnamefont">SiR Date: <%=Time.dateformatcompactwithtime(Time.getCalFromDate(customercareUserDetail.getUser().getSirdate()))%></font>
                            <br/>
                            <font class="formfieldnamefont">SiR Algorithm: <%=customercareUserDetail.getUser().getSiralgorithm()%></font>
                            <br/>
                            <font class="formfieldnamefont">SiR Breakdown:</font><font class="tinyfont"><%=customercareUserDetail.getUser().getSirdebug().replaceAll("\\\n", "<br/>")%></font>
                        </div>
                   
                    </td>
                </tr>
            </table>

        <% if (customercareUserDetail.getResearcher()!=null && customercareUserDetail.getResearcher().getResearcherid()>0){ %>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                <font class="mediumfont">Researcher Most Recent Financial Stats (Delayed, Not Guaranteed Accurate)</font>
                <br/>
                <font class="tinyfont">(Calculated in ResearcherRemainingBalanceOperations.java)</font>
                <br/><br/>
                <font class="smallfont">Max Possible Spend: $<%=customercareUserDetail.getResearcher().getNotaccuratemaxpossspend()%></font>
                <br/>
                <font class="smallfont">Max Remaining Spend: $<%=customercareUserDetail.getResearcher().getNotaccurateremainingpossspend()%></font>
                <br/>
                <font class="smallfont">Current Balance: $<%=customercareUserDetail.getResearcher().getNotaccuratecurrbalance()%></font>
                <br/>
                <font class="smallfont">Percent of Max: <%=customercareUserDetail.getResearcher().getNotaccuratepercentofmax()%> percent</font>
                <br/>
                <font class="smallfont">Amt To Charge: $<%=customercareUserDetail.getResearcher().getNotaccurateamttocharge()%></font>
                <br/>
            </div>
        <% } %>

        




<%@ include file="/template/footer.jsp" %>


