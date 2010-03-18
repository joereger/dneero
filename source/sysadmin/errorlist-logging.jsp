<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="com.dneero.startup.Log4jLevels" %>
<%@ page import="org.apache.log4j.Level" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "SysLog";
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            Errorlogging errorlogging = new Errorlogging();
            errorlogging.setClassname(request.getParameter("classname"));
            errorlogging.setLevel(request.getParameter("level"));
            errorlogging.save();
            Log4jLevels.setLevels();
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("delete")) {
        try {
            if (request.getParameter("errorloggingid")!=null && Num.isinteger(request.getParameter("errorloggingid"))){
                Errorlogging errorlogging = Errorlogging.get(Integer.parseInt(request.getParameter("errorloggingid")));
                Logger lgr = Logger.getLogger(errorlogging.getClassname());
                lgr.setLevel(Level.ERROR);
                try{errorlogging.delete();}catch(Exception ex){logger.error("",ex);}

                Log4jLevels.setLevels();
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <form action="/sysadmin/errorlist-logging.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/sysadmin/errorlist-logging.jsp">
            <input type="hidden" name="action" value="save">
            <%=Textbox.getHtml("classname", request.getParameter("classname"), 250, 50, "", "")%>
            <%
            TreeMap<String, String> levels = new TreeMap<String, String>();
            levels.put(String.valueOf("DEBUG"), "DEBUG");
            levels.put(String.valueOf("INFO"), "INFO");
            levels.put(String.valueOf("WARN"), "WARN");
            levels.put(String.valueOf("ERROR"), "ERROR");
            levels.put(String.valueOf("FATAL"), "FATAL");
            levels.put(String.valueOf("ALL"), "ALL");
            levels.put(String.valueOf("OFF"), "OFF");
            %>
            <%=Dropdown.getHtml("level", request.getParameter("level"), levels, "", "")%>
            <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Save">
        </form>

 <br/>
 <%
 List errorloggings = HibernateUtil.getSession().createQuery("from Errorlogging").setMaxResults(2500).list();
 %>
<%if (errorloggings==null || errorloggings.size()==0){%>
    <font class="normalfont">None found.</font>
<%} else {%>
    <%
        ArrayList<GridCol> cols=new ArrayList<GridCol>();
        cols.add(new GridCol("Delete?", "<a href=\"/sysadmin/errorlist-logging.jsp?action=delete&errorloggingid=<$errorloggingid$>\">delete</a>", false, "", "tinyfont"));
        cols.add(new GridCol("Id", "<$errorloggingid$>", false, "", "smallfont"));
        cols.add(new GridCol("Classname", "<$classname$>", false, "", "tinyfont"));
        cols.add(new GridCol("Level", "<$level$>", false, "", "smallfont"));
    %>
    <%=Grid.render(errorloggings, cols, 250, "/sysadmin/errorlist-logging.jsp", "page")%>
<%}%>








<%@ include file="/template/footer.jsp" %>



