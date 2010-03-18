<%@ page import="com.dneero.dao.*" %>
<%@ page import="com.dneero.dao.hibernate.HibernateUtil" %>
<%@ page import="com.dneero.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.dneero.display.SurveyResponseParser" %>
<%@ page import="com.dneero.display.components.def.Component" %>
<%@ page import="com.dneero.rank.RankForSurveyThread" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.dneero.iptrack.Activitytype" %>
<%@ page import="com.dneero.iptrack.IptrackAnalyzer" %>
<%@ page import="com.dneero.cache.html.DbcacheexpirableCache" %>
<%@ page import="com.dneero.display.SurveyResultsDisplay" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Ip Address Tracking";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercareIptrack customercareIptrack = (CustomercareIptrack)Pagez.getBeanMgr().get("CustomercareIptrack");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("search")) {
        try {
            customercareIptrack.setSearchuserid(Textbox.getValueFromRequest("searchuserid", "Userid", false, DatatypeString.DATATYPEID));
            customercareIptrack.setSearchactivitytypeid(Dropdown.getValueFromRequest("searchactivitytypeid", "Activity Type", false));
            customercareIptrack.setSearchip(Textbox.getValueFromRequest("searchip", "Ip", false, DatatypeString.DATATYPEID));
            customercareIptrack.setSearchoctet1(Textbox.getValueFromRequest("searchoctet1", "Octet1", false, DatatypeString.DATATYPEID));
            customercareIptrack.setSearchoctet2(Textbox.getValueFromRequest("searchoctet2", "Octet2", false, DatatypeString.DATATYPEID));
            customercareIptrack.setSearchoctet3(Textbox.getValueFromRequest("searchoctet3", "Octet3", false, DatatypeString.DATATYPEID));
            customercareIptrack.setSearchoctet4(Textbox.getValueFromRequest("searchoctet4", "Octet4", false, DatatypeString.DATATYPEID));
            customercareIptrack.initBean();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>


    <%

        String qipsStr = "";
        String resultsHtmlKey = "iptrack.jsp-questionableIps";
        String group = "iptrack.jsp-questionableIps";
        Object fromCache = DbcacheexpirableCache.get(resultsHtmlKey, group);
        if (fromCache!=null){
            try{qipsStr = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
        } else {
            StringBuffer qips = new StringBuffer();
            HashMap<String, Integer> ips = IptrackAnalyzer.analyze(2);
            Iterator keyValuePairs = ips.entrySet().iterator();
            for (int i = 0; i < ips.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String ip = (String)mapentry.getKey();
                Integer count = (Integer)mapentry.getValue();
                qips.append("<a href=\"/customercare/iptrack.jsp?action=search&searchip="+ip+"\">");
                qips.append(ip+"("+count+" userids)");
                qips.append("</a>");
                if (keyValuePairs.hasNext()){
                    qips.append(", ");
                }
            }
            qipsStr = qips.toString();
            DbcacheexpirableCache.put(resultsHtmlKey, group, qipsStr, Time.xMinutesAgoEnd(Calendar.getInstance(), -90).getTime());
        }
    %>
        <div class="rounded" style="padding: 15px; margin: 8px; background: #BFFFBF;">
            <font class="normalfont" style="font-weight:bold;">Questionable IPs</font>
            <br/>
            <font class="tinyfont">
            Refreshes every 90 min.
            </font>
            <br/>
            <font class="tinyfont">
            <%=qipsStr%>
            </font>
            <br/>
            <font class="tinyfont">
            We expect a single userid to be accessed from many ip addresses.  What we don't necessarily expect is to see multiple userids accessed from a single ip address.  It could be a proxy with multiple users behind it.  Or it could be a person using multiple accounts.
            </font>
        </div>
        <br/>

    <%
    if (request.getParameter("searchuserid")!=null && Num.isinteger(request.getParameter("searchuserid"))){
        User userEx = User.get(Integer.parseInt(request.getParameter("searchuserid")));
        if (userEx!=null && userEx.getUserid()>0){
            %>
            <div class="rounded" style="padding: 15px; margin: 8px; background: #e6e6e6;">
                <font class="normalfont" style="font-weight:bold;"><%=userEx.getFirstname()%> <%=userEx.getLastname()%></font>
                <br/>
                <font class="tinyfont">
                <%=userEx.getEmail()%>
                <br/>
                <a href="/customercare/userdetail.jsp?userid=<%=userEx.getUserid()%>">Go To User Screen</a>
                </font>
            </div>
            <br/>
            <%
        }
    }
    %>


    <form action="/customercare/iptrack.jsp" method="post" class="niceform">
        <input type="hidden" name="dpage" value="/customercare/iptrack.jsp">
        <input type="hidden" name="action" value="search">
        
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    <font class="tinyfont">Userid</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Activity</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Ip</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Octet1</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Octet2</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Octet3</font>
                </td>
                <td valign="top">
                    <font class="tinyfont">Octet4</font>
                </td>
                <td valign="top">

                </td>
            </tr>
            <tr>
                <td valign="top">
                    <%=Textbox.getHtml("searchuserid", customercareIptrack.getSearchuserid(), 255, 5, "", "")%>
                </td>
                <td valign="top">
                    <%=Dropdown.getHtml("searchactivitytypeid", customercareIptrack.getSearchactivitytypeid(), Activitytype.getTypes(), "","")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchip", customercareIptrack.getSearchip(), 255, 15, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchoctet1", customercareIptrack.getSearchoctet1(), 255, 3, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchoctet2", customercareIptrack.getSearchoctet2(), 255, 3, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchoctet3", customercareIptrack.getSearchoctet3(), 255, 3, "", "")%>
                </td>
                <td valign="top">
                    <%=Textbox.getHtml("searchoctet4", customercareIptrack.getSearchoctet4(), 255, 3, "", "")%>
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton sexybutton sexysimple sexyxxl" value="Search">
                </td>
            </tr>
        </table>
    </form>

        <br/>



        <%if (customercareIptrack.getIptracks()==null || customercareIptrack.getIptracks().size()==0){%>
            <font class="normalfont">No Ip Tracks!</font>
        <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("Date", "<$datetime|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                //cols.add(new GridCol("User Detail", "<a href=\"/customercare/userdetail.jsp?userid=<$userid$>\">View User</a>", false, "", "tinyfont"));
                cols.add(new GridCol("Userid", "<a href=\"/customercare/iptrack.jsp?action=search&searchuserid=<$userid$>\">Userid=<$userid$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Ip", "<a href=\"/customercare/iptrack.jsp?action=search&searchip=<$ip$>\"><$ip$></a>", false, "", "tinyfont"));
                cols.add(new GridCol("Activity", "<$description$>", false, "", "tinyfont"));
                cols.add(new GridCol(" ", "<a href=\"/customercare/iptrack.jsp?action=search&searchoctet1=<$octet1$>&searchoctet2=<$octet2$>&searchoctet3=<$octet3$>\">Zoom Subnet <$octet1$>.<$octet2$>.<$octet3$>.* </a>", false, "", "tinyfont"));
            %>
            <%=Grid.render(customercareIptrack.getIptracks(), cols, 200, "/customercare/iptrack.jsp", "page")%>
        <%}%>






<%@ include file="/template/footer.jsp" %>



