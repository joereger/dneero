<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherPanelsEdit" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Add People to Panel";
String navtab = "researchers";
String acl = "researcher";
%>
<%@ include file="/template/auth.jsp" %>
<%
ResearcherPanelsAddpeople researcherPanelsAddpeople = (ResearcherPanelsAddpeople)Pagez.getBeanMgr().get("ResearcherPanelsAddpeople");
%>
<%
if (researcherPanelsAddpeople.getPanelid()<=0){
    Pagez.getUserSession().setMessage("Please choose which panel to add people to.");   
}
%>
<%@ include file="/template/header.jsp" %>

<br/><br/>
    <form action="/researcher/panels-addpeopleconfirm.jsp" method="post">
        <input type="hidden" name="dpage" value="/researcher/panels-addpeopleconfirm.jsp">
        <input type="hidden" name="action" value="addstart">

        <table cellpadding="5" cellspacing="0" border="0">


            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel</font>
                    <br/>
                    <font class="smallfont">Choose the panel you'd like to add to or <a href="/researcher/panels.jsp">create a new panel</a>.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("panelid", String.valueOf(researcherPanelsAddpeople.getPanelid()), researcherPanelsAddpeople.getPanels(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Survey to Add From</font>
                    <br/>
                    <font class="smallfont">The survey you'd like to pull respondents from.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("surveyid", String.valueOf(researcherPanelsAddpeople.getSurveyid()), researcherPanelsAddpeople.getSurveys(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Filter to Apply</font>
                    <br/>
                    <font class="smallfont">Choose a filter to pull a subset of respondents.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("respondentfilterid", String.valueOf(researcherPanelsAddpeople.getRespondentfilterid()), researcherPanelsAddpeople.getRespondentfilters(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Continue">
                    <br/>
                    <font class="tinyfont">You'll see how many people this represents and confirm in the next step.</font>
                </td>
            </tr>


        </table>

    </form>

<%@ include file="/template/footer.jsp" %>

