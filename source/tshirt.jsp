<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dneero.htmluibeans.PublicTshirt" %>
<%@ page import="com.dneero.systemprops.SystemProperty" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Order a Signed dNeero T-Shirt";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
    PublicTshirt publicTshirt = (PublicTshirt) Pagez.getBeanMgr().get("PublicTshirt");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("buy")) {
        try {
            publicTshirt.buy();
            if (!publicTshirt.getToken().equals("")) {
                if (SystemProperty.getProp(SystemProperty.PROP_PAYPALENVIRONMENT).trim().equalsIgnoreCase("live")) {
                    Pagez.sendRedirect("https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + publicTshirt.getToken());
                    return;
                } else {
                    Pagez.sendRedirect("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + publicTshirt.getToken());
                    return;
                }
            }
        } catch(Exception ex){
                logger.error(ex);
                Pagez.getUserSession().setMessage("Sorry, there has been an error... please try again.");
        }
    }
%>
<%@ include file="/template/header.jsp" %>

<br/><br/>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr>
        <td valign="top">
            <img src="/images/t-shirt.jpg" alt="dNeero T-Shirt" border="0"/>
        </td>
        <td valign="top">
            <div class="rounded" style="background: #e6e6e6; padding: 10px;">
                <font style="font-size: 25px; color: #666666;"><b>Only $350</b></font>
                <br/>
                <font class="smallfont">Organic cotton dNeero.com t-shirt, personally signed by Joe Reger, Jr.  Lightweight for summer comfort or winter layering, our Men's Organic T-shirt is one of our most popular fine tees. It's what to wear when you care about having 100% organically-grown cotton next to your skin. The next best thing to nothing, naturally. 4.8oz Ultra fine combed ring spun organic.</font><br/>
                <font style="font-size: 9px;"><b>(supplies are limited)</b></font>
                <br/><br/>
                <div class="rounded" style="background: #ffffff; padding: 10px;">
                    <center>
                        <a href="/tshirt.jsp?action=buy"><img src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" border="0"></a>
                    </center>
                </div>
            </div>
        </td>
    </tr>
</table>






<%@ include file="/template/footer.jsp" %>