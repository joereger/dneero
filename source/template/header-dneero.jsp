<%@ page import="com.dneero.htmlui.Pagez" %>
<%@ page import="com.dneero.systemprops.BaseUrl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1" />
  <title>dNeero Social Surveys</title>
  <link rel="stylesheet" type="text/css" href="/css/basic.css" />
  <link rel="stylesheet" type="text/css" href="/css/dneero.css" />
  <link rel="stylesheet" type="text/css" href="/js/niftycube/niftyCorners.css" />
  <link rel="alternate" type="application/rss+xml" title="dNeero Social Surveys Blog" href="http://www.dneero.com/rss.xml" />
  <meta name="description" content="dNeero connects advertisers and market researchers with a large pool of qualified and motivated bloggers. Marketers initiate offers to blog (i.e. We'll pay $2 for a blogged review of our latest movie.), dNeero publishes those offers in a Yahoo-style directory and bloggers earn money (for themselves or for charity) taking surveys and posting their own results to their blogs. " />
  <meta name="keywords" content="market research,survey,blog,web 2.0,startup,money,market intelligence" />
  <script type="text/javascript">
    <%if (Pagez.getUserSession().getIsfacebookui()){%>
    var userSessionIsFacebookui = true;
    <%}%>
    <%if (!com.dneero.htmlui.Pagez.getUserSession().getIsfacebookui()){%>
    var userSessionIsFacebookui = false;
    <%}%>
    if (userSessionIsFacebookui) {
        if (window==top){
            top.location.href="<%=BaseUrl.get(true)%>facebookexitui.jsp";
        }
    } else {
        if (window!=top){
            //alert("Your session may have timed out.  If you're using the Facebook App please click your left-hand menu link to restart your session.  Thanks.");
            top.location.href="<%=BaseUrl.get(true)%>facebookenterui.jsp";
        }
    }
  </script>
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
            <td valign="top" style="text-align: right;" colspan="4">
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
                <%if (!navtab.equals("bloggers")){%><img src="/images/navtabs2/bloggers_off.gif" width="138" height="70" border="0"/><%}%>
                <%if (navtab.equals("bloggers")){%><img src="/images/navtabs2/bloggers_on.gif" width="138" height="70" border="0"/><%}%>
                </a>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                <a href="/researcher/index.jsp">
                <%if (!navtab.equals("researchers")){%><img src="/images/navtabs2/researchers_off.gif" width="138" height="70" border="0"/><%}%>
                <%if (navtab.equals("researchers")){%><img src="/images/navtabs2/researchers_on.gif" width="138" height="70" border="0"/><%}%>
                </a>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                <%if (!navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/index.jsp"><img src="/images/navtabs2/youraccount_off.gif" alt="" width="138" height="70" border="0"/></a><%}%>
                <%if (navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><a href="/account/index.jsp"><img src="/images/navtabs2/youraccount_on.gif" alt="" width="138" height="70" border="0"/></a><%}%>
                <%if (!navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><img src="/images/navtabs2/youraccount_off.gif" alt="" width="138" height="70" border="0"/></a><%}%>
                <%if (navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><img src="/images/navtabs2/youraccount_on.gif" alt="" width="138" height="70" border="0"/></a><%}%>
            </td>
            <td valign="top" background="/images/navtabs2/mainbar_bg.gif">
                 <%if (Pagez.getUserSession().getIsSysadmin()){%>
                    <a href="/sysadmin/index.jsp">
                    <%if (!navtab.equals("sysadmin")){%><img src="/images/navtabs2/sysadmin_off.gif" width="138" height="70" border="0"/><%}%>
                    <%if (navtab.equals("sysadmin")){%><img src="/images/navtabs2/sysadmin_on.gif" width="138" height="70" border="0"/><%}%>
                    </a>
                <%}%>
            </td>
        </tr>
        <tr>
            <td bgcolor="#dadada" style="text-align: left; vertical-align: middle;" colspan="6" height="25">
                <%if (navtab.equals("home")){%>
                    <a href="/publicsurveylist.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Current Surveys</font></a>
                    <a href="/publicoldsurveylist.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Old Surveys</font></a>
                    <a href="/blog.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">The dNeero Blog</font></a>
                    <a href="/charity.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Charity</font></a>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Sign Up</font></a><%}%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%><a href="/login.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Log In</font></a><%}%>
                <%}%>
                <%if (navtab.equals("bloggers")){%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getBloggerid()>0){%>
                        <a href="/publicsurveylist.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Find Surveys</font></a>
                        <a href="/blogger/bloggerearningsrevshare.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Earn Even More</font></a>
                        <a href="/blogger/index.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Earnings from Completed Surveys</font></a>
                        <a href="/blogger/bloggerdetails.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Blogger Profile</font></a>
                    <%}%>
                    <a href="/blogger/bloggerfaq.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Blogger FAQ</font></a>
                    <a href="/blogger/index.jsp?showmarketingmaterial=1"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Blogger Basics</font></a>
                <%}%>
                <%if (navtab.equals("researchers")){%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser().getResearcherid()>0){%>
                        <a href="/researcher/index.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">My Surveys</font></a>
                        <a href="/researcher/panels.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Panels</font></a>
                        <a href="/researcher/researcherdetails.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Researcher Details</font></a>
                        <a href="/researcher/researcherbilling.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Billing</font></a>
                    <%}%>
                    <a href="/researcher/researcherfaq.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Researcher FAQ</font></a>
                    <a href="/researcher/index.jsp?showmarketingmaterial=1"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Researcher Basics</font></a>
                <%}%>
                <%if (navtab.equals("youraccount")){%>
                    <%if (Pagez.getUserSession().getIsloggedin()){%>
                        <a href="/account/accountbalance.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Account Balance</font></a>
                        <a href="/account/accountsettings.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Account Settings</font></a>
                        <a href="/account/changepassword.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Change Password</font></a>
                        <a href="/account/accountsupportissueslist.jsp"><font class="subnavfont" style="padding-left: 15px; color: #000000;">Help/Support</font></a>
                    <%}%>
                <%}%>
                <%if (navtab.equals("sysadmin")){%>
                    <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIsSysadmin()){%>
                        <a href="/sysadmin/errorlist.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">SysLog</font></a>
                        <a href="/sysadmin/transactions.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Trans</font></a>
                        <a href="/sysadmin/userlist.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Users</font></a>
                        <a href="/sysadmin/sysadminsurveylist.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Surveys</font></a>
                        <a href="/sysadmin/editeula.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">EULA</font></a>
                        <a href="/sysadmin/sysadminsupportissueslist.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Support</font></a>
                        <a href="/sysadmin/rateblogpost.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Rate</font></a>
                        <a href="/sysadmin/manuallyrunscheduledtask.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Scheduled</font></a>
                        <a href="/sysadmin/systemprops.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">SysProps</font></a>
                        <a href="/sysadmin/instanceprops.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">InstanceProps</font></a>
                        <a href="/sysadmin/hibernatecache.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Cache</font></a>
                        <a href="/sysadmin/massemaillist.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">MassEmail</font></a>
                        <a href="/sysadmin/blogpost.jsp"><font class="subnavfont" style="padding-left: 10px; color: #000000;">Blog</font></a>
                    <%}%>
                <%}%>
            </td>
        </tr>
        <tr>
            <td background="/images/navtabs2/linedots.gif" colspan="6"><img src="/images/clear.gif" width="1" height="1"/></td>
        </tr>
    </table>
    <br/>


    <table width="775" cellspacing="0" border="0" cellpadding="20">
        <tr>
            <td valign="top">
                <font class="pagetitlefont"><%=pagetitle%></font>
                <br/>
                <%
                if (Pagez.getUserSession().getMessage()!=null && !Pagez.getUserSession().getMessage().equals("")){
                    %>
                    <br/>
                    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="formfieldfont"><%=Pagez.getUserSession().getMessage()%></font></div></center>
                    <br/><br/>
                    <%
                    //Clear the message since it's been displayed
                    Pagez.getUserSession().setMessage("");
                }
                %>
                <!-- Begin Body -->