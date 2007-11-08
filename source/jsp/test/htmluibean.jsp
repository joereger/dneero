<%@ page import="com.dneero.htmluibeans.Test" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dneero.htmluibeans.TestGrid" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="com.dneero.htmluibeans.TestGridSubobject" %>
<%@ page import="java.util.Calendar" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "HtmlUiBean";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/jsp/templates/header.jsp" %>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            ((Test) Pagez.getBeanMgr().get("Test")).setTextbox(Textbox.getValueFromRequest("textbox", "Text Box", false, DatatypeString.DATATYPEID));
            ((Test) Pagez.getBeanMgr().get("Test")).setDropdown(Dropdown.getValueFromRequest("dropdown", "Dropdown", false));
            ((Test) Pagez.getBeanMgr().get("Test")).setTextarea(Textarea.getValueFromRequest("textarea", "Textarea", false));
            ((Test) Pagez.getBeanMgr().get("Test")).setDropdownmultiselect(DropdownMultiselect.getValueFromRequest("dropdownmultiselect", "Dropdownmultiselect", false));
            ((Test) Pagez.getBeanMgr().get("Test")).setCheckboxesvalues(Checkboxes.getValueFromRequest("checkboxes", "Checkboxes", false));
            ((Test) Pagez.getBeanMgr().get("Test")).setBooleantest(CheckboxBoolean.getValueFromRequest("booleantest"));
            ((Test) Pagez.getBeanMgr().get("Test")).setCal(DateTime.getValueFromRequest("cal", "Cal", false));
            ((Test) Pagez.getBeanMgr().get("Test")).setCal2(DateTime.getValueFromRequest("cal2", "Cal2", false));
            ((Test) Pagez.getBeanMgr().get("Test")).save();
        } catch (ValidationException vex) {
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
        ArrayList<String> checkboxvalues = ((Test)Pagez.getBeanMgr().get("Test")).getCheckboxesvalues();
        ArrayList<String> possiblevalues = new ArrayList<String>();
        possiblevalues.add("another");
        possiblevalues.add("bradley");
        possiblevalues.add("charcoal");
    %>
    <br/><%=Checkboxes.getHtml("checkboxes", checkboxvalues, possiblevalues, "", "font-size: 14px; background: #ffcc00;")%>
    <%

        ArrayList<String> ddmsv = ((Test)Pagez.getBeanMgr().get("Test")).getDropdownmultiselect();
        ArrayList<String> possibleddvalues = new ArrayList<String>();
        possibleddvalues.add("snake");
        possibleddvalues.add("donkey");
        possibleddvalues.add("arse");
        possibleddvalues.add("sdfsfd");
        possibleddvalues.add("arsssde");
        possibleddvalues.add("32d22dwe");
        possibleddvalues.add("werwe23");
    %>
    <br/><%=DropdownMultiselect.getHtml("dropdownmultiselect", ddmsv, possibleddvalues, 3, "", "font-size: 14px; background: #ffcc00;")%>
    <br/><%=CheckboxBoolean.getHtml("booleantest", ((Test)Pagez.getBeanMgr().get("Test")).getBooleantest(), "", "font-size: 14px; background: #ffcc00;")%> Is True?
    <br/><%=DateTime.getHtml("cal", ((Test)Pagez.getBeanMgr().get("Test")).getCal(), "", "font-size: 14px; background: #ffcc00;")%>
    <br/><%=Date.getHtml("cal2", ((Test)Pagez.getBeanMgr().get("Test")).getCal2(), "", "font-size: 14px; background: #ffcc00;")%>
    <br/><input type="submit" value="go">
</form>
<br/><br/>
<%

    ArrayList<TestGrid> rows=new ArrayList<TestGrid>();
    rows.add(new TestGrid(1, "Sally", "A person", 32.7688, new TestGridSubobject(Calendar.getInstance(), "34")));
    rows.add(new TestGrid(2, "Edna", "A senior citizen", 567.998, new TestGridSubobject(Calendar.getInstance(), "78")));
    rows.add(new TestGrid(3, "Pupper", "A dog", 32, new TestGridSubobject(Calendar.getInstance(), "3")));
    rows.add(new TestGrid(4, "Sallton", "A lizard", 32, new TestGridSubobject(Calendar.getInstance(), "3")));
    rows.add(new TestGrid(5, "Antonio", "Another dog", 22.67, new TestGridSubobject(Calendar.getInstance(), "3")));
    rows.add(new TestGrid(6, "gfddg", "Another dog", 22.67, new TestGridSubobject(Calendar.getInstance(), "3")));
    rows.add(new TestGrid(7, "Antddgonio", "dfh dog", 22.67, new TestGridSubobject(Calendar.getInstance(), "3")));
    rows.add(new TestGrid(8, "dfdffgd", "dfh dfhgdh", 22.67, new TestGridSubobject(Calendar.getInstance(), "3")));
    rows.add(new TestGrid(9, "Ansdffsdtdfonio", "Another dfhdfgh", 22.67, new TestGridSubobject(Calendar.getInstance(), "3")));
    rows.add(new TestGrid(10, "dfssdf", "Another dfgdfhdfh", 22.67, new TestGridSubobject(Calendar.getInstance(), "3")));
    ArrayList<GridCol> cols=new ArrayList<GridCol>();
    cols.add(new GridCol("Name", "<a href=\"htmluibean.jsp?id=<$id$>\"><$name$></a>"));
    cols.add(new GridCol("Description", "<$description$>"));
    cols.add(new GridCol("Age", "<$testGridSubobject.age$>"));
    cols.add(new GridCol("Date", "<$testGridSubobject.cal$>"));
    cols.add(new GridCol("Money", "<a href=\"htmluibean.jsp?id=<$id$>\"><$money|"+Grid.GRIDCOLRENDERER_DOUBLEASMONEY+"$></a>"));
%>
<%=Grid.render(rows, cols, 5, "htmluibean.jsp", "page")%>


<%@ include file="/jsp/templates/footer.jsp" %>