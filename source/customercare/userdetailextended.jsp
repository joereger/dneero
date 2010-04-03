<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareUserDetail" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.htmluibeans.BloggerCompletedsurveysListitem" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.money.CurrentBalanceCalculator" %>
<%@ page import="com.dneero.money.SurveyMoneyStatus" %>
<%@ page import="com.dneero.htmluibeans.StaticVariables" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "User: "+((CustomercareUserDetailExtended) Pagez.getBeanMgr().get("CustomercareUserDetailExtended")).getName();
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
CustomercareUserDetailExtended customercareUserDetailExtended= (CustomercareUserDetailExtended)Pagez.getBeanMgr().get("CustomercareUserDetailExtended");
%>
<%
    if (request.getParameter("onlyshowsuccessfultransactions") != null && request.getParameter("onlyshowsuccessfultransactions").equals("1")) {
        try {
            customercareUserDetailExtended.setOnlyshowsuccessfultransactions(true);
            customercareUserDetailExtended.initBean();
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.getMessage());
        }
    }
%>
<%
    if (request.getParameter("onlyshownegativeamountbalance")!=null && request.getParameter("onlyshownegativeamountbalance").equals("1")) {
        try {
            customercareUserDetailExtended.setOnlyshownegativeamountbalance(true);
            customercareUserDetailExtended.initBean();
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.getMessage());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <a href="/customercare/userdetail.jsp?userid=<%=customercareUserDetailExtended.getUser().getUserid()%>"><font class="mediumfont">Back to <%=customercareUserDetailExtended.getUser().getNickname()%>'s Main User Screen</font></a>
        <br/><br/>




        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Account Balance (Internal Account Money Movement)</font>
            <br/>
            <a href="/customercare/userdetailextended.jsp?userid=<%=customercareUserDetailExtended.getUser().getUserid()%>&onlyshownegativeamountbalance=1"><font class="tinyfont">Only Show Negative Amts</font></a>
            <br/>
            <%if (customercareUserDetailExtended.getBalances()==null || customercareUserDetailExtended.getBalances().size()==0){%>
                <font class="normalfont">There are not yet any balance updates.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Id", "<$balanceid$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
                    cols.add(new GridCol("Description", "<$description$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Funds", "<$fundstype$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Amount", "<$amt$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Balance", "<$currentbalance$>", true, "", "tinyfont"));
                %>
                <%=Grid.render(customercareUserDetailExtended.getBalances(), cols, 50, "/customercare/userdetailextended.jsp?userid="+ customercareUserDetailExtended.getUser().getUserid()+"&onlyshownegativeamountbalance="+request.getParameter("onlyshownegativeamountbalance"), "pagetransactions")%>
            <%}%>
        </div>


        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Account Transactions (Real World Money Movement)</font>
            <br/>
            <a href="/customercare/userdetailextended.jsp?userid=<%=customercareUserDetailExtended.getUser().getUserid()%>&onlyshowsuccessfultransactions=1"><font class="tinyfont">Only Show Successful Transactions</font></a>
            <br/>
            <%if (customercareUserDetailExtended.getTransactions()==null || customercareUserDetailExtended.getTransactions().size()==0){%>
                <font class="normalfont">There are not yet any financial transactions.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Id", "<$balancetransactionid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
                    cols.add(new GridCol("Successful?", "<$issuccessful$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Desc", "<$description$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Notes", "<$notes$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Amount", "<$amt$>", false, "", "tinyfont"));
                %>
                <%=Grid.render(customercareUserDetailExtended.getTransactions(), cols, 50, "/customercare/userdetailextended.jsp?userid="+ customercareUserDetailExtended.getUser().getUserid()+"&onlyshowsuccessfultransactions="+request.getParameter("onlyshowsuccessfultransactions"), "pagetransactions")%>
            <%}%>
        </div>

        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Impressions</font>
            <br/>
            <%if (customercareUserDetailExtended.getImpressions()==null || customercareUserDetailExtended.getImpressions().size()==0){%>
                <font class="normalfont">There are not yet any impressions.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Id", "<$impressionid$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Conversation", "<$surveyid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Response", "<$responseid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Date", "<$firstseen|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
                    cols.add(new GridCol("Total Impr", "<$impressionstotal$>", false, "", "tinyfont"));
                    //cols.add(new GridCol("Impr To Be Paid", "<$impressionstobepaid$>", false, "", "tinyfont"));
                    //cols.add(new GridCol("Impr Paid", "<$impressionspaid$>", false, "", "tinyfont"));
                    //cols.add(new GridCol("Impr By Day", "<textarea rows=\"1\" cols=\"30\" style=\"font-size: 9px; border 0px solid #cccccc;\"><$impressionsbyday$></textarea>", false, "", "tinyfont"));
                    cols.add(new GridCol("Referer", "<textarea rows=\"1\" cols=\"14\" style=\"font-size: 9px; border 0px solid #cccccc;\"><$referer$></textarea>", false, "", "tinyfont"));
                %>
                <%=Grid.render(customercareUserDetailExtended.getImpressions(), cols, 50, "/customercare/userdetailextended.jsp?userid="+ customercareUserDetailExtended.getUser().getUserid(), "pageimpressions")%>
            <%}%>
        </div>

        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Completed Surveys</font>
            <br/>
                <%
                    for (Iterator<BloggerCompletedsurveysListitem> iterator=customercareUserDetailExtended.getResponses().iterator(); iterator.hasNext();){
                        BloggerCompletedsurveysListitem completedsurvey = iterator.next();
                        %>
                        <a href="/survey.jsp?surveyid=<%=completedsurvey.getSurveyid()%>"><font class="normalfont" style="font-weight: bold; color: #0000ff;"><%=completedsurvey.getSurveytitle()%></font></a>
                        <br/>
                        <font class="smallfont"><%=completedsurvey.getResponse().getResponsestatushtml()%></font><br/><br/>
                        <%
                    }
                %>
        </div>




<%@ include file="/template/footer.jsp" %>


