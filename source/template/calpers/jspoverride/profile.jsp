<%@ page import="com.dneero.htmlui.Dropdown" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmlui.ValidationException" %>
<%@ page import="com.dneero.htmluibeans.PublicProfile" %>
<%@ page import="com.dneero.privatelabel.PlUtil" %>
<%@ page import="java.util.ArrayList" %>
<%
    PublicProfile publicProfile=(PublicProfile) Pagez.getBeanMgr().get("PublicProfile");
%>
<%
if (publicProfile==null || publicProfile.getUser()==null || publicProfile.getUser().getUserid()==0 || !publicProfile.getUser().getIsenabled()){

    Pagez.getUserSession().setMessage("profile.jsp");
    Pagez.getUserSession().setMessage("request.getParameter(\"userid\")="+request.getParameter("userid"));
    if (publicProfile==null){
        Pagez.getUserSession().setMessage("publicProfile==null");
    } else if (publicProfile.getUser()==null){
        Pagez.getUserSession().setMessage("publicProfile.getUser()==null");
    } else if (publicProfile.getUser().getUserid()==0){
        Pagez.getUserSession().setMessage("publicProfile.getUser().getUserid()==0");
    } else if (!publicProfile.getUser().getIsenabled()){
        Pagez.getUserSession().setMessage("!publicProfile.getUser().getIsenabled()");
    } else {
        Pagez.getUserSession().setMessage("else another issue caused it");
    }

    Pagez.sendRedirect("/notauthorized.jsp");
    return;
}
%>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = publicProfile.getUser().getNickname();
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("add")) {
        try {
            publicProfile.setPanelid(Dropdown.getIntFromRequest("panelid", "Panel", true));
            publicProfile.addToPanel();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addsuperpanel")) {
        try {
            publicProfile.setSuperpanelid(Dropdown.getIntFromRequest("superpanelid", "SuperPanel", true));
            publicProfile.addToSuperPanel();
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <%if (publicProfile.getMsg()!=null && !publicProfile.getMsg().equals("")){%>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <%=publicProfile.getMsg()%>
        </font></div></center>
        <br/><br/>
    <%}%>

    <div class="rounded" style="background: #e6e6e6; text-align: left; padding: 15px;">
    <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <tr>
            <td valign="top" width="50%">

            </td>
            <td valign="top">
                <div class="rounded" style="background: #ffffff; text-align: left; padding: 15px;">
                    <table cellpadding="2" cellspacing="0" border="0" width="100%">

                        <tr>
                            <td valign="top" align="right">
                                <font class="formfieldnamefont">Joined</font>
                            </td>
                            <td valign="top">
                                <font class="smallfont"><%=Time.agoText(Time.getCalFromDate(publicProfile.getUser().getCreatedate()))%></font>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" align="right">
                                <font class="formfieldnamefont">Convos Joined</font>
                            </td>
                            <td valign="top">
                                <font class="smallfont"><%=publicProfile.getConvosjoined()%></font>
                            </td>
                        </tr>

                    </table>
                </div>
            </td>
          </tr>
       </table>
       </div>

        <font class="normalfont">
            <ul>
               <li>Below you see all the conversations that the named user has joined.</li>
               <li>Click on'[name] Answers' to see his/her answers for any of the conversations listed.</li>
               <li>Click on 'Results' to see "All Users' Answers" for the listed conversation.</li>
            </ul>
        </font>

       <br/><br/>
       <font class="mediumfont" style="color: #cccccc;"><%=Pagez._Surveys()%> <%=publicProfile.getUser().getNickname()%> Joined</font>
       <br/>
       <%if (publicProfile.getListitems()==null || publicProfile.getListitems().size()==0){%>
            <font class="normalfont">None... yet.</font>
       <%} else {%>
            <%
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                //cols.add(new GridCol("Date", "<$response.responsedate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont"));
                cols.add(new GridCol("", "<font class=\"normalfont\"><b><$survey.title$></b></font><br/><font class=\"tinyfont\">"+publicProfile.getUser().getNickname()+" Asked: <$userquestion.question$></font><br/>", false, "", ""));
                cols.add(new GridCol("", "<a href=\"/survey.jsp?s=<$response.surveyid$>&u="+publicProfile.getUser().getUserid()+"\">"+publicProfile.getUser().getNickname()+"'s Answers</a>", false, "", "smallfont"));
                cols.add(new GridCol("", "<a href=\"/surveyresults.jsp?s=<$response.surveyid$>\">Results</a>", false, "", "smallfont"));
                //cols.add(new GridCol("", "<a href=\"/profileimpressions.jsp?responseid=<$response.responseid$>\">Impressions</a>", false, "", "smallfont"));
            %>
            <%=Grid.render(publicProfile.getListitems(), cols, 50, "/profile.jsp?userid="+publicProfile.getUser().getUserid(), "pagesurveys")%>
        <%}%>



        
    


<%@ include file="/template/footer.jsp" %>