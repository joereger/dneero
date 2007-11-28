<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicProfile" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = ((PublicProfile) Pagez.getBeanMgr().get("PublicProfile")).getUser().getFirstname()+" "+((PublicProfile) Pagez.getBeanMgr().get("PublicProfile")).getUser().getLastname()+"'s Profile";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    PublicProfile publicProfile=(PublicProfile) Pagez.getBeanMgr().get("PublicProfile");
%>
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
<%@ include file="/template/header.jsp" %>

    <%if (publicProfile.getMsg()!=null && !publicProfile.getMsg().equals("")){%>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <%=publicProfile.getMsg()%>
        </font></div></center>
        <br/><br/>
    <%}%>

    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 20px;">
    <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" width="50%">
                <img src="/images/user.png" alt="" border="0" width="128" height="128"/>
            </td>
            <td valign="top">
                <div class="rounded" style="background: #ffffff; text-align: left; padding: 15px;">
                    <table cellpadding="10" cellspacing="0" border="0" width="100%">
                        <tr>
                            <td valign="top" width="50%">
                                <font class="formfieldnamefont">Social Influence Rating (TM)</font>
                            </td>
                            <td valign="top" width="50%">
                                <font class="smallfont"><%=publicProfile.getBlogger().getSocialinfluencerating()%></font>
                                <br/>
                                <font class="smallfont">Rank: <%=publicProfile.getBlogger().getSocialinfluenceratingranking()%></font>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" width="50%">
                                <font class="formfieldnamefont">Amt Earned for Charity</font>
                            </td>
                            <td valign="top" width="50%">
                                <font class="smallfont"><%=publicProfile.getCharityamtdonatedForscreen()%></font>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
          </tr>
       </table>
       </div>


       <br/><br/>
       <font class="mediumfont" style="color: #cccccc;">Surveys Taken</font>
       <br/>
       <%if (publicProfile.getListitems()==null || publicProfile.getListitems().size()==0){%>
            <font class="normalfont">None... yet.</font>
       <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Date", "<$response.responsedate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont"));
                cols.add(new GridCol("Survey Title", "<$survey.title$>", false, "", "normalfont"));
                cols.add(new GridCol("", "<a href=\"/survey.jsp?u="+publicProfile.getUser().getUserid()+"\">Answers</a>", false, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"/profileimpressions.jsp?responseid=<$response.responseid$>\">Impressions</a>", false, "", "smallfont"));
            %>
            <%=Grid.render(publicProfile.getListitems(), cols, 10, "/profile.jsp?userid="+publicProfile.getUser().getUserid(), "pagesurveys")%>
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
                <%=Grid.render(publicProfile.getPanels(), cols, 10, "/profile.jsp?userid="+publicProfile.getUser().getUserid(), "pagepanels")%>
            <%}%>


            <br/><br/>
            <form action="/profile.jsp" method="post">
                <input type="hidden" name="dpage" value="/profile.jsp">
                <input type="hidden" name="action" value="add">
                <%=com.dneero.htmlui.Dropdown.getHtml("panelid", String.valueOf(publicProfile.getPanelid()), publicProfile.getPanelids(), "", "")%>
                <input type="submit" class="formsubmitbutton" value="Add Blogger To Panel">
            </form>
        <%}%>

    


<%@ include file="/template/footer.jsp" %>