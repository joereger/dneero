<%@ page import="com.dneero.cache.providers.CacheFactory" %>
<%@ page import="com.dneero.dao.Pl" %>
<%@ page import="com.dneero.privatelabel.PlFinder" %>
<%@ page import="com.dneero.privatelabel.PlVerification" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Private Label Demographic Fields";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>

<%
    Pl pl = new Pl();
    if (request.getParameter("plid")!=null && Num.isinteger(request.getParameter("plid"))){
        pl = Pl.get(Integer.parseInt(request.getParameter("plid")));
    } else {
        Pagez.getUserSession().setMessage("plid wasn't set");
        Pagez.sendRedirect("/sysadmin/privatelabels.jsp");
        return;
    }
    if (pl==null || pl.getPlid()==0){
        Pagez.getUserSession().setMessage("pl==null or plid==0");
        Pagez.sendRedirect("/sysadmin/privatelabels.jsp");
        return;
    }
%>

<%
    Demographic demographic = new Demographic();
    if (request.getParameter("demographicid")!=null && Num.isinteger(request.getParameter("demographicid"))){
        demographic = Demographic.get(Integer.parseInt(request.getParameter("demographicid")));
    }
%>

<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("save")){
                demographic.setPlid(pl.getPlid());
                demographic.setName(Textbox.getValueFromRequest("name", "Name", true, DatatypeString.DATATYPEID));
                demographic.setDescription(Textbox.getValueFromRequest("description", "Description", false, DatatypeString.DATATYPEID));
                demographic.setPossiblevalues(Textarea.getValueFromRequest("possiblevalues", "Possible Values", false));
                demographic.setOrdernum(Textbox.getIntFromRequest("ordernum", "Order", true, DatatypeInteger.DATATYPEID));
                demographic.setIsrequired(CheckboxBoolean.getValueFromRequest("isrequired"));
                demographic.setType(Dropdown.getIntFromRequest("type", "Type", true));
                demographic.save();
                Pagez.getUserSession().setMessage("Saved!");
                Pagez.sendRedirect("/sysadmin/privatelabel_demographics.jsp?plid="+pl.getPlid());
                return;
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%
    if (request.getParameter("action") != null) {
        try {
            if (request.getParameter("action").equals("delete")){
                demographic.delete();
                Pagez.getUserSession().setMessage("Deleted.");
                Pagez.sendRedirect("/sysadmin/privatelabel_demographics.jsp?plid="+pl.getPlid());
                return;
            }
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.toString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <font class="largefont"><%=pl.getName()%></font>
        <br/><br/>

        <form action="/sysadmin/privatelabel_demographics_edit.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/sysadmin/privatelabel_demographics_edit.jsp">
            <input type="hidden" name="action" value="save" id="action">
            <input type="hidden" name="plid" value="<%=pl.getPlid()%>">
            <input type="hidden" name="demographicid" value="<%=demographic.getDemographicid()%>">

            <table cellpadding="3" cellspacing="0" border="0">
                <tr>
                    <td valign="top" width="33%">
                        <font class="formfieldnamefont">Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("name", demographic.getName(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Description</font>
                        <br/><font class="tinyfont">Users will see this description.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("description", demographic.getDescription(), 255, 35, "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top" colspan="2">
                        <font class="formfieldnamefont">Possible Values</font>
                        <br/><font class="tinyfont">(each on its own line)</font>
                        <br/>
                        <%=Textarea.getHtml("possiblevalues", demographic.getPossiblevalues(), 24, 80, "", "width: 100%;")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Order</font>
                        <br/><font class="tinyfont">Lower numbers appear first</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("ordernum", String.valueOf(demographic.getOrdernum()), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Required?</font>
                        <br/><font class="tinyfont">Are survey respondents required to answer this question?  Note that if you provide an "Other" or "NA" answer in the list of possible values you can require here but not put a massive burden on users.</font>
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("isrequired", demographic.getIsrequired(), "", "")%>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Type</font>
                        <br/><font class="tinyfont">How will the user answer?  If you want them to select from a list of options, choose Select Dropdown.  If you want them to type something freeform, choose Text Box.</font>
                    </td>
                    <td valign="top">
                        <%
                        TreeMap<String, String> typesToSelectFrom = new TreeMap<String, String>();
                        typesToSelectFrom.put(String.valueOf(Demographic.TYPE_SELECT), "Select Dropdown");
                        typesToSelectFrom.put(String.valueOf(Demographic.TYPE_TEXT), "Text Box");
                        %>
                        <%=Dropdown.getHtml("type", String.valueOf(demographic.getType()), typesToSelectFrom, "", "")%>
                    </td>
                </tr>



             </table>
             <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save"><br/>
             <br/><br/><br/><br/>
             <a href="/sysadmin/privatelabel_demographics_edit.jsp?action=delete&plid=<%=pl.getPlid()%>&demographicid=<%=demographic.getDemographicid()%>"><font class="tinyfont">Delete</font></a>



        </form>



<%@ include file="/template/footer.jsp" %>



