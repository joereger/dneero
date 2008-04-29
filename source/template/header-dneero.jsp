<%@ page import="com.dneero.systemprops.BaseUrl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1" />
  <title>dNeero: True Social Network Currency</title>
  <link rel="stylesheet" type="text/css" href="/css/basic.css" />
  <link rel="stylesheet" type="text/css" href="/css/dneero.css" />
  <link rel="stylesheet" type="text/css" href="/js/niftycube/niftyCorners.css" />
  <link rel="alternate" type="application/rss+xml" title="dNeero Conversations Blog" href="http://www.dneero.com/rss.xml" />
  <meta name="description" content="dNeero connects advertisers and market researchers with a large pool of qualified and motivated bloggers. Marketers initiate offers to blog (i.e. We'll pay $2 for a blogged review of our latest movie.), dNeero publishes those offers in a Yahoo-style directory and social people earn money (for themselves or for charity) joining conversations and posting their own results to their blogs. " />
  <meta name="keywords" content="conversation,market research,survey,blog,web 2.0,startup,money,market intelligence" />
  <script type="text/javascript" src="/js/niftycube/niftycube.js"></script>
  <script type="text/javascript">
   NiftyLoad=function(){
       Nifty("div.rounded","big");
   }
  </script>
</head>
<body>
          
    <table width="786" cellspacing="0" border="0" cellpadding="0">
        <tr>
            <td valign="top" colspan="2">
                <img src="/images/clear.gif" width="10" height="1" align="left"/>
                <a href="/"><img src="/images/dneero-logo.gif" width="200" height="92" border="0"/></a>
            </td>
            <td valign="top" style="text-align: right;" colspan="5">
                <%if (!Pagez.getUserSession().getIsloggedin()){%>
                    <div style="padding: 10px; text-align: right;">
                    <font class="subnavfont">Already have an account?<img src="/images/clear.gif" width="20" height="1"/><a href="/login.jsp">Log In</a></font>
                    <br/>
                    <font class="subnavfont">Want to get one?<img src="/images/clear.gif" width="20" height="1"/><a href="/registration.jsp">Sign Up</a></font>
                    </div>
                <%}%>
                <%if (Pagez.getUserSession().getIsloggedin()){%>
                    <div style="padding: 10px; text-align: right;">
                        <font class="subnavfont">Hi, <%=Pagez.getUserSession().getUser().getFirstname()%> <%=Pagez.getUserSession().getUser().getLastname()%>! <a href="/login.jsp?action=logout">Log Out</a></font>
                        <br/>
                        <font class="subnavfont">Need <a href="/account/accountsupportissueslist.jsp">Help?</a></font>
                    </div>
                <%}%>
            </td>
        </tr>
        <tr>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif" width="234">
                <img src="/images/clear.gif" width="1" height="85"/>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                <a href="/index.jsp">
                <%if (!navtab.equals("home")){%><img src="/images/navtabs2/home_off.gif" width="138" height="70" border="0"/><%}%>
                <%if (navtab.equals("home")){%><img src="/images/navtabs2/home_on.gif" width="138" height="70" border="0"/><%}%>
                </a>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                <a href="/blogger/index.jsp">
                <%if (!navtab.equals("bloggers")){%><img src="/images/navtabs2/socialpeople_off.gif" width="138" height="70" border="0"/><%}%>
                <%if (navtab.equals("bloggers")){%><img src="/images/navtabs2/socialpeople_on.gif" width="138" height="70" border="0"/><%}%>
                </a>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                <a href="/researcher/index.jsp">
                <%if (!navtab.equals("researchers")){%><img src="/images/navtabs2/conversationigniters_off.gif" width="138" height="70" border="0"/><%}%>
                <%if (navtab.equals("researchers")){%><img src="/images/navtabs2/conversationigniters_on.gif" width="138" height="70" border="0"/><%}%>
                </a>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                <%if (!navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/index.jsp"><img src="/images/navtabs2/youraccount_off.gif" alt="" width="138" height="70" border="0"/></a><%}%>
                <%if (navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/index.jsp"><img src="/images/navtabs2/youraccount_on.gif" alt="" width="138" height="70" border="0"/></a><%}%>
                <%if (!navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><img src="/images/navtabs2/youraccount_off.gif" alt="" width="138" height="70" border="0"/></a><%}%>
                <%if (navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><img src="/images/navtabs2/youraccount_on.gif" alt="" width="138" height="70" border="0"/></a><%}%>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                 <%if (Pagez.getUserSession().getIsCustomerCare()){%>
                    <a href="/customercare/index.jsp">
                    <%if (!navtab.equals("customercare")){%><img src="/images/navtabs2/customercare_off.gif" width="138" height="70" border="0"/><%}%>
                    <%if (navtab.equals("customercare")){%><img src="/images/navtabs2/customercare_on.gif" width="138" height="70" border="0"/><%}%>
                    </a>
                <%}%>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                 <%if (Pagez.getUserSession().getIsSysadmin()){%>
                    <a href="/sysadmin/index.jsp">
                    <%if (!navtab.equals("sysadmin")){%><img src="/images/navtabs2/sysadmin_off.gif" width="66" height="70" border="0"/><%}%>
                    <%if (navtab.equals("sysadmin")){%><img src="/images/navtabs2/sysadmin_on.gif" width="66" height="70" border="0"/><%}%>
                    </a>
                <%}%>
            </td>
        </tr>
        <tr>
            <td bgcolor="#dadada" style="text-align: left; vertical-align: middle;" colspan="7" height="25">
                <%if (navtab.equals("home")){%>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/publicsurveylist.jsp"><font class="subnavfont" style="color: #000000;">Active Conversations</font></a>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/publicoldsurveylist.jsp"><font class="subnavfont" style="color: #000000;">Old Conversations</font></a>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/blog.jsp"><font class="subnavfont" style="color: #000000;">The dNeero Blog</font></a>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/charity.jsp"><font class="subnavfont" style="color: #000000;">Charity</font></a>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/reseller.jsp"><font class="subnavfont" style="color: #000000;">Reseller Program</font></a>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <%if (!Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><font class="subnavfont" style="color: #000000;">Sign Up</font></a><%}%>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <%if (!Pagez.getUserSession().getIsloggedin()){%><a href="/login.jsp"><font class="subnavfont" style="color: #000000;">Log In</font></a><%}%>
                <%}%>
                <%if (navtab.equals("bloggers")){%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){%>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/publicsurveylist.jsp"><font class="subnavfont" style="color: #000000;">Find Conversations</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/blogger/bloggerearningsrevshare.jsp"><font class="subnavfont" style="color: #000000;">Referral Program</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/blogger/index.jsp"><font class="subnavfont" style="color: #000000;">Earnings from Completed</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/blogger/bloggerdetails.jsp"><font class="subnavfont" style="color: #000000;">Profile</font></a>
                    <%}%>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/blogger/bloggerfaq.jsp"><font class="subnavfont" style="color: #000000;">Social People FAQ</font></a>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/blogger/index.jsp?showmarketingmaterial=1"><font class="subnavfont" style="color: #000000;">Social People Basics</font></a>
                <%}%>
                <%if (navtab.equals("researchers")){%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getResearcherid()>0){%>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/researcher/index.jsp"><font class="subnavfont" style="color: #000000;">My Conversations</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/researcher/panels.jsp"><font class="subnavfont" style="color: #000000;">Panels</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/researcher/rank-list.jsp"><font class="subnavfont" style="color: #000000;">Rankings</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/researcher/researcherdetails.jsp"><font class="subnavfont" style="color: #000000;">Igniter Details</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/researcher/researcherbilling.jsp"><font class="subnavfont" style="color: #000000;">Billing</font></a>
                    <%}%>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/researcher/researcherfaq.jsp"><font class="subnavfont" style="color: #000000;">Igniter FAQ</font></a>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/researcher/index.jsp?showmarketingmaterial=1"><font class="subnavfont" style="color: #000000;">Igniter Basics</font></a>
                <%}%>
                <%if (navtab.equals("youraccount")){%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/account/accountbalance.jsp"><font class="subnavfont" style="color: #000000;">Account Balance</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/account/accountsettings.jsp"><font class="subnavfont" style="color: #000000;">Account Settings</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/account/changepassword.jsp"><font class="subnavfont" style="color: #000000;">Change Password</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/account/reseller.jsp"><font class="subnavfont" style="color: #000000;">Reseller Program</font></a>
                        <img src="/images/clear.gif" alt="" width="10" height="1"/>
                        <a href="/account/accountsupportissueslist.jsp"><font class="subnavfont" style="color: #000000;">Help/Support</font></a>
                    <%}%>
                <%}%>
                <%if (navtab.equals("customercare")){%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIsCustomerCare()){%>
                        <a href="/customercare/sysadminsupportissueslist.jsp"><font class="subnavfont" style=" color: #000000;">Support Issues</font></a>                      
                        <a href="/customercare/userlist.jsp"><font class="subnavfont" style=" color: #000000;">Users</font></a>
                        <a href="/customercare/sysadminsurveylist.jsp"><font class="subnavfont" style=" color: #000000;">Conversations</font></a>
                        <a href="/customercare/charityreport.jsp"><font class="subnavfont" style=" color: #000000;">Charity</font></a>
                        <a href="/customercare/couponlist.jsp"><font class="subnavfont" style=" color: #000000;">Coupons</font></a>
                        <a href="/customercare/transactions.jsp"><font class="subnavfont" style=" color: #000000;">Real-World Transactions</font></a>
                        <a href="/customercare/balance.jsp"><font class="subnavfont" style=" color: #000000;">Balance Updates</font></a>
                    <%}%>
                <%}%>
                <%if (navtab.equals("sysadmin")){%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIsSysadmin()){%>
                        <a href="/sysadmin/errorlist.jsp"><font class="subnavfont" style=" color: #000000;">Log</font></a>
                        <a href="/sysadmin/editeula.jsp"><font class="subnavfont" style=" color: #000000;">Eula</font></a>
                        <a href="/sysadmin/rateblogpost.jsp"><font class="subnavfont" style=" color: #000000;">Rate</font></a>
                        <a href="/sysadmin/manuallyrunscheduledtask.jsp"><font class="subnavfont" style=" color: #000000;">Scheds</font></a>
                        <a href="/sysadmin/systemprops.jsp"><font class="subnavfont" style=" color: #000000;">SysProps</font></a>
                        <a href="/sysadmin/instanceprops.jsp"><font class="subnavfont" style=" color: #000000;">InsProps</font></a>
                        <a href="/sysadmin/hibernatecache.jsp"><font class="subnavfont" style=" color: #000000;">Cache</font></a>
                        <a href="/sysadmin/massemaillist.jsp"><font class="subnavfont" style=" color: #000000;">Email</font></a>
                        <a href="/sysadmin/demographicreport.jsp"><font class="subnavfont" style=" color: #000000;">Demogrs</font></a>
                        <a href="/sysadmin/pageperformance.jsp"><font class="subnavfont" style=" color: #000000;">Perf</font></a>
                        <a href="/sysadmin/blogpost.jsp"><font class="subnavfont" style=" color: #000000;">Blog</font></a>
                    <%}%>
                <%}%>
            </td>
        </tr>
        <tr>
            <td background="/images/navtabs2/linedots.gif" colspan="7"><img src="/images/clear.gif" width="1" height="1"/></td>
        </tr>
    </table>
    <br/>


    <table width="775" cellspacing="0" border="0" cellpadding="20">
        <tr>
            <td valign="top">
                <%if (pagetitle!=null && !pagetitle.equals("")){%>
                    <font class="pagetitlefont"><%=pagetitle%></font>
                    <br/>
                <%}%>
                <%
                logger.debug("Pagez.getUserSession().getMessage()="+Pagez.getUserSession().getMessage());
                if (Pagez.getUserSession().getMessage()!=null && !Pagez.getUserSession().getMessage().equals("")){
                    %>
                    <br/>
                    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="formfieldnamefont"><%=Pagez.getUserSession().getMessage()%></font></div></center>
                    <br/><br/>
                    <%
                    //Clear the message since it's been displayed
                    Pagez.getUserSession().setMessage("");
                }
                %>
                <!-- Begin Body -->