<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsEdit" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Add People to SuperPanel";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercarePanelsAddpeopleconfirm customercarePanelsAddpeopleconfirm= (CustomercarePanelsAddpeopleconfirm)Pagez.getBeanMgr().get("CustomercarePanelsAddpeopleconfirm");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addconfirm")) {
        try {
            customercarePanelsAddpeopleconfirm.setBloggeridstoaddcommasep(Textbox.getValueFromRequest("bloggeridstoaddcommasep", "Bloggeridstoaddcommasep", false, DatatypeString.DATATYPEID));
            customercarePanelsAddpeopleconfirm.add();
            Pagez.getUserSession().setMessage("People added to panel.");
            Pagez.sendRedirect("/customercare/panels.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<br/><br/>
    <form action="/customercare/panels-addpeopleconfirm.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/customercare/panels-addpeopleconfirm.jsp">
        <input type="hidden" name="action" value="addconfirm">
        <input type="hidden" name="panelid" value="<%=customercarePanelsAddpeopleconfirm.getPanelid()%>">
        <input type="hidden" name="surveyid" value="<%=customercarePanelsAddpeopleconfirm.getSurveyid()%>">
        <input type="hidden" name="respondentfilterid" value="<%=customercarePanelsAddpeopleconfirm.getRespondentfilterid()%>">
        <input type="hidden" name="bloggeridstoaddcommasep" value="<%=customercarePanelsAddpeopleconfirm.getBloggeridstoaddcommasep()%>">

        <table cellpadding="5" cellspacing="0" border="0">

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Total People in Selection</font>
                    <br/>
                    <font class="tinyfont">The total number of people from the <%=Pagez._survey()%> and filter selected.</font>
                </td>
                <td valign="top">
                    <font class="mediumfont"><%=customercarePanelsAddpeopleconfirm.getNumberofrespondents()%></font>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">New People to be Added</font>
                    <br/>
                    <font class="tinyfont">The number of new people that weren't already in the panel.</font>
                </td>
                <td valign="top">
                    <font class="mediumfont"><%=customercarePanelsAddpeopleconfirm.getNumberofnewrespondents()%></font>
                </td>
            </tr>


             <tr>
                <td valign="top">
                    <br/><br/>
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Yes, Add These People">
                    <br/>
                    <font class="tinyfont">People will be added to the panel '<%=customercarePanelsAddpeopleconfirm.getPanel().getName()%>'</font>
                    <br/>
                    <a href="/customercare/panels-addpeople.jsp?surveyid=<%=customercarePanelsAddpeopleconfirm.getSurveyid()%>&respondentfilterid=<%=customercarePanelsAddpeopleconfirm.getRespondentfilterid()%>&panelid=<%=customercarePanelsAddpeopleconfirm.getPanelid()%>"><font class="tinyfont">Nevermind, Take me Back</font></a>
                </td>
                <td valign="top">

                </td>
            </tr>

        </table>

    </form>

<%@ include file="/template/footer.jsp" %>

