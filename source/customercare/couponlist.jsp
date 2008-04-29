<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareUserList" %>
<%@ page import="com.dneero.dbgrid.GridCol" %>
<%@ page import="com.dneero.dbgrid.Grid" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.CustomercareCouponList" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Coupons";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareCouponList customercareCouponListList= (CustomercareCouponList) Pagez.getBeanMgr().get("CustomercareCouponList");
%>
<%@ include file="/template/header.jsp" %>




        <table cellpadding='3' cellspacing='3' border='0'>
            <tr>
                <td valign="top">
                    <font class="mediumfont">Active Coupons (<a href="/customercare/coupondetail.jsp">new</a>)</font><br/><br/>
                    <%if (customercareCouponListList.getActivecoupons()==null || customercareCouponListList.getActivecoupons().size()==0){%>
                        <font class="normalfont">No coupons found dude.</font>
                    <%} else {%>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("", "<a href=\"/customercare/coupondetail.jsp?couponid=<$couponid$>\"><$name$></a>", false, "", "tinyfont"));
                            cols.add(new GridCol("Code", "<$couponcode$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Discount Percent", "<$discountpercent$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Times", "<$timescanberedeemed$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Start/End Date", "<$startdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$><br/><$enddate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                        %>
                        <%=Grid.render(customercareCouponListList.getActivecoupons(), cols, 50, "/customercare/couponlist.jsp", "page3")%>
                    <%}%>
                    <br/><br/>
                    <font class="mediumfont">Upcoming Coupons</font><br/><br/>
                    <%if (customercareCouponListList.getUpcomingcoupons()==null || customercareCouponListList.getUpcomingcoupons().size()==0){%>
                        <font class="normalfont">No upcoming coupons found dude.</font>
                    <%} else {%>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("", "<a href=\"/customercare/coupondetail.jsp?couponid=<$couponid$>\"><$name$></a>", false, "", "tinyfont"));
                            cols.add(new GridCol("Code", "<$couponcode$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Discount Percent", "<$discountpercent$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Times", "<$timescanberedeemed$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Start/End Date", "<$startdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$><br/><$enddate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                        %>
                        <%=Grid.render(customercareCouponListList.getUpcomingcoupons(), cols, 50, "/customercare/couponlist.jsp", "page4")%>
                    <%}%>
                    <br/><br/>
                    <font class="mediumfont">All Coupons</font><br/><br/>
                    <%if (customercareCouponListList.getCoupons()==null || customercareCouponListList.getCoupons().size()==0){%>
                        <font class="normalfont">No coupons found dude.</font>
                    <%} else {%>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("", "<a href=\"/customercare/coupondetail.jsp?couponid=<$couponid$>\"><$name$></a>", false, "", "tinyfont"));
                            cols.add(new GridCol("Code", "<$couponcode$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Discount Percent", "<$discountpercent$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Times", "<$timescanberedeemed$>", false, "", "tinyfont"));
                            cols.add(new GridCol("Start/End Date", "<$startdate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$><br/><$enddate|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                        %>
                        <%=Grid.render(customercareCouponListList.getCoupons(), cols, 200, "/customercare/couponlist.jsp", "page")%>
                    <%}%>
                </td>
                <td valign="top">
                    <font class="mediumfont">Redemptions</font><br/><br/>
                    <%if (customercareCouponListList.getCouponredemptions()==null || customercareCouponListList.getCouponredemptions().size()==0){%>
                        <font class="normalfont">No coupon redemptions found dude.</font>
                    <%} else {%>
                        <%
                            ArrayList<GridCol> cols=new ArrayList<GridCol>();
                            cols.add(new GridCol("Userid", "<a href=\"/customercare/userdetail.jsp?userid=<$userid$>\"><$userid$></a>", false, "", "tinyfont"));
                            cols.add(new GridCol("Couponid", "<a href=\"/customercare/coupondetail.jsp?couponid=<$couponid$>\"><$couponid$></a>", false, "", "tinyfont"));
                            cols.add(new GridCol("Surveyid", "<a href=\"/customercare/sysadminsurveydetail.jsp?surveyid=<$surveyid$>\"><$surveyid$></a>", false, "", "tinyfont"));
                            cols.add(new GridCol("Date", "<$date|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>", false, "", "tinyfont"));
                        %>
                        <%=Grid.render(customercareCouponListList.getCouponredemptions(), cols, 200, "/customercare/couponlist.jsp", "page2")%>
                    <%}%>
                </td>
            </tr>
        </table>

       



<%@ include file="/template/footer.jsp" %>



