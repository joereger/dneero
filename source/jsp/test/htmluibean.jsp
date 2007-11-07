<%@ page import="com.dneero.htmluibeans.Test" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="java.util.ArrayList" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "HtmlUiBean";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("save")){
    try{
        ((Test)Pagez.getBeanMgr().get("Test")).setTextbox(Textbox.getValueFromRequest("textbox", "Text Box", false, DatatypeString.DATATYPEID));
        ((Test)Pagez.getBeanMgr().get("Test")).setDropdown(Dropdown.getValueFromRequest("dropdown", "Dropdown", false));
        ((Test)Pagez.getBeanMgr().get("Test")).setTextarea(Textarea.getValueFromRequest("textarea", "Textarea", false));
        ((Test)Pagez.getBeanMgr().get("Test")).setCheckboxesvalues(Checkboxes.getValueFromRequest("checkboxes", "Checkboxes", false));
        ((Test)Pagez.getBeanMgr().get("Test")).save();
    } catch (ValidationException vex){
        %>
        There's been an error: <%=vex.getErrorsAsSingleString()%>
        <%
    }
}
%>

<form action="" method="get">
    <input type="hidden" name="action" value="save">
    <br/><%=Textbox.getHtml("textbox", ((Test)Pagez.getBeanMgr().get("Test")).getTextbox(), 255, 35, "", "font-size: 14px; background: #ffcc00;")%>
    <%
        ArrayList<String> options = new ArrayList<String>();
        options.add("a");
        options.add("b");
        options.add("c");
    %>
    <br/><%=Dropdown.getHtml("dropdown",((Test)Pagez.getBeanMgr().get("Test")).getDropdown(), options, "","font-size: 14px; background: #ffcc00;")%>
    <br/><%=Textarea.getHtml("textarea", ((Test)Pagez.getBeanMgr().get("Test")).getTextarea(), 3, 35, "", "font-size: 14px; background: #ffcc00;")%>
    <%
        ArrayList<String> possiblevalues = new ArrayList<String>();
        ArrayList<String> checkboxvalues = ((Test)Pagez.getBeanMgr().get("Test")).getCheckboxesvalues();
        possiblevalues.add("another");
        possiblevalues.add("bradley");
        possiblevalues.add("charcoal");
    %>
    <br/><%=Checkboxes.getHtml("checkboxes", checkboxvalues, possiblevalues, "", "font-size: 14px; background: #ffcc00;")%>
    <br/><input type="submit" value="go">
</form>
<br/><br/>
hasbeeninitialized=<%=((Test) Pagez.getBeanMgr().get("Test")).getHasbeeninitialized()%>
<br/><br/>
Pagez.getUserSession().getPendingSurveyResponseAsString()=<%=Pagez.getUserSession().getPendingSurveyResponseAsString()%>

<%@ include file="/jsp/templates/footer.jsp" %>