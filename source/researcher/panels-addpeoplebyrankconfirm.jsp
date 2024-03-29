<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsEdit" %>
<%@ page import="com.dneero.htmlui.*" %>
<%String jspPageName="/researcher/panels-addpeoplebyrankconfirm.jsp";%>
<%@ include file="/jspOverrideFrameworkHeader.jsp" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Add People to Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherPanelsAddpeopleByRankconfirm researcherPanelsAddpeopleconfirm = (ResearcherPanelsAddpeopleByRankconfirm)Pagez.getBeanMgr().get("ResearcherPanelsAddpeopleByRankconfirm");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addconfirm")) {
        try {
            researcherPanelsAddpeopleconfirm.setBloggeridstoaddcommasep(Textbox.getValueFromRequest("bloggeridstoaddcommasep", "Bloggeridstoaddcommasep", false, DatatypeString.DATATYPEID));
            researcherPanelsAddpeopleconfirm.add();
            Pagez.getUserSession().setMessage("People added to panel.");
            Pagez.sendRedirect("/researcher/panels.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<br/><br/>
    <form action="/researcher/panels-addpeoplebyrankconfirm.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/researcher/panels-addpeoplebyrankconfirm.jsp">
        <input type="hidden" name="action" value="addconfirm">
        <input type="hidden" name="panelid" value="<%=researcherPanelsAddpeopleconfirm.getPanelid()%>">
        <input type="hidden" name="respondentfilterid" value="<%=researcherPanelsAddpeopleconfirm.getRespondentfilterid()%>">
        <input type="hidden" name="bloggeridstoaddcommasep" value="<%=researcherPanelsAddpeopleconfirm.getBloggeridstoaddcommasep()%>">

        <table cellpadding="5" cellspacing="0" border="0">



            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">New People to be Added</font>
                    <br/>
                    <font class="tinyfont">The number of new people that weren't already in the panel.</font>
                </td>
                <td valign="top">
                    <font class="mediumfont"><%=researcherPanelsAddpeopleconfirm.getNumberofnewrespondents()%></font>
                </td>
            </tr>


             <tr>
                <td valign="top">
                    <br/><br/>
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Yes, Add These People">
                    <br/>
                    <font class="tinyfont">People will be added to the panel '<%=researcherPanelsAddpeopleconfirm.getPanel().getName()%>'</font>
                    <br/>
                    <a href="/researcher/panels-addpeople.jsp?respondentfilterid=<%=researcherPanelsAddpeopleconfirm.getRespondentfilterid()%>&rankid=<%=researcherPanelsAddpeopleconfirm.getRankid()%>&panelid=<%=researcherPanelsAddpeopleconfirm.getPanelid()%>&rankpercentofatleast=<%=researcherPanelsAddpeopleconfirm.getRankpercentofatleast()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
                <td valign="top">

                </td>
            </tr>

        </table>

    </form>

<%@ include file="/template/footer.jsp" %><%@ include file="/jspOverrideFrameworkFooter.jsp" %>

