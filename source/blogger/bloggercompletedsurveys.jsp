<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveys" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveysListitem" %>
<%@ page import="java.util.Iterator" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Earnings from Completed Surveys";
String navtab = "bloggers";
String acl = "blogger";
%>
<%@ include file="/template/auth.jsp" %>
<%
    BloggerCompletedsurveys bloggerCompletedsurveys = (BloggerCompletedsurveys) Pagez.getBeanMgr().get("BloggerCompletedsurveys");
%>
<%@ include file="/template/header.jsp" %>


    <br/><br/>
    <%
    if (bloggerCompletedsurveys.getList() != null && bloggerCompletedsurveys.getList().size()>0) {
        for (Iterator<BloggerCompletedsurveysListitem> iterator=bloggerCompletedsurveys.getList().iterator(); iterator.hasNext();){
            BloggerCompletedsurveysListitem bloggerCompletedsurveysListitem= iterator.next();

            %>
            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                <table cellpadding="2" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td valign="top">
                            <font class="tinyfont"><%=bloggerCompletedsurveysListitem.getResponsedate()%></font><br/>
                            <font class="normalfont" style="font-weight: bold; color: #0000ff;"><a href="/survey.jsp?surveyid=<%=bloggerCompletedsurveysListitem.getSurveyid()%>"><%=bloggerCompletedsurveysListitem.getSurveytitle()%></a></font><br/>
                            <font class="tinyfont" style="font-weight: bold;">Est earnings: <%=bloggerCompletedsurveysListitem.getAmttotal()%></font>
                            <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
                                <br/>
                                <font class="tinyfont" style="font-weight:bold;">
                                    <%if (bloggerCompletedsurveysListitem.getResponse().getPoststatus()==0){%>
                                        <a href="/survey.jsp?surveyid=<%=bloggerCompletedsurveysListitem.getSurveyid()%>">Needs to be Posted</a>
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
                            <font class="tinyfont" style="font-weight:bold; text-decoration: none;"><a href="/blogger/impressions.jsp?surveyid=<%=bloggerCompletedsurveysListitem.getSurveyid()%>"><%=bloggerCompletedsurveysListitem.getTotalimpressions()%></a> impressions</font>
                        </td>
                        <td valign="top" align="right">
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


    <br/><br/>
    <% if (!Pagez.getUserSession().getIsfacebookui()){ %>
        <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="smallfont">
        <img src="/images/lightbulb_on.png" alt="" align="right"/>
        Your blog posting script: Click on any survey title to access the script that you'll use to post the survey to your blog.  You must do this to make money on your blog traffic impressions.
        <br/><br/>
        Note: Earnings calculations are not final.   Final payment notification and calculation can be found on <a href="/account/accountbalance.jsp"><font class="smallfont">Your Account Balance</font></a> page.
        </font></div></center>
    <%}%>

<%@ include file="/template/footer.jsp" %>

