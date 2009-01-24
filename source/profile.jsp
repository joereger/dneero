<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicProfile" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.helpers.NicknameHelper" %>
<%@ page import="com.dneero.helpers.IsBloggerInPanel" %>
<%
    PublicProfile publicProfile=(PublicProfile) Pagez.getBeanMgr().get("PublicProfile");
%>
<%
if (publicProfile==null || publicProfile.getUser()==null || publicProfile.getUser().getUserid()==0 || publicProfile.getUser().getBloggerid()==0 || !publicProfile.getUser().getIsenabled()){
    Pagez.sendRedirect("/notauthorized.jsp");
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = NicknameHelper.getNameOrNickname(publicProfile.getUser());
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("add")) {
        try {
            publicProfile.setPanelid(Dropdown.getIntFromRequest("panelid", "Panel", true));
            publicProfile.addToPanel();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addsuperpanel")) {
        try {
            publicProfile.setSuperpanelid(Dropdown.getIntFromRequest("superpanelid", "SuperPanel", true));
            publicProfile.addToSuperPanel();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <%if (publicProfile.getMsg()!=null && !publicProfile.getMsg().equals("")){%>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <%=publicProfile.getMsg()%>
        </font></div></center>
        <br/><br/>
    <%}%>

    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
    <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" width="50%">
                <%if (publicProfile.getBlogger()!=null){%>
                    <%--<font class="mediumfont" style="color: #ffffff;">Posting Venues</font>--%>
                    <%--<br/>--%>
                    <table cellpadding="2" cellspacing="0" border="0" width="100%">
                        <%
                            for (Iterator<Venue> iterator=publicProfile.getBlogger().getVenues().iterator(); iterator.hasNext();) {
                                Venue venue=iterator.next();
                                if (venue.getIsactive() && !venue.getIssysadminrejected()){
                                    %>
                                    <tr>
                                        <td valign="top">
                                            <div class="rounded" style="background: #cccccc; text-align: left; padding: 8px;">
                                                <a href="http://<%=venue.getUrl()%>" target="_venue"><font class="normalfont" style="font-weight: bold;">http://<%=Str.truncateString(venue.getUrl(), 25)%></font></a>
                                                <br/><font class="tinyfont" style="font-weight: bold;"><%=venue.getFocus()%></font>
                                            </div>
                                        </td>
                                    </tr>
                                    <%
                                }
                            }
                        %>
                    </table>
                <%}%>
            </td>
            <td valign="top">
                <div class="rounded" style="background: #ffffff; text-align: left; padding: 15px;">
                    <table cellpadding="2" cellspacing="0" border="0" width="100%">
                        <%if (publicProfile.getBlogger()!=null){%>
                            <tr>
                                <td valign="top" align="right" nowrap>
                                    <font class="formfieldnamefont">Social Influence Rating<sup>TM</sup></font>
                                </td>
                                <td valign="top">
                                    <font class="smallfont"><%=publicProfile.getSocialinfluenceratingforscreen()%></font>
                                </td>
                            </tr>
                        <%}%>
                        <tr>
                            <td valign="top" align="right">
                                <font class="formfieldnamefont">Joined</font>
                            </td>
                            <td valign="top">
                                <font class="smallfont"><%=Time.agoText(Time.getCalFromDate(publicProfile.getUser().getCreatedate()))%></font>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" align="right">
                                <font class="formfieldnamefont">Convos Joined</font>
                            </td>
                            <td valign="top">
                                <font class="smallfont"><%=publicProfile.getConvosjoined()%></font>
                            </td>
                        </tr>
                        <%if (publicProfile.getBlogger()!=null){%>
                            <tr>
                                <td valign="top" align="right">
                                    <font class="formfieldnamefont">Location</font>
                                </td>
                                <td valign="top">
                                    <font class="smallfont"><%=publicProfile.getBlogger().getCity()%>, <%=publicProfile.getBlogger().getState()%>, <%=publicProfile.getBlogger().getCountry()%></font>
                                </td>
                            </tr>
                        <%}%>
                        <tr>
                            <td valign="top" align="right">
                                <font class="formfieldnamefont">Amt Earned for Charity</font>
                            </td>
                            <td valign="top">
                                <font class="smallfont"><%=publicProfile.getCharityamtdonatedForscreen()%></font>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" align="right">
                                <font class="formfieldnamefont">People Referred</font>
                            </td>
                            <td valign="top">
                                <font class="smallfont"><%=publicProfile.getPeoplereferred()%></font>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" align="right">
                                <a href="/superpanels.jsp"><font class="formfieldnamefont">SuperPanels</font></a>
                            </td>
                            <td valign="top">
                                <%if (publicProfile.getSuperpanels()==null || publicProfile.getSuperpanels().size()==0){%>
                                    <font class="smallfont">None</font>
                                <%} else {%>
                                    <font class="smallfont"><%
                                        for (Iterator<Panel> iterator=publicProfile.getSuperpanels().iterator(); iterator.hasNext();) {
                                            Panel panel=iterator.next();
                                            %><%=panel.getName()%><%
                                            if (iterator.hasNext()){
                                                %>, <%
                                            }
                                        }
                                    %>
                                    </font>
                                <%}%>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
          </tr>
       </table>
       </div>


       <br/><br/>
       <font class="mediumfont" style="color: #cccccc;">Conversations <%=NicknameHelper.getNameOrNickname(publicProfile.getUser())%> Joined</font>
       <br/>
       <%if (publicProfile.getListitems()==null || publicProfile.getListitems().size()==0){%>
            <font class="normalfont">None... yet.</font>
       <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                //cols.add(new GridCol("Date", "<$response.responsedate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont"));
                cols.add(new GridCol("", "<font class=\"normalfont\"><b><$survey.title$></b></font><br/><font class=\"tinyfont\">"+NicknameHelper.getNameOrNickname(publicProfile.getUser())+" Asked: <$userquestion.question$></font><br/>", false, "", ""));
                cols.add(new GridCol("", "<a href=\"/survey.jsp?s=<$response.surveyid$>&u="+publicProfile.getUser().getUserid()+"\">"+NicknameHelper.getNameOrNickname(publicProfile.getUser())+"'s Answers</a>", false, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"/surveyresults.jsp?s=<$response.surveyid$>\">Results</a>", false, "", "smallfont"));
                //cols.add(new GridCol("", "<a href=\"/profileimpressions.jsp?responseid=<$response.responseid$>\">Impressions</a>", false, "", "smallfont"));
            %>
            <%=Grid.render(publicProfile.getListitems(), cols, 50, "/profile.jsp?userid="+publicProfile.getUser().getUserid(), "pagesurveys")%>
        <%}%>



        <% if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getResearcherid()>0){ %>
            <br/><br/>
            <font class="mediumfont" style="color: #cccccc;">Panel Membership</font>
            <br/>
            <%if (publicProfile.getPanels()==null || publicProfile.getPanels().size()==0){%>
                <font class="normalfont">None... yet.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Panel Name", "<$name$>", true, "", "smallfont"));
               %>
                <%=Grid.render(publicProfile.getPanels(), cols, 20, "/profile.jsp?userid="+publicProfile.getUser().getUserid(), "pagepanels")%>
            <%}%>


            <br/><br/>
            <form action="/profile.jsp" method="post">
                <input type="hidden" name="dpage" value="/profile.jsp">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="userid" value="<%=publicProfile.getUser().getUserid()%>">
                <%=com.dneero.htmlui.Dropdown.getHtml("panelid", String.valueOf(publicProfile.getPanelid()), publicProfile.getPanelids(), "", "")%>
                <input type="submit" class="formsubmitbutton" value="Add Blogger To Panel">
            </form>
        <%}%>


        <% if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIsCustomerCare()){ %>
            <br/><br/>
            <font class="mediumfont" style="color: #cccccc;">SuperPanel Membership</font>
            <br/>
            <%if (publicProfile.getSuperpanels()==null || publicProfile.getSuperpanels().size()==0){%>
                <font class="normalfont">None.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("SuperPanel Name", "<$name$>", true, "", "smallfont"));
               %>
                <%=Grid.render(publicProfile.getSuperpanels(), cols, 20, "/profile.jsp?userid="+publicProfile.getUser().getUserid(), "pagesuperpanels")%>
            <%}%>
            <br/><br/>
            <form action="/profile.jsp" method="post">
                <input type="hidden" name="dpage" value="/profile.jsp">
                <input type="hidden" name="action" value="addsuperpanel">
                <input type="hidden" name="userid" value="<%=publicProfile.getUser().getUserid()%>">
                <%=com.dneero.htmlui.Dropdown.getHtml("superpanelid", String.valueOf(publicProfile.getSuperpanelid()), publicProfile.getSuperPanelids(), "", "")%>
                <input type="submit" class="formsubmitbutton" value="Add Blogger To SuperPanel">
            </form>
        <%}%>

        <% if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIsCustomerCare()){ %>
            <br/><br/>
            <font class="mediumfont" style="color: #cccccc;">Social Influence Rating Breakdown</font>
            <br/>
            <font class="smallfont">
            <%=publicProfile.getUser().getSirdebug().replaceAll("\\\n", "<br/>")%>
            </font>
        <%}%>

    


<%@ include file="/template/footer.jsp" %>