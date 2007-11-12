<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.BloggerDetails" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.util.Util" %>
<%@ page import="com.dneero.constants.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Your Profile";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
BloggerDetails bloggerDetails = (BloggerDetails)Pagez.getBeanMgr().get("BloggerDetails");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            bloggerDetails.setBirthdate(DateTime.getValueFromRequest("birthdate", "Birth Date", true).getTime());
            bloggerDetails.setBlogfocus(Dropdown.getValueFromRequest("blogfocus", "Your Focus", true));
            bloggerDetails.setCity(Dropdown.getValueFromRequest("city", "City", true));
            bloggerDetails.setEducationlevel(Dropdown.getValueFromRequest("educationlevel", "Education Level", true));
            bloggerDetails.setEthnicity(Dropdown.getValueFromRequest("ethnicity", "Ethnicity", true));
            bloggerDetails.setGender(Dropdown.getValueFromRequest("gender", "Gender", true));
            bloggerDetails.setIncome(Dropdown.getValueFromRequest("income", "Income", true));
            bloggerDetails.setMaritalstatus(Dropdown.getValueFromRequest("maritalstatus", "Marital Status", true));
            bloggerDetails.setPolitics(Dropdown.getValueFromRequest("politics", "Politics", true));
            bloggerDetails.setProfession(Dropdown.getValueFromRequest("profession", "Profession", true));
            bloggerDetails.setState(Dropdown.getValueFromRequest("state", "State", true));
            bloggerDetails.setUserid(Pagez.getUserSession().getUser().getUserid());
            bloggerDetails.saveAction();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>




    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()==0){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">Quick One-time Configuration Required</font>
            <br/>
            <font class="smallfont">Before you can make money we need to collect a few pieces of information about you.  This will only take a minute or two and you won't have to do it again on future visits.  You'll be filling out surveys, posting them socially and making money in no time! We must take your profile so that we can find offers that fit you.  This is a one-time step.  You can edit your answers later on.</font>
        </div>
    <%}%>

    <table cellpadding="10" cellspacing="0" border="0" width="90%">
        <tr>
            <td valign="top">
                <form action="bloggerdetails.jsp" method="post">
                    <input type="hidden" name="action" value="save">

                    <table cellpadding="0" cellspacing="0" border="0">

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Birthdate</font>
                            </td>
                            <td valign="top">
                                <%=DateTime.getHtml("birthdate", bloggerDetails.getBirthdate(), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Gender</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("gender", bloggerDetails.getGender(), Util.treeSetToArrayList(Genders.get()), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Ethnicity</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("ethnicity", bloggerDetails.getEthnicity(), Util.treeSetToArrayList(Ethnicities.get()), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Marital Status</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("maritalstatus", bloggerDetails.getMaritalstatus(), Util.treeSetToArrayList(Maritalstatuses.get()), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Income</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("income", bloggerDetails.getIncome(), Util.treeSetToArrayList(Incomes.get()), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Education</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("educationlevel", bloggerDetails.getEducationlevel(), Util.treeSetToArrayList(Educationlevels.get()), "", "")%>
                            </td>
                        </tr>


                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Nearest City</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("city", bloggerDetails.getCity(), Util.treeSetToArrayList(Cities.get()), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">State</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("state", bloggerDetails.getState(), Util.treeSetToArrayList(States.get()), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Profession</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("profession", bloggerDetails.getProfession(), Util.treeSetToArrayList(Professions.get()), "", "")%>
                            </td>
                        </tr>


                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Politics</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("politics", bloggerDetails.getPolitics(), Util.treeSetToArrayList(Politics.get()), "", "")%>
                            </td>
                        </tr>

                        <tr>
                            <td valign="top">
                                <font class="formfieldnamefont">Your Focus</font>
                                <br/>
                                <font class="tinyfont">The most accurate description of what<br/>you blog about or discuss on<br/>social networks.</font>
                            </td>
                            <td valign="top">
                                <%=Dropdown.getHtml("blogfocus", bloggerDetails.getBlogfocus(), Util.treeSetToArrayList(Blogfocuses.get()), "", "")%>
                            </td>
                        </tr>


                        <tr>
                            <td valign="top">
                            </td>
                            <td valign="top">
                               <input type="submit" value="Save Profile">
                            </td>
                        </tr>


                    </table>

                </form>

            </td>
            <td valign="top" width="200">
                <div class="rounded" style="padding: 15px; margin: 5px; background: #00ff00;">
                    <div class="rounded" style="padding: 15px; margin: 5px; background: #ffffff;">
                        <center>
                            <img src="/images/lock-64.png" width="64" height="64"/>
                            <br/>
                            <font class="mediumfont" style="color: #666666;">This information will be kept private.</font> 
                        </center>
                    </div>
                    <font class="smallfont">
                        <br/><br/><b>Why do you collect this information?</b><br/>
                        For two reasons. First, to present you with the most relevant surveys.  Second, because market researchers need to be able to target surveys to particular demographics

                        <br/><br/><b>Will this data ever be made available to others?</b><br/>
                        It will not be made available in a way that can be tied back to you.  However, we may create aggregate results on surveys that say things like "people aged 23-25 answered this way."  When we do this we're using the information from this page but it's in a way that your particular identity isn't shared.

                        <br/><br/><b>Check the survey criteria</b><br/>
                        If a survey has a sufficiently limited demographic target it's possible that survey readers can infer demographic data about you.  For example, if a researcher creates a survey targeted only to individuals with an income of between $30,000 and $40,000 per year and you take the survey then people can infer what your salary is.  When a survey is displayed both the criteria and who took the survey are visible.  Take this into account and if you're not comfortable with the scope of the demographic criteria that the researcher has chosen, tell them... they may choose to widen it.
                    </font>
                    <br/><br/>
                </div>
            </td>
        </tr>
    </table>


<%@ include file="/jsp/templates/footer.jsp" %>

