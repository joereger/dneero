<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveys" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveysListitem" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.util.Num" %>
<%@ page import="com.dneero.dao.Response" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Joined Conversations";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/template/auth.jsp" %>
<%
    BloggerCompletedsurveys bloggerCompletedsurveys = (BloggerCompletedsurveys) Pagez.getBeanMgr().get("BloggerCompletedsurveys");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("refreshresponsehtml")) {
        try {
            if (request.getParameter("responseid") != null && Num.isinteger(request.getParameter("responseid"))) {
                bloggerCompletedsurveys.refreshResponseHtml(Integer.parseInt(request.getParameter("responseid")));
                Response resp = Response.get(Integer.parseInt(request.getParameter("responseid")));
                Pagez.getUserSession().setMessage("'"+resp.getSurvey().getTitle()+"' refreshed.  Check it out below.");
            }
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <div class="rounded" style="background: #e6e6e6; padding: 10px;">
        <font class="formfieldnamefont" style="color: #666666">A few notes about joined conversations:</font>
        <font class="smallfont" style="color: #666666;">
            <ul>
                <li><b>You get paid for conversations when you generate clicks/displays for conversations in the days after you take it.</b>  The little squares represent days you have to generate clicks.  Underneath the squares is a summary of your status.</li>
                <li><b>Red squares</b> are days where you didn't generate enough clicks.  <b>Green squares</b> are days that you did.</li>
                <li><b>Once you can no longer generate clicks</b> to qualify for payment your squares may turn all grey.</li>
                <li><b>The square on the left represents the day that you join the conversation.</b>  Squares to the right of that square are the days after that.</li>
                <li><b>All conversation status boxes will automatically update overnight.</b>  Or, you can click the Refresh button to update whenever you'd like to check your status.</li>
                <li><b>Clicks/displays are recorded once every five minutes.</b>  Keep this in mind when you click the Refresh button.</li>
            </ul>
        </font>
    </div>
    <br/><br/>
    <%
    if (1==2 && bloggerCompletedsurveys.getList() != null && bloggerCompletedsurveys.getList().size()>0) {
        for (Iterator<BloggerCompletedsurveysListitem> iterator=bloggerCompletedsurveys.getList().iterator(); iterator.hasNext();){
            BloggerCompletedsurveysListitem bloggerCompletedsurveysListitem= iterator.next();
            %>
            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                <table cellpadding="2" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td valign="top">
                            <font class="tinyfont"><%=bloggerCompletedsurveysListitem.getResponsedate()%></font><br/>
                            <font class="normalfont" style="font-weight: bold; color: #0000ff;"><a href="/surveypostit.jsp?surveyid=<%=bloggerCompletedsurveysListitem.getSurveyid()%>"><%=bloggerCompletedsurveysListitem.getSurveytitle()%></a></font><br/>
                            <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                                <br/>
                                <font class="tinyfont" style="font-weight:bold;">
                                    <%if (bloggerCompletedsurveysListitem.getResponse().getPoststatus()==0){%>
                                        <a href="/surveypostit.jsp?surveyid=<%=bloggerCompletedsurveysListitem.getSurveyid()%>">Needs to be Posted</a>
                                    <%} else if (bloggerCompletedsurveysListitem.getResponse().getPoststatus()==1){%>
                                        Posted at Least Once
                                    <%} else if (bloggerCompletedsurveysListitem.getResponse().getPoststatus()==2){%>
                                        Posted Successfully
                                    <%} else if (bloggerCompletedsurveysListitem.getResponse().getPoststatus()==3){%>
                                        Too Late to Post
                                    <%}%>
                                </font>
                            <% } %>
                            <br/>
                            <font class="tinyfont" style="font-weight:bold; text-decoration: none;"><a href="/blogger/impressions.jsp?surveyid=<%=bloggerCompletedsurveysListitem.getSurveyid()%>">Impressions</a></font>
                        </td>
                        <td valign="top" width="225">
                            <a href="/blogger/bloggercompletedsurveys.jsp?action=refreshresponsehtml&responseid=<%=bloggerCompletedsurveysListitem.getResponse().getResponseid()%>"><font class="tinyfont">Refresh</font></a><br/>
                            <%=bloggerCompletedsurveysListitem.getResponse().getResponsestatushtml()%>
                        </td>
                    </tr>
                </table>
            </div>
            <br/>
            <%
        }
    }
    %>

    <%
        StringBuffer template = new StringBuffer();
        template.append("<div class=\"rounded\" style=\"background: #e6e6e6; padding: 10px;\">\n" +
"            <table cellpadding=\"2\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
"                <tr>\n" +
"                    <td valign=\"top\">\n" +
"                        <font class=\"tinyfont\"><$responsedate$></font><br/>\n" +
"                        <font class=\"normalfont\" style=\"font-weight: bold; color: #0000ff;\"><a href=\"/surveypostit.jsp?surveyid=<$surveyid$>\"><$surveytitle$></a></font><br/>\n" +
"                        <br/>\n" +
"                        <font class=\"tinyfont\" style=\"font-weight:bold; text-decoration: none;\"><a href=\"/blogger/impressions.jsp?surveyid=<$surveyid$>\">Impressions</a> | <a href=\"/survey.jsp?surveyid=<$surveyid$>\">Edit Answers</a></font>\n" +
"                    </td>\n" +
"                    <td valign=\"top\" width=\"225\">\n" +
"                        <a href=\"/blogger/bloggercompletedsurveys.jsp?action=refreshresponsehtml&responseid=<$response.responseid$>&page="+Pagez.getRequest().getParameter("page")+"\"><font class=\"tinyfont\">Refresh</font></a><br/>\n" +
"                        <$response.responsestatushtml$>\n" +
"                    </td>\n" +
"                </tr>\n" +
"            </table>\n" +
"        </div>");
    %>



    <%if (bloggerCompletedsurveys.getList()==null || bloggerCompletedsurveys.getList().size()==0){%>
        <font class="normalfont">You haven't yet joined any conversations!</font>
    <%} else {%>
        <%
            ArrayList<GridCol> cols = new ArrayList<GridCol>();
            cols.add(new GridCol("", template.toString(), true, "", "tinyfont", "background: #ffffff;", ""));
        %>
        <%=Grid.render(bloggerCompletedsurveys.getList(), cols, 10, "/blogger/bloggercompletedsurveys.jsp", "page")%>
    <%}%>








    <br/><br/>
    <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        Your blog posting script: Click on any conversation title to access the script that you'll use to post the conversation to your blog.  You must do this to make money on your blog traffic impressions.
        <br/><br/>
        Note: Earnings calculations are not final.   Final payment notification and calculation can be found on <a href="/account/accountbalance.jsp"><font class="smallfont">Your Account Balance</font></a> page.
        </font></div></center>
    <%}%>

<%@ include file="/template/footer.jsp" %>

