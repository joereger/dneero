<%@ page import="com.dneero.finders.UserProfileCompletenessChecker" %>
<%@ page import="com.dneero.htmluibeans.BloggerDetails" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%@ page import="com.dneero.util.Util" %>
<%String jspPageName="/blogger/bloggerdetails.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Your Profile";
String navtab = "bloggers";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
BloggerDetails bloggerDetails = (BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            boolean demographicsFieldsAreOK = true;
            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                           .add(Restrictions.eq("plid", Pagez.getUserSession().getPl().getPlid()))
                           .addOrder(Order.asc("ordernum"))
                           .setCacheable(true)
                           .list();
            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                Demographic demographic = demographicIterator.next();
                try{
                    boolean requireField = demographic.getIsrequired();
                    bloggerDetails.getDemographicsXML().setValue(demographic.getDemographicid(), Dropdown.getValueFromRequest("demographic_"+demographic.getDemographicid(), demographic.getName(), requireField));
                } catch (com.dneero.htmlui.ValidationException vex) {
                    demographicsFieldsAreOK = false;
                    Pagez.getUserSession().setMessage("If you don't want to answer a question please choose the non-answering value ('NA', 'Other', etc)");
                }
            }

            if (demographicsFieldsAreOK){
                bloggerDetails.setBirthdate(DateTime.getValueFromRequest("birthdate", "Birth Date", true).getTime());
                bloggerDetails.setUserid(Pagez.getUserSession().getUser().getUserid());
                bloggerDetails.setVenueurl(Textbox.getValueFromRequest("venueurl", "Venue URL", false, DatatypeString.DATATYPEID));
                bloggerDetails.setVenuefocus(Dropdown.getValueFromRequest("venuefocus", "Venue Focus", false));
                bloggerDetails.saveAction();
                if (Pagez.getUserSession().getIsfacebookui()){
                    Pagez.getUserSession().setMessage("Profile Saved Successfully. <a href=\"/blogger/bloggerdetails.jsp\">Edit again?</a>");
                } else {
                    Pagez.getUserSession().setMessage("Profile Saved Successfully. <a href=\"/blogger/bloggerdetails.jsp\">Edit again?</a>");
                }
                if (Pagez.getUserSession().getIsfacebookui()){
                    if (bloggerDetails.getIsnewblogger()){
                        Pagez.sendRedirect("/blogger/index.jsp");
                        return;
                    } else {
                        Pagez.sendRedirect("/publicsurveylist.jsp");
                        return;
                    }
                } else {
                    Pagez.sendRedirect("/blogger/index.jsp");
                    return;
                }
            }
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deletevenue")) {
        try {
            if (request.getParameter("venueid")!=null && Num.isinteger(request.getParameter("venueid"))){
                int venueid = Integer.parseInt(request.getParameter("venueid"));
                Venue venue = Venue.get(venueid);
                if (venue.getBloggerid()==Pagez.getUserSession().getUser().getBloggerid()){
                    venue.setIsactive(false);
                    venue.save();
                    Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
                    blogger.refresh();
                    bloggerDetails.initBean();
                    if (!UserProfileCompletenessChecker.isProfileComplete(Pagez.getUserSession().getUser())){
                        Pagez.getUserSession().setIsbloggerprofileok(false);
                    } else {
                        Pagez.getUserSession().setIsbloggerprofileok(true);
                    }
                }
            }
            Pagez.getUserSession().setMessage("Venue deleted!");
        } catch (Exception ex) {
            Pagez.getUserSession().setMessage("Sorry, there was an error.");
            logger.error("", ex);
        }
    }
%>
<%
if (!Pagez.getUserSession().getIsbloggerprofileok()){
    Pagez.getUserSession().setMessage("Please verify that your profile is up to date and accurate before continuing.");
}
%>
<%@ include file="/template/header.jsp" %>




    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()==0){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">Quick One-time Configuration Required</font>
            <br/>
            <font class="smallfont">We need to collect a few pieces of information about you. You won't have to do this again on future visits.  This information will be kept private.</font>
        </div>
    <%}%>

    <table cellpadding="10" cellspacing="0" border="0" width="90%">
        <tr>
            <td valign="top">
                <form action="/blogger/bloggerdetails.jsp" method="post" class="niceform">
                    <input type="hidden" name="dpage" value="/blogger/bloggerdetails.jsp">
                    <input type="hidden" name="action" value="save">

                    <table cellpadding="3" cellspacing="0" border="0">


                        <%
                            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", Pagez.getUserSession().getPl().getPlid()))
                                           .addOrder(Order.asc("ordernum"))
                                           .setCacheable(true)
                                           .list();
                            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                                Demographic demographic = demographicIterator.next();
                                %>

                                <tr>
                                    <td valign="top">
                                        <font class="formfieldnamefont"><%=demographic.getName()%></font>
                                        <%if (!demographic.getIsrequired()){ %>
                                            <font class="formfieldnamefont">(Optional)</font>
                                        <%}%>
                                        <%if (demographic.getDescription().length()>0){ %>
                                            <br/><font class="tinyfont"><%=demographic.getDescription()%></font>
                                        <%}%>
                                    </td>
                                    <td valign="top">
                                        <%=Dropdown.getHtml("demographic_"+demographic.getDemographicid(),
                                                            bloggerDetails.getDemographicsXML().getValue(demographic.getDemographicid()),
                                                            Util.stringArrayToTreeMap(bloggerDetails.getDemographicsXML().getAllPossibleValues(demographic.getDemographicid())),
                                                            "",
                                                            "")%>
                                    </td>
                                </tr>

                                <%
                            }
                        %>


                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Birthdate</font>
                            </td>
                            <td valign="top">
                                <%=Date.getHtml("birthdate", bloggerDetails.getBirthdate(), "", "")%>
                            </td>
                        </tr>


                        <%if (Pagez.getUserSession().getPl().getIsvenuerequired()){%>
                            <%if (!Pagez.getUserSession().getIsfacebookui()){%>
                                <tr>
                                    <td valign="top" colspan="2">
                                        <br/><br/>
                                        <font class="formfieldnamefont">Posting Venues</font>
                                        <br/>
                                        <font class="tinyfont">Share the URL of a place that you post things.</font>
                                        <table cellpadding="3" cellspacing="0" border="0">
                                            <%
                                                Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
                                                for (Iterator<Venue> iterator=blogger.getVenues().iterator(); iterator.hasNext();) {
                                                    Venue venue=iterator.next();
                                                    if (venue.getIsactive()){
                                                        String sysadminrejected = "";
                                                        if (venue.getIssysadminrejected()){
                                                            sysadminrejected = "<a href=\"/account/reviewables.jsp\"><img src=\"/images/alert-16.png\" alt=\"Rejected\" border=\"0\"></a>";
                                                        } else {
                                                            if (venue.getIssysadminreviewed() && !venue.getIssysadminrejected()){
                                                                sysadminrejected = "<img src=\"/images/ok-16.png\" alt=\"Approved\" border=\"0\">";
                                                            }
                                                        }
                                                        %>
                                                        <tr>
                                                            <td valign="top">
                                                                <font class="tinyfont" style="font-weight: bold;"><a href="/blogger/bloggerdetails.jsp?action=deletevenue&venueid=<%=venue.getVenueid()%>"><img src="/images/delete-16.png" alt="Delete" border="0"></a> http://<%=Str.truncateString(venue.getUrl(), 40)%><%=sysadminrejected%></font>
                                                            </td>
                                                            <td valign="top">
                                                                <font class="tinyfont"><%=venue.getFocus()%></font>
                                                            </td>
                                                        </tr>
                                                        <%
                                                    }
                                                }
                                            %>
                                            <%if (bloggerDetails.getVenuecount()<=4){%>
                                                <tr>
                                                    <td valign="top">
                                                        <%=Textbox.getHtml("venueurl", bloggerDetails.getVenueurl(), 255, 25, "", "")%><br/>

                                                    </td>
                                                    <td valign="top">
                                                        <%=Dropdown.getHtml("venuefocus", bloggerDetails.getVenuefocus(), Util.treeSetToTreeMap(Blogfocuses.get()), "", "")%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                        <font class="tinyfont" style="font-weight: bold;">URL of Venue</font>
                                                        <br/>
                                                        <font class="tinyfont">The main or home page.</font>
                                                        <br/>
                                                        <font class="tinyfont">ex: www.myblog.com</font>
                                                    </td>
                                                    <td valign="top">
                                                        <font class="tinyfont" style="font-weight: bold;">Focus</font>
                                                        <br/>
                                                        <font class="tinyfont">The primary topic covered.</font>
                                                    </td>
                                                </tr>
                                            <%}%>

                                        </table>

                                    </td>
                                </tr>
                            <%}%>
                        <%}%>


                        <tr>
                            <td valign="top">
                            </td>
                            <td valign="top">
                               <br/><br/>
                               <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save Profile">
                            </td>
                        </tr>


                    </table>

                </form>

            </td>
            <td valign="top" width="200">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <center>
                            <img src="/images/lock-64.png" width="64" height="64"/>
                            <br/>
                            <font class="mediumfont" style="color: #666666;">This information will be kept private.</font> 
                        </center>
                    </div>
                    <font class="smallfont">
                        <br/><br/><b>Why do you collect this information?</b><br/>
                        For two reasons. First, to present you with the most relevant <%=Pagez._surveys()%>.  Second, because market researchers need to be able to target <%=Pagez._surveys()%> to particular demographics

                        <br/><br/><b>Will this data ever be made available to others?</b><br/>
                        Posting Venues and geographic information will be shared on your profile.  Other information will not be made available in a way that can be tied back to you.  However, we may create aggregate results on <%=Pagez._surveys()%> that say things like "people aged 23-25 answered this way."  When we do this we're using the information from this page but it's in a way that your particular identity isn't shared.

                        <br/><br/><b>Check the <%=Pagez._survey()%> criteria</b><br/>
                        If a <%=Pagez._survey()%> has a sufficiently limited demographic target it's possible that <%=Pagez._survey()%> readers can infer demographic data about you.  For example, if somebode creates a <%=Pagez._survey()%> targeted only to individuals with an income of between $30,000 and $40,000 per year and you join the <%=Pagez._survey()%> then people can infer what your salary is.  When a <%=Pagez._survey()%> is displayed both the criteria and who joined in are visible.  Take this into account and if you're not comfortable with the scope of the demographic criteria that the researcher has chosen, tell them... they may choose to widen it.
                    </font>
                    <br/><br/>
                </div>
            </td>
        </tr>
    </table>


<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>

