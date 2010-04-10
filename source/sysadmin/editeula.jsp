<%@ page import="com.dneero.htmluibeans.SysadminEditEula" %>
<%@ page import="com.dneero.privatelabel.ListOfPrivateLabels" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Edit EULA";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminEditEula sysadminEditEula = (SysadminEditEula)Pagez.getBeanMgr().get("SysadminEditEula");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminEditEula.setEula(Textbox.getValueFromRequest("eula", "Eula", false, DatatypeString.DATATYPEID));
            sysadminEditEula.edit();
            Pagez.getUserSession().setMessage("EULA updated.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>



        <font class="mediumfont">Be careful here... every edit... even the slightest one... causes every user to have to read and accept the new EULA the next time they log in.</font>
        <br/><br/>

    <form action="/sysadmin/editeula.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/sysadmin/editeula.jsp">
        <input type="hidden" name="action" value="choosepl">

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Private Label</font>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("plid", String.valueOf(sysadminEditEula.getPlid()), ListOfPrivateLabels.getList(), "", "")%>
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyl" value="Change PL">
                </td>
            </tr>
        </table>
    </form>




    <%if (sysadminEditEula.getPlid()>0){%>
        <br/><br/><br/>
         <form action="/sysadmin/editeula.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/sysadmin/editeula.jsp">
            <input type="hidden" name="action" value="save">
             <input type="hidden" name="plid" value="<%=sysadminEditEula.getPlid()%>">

            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Last Edited: <%=sysadminEditEula.getDate()%></font>
                    </td>
                    <td valign="top">
                        <font class="formfieldnamefont">Eulaid: <%=sysadminEditEula.getEulaid()%></font>
                    </td>
                </tr>

                <tr>
                    <td valign="top" colspan="2">
                        <%=Textarea.getHtml("eula", sysadminEditEula.getEula(), 25, 80, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top" colspan="2">
                        <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Edit the EULA and Force All Users to Re-Accept">
                    </td>
                </tr>

            </table>
    </form>
    <%}%>


<%@ include file="/template/footer.jsp" %>