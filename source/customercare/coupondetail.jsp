<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.CustomercareSupportIssueDetail" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dneero.util.Str" %>
<%@ page import="com.dneero.util.Time" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="com.dneero.htmlui.*" %>
<%@ page import="com.dneero.htmluibeans.CustomercareCouponDetail" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Coupon Detail";
String navtab = "customercare";
String acl = "customercare";
%>
<%@ include file="/template/auth.jsp" %>
<%
    CustomercareCouponDetail customercareCouponDetail= (CustomercareCouponDetail) Pagez.getBeanMgr().get("CustomercareCouponDetail");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        try {
            customercareCouponDetail.getCoupon().setName(Textbox.getValueFromRequest("name", "Name", true, DatatypeString.DATATYPEID));
            customercareCouponDetail.getCoupon().setDescription(Textarea.getValueFromRequest("notes", "Notes", true));
            customercareCouponDetail.getCoupon().setCouponcode(Textbox.getValueFromRequest("couponcode", "Coupon Code", true, DatatypeString.DATATYPEID));
            customercareCouponDetail.getCoupon().setStartdate(DateTime.getValueFromRequest("startdate", "Start Date", true).getTime());
            customercareCouponDetail.getCoupon().setEnddate(DateTime.getValueFromRequest("enddate", "End Date", true).getTime());
            customercareCouponDetail.getCoupon().setDiscountpercent(Textbox.getDblFromRequest("discountpercent", "Discount Percent", true, DatatypeDouble.DATATYPEID));
            customercareCouponDetail.getCoupon().setCommaseplistofuserids(Textarea.getValueFromRequest("commaseplistofuserids", "Comma Sep List of Userids", false));
            customercareCouponDetail.getCoupon().setTimescanberedeemed(Textbox.getIntFromRequest("timescanberedeemed", "Times Can Be Redeemed", false, DatatypeInteger.DATATYPEID));
            customercareCouponDetail.save();
            Pagez.sendRedirect("/customercare/couponlist.jsp");
            return;
        } catch (com.dneero.htmlui.ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>








        <form action="/customercare/coupondetail.jsp" method="post" class="niceform">
            <input type="hidden" name="dpage" value="/customercare/coupondetail.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="couponid" value="<%=customercareCouponDetail.getCoupon().getCouponid()%>">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Coupon Name</font>
                        <br/>
                        <font class="tinyfont">User will see this name.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("name", customercareCouponDetail.getCoupon().getName(), 255, 25, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Description</font>
                        <br/>
                        <font class="tinyfont">User will see this description.</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("notes", customercareCouponDetail.getCoupon().getDescription(), 4, 50, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Coupon Code</font>
                        <br/>
                        <font class="tinyfont">What a person will type in to redeem.  Will be converted to all UPPERCASE automatically.  Don't use spaces or funky characters.  Letters and numbers is it.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("couponcode", customercareCouponDetail.getCoupon().getCouponcode(), 25, 15, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Discount Percent</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("discountpercent", String.valueOf(customercareCouponDetail.getCoupon().getDiscountpercent()), 255, 5, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Times Can Be Redeemed</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("timescanberedeemed", String.valueOf(customercareCouponDetail.getCoupon().getTimescanberedeemed()), 255, 5, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Comma Separated List of Userids</font>
                        <br/>
                        <font class="tinyfont">List of userids this is valid for. Leave blank to have it work for anybody.  ex: 453,3457,32,8765</font>
                    </td>
                    <td valign="top">
                        <%=Textarea.getHtml("commaseplistofuserids", customercareCouponDetail.getCoupon().getCommaseplistofuserids(), 3, 50, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Start Date</font>
                    </td>
                    <td valign="top">
                        <%=DateTime.getHtml("startdate", customercareCouponDetail.getCoupon().getStartdate(), "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">End Date</font>
                    </td>
                    <td valign="top">
                        <%=DateTime.getHtml("enddate", customercareCouponDetail.getCoupon().getEnddate(), "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton" value="Save Coupon">
                    </td>
                </tr>

            </table>

        </form>





<%@ include file="/template/footer.jsp" %>



