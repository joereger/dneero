<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.SysadminUserDetail" %>
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
String pagetitle = "User: "+((SysadminUserDetail) Pagez.getBeanMgr().get("SysadminUserDetail")).getEmail();
String navtab = "sysadmin";
String acl = "sysadmin";
%>
<%@ include file="/template/auth.jsp" %>
<%
SysadminUserDetail sysadminUserDetail = (SysadminUserDetail)Pagez.getBeanMgr().get("SysadminUserDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            sysadminUserDetail.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setEmail(Textbox.getValueFromRequest("email", "Email", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.setPaypaladdress(Textbox.getValueFromRequest("paypaladdress", "PayPal Address", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.setReferredbyuserid(Textbox.getIntFromRequest("referredbyuserid", "Referredbyuserid", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.setFacebookuid(Textbox.getValueFromRequest("facebookuserid", "Facebookuserid", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.save();
            Pagez.getUserSession().setMessage("User details saved");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("togglesysadmin")) {
        try {
            sysadminUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.togglesysadminprivs();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("updateresellerpercent")) {
        try {
            sysadminUserDetail.setResellerpercent(Textbox.getDblFromRequest("resellerpercent", "Reseller Percent", true, DatatypeDouble.DATATYPEID));
            sysadminUserDetail.updateresellerpercent();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deleteuser")) {
        try {
            sysadminUserDetail.setActivitypin(Textbox.getValueFromRequest("activitypin", "Activity Pin", false, DatatypeString.DATATYPEID));
            sysadminUserDetail.deleteuser();
            Pagez.getUserSession().setMessage("User deleted");
            Pagez.sendRedirect("/sysadmin/userlist.jsp");
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("toggleisenabled")) {
        try {
            sysadminUserDetail.toggleisenabled();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("giveusermoney")) {
        try {
            sysadminUserDetail.setAmt(Textbox.getDblFromRequest("amt", "Amount", true, DatatypeDouble.DATATYPEID));
            sysadminUserDetail.setReason(Textbox.getValueFromRequest("reason", "Reason", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setFundstype(Dropdown.getIntFromRequest("fundstype", "Funds Type", true));
            sysadminUserDetail.giveusermoney();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("takeusermoney")) {
        try {
            sysadminUserDetail.setAmt(Textbox.getDblFromRequest("amt", "Amount", true, DatatypeDouble.DATATYPEID));
            sysadminUserDetail.setReason(Textbox.getValueFromRequest("reason", "Reason", true, DatatypeString.DATATYPEID));
            sysadminUserDetail.setFundstype(Dropdown.getIntFromRequest("fundstype", "Funds Type", true));
            sysadminUserDetail.takeusermoney();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("passwordresetemail")) {
        try {
            sysadminUserDetail.sendresetpasswordemail();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("reactivationemail")) {
        try {
            sysadminUserDetail.reactivatebyemail();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("researcherremainingbalanceoperations")) {
        try {
            sysadminUserDetail.runResearcherRemainingBalanceOperations();
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("onlyshowsuccessfultransactions") != null && request.getParameter("onlyshowsuccessfultransactions").equals("1")) {
        try {
            sysadminUserDetail.setOnlyshowsuccessfultransactions(true);
            sysadminUserDetail.initBean();
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.getMessage());
        }
    }
%>
<%
    if (request.getParameter("onlyshownegativeamountbalance")!=null && request.getParameter("onlyshownegativeamountbalance").equals("1")) {
        try {
            sysadminUserDetail.setOnlyshownegativeamountbalance(true);
            sysadminUserDetail.initBean();
        } catch (Exception vex) {
            Pagez.getUserSession().setMessage(vex.getMessage());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

        <%
            CurrentBalanceCalculator cbc=new CurrentBalanceCalculator(sysadminUserDetail.getUser());
        %>

        <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top" width="50%">

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">First Name</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("firstname", sysadminUserDetail.getFirstname(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Last Name</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("lastname", sysadminUserDetail.getLastname(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Email</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("email", sysadminUserDetail.getEmail(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">PayPal Address</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("paypaladdress", sysadminUserDetail.getPaypaladdress(), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Referredbyuserid</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("referredbyuserid", String.valueOf(sysadminUserDetail.getReferredbyuserid()), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Facebook uid</font>
                                        </td>
                                        <td valign="top">
                                            <%=Textbox.getHtml("facebookuserid", String.valueOf(sysadminUserDetail.getFacebookuid()), 255, 35, "", "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Reseller Code</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=sysadminUserDetail.getUser().getResellercode()%></font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Reseller Percent</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont"><%=sysadminUserDetail.getUser().getResellerpercent()%>%</font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Currentbalance</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont">$<%=cbc.getCurrentbalance()%></font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Currentbalanceresearcher</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont">$<%=cbc.getCurrentbalanceresearcher()%></font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">
                                            <font class="formfieldnamefont">Currentbalanceblogger</font>
                                        </td>
                                        <td valign="top">
                                            <font class="smallfont">$<%=cbc.getCurrentbalanceblogger()%></font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top">

                                        </td>
                                        <td valign="top">
                                            <input type="submit" class="formsubmitbutton" value="Save User Details">
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="passwordresetemail">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="Send Password Reset Email">
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="reactivationemail">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="Force Re-Activation By Email">
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="researcherremainingbalanceoperations">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <input type="submit" class="formsubmitbutton" value="ResearcherRemainingBalanceOperations">
                            </form>
                            <br/>
                            <font class="tinyfont">This will process account balances, remaining impressions, credit card transfers, etc for only this account.  Only does something if this user has a researcher record.</font>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="togglesysadmin">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <%if (sysadminUserDetail.getIssysadmin()){%>
                                    <font class="mediumfont">User is a Sysadmin.</font>
                                <%} else {%>
                                    <font class="mediumfont">User is not a Sysadmin.</font>
                                <%}%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Toggle Sysadmin Privileges">
                                <%=Textbox.getHtml("activitypin", String.valueOf(sysadminUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                        </div>
                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="deleteuser">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Delete User">
                                <%=Textbox.getHtml("activitypin", String.valueOf(sysadminUserDetail.getActivitypin()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">You must type "yes, i want to do this" in the box to make this happen</font>
                            </form>
                        </div>
                    </td>
                 
                    <td valign="top" width="50%">

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="toggleisenabled">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <%if (sysadminUserDetail.getIsenabled()){%>
                                    <font class="mediumfont">This Account is Currently Enabled.</font>
                                    <br/>
                                    <input type="submit" class="formsubmitbutton" value="Disable Account">
                                <%} else {%>
                                    <font class="mediumfont">This Account is Currently Disabled.</font>
                                    <br/>
                                    <input type="submit" class="formsubmitbutton" value="Enable Account">
                                <%}%>
                            </form>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="giveusermoney">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <font class="mediumfont">Give User Money</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to give:</font>
                                <br/>
                                <%=Textbox.getHtml("amt", String.valueOf(sysadminUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <%=Dropdown.getHtml("fundstype", String.valueOf(sysadminUserDetail.getFundstype()), StaticVariables.getFundsTypes(), "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <font class="tinyfont">(user will see)</font>
                                <br/>
                                <%=Textbox.getHtml("reason", sysadminUserDetail.getReason(), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Give User Money">
                            </form>
                        </div>


                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="takeusermoney">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <font class="mediumfont">Take Money from User</font>
                                <br/>
                                <font class="formfieldnamefont">Amount to take:</font>
                                <br/>
                                <%=Textbox.getHtml("amt", String.valueOf(sysadminUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <%=Dropdown.getHtml("fundstype", String.valueOf(sysadminUserDetail.getFundstype()), StaticVariables.getFundsTypes(), "", "")%>
                                <br/>
                                <font class="formfieldnamefont">Detailed Reason:</font>
                                <font class="tinyfont">(user will see)</font>
                                <br/>
                                <%=Textbox.getHtml("reason", sysadminUserDetail.getReason(), 255, 25, "", "")%>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Take User Money">
                            </form>
                        </div>

                        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                            <form action="/sysadmin/userdetail.jsp" method="post">
                                <input type="hidden" name="dpage" value="/sysadmin/userdetail.jsp">
                                <input type="hidden" name="action" value="updateresellerpercent">
                                <input type="hidden" name="userid" value="<%=sysadminUserDetail.getUserid()%>">
                                <font class="mediumfont">Reseller Percent</font>
                                <br/>
                                <font class="formfieldnamefont">Percent this person earns from reselling:</font>
                                <br/>
                                <%=Textbox.getHtml("resellerpercent", String.valueOf(sysadminUserDetail.getAmt()), 255, 25, "", "")%>
                                <br/>
                                <font class="tinyfont">Leave at 0 to use default value of <%=SurveyMoneyStatus.RESELLERPERCENTDEFAULT%></font>
                                <br/>
                                <input type="submit" class="formsubmitbutton" value="Update">
                            </form>
                        </div>
                   
                    </td>
                </tr>
            </table>

        <% if (sysadminUserDetail.getResearcher()!=null && sysadminUserDetail.getResearcher().getResearcherid()>0){ %>
            <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
                <font class="mediumfont">Researcher Most Recent Financial Stats (Not Accurate)</font>
                <br/>
                <font class="tinyfont">(Calculated in ResearcherRemainingBalanceOperations.java)</font>
                <br/><br/>
                <font class="smallfont">Max Possible Spend: $<%=sysadminUserDetail.getResearcher().getNotaccuratemaxpossspend()%></font>
                <br/>
                <font class="smallfont">Max Remaining Spend: $<%=sysadminUserDetail.getResearcher().getNotaccurateremainingpossspend()%></font>
                <br/>
                <font class="smallfont">Current Balance: $<%=sysadminUserDetail.getResearcher().getNotaccuratecurrbalance()%></font>
                <br/>
                <font class="smallfont">Percent of Max: <%=sysadminUserDetail.getResearcher().getNotaccuratepercentofmax()%> percent</font>
                <br/>
                <font class="smallfont">Amt To Charge: $<%=sysadminUserDetail.getResearcher().getNotaccurateamttocharge()%></font>
                <br/>
            </div>
        <% } %>

        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Account Balance (Internal Account Money Movement)</font>
            <br/>
            <a href="/sysadmin/userdetail.jsp?userid=<%=sysadminUserDetail.getUser().getUserid()%>&onlyshownegativeamountbalance=1"><font class="tinyfont">Only Show Negative Amts</font></a>
            <br/>
            <%if (sysadminUserDetail.getBalances()==null || sysadminUserDetail.getBalances().size()==0){%>
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
                <%=Grid.render(sysadminUserDetail.getBalances(), cols, 50, "/sysadmin/userdetail.jsp?userid="+sysadminUserDetail.getUser().getUserid()+"&onlyshownegativeamountbalance="+request.getParameter("onlyshownegativeamountbalance"), "pagetransactions")%>
            <%}%>
        </div>


        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Account Transactions (Real World Money Movement)</font>
            <br/>
            <a href="/sysadmin/userdetail.jsp?userid=<%=sysadminUserDetail.getUser().getUserid()%>&onlyshowsuccessfultransactions=1"><font class="tinyfont">Only Show Successful Transactions</font></a>
            <br/>
            <%if (sysadminUserDetail.getTransactions()==null || sysadminUserDetail.getTransactions().size()==0){%>
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
                <%=Grid.render(sysadminUserDetail.getTransactions(), cols, 50, "/sysadmin/userdetail.jsp?userid="+sysadminUserDetail.getUser().getUserid()+"&onlyshowsuccessfultransactions="+request.getParameter("onlyshowsuccessfultransactions"), "pagetransactions")%>
            <%}%>
        </div>

        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Impressions</font>
            <br/>
            <%if (sysadminUserDetail.getImpressions()==null || sysadminUserDetail.getImpressions().size()==0){%>
                <font class="normalfont">There are not yet any impressions.</font>
            <%} else {%>
                <%
                    ArrayList<GridCol> cols=new ArrayList<GridCol>();
                    cols.add(new GridCol("Id", "<$impressionid$>", true, "", "tinyfont"));
                    cols.add(new GridCol("Conversation", "<$surveyid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Response", "<$responseid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Date", "<$firstseen|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", true, "", "tinyfont", "", "background: #e6e6e6;"));
                    cols.add(new GridCol("Total Impr", "<$impressionstotal$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Impr To Be Paid", "<$impressionstobepaid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Impr Paid", "<$impressionspaid$>", false, "", "tinyfont"));
                    cols.add(new GridCol("Impr By Day", "<textarea rows=\"1\" cols=\"30\" style=\"font-size: 9px; border 0px solid #cccccc;\"><$impressionsbyday$></textarea>", false, "", "tinyfont"));
                    cols.add(new GridCol("Referer", "<textarea rows=\"1\" cols=\"14\" style=\"font-size: 9px; border 0px solid #cccccc;\"><$referer$></textarea>", false, "", "tinyfont"));
                %>
                <%=Grid.render(sysadminUserDetail.getImpressions(), cols, 50, "/sysadmin/userdetail.jsp?userid="+sysadminUserDetail.getUser().getUserid(), "pageimpressions")%>
            <%}%>
        </div>

        <div class="rounded" style="padding: 15px; margin: 5px; background: #BFFFBF;">
            <font class="mediumfont">Completed Surveys</font>
            <br/>
                <%
                    for (Iterator<BloggerCompletedsurveysListitem> iterator=sysadminUserDetail.getResponses().iterator(); iterator.hasNext();){
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


