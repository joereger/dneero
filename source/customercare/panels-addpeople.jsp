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
CustomercarePanelsAddpeople customercarePanelsAddpeople= (CustomercarePanelsAddpeople)Pagez.getBeanMgr().get("CustomercarePanelsAddpeople");
%>
<%
if (customercarePanelsAddpeople.getPanelid()<=0){
    Pagez.getUserSession().setMessage("Please choose which panel to add people to.");   
}
%>
<%@ include file="/template/header.jsp" %>

<br/><br/>
<%if (request.getParameter("showonly")==null || (request.getParameter("showonly")!=null && request.getParameter("showonly").equals("addbypanel"))){%>
<div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
    <font class="mediumfont">Add People from Another Panel</font>
    <br/>
    <form action="/customercare/panels-addpeoplebypanelconfirm.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/customercare/panels-addpeoplebypanelconfirm.jsp">
        <input type="hidden" name="action" value="addstartpanel">


        <table cellpadding="5" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel</font>
                    <br/>
                    <font class="smallfont">Choose the panel you'd like to add to or <a href="/researcher/panels.jsp">create a new panel</a>.</font>
                </td>
                <td valign="top" width="60%">
                    <%=Dropdown.getHtml("panelid", String.valueOf(customercarePanelsAddpeople.getPanelid()), customercarePanelsAddpeople.getPanels(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel to Add From</font>
                    <br/>
                    <font class="smallfont">People from this panel will be added to the panel you've chosen above.</font>
                </td>
                <td valign="top" width="60%">
                    <%=Dropdown.getHtml("panelidtoaddfrom", String.valueOf(customercarePanelsAddpeople.getPanelidtoaddfrom()), customercarePanelsAddpeople.getPanelsToAddFrom(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Filter to Apply</font>
                    <br/>
                    <font class="smallfont">Choose a filter to pull a subset of participants.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("respondentfilterid", String.valueOf(customercarePanelsAddpeople.getRespondentfilterid()), customercarePanelsAddpeople.getRespondentfilters(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Continue">
                    <br/>
                    <font class="tinyfont">You'll see how many people this represents and confirm in the next step.</font>
                </td>
            </tr>


        </table>

    </form>
</div>
<%}%>

<%if (request.getParameter("showonly")==null || (request.getParameter("showonly")!=null && request.getParameter("showonly").equals("addbysurvey"))){%>
<div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
    <font class="mediumfont">Add By Conversation Participants</font>
    <br/>
    <form action="/customercare/panels-addpeopleconfirm.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/customercare/panels-addpeopleconfirm.jsp">
        <input type="hidden" name="action" value="addstart">


        <table cellpadding="5" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel</font>
                    <br/>
                    <font class="smallfont">Choose the panel you'd like to add to or <a href="/researcher/panels.jsp">create a new panel</a>.</font>
                </td>
                <td valign="top" width="60%">
                    <%=Dropdown.getHtml("panelid", String.valueOf(customercarePanelsAddpeople.getPanelid()), customercarePanelsAddpeople.getPanels(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Conversation to Add From</font>
                    <br/>
                    <font class="smallfont">The conversation you'd like to pull respondents from.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("surveyid", String.valueOf(customercarePanelsAddpeople.getSurveyid()), customercarePanelsAddpeople.getSurveys(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Filter to Apply</font>
                    <br/>
                    <font class="smallfont">Choose a filter to pull a subset of participants.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("respondentfilterid", String.valueOf(customercarePanelsAddpeople.getRespondentfilterid()), customercarePanelsAddpeople.getRespondentfilters(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Continue">
                    <br/>
                    <font class="tinyfont">You'll see how many people this represents and confirm in the next step.</font>
                </td>
            </tr>


        </table>

    </form>
</div>
<%}%>

<%if (request.getParameter("showonly")==null || (request.getParameter("showonly")!=null && request.getParameter("showonly").equals("addbyrankingpercent"))){%>
<div class="rounded" style="padding: 15px; margin: 5px; background: #e6e6e6;">
    <font class="mediumfont">Add By Ranking Percentage</font>
    <br/>
    <form action="/customercare/panels-addpeoplebyrankconfirm.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/customercare/panels-addpeoplebyrankconfirm.jsp">
        <input type="hidden" name="action" value="addstartrank">


        <table cellpadding="5" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Panel</font>
                    <br/>
                    <font class="smallfont">Choose the panel you'd like to add to or <a href="/customercare/panels.jsp">create a new panel</a>.</font>
                </td>
                <td valign="top" width="60%">
                    <%=Dropdown.getHtml("panelid", String.valueOf(customercarePanelsAddpeople.getPanelid()), customercarePanelsAddpeople.getPanels(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Ranking</font>
                    <br/>
                    <font class="smallfont">Choose the Ranking you'd like to use to add.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("rankid", String.valueOf(customercarePanelsAddpeople.getRankid()), customercarePanelsAddpeople.getRanks(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Top X% of Ranked People</font>
                    <br/>
                    <font class="smallfont">Use this to only pull the top X% of people who're ranked.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("rankpercentofatleast", String.valueOf(customercarePanelsAddpeople.getRankpercentofatleast()), customercarePanelsAddpeople.getRankpercentofatleastOptions(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Filter to Apply</font>
                    <br/>
                    <font class="smallfont">Choose a filter to pull a subset of respondents.</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("respondentfilterid", String.valueOf(customercarePanelsAddpeople.getRespondentfilterid()), customercarePanelsAddpeople.getRespondentfilters(), "", "")%>
                </td>
            </tr>
            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Continue">
                    <br/>
                    <font class="tinyfont">You'll see how many people this represents and confirm in the next step.</font>
                </td>
            </tr>


        </table>

    </form>
</div>
<%}%>

<%@ include file="/template/footer.jsp" %>

