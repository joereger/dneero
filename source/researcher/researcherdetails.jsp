<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.ResearcherDetails" %>
<%@ page import="com.dneero.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Researcher Profile";
String navtab = "researchers";
String acl = "account";
%>
<%@ include file="/jsp/templates/auth.jsp" %>
<%
    ResearcherDetails researcherDetails=(ResearcherDetails) Pagez.getBeanMgr().get("ResearcherDetails");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            researcherDetails.setCompanyname(Textbox.getValueFromRequest("companyname", "Name", true, DatatypeString.DATATYPEID));
            researcherDetails.setCompanytype(Dropdown.getValueFromRequest("companytype", "Type", false));
            researcherDetails.setPhone(Textbox.getValueFromRequest("phone", "Phone", false, DatatypeString.DATATYPEID));
            researcherDetails.saveAction();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/jsp/templates/header.jsp" %>



    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getResearcherid()==0){%>
        <div class="rounded" style="padding: 15px; margin: 5px; background: #F2FFBF;">
            <font class="mediumfont">One-time Researcher Configuration Step 1 of 1: Profile Setup</font>
            <br/>
            <font class="smallfont">We must take your profile so that we can provide you the highest level of service.  This is a one-time step.  You can edit your answers later on.</font>
        </div>
    <%}%>


    <form action="researcherdetails.jsp" method="post">
        <input type="hidden" name="action" value="save">


            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Name</font>
                        <br/>
                        <font class="tinyfont">Could be company name.  Just something to identify your research or marketing organization.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("companyname", researcherDetails.getCompanyname(), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Type</font>
                        <br/>
                        <font class="tinyfont">Choose the option that best describes you/your organization.</font>
                    </td>
                    <td valign="top">
                        <%=Dropdown.getHtml("companytype",researcherDetails.getCompanytype(), researcherDetails.getCompanytypes(), "","")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Phone (Optional)</font>
                        <br/>
                        <font class="tinyfont">So we can contact you regarding<br/>billing and survey issues.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("phone", researcherDetails.getPhone(), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" value="Save Researcher Profile">
                    </td>
                </tr>

            </table>

        </form>



<%@ include file="/jsp/templates/footer.jsp" %>

